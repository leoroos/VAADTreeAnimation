package de.lere.vaad.animation.binarysearchtree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.items.AnimationPropertyItem;
import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.items.FontPropertyItem;
import algoanim.properties.items.IntegerPropertyItem;
import algoanim.properties.items.StringPropertyItem;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.animation.DefaultVisibilityEventListener;
import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.GraphWriterImpl;
import de.lere.vaad.animation.SourceCodeWriter;
import de.lere.vaad.animation.StepWriter;
import de.lere.vaad.animation.Timings;
import de.lere.vaad.animation.locationhandler.LocationDirector;
import de.lere.vaad.animation.locationhandler.LocationHandler;
import de.lere.vaad.animation.locationhandler.NextStateOnLocationDirector;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.events.DefaultTreeModelChangeEventListener;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.utils.MathHelper;
import de.lere.vaad.utils.NodeHelper;
import de.lere.vaad.utils.TextLoaderUtil;

public class BinarySearchTreeAnimation<T extends Comparable<T>> {

	public interface Mission<T> {

		void accomplish(T arg);

	}

	private static final Point GRAPHROOT_COORDINATES = new Point(400, 300);
	private static final int MIN_NODES_DISTANCE = 10;
	private static final int MAX_GRAPH_WIDTH = 1920;
	private static final int DEFAULT_GRAPH_WIDTH = 640;

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

	private final BinaryTreeProperties btProps;

	public final LocationDirector<Offset> DIRECTOR_DESCRIPTION_BEGINNING;
	public final LocationDirector<Coordinates> DIRECTOR_GRAPHROOT;
	public final LocationDirector<Coordinates> DIRECTOR_HEADER;
	public final LocationDirector<Offset> DIRECTOR_MACROSTEP;
	public final LocationDirector<Offset> DIRECTOR_MICROSTEP;
	public final LocationDirector<Coordinates> DIRECTOR_SMALLISH_SOURCECODE;
	public final LocationDirector<Coordinates> DIRECTOR_LONGER_SOURCECODE;

	private final Language language;
	private BinaryTreeLayout layout;

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
	private StepWriter stepWriter;
	private Timings timings;

	public BinarySearchTreeAnimation(Language l, BinaryTreeProperties btp,
			T[] initialTree) {
		if (l == null || btp == null) {
			throw new IllegalArgumentException("no null values allowed");
		}
		if (initialTree == null) {
			throw new IllegalArgumentException(
					"null not allowed for initial tree. Use empty array instead");
		}
		this.language = l;
		this.btProps = btp;
		l.setStepMode(true);

		BinaryTreeModel<T> model = BinaryTreeModel
				.createTreeByInsert(initialTree);
		int preferredFirstLevelWidthConsideringGraphSize = calculateOptimalFirstLevelWidth(model);
		Point graphLocation = getOptimalGraphPositionForFirstLevelWidth(preferredFirstLevelWidthConsideringGraphSize);

		this.layout = new BinaryTreeLayout(graphLocation,
				preferredFirstLevelWidthConsideringGraphSize, 60,
				btp.getGraphProperties(), this.getClass().getSimpleName()
						+ "Graph");
		this.lh = new LocationHandler(this.language, btp);
		this.textLoader = new TextLoaderUtil(getClass(), "resources");
		this.INITIAL_DESCRIPTION = textLoader.getText("initialDescription.txt");
		this.initialTree = initialTree;
		this.stepWriter = new StepWriter(language);
		this.timings = new Timings();

		DIRECTOR_HEADER = createHeaderLocDir();
		DIRECTOR_DESCRIPTION_BEGINNING = createDescriptionBeginningLocDir(DIRECTOR_HEADER);
		DIRECTOR_MACROSTEP = createMacroStepLocDir(DIRECTOR_DESCRIPTION_BEGINNING);
		DIRECTOR_MICROSTEP = createMicroStepLocDir(DIRECTOR_MACROSTEP);
		DIRECTOR_GRAPHROOT = createDirectorGraphroot();
		DIRECTOR_SMALLISH_SOURCECODE = createSourceCodeLocDir();
		DIRECTOR_LONGER_SOURCECODE = createLongerSourceCodeLocDir();

	}

	private LocationDirector<Coordinates> createLongerSourceCodeLocDir() {

		Point orientationAnchor = this.layout.getNEBoundary();
		Point avoidingGraphSourceOverlappingPoint = new Point(
				orientationAnchor.x + MIN_NODES_DISTANCE,
				GRAPHROOT_COORDINATES.y >> 1);
		return new NextStateOnLocationDirector<Coordinates>(
				Node.convertToNode(avoidingGraphSourceOverlappingPoint));
	}

