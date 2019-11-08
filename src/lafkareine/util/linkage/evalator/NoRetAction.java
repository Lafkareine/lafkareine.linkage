package lafkareine.util.linkage.evalator;

public interface NoRetAction<T> extends BasicAction<T> {

	@Override
	default T action(T arg) {
		apply(arg);
		return arg;
	}

	void apply(T arg);
}
