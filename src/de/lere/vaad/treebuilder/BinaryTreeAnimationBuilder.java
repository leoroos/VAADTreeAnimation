package de.lere.vaad.treebuilder;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * provides the animations for a model and it's changes
 * 
 * @author Leo Roos, Rene Hertling
 * 
 * @param <T>
 */
public class BinaryTreeAnimationBuilder<T extends Comparable<T>> implements
		BinaryTreeModelListener<T> {
	public static final BinaryTreeLayout DEFAULT_LAYOUT = new BinaryTreeLayout(
			new Point(240, 0), 120, 30, Color.WHITE, "DefaultGraphName");

	private BinaryTreeModel<T> model;
	private BinaryTreeLayout layout;
	private Language language;

	private NewTreeAnimator<T> newTreeAnimatror;
	private TreeInsertOperationAnimator<T> insertAnimator;
	private TreeDeleteOperationAnimator<T> deleteAnimator;
	private TreeLeftRotationAnimator<T> leftRotationAnimator;
	private TreeRightRotationAnimator<T> rightRotationAnimator;	
	
	//public final DefaultBinaryTreeAnimations<T> defaultAnimations = new DefaultBinaryTreeAnimations<T>(new GraphWriterImpl<T>());
	public final ExtractedBinaryTreeAnimations<T> defaultAnimations = new ExtractedBinaryTreeAnimations<T>(new GraphWriterImpl<T>());

	public BinaryTreeAnimationBuilder(Language lang) {
		this.language = lang;
		this.model = new BinaryTreeModel<T>();
		this.layout = DEFAULT_LAYOUT;		
		this.newTreeAnimatror = defaultAnimations;
		this.insertAnimator = defaultAnimations;
		this.deleteAnimator = defaultAnimations;
		this.leftRotationAnimator = defaultAnimations;
		this.rightRotationAnimator = defaultAnimations;
	}

	public void setModel(BinaryTreeModel<T> model) {
		BinaryTreeModel<T> old = this.model.copy();
		if (this.model != null) {
			this.model.removeListener(this);
		}
		this.model = model;
		this.model.addListener(this);
		this.newTreeAnimatror.animate(language, model, this.layout);
	}

	public void setLayout(BinaryTreeLayout layout) {
		this.layout = layout;
	}

	public BinaryTreeLayout getLayout() {
		return layout;
	}
	
	/**
	 * <pre>
	 * 	#remove edge to graph
	 * 	highlightEdge on  "Zig" (1,4) 
	 * 	hide "Zig" after 75 ticks	
	 * 	#new graph without edge to moved node	
	 * 	graph "Zig" size 5 highlightColor red nodes { "P" offset (0,0) from "loc1" , "X" offset (0,0) from "loc2" , "C" offset (0,0) from "loc3", "A" offset (0,0) from "loc4" , "B" offset (0,0) from "loc5" } edges { ( 0, 1) ( 0, 2) ( 1 , 3 ) }
	 * }
	 * 
	 * {	
	 * 	move "Zig" type "translate #1" via "vec1_3" within 150 ticks
	 * 	move "Zig" type "translate #2" via "vec2_1" within 150 ticks
	 * 	move "Zig" type "translate #3" via "vec3_7" within 150 ticks
	 * 	move "Zig" type "translate #4" via "vec4_2" within 150 ticks
	 * 	move "Zig" type "translate #5" via "vec5_6" within 150 ticks			
	 * }
	 * 
	 * {
	 * 	hide "Zig"		
	 * 	graph "Zig" size 5 highlightColor red nodes { "X" offset (0,0) from "loc1" ,  "A" offset (0,0) from "loc2" , "P" offset (0,0) from "loc3" , "B" offset (0,0) from "loc6",  "C" offset (0,0) from "loc7" } edges { ( 0, 1) ( 0, 2) ( 2 , 3 ) (2,4) }
	 * 	highlightEdge on "Zig" (2,3)
	 * 	unhighlightEdge on "Zig" (2,3) after 75 ticks
	 * </pre>
	 * */

	@Override
	public void update(TreeInsertEvent<T> event) {
		insertAnimator.animate(language, event, layout);
	}

	@Override
	public void update(TreeDeleteEvent<T> event) {		
		deleteAnimator.animate(language, event, layout);
	}	

	@Override
	public void update(TreeLeftRotateEvent<T> event) {
		leftRotationAnimator.animate(language, event, layout);		
	}

	@Override
	public void update(TreeRightRotateEvent<T> event) {
		rightRotationAnimator.animate(language, event, layout);
	}
}
