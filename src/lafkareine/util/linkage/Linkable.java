
package lafkareine.util.linkage;


import java.util.function.Consumer;
import java.util.function.UnaryOperator;




public class Linkable<T> extends ReadOnlyLinkable<T>{
	
	private byte lazyflag;
	
	/*
	public BasicListener<? super T> addListener(NoArgListener<? super T> listener) {
		return addListener((BasicListener<Object>) listener);
	}
	
	public BasicListener<? super T> addListener(NoOldListener<? super T> listener) {
		return addListener((BasicListener<Object>) listener);
	}
	*/

	private void runningListener(T oldcache, BasicAction<T> oldaction, byte oldlazyflag) {
		for (BasicListener<? super T> listener : getListeners()) {
			if (oldlazyflag == 2 && listener.requireOld()) {
				oldcache = oldaction.action(oldcache);
				oldlazyflag = 1;
			}
			listener.listen(oldcache, listener.requireLatest() ? get() : null);
		}
	}
	
	public interface BasicAction<T> {
		
		T action(T arg);
		
	}
	
	public interface NoArgAction<T> extends BasicAction<T> {
		
		@Override
		default T action(T arg) {
			return create();
		}
		
		T create();
	}
	
	public interface NoRetAction<T> extends BasicAction<T> {
		
		@Override
		default T action(T arg) {
			apply(arg);
			return arg;
		}
		
		void apply(T arg);
	}
	
	
	private BasicAction<T> evalater;
	
	
	public Linkable() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public Linkable(T value) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(value);
	}
	
	public Linkable(T init, BasicAction<T> evalater, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, concerns);
	}
	
	public Linkable(NoArgAction<T> evalater, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater, concerns);
	}
	
	public Linkable(T init, NoRetAction<T> evalater, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, concerns);
	}
	
	public Linkable(T init, BasicAction<T> evalater) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater);
	}
	
	public Linkable(NoArgAction<T> evalater) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater);
	}
	
	public Linkable(T init, NoRetAction<T> evalater) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater);
	}
	
	public Linkable(T init, BasicAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		/** Impulsive Killer Jack The Ripper
		 *
		 */

		set(init, evalater, option, concerns);
	}
	
	public Linkable(NoArgAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater, option, concerns);
	}
	
	public Linkable(T init, NoRetAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, option, concerns);
	}
	
	public Linkable(T init, BasicAction<T> evalater, LazyOption option) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, option);
	}
	
	public Linkable(NoArgAction<T> evalater, LazyOption option) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater, option);
	}
	
	public Linkable(T init, NoRetAction<T> evalater, LazyOption option) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, option);
	}
	
	
	public void set(T init, BasicAction<T> evalater, LinkableBase... concerns) {
		set(init, evalater, LazyOption.QUICK, concerns);
	}
	
	public void set(NoArgAction<T> evalater, LinkableBase... concerns) {
		set(evalater, LazyOption.QUICK, concerns);
	}
	
	public void set(T init, NoRetAction<T> evalater, LinkableBase... concerns) {
		set(init, evalater, LazyOption.QUICK, concerns);
	}
	
	/** @param evalater
	 */
	public void set(T init, BasicAction<T> evalater) {
		set(init, evalater, LazyOption.QUICK, inferConcerns(evalater));
	}
	
	public void set(NoArgAction<T> evalater) {
		set(evalater, LazyOption.QUICK, inferConcerns(evalater));
	}
	
	public void set(T init, NoRetAction<T> evalater) {
		set(init, evalater, LazyOption.QUICK, inferConcerns(evalater));
	}
	
	
	//setWithOption
	public void set(T init, BasicAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		T oldcache = cache;
		BasicAction<T> oldaction = evalater;
		byte oldlazyflag = lazyflag;
		
		this.evalater = evalater;
		if (concerns == null) {
			cache = null;
		}
		if (option == LazyOption.QUICK) {
			lazyflag = 0;
			cache = evalater.action(init);
		} else {
			lazyflag = 2;
			cache = init;
		}
		launchUpdate(concerns);
		
		runningListener(oldcache, oldaction, oldlazyflag);
	}
	
	public void set(NoArgAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		set(null, evalater, option, concerns);
	}
	
	public void set(T init, NoRetAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		set(init, (BasicAction<T>) evalater, option, concerns);
	}
	
	/** @param evalater
	 */
	public void set(T init, BasicAction<T> evalater, LazyOption option) {
		set(init, evalater, option, inferConcerns(evalater));
	}
	
	public void set(NoArgAction<T> evalater, LazyOption option) {
		set(evalater, option, inferConcerns(evalater));
	}
	
	public void set(T init, NoRetAction<T> evalater, LazyOption option) {
		set(init, evalater, option, inferConcerns(evalater));
	}
	
	//setInLazy
	
	public void setInLazy(T init, BasicAction<T> evalater, LinkableBase... concerns) {
		set(init, evalater, LazyOption.LAZY, concerns);
	}
	
	public void setInLazy(NoArgAction<T> evalater, LinkableBase... concerns) {
		set(evalater, LazyOption.LAZY, concerns);
	}
	
	public void setInLazy(T init, NoRetAction<T> evalater, LinkableBase... concerns) {
		set(init, evalater, LazyOption.LAZY, concerns);
	}
	
	/** @param evalater
	 */
	public void setInLazy(T init, BasicAction<T> evalater) {
		set(init, evalater, LazyOption.LAZY, inferConcerns(evalater));
	}
	
	public void setInLazy(NoArgAction<T> evalater) {
		set(evalater, LazyOption.LAZY, inferConcerns(evalater));
	}
	
	public void setInLazy(T init, NoRetAction<T> evalater) {
		set(init, evalater, LazyOption.LAZY, inferConcerns(evalater));
	}
	
	private T cache;
	
	public void set(T value) {
		
		T oldcache = cache;
		BasicAction<T> oldaction = evalater;
		byte oldlazyflag = lazyflag;
		
		cache = value;
		evalater = null;
		lazyflag = 0;
		
		launchUpdate();
		runningListener(oldcache, oldaction, oldlazyflag);
	}
	
	
	@Override
	public T get() {
		if (lazyflag != 2) {
			return cache;
		} else {
			cache = evalater.action(cache);
			lazyflag = 1;
		}
		return cache;
	}
	
	@Override
	protected final void action() {
		// TODO 自動生成されたメソッド・スタブ
		T oldcache = cache;
		BasicAction<T> oldaction = evalater;
		byte oldlazyflag = lazyflag;
		
		if (lazyflag == 0)
			cache = evalater.action(cache);
		else
			lazyflag = 2;
		
		runningListener(oldcache, oldaction, oldlazyflag);
	}
	
	public void transaction(UnaryOperator<T> transaction) {
		set(transaction.apply(get()));
	}
	
	public void transaction(Consumer<T> transaction) {
		transaction.accept(get());
		set(get());
	}
	

	
	@Override
	public Linkable<T> asWriteable() {
		// TODO 自動生成されたメソッド・スタブ
		return this;
	}
}
