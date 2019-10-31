package lafkareine.util.linkage;

public class LinkableTypedPath<T, U> extends Readable<U>{

	private U cache;

	private Getter<T, U> getter;

	private Readable<T> from;

	@Override
	protected void action() {
		cache = getter.get(from.get()).get();
		setInputsInSecretly(from, getter.get(from.get()));
	}

	public interface Getter<T,U>{
		 Readable<U> get(T from);
	};

	public LinkableTypedPath(){
		super();
	}

	public LinkableTypedPath(Readable<T> from, Getter<T,U> getter){
		set(from, getter);
	}

	public void set(Readable<T> from, Getter<T,U> getter){

		this.from = from;
		this.getter = getter;
		cache = getter.get(from.get()).get();
		launchUpdate(from, getter.get(from.get()));
	}

	public U get() {
		return cache;
	}
}
