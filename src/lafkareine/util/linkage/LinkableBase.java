package lafkareine.util.linkage;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public abstract class LinkableBase {

	/**
	 * 新しい規格
	 * 「キャンセルの可能性があるひずみがある」 →　負の数
	 * 「確実に再計算が(親の辺奥が確定している)必要なひずみがある」 →　正の数
	 * 「ひずみがない」　→　0
	 */
	private int mark = 1;
	private WeakReference<LinkableBase> weakref = new WeakReference<>(this);
	private static final WeakReference<LinkableBase> nullref = new WeakReference<>(null);
	private Object operation_slot;
	private LinkableBase[] inputs = BEFORE_INITIALIZING;
	private static final LinkableBase[] BEFORE_INITIALIZING = new LinkableBase[]{};
	private static final LinkableBase[] NOTHING_INPUTS = new LinkableBase[]{};
	private WeakReference<LinkableBase>[] outputs = NOTHING_OUTPUTS;
	private static final WeakReference<LinkableBase>[] NOTHING_OUTPUTS = new WeakReference[]{};

	public final boolean isReady() {
		return 0 == mark;
	}

	protected abstract void action();

	public final void RESULT_OPTION_CANCEL() {
		assert (mark != Integer.MAX_VALUE) : "the operation is called outside this::action()";
		mark = Integer.MIN_VALUE;
	}


	private enum SEND_MESSAGE {DIFFER, READY, CANCEL}

	private void send_outputs(SEND_MESSAGE message) {
		var ol = outputs;
		outputs = NOTHING_OUTPUTS;
		switch (message) {
			case DIFFER:
				s_send_differ(ol);
				break;
			case READY:
				s_send_ready(ol);
				break;
			case CANCEL:
				s_send_cancel(ol);
				break;
			default:
				throw new IllegalArgumentException();
		}
		if (outputs == NOTHING_OUTPUTS) {
			ol = optimize_output_array(ol, outputs.length);
			int index_to = 0;
			for (var e : outputs) {
				ol[index_to++] = e;
			}
		}
		outputs = ol;
	}

	private void send_differ() {
		send_outputs(SEND_MESSAGE.DIFFER);
	}

	private void send_ready() {
		send_outputs(SEND_MESSAGE.READY);
	}

	private void send_cancel() {
		send_outputs(SEND_MESSAGE.CANCEL);
	}

	private static void s_send_differ(WeakReference<LinkableBase>[] outputs) {
		for (var e : outputs) {
			var to = e.get();
			if (to != null) to.receive_differ();
		}
	}

	private static void s_send_ready(WeakReference<LinkableBase>[] outputs) {
		for (var e : outputs) {
			var to = e.get();
			if (to != null) to.receive_ready();
		}
	}

	private static void s_send_cancel(WeakReference<LinkableBase>[] outputs) {
		for (var e : outputs) {
			var to = e.get();
			if (to != null) to.receive_cancel();
		}
	}

	/**
	 * ///////
	 * /////////
	 * ////////
	 */
	private void receive_differ() {
		if (0 < mark) {
			mark++;
		} else {
			if (mark-- == 0) send_differ();
		}
	}

	private void receive_ready() {
		assert (mark != 0) : this + ":: the mark is 0, receive_ready isn't called anything";

		//もし「キャンセル可能なひずみ」が登録されているなら、キャンセル不可能なものに変更する。
		if (mark < 0) mark *= -1;
		//ひずみが全部消えたら更新を始められる
		if (--mark == 0) {
			//更新処理中のマークはInteger.MAX_VALUEで統一する。
			mark = Integer.MAX_VALUE;
			//事前の準備は完了したので更新を始める
			action();
			//リターンに何かしらのオプションが選択された可能性がある
			//ただし子の更新処理を始める前にマークを0にしておく必要があるので、オプションを他の変数に移す。
			int option = mark;
			//オプションによって処理を分岐させる。
			//オプションは、Integer.MAX_VALUE：普通の更新、Integer.MIX_VALUE:キャンセル、の二つを今のところ想定している
			//親情報の更新では好き勝手な値がやってくる
			switch (option) {
				case Integer.MAX_VALUE:
					mark = 0;
					send_ready();
					break;
				case Integer.MIN_VALUE:
					mark = 0;
					send_cancel();
					break;
				default:
					//親情報を更新されたときなんかは、ひずみの解消されていない親が設定されるかもしれないので、receive_differが発せられた状態になっているかもしれない。
					//その場合は、0になっていたときは更新処理ができるけど、それ以外ではとくに何もしない。
					if (option == 0) send_ready();
			}
		}
	}

	private void receive_cancel() {
		if (mark > 0) {
			receive_ready();
		} else {
			if (++mark == 0) send_cancel();
		}
	}


	protected final void launchUpdate(LinkableBase... new_inputs) {
		assert (!(mark == Integer.MAX_VALUE || mark == Integer.MIN_VALUE)) : "it is refresh-event now, the operation can't call in refresh-event";

		boolean before_ready = isReady();

		swapInputs(new_inputs);
		if (before_ready) {
			send_differ();
		}
		if (isReady()) {
			send_ready();
		}
	}

	protected final void launchUpdate() {
		launchUpdate(NOTHING_INPUTS);
	}

	public final void setInputsInSecretly(LinkableBase... new_inputs) {
		assert (mark != Integer.MIN_VALUE) : "the accepting was canceled, the operation cause a risk to brake integrity";
		assert (mark == Integer.MAX_VALUE) : "it is not refresh-event now, the operation can call in refresh-event only";


		weakref.clear();
		weakref = new WeakReference<>(this);

		swapInputs(new_inputs);
	}

	private void swapInputs(LinkableBase[] new_inputs) {
		for (var e : inputs) {
			e.remove_output(this.weakref);
		}
		if (new_inputs == BEFORE_INITIALIZING) {
			mark = 1;
			inputs = BEFORE_INITIALIZING;
		} else {
			mark = 0;
			var opt_inputs = optimize_inputs(new_inputs);
			for (var e : opt_inputs) {
				e.add_output(this.weakref);
				if (!e.isReady()) {
					mark += 1;
				}
			}
			inputs = opt_inputs;
		}
	}

	public final void freeze() {
		launchUpdate(BEFORE_INITIALIZING);
	}

	public static void batch(LinkableBase... list) {
		for (var e : list) {
			e.freeze();
		}
	}

	private void add_output(WeakReference<LinkableBase> addthing) {
		outputs = optimize_output_array(outputs, 1);
		outputs[0] = addthing;
	}

	private void remove_output(WeakReference<LinkableBase> remove_thing) {
		for (int i = 0; i < outputs.length; i++) {
			if (outputs[i] == remove_thing) outputs[i] = nullref;
		}
		outputs = optimize_output_array(outputs, 0);
	}

	private static WeakReference<LinkableBase>[] optimize_output_array(WeakReference<LinkableBase>[] old_array, int extra_space) {
		int retain_num = 0;
		for (var e : old_array) {
			if (e.get() != null) retain_num += 1;
		}
		if (retain_num + extra_space == old_array.length) {
			return old_array;
		} else {
			WeakReference<LinkableBase>[] new_array = new WeakReference[retain_num + extra_space];
			//ケツから詰めたほうが、空きスペースを前から探索するとき早くなる
			int index_to = new_array.length;
			for (var e : old_array) {
				if (e.get() != null) new_array[--index_to] = e;
			}
			return new_array;
		}
	}

	private static LinkableBase[] optimize_inputs(LinkableBase[] raw_inputs) {
		if(raw_inputs.length == 0)return  raw_inputs;
		int nullnum = 0;
		//nullになっている奴をはじく
		//重複があった場合は、ひとつだけを残してあと全部nullにする
		for (int i = raw_inputs.length - 1; i > 0; i--) {
			LinkableBase slot = raw_inputs[i];
			if (slot == null) {
				nullnum += 1;
			} else {
				for (int j = 0; j < i; j++) {
					if (raw_inputs[j] == slot) {
						raw_inputs[i] = null;
						nullnum += 1;
						break;
					}
				}
			}
		}
		if (nullnum == 0) {
			return raw_inputs;
		} else {
			LinkableBase[] optimized_inputs = new LinkableBase[raw_inputs.length - nullnum];
			int index_to = 0;
			for (var e : raw_inputs) {
				optimized_inputs[index_to++] = e;
			}
			return optimized_inputs;
		}
	}

	protected static LinkableBase[] inferInputs(Object worker) {
		if (worker != null) {
			Class evalclass = worker.getClass();
			try {
				Field[] fields = evalclass.getDeclaredFields();
				LinkableBase[] buffer = new LinkableBase[fields.length];
				int i = 0;
				for (Field fld : evalclass.getDeclaredFields()) {
					if (LinkableBase.class.isAssignableFrom(fld.getType())) {
						fld.setAccessible(true);
						buffer[i++] = (LinkableBase) fld.get(worker);
					}
				}

				LinkableBase[] array = new LinkableBase[i];
				for (int j = 0; j < i; j++) {
					array[j] = buffer[j];
				}
				return array;
			} catch (SecurityException e) {
				// TODO: handle exception
				e.printStackTrace();
				System.err.println("セキュリティエラー：リアクティブシステム内で依存親の自動推測を行おうとしたが、リフレクションを行えなかった");
				System.err.println("設定は行われなかった");
			} catch (IllegalArgumentException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

		}
		return null;
	}
}
