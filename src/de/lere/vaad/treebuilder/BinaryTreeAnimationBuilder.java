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
		TreeEventListener<T> {
	public static final BinaryTreeLayout DEFAULT_LAYOUT = new BinaryTreeLayout(
			new Point(240, 0), 120, 30, Color.WHITE, "DefaultGraphName");

	private BinaryTreeModel<T> model;
	private BinaryTreeLayout layout;
	private Language language;

	private TreeAnimator<T> animations;

	public BinaryTreeAnimationBuilder(Language lang) {
		this.language = lang;
		this.model = new BinaryTreeModel<T>();
		this.layout = DEFAULT_LAYOUT;
		// defaultAnimations = new DefaultBinaryTreeAnimations<T>(new
		// GraphWriterImpl<T>());
		this.animations = new ExtractedBinaryTreeAnimations<T>(
				new GraphWriterImpl<T>(language));
	}

	public void setModel(BinaryTreeModel<T> model) {
		if (this.model != null) {
			this.model.removeListener(this);
		}
		this.model = model;
		this.model.addListener(this);
		BinaryTreeModel<T> copy = this.model.copy();
		this.update(new TreeNewEvent<T>(copy, copy, null));
	}

	public void setLayout(BinaryTreeLayout layout) {
		this.layout = layout;
	}

	public BinaryTreeLayout getLayout() {
		return layout;
	}

	@Override
	public void update(TreeEvent<T> event) {
		this.animations.animate(language, event, layout);
	}

}
