package de.lere.vaad;

import generators.helpers.OffsetCoords;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import resources.descriptions.ResourceAccessor;
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
import algoanim.util.Node;

public class SplayTreeAnimation implements LocationDirectorProvider {

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

	private static final String AUTHORS = "Rene Hertling, Leo Roos";
	private static final String TITLE = "Splaytree Animation";
	private Language language;

	private SourceCode sourceCode;
	private int runninggroupidentifier = 0;

	private SplayTreeAnimation(Language l) {
		this.language = l;
		l.setStepMode(true);
	}

	/**
	 * Container Object for properties of this Animation
	 */
	private SplayTreeProperties splayProps = new SplayTreeProperties();

	private Hashtable<String, LocationDirector> directors = new Hashtable<String, LocationDirector>();

	/**
	 * Add a director for a specific location.
	 * 
	 * @param key
	 *            the unique key to identify a director.
	 * @param director
	 */
	public void addDirector(String key, LocationDirector director) {
		this.directors.put(key, director);
	}

	public LocationDirector getDirector(String key) {
		return this.directors.get(key);
	}

	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height
		Language l = new AnimalScript(TITLE, AUTHORS, 640,
				480);
		SplayTreeAnimation animation = new SplayTreeAnimation(l);
		try {
			animation.buildAnimation();
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

	private void buildAnimation() throws IOException {
		init();

		//
		// Intro
		//
		
		nextStateOnLocation(actionHeaderText(), Location.Header);

		nextStateOnLocation(ResourceAccessor.INTRO.getText(),
				Location.DescriptionBeginning);

		language.nextStep();

		nextStateOnLocation(ResourceAccessor.DESCRIPTION.getText(),
				Location.DescriptionBeginning);

		language.nextStep();

		nextStateOnLocation(actionSourceCodeText(),
				Location.DescriptionBeginning);

		language.nextStep();
		
		//
		// Zig-Step
		//
		String actionZigMacroDescription = "Wenn p die Wurzel ist wird der Zig-Step ausgeführt. \n" + 
				"Dieser Schritt wird nur ausgeführt wenn der Baum eine\n" + 
				"ungerade Knotenanzahl hat und es sich um die letzte\n" + 
				"Splay-Operation einer Transformation handelt.";
		nextStateOnLocation(actionZigMacroDescription, Location.Macrostep);
		
		language.nextStep();
		
		nextStateOnLocation("Führe Rechtsrotation um p aus.", Location.Macrostep);

		//
		// ZigZig-Step
		//

		String actionZigzigMacroDescription = "Sind x und p beide links bzw. rechts von g wird der\n"
				+ "Zig-zig Schritt ausgeführt.";
		nextStateOnLocation(actionZigzigMacroDescription, Location.Macrostep);

		language.nextStep();

		nextStateOnLocation("Führe Rechtsrotation um g aus.",
				Location.Microstep);

		language.nextStep();

		nextStateOnLocation("Führe Rechtsrotation um p aus.",
				Location.Microstep);

		language.nextStep();

		//
		// ZigZag-Step
		//
		String actionZigzagMacroDescription = "Befindet sich p links von g und x rechts von p, bzw.\n" + 
				"p rechts von g und x links von p, wird ein Zig-zag Schritt ausgeführt.\n"; 				
		nextStateOnLocation(actionZigzagMacroDescription ,Location.Macrostep);
		
		language.nextStep();
		
		nextStateOnLocation("Führe Linksrotation um p aus.", Location.Microstep);
		
		language.nextStep();
		
		nextStateOnLocation("Führe Rechtsrotation um g aus.", Location.Microstep);
		
		language.nextStep();
		
		//
		// Description of Behavior for Access Operations
		//
		nextStateOnLocation("", Location.DescriptionBeginning);
		String accessBehaviourMacroDescription = "Verhalten bei zugreifenden Operationen	\n" + 
				"Suche:\n" + 
				"Wird ein Knoten gesucht, wird auf diesem eine Splay Operation ausgeführt.\n" + 
				"Einfügen:\n" + 
				"Beim Einfügen wird der Knoten wie in einen Binärbaum hinzugefügt und\n" + 
				"anschließend der eingefügte Knoten zur Wurzel gesplayed\n" + 
				"Löschen:\n" + 
				"Beim Löschen wird der zu löschende Knoten wie in einem Binärbaum gelöscht\n" + 
				"und sein Parent zur Wurzel gesplayed.";
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

	/**
	 * Puts the a new action on a location
	 * 
	 * @param action
	 * @param location
	 */
	private void nextStateOnLocation(Action action, Location location) {
		getDirector(location.DIRECTOR_NAME).nextState(action);
	}

	/**
	 * creates a default action for the given List of strings, i.e. a group of
	 * Texts that will be placed on the location parameter and hidden if someone
	 * els will uses the location.
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

	/**
	 * @see #nextStateOnLocation(List, Location)
	 */
	private void nextStateOnLocation(final String newText, Location location) {
		nextStateOnLocation(StringHelper.getStringToLinesAtDelimiter(newText),
				location);
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
				Text header = language.newText(location.getLocation(), TITLE,
						"header", null, props);
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
		List<String> split = StringHelper
				.getStringToLinesAtDelimiter(codeGroupText);
		for (int i = 0; i < split.size(); i++) {
			sc.addCodeLine(split.get(i), "", 0, null);
		}
		return sc;
	}

	private Group createTextGroup(String text, Node location) {
		List<String> stringToLinesAtDelimiter = StringHelper
				.getStringToLinesAtDelimiter(text);
		return createTextGroup(stringToLinesAtDelimiter, location);
	}

	private Group createTextGroup(List<String> readLines, Node anchor) {
		Node nextTextPos = anchor;
		int groupId = runninggroupidentifier++;
		LinkedList<Primitive> texts = new LinkedList<Primitive>();
		for (int i = 0; i < readLines.size(); i++) {
			Text text = language.newText(nextTextPos, readLines.get(i), groupId
					+ "id" + i, null);
			nextTextPos = new OffsetCoords(nextTextPos, 0,
					splayProps.textVerticalHeight);
			texts.add(text);
		}
		Group introGroup = language.newGroup(texts, "group" + groupId);
		return introGroup;
	}

	private void init() throws IOException {
		initializeLocationDirectors();
		loadTexts();

	}

	private void initializeLocationDirectors() {
		Coordinates headerPos = new Coordinates(10, 10);
		addDirector(DIRECTOR_HEADER, new AnimationLocationDirector(headerPos));

		OffsetCoords beginDescriptionLoc = new OffsetCoords(headerPos, 30,
				2 * splayProps.textVerticalHeight);
		addDirector(DIRECTOR_DESCRIPTION_BEGINNING,
				new AnimationLocationDirector(beginDescriptionLoc));

		OffsetCoords macroStepLocation = new OffsetCoords(beginDescriptionLoc,
				0, 5 * splayProps.textVerticalHeight);
		addDirector(DIRECTOR_MACROSTEP, new AnimationLocationDirector(
				macroStepLocation));

		OffsetCoords microStepLocation = new OffsetCoords(macroStepLocation, 0,
				4 * splayProps.textVerticalHeight);
		addDirector(DIRECTOR_MICROSTEP, new AnimationLocationDirector(
				microStepLocation));

		Coordinates graphroot = new Coordinates(250, 300);
		addDirector(DIRECTOR_GRAPHROOT,
				new AnimationLocationDirector(graphroot));

	}

	private void loadTexts() throws IOException {
		ResourceAccessor[] texts = { ResourceAccessor.DESCRIPTION, //
				ResourceAccessor.INTRO, //
				ResourceAccessor.BEHAVIOUR, //
				ResourceAccessor.ZIG_STEP, //
				ResourceAccessor.ZIGZAG_STEP, //
				ResourceAccessor.ZIGZIG_STEP, //
		};
		for (ResourceAccessor resourceAccessor : texts) {
			resourceAccessor.init();
		}
	}

}
