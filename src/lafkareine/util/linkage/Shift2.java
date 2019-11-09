package lafkareine.util.linkage;

public abstract class Shift2 extends LinkableBase{

	public interface ConcernSetter{
		void setConcerns(LinkableBase... concerns);
	}

	private class Gear extends LinkableBase implements ConcernSetter{
		@Override
		public final void setConcerns(LinkableBase... concerns){
			LinkableBase[] array = new LinkableBase[concerns.length+1];
			int i = 0;
			array[i++] = this;
			for(var e:concerns){
				array[i++] = e;
			}
			Shift2.this.launchUpdate(array);
		}

		@Override
		protected void action() {
			selectConcerns(this);
		}
	}

	private final Gear gear = new Gear();

	public Shift2(LinkableBase... roots){
		gear.launchUpdate(roots);
	}

	@Override
	protected void action() {
	}

	protected abstract void selectConcerns(ConcernSetter setter);
}
