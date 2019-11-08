
package lafkareine.util.linkage;


import java.util.function.Consumer;
import java.util.function.UnaryOperator;


public class LinkableLazy<T> extends Readable<T>{

	private byte lazyflag;

	/*
	public BasicListener<? super T> addListener(NoArgListener<? super T> listener) {
		return addListener((BasicListener<Object>) listener);
	}

	public BasicListener<? super T> addListener(NoOldListener<? super T> listener) {
		return addListener((BasicListener<Object>) listener);
	}
	*/

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


	public LinkableLazy() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public LinkableLazy(T value) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(value);
	}

	public LinkableLazy(T init, BasicAction<T> evalater, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, concerns);
	}

	public LinkableLazy(NoArgAction<T> evalater, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater, concerns);
	}

	public LinkableLazy(T init, NoRetAction<T> evalater, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, concerns);
	}

	public LinkableLazy(T init, BasicAction<T> evalater) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater);
	}

	public LinkableLazy(NoArgAction<T> evalater) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater);
	}

	public LinkableLazy(T init, NoRetAction<T> evalater) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater);
	}

	public LinkableLazy(T init, BasicAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		/** Impulsive Killer Jack The Ripper
		 *
		 */

		set(init, evalater, option, concerns);
	}

	public LinkableLazy(NoArgAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater, option, concerns);
	}

	public LinkableLazy(T init, NoRetAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, option, concerns);
	}

	public LinkableLazy(T init, BasicAction<T> evalater, LazyOption option) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(init, evalater, option);
	}

	public LinkableLazy(NoArgAction<T> evalater, LazyOption option) {
		// TODO 自動生成されたコンストラクター・スタブ
		set(evalater, option);
	}

	public LinkableLazy(T init, NoRetAction<T> evalater, LazyOption option) {
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

		if(concerns == null) throw new NullPointerException(" Null concerns was denied");
		this.evalater = evalater;

		if (option == LazyOption.QUICK) {
			lazyflag = 0;
		} else {
			lazyflag = 2;
		}
		cache = init;

		launchFirstAction(concerns);
	}
	
	public void set(NoArgAction<T> evalater, LazyOption option, LinkableBase... concerns) {
		set(null, (BasicAction<T>) evalater, option, concerns);
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
		setInLazy(init, evalater, inferConcerns(evalater));
	}
	
	public void setInLazy(NoArgAction<T> evalater) {
		setInLazy(evalater, inferConcerns(evalater));
	}
	
	public void setInLazy(T init, NoRetAction<T> evalater) {
		setInLazy(init, evalater, inferConcerns(evalater));
	}
	
	private T cache;
	
	public final void set(T value) {
		
		cache = value;
		evalater = null;
		lazyflag = 0;
		
		launchUpdate();
	}
	
	
	@Override
	public final T get(AutoGuaranteed guaranteed) {
		if (lazyflag != 2) {
			return cache;
		} else {
			cache = evalater.action(cache);
			if(isReady()){
				lazyflag = 1;
			} else{
				throw new IllegalStateException("It's unready now");
			}
		}
		return cache;
	}
	
	@Override
	protected final void action() {
		// TODO 自動生成されたメソッド・スタブ
		if (lazyflag == 0)
			cache = evalater.action(cache);
		else
			lazyflag = 2;
	}
	
	public void transaction(UnaryOperator<T> transaction) {
		set(transaction.apply(get()));
	}
	
	public void transaction(Consumer<T> transaction) {
		transaction.accept(get());
		set(get());
	}

}
