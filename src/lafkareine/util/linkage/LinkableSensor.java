
package lafkareine.util.linkage;


public class LinkableSensor extends LinkableBase{
	
	private boolean isActed = false;
	
	public LinkableSensor(LinkableBase... concern) {
		// TODO 自動生成されたコンストラクター・スタブ
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
		return isActed;
	}
}
