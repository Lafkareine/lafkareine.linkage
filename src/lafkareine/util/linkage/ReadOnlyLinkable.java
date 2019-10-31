
package lafkareine.util.linkage;


import java.util.function.Consumer;
import java.util.function.Function;


public abstract class ReadOnlyLinkable<T> extends Readable<T>{

	public abstract Linkable<T> asWriteable();
}