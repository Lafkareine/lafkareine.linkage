
package lafkareine.util.linkage;


import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LinkableListPath<T, U> extends Listenable<List<U>> {

	@Override
	public List<U> get(AutoGuaranteed guaranteed) {
		return cache;
	}

	private Function<? super T,? extends Listenable<? extends U>> navigator;

	private Listenable<? extends Collection<? extends T>> from;

	private List<U> cache;

	public LinkableListPath(){
		super();
	}

	public LinkableListPath(Listenable<? extends Collection<? extends T>> from, Function<? super T,? extends Listenable<? extends U>> navigator) {
		set(from, navigator);
	}

	public void set(Listenable<? extends Collection<? extends T>> from, Function<? super T,? extends Listenable<? extends U>> navigator) {
		this.from = from;
		this.navigator = navigator;
		if(from.isReady()){
			concern(true);
		}else{
			launchUpdate(from);
		}
	}

	public void set(Listenable<Collection<T>> from) {
		set(from,navigator);
	}

	public final U pick(int index){
		return cache.get(index);
	}

	@Override
	protected void action() {
		// TODO 自動生成されたメソッド・スタブ
		concern(false);
	}

	private void concern(boolean updadte){
		var array = makeInputsArray(from, navigator);
		if(updadte){launchUpdate(array);}else{setConcernsInSecretly(array);}
		if(isReadyToAction()){
			List<U> oldcache = cache;
			cache = makeCache(from.get(), navigator);
			defaultRunListner(oldcache, cache);
		}
	}

	private static <T, U> LinkableBase[] makeInputsArray(Listenable<? extends Collection<? extends T>> from, Function<? super T,? extends Listenable<? extends U>> navigator){
		final Collection<? extends T> container = from.get();
		LinkableBase[] parents = new LinkableBase[container.size()+1];
		int i = 0;
		for(var e:container){
			parents[i++] = navigator.apply(e);
		}
		parents[i] = from;
		return  parents;
	}

	private static <T, U> List<U> makeCache(Collection<? extends T> container,Function<? super T,? extends Listenable<? extends U>> navigator){
		return container.stream().map(x -> navigator.apply(x).get()).collect(Collectors.toList());
	}
}


