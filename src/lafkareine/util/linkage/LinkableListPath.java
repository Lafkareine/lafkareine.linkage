
package lafkareine.util.linkage;


import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LinkableListPath<T, U> extends Readable<List<U>> {

	@Override
	public List<U> get() {
		return cache;
	}

	private Function<? super T,? extends Readable<? extends U>> navigator;

	private Readable<? extends Collection<? extends T>> from;

	private List<U> cache;

	public LinkableListPath(){
		super();
	}

	public LinkableListPath(Readable<? extends Collection<? extends T>> from, Function<? super T,? extends Readable<? extends U>> navigator) {
		set(from, navigator);
	}

	public void set(Readable<? extends Collection<? extends T>> from, Function<? super T,? extends Readable<? extends U>> navigator) {
		this.from = from;
		this.navigator = navigator;
		List<U> oldcache = cache;
		cache = makeCache(from.get(),navigator);
		launchUpdate(makeInputsArray(from, navigator));
		defaultRunListner(oldcache,cache);
	}

	public void set(Readable<Collection<T>> from) {
		set(from,navigator);
	}

	public final U pick(int index){
		return cache.get(index);
	}

	@Override
	protected void action() {
		// TODO 自動生成されたメソッド・スタブ
		setInputsInSecretly(makeInputsArray(from, navigator));
		if(isReady()){
			List<U> oldcache = cache;
			cache = makeCache(from.get(), navigator);
			defaultRunListner(oldcache, cache);
		}
	}

	private static <T, U> LinkableBase[] makeInputsArray(Readable<? extends Collection<? extends T>> from, Function<? super T,? extends Readable<? extends U>> navigator){
		final Collection<? extends T> container = from.get();
		LinkableBase[] parents = new LinkableBase[container.size()+1];
		int i = 0;
		for(var e:container){
			parents[i++] = navigator.apply(e);
		}
		parents[i] = from;
		return  parents;
	}

	private static <T, U> List<U> makeCache(Collection<? extends T> container,Function<? super T,? extends Readable<? extends U>> navigator){
		return container.stream().map(x -> navigator.apply(x).get()).collect(Collectors.toList());
	}


}


