package de.lere.vaad.binarysearchtree;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
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
import de.lere.vaad.treebuilder.BinaryTreeAnimationBuilder;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.utils.CorrectedOffset;
import de.lere.vaad.utils.NodeHelper;
import de.lere.vaad.utils.StrUtils;
import edu.umd.cs.findbugs.annotations.Nullable;

public class BinarySerachTreeAnimation {

	private static final Point GRAPHROOT_COORDINATES = new Point(400, 300);
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
	private BinaryTreeLayout layout;

	private BinarySerachTreeAnimation(Language l, TreeAnimationProperties tp) {
		this.language = l;
		this.animationProperties = tp;
		l.setStepMode(true);
		this.lh = new LocationDirectorProviderImpl();
		this.layout = new BinaryTreeLayout(GRAPHROOT_COORDINATES, 160, 60);
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

		TreeAnimationProperties tps = new TreeAnimationProperties();
		tps.authors = "Leo Roos, Rene Hertling";
		tps.title = "Binary Search Tree";

		AnimalScript animalScript = new AnimalScript(tps.authors, tps.title,
				tps.screenResolution.width, tps.screenResolution.height);

		BinarySerachTreeAnimation binarySerachTreeAnimation = new BinarySerachTreeAnimation(
				animalScript, tps);
		binarySerachTreeAnimation.buildAnimation();

		String animationCode = animalScript.getAnimationCode();
		System.out.println(animationCode);
		try {
			FileUtils.writeStringToFile(new File("/tmp/animation.asu"),
					animationCode);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void buildAnimation() {
		initializeLocationDirectors();

		BinaryTreeModel<String> model = BinaryTreeModel.createTreeByInsert("J",
				"F", "P", "D", "G", "L", "V", "C", "N", "S", "X", "Q", "U");
		BinarySearchTreeAnimator<String> animator = new BinarySearchTreeAnimator<String>(
				language);
		animator.setLayout(this.layout);

		nextStateOnLocation("Der Binäre Suchbaum", Location.Header);

		step();

		nextStateOnLocation(INITIAL_DESCRIPTION, Location.DescriptionBeginning);

		step();

		hideAll(animator);
		nextStateOnLocation("Beispiel eines binären Suchbaums",
				Location.Macrostep);
		nextStateOnLocation(
				"Alle Knoten kleiner J befinden sich linkes von der Wurzel,\n"
						+ "alle Knoten größer J rechts von der Wurzel.",
				Location.Microstep);

		animator.setModel(model);

		step();

		hideAll(animator);

		nextStateOnLocation(
				"Ein binärer Suchbaum ermöglicht das Einfügen, Entfernen und Suchen von Werten\n"
						+ "in maximal O(h) Zeit, wobei h seine Höhe darstellt.",
				Location.DescriptionBeginning);

		step();

		nextStateOnLocation(
				"Beispielhafte Darstellung der Suche eines Knotens",
				Location.DescriptionBeginning);
		animator.setModel(model);

		step();

		nextStateOnLocation(
				"Die Suche im binären Suchbaum beginnt bei der Wurzel,\n"
						+ "vergleicht darauf ob der gesuchte Knoten größer oder kleiner ist und führt seine Suche entsprechend\n"
						+ "rechts oder links fort.", Location.Macrostep);
		step();

		nextStateOnLocation(
				"Soll der Knoten V gesucht werden führt die Suche an J und P vorbei.",
				Location.Microstep);

		model.search("V");

		step();

		nextStateOnLocation("Analog für C", Location.Microstep);

		step();

		model.search("C");

		step();

		nextStateOnLocation("und Q", Location.Microstep);

		step();

		model.search("Q");

		step();

		hideAll(null);
		nextStateOnLocation(
				"Beispielhafte Darstellung des Einfügens eines Knotens",
				Location.DescriptionBeginning);
		nextStateOnLocation(
				"Beim Einfügen wird zuerst wie beim Suchen vorgegangen.\n"
						+ "Es wird versucht den einzufügenden Knoten zu suchen, wird dabei ein Blatt\n"
						+ "erreicht wird der Knoten dort wo erwartet worden wäre ihn zu finden eingefügt.",
				Location.Macrostep);
		animator.setModel(model);

		step();

		nextStateOnLocation(
				"Knoten T ist größer als die Wurzel J wird also rechts davon einsortiert.\n"
						+ "T ist auch größer als P und wird rechts von diesem einsortiert,\n"
						+ "kleiner als V, größer als S und schließlich kleiner als U.",
				Location.Microstep);
		animator.setModel(model);

		step();

		model.insert("T");

		step();

		nextStateOnLocation("Analog wird E rechts von D eingefügt ...",
				Location.Microstep);

		step();

		model.insert("E");

		step();

		nextStateOnLocation("... I rechts von G", Location.Microstep);

		step();

		model.insert("I");

		step();

		nextStateOnLocation("und H links von I", Location.Microstep);

		step();

		model.insert("H");

		step();

		// Hide all
		hideAll(animator);
		nextStateOnLocation(
				"Beim löschen wird vorgeganen wie bei einem gewöhnlichen\n"
						+ "Binärbaum. Dabei werden drei Fälle unterschieden:\n"
						+ "1. Der zu löschende Knoten hat keine Kinder\n"
						+ "2. Der zu löschende Knoten hat ein Kind\n"
						+ "3. Der zu löschende Knoten hat zwei Kinder",
				Location.DescriptionBeginning);

		step();

		
		nextStateOnLocation(
				"Beispielhafte Darstellung des Löschens eines Knotens.",
				Location.DescriptionBeginning);
		nextStateOnLocation("1. Der zu löschende Knoten hat keine Kinder\n",
				Location.Macrostep);
		nextStateOnLocation("Zum Beispiel löschen des Knotens H.\n"
				+ "Der Knoten kann entfernt werden ohne zusätzliche\n"
				+ "Operationen ausführen zu müssen.", Location.Microstep);
		animator.setModel(model);
		step();

		model.delete("H");

		step();

		nextStateOnLocation("2. Der zu löschende Knoten hat genau ein Kind\n",
				Location.Macrostep);
		nextStateOnLocation(
				"Zum Beispiel löschen von U.\nDer zu löschende Knoten kann mit seinem Kind ersetzt werden.",
				Location.Microstep);
		step();

		model.delete("U");

		step();

		nextStateOnLocation("3. Der zu löschende Knoten hat zwei Kinder",
				Location.Macrostep);
		nextStateOnLocation(
				"Zum Beispiel löschen von J.\n"
						+ "Der zu löschende Knoten muss mit seinem direkten Nachfolger oder Vorgänger ersetzt werden.\n"
						+ "Die möglicherweise vorhandenen Unterbäume müssen entsprechend umgehängt werden.",
				Location.Microstep);
		step();

		model.delete("J");
		step();
		
		nextStateOnLocation(
				"Analog für F ...",
				Location.Microstep);
		step();
		
		model.delete("F");
		
		step();
		
		nextStateOnLocation(
				"... und für P",
				Location.Microstep);
		step();
		
		model.delete("P");
		
		step();

		hideAll(animator);
		nextStateOnLocation("Ende", Location.Macrostep);
		step();
	}
 
	private void hideAll(BinarySearchTreeAnimator<String> animator) {
		nextStateOnLocation("", Location.DescriptionBeginning);
		nextStateOnLocation("", Location.Macrostep);
		nextStateOnLocation("", Location.Microstep);
		if (animator != null)
			animator.setModel(newEmptyModel());
	}

	private void step() {
		language.nextStep();
	}

	private BinaryTreeModel<String> newEmptyModel() {
		return new BinaryTreeModel<String>();
	}

	/**
	 * @see #nextStateOnLocation(List, Location)
	 */
	private void nextStateOnLocation(final String newText, Location location) {
		nextStateOnLocation(StrUtils.toLines(newText), location);
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

	/**
	 * Puts the a new action on a location
	 * 
	 * @param action
	 * @param location
	 */
	private void nextStateOnLocation(Action action, Location location) {
		lh.getDirector(location.DIRECTOR_NAME).nextState(action);
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

		addDirector(
				DIRECTOR_GRAPHROOT,
				new AnimationLocationDirector(
						CorrectedOffset.getOffsetForCoordinate(NodeHelper
								.convertAWTPointToCoordinates(GRAPHROOT_COORDINATES))));

	}

	private SourceCode getSourceCodeDummy(Offset node) {
		SourceCodeProperties scProps = new SourceCodeProperties();
		SourceCode sc = language.newSourceCode(node, "algorithmOperations",
				null, scProps);
		String codeGroupText = " \n \n \n";
		sc = getFilledSourceCode(codeGroupText, sc);
		return sc;
	}

	private @Nullable
	Group createTextGroup(String text, Offset location) {
		List<String> stringToLinesAtDelimiter = StrUtils.toLines(text);
		return createTextGroup(stringToLinesAtDelimiter, location);
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

	private static String getText(String name) {
		InputStream resourceAsStream = BinarySerachTreeAnimation.class
				.getResourceAsStream("resources/" + name);
		InputStreamReader reader = new InputStreamReader(resourceAsStream);
		String result;
		try {
			result = IOUtils.toString(reader);
		} catch (IOException e) {
			result = e.toString();
		}
		return result;
	}

	private static String INITIAL_DESCRIPTION = getText("initialDescription.txt");

}
