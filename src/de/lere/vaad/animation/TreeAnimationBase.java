package de.lere.vaad.animation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
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
import de.lere.vaad.animation.binarysearchtree.BinarySearchTreeSetup;
import de.lere.vaad.animation.locationhandler.LocationDirector;
import de.lere.vaad.animation.locationhandler.LocationHandler;
import de.lere.vaad.animation.locationhandler.NextStateOnLocationDirector;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.utils.MathHelper;
import de.lere.vaad.utils.NodeHelper;

public abstract class TreeAnimationBase<T extends Comparable<T>> {

	public interface Mission<T> {

		void accomplish(T arg);

	}

	private static final int DEFAULT_GRAPH_WIDTH = 640;
	protected static final Point GRAPHROOT_COORDINATES = new Point(400, 300);
	public static final int MAX_GRAPH_WIDTH = 1920;
	public static final int MIN_NODES_DISTANCE = 10;
	protected final BinaryTreeProperties btProps;
	@Nullable
	protected T[] deleteArray;
	protected final LocationDirector<Offset> DIRECTOR_DESCRIPTION_BEGINNING;
	protected final LocationDirector<Coordinates> DIRECTOR_GRAPHROOT;
	protected final LocationDirector<Coordinates> DIRECTOR_HEADER;
	protected final LocationDirector<Coordinates> DIRECTOR_LONGER_SOURCECODE;
	protected final LocationDirector<Offset> DIRECTOR_MACROSTEP;
	protected final LocationDirector<Offset> DIRECTOR_MICROSTEP;
	protected final LocationDirector<Coordinates> DIRECTOR_SMALLISH_SOURCECODE;
	protected final T[] initialTree;
	@Nullable
	protected T[] insertionArray;
	protected final Language language;
	protected BinaryTreeLayout layout;
	/**
	 * Container Object for properties of this Animation
	 */
	protected LocationHandler lh;
	@Nullable
	protected T[] searchArray;
	private boolean showIntro = true;
	private String INITIAL_DESCRIPTION;
	private StepWriter stepWriter;

	public TreeAnimationBase(Language l, BinaryTreeProperties btp,
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
		this.INITIAL_DESCRIPTION = getInitialDescription();
		this.initialTree = initialTree;
		this.stepWriter = new StepWriter(language);

		DIRECTOR_HEADER = createHeaderLocDir();
		DIRECTOR_DESCRIPTION_BEGINNING = createDescriptionBeginningLocDir(DIRECTOR_HEADER);
		DIRECTOR_MACROSTEP = createMacroStepLocDir(DIRECTOR_DESCRIPTION_BEGINNING);
		DIRECTOR_MICROSTEP = createMicroStepLocDir(DIRECTOR_MACROSTEP);
		DIRECTOR_GRAPHROOT = createDirectorGraphroot();
		DIRECTOR_SMALLISH_SOURCECODE = createSourceCodeLocDir();
		DIRECTOR_LONGER_SOURCECODE = createLongerSourceCodeLocDir();

	}

	protected BinaryTreeProperties getBinaryTreeProperties() {
		if (btProps == null)
			throw new IllegalStateException(
					"BinaryTreeProperties must have been set");
		return this.btProps;
	}

