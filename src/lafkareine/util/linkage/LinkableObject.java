package lafkareine.util.linkage;

public abstract class LinkableObject extends LinkableBase{

	public LinkableObject(LinkableBase... concerns){
		super();
		action();
		launchUpdate(concerns);
	}
}
