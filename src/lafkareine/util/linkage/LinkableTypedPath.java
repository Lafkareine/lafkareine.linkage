package lafkareine.util.linkage;

public class LinkableTypedPath<T, U> extends Readable<U>{

	private U cache;

	private Navigator<T, U> navigator;

	private Readable<T> from;

	@Override
	protected void action() {
		setInputsInSecretly(from, navigator.get(from.get()));
		if(isReady()) {
			cache = navigator.get(from.get()).get();
		}
	}

	public LinkableTypedPath(){
		super();
	}

	public LinkableTypedPath(Readable<T> from, Navigator<T,U> navigator){
		set(from, navigator);
	}

	public void set(Readable<T> from, Navigator<T,U> navigator){

		this.from = from;
		this.navigator = navigator;
		cache = navigator.get(from.get()).get();
		launchUpdate(from, navigator.get(from.get()));
	}

	public U get() {
		return cache;
	}
}
