package lafkareine.util.linkage;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Readable<T> extends LinkableBase {
	/**このLinkableが持つ状態を読み取ります
	 * もしisReadyがfalseだった場合、自動的にIllegalStateExceptionが呼び出されます
	 * */
	public final T get(){
		if(!isReady()) throw new IllegalStateException("It's unready now");
		return get(AutoGuaranteed.READY);
	}

	protected abstract T get(AutoGuaranteed guaranteed);

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

	enum AutoGuaranteed{
		READY
	}
}
