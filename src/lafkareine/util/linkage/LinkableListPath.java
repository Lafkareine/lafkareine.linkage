
package lafkareine.util.linkage;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LinkableListPath<T, U> extends Readable<List<U>> {

	@Override
	public List<U> get() {
		return cache;
	}

	public interface Listener{
		void exchange ();
	}

	private Listener[] listeners = NOTHING_LISTENER;

	private static final Listener[] NOTHING_LISTENER = new Listener[] {};


	private Navigator<T, U> navigator;

	private Readable<Collection<T>> from;

	private List<U> cache;

	public LinkableListPath(){
		super();
	}

	public LinkableListPath(Readable<Collection<T>> from, Navigator<T,U> navigator) {
		set(from, navigator);
	}

	public void set(Readable<Collection<T>> from, Navigator<T,U> navigator) {
		this.from = from;
		this.navigator = navigator;
		launchUpdate(makeInputsArray(from, navigator));
	}

	public void set(Readable<Collection<T>> from) {
		this.from = from;
		launchUpdate(makeInputsArray(from, navigator));
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
			runListener(oldcache, cache);
		}
	}

	private void runListener(List<U> old, List<U> neo){
		for(var e:getListeners()){
			e.listen(old,neo);
		}
	}

	private static <T, U> LinkableBase[] makeInputsArray(Readable<Collection<T>> from, Navigator<T,U> navigator){
		final Collection<T> container = from.get();
		LinkableBase[] parents = new LinkableBase[container.size()+1];
		int i = 0;
		for(var e:container){
			parents[i++] = navigator.get(e);
		}
		parents[i] = from;
		return  parents;
	}

	private static <T, U> List<U> makeCache(Collection<T> container, Navigator<T,U> navigator){
		return container.stream().map(x -> navigator.get(x).get()).collect(Collectors.toList());
	}
}


