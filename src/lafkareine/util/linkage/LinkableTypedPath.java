package lafkareine.util.linkage;

public class LinkableTypedPath<T, U> extends LinkablePath<ReadOnlyLinkable<T>, ReadOnlyLinkable<U>>{

	public interface Getter<T,U> extends LinkablePath.Getter<ReadOnlyLinkable<T>, ReadOnlyLinkable<U>>{
		@Override
		 default ReadOnlyLinkable<U> get(ReadOnlyLinkable<T> from){
			return rip(from.get());
		};

		Linkable<U> rip(T from);
	}

	public LinkableTypedPath(ReadOnlyLinkable<T> from, Getter<T,U> getter){
		super(from, getter);
	}

	public U get() {
		return super.unwrap().get();
	}
}
