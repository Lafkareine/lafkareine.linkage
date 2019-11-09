
package lafkareine.util.linkage;

import java.util.List;
import java.util.function.Function;

public class LinkablePathList<T extends LinkableBase, U extends LinkableBase> extends Listenable<List<U>> {

	private class Junction extends LinkableBase{

		private T from;

		private void set(T from){
			this.from = from;
			launchAction(from);
		}

		@Override
		protected void action() {
			List<U> neocache = navigator.apply(from);
			List<U> oldcache = cache;
			cache = neocache;

			var array = makeInputArray(this, neocache);
			LinkablePathList.this.launchUpdate(array);
			defaultRunListner(oldcache, neocache);
		}
	}

	@Override
	public final List<U> get(AutoGuaranteed guaranteed) {
		return cache;
	}

	private List<U> cache;

	private Junction junction = new Junction();

	private Function<? super T, ? extends List<U>> navigator;

	public LinkablePathList() {
		super();
	}

	public LinkablePathList(T from, Function<? super T, ? extends List<U>> navigator) {
		set(from, navigator);
	}

	public void set(T from) {
		junction.set(from);
	}

	public void set(T from, Function<? super T, ? extends List<U>> navigator) {
		this.navigator = navigator;
		junction.set(from);
	}


	private static LinkableBase[] makeInputArray(LinkableBase from, List<? extends LinkableBase> path) {
		LinkableBase[] inputs = new LinkableBase[path.size() + 1];
		int i = 0;
		inputs[i++] = from;
		for (var e : path) {
			inputs[i++] = e;
		}
		return inputs;
	}

	@Override
	protected void action() {
	}
}


