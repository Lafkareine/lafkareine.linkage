package lafkareine.util.linkage;

public abstract class LinkableLazyValue<T> extends Readable<T> {

	private T cache;

	private boolean needs;

	public LinkableLazyValue(){
		super();
	}

	public LinkableLazyValue(LinkableBase... concerns){
		super();
		update(concerns);
	}

	public LinkableLazyValue(T init, LinkableBase... concerns){
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

	@Override
	public final T get(AutoGuaranteed guaranteed) {
		if(needs){
			needs = false;
			cache = calc(cache);
		}
		return cache;
	}

	@Override
	protected final void action() {
		needs = true;
	}

	protected abstract T calc(T cache);
}
