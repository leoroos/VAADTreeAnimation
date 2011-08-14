package de.lere.vaad.animation.splaytree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.animation.locationhandler.Action;
import de.lere.vaad.animation.locationhandler.ActionAdapter;
import de.lere.vaad.animation.locationhandler.LocationDirector;
import de.lere.vaad.animation.locationhandler.LocationDirectorProvider;
import de.lere.vaad.animation.locationhandler.LocationHandler;
import de.lere.vaad.animation.locationhandler.LocationProvider;
import de.lere.vaad.animation.locationhandler.NextStateOnLocationDirector;
import de.lere.vaad.animation.splaytree.resources.descriptions.SplayTreeResourceAccessor;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.events.TreeEventListenerAggregator;
import de.lere.vaad.utils.CorrectedOffset;
import de.lere.vaad.utils.NodeHelper;
import de.lere.vaad.utils.StrUtils;
import edu.umd.cs.findbugs.annotations.Nullable;

public class SplayTreeAnimation {

	private static final Point GRAPHROOT_COORDINATES = new Point(400, 300);

	private final Language language;

	private SourceCode sourceCode;
	private final BinaryTreeProperties animationProperties;
	private final BinaryTreeLayout layout;

	private SplayTreeAnimation(Language l, BinaryTreeProperties tp) {
		this.language = l;
		this.animationProperties = tp;
		l.setStepMode(true);
		this.layout = new BinaryTreeLayout(GRAPHROOT_COORDINATES, 200, 60, tp.getGraphProperties());
		this.lh = new LocationHandler(language, tp);

		DIRECTOR_HEADER = createHeaderLocDir();
		DIRECTOR_DESCRIPTION_BEGINNING = createDescriptionBeginningLocDir(DIRECTOR_HEADER);
		DIRECTOR_MACROSTEP = createMacroStepLocDir(DIRECTOR_DESCRIPTION_BEGINNING);
		DIRECTOR_MICROSTEP = createMicroStepLocDir(DIRECTOR_MACROSTEP);
		DIRECTOR_GRAPHROOT = createDirectorGraphroot();
		DIRECTOR_SOURCECODE = createSourceCodeLocDir();
	}

	public final LocationDirector<Offset> DIRECTOR_DESCRIPTION_BEGINNING;

	public final LocationDirector<Coordinates> DIRECTOR_GRAPHROOT;

	public final LocationDirector<Coordinates> DIRECTOR_HEADER;
	public final LocationDirector<Offset> DIRECTOR_MACROSTEP;
	public final LocationDirector<Offset> DIRECTOR_MICROSTEP;
	public final LocationDirector<Coordinates> DIRECTOR_SOURCECODE;

	/**
	 * Container Object for properties of this Animation
	 */
	private BinaryTreeProperties splayProps = new BinaryTreeProperties();

	private final LocationHandler lh;

	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height

		BinaryTreeProperties tp = new BinaryTreeProperties();

		tp.authors = "Rene Hertling, Leo Roos";

		tp.title = "Splaytree Animation";

