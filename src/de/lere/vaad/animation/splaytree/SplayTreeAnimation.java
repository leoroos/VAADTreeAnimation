package de.lere.vaad.animation.splaytree;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.animation.DefaultVisibilityEventListener;
import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.GraphWriterImpl;
import de.lere.vaad.animation.SourceCodeWriter;
import de.lere.vaad.animation.StepWriter;
import de.lere.vaad.animation.Timings;
import de.lere.vaad.animation.TreeAnimationBase;
import de.lere.vaad.animation.binarysearchtree.BinaryTreeSetup;
import de.lere.vaad.animation.locationhandler.LocationDirector;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.events.TreeDeleteEvent;
import de.lere.vaad.treebuilder.events.TreeInsertEvent;
import de.lere.vaad.treebuilder.events.TreeModelChangeEventListenerForSplaytreeAnimations;
import de.lere.vaad.treebuilder.events.TreeSearchEvent;
import de.lere.vaad.utils.TextLoaderUtil;

public class SplayTreeAnimation<T extends Comparable<T>> extends
		TreeAnimationBase<T> implements SplayTreeStepListener {

	private TextLoaderUtil loaderUtil;
	private boolean isDeleting;

	private TextLoaderUtil getLoaderUtil() {
		if (loaderUtil == null) {
			loaderUtil = new TextLoaderUtil(getClass(),
					"resources/descriptions");

		}
		return loaderUtil;
	}

	public SplayTreeAnimation(Language l, BinaryTreeProperties tp, T[] initial) {
		super(l, tp, initial);
	}

	/**
	 * Container Object for properties of this Animation
	 */
	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height

		BinaryTreeProperties tp = new BinaryTreeProperties();

		tp.getGraphProperties().set("fillColor", Color.WHITE);
		tp.getGraphProperties().set("highlightColor", Color.RED);
		tp.getSourceCodeProperties().set("highlightColor", Color.CYAN);

		tp.authors = "Rene Hertling, Leo Roos";

		tp.title = "Splaytree Animation";

		Language l = new AnimalScript(tp.title, tp.authors,
				tp.screenResolution.width, tp.screenResolution.height);
		SplayTreeAnimation<Integer> animation = new SplayTreeAnimation<Integer>(
				l, tp,
				new Integer[] { 20, 2, 14, 6, 1, 4, 23, 345, 34, 90, 12 });
		animation.buildAnimation();

		String animationCode = l.getAnimationCode();

		System.out.println(animationCode);

		File file = new File("/tmp/splayTreeAnimation.asu");
		try {
			FileUtils.writeStringToFile(file, animationCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BinaryTreeSetup<String> createStaticSetup(GraphWriter<String> writer) {
		BinaryTreeSetup<String> setup = new BinaryTreeSetup<String>();
		setup.setBinaryTreeProperties(getBinaryTreeProperties());
		setup.setLang(language);
		setup.setStepWriter(new StepWriter(language));
		setup.setWriter(writer);
		return setup;
	}

	private BinaryTreeSetup<T> createSetup(GraphWriter<T> writer) {
		BinaryTreeSetup<T> setup = new BinaryTreeSetup<T>();
		setup.setBinaryTreeProperties(getBinaryTreeProperties());
		setup.setLang(language);
		setup.setStepWriter(new StepWriter(language));
		setup.setWriter(writer);
		return setup;
	}

	@Override
	protected void doBuildAnimation() {

		BinaryTreeSetup<String> setup = performStaticPresentation();
		setup.getStepWriter().step("GenerischerTeil");
		animateGenericPart();
	}

	private void animateGenericPart() {
		GraphWriter<T> gWriter = new GraphWriterImpl<T>(language, this.layout);
		BinaryTreeSetup<T> setup2 = createSetup(gWriter);
		SplayTreeModel<T> model = new SplayTreeModel<T>();
		DefaultVisibilityEventListener<T> visibilityAnimator = new DefaultVisibilityEventListener<T>(
				gWriter);
		SplayTreeModelChangeListener<T> changeListener = new SplayTreeModelChangeListener<T>(
				setup2);
		if (hasElements(initialTree)) {
			for (T t : initialTree) {
				model.insert(t);
			}
		}
		model.addListener(visibilityAnimator);
		model.addListener(changeListener);
		changeListener.add(this);

		if (hasElements(insertionArray)) {
			setup2.getStepWriter().step("Einfügen");
			nextStateOnLocation("Es werden nun Knoten eingefügt.",
					DIRECTOR_MACROSTEP);
			step();
			model.show();
			step();
			for (T t : insertionArray) {
				model.insert(t);
			}
		}
		if (hasElements(searchArray)) {
			setup2.getStepWriter().step("Suchen");
			nextStateOnLocation("Es wird nun nach Knoten gesucht.",
					DIRECTOR_MACROSTEP);
			step();
			model.show();
			step();
			for (T t : searchArray) {
				model.search(t);
			}
		}
		if (hasElements(deleteArray)) {
			setup2.getStepWriter().step("Löschen");
			nextStateOnLocation("Es werden nun Knoten aus dem Baum entfernt",
					DIRECTOR_MACROSTEP);
			step();
			model.show();
			step();
			for (T t : deleteArray) {
				model.delete(t);
			}
		}
	}

	private BinaryTreeSetup<String> performStaticPresentation() {
		SourceCodeWriter scw = new SourceCodeWriter(language,
				getBinaryTreeProperties().getSourceCodeProperties(),
				DIRECTOR_SMALLISH_SOURCECODE, new Timings());

		scw.newSourceCode("1. Zig Step\n" + "2. Zig-zig Step\n"
				+ "3. Zig-zag Step");
		scw.setAutomaticLineUnhighlighting(true);
		scw.highlight(1);

		//
		// Zig-Step
		//
		String actionZigMacroDescription = "Wenn p die Wurzel ist wird der Zig-Step ausgeführt. \n"
				+ "Dieser Schritt wird nur ausgeführt wenn der Baum eine\n"
				+ "ungerade Knotenanzahl hat und es sich um die letzte\n"
				+ "Splay-Operation einer Transformation handelt.";
		nextStateOnLocation(actionZigMacroDescription, DIRECTOR_MACROSTEP);

		final GraphWriter<String> writer = new GraphWriterImpl<String>(
				language, layout);
		BinaryTreeSetup<String> setup = createStaticSetup(writer);

		BinaryTreeModel<String> model = BinaryTreeModel
				.createTreeByInsert("P,F,R,A,G".split(","));

		DefaultVisibilityEventListener<String> visibilityAnimator = new DefaultVisibilityEventListener<String>(
				writer);
		TreeModelChangeEventListenerForSplaytreeAnimations<String> changeAnimator = new TreeModelChangeEventListenerForSplaytreeAnimations<String>(
				setup);
		model.addListener(changeAnimator);
		model.addListener(visibilityAnimator);
		model.show();

		step();

		nextStateOnLocation("Führe Rechtsrotation um P aus.",
				DIRECTOR_MACROSTEP);

		step();

		rightRotateAround(model, "P");

		//
		// ZigZig-Step
		//

		hideAll(model);
		scw.highlight(2);
		String actionZigzigMacroDescription = "Es wird C gesucht.\n"
				+ "Ist der gesuchte Knoten der linke Sohn seines Vaters der wiederum ein linkes Kind ist,\n"
				+ "wird der Zig-zig Schritt ausgeführt, der eine doppelte Rechtsrotation ist.\n"
				+ "(bzw. doppelte Linksrotation im Spiegelverekehrten Fall)";
		nextStateOnLocation(actionZigzigMacroDescription, DIRECTOR_MACROSTEP);

		model = BinaryTreeModel.createTreeByInsert("P,G,R,C,H,A,B".split(","));
		model.addListener(changeAnimator);
		model.addListener(visibilityAnimator);
		model.show();
		step();

		nextStateOnLocation("Führe Rechtsrotation um g aus.",
				DIRECTOR_MICROSTEP);

		de.lere.vaad.treebuilder.Node<String> g = model.search("G");
		step();
		model.rightRotate(g);

		nextStateOnLocation("Führe Rechtsrotation um p aus.",
				DIRECTOR_MICROSTEP);
		step();
		rightRotateAround(model, "P");

		//
		// ZigZag-Step
		//
		hideAll(model);
		scw.highlight(3);
		String actionZigzagMacroDescription = "Sei I gesucht.\n"
				+ "I befindet sich links von P und recht von G bzw.\n"
				+ "Hier wird ein Zig-zag Schritt ausgeführt.\n";
		nextStateOnLocation(actionZigzagMacroDescription, DIRECTOR_MACROSTEP);
		model = BinaryTreeModel.createTreeByInsert("P,G,S,E,I,H,J".split(","));
		model.addListener(changeAnimator);
		model.addListener(visibilityAnimator);
		model.show();
		step();

		nextStateOnLocation("Führe Linksrotation um G aus.", DIRECTOR_MICROSTEP);
		step();
		leftRotateAround(model, "G");

		nextStateOnLocation("Führe Rechtsrotation um P aus.",
				DIRECTOR_MICROSTEP);
		step();
		rightRotateAround(model, "P");

		//
		// Description of Behavior for Access Operations
		//
		hideAll(model);
		scw.unhighlight(3);
		scw.hide();
		nextStateOnLocation("", DIRECTOR_DESCRIPTION_BEGINNING);
		String accessBehaviourMacroDescription = "Verhalten bei zugreifenden Operationen	\n\n"
				+ "Suche:\n"
				+ "Wird ein Knoten gesucht und gefunden, so wird auf diesem eine Splay-Operation ausgeführt.\n"
				+ "Einfügen:\n"
				+ "Beim Einfügen wird der Knoten wie in einen Binärbaum hinzugefügt und\n"
				+ "anschließend wird der eingefügte gesplayed\n"
				+ "Löschen:\n"
				+ "Beim Löschen wird der zu löschende Knoten wie in einem Binärbaum gelöscht\n"
				+ "und dann der Parent des gelöschten Knotens gesplayed.";
		nextStateOnLocation(accessBehaviourMacroDescription, DIRECTOR_MACROSTEP);

		step();
		//
		// Showcase the operations
		//
		String showcaseMacroDescription = "Es folgt nun eine beispielhafte Darstellung der Operationen.";
		nextStateOnLocation(showcaseMacroDescription, DIRECTOR_MACROSTEP);
		step();
		nextStateOnLocation("Suche nach einem Knoten:", DIRECTOR_MACROSTEP);
		step();
		BinaryTreeModel<String> btree = BinaryTreeModel
				.createTreeByInsert("10,5,20,1,7,15,25".split(","));
		SplayTreeModel<String> splay = SplayTreeModel.from(btree);
		SplayTreeModelChangeListener<String> changeListener = new SplayTreeModelChangeListener<String>(
				setup);
		splay.addListener(visibilityAnimator);
		splay.addListener(changeListener);
		changeListener.add(this);
		splay.show();
		step();
		splay.search("25");
		nextStateOnLocation("Einfügen eines Knotens:", DIRECTOR_MACROSTEP);
		step();
		splay.insert("40");
		step();
		nextStateOnLocation("Löschen eines Knotens:", DIRECTOR_MACROSTEP);
		step();
		splay.delete("10");
		step();
		hideAll(splay);
		return setup;
	}

	private void rightRotateAround(BinaryTreeModel<String> model, String v) {
		de.lere.vaad.treebuilder.Node<String> p = model.search(v);
		model.rightRotate(p);
	}

	private void leftRotateAround(BinaryTreeModel<String> model, String v) {
		de.lere.vaad.treebuilder.Node<String> p = model.search(v);
		model.leftRotate(p);
	}

	private <T extends Node> void nextStateOnLocation(String string,
			LocationDirector<T> director) {
		lh.nextStateOnLocation(string, director);
	}

	@Override
	public void newStep(SplayTreeEvent step) {
		SplayTreeEvent<?> cause = step;
		if (cause instanceof ZigStartedEvent) {
			nextStateOnLocation(
					"Der Vater des zu splayenden Knotens ist die Wurzel des Baumes. Daher wird als nächstes ein Zig-Step durchgeführt.",
					DIRECTOR_MICROSTEP);
		} else if (cause instanceof ZigZigStartedEvent) {
			nextStateOnLocation(
					"Der zu splayende Knoten ist das rechte/linke Kind seines Vaters,\n der wiederrum rechtes/linkes Kind seines Vaters ist.\n Es wird daher ein ZigZig-Step durchgeführt.",
					DIRECTOR_MICROSTEP);
		} else if (cause instanceof ZigZagStartedEvent) {
			nextStateOnLocation(
					"Der zu splayende Knoten ist das rechte/linke Kind seines Vaters,\n der selbst allerdings linkes/rechtes Kind seines Vaters ist.\n Es wird daher ein ZigZag-Step durchgeführt.",
					DIRECTOR_MICROSTEP);
		} else {
			throw new IllegalArgumentException("Invalid Splay-Event");
		}
		step();
	}

	@Override
	public void nodeFound(TreeSearchEvent event) {
		nextStateOnLocation("Suche nach Knoten mit dem Wert: "
				+ event.searchVal, DIRECTOR_MACROSTEP);
		isDeleting = false;
		step();
	}

	@Override
	public void nodeDelete(TreeDeleteEvent event) {
		nextStateOnLocation("Lösche Knoten mit dem Wert:" + event.deleteValue,
				DIRECTOR_MACROSTEP);
		isDeleting = true;
		step();
	}

	@Override
	public void nodeInserted(TreeInsertEvent event) {
		nextStateOnLocation("Füge Knoten mit dem Wert <"
				+ event.nodeOfModification.getValue() + "> ein.",
				DIRECTOR_MACROSTEP);
		isDeleting = false;
		step();
	}

	@Override
	protected String getAnimationTitle() {
		return getBinaryTreeProperties().title;
	}

	@Override
	protected String getInitialDescription() {
		return getLoaderUtil().getText("intro");
	}

	@Override
	public void newOperation(SplayStartedEvent operation) {
		if (isDeleting) {
			nextStateOnLocation(
					"Der Vaterknoten des gelöschten Knoten wird nun gesplayed",
					DIRECTOR_MICROSTEP);
		} else {
			nextStateOnLocation("Der Knoten wird nun an die Wurzel gesplayed.",
					DIRECTOR_MICROSTEP);
		}
		step();
	}

	@Override
	public void operationEnded(SplayEndedEvent operation) {
		nextStateOnLocation(
				"Der Knoten befindet sich nun an der Wurzel. Der Splay-Vorgang ist beendet.",
				DIRECTOR_MICROSTEP);
		step();
		nextStateOnLocation("", DIRECTOR_MICROSTEP);
		nextStateOnLocation("", DIRECTOR_MACROSTEP);
		step();
	}
}
