package de.lere.vaad.animation.binarysearchtree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.animation.DefaultVisibilityEventListener;
import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.GraphWriterImpl;
import de.lere.vaad.animation.SourceCodeWriter;
import de.lere.vaad.animation.Timings;
import de.lere.vaad.animation.TreeAnimationBase;
import de.lere.vaad.animation.locationhandler.LocationDirector;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.events.DefaultTreeModelChangeEventListener;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.utils.TextLoaderUtil;

public class BinarySearchTreeAnimation<T extends Comparable<T>> extends
		TreeAnimationBase<T> {

	public static void main(String[] args) {

		BinaryTreeProperties tps = new BinaryTreeProperties();
		tps.authors = "Leo Roos, Rene Hertling";
		tps.title = "Binary Search Tree";

		AnimalScript animalScript = new AnimalScript(tps.authors, tps.title,
				tps.screenResolution.width, tps.screenResolution.height);

		BinarySearchTreeAnimation<Integer> binarySerachTreeAnimation = new BinarySearchTreeAnimation<Integer>(
				animalScript, tps, new Integer[] { 20, 2, 14, 6, 1, 4, 23, 345,
						34, 90, 12 });
		binarySerachTreeAnimation.buildAnimation();

		String animationCode = animalScript.getAnimationCode();
		// System.out.println(animationCode);
		try {
			FileUtils.writeStringToFile(new File("/tmp/animation.asu"),
					animationCode);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	TextLoaderUtil textLoader;
	private Timings timings;

	public BinarySearchTreeAnimation(Language l, BinaryTreeProperties btp,
			T[] initialTree) {
		super(l, btp, initialTree);
		this.timings = new Timings();

	}

	/**
	 * Builds the animation according to configuration
	 */
	@Override
	public void doBuildAnimation() {

		final BinaryTreeModel<T> model = BinaryTreeModel
				.createTreeByInsert(initialTree);

		GraphWriter<T> writer = new GraphWriterImpl<T>(language, layout);
		SourceCodeWriter sourceCodeWriter = new SourceCodeWriter(language,
				getBinaryTreeProperties().getSourceCodeProperties(),
				DIRECTOR_SMALLISH_SOURCECODE, timings);

		/* default animations for tree changes */
		model.addListener(new DefaultTreeModelChangeEventListener<T>(
				createBinaryTreeSetup(writer, sourceCodeWriter)));
		model.addListener(new DefaultVisibilityEventListener<T>(writer));

		if (hasElements(searchArray)) {
			Mission<T> searchMission = new Mission<T>() {
				@Override
				public void accomplish(T arg) {
					model.search(arg);
				}
			};
			equipModelAccomplishMissionReturnTraceless(model, searchMission,
					searchArray, new TreeSearchAnimation<T>(
							createBinaryTreeSetup(writer, sourceCodeWriter)));

		}

		if (hasElements(this.insertionArray)) {

			Mission<T> insertionCmd = new Mission<T>() {
				@Override
				public void accomplish(T arg) {
					model.insert(arg);
				}
			};

			equipModelAccomplishMissionReturnTraceless(model, insertionCmd,
					this.insertionArray, new TreeInsertionAnimation<T>(
							createBinaryTreeSetup(writer, sourceCodeWriter)));

		}

		if (hasElements(deleteArray)) {
			nextStateOnLocation(
					"Beim löschen wird vorgeganen wie bei einem gewöhnlichen\n"
							+ "Binärbaum. Dabei werden drei Fälle unterschieden:\n"
							+ "1. Der zu löschende Knoten hat keine Kinder\n"
							+ "2. Der zu löschende Knoten hat ein Kind\n"
							+ "3. Der zu löschende Knoten hat zwei Kinder",
					DIRECTOR_DESCRIPTION_BEGINNING);
			step();
			hideAllDescriptions();

			SourceCodeWriter deleteSCW = new SourceCodeWriter(language,
					getBinaryTreeProperties().getSourceCodeProperties(),
					DIRECTOR_LONGER_SOURCECODE, timings);
			Mission<T> deleteMission = new Mission<T>() {
				@Override
				public void accomplish(T arg) {
					model.delete(arg);
				}
			};
			equipModelAccomplishMissionReturnTraceless(model, deleteMission,
					deleteArray, new TreeDeleteAnimation<T>(
							createBinaryTreeSetup(writer, deleteSCW)));

		}

	}

	@Override
	protected String getAnimationTitle() {
		return getBinaryTreeProperties().title;
	}

	private void equipModelAccomplishMissionReturnTraceless(
			BinaryTreeModel<T> model,
			de.lere.vaad.animation.TreeAnimationBase.Mission<T> mission,
			T[] targetArray, TreeEventListener<T> targetAnimations) {
		ArrayList<de.lere.vaad.animation.TreeAnimationBase.Mission<T>> missions = new ArrayList<Mission<T>>();
		missions.add(mission);
		equipModelAccomplishMissionReturnTraceless(model, missions,
				targetArray, targetAnimations);
	}

	Group createTextGroup(String string, Node location) {
		return this.lh.createTextGroup(string, location);
	}

	@Override
	protected String getInitialDescription() {
		this.textLoader = new TextLoaderUtil(getClass(), "resources");
		String initialDescription = textLoader
				.getText("initialDescription.txt");
		return initialDescription;
	}

	private <U extends Node> void nextStateOnLocation(String string,
			LocationDirector<U> director) {
		lh.nextStateOnLocation(string, director);
	}

}
