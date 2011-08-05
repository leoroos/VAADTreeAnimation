package de.lere.vaad.treebuilder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import de.lere.vaad.treebuilder.BinaryTreeModel.Edge;

/**
 * A Edge class that serves as transformation from {@link Node}s to integer position based edge descriptions
 * 
 * @author leo
 *
 */
public class PositionEdge {
	public PositionEdge(Edge<?> n) {
		this.parent = n.parentPos.getPosition();
		this.child = n.childPos.getPosition();
	}

	public PositionEdge(int parent, int child) {
		this.parent = parent;
		this.child = child;
	}

	int parent;
	int child;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		PositionEdge rhs = (PositionEdge) obj;
		return new EqualsBuilder().append(parent, rhs.parent)
				.append(child, rhs.child).isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(29, 83).append(parent).append(child).toHashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.SHORT_PREFIX_STYLE).append("parent",parent).append("child",child).toString();
	}
}