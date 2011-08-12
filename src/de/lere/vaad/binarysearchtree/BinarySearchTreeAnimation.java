package de.lere.vaad.binarysearchtree;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
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

public class BinarySearchTreeAnimation {

	private static final Point GRAPHROOT_COORDINATES = new Point(400, 300);

	private final String INITIAL_DESCRIPTION;

	public static void main(String[] args) {

		BinaryTreeProperties tps = new BinaryTreeProperties();
		tps.authors = "Leo Roos, Rene Hertling";
		tps.title = "Binary Search Tree";

		AnimalScript animalScript = new AnimalScript(tps.authors, tps.title,
				tps.screenResolution.width, tps.screenResolution.height);

		BinarySearchTreeAnimation binarySerachTreeAnimation = new BinarySearchTreeAnimation(
				animalScript, tps);
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

	public BinarySearchTreeAnimation(Language l, BinaryTreeProperties tp) {
		if(l == null || tp == null)
			throw new IllegalArgumentException("no null values allowed");
		this.language = l;
		this.animationProperties = tp;
		l.setStepMode(true);
		this.layout = new BinaryTreeLayout(GRAPHROOT_COORDINATES, 160, 60);
		this.lh = new LocationHandler(this.language, this.layout);
		this.textLoader = new TextLoaderUtil(getClass(), "resources");
		this.INITIAL_DESCRIPTION = textLoader.getText("initialDescription.txt");

		DIRECTOR_HEADER = createHeaderLocDir();
		DIRECTOR_DESCRIPTION_BEGINNING = createDescriptionBeginningLocDir(DIRECTOR_HEADER);
		DIRECTOR_MACROSTEP = createMacroStepLocDir(DIRECTOR_DESCRIPTION_BEGINNING);
		DIRECTOR_MICROSTEP = createMicroStepLocDir(DIRECTOR_MACROSTEP);
		DIRECTOR_GRAPHROOT = createDirectorGraphroot();
		DIRECTOR_SOURCECODE = createSourceCodeLocDir();

	}

	private void buildAnimation() {

		SourceCodeProperties codeProperties = new SourceCodeProperties();
		codeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.GREEN);
		final SourceCode sourceCode = language.newSourceCode(
				DIRECTOR_SOURCECODE.getLocation(), "insertionSource",
				new Hidden(), codeProperties);

		getFilledSourceCode(this.textLoader.getText("insertAlgo.txt"), sourceCode);
		sourceCode.hide();

		final BinaryTreeModel<String> model = BinaryTreeModel
				.createTreeByInsert("J", "F", "P", "D", "G", "L", "V", "C",
						"N", "S", "X", "Q", "U");
		// BinarySearchTreeAnimator<String> animator = new
		// BinarySearchTreeAnimator<String>(
		// language);

		final GraphWriterImpl<String> writer = new GraphWriterImpl<String>(
				language, layout);
		TreeEventListenerAggregator<String> animator = new TreeEventListenerAggregator<String>(
				language);
		animator.addAnimatior(new ExtractedBinaryTreeAnimations<String>(writer));
		animator.setLayout(this.layout);

		// Code highlighting during insertion
		model.addListener(new TreeEventListener<String>() {

			private int[] previouslyHighlighted = {};
			private de.lere.vaad.treebuilder.Node<String> previouslyHighlightedNode;
			private BinaryTreeModel<String> codeCurModel;
			private de.lere.vaad.treebuilder.Node<String> codeCurrentNodePos;

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
				highlightNode(codeCurrentNodePos , codeCurModel);
			}

			private void highlightNode(
					de.lere.vaad.treebuilder.Node<String> currentPosition,
					BinaryTreeModel<String> model) {
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
			public void update(TreeEvent<String> event) {
				if (event instanceof TreeInsertSourceCodeTraversing<?>) {
					TreeInsertSourceCodeTraversing<String> ie = (TreeInsertSourceCodeTraversing<String>) event;
					InsertSourceCodePosition curPos = ie.position;
					codeCurrentNodePos = ie.currentPosition;
					codeCurModel = ie.currentModel;
					switch (curPos) {

					case Init:
						highlightLines(sourceCode, 2, 3);

						break;
					case CheckingIfInsertionPossible:
						highlightLines(sourceCode, 4);

						break;
					case TestingIfWhereToFromCurrent:
						highlightLines(sourceCode, 6);
						break;
					case LookingAlongLeftChild:
						highlightLines(sourceCode, 7);

						break;
					case LookingAlongRightChild:
						highlightLines(sourceCode, 9);

						break;
					case SettingParentForNewCurrentNode:
						highlightLines(sourceCode, 10);
						
						break;
					case CheckingIfNewIsRoot:
						highlightLines(sourceCode, 11);
						break;
					case FinalIsSettingToRoot:
						highlightLines(sourceCode, 12);
						break;
					case FinalIsSettingAsLeftChildFrom:
						highlightLines(sourceCode, 14);
						break;
					case FinalIsSettingAsRightChildFrom:
						highlightLines(sourceCode, 16);
						break;
					case CheckingIfToSetLeftInFinalStep:
						highlightLines(sourceCode, 13);
						break;	

					default:
						throw new EndOfTheWorldException();
					}
				}
			}
		});

		// text description during insertion
		model.addListener(new TreeEventListener<String>() {

			private void describe(String string) {
				nextStateOnLocation(string, DIRECTOR_MICROSTEP);
			}

			@Override
			public void update(TreeEvent<String> event) {
				if (event instanceof TreeInsertSourceCodeTraversing<?>) {
					TreeInsertSourceCodeTraversing<String> ie = (TreeInsertSourceCodeTraversing<String>) event;
					InsertSourceCodePosition position = ie.position;
					de.lere.vaad.treebuilder.Node<String> curPos = ie.currentPosition;
					String val = ie.insertionValue;
					switch (position) {

					case Init:
						nextStateOnLocation("Einfügen von " + val,
								DIRECTOR_MACROSTEP);
						describe("Initialisierung: die Suche wird an der Wurzel "
								+ curPos.getValue() + " begonnen ");
						break;
					case CheckingIfInsertionPossible:
						if(curPos == null) {
							describe("Nächste zu prüfende Position ist Null also wurde Platz zum Einfügen gefunden.");
						} else {
							describe("Solange kein Platz zum Einfügen gefunden wurde suche weiter um "
									+ val
									+ " einzufügen. "
									+ "Prüfe als nächstes "
									+ curPos.getValue());
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
		model.addListener(new TreeEventListener<String>() {

			@Override
			public void update(TreeEvent<String> event) {
				if (event instanceof TreeInsertSourceCodeTraversing<?>) {
					step();
				}
			}
		});
		
		model.addListener(new TreeEventListener<String>() {

			@Override
			public void update(TreeEvent<String> event) {
				if(event instanceof TreeInsertEvent<?>){
					TreeInsertEvent<String> e = (TreeInsertEvent<String>) event;
					int compStats = ((TreeInsertEvent<String>) event).insertionResult.numOfComparisons;
					step();
					nextStateOnLocation("Das Einfügen des Knotens " + e.nodeOfModification.getValue() + " hat " + compStats + " Vergleiche benötigt.", DIRECTOR_MICROSTEP);
				}
				
			}
		});

		nextStateOnLocation("Der Binäre Suchbaum", DIRECTOR_HEADER);

		step();

		nextStateOnLocation(INITIAL_DESCRIPTION, DIRECTOR_DESCRIPTION_BEGINNING);

		step();

		hideAll(animator);
		nextStateOnLocation("Beispiel eines binären Suchbaums",
				DIRECTOR_MACROSTEP);
		nextStateOnLocation(
				"Alle Knoten kleiner J befinden sich linkes von der Wurzel,\n"
						+ "alle Knoten größer J rechts von der Wurzel.",
				DIRECTOR_MICROSTEP);

		animator.setModel(model);

		step();

		hideAll(animator);

		nextStateOnLocation(
				"Ein binärer Suchbaum ermöglicht das Einfügen, Entfernen und Suchen von Werten\n"
						+ "in maximal O(h) Zeit, wobei h seine Höhe darstellt.",
				DIRECTOR_DESCRIPTION_BEGINNING);

		step();

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

		model.search("V");

		step();

		nextStateOnLocation("Analog für C", DIRECTOR_MICROSTEP);

		step();

		model.search("C");

		step();

		nextStateOnLocation("und Q", DIRECTOR_MICROSTEP);

		step();

		model.search("Q");

		step();

		hideAll(null);
		nextStateOnLocation(
				"Beispielhafte Darstellung des Einfügens eines Knotens",
				DIRECTOR_DESCRIPTION_BEGINNING);
		nextStateOnLocation(
				"Beim Einfügen wird zuerst wie beim Suchen vorgegangen.\n"
						+ "Es wird versucht den einzufügenden Knoten zu suchen, wird dabei ein Blatt\n"
						+ "erreicht wird der Knoten dort wo erwartet worden wäre ihn zu finden eingefügt.",
				DIRECTOR_MACROSTEP);
		animator.setModel(model);
		sourceCode.show();

		step();

		nextStateOnLocation(
				"Knoten T ist größer als die Wurzel J wird also rechts davon einsortiert.\n"
						+ "T ist auch größer als P und wird rechts von diesem einsortiert,\n"
						+ "kleiner als V, größer als S und schließlich kleiner als U.",
				DIRECTOR_MICROSTEP);
		animator.setModel(model);

		step();

		model.insert("T");

		step();

		nextStateOnLocation("Analog wird E rechts von D eingefügt ...",
				DIRECTOR_MICROSTEP);

		step();

		model.insert("E");

		step();

		nextStateOnLocation("... I rechts von G", DIRECTOR_MICROSTEP);

		step();

		model.insert("I");

		step();

		nextStateOnLocation("und H links von I", DIRECTOR_MICROSTEP);

		step();

		model.insert("H");

		step();

		// Hide all
		hideAll(animator);
		sourceCode.hide();
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
		nextStateOnLocation("1. Der zu löschende Knoten hat keine Kinder\n",
				DIRECTOR_MACROSTEP);
		nextStateOnLocation("Zum Beispiel löschen des Knotens H.\n"
				+ "Der Knoten kann entfernt werden ohne zusätzliche\n"
				+ "Operationen ausführen zu müssen.", DIRECTOR_MICROSTEP);
		animator.setModel(model);
		step();

		model.delete("H");

		step();

		nextStateOnLocation("2. Der zu löschende Knoten hat genau ein Kind\n",
				DIRECTOR_MACROSTEP);
		nextStateOnLocation(
				"Zum Beispiel löschen von U.\nDer zu löschende Knoten kann mit seinem Kind ersetzt werden.",
				DIRECTOR_MICROSTEP);
		step();

		model.delete("U");

		step();

		nextStateOnLocation("3. Der zu löschende Knoten hat zwei Kinder",
				DIRECTOR_MACROSTEP);
		nextStateOnLocation(
				"Zum Beispiel löschen von J.\n"
						+ "Der zu löschende Knoten muss mit seinem direkten Nachfolger oder Vorgänger ersetzt werden.\n"
						+ "Die möglicherweise vorhandenen Unterbäume müssen entsprechend umgehängt werden.",
				DIRECTOR_MICROSTEP);
		step();

		model.delete("J");
		step();

		nextStateOnLocation("Analog für F ...", DIRECTOR_MICROSTEP);
		step();

		model.delete("F");

		step();

		nextStateOnLocation("... und für P", DIRECTOR_MICROSTEP);
		step();

		model.delete("P");

		step();

		hideAll(animator);
		nextStateOnLocation("Ende", DIRECTOR_MACROSTEP);
		step();
	}

	private LocationDirector<Offset> createDescriptionBeginningLocDir(
			LocationDirector<Coordinates> headerPos) {
		Text headerDummyPrimitive = language.newText(headerPos.getLocation(),
				"12345678890", "headerDummyText", new Hidden());
		int textVerticalHeight = this.animationProperties.textVerticalHeight;
		Offset beginDescriptionLoc = new Offset(30,
				2 * textVerticalHeight, headerDummyPrimitive,
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

	public SourceCode getSourceCode() {
		if (sourceCode == null) {
			throw new IllegalStateException("no source code has been set yet");
		} else {
			return sourceCode;
		}
	}

	private void hideAll(TreeEventListenerAggregator<String> animator) {
		nextStateOnLocation("", DIRECTOR_DESCRIPTION_BEGINNING);
		nextStateOnLocation("", DIRECTOR_MACROSTEP);
		nextStateOnLocation("", DIRECTOR_MICROSTEP);
		if (animator != null) {
			animator.setModel(newEmptyModel());
		}
	}

	private BinaryTreeModel<String> newEmptyModel() {
		return new BinaryTreeModel<String>();
	}

	private void step() {
		language.nextStep();
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
