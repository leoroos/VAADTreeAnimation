package de.lere.vaad.binarysearchtree;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.EndOfTheWorldException;
import de.lere.vaad.locationhandler.LocationDirector;
import de.lere.vaad.locationhandler.LocationHandler;
import de.lere.vaad.locationhandler.NextStateOnLocationDirector;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.ExtractedBinaryTreeAnimations;
import de.lere.vaad.treebuilder.GraphWriterImpl;
import de.lere.vaad.treebuilder.TreeEvent;
import de.lere.vaad.treebuilder.TreeEventListener;
import de.lere.vaad.treebuilder.TreeEventListenerAggregator;
import de.lere.vaad.treebuilder.TreeInsertEvent;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition;
import de.lere.vaad.utils.NodeHelper;
import de.lere.vaad.utils.TextLoaderUtil;

public class BinarySearchTreeAnimation<T extends Comparable<T>> {

	private static final Point GRAPHROOT_COORDINATES = new Point(400, 300);

	private final String INITIAL_DESCRIPTION;

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

	private final BinaryTreeProperties animationProperties;

	public final LocationDirector<Offset> DIRECTOR_DESCRIPTION_BEGINNING;
	public final NextStateOnLocationDirector<Coordinates> DIRECTOR_GRAPHROOT;
	public final LocationDirector<Coordinates> DIRECTOR_HEADER;
	public final LocationDirector<Offset> DIRECTOR_MACROSTEP;
	public final LocationDirector<Offset> DIRECTOR_MICROSTEP;
	public final NextStateOnLocationDirector<Coordinates> DIRECTOR_SOURCECODE;

	private final Language language;
	private BinaryTreeLayout layout;

	private SourceCode sourceCode;

	/**
	 * Container Object for properties of this Animation
	 */

	private LocationHandler lh;

	private TextLoaderUtil textLoader;

	private final T[] initialTree;

	@Nullable
	private T[] insertionArray;

	@Nullable
	private T[] searchArray;

	@Nullable
	private T[] deleteArray;

	private boolean showIntro = true;

	public BinarySearchTreeAnimation(Language l, BinaryTreeProperties btp,
			T[] initialTree) {
		if (l == null || btp == null)
			throw new IllegalArgumentException("no null values allowed");
		if (initialTree == null)
			throw new IllegalArgumentException(
					"null not allowed for initial tree. Use empty array instead");
		this.language = l;
		this.animationProperties = btp;
		l.setStepMode(true);
		this.layout = new BinaryTreeLayout(GRAPHROOT_COORDINATES, 160, 60);
		this.lh = new LocationHandler(this.language, btp);
		this.textLoader = new TextLoaderUtil(getClass(), "resources");
		this.INITIAL_DESCRIPTION = textLoader.getText("initialDescription.txt");
		this.initialTree = initialTree;

		DIRECTOR_HEADER = createHeaderLocDir();
		DIRECTOR_DESCRIPTION_BEGINNING = createDescriptionBeginningLocDir(DIRECTOR_HEADER);
		DIRECTOR_MACROSTEP = createMacroStepLocDir(DIRECTOR_DESCRIPTION_BEGINNING);
		DIRECTOR_MICROSTEP = createMicroStepLocDir(DIRECTOR_MACROSTEP);
		DIRECTOR_GRAPHROOT = createDirectorGraphroot();
		DIRECTOR_SOURCECODE = createSourceCodeLocDir();

	}

