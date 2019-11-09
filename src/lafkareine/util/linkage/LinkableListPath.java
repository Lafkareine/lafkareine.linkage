
package lafkareine.util.linkage;


import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LinkableListPath<T, U> extends Listenable<List<U>> {
	
	private class Junction extends LinkableBase{

		private Readable<? extends Collection<? extends T>> from;

		final void set(Readable<? extends Collection<? extends T>> from){
			this.from = from;
			launchAction(from);
		}
		
		@Override
		protected void action() {
			LinkableListPath.this.launchAction(makeInputsArray(from, navigator, junction));
		}
	}

	@Override
	public List<U> get(AutoGuaranteed guaranteed) {
		return cache;
	}

	private Function<? super T,? extends Readable<? extends U>> navigator;

	private List<U> cache;

	private final Junction junction = new Junction();

	public LinkableListPath(){
		super();
	}

	public LinkableListPath(Readable<? extends Collection<? extends T>> from, Function<? super T,? extends Readable<? extends U>> navigator) {
		set(from, navigator);
	}

	public void set(Readable<? extends Collection<? extends T>> from, Function<? super T,? extends Readable<? extends U>> navigator) {
		this.navigator = navigator;
		junction.set(from);
	}

	public void set(Readable<? extends Collection<? extends T>> from) {
		junction.set(from);
	}

	public final U pick(int index){
		return cache.get(index);
	}

	@Override
	protected void action() {
		List<U> oldcache = cache;
		List<U> neocache = makeCache(junction.from.get(),navigator);
		cache = neocache;
		defaultRunListner(oldcache, neocache);
	}

	private static <T, U> LinkableBase[] makeInputsArray(Readable<? extends Collection<? extends T>> from, Function<? super T,? extends Readable<? extends U>> navigator, LinkableBase junction){
		final Collection<? extends T> container = from.get();
		LinkableBase[] parents = new LinkableBase[container.size()+1];
		int i = 0;
		for(var e:container){
			parents[i++] = navigator.apply(e);
		}
		parents[i] = junction;
		return  parents;
	}

	private static <T, U> List<U> makeCache(Collection<? extends T> container,Function<? super T,? extends Readable<? extends U>> navigator){
		return container.stream().map(x -> navigator.apply(x).get()).collect(Collectors.toList());
	}
}


