
package lafkareine.util.linkage;


public class LinkableSensor extends LinkableBase{
	
	private boolean isActed = true;

	public LinkableSensor(boolean init, LinkableBase... concern) {
		// TODO 自動生成されたコンストラクター・スタブ
		isActed = init;
		launchUpdate(concern);
	}

	public LinkableSensor(LinkableBase... concern) {
		// TODO 自動生成されたコンストラクター・スタブ
		launchUpdate(concern);
	}

	public LinkableSensor(){}


	public void setTarget(boolean init, LinkableBase... concern) {
		isActed = init;
		launchUpdate(concern);
	}

	public void setTarget(LinkableBase... concern) {
		isActed = true;
		launchUpdate(concern);
	}
	
	@Override
	protected void action() {
		// TODO 自動生成されたメソッド・スタブ
		isActed = true;
	}
	
	public boolean get() {
		// TODO 自動生成されたメソッド・スタブ
		boolean b = isActed;
		isActed = !b;
		return b;
	}
}