	/**
	 * Builds the animation according to configuration
	 */
	public void buildAnimation() {

		final SourceCode insertionSourceCode = language.newSourceCode(
				DIRECTOR_SOURCECODE.getLocation(), "insertionSource",
				new Hidden(),
				this.animationProperties.getSourceCodeProperties());

		getFilledSourceCode(this.textLoader.getText("insertAlgo.txt"),
				insertionSourceCode);
		insertionSourceCode.hide();
		//
		// final BinaryTreeModel<String> model = BinaryTreeModel
		// .createTreeByInsert("J", "F", "P", "D", "G", "L", "V", "C",
		// "N", "S", "X", "Q", "U");
		// BinarySearchTreeAnimator<String> animator = new
		// BinarySearchTreeAnimator<String>(
		// language);

		BinaryTreeModel<T> model = BinaryTreeModel
				.createTreeByInsert(initialTree);

		final GraphWriterImpl<T> writer = new GraphWriterImpl<T>(language,
				layout);
		TreeEventListenerAggregator<T> animator = new TreeEventListenerAggregator<T>(
				language);
		animator.addAnimatior(new ExtractedBinaryTreeAnimations<T>(writer));
		animator.setLayout(this.layout);

		// Code highlighting during insertion
		model.addListener(new TreeEventListener<T>() {

			private int[] previouslyHighlighted = {};
			private de.lere.vaad.treebuilder.Node<T> previouslyHighlightedNode;
			private BinaryTreeModel<T> codeCurModel;
			private de.lere.vaad.treebuilder.Node<T> codeCurrentNodePos;

			private void highlightLines(final SourceCode sourceCode,
					int... lines) {
				lines = makeZeroBased(lines);
				for (int i : previouslyHighlighted) {
					sourceCode.unhighlight(i);
				}
				this.previouslyHighlighted = lines;
				for (int j : lines) {
					sourceCode.highlight(j);
				}
				highlightNode(codeCurrentNodePos, codeCurModel);
			}

			private void highlightNode(
					de.lere.vaad.treebuilder.Node<T> currentPosition,
					BinaryTreeModel<T> model) {
				if (previouslyHighlightedNode != null)
					writer.unhighlightNode(model, previouslyHighlightedNode);
				previouslyHighlightedNode = currentPosition;
				writer.highlightNode(model, currentPosition);
			}

			private int[] makeZeroBased(int[] lines) {
				for (int i = 0; i < lines.length; i++) {
					int j = lines[i] - 1;
					if (j < 0) {
						throw new IllegalArgumentException(
								"expected only args greater 0 but was " + j);
					}
					lines[i] = j;
				}
				return lines;
			}

			@Override
			public void update(TreeEvent<T> event) {
				if (event instanceof TreeInsertSourceCodeTraversing<?>) {
					TreeInsertSourceCodeTraversing<T> ie = (TreeInsertSourceCodeTraversing<T>) event;
					InsertSourceCodePosition curPos = ie.position;
					codeCurrentNodePos = ie.currentPosition;
					codeCurModel = ie.currentModel;
					switch (curPos) {

					case Init:
						highlightLines(insertionSourceCode, 2, 3);

						break;
					case CheckingIfInsertionPossible:
						highlightLines(insertionSourceCode, 4);

						break;
					case TestingIfWhereToFromCurrent:
						highlightLines(insertionSourceCode, 6);
						break;
					case LookingAlongLeftChild:
						highlightLines(insertionSourceCode, 7);

						break;
					case LookingAlongRightChild:
						highlightLines(insertionSourceCode, 9);

						break;
					case SettingParentForNewCurrentNode:
						highlightLines(insertionSourceCode, 10);

						break;
					case CheckingIfNewIsRoot:
						highlightLines(insertionSourceCode, 11);
						break;
					case FinalIsSettingToRoot:
						highlightLines(insertionSourceCode, 12);
						break;
					case FinalIsSettingAsLeftChildFrom:
						highlightLines(insertionSourceCode, 14);
						break;
					case FinalIsSettingAsRightChildFrom:
						highlightLines(insertionSourceCode, 16);
						break;
					case CheckingIfToSetLeftInFinalStep:
						highlightLines(insertionSourceCode, 13);
						break;

					default:
						throw new EndOfTheWorldException();
					}
				}
			}
		});

		// text description during insertion
		model.addListener(new TreeEventListener<T>() {

			private void describe(String string) {
				nextStateOnLocation(string, DIRECTOR_MICROSTEP);
			}

			@Override
			public void update(TreeEvent<T> event) {
				if (event instanceof TreeInsertSourceCodeTraversing<?>) {
					TreeInsertSourceCodeTraversing<T> ie = (TreeInsertSourceCodeTraversing<T>) event;
					InsertSourceCodePosition position = ie.position;
					de.lere.vaad.treebuilder.Node<T> curPos = ie.currentPosition;
					T val = ie.insertionValue;
					switch (position) {

					case Init:
						nextStateOnLocation("Einfügen von " + val,
								DIRECTOR_MACROSTEP);
						describe("Initialisierung: die Suche wird an der Wurzel "
								+ curPos.getValue() + " begonnen ");
						break;
					case CheckingIfInsertionPossible:
						if (curPos == null) {
							describe("Nächste zu prüfende Position ist Null also wurde Platz zum Einfügen gefunden.");
						} else {
							describe("Solange kein Platz zum Einfügen gefunden wurde suche weiter um "
									+ val
									+ " einzufügen. "
									+ "Prüfe als nächstes " + curPos.getValue());
						}
						break;
					case TestingIfWhereToFromCurrent:
						describe("Teste ob Knoten " + val
								+ " links oder recht von " + curPos.getValue()
								+ " einzufügen ist.");
						break;
					case LookingAlongLeftChild:
						describe(val
								+ " war kleiner deswegen suche links weiter nach Ort zum Einfügen");

						break;
					case LookingAlongRightChild:
						describe(val
								+ " war größer deswegen suche rechts weiter nach Ort zum Einfügen");
						break;
					case SettingParentForNewCurrentNode:
						if (curPos == null) {
							describe("Der Ort zur Ersetzung hatte keinen Parent also wird für "
									+ val + " null als parent gesetzt");
						} else {
							describe(val + " bekommt einen neuen Parent: "
									+ curPos.getValue());
						}

						break;
					case CheckingIfNewIsRoot:
						describe("Prüfe ob " + val + " neuer root ist.");
						break;
					case FinalIsSettingToRoot:
						describe(val + " wird neue Wurzel.");
						break;
					case FinalIsSettingAsLeftChildFrom:
						describe("Knoten " + val + " als linkes Kind eingfügt");
						break;
					case FinalIsSettingAsRightChildFrom:
						describe("Knoten " + curPos.getValue()
								+ " als rechtes Kind eingfügt");
						break;
					case CheckingIfToSetLeftInFinalStep:
						describe("Prüfe ob Knoten " + val
								+ " links oder rechts von " + curPos.getValue()
								+ " eingefügt werden soll");
						break;

					default:
						throw new EndOfTheWorldException();
					}
				}
			}
		});

		// activate steps while traversing insertion algorithm
		model.addListener(new TreeEventListener<T>() {

			@Override
			public void update(TreeEvent<T> event) {
				if (event instanceof TreeInsertSourceCodeTraversing<?>) {
					step();
				}
			}
		});

		model.addListener(new TreeEventListener<T>() {

			@Override
			public void update(TreeEvent<T> event) {
				if (event instanceof TreeInsertEvent<?>) {
					TreeInsertEvent<T> e = (TreeInsertEvent<T>) event;
					int compStats = ((TreeInsertEvent<T>) event).insertionResult.numOfComparisons;
					step();
					nextStateOnLocation("Das Einfügen des Knotens "
							+ e.nodeOfModification.getValue() + " hat "
							+ compStats + " Vergleiche benötigt.",
							DIRECTOR_MICROSTEP);
				}

			}
		});

		// Intro ----------------------------------------
		nextStateOnLocation("Der Binäre Suchbaum", DIRECTOR_HEADER);

		nextStateOnLocation(INITIAL_DESCRIPTION, DIRECTOR_DESCRIPTION_BEGINNING);

		step();

		hideAll(animator);
		nextStateOnLocation("Beispiel eines binären Suchbaums",
				DIRECTOR_MACROSTEP);

		T rv = model.getRoot().getValue();
		nextStateOnLocation("Alle Knoten kleiner " + rv
				+ " befinden sich linkes von der Wurzel,\n"
				+ "alle Knoten größer " + rv + " rechts von der Wurzel.",
				DIRECTOR_MICROSTEP);

		animator.setModel(model);

		step();

		hideAll(animator);

		nextStateOnLocation(
				"Ein binärer Suchbaum ermöglicht das Einfügen, Entfernen und Suchen von Werten\n"
						+ "in durchschnittlich θ(lg h) Zeit und im schlechtesten Fall \n"
						+ "in maximal θ(h) Zeit, wobei h seine Höhe darstellt.",
				DIRECTOR_DESCRIPTION_BEGINNING);

		step();

		// Search if search array greater 0
		nextStateOnLocation(
				"Beispielhafte Darstellung der Suche eines Knotens",
				DIRECTOR_DESCRIPTION_BEGINNING);
		animator.setModel(model);

		step();

		nextStateOnLocation(
				"Die Suche im binären Suchbaum beginnt bei der Wurzel,\n"
						+ "vergleicht darauf ob der gesuchte Knoten größer oder kleiner ist und führt seine Suche entsprechend\n"
						+ "rechts oder links fort.", DIRECTOR_MACROSTEP);
		step();

		nextStateOnLocation(
				"Soll der Knoten V gesucht werden führt die Suche an J und P vorbei.",
				DIRECTOR_MICROSTEP);

		// model.search("V");
		//
		// step();
		//
		// nextStateOnLocation("Analog für C", DIRECTOR_MICROSTEP);
		//
		// step();
		//
		// model.search("C");
		//
		// step();
		//
		// nextStateOnLocation("und Q", DIRECTOR_MICROSTEP);
		//
		// step();
		//
		// model.search("Q");
		//
		// step();

		// Insert if insert array greater 0

		hideAll(null);
		nextStateOnLocation("Einfügens eines Knotens",
				DIRECTOR_DESCRIPTION_BEGINNING);
		nextStateOnLocation(
				"Beim Einfügen wird zuerst wie beim Suchen vorgegangen.\n"
						+ "Es wird versucht den einzufügenden Knoten zu suchen, wird dabei ein Blatt\n"
						+ "erreicht wird der Knoten dort wo erwartet worden wäre ihn zu finden eingefügt.",
				DIRECTOR_MACROSTEP);
		animator.setModel(model);
		insertionSourceCode.show();

		step();

		T[] insertionArray = getInsertionArray();
		for (T insEl : insertionArray) {
			model.insert(insEl);
		}

		step();

		// model.insert("T");
		//
		// step();
		//
		// nextStateOnLocation("Analog wird E rechts von D eingefügt ...",
		// DIRECTOR_MICROSTEP);
		//
		// step();
		//
		// model.insert("E");
		//
		// step();
		//
		// nextStateOnLocation("... I rechts von G", DIRECTOR_MICROSTEP);
		//
		// step();
		//
		// model.insert("I");
		//
		// step();
		//
		// nextStateOnLocation("und H links von I", DIRECTOR_MICROSTEP);
		//
		// step();
		//
		// model.insert("H");
		//
		// step();

		// Delete if delete array greater 0
		// Hide all
		hideAll(animator);
		insertionSourceCode.hide();
		nextStateOnLocation(
				"Beim löschen wird vorgeganen wie bei einem gewöhnlichen\n"
						+ "Binärbaum. Dabei werden drei Fälle unterschieden:\n"
						+ "1. Der zu löschende Knoten hat keine Kinder\n"
						+ "2. Der zu löschende Knoten hat ein Kind\n"
						+ "3. Der zu löschende Knoten hat zwei Kinder",
				DIRECTOR_DESCRIPTION_BEGINNING);

		step();

		nextStateOnLocation(
				"Beispielhafte Darstellung des Löschens eines Knotens.",
				DIRECTOR_DESCRIPTION_BEGINNING);

		animator.setModel(model);
		de.lere.vaad.treebuilder.Node<T> root = model.getRoot();
		while (root != null) {
			T value = root.getValue();
			if (root.hasLeftChild() && root.hasRightChild()) {
				nextStateOnLocation(
						"3. Der zu löschende Knoten hat zwei Kinder",
						DIRECTOR_MACROSTEP);
				nextStateOnLocation(
						"Löschen von "
								+ value
								+ ".\n"
								+ "Der zu löschende Knoten muss mit seinem direkten Nachfolger oder Vorgänger ersetzt werden.\n"
								+ "Die möglicherweise vorhandenen Unterbäume müssen entsprechend umgehängt werden.",
						DIRECTOR_MICROSTEP);
				step();
				model.delete(value);
				step();
			} else if (root.hasLeftChild() || root.hasRightChild()) {
				nextStateOnLocation(
						"2. Der zu löschende Knoten hat genau ein Kind\n",
						DIRECTOR_MACROSTEP);
				nextStateOnLocation(
						"Löschen von " + value + ".\n" +
						".Der zu löschende Knoten kann mit seinem Kind ersetzt werden.",
						DIRECTOR_MICROSTEP);
				step();
				model.delete(value);
				step();
			} else {
				nextStateOnLocation("1. Der zu löschende Knoten hat keine Kinder\n",
						DIRECTOR_MACROSTEP);
				nextStateOnLocation("Löschen des Knotens "+value+".\n"
						+ "Der Knoten kann entfernt werden ohne das zusätzliche\n"
						+ "Operationen ausgeführt werden müssen.", DIRECTOR_MICROSTEP);
				model.delete(value);
				step();
			}
			root = model.getRoot();
		}
		step();
		hideAll(animator);
		nextStateOnLocation("Eine Verfeinerung der Animationen analog zu Insertion folgt in 5.1", DIRECTOR_MACROSTEP);
		step();
	}

