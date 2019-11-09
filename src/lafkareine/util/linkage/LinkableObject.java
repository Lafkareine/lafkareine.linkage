package lafkareine.util.linkage;

public abstract class LinkableObject extends LinkableBase{

	public LinkableObject(LinkableBase... concerns){
		super();
		setConcerns(concerns);
	}

	public LinkableObject(){
		super();
	}

	public final void setConcerns(LinkableBase... concerns){
		launchAction(concerns);
	}
}
