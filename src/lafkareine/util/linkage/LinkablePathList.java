
package lafkareine.util.linkage;

import java.util.List;
import java.util.function.Function;

public class LinkablePathList<T extends  LinkableBase, U extends LinkableBase> extends Listenable<List<U>> {

	@Override
	public final List<U> get(AutoGuaranteed guaranteed) {
		return cache;
	}

	private List<U> cache;

	private Function<? super T, ? extends List<U>> navigator;

	private T from;

	public LinkablePathList() {
		super();
	}

	public LinkablePathList(T from, Function<? super T, ? extends List<U>> navigator) {
		set(from, navigator);
	}

	public void set(T from, Function<? super T, ? extends List<U>> navigator) {
		this.from = from;
		this.navigator = navigator;
		if(from.isReady()) {
			concern(true);
		}else {
			launchUpdate(from);
		}
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
		concern(false);
	}

	private void concern(boolean update) {
		List<U> neocache = navigator.apply(from);
		// TODO 自動生成されたメソッド・スタブ
		var array = makeInputArray(from, neocache);
		if (update) {
			launchUpdate(array);
		} else {
			setConcernsInSecretly(array);
		}
		if (isReadyToAction()) {
			List<U> oldcache = cache;
			cache = neocache;
			defaultRunListner(oldcache, neocache);
		}
	}
}