	private @Nullable
	T[] getInsertionArray() {
		return insertionArray;
	}

	private LocationDirector<Offset> createDescriptionBeginningLocDir(
			LocationDirector<Coordinates> headerPos) {
		Text headerDummyPrimitive = language.newText(headerPos.getLocation(),
				"12345678890", "headerDummyText", new Hidden());
		int textVerticalHeight = this.animationProperties.textVerticalHeight;
		Offset beginDescriptionLoc = new Offset(30, 2 * textVerticalHeight,
				headerDummyPrimitive, AnimalScript.DIRECTION_SE);
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

	public SourceCode getSourceCode() {
		if (sourceCode == null) {
			throw new IllegalStateException("no source code has been set yet");
		} else {
			return sourceCode;
		}
	}

	private void hideAll(TreeEventListenerAggregator<T> animator) {
		nextStateOnLocation("", DIRECTOR_DESCRIPTION_BEGINNING);
		nextStateOnLocation("", DIRECTOR_MACROSTEP);
		nextStateOnLocation("", DIRECTOR_MICROSTEP);
		if (animator != null) {
			animator.setModel(newEmptyModel());
		}
	}

	private BinaryTreeModel<T> newEmptyModel() {
		return new BinaryTreeModel<T>();
	}

	private void step() {
		language.nextStep();
	}

	private SourceCode getFilledSourceCode(String text, SourceCode sc) {
		return lh.getFilledSourceCode(text, sc);
	}

	private <U extends Node> void nextStateOnLocation(String string,
			LocationDirector<U> director) {
		lh.nextStateOnLocation(string, director);
	}

	private Group createTextGroup(String string, Node location) {
		return this.lh.createTextGroup(string, location);
	}

	public void setInsertionAnimation(T[] insertNodes) {
		this.insertionArray = insertNodes;
	}

	/**
	 * Whether to display the intro. Default is <code>true</code>.
	 * 
	 * @param show
	 */
	public void setShowIntro(boolean show) {
		this.showIntro = show;
	}

	public boolean isShowIntro() {
		return showIntro;
	}

}
