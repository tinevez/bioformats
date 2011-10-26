package ome.scifio;

public abstract class AbstractChecker<M extends Metadata> implements Checker<M> {

	public int compareTo(Checker<?> c) {
		return this.getPriority().compareTo(c.getPriority());
	}
}
