
package lafkareine.util.linkage;

import java.util.function.Function;

public class LinkablePath<T extends  LinkableBase, U extends LinkableBase> extends Readable<U> {

	@Override
	public final U get() {
		return cache;
	}

	private U cache;

	private Function<? super T, ? extends U> navigator;
	
	private T from;

	public LinkablePath(){
		super();
	}
	
	public LinkablePath(T from, Function<? super T, ? extends U> navigator) {
		set(from, navigator);
	}
	
	public void set(T from, Function<? super T, ? extends U> navigator) {
		this.from = from;
		this.navigator = navigator;
		cache = navigator.apply(from);
		launchUpdate(from, navigator.apply(from));
	}

	@Override
	protected void action() {
		U neocache = navigator.apply(from);
		// TODO 自動生成されたメソッド・スタブ
		setInputsInSecretly(from, neocache);
		if(isReady()){
			U oldcache = cache;
			cache = neocache;
			defaultRunListner(oldcache, neocache);
		}
	}
}