	protected int calculateOptimalFirstLevelWidth(BinaryTreeModel<T> model) {
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

	protected TextProperties copyOfTextProperties(TextProperties textProperties) {
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

	protected BinarySearchTreeSetup<T> createBinaryTreeSetup(
			GraphWriter<T> writer, SourceCodeWriter scw) {
		BinarySearchTreeSetup<T> p = new BinarySearchTreeSetup<T>();
		p.setCoarseDescription(DIRECTOR_MACROSTEP);
		p.setFineDescription(DIRECTOR_MICROSTEP);
		p.setLang(language);
		p.setLh(lh);
		p.setBinaryTreeProperties(getBinaryTreeProperties());
		p.setSourceCodeAnchor(scw);
		p.setStepWriter(new StepWriter(language));
		p.setWriter(writer);
		return p;
	}

	protected LocationDirector<Offset> createDescriptionBeginningLocDir(
			LocationDirector<Coordinates> headerPos) {
		Text headerDummyPrimitive = language.newText(headerPos.getLocation(),
				"12345678890", "headerDummyText", new Hidden());
		int textVerticalHeight = getBinaryTreeProperties().textVerticalHeight;
		Offset beginDescriptionLoc = new Offset(30, 2 * textVerticalHeight,
				headerDummyPrimitive, AnimalScript.DIRECTION_SE);
		NextStateOnLocationDirector<Offset> director = new NextStateOnLocationDirector<Offset>(
				beginDescriptionLoc);
		return director;
	}

	protected NextStateOnLocationDirector<Coordinates> createDirectorGraphroot() {
		Coordinates coordinates = NodeHelper
				.convertAWTPointToCoordinates(GRAPHROOT_COORDINATES);
		return new NextStateOnLocationDirector<Coordinates>(coordinates);
	}

	protected NextStateOnLocationDirector<Coordinates> createHeaderLocDir() {
		NextStateOnLocationDirector<Coordinates> director = new NextStateOnLocationDirector<Coordinates>(
				new Coordinates(10, 10));
		return director;
	}

	private TextProperties getHeaderTextProperties(TextProperties textProperties) {
		TextProperties copyTps = copyOfTextProperties(textProperties);
		Font afon = (Font) copyTps.get("font");
		Font biggerFon = new Font(afon.getName(), afon.getStyle(),
				afon.getSize() << 1);
		copyTps.set("font", biggerFon);
		return copyTps;
	}

	protected void createHeaderText(String title, Color color, Node location) {
		TextProperties headerProps = getHeaderTextProperties(btProps
				.getTextProperties());
		Text headerText = language.newText(location, title, "headerText", null,
				headerProps);

		Offset downright = new Offset(0, 0, headerText, "SE");
		Offset upLeft = new Offset(0, 0, headerText, "NW");
		RectProperties rectProperties = new RectProperties();
		rectProperties.set("fillColor", color);
		rectProperties.set("filled", true);
		rectProperties.set("depth", 2);
		language.newRect(upLeft, downright, "boxBehindHeader", null,
				rectProperties);
	}

	protected LocationDirector<Coordinates> createLongerSourceCodeLocDir() {

		Point orientationAnchor = this.layout.getNEBoundary();
		Point avoidingGraphSourceOverlappingPoint = new Point(
				orientationAnchor.x + MIN_NODES_DISTANCE,
				GRAPHROOT_COORDINATES.y >> 1);
		return new NextStateOnLocationDirector<Coordinates>(
				Node.convertToNode(avoidingGraphSourceOverlappingPoint));
	}

	protected NextStateOnLocationDirector<Offset> createMacroStepLocDir(
			LocationDirector<?> beginDescriptionLoc) {
		String codeGroupText = " \n \n \n";

		Group createTextGroup = lh.createTextGroup(codeGroupText,
				beginDescriptionLoc.getLocation());

		Offset macroStepLocation = new Offset(0,
				getBinaryTreeProperties().textVerticalHeight, createTextGroup,
				AnimalScript.DIRECTION_SW);

		NextStateOnLocationDirector<Offset> director = new NextStateOnLocationDirector<Offset>(
				macroStepLocation);
		return director;
	}

	protected NextStateOnLocationDirector<Offset> createMicroStepLocDir(
			LocationDirector<?> macroStep) {
		Group macroStepGroup = getLocationHandler().createTextGroup(
				" \n \n \n \n", macroStep.getLocation());

		Offset microStepLocation = new Offset(0, 5, macroStepGroup,
				AnimalScript.DIRECTION_SW);

		return new NextStateOnLocationDirector<Offset>(microStepLocation);
	}

	protected NextStateOnLocationDirector<Coordinates> createSourceCodeLocDir() {
		Point orientationAnchor = this.layout.getNEBoundary();
		Point avoidingGraphSourceOverlappingPoint = new Point(
				orientationAnchor.x + MIN_NODES_DISTANCE, orientationAnchor.y);
		return new NextStateOnLocationDirector<Coordinates>(
				Node.convertToNode(avoidingGraphSourceOverlappingPoint));
	}

	protected LocationHandler getLocationHandler() {
		return this.lh;
	}

	protected void equipModelAccomplishMissionReturnTraceless(
			final BinaryTreeModel<T> modelOfOperation,
			List<Mission<T>> mission, T[] targetArray,
			ArrayList<TreeEventListener<T>> animationArming) {

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
		hideAll(modelOfOperation);
		for (TreeEventListener<T> treeEventListener : animationArming) {
			modelOfOperation.removeListener(treeEventListener);
		}
	}
	
	public final void buildAnimation() {
		/* omnipotent Header */
		createHeaderText(getAnimationTitle(), Color.GREEN,
				DIRECTOR_HEADER.getLocation());
		
		firstDescriptions();
		doBuildAnimation();
		
		finalAnimationRemarks();
	}

	private void firstDescriptions() {
		if(isShowIntro()){
			putDescriptionUp();
		}
		step();
		hideAllDescriptions();
	}

	protected void finalAnimationRemarks() {
		hideAllDescriptions();
		lh.nextStateOnLocation("Keine weiteren Animationen konfiguriert.",
				DIRECTOR_MACROSTEP);
		step();
	}

	protected abstract String getAnimationTitle() ;

	protected abstract void doBuildAnimation();
	
	
	protected void hideAll(@Nullable BinaryTreeModel<?> model,
			Primitive... hideable) {
		hideAllDescriptions();
		if (model != null) {
			model.hide();
		}
		for (Primitive primitive : hideable) {
			if (primitive != null)
				primitive.hide();
		}
	}

	protected void equipModelAccomplishMissionReturnTraceless(
			final BinaryTreeModel<T> modelOfOperation,
			List<Mission<T>> mission, T[] targetArray,
			TreeEventListener<?>... animations) {

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

	public boolean isShowIntro() {
		return showIntro;
	}

	public void setDeleteAnimation(T[] deleteArray) {
		this.deleteArray = deleteArray;
	}

	public void setInsertionAnimation(T[] insertNodes) {
		this.insertionArray = insertNodes;
	}

	public void setSearchAnimation(T[] searchArray) {
		this.searchArray = searchArray;
	}

	/**
	 * Whether to display the intro. Default is <code>true</code>.
	 * 
	 * @param show
	 */
	public void setShowIntro(boolean show) {
		this.showIntro = show;
	}

	protected void performForEach(List<Mission<T>> missions, T[] anElementArray) {
		if (hasElements(anElementArray)) {
			for (T insEl : anElementArray) {
				for (Mission<T> mission : missions) {
					mission.accomplish(insEl);
				}
			}
		}
	}

	protected abstract String getInitialDescription();

	protected boolean hasElements(T[] anElementArray) {
		return anElementArray != null && anElementArray.length > 0;
	}
	

	protected void putDescriptionUp() {
		lh.nextStateOnLocation(INITIAL_DESCRIPTION, DIRECTOR_DESCRIPTION_BEGINNING);
	}
	

	private Point getOptimalGraphPositionForFirstLevelWidth(int i) {
		int horizontalMinimalXforfullyVisibleGraph = i * 2;
		int maxXlocation = Math.max(horizontalMinimalXforfullyVisibleGraph,
				GRAPHROOT_COORDINATES.x);
		Point point = new Point(maxXlocation, GRAPHROOT_COORDINATES.y);
		return point;
	}
	
	protected void step() {
		this.stepWriter.step();
	}
	
	protected void hideAllDescriptions() {
		getLocationHandler().nextStateOnLocation("", DIRECTOR_DESCRIPTION_BEGINNING);
		getLocationHandler().nextStateOnLocation("", DIRECTOR_MACROSTEP);
		getLocationHandler().nextStateOnLocation("", DIRECTOR_MICROSTEP);
	}
}