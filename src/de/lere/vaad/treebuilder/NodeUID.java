package de.lere.vaad.treebuilder;

public final class NodeUID {

	private static long uidcounter = 0;
	private long uid;
	
	public NodeUID() {
		this.uid = uidcounter++;
	}
	
	public long getUid() {
		return uid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NodeUID)
			return ((NodeUID) obj).getUid() == this.uid;
		return false;
	}
	
	@Override
	public int hashCode() {
		return 11 * (int)this.uid;
	}
	
	@Override
	public String toString() {
		return Long.toString(getUid());
	}
	
}