		Language l = new AnimalScript(tp.title, tp.authors,
				tp.screenResolution.width, tp.screenResolution.height);
		SplayTreeAnimation animation = new SplayTreeAnimation(l, tp);
		try {
			animation.buildAnimation(tp);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		String animationCode = l.getAnimationCode();

		System.out.println(animationCode);

		File file = new File("/tmp/animationCode.asu");
		try {
			FileUtils.writeStringToFile(file, animationCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildAnimation(BinaryTreeProperties props) throws IOException {
		//
		// Intro
		//

		lh.nextStateOnLocation(actionHeaderText(Coordinates.class),
				DIRECTOR_HEADER);

		lh.nextStateOnLocation(SplayTreeResourceAccessor.INTRO.getText(),
				DIRECTOR_DESCRIPTION_BEGINNING);

		step();

		lh.nextStateOnLocation(SplayTreeResourceAccessor.DESCRIPTION.getText(),
				DIRECTOR_DESCRIPTION_BEGINNING);

		lh.nextStateOnLocation(actionSourceCodeText(Offset.class),
				DIRECTOR_DESCRIPTION_BEGINNING);

		step();

		//
		// Zig-Step
		//
		String actionZigMacroDescription = "Wenn p die Wurzel ist wird der Zig-Step ausgeführt. \n"
				+ "Dieser Schritt wird nur ausgeführt wenn der Baum eine\n"
				+ "ungerade Knotenanzahl hat und es sich um die letzte\n"
				+ "Splay-Operation einer Transformation handelt.";
		nextStateOnLocation(actionZigMacroDescription, DIRECTOR_MACROSTEP);

		nextStateOnLocation("Führe Rechtsrotation um P aus.",
				DIRECTOR_MACROSTEP);

		 ;
		BinaryTreeModel<String> model = BinaryTreeModel
				.createTreeByInsert("P,F,R,A,G".split(","));

		model.addListener(new TreeEventListenerAggregator<String>(
				language));
		model.show();
		
		for (int i = 0; i < 20; ++i) {
			if ((i % 2) == 0) {
				model.rightRotate(model.getRoot());
			} else {
				model.leftRotate(model.getRoot());
			}
		}

		step();
		//
		// ZigZig-Step
		//

		hideAll(model);
		String actionZigzigMacroDescription = "Es wird C gesucht.\n"
				+ "Ist der gesuchte Knoten der linke Sohn seines Vaters der wiederum ein linkes Kind ist,\n"
				+ "wird der Zig-zig Schritt ausgeführt, der eine doppelte Rechtsrotation ist.\n"
				+ "(bzw. doppelte Linksrotation im Spiegelverekehrten Fall)";
		nextStateOnLocation(actionZigzigMacroDescription, DIRECTOR_MACROSTEP);

		model = BinaryTreeModel.createTreeByInsert("P,G,R,C,H,A,B".split(","));
		model.show();

		step();

		nextStateOnLocation("Führe Rechtsrotation um g aus.",
				DIRECTOR_MICROSTEP);

		de.lere.vaad.treebuilder.Node<String> g = model.search("G");
		model.rightRotate(g);

		nextStateOnLocation("Führe Rechtsrotation um p aus.",
				DIRECTOR_MICROSTEP);

		rightRotateAround(model, "P");

		//
		// ZigZag-Step
		//
		hideAll(model);
		String actionZigzagMacroDescription = "Sei I gesucht.\n"
				+ "I befindet sich links von P und recht von G bzw.\n"
				+ "Hier wird ein Zig-zag Schritt ausgeführt.\n";
		nextStateOnLocation(actionZigzagMacroDescription, DIRECTOR_MACROSTEP);

		model = BinaryTreeModel.createTreeByInsert("P,G,S,E,I,H,J".split(","));
		model.show();

		step();

		nextStateOnLocation("Führe Linksrotation um G aus.", DIRECTOR_MICROSTEP);
		leftRotateAround(model, "G");

		step();

		nextStateOnLocation("Führe Rechtsrotation um P aus.",
				DIRECTOR_MICROSTEP);
		rightRotateAround(model, "P");

		step();

		//
		// Description of Behavior for Access Operations
		//
		hideAll(model);
		String accessBehaviourMacroDescription = "Verhalten bei zugreifenden Operationen	\n"
				+ "Suche:\n"
				+ "Wird ein Knoten gesucht, wird auf diesem eine Splay Operation ausgeführt.\n"
				+ "Einfügen:\n"
				+ "Beim Einfügen wird der Knoten wie in einen Binärbaum hinzugefügt und\n"
				+ "anschließend der eingefügte Knoten zur Wurzel gesplayed\n"
				+ "Löschen:\n"
				+ "Beim Löschen wird der zu löschende Knoten wie in einem Binärbaum gelöscht\n"
				+ "und sein Parent zur Wurzel gesplayed.";
		nextStateOnLocation(accessBehaviourMacroDescription, DIRECTOR_MACROSTEP);

		step();
		//
		// Showcase the operations
		//
		String showcaseMacroDescription = "Beispielhafte Darstellung der beschriebenen Operationen.";
		nextStateOnLocation(showcaseMacroDescription, DIRECTOR_MACROSTEP);

		nextStateOnLocation("Suche nach einen Schlüssel $$", DIRECTOR_MICROSTEP);

		step();

		nextStateOnLocation("Einfügen des Schlüssels $$", DIRECTOR_MICROSTEP);

		step();

		nextStateOnLocation("Löschen eines Schlüssels $$", DIRECTOR_MICROSTEP);

		step();
	}

	private void hideAll(BinaryTreeModel<String> model) {
		nextStateOnLocation("", DIRECTOR_DESCRIPTION_BEGINNING);
		nextStateOnLocation("", DIRECTOR_MACROSTEP);
		nextStateOnLocation("", DIRECTOR_MICROSTEP);
		if (model != null)
			model.hide();
	}

	private void rightRotateAround(BinaryTreeModel<String> model, String v) {
		de.lere.vaad.treebuilder.Node<String> p = model.search(v);
		model.rightRotate(p);
	}

	private void leftRotateAround(BinaryTreeModel<String> model, String v) {
		de.lere.vaad.treebuilder.Node<String> p = model.search(v);
		model.leftRotate(p);
	}

	private void step() {
		language.nextStep();
	}

	Random r = new Random(22);

	private List<Integer> createSomeInts(int howmuch) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < howmuch; ++i) {
			list.add(r.nextInt(howmuch * 20));
		}
		return list;
	}

	private <T extends Node> Action<T> actionSourceCodeText(Class<T> type) {
		Action<T> sourceCodeText = new ActionAdapter<T>() {
			@Override
			public void activateOn(LocationProvider<T> location) {
				SourceCodeProperties scProps = new SourceCodeProperties();
				scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
						Color.RED);
				scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
						"Monospaced", Font.PLAIN, 12));
				scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
						Color.RED);
				scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
				// now, create the source code entity
				SourceCode sc = language.newSourceCode(location.getLocation(),
						"algorithmOperations", null, scProps);
				String codeGroupText = "1. Zig Step\n" + "2. Zig-zig Step\n"
						+ "3. Zig-zag Step";
				sc = getFilledSourceCode(codeGroupText, sc);
				hideOnDeactivate(sc);
				SplayTreeAnimation.this.setSourceCodeGroup(sc);
			}
		};
		return sourceCodeText;
	}

	private <T extends Node> Action<T> actionHeaderText(Class<T> clazz) {
		Action<T> headerText = new ActionAdapter<T>() {
			@Override
			public void activateOn(LocationProvider<T> location) {
				TextProperties props = new TextProperties();
				props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
				Text header = language.newText(location.getLocation(),
						animationProperties.title, "header", null, props);
				hideOnDeactivate(header);
			}
		};
		return headerText;
	}

	void setSourceCodeGroup(SourceCode sc) {
		this.sourceCode = sc;
	}

	public SourceCode getSourceCode() {
		if (sourceCode == null)
			throw new IllegalStateException("no source code has been set yet");
		else
			return sourceCode;
	}

	private SourceCode getSourceCodeDummy(Offset node) {
		SourceCodeProperties scProps = new SourceCodeProperties();
		SourceCode sc = language.newSourceCode(node, "algorithmOperations",
				null, scProps);
		String codeGroupText = " \n \n \n";
		sc = getFilledSourceCode(codeGroupText, sc);
		return sc;
	}

	private LocationDirector<Offset> createDescriptionBeginningLocDir(
			LocationDirector<Coordinates> headerPos) {
		Text headerDummyPrimitive = language.newText(headerPos.getLocation(),
				"12345678890", "headerDummyText", new Hidden());
		Offset beginDescriptionLoc = new Offset(30,
				2 * splayProps.textVerticalHeight, headerDummyPrimitive,
				AnimalScript.DIRECTION_SE);
		NextStateOnLocationDirector<Offset> director = new NextStateOnLocationDirector<Offset>(
				beginDescriptionLoc);
		return director;
	}

	private NextStateOnLocationDirector<Coordinates> createDirectorGraphroot() {
		Coordinates coordinates = NodeHelper
				.convertAWTPointToCoordinates(GRAPHROOT_COORDINATES);
		return new NextStateOnLocationDirector<Coordinates>(coordinates);
	}

	private NextStateOnLocationDirector<Coordinates> createHeaderLocDir() {
		NextStateOnLocationDirector<Coordinates> director = new NextStateOnLocationDirector<Coordinates>(
				new Coordinates(10, 10));
		return director;
	}

	private NextStateOnLocationDirector<Offset> createMacroStepLocDir(
			LocationDirector<?> beginDescriptionLoc) {
		SourceCodeProperties scProps = new SourceCodeProperties();
		SourceCode sc = language.newSourceCode(
				beginDescriptionLoc.getLocation(), "algorithmOperations", null,
				scProps);
		String codeGroupText = " \n \n \n";

		sc = lh.getFilledSourceCode(codeGroupText, sc);

		Offset macroStepLocation = new Offset(0,
				this.animationProperties.textVerticalHeight, sc,
				AnimalScript.DIRECTION_SW);

		NextStateOnLocationDirector<Offset> director = new NextStateOnLocationDirector<Offset>(
				macroStepLocation);
		return director;
	}

	private NextStateOnLocationDirector<Offset> createMicroStepLocDir(
			LocationDirector<?> macroStep) {
		Group macroStepGroup = createTextGroup(" \n \n \n \n",
				macroStep.getLocation());

		Offset microStepLocation = new Offset(0, 5, macroStepGroup,
				AnimalScript.DIRECTION_SW);

		return new NextStateOnLocationDirector<Offset>(microStepLocation);
	}

	private NextStateOnLocationDirector<Coordinates> createSourceCodeLocDir() {
		return new NextStateOnLocationDirector<Coordinates>(
				Node.convertToNode(this.layout.getNEBoundary()));
	}

	private SourceCode getFilledSourceCode(String text, SourceCode sc) {
		return lh.getFilledSourceCode(text, sc);
	}

	private <T extends Node> void nextStateOnLocation(String string,
			LocationDirector<T> director) {
		lh.nextStateOnLocation(string, director);
	}

	private Group createTextGroup(String string, Node location) {
		return this.lh.createTextGroup(string, location);
	}

}
