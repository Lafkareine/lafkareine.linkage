package lafkareine.util.linkage;

public abstract class LinkableLazyValue<T> extends Readable<T> {

	private T cache;

	private boolean needs;

	public LinkableLazyValue(){
		super();
	}

	public final void update(LinkableBase... concerns){
		launchFirstAction(concerns);
	}

	public final void update(T init, LinkableBase... concerns){
		cache = init;
		launchFirstAction(concerns);
	}

	public LinkableLazyValue(LinkableBase... concerns){
		super();
		update(concerns);
	}

	public LinkableLazyValue(T init, LinkableBase... concerns){
		super();
		update(init, concerns);
	}

	@Override
	public final T get(AutoGuaranteed guaranteed) {
		if(needs){
		needs = false;
		cache = calc(cache);
		if(isReady())throw new IllegalStateException("It's unready now");
		}
		return cache;
	}

	@Override
	protected final void action() {
		needs = true;
	}

	protected abstract T calc(T cache);
}
