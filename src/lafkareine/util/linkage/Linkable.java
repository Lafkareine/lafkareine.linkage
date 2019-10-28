
package lafkareine.util.linkage;


import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;




public class Linkable<T> extends ReadOnlyLinkable<T>{
	
	private byte lazyflag;
	
	private BasicListener[] listeners = NOTHING_LISTENER;

	private static final BasicListener[] NOTHING_LISTENER = new BasicListener[] {};
	
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
	
	public BasicListener<? super T> addListener(BasicListener<? super T> listener) {
		listeners = Arrays.copyOf(listeners, listeners.length + 1);
		listeners[listeners.length - 1] = listener;
		return listener;
	}
	/*
	public BasicListener<? super T> addListener(NoArgListener<? super T> listener) {
		return addListener((BasicListener<Object>) listener);
	}
	
	public BasicListener<? super T> addListener(NoOldListener<? super T> listener) {
		return addListener((BasicListener<Object>) listener);
	}
	*/

	private void runningListener(T oldcache, BasicAction<T> oldaction, byte oldlazyflag) {
		for (BasicListener<? super T> listener : listeners) {
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
	
	public Linkable(T init, BasicAction<T> evalater, LinkableBase... inputs) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, inputs);
	}
	
	public Linkable(NoArgAction<T> evalater, LinkableBase... inputs) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater, inputs);
	}
	
	public Linkable(T init, NoRetAction<T> evalater, LinkableBase... inputs) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, inputs);
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
	
	public Linkable(T init, BasicAction<T> evalater, LazyOption option, LinkableBase... inputs) {
		// TODO 自動生成されたコンストラクター・スタブ
		/** Impulsive Killer Jack The Ripper
		 *
		 */

		set(init, evalater, option, inputs);
	}
	
	public Linkable(NoArgAction<T> evalater, LazyOption option, LinkableBase... inputs) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater, option, inputs);
	}
	
	public Linkable(T init, NoRetAction<T> evalater, LazyOption option, LinkableBase... inputs) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, option, inputs);
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
	
	
	public void set(T init, BasicAction<T> evalater, LinkableBase... inputs) {
		set(init, evalater, LazyOption.QUICK, inputs);
	}
	
	public void set(NoArgAction<T> evalater, LinkableBase... inputs) {
		set(evalater, LazyOption.QUICK, inputs);
	}
	
	public void set(T init, NoRetAction<T> evalater, LinkableBase... inputs) {
		set(init, evalater, LazyOption.QUICK, inputs);
	}
	
	/** @param evalater
	 */
	public void set(T init, BasicAction<T> evalater) {
		set(init, evalater, LazyOption.QUICK, inferInputs(evalater));
	}
	
	public void set(NoArgAction<T> evalater) {
		set(evalater, LazyOption.QUICK, inferInputs(evalater));
	}
	
	public void set(T init, NoRetAction<T> evalater) {
		set(init, evalater, LazyOption.QUICK, inferInputs(evalater));
	}
	
	
	//setWithOption
	public void set(T init, BasicAction<T> evalater, LazyOption option, LinkableBase... inputs) {
		T oldcache = cache;
		BasicAction<T> oldaction = evalater;
		byte oldlazyflag = lazyflag;
		
		this.evalater = evalater;
		if (inputs == null) {
			cache = null;
		}
		if (option == LazyOption.QUICK) {
			lazyflag = 0;
			cache = evalater.action(init);
		} else {
			lazyflag = 2;
			cache = init;
		}
		launchUpdate(inputs);
		
		runningListener(oldcache, oldaction, oldlazyflag);
	}
	
	public void set(NoArgAction<T> evalater, LazyOption option, LinkableBase... inputs) {
		set(null, evalater, option, inputs);
	}
	
	public void set(T init, NoRetAction<T> evalater, LazyOption option, LinkableBase... inputs) {
		set(init, (BasicAction<T>) evalater, option, inputs);
	}
	
	/** @param evalater
	 */
	public void set(T init, BasicAction<T> evalater, LazyOption option) {
		set(init, evalater, option, inferInputs(evalater));
	}
	
	public void set(NoArgAction<T> evalater, LazyOption option) {
		set(evalater, option, inferInputs(evalater));
	}
	
	public void set(T init, NoRetAction<T> evalater, LazyOption option) {
		set(init, evalater, option, inferInputs(evalater));
	}
	
	//setInLazy
	
	public void setInLazy(T init, BasicAction<T> evalater, LinkableBase... inputs) {
		set(init, evalater, LazyOption.LAZY, inputs);
	}
	
	public void setInLazy(NoArgAction<T> evalater, LinkableBase... inputs) {
		set(evalater, LazyOption.LAZY, inputs);
	}
	
	public void setInLazy(T init, NoRetAction<T> evalater, LinkableBase... inputs) {
		set(init, evalater, LazyOption.LAZY, inputs);
	}
	
	/** @param evalater
	 */
	public void setInLazy(T init, BasicAction<T> evalater) {
		set(init, evalater, LazyOption.LAZY, inferInputs(evalater));
	}
	
	public void setInLazy(NoArgAction<T> evalater) {
		set(evalater, LazyOption.LAZY, inferInputs(evalater));
	}
	
	public void setInLazy(T init, NoRetAction<T> evalater) {
		set(init, evalater, LazyOption.LAZY, inferInputs(evalater));
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
	public void focus(Consumer<T> work) {
		work.accept(get());
	}
	
	@Override
	public <U> U focus(Function<T, U> work) {
		return work.apply(get());
	}
	
	@Override
	public Linkable<T> asWriteable() {
		// TODO 自動生成されたメソッド・スタブ
		return this;
	}
}