	private int calculateOptimalFirstLevelWidth(BinaryTreeModel<T> model) {
		int height = model.height();
		int maxNodes = MathHelper.getMaxNumberOfNodesForLevel(height);
		int optimalFirstLevelWidth = maxNodes * MIN_NODES_DISTANCE / 4;
		int maxFirstLevelWidth = MAX_GRAPH_WIDTH / 4;
		int optimalAllowedFirstLevelWidth = Math.min(optimalFirstLevelWidth,
				maxFirstLevelWidth);
		int preferredFirstLevelWidth = DEFAULT_GRAPH_WIDTH / 4;
		int preferredFirstLevelWidthConsideringGraphSize = Math.max(
				preferredFirstLevelWidth, optimalAllowedFirstLevelWidth);
		return preferredFirstLevelWidthConsideringGraphSize;
	}

	protected Point getOptimalGraphPositionForFirstLevelWidth(int i) {
		int horizontalMinimalXforfullyVisibleGraph = i * 2;
		int maxXlocation = Math.max(horizontalMinimalXforfullyVisibleGraph,
				GRAPHROOT_COORDINATES.x);
		Point point = new Point(maxXlocation, GRAPHROOT_COORDINATES.y);
		return point;
	}

	/**
	 * Builds the animation according to configuration
	 */
	public void buildAnimation() {

		final BinaryTreeModel<T> model = BinaryTreeModel
				.createTreeByInsert(initialTree);

		GraphWriter<T> writer = new GraphWriterImpl<T>(language, layout);
		SourceCodeWriter sourceCodeWriter = new SourceCodeWriter(language,
				this.btProps.getSourceCodeProperties(),
				DIRECTOR_SMALLISH_SOURCECODE, timings);

		/* default animations for tree changes */
		model.addListener(new DefaultTreeModelChangeEventListener<T>(
				createBinaryTreeSetup(writer, sourceCodeWriter)));
		model.addListener(new DefaultVisibilityEventListener<T>(writer));

		/* omnipotent Header */
		createHeaderText(btProps.title, Color.GREEN, DIRECTOR_HEADER.getLocation());

		/* Optional intro */
		if (isShowIntro()) {
			putDescriptionUp();
			step();
			hideAllDescriptions();
		}

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
					this.btProps.getSourceCodeProperties(),
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

		hideAll(model, null);
		nextStateOnLocation(
				"Keine weiteren Animationen konfiguriert.",
				DIRECTOR_MACROSTEP);
		step();
	}

	private void createHeaderText(String title, Color color, Node location) {
		TextProperties headerProps = getHeaderTextProperties(btProps
				.getTextProperties());		
		Text headerText = language.newText(location, title, "headerText", null, headerProps);
		
		Offset downright = new Offset(0, 0, headerText, "SE");
		Offset upLeft = new Offset(0, 0, headerText, "NW");
		RectProperties rectProperties = new RectProperties();
		rectProperties.set("fillColor", color);
		rectProperties.set("filled", true);
		rectProperties.set("depth", 2);
		language.newRect(upLeft, downright, "boxBehindHeader", null, rectProperties);
	}

	private TextProperties getHeaderTextProperties(TextProperties textProperties) {
		TextProperties copyTps = copyOfTextProperties(textProperties);
		Font afon = (Font) copyTps.get("font");
		Font biggerFon = new Font(afon.getName(), afon.getStyle(),
				afon.getSize() << 1);
		copyTps.set("font", biggerFon);
		return copyTps;
	}

	private TextProperties copyOfTextProperties(TextProperties textProperties) {
		ColorPropertyItem color = (ColorPropertyItem) textProperties
				.getItem("color");
		BooleanPropertyItem centered = (BooleanPropertyItem) textProperties
				.getItem("centered");
		FontPropertyItem font = (FontPropertyItem) textProperties
				.getItem("font");
		IntegerPropertyItem depth = (IntegerPropertyItem) textProperties
				.getItem("depth");
		StringPropertyItem nameStr = (StringPropertyItem) textProperties
				.getItem("name");

		
		
		TextProperties copyTps = new TextProperties("CopyOf"
				+ (String) nameStr.get());

		copyTps.setDefault("color", color);
		copyTps.setDefault("centered", centered);
		copyTps.setDefault("font", font);
		copyTps.setDefault("hidden", new BooleanPropertyItem(true));
		copyTps.setDefault("depth", depth);
		return copyTps;
	}

	private void equipModelAccomplishMissionReturnTraceless(
			final BinaryTreeModel<T> modelOfOperation, Mission<T> mission,
			T[] targetArray, TreeEventListener<?>... animations) {

		ArrayList<TreeEventListener<T>> arming = new ArrayList<TreeEventListener<T>>();
		for (TreeEventListener<?> treeEventListener : animations) {
			/* got to be <T> */
			@SuppressWarnings("unchecked")
			TreeEventListener<T> treeEventListenerT = (TreeEventListener<T>) treeEventListener;
			arming.add(treeEventListenerT);
		}
		this.equipModelAccomplishMissionReturnTraceless(modelOfOperation,
				mission, targetArray, arming);

	}

