package lafkareine.util.linkage;

import java.util.function.Function;

public class LinkableTypedPath<T, U> extends Listenable<U> {

	private U cache;

	private Function<? super T, ? extends Listenable<U>> navigator;

	private Listenable<? extends T> from;

	@Override
	protected void action() {
		concern(false);
	}

	public LinkableTypedPath(){
		super();
	}

	public LinkableTypedPath(Listenable<? extends T> from, Function<? super T,? extends Listenable<U>> navigator){
		set(from, navigator);
	}

	public void set(Listenable<? extends T> from, Function<? super T,? extends Listenable<U>> navigator){
		this.from = from;
		this.navigator = navigator;
		if(from.isReady()){
			concern(true);
		}else {
			launchUpdate(from);
		}
	}

	private void concern(boolean update){
		U oldcache = cache;
		Listenable<U> target = navigator.apply(from.get());
		if(update){
			launchUpdate(from,target);
		}else{
			setConcernsInSecretly(from, target);
		}
		if(isReadyToAction()) {
			cache = target.get();
			defaultRunListner(oldcache,cache);
		}
	}

	public void set(Listenable<? extends T> from) {
		this.from = from;
		set(from,navigator);
	}

	public U get(AutoGuaranteed guaranteed) {
		return cache;
	}
}
