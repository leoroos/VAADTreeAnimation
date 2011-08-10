package de.lere.vaad.splaytree;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	private final BinaryTreeLayout layout;

	private SplayTreeAnimation(Language l, TreeAnimationProperties tp) {
		this.language = l;
		this.animationProperties = tp;
		l.setStepMode(true);
		this.lh = new LocationDirectorProviderImpl();
		this.layout = new BinaryTreeLayout(NodeHelper.convertCoordinatesToAWTPoint(GRAPHROOT_COORDINATES), 200, 60);
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

	private void buildAnimation(TreeAnimationProperties props)
			throws IOException {
		init();

		//
		// Intro
		//

		nextStateOnLocation(actionHeaderText(), Location.Header);

		 nextStateOnLocation(SplayTreeResourceAccessor.INTRO.getText(),
		 Location.DescriptionBeginning);
		 
		 step();

		nextStateOnLocation(SplayTreeResourceAccessor.DESCRIPTION.getText(),
				Location.DescriptionBeginning);

		nextStateOnLocation(actionSourceCodeText(),
				Location.DescriptionBeginning);

		step();

		//
		// Zig-Step
		//
		String actionZigMacroDescription = "Wenn p die Wurzel ist wird der Zig-Step ausgeführt. \n"
				+ "Dieser Schritt wird nur ausgeführt wenn der Baum eine\n"
				+ "ungerade Knotenanzahl hat und es sich um die letzte\n"
				+ "Splay-Operation einer Transformation handelt.";
		nextStateOnLocation(actionZigMacroDescription, Location.Macrostep);

		nextStateOnLocation("Führe Rechtsrotation um P aus.",
				Location.Macrostep);

		

		BinaryTreeAnimationBuilder<String> animator = new BinaryTreeAnimationBuilder<String>(
				language);
		animator.setLayout(this.layout);
		BinaryTreeModel<String> model = BinaryTreeModel.createTreeByInsert("P,F,R,A,G".split(","));		
		animator.setModel(model);
		
		for(int i = 0; i < 20; ++i ){
			if((i % 2) == 0 ){
				model.rightRotate(model.getRoot());
			}
			else {
				model.leftRotate(model.getRoot());
			}
		}
				
		step();
		//
		// ZigZig-Step
		//

		hideAll(animator);
		String actionZigzigMacroDescription = "Es wird C gesucht.\n" +
				"Ist der gesuchte Knoten der linke Sohn seines Vaters der wiederum ein linkes Kind ist,\n"
				+ "wird der Zig-zig Schritt ausgeführt, der eine doppelte Rechtsrotation ist.\n" +
						"(bzw. doppelte Linksrotation im Spiegelverekehrten Fall)";
		nextStateOnLocation(actionZigzigMacroDescription, Location.Macrostep);

		model = BinaryTreeModel.createTreeByInsert("P,G,R,C,H,A,B".split(","));		
		animator.setModel(model);
		
		
		step();

		nextStateOnLocation("Führe Rechtsrotation um g aus.",
				Location.Microstep);
		
		Node<String> g = model.search("G");
		model.rightRotate(g);
		

		nextStateOnLocation("Führe Rechtsrotation um p aus.",
				Location.Microstep);
		
		rightRotateAround(model, "P");
		
		

		//
		// ZigZag-Step
		//
		hideAll(animator);
		String actionZigzagMacroDescription = "Sei I gesucht.\n" +
				"I befindet sich links von P und recht von G bzw.\n"
				+ "Hier wird ein Zig-zag Schritt ausgeführt.\n";
		nextStateOnLocation(actionZigzagMacroDescription, Location.Macrostep);
		
		model = BinaryTreeModel.createTreeByInsert("P,G,S,E,I,H,J".split(","));		
		animator.setModel(model);

		step();

		nextStateOnLocation("Führe Linksrotation um G aus.", Location.Microstep);
		leftRotateAround(model, "G");
		
		step();

		nextStateOnLocation("Führe Rechtsrotation um P aus.",
				Location.Microstep);
		rightRotateAround(model, "P");

		step();

		//
		// Description of Behavior for Access Operations
		//
		hideAll(animator);
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

		step();
		//
		// Showcase the operations
		//
		String showcaseMacroDescription = "Beispielhafte Darstellung der beschriebenen Operationen.";
		nextStateOnLocation(showcaseMacroDescription, Location.Macrostep);

		nextStateOnLocation("Suche nach einen Schlüssel $$", Location.Microstep);

		step();

		nextStateOnLocation("Einfügen des Schlüssels $$", Location.Microstep);

		step();

		nextStateOnLocation("Löschen eines Schlüssels $$", Location.Microstep);

		step();
	}
	

	private void hideAll(BinaryTreeAnimationBuilder<String> animator) {
		nextStateOnLocation("", Location.DescriptionBeginning);
		nextStateOnLocation("", Location.Macrostep);
		nextStateOnLocation("", Location.Microstep);
		if (animator != null)
			animator.setModel(new BinaryTreeModel<String> ());
	}

	private void rightRotateAround(BinaryTreeModel<String> model, String v) {
		Node<String> p = model.search(v);
		model.rightRotate(p);
	}
	
	private void leftRotateAround(BinaryTreeModel<String> model, String v) {
		Node<String> p = model.search(v);
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

	private @Nullable
	Group createTextGroup(List<String> readLines, Offset anchor) {
		if (readLines.isEmpty())
			return null;
		Offset nextTextPos = anchor;
		int groupId = runninggroupidentifier++;
		LinkedList<Primitive> texts = new LinkedList<Primitive>();
		for (int i = 0; i < readLines.size(); i++) {
			Text text = language.newText(nextTextPos, readLines.get(i), groupId
					+ "id" + i, null);
			nextTextPos = new Offset(0,
					this.animationProperties.verticalTextGap, text, "SW");
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

	private @Nullable
	Group createTextGroup(String text, Offset location) {
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
