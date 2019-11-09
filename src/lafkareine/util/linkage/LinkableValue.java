package lafkareine.util.linkage;

public abstract class LinkableValue<T> extends Listenable<T> {

	public LinkableValue(LinkableBase... concerns){
		super();
		update(concerns);
	}

	public LinkableValue(T init, LinkableBase... concerns){
		super();
		update(init, concerns);
	}

	public final void update(LinkableBase... concerns){
		launchAction(concerns);
	}

	public final void update(T init, LinkableBase... concerns){
		cache = init;
		launchAction(concerns);
	}

	private T cache;

	@Override
	public final T get(AutoGuaranteed guaranteed) {return cache;
	}

	@Override
	protected final void action() {
		T oldcache = cache;
		T neocache = calc(oldcache);
		cache = neocache;
		defaultRunListner(oldcache, neocache);
	}

	abstract protected T calc(T cache);
}
