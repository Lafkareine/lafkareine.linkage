package lafkareine.util.linkage;

public interface NoRetAction<T> extends BasicAction<T> {

	@Override
	default T action(T arg) {
		apply(arg);
		return arg;
	}

	void apply(T arg);
}
