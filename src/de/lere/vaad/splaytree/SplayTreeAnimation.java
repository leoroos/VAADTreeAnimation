package de.lere.vaad.splaytree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
import algoanim.util.Offset;
import de.lere.vaad.TreeAnimationProperties;
import de.lere.vaad.locationhandler.Action;
import de.lere.vaad.locationhandler.ActionAdapter;
import de.lere.vaad.locationhandler.AnimationLocationDirector;
import de.lere.vaad.locationhandler.LocationDirector;
import de.lere.vaad.locationhandler.LocationDirectorProvider;
import de.lere.vaad.locationhandler.LocationDirectorProviderImpl;
import de.lere.vaad.locationhandler.LocationProvider;
import de.lere.vaad.splaytree.resources.descriptions.SplayTreeResourceAccessor;
import de.lere.vaad.treebuilder.BinaryTreeAnimationBuilder;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.utils.CorrectedOffset;
import de.lere.vaad.utils.NodeHelper;
import de.lere.vaad.utils.StrUtils;
import edu.umd.cs.findbugs.annotations.Nullable;

public class SplayTreeAnimation {

	private static final Coordinates GRAPHROOT_COORDINATES = new Coordinates(
			400, 300);
	public static final String DIRECTOR_HEADER = "de.lere.vaad.headerdirector";
	public static final String DIRECTOR_DESCRIPTION_BEGINNING = "de.lere.vaad.descriptionbeginningdirector";
	public static final String DIRECTOR_MACROSTEP = "de.lere.vaad.macrostepdirector";
	public static final String DIRECTOR_MICROSTEP = "de.lere.vaad.microstepdirector";
	public static final String DIRECTOR_GRAPHROOT = "de.lere.vaad.graphrootdirector";

	enum Location {
		Header(DIRECTOR_HEADER), DescriptionBeginning(
				DIRECTOR_DESCRIPTION_BEGINNING), Macrostep(DIRECTOR_MACROSTEP), Microstep(
				DIRECTOR_MICROSTEP), Graphroot(DIRECTOR_GRAPHROOT);

		public final String DIRECTOR_NAME;

		private Location(String director) {
			this.DIRECTOR_NAME = director;
		}
	}

	private final Language language;

	private SourceCode sourceCode;
	private int runninggroupidentifier = 0;
	private final TreeAnimationProperties animationProperties;

	private SplayTreeAnimation(Language l, TreeAnimationProperties tp) {
		this.language = l;
		this.animationProperties = tp;
		l.setStepMode(true);
		this.lh = new LocationDirectorProviderImpl();
	}

	/**
	 * Container Object for properties of this Animation
	 */
	private TreeAnimationProperties splayProps = new TreeAnimationProperties();

	private final LocationDirectorProvider lh;
	
	/**
	 * Add a director for a specific location.
	 * 
	 * @param key
	 *            the unique key to identify a director.
	 * @param director
	 */
	public void addDirector(String key, LocationDirector director) {
		lh.addDirector(key, director);
	}

	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height

		TreeAnimationProperties tp = new TreeAnimationProperties();

		tp.authors = "Rene Hertling, Leo Roos";

		tp.title = "Splaytree Animation";
		

		Language l = new AnimalScript(tp.title, tp.authors, tp.screenResolution.width, tp.screenResolution.height);
		SplayTreeAnimation animation = new SplayTreeAnimation(l,tp);
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

