
package lafkareine.util.linkage;


import java.util.List;

public class LinkableListPath<T, U extends LinkableBase> extends LinkableBase {

	public interface Getter<T, U extends  LinkableBase> {
		U get(T from);
	}

	public interface Listener{
		void exchange ();
	}

	private Listener[] listeners = NOTHING_LISTENER;

	private static final Listener[] NOTHING_LISTENER = new Listener[] {};


	private Getter<T, U> getter;

	private ReadOnlyLinkable<List<T>> from;

	public LinkableListPath(ReadOnlyLinkable<List<T>> from, Getter<T,U> getter) {
		set(from, getter);
	}

	public void set(ReadOnlyLinkable<List<T>> from, Getter<T,U> getter) {
		this.from = from;
		this.getter = getter;
		launchUpdate(makeInputsArray(from, getter));}

	public void set(ReadOnlyLinkable<List<T>> from) {
		this.from = from;
		launchUpdate(makeInputsArray(from, getter));
	}

	public final U pick(int index){
		return getter.get(from.get().get(index));
	}

	@Override
	protected void action() {
		// TODO 自動生成されたメソッド・スタブ
		setInputsInSecretly(makeInputsArray(from, getter));
	}

	private void runListener(){
		for(var e:listeners){
			e.exchange();
		}
	}

	private static <T, U extends LinkableBase> LinkableBase[] makeInputsArray(ReadOnlyLinkable<List<T>> from, Getter<T,U> getter){
		LinkableBase[] parents = new LinkableBase[from.get().size()+1];
		int i = 0;
		for(var e:from.get()){
			parents[i++] = getter.get(e);
		}
		parents[i] = from;
		return  parents;
	}

	public final Listener addListner(Listener added){
		if(added == null) return null;
		final Listener[] new_array = new Listener[listeners.length+1];
		final Listener[] old_array = listeners;
		for(int i = 0; i < old_array.length; i++){
			new_array[i] = old_array[i];
		}
		new_array[old_array.length] = added;
		return  added;
	}

	public final boolean removeListner(Listener added){
		final Listener[] old_array = listeners;
		int nullnum = 0;
		for(int i = 0; i < old_array.length; i++){
			if(old_array[i] == added)old_array[i] = null;
			nullnum += 1;
		}
		final Listener[] new_array = new Listener[listeners.length-nullnum];
		int index_to = 0;
		for(var e:old_array){
			if(e != null)new_array[index_to++] = e;
		}
		return nullnum > 0;
	}
}


