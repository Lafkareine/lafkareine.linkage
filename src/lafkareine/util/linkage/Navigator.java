package lafkareine.util.linkage;

public interface Navigator<T, U> {
		Readable<U> get(T from);
}
