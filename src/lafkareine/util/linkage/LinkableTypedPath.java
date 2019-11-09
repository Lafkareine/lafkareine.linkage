package lafkareine.util.linkage;

import java.util.function.Function;

public class LinkableTypedPath<T, U> extends Listenable<U> {

	private class Junction extends LinkableBase{
		private Readable<? extends T> from;

		public void set(Readable<? extends T> from) {
			this.from = from;
			launchAction(from);
		}

		@Override
		protected void action() {
			U oldcache = cache;
			Readable<U> target = navigator.apply(from.get());
			U neocache = target.get();
			cache = neocache;
			launchUpdate(this,target);
			defaultRunListner(oldcache,neocache);
		}
	}

	private U cache;

	private Function<? super T, ? extends Readable<U>> navigator;

	private final Junction junction = new Junction();

	@Override
	protected void action() {
		concern(false);
	}

	public LinkableTypedPath(){
		super();
	}

	public LinkableTypedPath(Readable<? extends T> from, Function<? super T,? extends Readable<U>> navigator){
		set(from, navigator);
	}

	public void set(Readable<? extends T> from, Function<? super T,? extends Readable<U>> navigator){
		this.navigator = navigator;
		junction.set(from);
	}

	private void concern(boolean update){

	}

	public void set(Readable<? extends T> from) {
		set(from,navigator);
	}

	public U get(AutoGuaranteed guaranteed) {
		return cache;
	}
}
