
package lafkareine.util.linkage;

import java.util.List;
import java.util.function.Function;

public class LinkablePathList<T extends  LinkableBase, U extends LinkableBase> extends Readable<List<U>> {

	@Override
	public final List<U> get() {
		return cache;
	}

	private List<U> cache;

	private Function<? super T, ? extends List<U>> navigator;

	private T from;

	public LinkablePathList(){
		super();
	}

	public LinkablePathList(T from, Function<? super T, ? extends List<U>> navigator) {
		set(from, navigator);
	}
	
	public void set(T from, Function<? super T, ? extends List<U>> navigator) {
		this.from = from;
		this.navigator = navigator;
		List<U> oldcache = cache;
		cache = navigator.apply(from);
		launchUpdate(makeInputArray(from, cache));
		defaultRunListner(oldcache,cache);
	}

	private static LinkableBase[] makeInputArray(LinkableBase from, List<? extends LinkableBase> path){
		LinkableBase[] inputs = new LinkableBase[path.size()+1];
		int i = 0;
		inputs[i++] = from;
		for(var e : path){
			inputs[i++] = e;
		}
		return inputs;
	}

	@Override
	protected void action() {
		List<U> neocache = navigator.apply(from);
		// TODO 自動生成されたメソッド・スタブ
		setConcernsInSecretly(makeInputArray(from,neocache));
		if(isReady()){
			List<U> oldcache = cache;
			cache = neocache;
			defaultRunListner(oldcache, neocache);
		}
	}
}


