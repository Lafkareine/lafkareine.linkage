
package lafkareine.util.linkage;


import java.util.function.Consumer;
import java.util.function.UnaryOperator;




public class Linkable<T> extends Listenable<T> {
	

	/*
	public BasicListener<? super T> addListener(NoArgListener<? super T> listener) {
		return addListener((BasicListener<Object>) listener);
	}
	
	public BasicListener<? super T> addListener(NoOldListener<? super T> listener) {
		return addListener((BasicListener<Object>) listener);
	}
	*/

	private BasicAction<T> evalater;

	private T cache;


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


	public final void set(T init, BasicAction<T> evalater, LinkableBase... concerns) {
		this.evalater = evalater;
		if(concerns == null) throw new NullPointerException(" Null concerns was denied");
		launchAction(concerns);
	}
	public final void set(NoArgAction<T> evalater, LinkableBase... concerns){
		set(null, (BasicAction<T>) evalater, concerns);
	};

	public final void set(T init, NoRetAction<T> evalater, LinkableBase... concerns){
		set(init, (BasicAction<T>) evalater, concerns);
	};

	public final void set(T init, BasicAction<T> evalater){
		set(init, evalater, inferConcerns(evalater));
	};

	public final void set(NoArgAction<T> evalater){
		set(null, (BasicAction<T>) evalater);
	};

	public final void set(T init, NoRetAction<T> evalater){
		set(init, (BasicAction<T>) evalater);
	};

	public final void set(T value) {
		cache = value;
		evalater = null;
		launchUpdate();
	}
	
	
	public final T get(AutoGuaranteed guaranteed) {
		return cache;
	}
	
	@Override
	protected final void action() {
		// TODO 自動生成されたメソッド・スタブ
		eval(cache);
	}

	private void eval(T arg){
		T oldcache = arg;
		T neocache = evalater.action(arg);
		cache = neocache;
		defaultRunListner(oldcache,neocache);

	}


	public void transaction(UnaryOperator<T> transaction) {
		set(transaction.apply(get()));
	}

	public void transaction(Consumer<T> transaction) {
		transaction.accept(get());
		set(get());
	}


}
