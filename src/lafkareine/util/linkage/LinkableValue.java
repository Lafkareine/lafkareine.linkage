package lafkareine.util.linkage;

public abstract class LinkableValue<T> extends Readable<T> {

	public LinkableValue(LinkableBase... concerns){
		super();
		action();
		launchUpdate(concerns);
	}

	private T cache;

	@Override
	public T get() {
		return cache;
	}

	@Override
	protected void action() {
		T oldcache = cache;
		cache = calc();
		defaultRunListner(oldcache, cache);
	}

	abstract protected T calc();
}