	private void equipModelAccomplishMissionReturnTraceless(
			final BinaryTreeModel<T> modelOfOperation, Mission<T> mission,
			T[] targetArray, ArrayList<TreeEventListener<T>> animationArming) {

		// new TreeEventListener<T>() {
		//
		// @Override
		// public void update(TreeEvent<T> event) {
		// if (event instanceof TreeModelChangeEvent<?>) {
		// nextStateOnLocation("Noch zu bearbeiten: ",
		// DIRECTOR_DESCRIPTION_BEGINNING);
		// }
		// }
		//
		// };

		for (TreeEventListener<T> treeEventListener : animationArming) {
			modelOfOperation.addListener(treeEventListener);
		}

		modelOfOperation.show();

		// Iterate Command over model
		performForEach(mission, targetArray);

		/*
		 * hide before removing listener to allow them to perform according
		 * steps
		 */
		hideAll(modelOfOperation, null);
		for (TreeEventListener<T> treeEventListener : animationArming) {
			modelOfOperation.removeListener(treeEventListener);
		}
	}

	private void putDescriptionUp() {
		nextStateOnLocation(INITIAL_DESCRIPTION, DIRECTOR_DESCRIPTION_BEGINNING);

		nextStateOnLocation(
				"Ein binärer Suchbaum ermöglicht das Einfügen, Entfernen und Suchen von Werten\n"
						+ "in durchschnittlich θ(lg h) Zeit und im schlechtesten Fall \n"
						+ "in maximal θ(h) Zeit, wobei h seine Höhe darstellt.",
				DIRECTOR_MICROSTEP);
	}

	private BinarySearchTreeSetup<T> createBinaryTreeSetup(
			GraphWriter<T> writer, SourceCodeWriter scw) {
		BinarySearchTreeSetup<T> p = new BinarySearchTreeSetup<T>();
		p.setCoarseDescription(DIRECTOR_MACROSTEP);
		p.setFineDescription(DIRECTOR_MICROSTEP);
		p.setLang(language);
		p.setLh(lh);
		p.setBinaryTreeProperties(this.btProps);
		p.setSourceCodeAnchor(scw);
		p.setStepWriter(new StepWriter(language));
		p.setWriter(writer);
		return p;
	}

	private void performForEach(Mission<T> insCommand, T[] anElementArray) {
		List<Mission<T>> result = new ArrayList<Mission<T>>();
		result.add(insCommand);
		performForEach(result, anElementArray);

	}

	private void performForEach(List<Mission<T>> missions, T[] anElementArray) {
		if (hasElements(anElementArray)) {
			for (T insEl : anElementArray) {
				for (Mission<T> mission : missions) {
					mission.accomplish(insEl);
				}
			}
		}
	}

	private boolean hasElements(T[] anElementArray) {
		return anElementArray != null && anElementArray.length > 0;
	}

	private LocationDirector<Offset> createDescriptionBeginningLocDir(
			LocationDirector<Coordinates> headerPos) {
		Text headerDummyPrimitive = language.newText(headerPos.getLocation(),
				"12345678890", "headerDummyText", new Hidden());
		int textVerticalHeight = this.btProps.textVerticalHeight;
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
		// SourceCodeProperties scProps = new SourceCodeProperties();
		// SourceCode sc = language.newSourceCode(
		// beginDescriptionLoc.getLocation(), "algorithmOperations", null,
		// scProps);
		String codeGroupText = " \n \n \n";

		Group createTextGroup = lh.createTextGroup(codeGroupText,
				beginDescriptionLoc.getLocation());

		Offset macroStepLocation = new Offset(0,
				this.btProps.textVerticalHeight, createTextGroup,
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
		Point orientationAnchor = this.layout.getNEBoundary();
		Point avoidingGraphSourceOverlappingPoint = new Point(
				orientationAnchor.x + MIN_NODES_DISTANCE, orientationAnchor.y);
		return new NextStateOnLocationDirector<Coordinates>(
				Node.convertToNode(avoidingGraphSourceOverlappingPoint));
	}

	private void hideAll(@Nullable BinaryTreeModel<T> model,
			@Nullable SourceCode code) {
		hideAllDescriptions();
		if (model != null) {
			model.hide();
		}
		if (code != null) {
			code.hide();
		}
	}

	private void hideAllDescriptions() {
		nextStateOnLocation("", DIRECTOR_DESCRIPTION_BEGINNING);
		nextStateOnLocation("", DIRECTOR_MACROSTEP);
		nextStateOnLocation("", DIRECTOR_MICROSTEP);
	}

	private void step() {
		this.stepWriter.step();
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

	public void setSearchAnimation(T[] searchArray) {
		this.searchArray = searchArray;
	}

	public void setDeleteAnimation(T[] deleteArray) {
		this.deleteArray = deleteArray;
	}

}
