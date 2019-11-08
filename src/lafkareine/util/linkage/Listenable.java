package lafkareine.util.linkage;

import java.util.Arrays;

public abstract class Listenable<T> extends Readable<T> {


	public interface BasicListener<T> {

		void listen(T old, T latest);
	}

	public interface NoOldListener<T> extends BasicListener<T> {

		@Override
		default void listen(T old, T latest) {
			// TODO 自動生成されたメソッド・スタブ
			listen(latest);
		}

		void listen(T latest);

	}

	public interface NoArgListener<T> extends BasicListener<T> {

		@Override
		default void listen(T old, T latest) {
			// TODO 自動生成されたメソッド・スタブ
			listen();
		}

		void listen();
	}


	/**読み取れるんだからListenerが必要だろ、みたいな*/


	private BasicListener<? super T>[] listeners = NOTHING_LISTENER;

	protected final BasicListener<? super T>[] getListeners(){return  listeners;}

	private static final BasicListener[] NOTHING_LISTENER = new BasicListener[] {};


	public BasicListener<? super T> addListener(BasicListener<? super T> listener) {
		listeners = Arrays.copyOf(listeners, listeners.length + 1);
		listeners[listeners.length - 1] = listener;
		return listener;
	}

	public final BasicListener<? super T> addListener(NoArgListener<? super T> listener){
		return  addListener(listener);
	};

	public final BasicListener<? super T> addListener(NoOldListener<? super T> listener){
		return  addListener(listener);
	};



	public boolean removeListener(Object listener) {
		if (listener == null)
			return false;
		int rmvnum = 0;
		for (int i = 0; i < listeners.length; i++) {
			if (listener.equals(listeners[i]) && i + ++rmvnum < listeners.length) {
				listeners[i--] = listeners[rmvnum];
			}
		}
		if (rmvnum == 0)
			return false;
		listeners = Arrays.copyOf(listeners, listeners.length - rmvnum);
		return true;
	}

	protected void defaultRunListner(T old, T neo){
		for(var e:listeners){
			e.listen(old,neo);
		}
	}
}
