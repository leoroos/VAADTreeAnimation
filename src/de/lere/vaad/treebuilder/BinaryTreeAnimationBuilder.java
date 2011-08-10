package de.lere.vaad.treebuilder;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import de.lere.vaad.EndOfTheWorldException;

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

	@Override
	public void update(TreeSearchEvent<T> event) {
		throw new EndOfTheWorldException("Won't handle this event");
	}
}
