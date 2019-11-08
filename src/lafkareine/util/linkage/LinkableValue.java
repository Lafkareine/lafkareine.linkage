package lafkareine.util.linkage;

public abstract class LinkableValue<T> extends Listenable<T> {

	public LinkableValue(LinkableBase... concerns){
		super();
		update(concerns);
	}

	public LinkableValue(T init, LinkableBase... concerns){
		super();
		update(init, concerns);
	}

	public final void update(LinkableBase... concerns){
		launchFirstAction(concerns);
	}

	public final void update(T init, LinkableBase... concerns){
		cache = init;
		launchFirstAction(concerns);
	}

	private T cache;

	@Override
	public final T get(AutoGuaranteed guaranteed) {return cache;
	}

	@Override
	protected final void action() {
		T oldcache = cache;
		T neocache = calc(oldcache);
		// 準備が完了していない親を持っている場合、無効な更新であるとしてキャッシュを更新せずリスナの駆動も行わない
		// あとで親の準備がすべて完了したときにもう一回アクションがある
		if(!isReadyToAction()){
		cache = neocache;
		defaultRunListner(oldcache, cache);
		}
	}

	abstract protected T calc(T cache);
}
