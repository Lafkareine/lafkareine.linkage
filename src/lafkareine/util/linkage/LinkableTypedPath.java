package lafkareine.util.linkage;

import java.util.function.Function;

public class LinkableTypedPath<T, U> extends Readable<U>{

	private U cache;

	private Function<? super T, ? extends Readable<U>> navigator;

	private Readable<? extends T> from;

	@Override
	protected void action() {
		U oldcache = cache;
		Readable<U> target = navigator.apply(from.get());
		setInputsInSecretly(from, target);
		if(isReady()) {
			cache = target.get();
			defaultRunListner(oldcache,cache);
		}
	}

	public LinkableTypedPath(){
		super();
	}

	public LinkableTypedPath(Readable<? extends T> from, Function<? super T,? extends Readable<U>> navigator){
		set(from, navigator);
	}

	public void set(Readable<? extends T> from, Function<? super T,? extends Readable<U>> navigator){
		this.from = from;
		this.navigator = navigator;
		cache = navigator.apply(from.get()).get();
		launchUpdate(from, navigator.apply(from.get()));
	}

	public U get() {
		return cache;
	}
}