	private void buildAnimation(TreeAnimationProperties props)
			throws IOException {
		init();

		//
		// Intro
		//

		nextStateOnLocation(actionHeaderText(), Location.Header);

		// nextStateOnLocation(ResourceAccessor.INTRO.getText(),
		// Location.DescriptionBeginning);

		BinaryTreeAnimationBuilder<Integer> aniBui = new BinaryTreeAnimationBuilder<Integer>(
				language);
		Offset graphRootLocation = lh.getDirector(Location.Graphroot.DIRECTOR_NAME)
				.getLocation();
		Point graphRootPoint = NodeHelper
				.convertOffsetToAWTPoint(graphRootLocation);
		BinaryTreeLayout blay = new BinaryTreeLayout(graphRootPoint, 200, 60);
		aniBui.setLayout(blay);
		BinaryTreeModel<Integer> model = new BinaryTreeModel<Integer>();
		// language.nextStep();
		List<Integer> intList = createSomeInts(15);
		Iterator<Integer> iterator2 = intList.iterator();
		while (iterator2.hasNext()) {
			model.insert(iterator2.next());
			// language.nextStep();
		}
		aniBui.setModel(model);
		model.insert(1234);
		language.nextStep();
		model.insert(12345);
		language.nextStep();
		Collections.shuffle(intList);
		List<Node<Integer>> nodesInOrder = model.getNodesInOrder();
		Collections.shuffle(nodesInOrder);
		Iterator<Node<Integer>> iterator3 = nodesInOrder.iterator();
		Random r = new Random(123);
		// while(iterator3.hasNext()){
		// Node<Integer> next = iterator3.next();
		// boolean b = r.nextBoolean();
		// if(b){
		// model.rightRotate(next);
		// nextStateOnLocation("Right Rotation aroung" + next.getValue(),
		// Location.Microstep);
		// }
		// else {
		// model.leftRotate(next);
		// nextStateOnLocation("Left Rotation" + next.getValue(),
		// Location.Microstep);
		// }
		// language.nextStep();
		// }

		Iterator<Integer> iterator = intList.iterator();
		while (iterator.hasNext()) {
			Integer next = iterator.next();
			model.delete(next);
			language.nextStep();
		}

		nextStateOnLocation(SplayTreeResourceAccessor.DESCRIPTION.getText(),
				Location.DescriptionBeginning);

		nextStateOnLocation(actionSourceCodeText(),
				Location.DescriptionBeginning);

		language.nextStep();

		//
		// Zig-Step
		//
		String actionZigMacroDescription = "Wenn p die Wurzel ist wird der Zig-Step ausgeführt. \n"
				+ "Dieser Schritt wird nur ausgeführt wenn der Baum eine\n"
				+ "ungerade Knotenanzahl hat und es sich um die letzte\n"
				+ "Splay-Operation einer Transformation handelt.";
		nextStateOnLocation(actionZigMacroDescription, Location.Macrostep);

		nextStateOnLocation("Führe Rechtsrotation um p aus.",
				Location.Macrostep);

		//
		// ZigZig-Step
		//

		String actionZigzigMacroDescription = "Sind x und p beide links bzw. rechts von g wird der\n"
				+ "Zig-zig Schritt ausgeführt.";
		nextStateOnLocation(actionZigzigMacroDescription, Location.Macrostep);

		language.nextStep();

		nextStateOnLocation("Führe Rechtsrotation um g aus.",
				Location.Microstep);

		nextStateOnLocation("Führe Rechtsrotation um p aus.",
				Location.Microstep);

		//
		// ZigZag-Step
		//
		String actionZigzagMacroDescription = "Befindet sich p links von g und x rechts von p, bzw.\n"
				+ "p rechts von g und x links von p, wird ein Zig-zag Schritt ausgeführt.\n";
		nextStateOnLocation(actionZigzagMacroDescription, Location.Macrostep);

		language.nextStep();

		nextStateOnLocation("Führe Linksrotation um p aus.", Location.Microstep);

		language.nextStep();

		nextStateOnLocation("Führe Rechtsrotation um g aus.",
				Location.Microstep);

		language.nextStep();

		//
		// Description of Behavior for Access Operations
		//
		nextStateOnLocation("", Location.Microstep);
		nextStateOnLocation("", Location.DescriptionBeginning);
		String accessBehaviourMacroDescription = "Verhalten bei zugreifenden Operationen	\n"
				+ "Suche:\n"
				+ "Wird ein Knoten gesucht, wird auf diesem eine Splay Operation ausgeführt.\n"
				+ "Einfügen:\n"
				+ "Beim Einfügen wird der Knoten wie in einen Binärbaum hinzugefügt und\n"
				+ "anschließend der eingefügte Knoten zur Wurzel gesplayed\n"
				+ "Löschen:\n"
				+ "Beim Löschen wird der zu löschende Knoten wie in einem Binärbaum gelöscht\n"
				+ "und sein Parent zur Wurzel gesplayed.";
		nextStateOnLocation(accessBehaviourMacroDescription, Location.Macrostep);

		language.nextStep();
		//
		// Showcase the operations
		//
		String showcaseMacroDescription = "Beispielhafte Darstellung der beschriebenen Operationen.";
		nextStateOnLocation(showcaseMacroDescription, Location.Macrostep);

		nextStateOnLocation("Suche nach einen Schlüssel $$", Location.Microstep);

		language.nextStep();

		nextStateOnLocation("Einfügen des Schlüssels $$", Location.Microstep);

		language.nextStep();

		nextStateOnLocation("Löschen eines Schlüssels $$", Location.Microstep);

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

	/**
	 * Puts the a new action on a location
	 * 
	 * @param action
	 * @param location
	 */
	private void nextStateOnLocation(Action action, Location location) {
		lh.getDirector(location.DIRECTOR_NAME).nextState(action);
	}

	/**
	 * creates a default action for the given List of strings, i.e. a group of
	 * Texts that will be placed on the location parameter and hidden if someone
	 * else uses the location.
	 */
	private void nextStateOnLocation(final List<String> text, Location location) {
		ActionAdapter aAction = new ActionAdapter() {

			@Override
			public void activateOn(LocationProvider location) {				
				Group aTextGroup = createTextGroup(text, location.getLocation());
				hideOnDeactivate(aTextGroup);
			}
		};
		nextStateOnLocation(aAction, location);
	}
	
	private @Nullable Group createTextGroup(List<String> readLines, Offset anchor) {
		if(readLines.isEmpty())
			return null;
		Offset nextTextPos = anchor;
		int groupId = runninggroupidentifier++;
		LinkedList<Primitive> texts = new LinkedList<Primitive>();
		for (int i = 0; i < readLines.size(); i++) {
			Text text = language.newText(nextTextPos, readLines.get(i), groupId
					+ "id" + i, null);
			nextTextPos = new Offset(0, this.animationProperties.verticalTextGap, text, "SW");
			texts.add(text);
		}
		Group introGroup = language.newGroup(texts, "group" + groupId);
		return introGroup;
	}

	private void init() throws IOException {
		initializeLocationDirectors();
	}

	/**
	 * @see #nextStateOnLocation(List, Location)
	 */
	private void nextStateOnLocation(final String newText, Location location) {
		nextStateOnLocation(StrUtils.toLines(newText), location);
	}

	private Action actionSourceCodeText() {
		Action sourceCodeText = new ActionAdapter() {
			@Override
			public void activateOn(LocationProvider location) {
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

	private Action actionHeaderText() {
		Action headerText = new ActionAdapter() {
			@Override
			public void activateOn(LocationProvider location) {
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

	/**
	 * Fills the {@link SourceCode} object {@code sc} with the lines provided by
	 * {@code codeGroupText}.
	 */
	private SourceCode getFilledSourceCode(String codeGroupText, SourceCode sc) {
		List<String> split = StrUtils.toLines(codeGroupText);
		for (int i = 0; i < split.size(); i++) {
			sc.addCodeLine(split.get(i), "", 0, null);
		}
		return sc;
	}



	private void initializeLocationDirectors() {
		Coordinates headerPosCoord = new Coordinates(10, 10);
		Text headerDummyPrimitive = language.newText(headerPosCoord,
				"12345678890", "headerDummyText", null);
		headerDummyPrimitive.hide();
		addDirector(DIRECTOR_HEADER, new AnimationLocationDirector(new Offset(
				0, 0, headerDummyPrimitive, AnimalScript.DIRECTION_NW)));

		Offset beginDescriptionLoc = new Offset(30,
				2 * splayProps.textVerticalHeight, headerDummyPrimitive,
				AnimalScript.DIRECTION_SE);
		addDirector(DIRECTOR_DESCRIPTION_BEGINNING,
				new AnimationLocationDirector(beginDescriptionLoc));
		SourceCode code = this.getSourceCodeDummy(beginDescriptionLoc);

		Offset macroStepLocation = new Offset(0, 10, code,
				AnimalScript.DIRECTION_SW);

		addDirector(DIRECTOR_MACROSTEP, new AnimationLocationDirector(
				macroStepLocation));

		Group macroStepGroup = createTextGroup(" \n \n \n \n",
				macroStepLocation);

		Offset microStepLocation = new Offset(0, 5, macroStepGroup,
				AnimalScript.DIRECTION_SW);

		addDirector(DIRECTOR_MICROSTEP, new AnimationLocationDirector(
				microStepLocation));

		addDirector(DIRECTOR_GRAPHROOT, new AnimationLocationDirector(
				CorrectedOffset.getOffsetForCoordinate(GRAPHROOT_COORDINATES)));

	}
	
	private @Nullable Group createTextGroup(String text, Offset location) {
		List<String> stringToLinesAtDelimiter = StrUtils.toLines(text);
		return createTextGroup(stringToLinesAtDelimiter, location);
	}

	private SourceCode getSourceCodeDummy(Offset node) {
		SourceCodeProperties scProps = new SourceCodeProperties();
		SourceCode sc = language.newSourceCode(node, "algorithmOperations",
				null, scProps);
		String codeGroupText = " \n \n \n";
		sc = getFilledSourceCode(codeGroupText, sc);
		return sc;
	}

}
