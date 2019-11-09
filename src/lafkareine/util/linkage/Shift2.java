package lafkareine.util.linkage;

public abstract class Shift2 extends LinkableBase{

	public interface ConcernSetter{
		void setConcerns(LinkableBase... concerns);
	}

	private class Gear extends LinkableBase implements ConcernSetter{
		@Override
		public final void setConcerns(LinkableBase... concerns){
			Shift2.this.launchUpdate(concerns);
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
