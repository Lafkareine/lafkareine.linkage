
package lafkareine.util.linkage;


import java.util.Arrays;

public class LinkablePath<T extends  LinkableBase, U extends LinkableBase> extends LinkableBase {
	
	public interface Getter<T extends LinkableBase, U extends  LinkableBase> {
		U get(T from);
	}

	public interface Listener<U extends LinkableBase> {
		void exchange (U from, U to);
	}

	private Listener<U>[] listeners = NOTHING_LISTENER;

	private static final Listener[] NOTHING_LISTENER = new Listener[] {};

	private U cache;
	
	private Getter<T, U> getter;
	
	private T from;
	
	public LinkablePath(T from, Getter<T,U> getter) {
		this.from = from;
		this.getter = getter;
		cache = getter.get(from);
		launchUpdate(from, getter.get(from));
	}
	
	public <T extends LinkableBase> void set(T from, Getter<T, U> getter) {
		launchUpdate(from, getter.get(from));
	}

	public final U unwrap() {
		return cache;
	}

	@Override
	protected void action() {
		// TODO 自動生成されたメソッド・スタブ
		U oldcache = cache;
		cache = getter.get(from);
		setInputsInSecretly(from, cache);
	}

	public final Listener<U> addListner(Listener<U> added){
		if(added == null) return null;
		final Listener<U>[] new_array = new Listener[listeners.length+1];
		final Listener<U>[] old_array = listeners;
		for(int i = 0; i < old_array.length; i++){
			new_array[i] = old_array[i];
		}
		new_array[old_array.length] = added;
		return  added;
	}

	public final boolean removeListner(Listener<U> added){
		final Listener<U>[] old_array = listeners;
		int nullnum = 0;
		for(int i = 0; i < old_array.length; i++){
			if(old_array[i] == added)old_array[i] = null;
			nullnum += 1;
		}
		final Listener<U>[] new_array = new Listener[listeners.length-nullnum];
		int index_to = 0;
		for(var e:old_array){
			if(e != null)new_array[index_to++] = e;
		}
		return nullnum > 0;
	}
}


