package lafkareine.util.linkage;

public interface NoArgAction<T> {
	default T action(T arg){
		return create();
	};

	T create();
}
