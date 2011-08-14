package de.lere.vaad.treebuilder.events;

import java.util.ArrayList;

import algoanim.primitives.generators.Language;
import de.lere.vaad.animation.DefaultBinaryTreeAnimations;
import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.GraphWriterImpl;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;

/**
 * provides the animations for a model and it's changes
 * 
 * @author Leo Roos, Rene Hertling
 * 
 * @param <T>
 */
public class TreeEventListenerAggregator<T extends Comparable<T>> implements
		TreeEventListener<T> {
	public static final BinaryTreeLayout DEFAULT_LAYOUT = BinaryTreeLayout.DEFAULT;

	private BinaryTreeModel<T> model;
	private BinaryTreeLayout layout;
	private Language language;

	private ArrayList<TreeEventListener<T>> animations = new ArrayList<TreeEventListener<T>>();

	private DefaultBinaryTreeAnimations<T> defaultAnims;

	public TreeEventListenerAggregator(Language lang) {
		this.language = lang;
		this.model = new BinaryTreeModel<T>();
		this.layout = DEFAULT_LAYOUT;
		GraphWriter<T> defaultWriter = new GraphWriterImpl<T>(language, layout);
		this.defaultAnims = new DefaultBinaryTreeAnimations<T>(defaultWriter);
		this.animations.add(defaultAnims);
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
		for (TreeEventListener<T> animation : animations) {
			animation.update(event);
		}
	}

	public boolean addAnimatior(TreeEventListener<T> animator) {
		if (this.animations.contains(defaultAnims)) {
			this.animations.remove(defaultAnims);
		}
		boolean add = animations.add(animator);
		return add;
	}

	public boolean removeAnimatior(TreeEventListener<T> animator) {
		boolean remove = animations.remove(animator);
		if (animations.size() < 1)
			this.animations.add(defaultAnims);
		return remove;
	}

}
