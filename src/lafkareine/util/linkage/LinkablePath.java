
package lafkareine.util.linkage;

import java.util.function.Function;

public class LinkablePath<T extends  LinkableBase, U extends LinkableBase> extends Listenable<U> {

	@Override
	public final U get(AutoGuaranteed guaranteed) {
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
		if(from.isReady()){
			concern(true);
		}else {
			launchUpdate(from);
		}
	}

	public void set(T from) {
		this.from = from;
		set(from,navigator);
	}

	@Override
	protected void action() {
		concern(false);
	}

	private final void concern(boolean update){
		U neocache = navigator.apply(from);
		// TODO 自動生成されたメソッド・スタブ
		if(update){launchUpdate(from, neocache);}else{setConcernsInSecretly(from, neocache);}
		if(isReadyToAction()){
			U oldcache = cache;
			cache = neocache;
			defaultRunListner(oldcache, neocache);
		}
	}
}


