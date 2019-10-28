
package lafkareine.util.linkage;


import java.util.function.Consumer;
import java.util.function.Function;


public abstract class ReadOnlyLinkable<T> extends LinkableBase {
	
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
	
	public ReadOnlyLinkable() {
		super();
	}
	
	public abstract boolean removeListener(Object listener);
	
	public abstract BasicListener<? super T> addListener(BasicListener<? super T> listener);
	
	public BasicListener<? super T> addListener(NoArgListener<? super T> listener){
		return  addListener(listener);
	};
	
	public BasicListener<? super T> addListener(NoOldListener<? super T> listener){
		return  addListener(listener);
	};
	
	public abstract Linkable<T> asWriteable();
	
	public abstract <U> U focus(Function<T, U> work);
	
	public abstract void focus(Consumer<T> work);
	
	public abstract T get();
}