
package lafkareine.util.linkage;

import java.util.function.Function;

public class LinkablePath<T extends  LinkableBase, U extends LinkableBase> extends Listenable<U> {

	private class Junction extends LinkableBase{

		private T from;

		public void set(T from) {
			this.from = from;
			launchAction(from);
		}

		@Override
		protected void action() {
			U oldcache = cache;
			U neocache = navigator.apply(from);
			cache = neocache;
			LinkablePath.this.launchUpdate(this, neocache);
			defaultRunListner(oldcache, neocache);
		}
	}

	private U cache;

	private Function<? super T, ? extends U> navigator;

	private final Junction junction = new Junction();


	public LinkablePath(){
		super();
	}
	
	public LinkablePath(T from, Function<? super T, ? extends U> navigator) {
		set(from, navigator);
	}
	
	public void set(T from, Function<? super T, ? extends U> navigator) {
		this.navigator = navigator;
		junction.set(from);
	}

	public void set(T from) {
		junction.set(from);
	}

	@Override
	protected void action() {}

	@Override
	public final U get(AutoGuaranteed guaranteed) {
		return cache;
	}

}


