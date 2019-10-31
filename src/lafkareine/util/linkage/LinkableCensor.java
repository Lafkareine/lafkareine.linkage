
package lafkareine.util.linkage;


public class LinkableCensor extends LinkableBase{
	
	private boolean isActed = false;
	
	public LinkableCensor(LinkableBase... target) {
		// TODO 自動生成されたコンストラクター・スタブ
		launchUpdate(target);
	}
	
	public void setTarget(LinkableBase... target) {
		isActed = true;
		launchUpdate(target);
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
