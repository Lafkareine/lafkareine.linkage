package lafkareine.util.linkage;

import java.nio.channels.Selector;

public class Shift extends LinkableBase{

	public interface ConcernSetter{

		void setConcerns(LinkableBase... concerns);
	}

	public interface ConcernSelector{
		void select(ConcernSetter setter);
	}

	private class Gear extends LinkableBase implements ConcernSetter{

		private ConcernSelector selector;

		private void set(ConcernSelector selector, LinkableBase[] roots){
			this.selector = selector;
			launchAction(roots);
		}

		@Override
		protected void action() {
			selector.select(this);
		}

		@Override
		public void setConcerns(LinkableBase... concerns) {
			LinkableBase[] array = new LinkableBase[concerns.length+1];
			int i = 0;
			array[i++] = this;
			for(var e:concerns){
				array[i++] = e;
			}
			Shift.this.launchUpdate(array);
		}
	}

	public Shift(){
		super();
	}

	public Shift(ConcernSelector selector, LinkableBase... roots){
		set(selector,roots);
	}

	private Gear gear = new Gear();

	public void set(ConcernSelector selector, LinkableBase... roots){
		gear.set(selector, roots);
	}

	@Override
	protected final void action() {
	}
}
