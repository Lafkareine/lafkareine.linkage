
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
		this(action, inferInputs(action));
	}
	
	public LinkableAction(NoArgAction action, LinkableBase... inputs) {
		// TODO 自動生成されたコンストラクター・スタブ
		p_set(action, inputs);
	}

	public LinkableAction(ArgAction action) {
		// TODO 自動生成されたコンストラクター・スタブ
		this(action, inferInputs(action));
	}

	public LinkableAction(ArgAction action, LinkableBase... inputs) {
		// TODO 自動生成されたコンストラクター・スタブ
		p_set(action, inputs);
	}
	
	public void set(NoArgAction action) {
		p_set(action, inferInputs(action));
	}
	
	public void set(NoArgAction action, LinkableBase... inputs) {
		// TODO 自動生成されたメソッド・スタブ
		p_set(action, inputs);
	}

	private void p_set(Action action, LinkableBase[] inputs){
		this.action = action;
		launchUpdate(inputs);

	}

	public void set(ArgAction action) {
		p_set(action, inferInputs(action));
	}

	public void set(ArgAction action, LinkableBase... inputs) {
		// TODO 自動生成されたメソッド・スタブ
		p_set(action, inputs);
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
