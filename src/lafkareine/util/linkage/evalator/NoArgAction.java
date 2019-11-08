package lafkareine.util.linkage.evalator;

public interface NoArgAction<T> {
	default T action(T arg){
		return create();
	};

	T create();
}
