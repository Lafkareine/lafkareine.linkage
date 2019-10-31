package lafkareine.util.linkage;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Readable<T> extends LinkableBase{

	/**このLinkableが持つ状態を読み取ります
	 * */
	public abstract T get();

	/** get()で得られるものを引数のConsumerに渡します
	 * この要素に集中した処理を行いたいときに有用です
	 * */
	public void focus(Consumer<? super T> work) {
		work.accept(get());
	}

	/** get()で得られるものを引数のFunctionに渡します
	 * この要素に集中した処理を行いたいときに有用です
	 * */
	public <U> U focus(Function<? super T, U> work) {
		return work.apply(get());
	}



	public interface BasicListener<T> {

		void listen(T old, T latest);

		default boolean requireLatest() {
			return true;
		}

		default boolean requireOld() {
			return true;
		}
	}

	public interface NoOldListener<T> extends BasicListener<T> {

		@Override
		default void listen(T old, T latest) {
			// TODO 自動生成されたメソッド・スタブ
			listen(latest);
		}

		void listen(T latest);

		@Override
		default boolean requireOld() {
			return false;
		}
	}

	public interface NoArgListener<T> extends BasicListener<T> {

		@Override
		default void listen(T old, T latest) {
			// TODO 自動生成されたメソッド・スタブ
			listen();
		}

		void listen();

		@Override
		default boolean requireLatest() {
			// TODO 自動生成されたメソッド・スタブ
			return false;
		}

		@Override
		default boolean requireOld() {
			return false;
		}
	}


	/**読み取れるんだからListenerが必要だろ、みたいな*/


	private BasicListener[] listeners = NOTHING_LISTENER;

	protected final BasicListener[] getListeners(){return  listeners;}

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
}
