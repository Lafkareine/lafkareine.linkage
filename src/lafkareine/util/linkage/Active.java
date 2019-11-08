package lafkareine.util.linkage;

import java.util.Arrays;

public abstract class Active extends LinkableBase{

	public interface Reactor{
		void react();
	}

	private Reactor[] reactors = new Reactor[]{};

	public final void addReactor(Reactor added){
		reactors = Arrays.copyOf(reactors, reactors.length+1);
		reactors[reactors.length-1] = added;
	}

	public final void removeReactor(Reactor removed){
		final Reactor[] reactors = this.reactors;
		for(int i = 0;i < reactors.length;i++){
			if(reactors[i] == removed){
				reactors[i] = reactors[reactors.length-1];
			}
		}
		this.reactors = Arrays.copyOf(reactors, reactors.length-1);
	}

	protected final void runReactor(){
		for(var e:reactors){
			e.react();
		}
	}
}
