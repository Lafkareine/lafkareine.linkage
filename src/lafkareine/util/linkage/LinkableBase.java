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
	private LinkableBase[] concerns = BEFORE_INITIALIZING;
	private static final LinkableBase[] BEFORE_INITIALIZING = new LinkableBase[]{};
	private static final LinkableBase[] NOTHING_CONCERNS = new LinkableBase[]{};
	private WeakReference<LinkableBase>[] observers = NOTHING_OBSERVERS;
	private static final WeakReference<LinkableBase>[] NOTHING_OBSERVERS = new WeakReference[]{};

	public final boolean isReady() {
		return 0 == mark;
	}

	protected abstract void action();

	public final void RESULT_OPTION_CANCEL() {
		assert (mark != Integer.MAX_VALUE) : "the operation is called outside this::action()";
		mark = Integer.MIN_VALUE;
	}


	private enum NOTIFICATION {DIFFER, READY, CANCEL}

	private void send_observers(NOTIFICATION notification) {
		var ol = observers;
		observers = NOTHING_OBSERVERS;
		switch (notification) {
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
		if (observers == NOTHING_OBSERVERS) {
			ol = optimize_observers(ol, observers.length);
			int index_to = 0;
			for (var e : observers) {
				ol[index_to++] = e;
			}
		}
		observers = ol;
	}

	private void send_differ() {
		send_observers(NOTIFICATION.DIFFER);
	}

	private void send_ready() {
		send_observers(NOTIFICATION.READY);
	}

	private void send_cancel() {
		send_observers(NOTIFICATION.CANCEL);
	}

	private static void s_send_differ(WeakReference<LinkableBase>[] observers) {
		for (var e : observers) {
			var to = e.get();
			if (to != null) to.receive_differ();
		}
	}

	private static void s_send_ready(WeakReference<LinkableBase>[] observers) {
		for (var e : observers) {
			var to = e.get();
			if (to != null) to.receive_ready();
		}
	}

	private static void s_send_cancel(WeakReference<LinkableBase>[] observers) {
		for (var e : observers) {
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


	protected final void launchUpdate(LinkableBase... new_concerns) {
		assert (!(mark == Integer.MAX_VALUE || mark == Integer.MIN_VALUE)) : "it is refresh-event now, the operation can't call in refresh-event";

		boolean before_ready = isReady();

		swapConcerns(new_concerns);
		if (before_ready) {
			send_differ();
		}
		if (isReady()) {
			send_ready();
		}
	}

	protected final void launchUpdate() {
		launchUpdate(NOTHING_CONCERNS);
	}

	public final void setConcernsInSecretly(LinkableBase... new_concerns) {
		assert (mark != Integer.MIN_VALUE) : "the accepting was canceled, the operation cause a risk to brake integrity";
		assert (mark == Integer.MAX_VALUE) : "it is not refresh-event now, the operation can call in refresh-event only";


		weakref.clear();
		weakref = new WeakReference<>(this);

		swapConcerns(new_concerns);
	}

	private void swapConcerns(LinkableBase[] new_concerns) {
		for (var e : concerns) {
			e.remove_observer(this.weakref);
		}
		if (new_concerns == BEFORE_INITIALIZING) {
			mark = 1;
			concerns = BEFORE_INITIALIZING;
		} else {
			mark = 0;
			var opt_concerns = optimize_concerns(new_concerns);
			for (var e : opt_concerns) {
				e.add_observer(this.weakref);
				if (!e.isReady()) {
					mark += 1;
				}
			}
			concerns = opt_concerns;
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

	private void add_observer(WeakReference<LinkableBase> addthing) {
		observers = optimize_observers(observers, 1);
		observers[0] = addthing;
	}

	private void remove_observer(WeakReference<LinkableBase> remove_thing) {
		for (int i = 0; i < observers.length; i++) {
			if (observers[i] == remove_thing) observers[i] = nullref;
		}
		observers = optimize_observers(observers, 0);
	}

	private static WeakReference<LinkableBase>[] optimize_observers(WeakReference<LinkableBase>[] old_array, int extra_space) {
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

	private static LinkableBase[] optimize_concerns(LinkableBase[] raw_concerns) {
		if(raw_concerns.length == 0)return  raw_concerns;
		int nullnum = 0;
		//nullになっている奴をはじく
		//重複があった場合は、ひとつだけを残してあと全部nullにする
		for (int i = raw_concerns.length - 1; i > 0; i--) {
			LinkableBase slot = raw_concerns[i];
			if (slot == null) {
				nullnum += 1;
			} else {
				for (int j = 0; j < i; j++) {
					if (raw_concerns[j] == slot) {
						raw_concerns[i] = null;
						nullnum += 1;
						break;
					}
				}
			}
		}
		if (nullnum == 0) {
			return raw_concerns;
		} else {
			LinkableBase[] optimized_concerns = new LinkableBase[raw_concerns.length - nullnum];
			int index_to = 0;
			for (var e : raw_concerns) {
				optimized_concerns[index_to++] = e;
			}
			return optimized_concerns;
		}
	}

	protected static LinkableBase[] inferConcerns(Object worker) {
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
