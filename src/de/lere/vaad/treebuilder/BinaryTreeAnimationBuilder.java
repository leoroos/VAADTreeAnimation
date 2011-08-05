package de.lere.vaad.treebuilder;

import java.util.List;

import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import de.lere.vaad.CorrectedOffset;

public class BinaryTreeAnimationBuilder<T extends Comparable<T>> {
	public static class BinaryTreeLayout {
		
		public BinaryTreeLayout(algoanim.util.Node location, int firstLevelWidth, int verticalGaps) {
			this.rootLocation = location;
			this.firstLevelWidth = firstLevelWidth;
			this.verticalGaps = verticalGaps;
		}
		
		public BinaryTreeLayout(BinaryTreeLayout layout) {
			this.rootLocation = layout.rootLocation;
			this.firstLevelWidth = layout.firstLevelWidth;
			this.verticalGaps = layout.verticalGaps;
		}

		algoanim.util.Node rootLocation;
		int firstLevelWidth;
		int verticalGaps;	
	}
	
	public static final BinaryTreeLayout DEFAULT_LAYOUT = new BinaryTreeLayout(CorrectedOffset.getOffsetForCoordinate(new Coordinates(0,0)), 500, 100);
	
	private BinaryTreeModel<T> model;
	private BinaryTreeLayout layout;
	private Language language;
	
	public BinaryTreeAnimationBuilder(Language lang){
		this.language = lang;
		this.model = new BinaryTreeModel<T>();
		this.layout = DEFAULT_LAYOUT;
	}

	//Maybe copy as well
	public void setModel(BinaryTreeModel<?> model) {
//		this.model = model;
	}

	public void setLayout(BinaryTreeLayout layout) {
		this.layout = new BinaryTreeLayout(layout);
	}

	public void buildCurrentGraph() {
		List<Node<T>> nodes = model.getNodes();
		for(Node<T> node : nodes){
			int position = node.getPosition();
		}
	}
	
	private class Edge{
		public Edge(Node parentPos, Node childPos) {
			ParentPos = parentPos;
			ChildPos = childPos;
		}
		private final Node ParentPos;
		private final Node ChildPos;
	}
}
