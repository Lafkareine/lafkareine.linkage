
package lafkareine.util.linkage;


public class LinkableTrigger extends Active{

	public LinkableTrigger() {
		// TODO 自動生成されたコンストラクター・スタブ
		super();
		launchUpdate();
	}

	public LinkableTrigger(LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		super();
		launchUpdate(concerns);
	}

	@Override
	protected void action() {
		// TODO 自動生成されたメソッド・スタブ
		runReactor();
	}

	public final void start(){
		launchUpdate();
		runReactor();
	}
}
