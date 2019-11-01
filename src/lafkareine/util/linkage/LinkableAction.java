
package lafkareine.util.linkage;


public class LinkableAction extends Active {
	
	private Action action;

	public interface NoArgAction extends Action{
		default void act(LinkableAction me){
			run();
		}

		void run();
	}

	public interface Action{

		void act(LinkableAction me);
	}

	public interface ArgAction extends  Action{}

	public LinkableAction(NoArgAction action) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(action, inferConcerns(action));
	}
	
	public LinkableAction(NoArgAction action, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		p_set(action, concerns);
	}

	public LinkableAction(ArgAction action) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(action, inferConcerns(action));
	}

	public LinkableAction(ArgAction action, LinkableBase... concerns) {
		// TODO 自動生成されたコンストラクター・スタブ
		p_set(action, concerns);
	}
	
	public void set(NoArgAction action) {
		p_set(action, inferConcerns(action));
	}
	
	public void set(NoArgAction action, LinkableBase... concerns) {
		// TODO 自動生成されたメソッド・スタブ
		p_set(action, concerns);
	}

	private void p_set(Action action, LinkableBase[] concerns){
		this.action = action;
		launchUpdate(concerns);

	}

	public void set(ArgAction action) {
		p_set(action, inferConcerns(action));
	}

	public void set(ArgAction action, LinkableBase... concerns) {
		// TODO 自動生成されたメソッド・スタブ
		p_set(action, concerns);
	}
	
	public LinkableAction() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
	protected void action() {
		// TODO 自動生成されたメソッド・スタブ
		action.act(this);
		runReactor();
	}
}
