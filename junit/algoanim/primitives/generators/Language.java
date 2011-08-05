/*jadclipse*/package algoanim.primitives.generators;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import algoanim.exceptions.NotEnoughNodesException;
import algoanim.interactionsupport.DocumentationLink;
import algoanim.interactionsupport.FillInBlanksQuestion;
import algoanim.interactionsupport.GroupInfo;
import algoanim.interactionsupport.MultipleChoiceQuestion;
import algoanim.interactionsupport.MultipleSelectionQuestion;
import algoanim.interactionsupport.TrueFalseQuestion;
import algoanim.primitives.Arc;
import algoanim.primitives.ArrayBasedQueue;
import algoanim.primitives.ArrayBasedStack;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.Circle;
import algoanim.primitives.CircleSeg;
import algoanim.primitives.ConceptualQueue;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Ellipse;
import algoanim.primitives.EllipseSeg;
import algoanim.primitives.Graph;
import algoanim.primitives.Group;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.ListBasedStack;
import algoanim.primitives.ListElement;
import algoanim.primitives.Point;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.Variables;
import algoanim.primitives.vhdl.AndGate;
import algoanim.properties.ArcProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CircleSegProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.EllipseSegProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.ListElementProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.QueueProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.StackProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.properties.VHDLElementProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

public abstract class Language {
	public Language(String title, String author, int x, int y) {

		/* 106 */interactiveElements = new HashMap(89);

		/* 111 */hideInThisStep = new Vector(21, 15);

		/* 116 */showInThisStep = new Vector(21, 15);
	}

	public abstract void addLine(StringBuilder stringbuilder);

	public void addLine(String line) {

		/* 137 */addLine(new StringBuilder(line));
	}

	public abstract void addError(StringBuilder stringbuilder);

	public void addError(String error) {

		/* 155 */addError(new StringBuilder(error));
	}

	public abstract void addItem(Primitive primitive);

	public abstract void writeFile(String s);

	public abstract void finalizeGeneration();

	public abstract String getAnimationCode();

	public abstract int getStep();

	public abstract boolean isNameUsed(String s);

	public abstract Vector validDirections();

	public abstract boolean isValidDirection(String s);

	public abstract void setStepMode(boolean flag);

	public void nextStep() {

		/* 240 */nextStep(-1, null);
	}

	public void nextStep(int delay) {

		/* 254 */nextStep(delay, null);
	}

	public void nextStep(String label) {

		/* 268 */nextStep(-1, label);
	}

	public abstract void nextStep(int i, String s);

	public abstract Point newPoint(Node node, String s,
			DisplayOptions displayoptions, PointProperties pointproperties);

	public Arc newArc(Node center, Node radius, String name,
			DisplayOptions display) {

		/* 337 */return newArc(center, radius, name, display,
				new ArcProperties());
	}

	public abstract Arc newArc(Node node, Node node1, String s,
			DisplayOptions displayoptions, ArcProperties arcproperties);

	public Circle newCircle(Node center, int radius, String name,
			DisplayOptions display) {

		/* 382 */return newCircle(center, radius, name, display,
				new CircleProperties());
	}

	public abstract Circle newCircle(Node node, int i, String s,
			DisplayOptions displayoptions, CircleProperties circleproperties);

	public abstract CircleSeg newCircleSeg(Node node, int i, String s,
			DisplayOptions displayoptions,
			CircleSegProperties circlesegproperties);

	public EllipseSeg newEllipseSeg(Node center, Node radius, String name,
			DisplayOptions display) {

		/* 454 */return newEllipseSeg(center, radius, name, display,
				new EllipseSegProperties());
	}

	public abstract EllipseSeg newEllipseSeg(Node node, Node node1, String s,
			DisplayOptions displayoptions,
			EllipseSegProperties ellipsesegproperties);

	public abstract Ellipse newEllipse(Node node, Node node1, String s,
			DisplayOptions displayoptions, EllipseProperties ellipseproperties);

	public Ellipse newEllipse(Node center, Node radius, String name,
			DisplayOptions display) {

		/* 528 */return newEllipse(center, radius, name, display,
				new EllipseProperties());
	}

	public CircleSeg newCircleSeg(Node center, int radius, String name,
			DisplayOptions display) {

		/* 550 */return newCircleSeg(center, radius, name, display,
				new CircleSegProperties());
	}

	public abstract Graph newGraph(String s, int ai[][], Node anode[],
			String as[], DisplayOptions displayoptions,
			GraphProperties graphproperties);

	public Graph newGraph(String name, int graphAdjacencyMatrix[][],
			Node graphNodes[], String labels[], DisplayOptions display) {

		/* 602 */return newGraph(name, graphAdjacencyMatrix, graphNodes,
				labels, display, new GraphProperties());
	}

	public abstract Polyline newPolyline(Node anode[], String s,
			DisplayOptions displayoptions, PolylineProperties polylineproperties);

	public Polyline newPolyline(Node vertices[], String name,
			DisplayOptions display) {

		/* 649 */return newPolyline(vertices, name, display,
				new PolylineProperties());
	}

	public abstract Polygon newPolygon(Node anode[], String s,
			DisplayOptions displayoptions, PolygonProperties polygonproperties)
			throws NotEnoughNodesException;

	public Polygon newPolygon(Node vertices[], String name,
			DisplayOptions display) throws NotEnoughNodesException {

		/* 697 */return newPolygon(vertices, name, display,
				new PolygonProperties());
	}

	public abstract Rect newRect(Node node, Node node1, String s,
			DisplayOptions displayoptions, RectProperties rectproperties);

	public Rect newRect(Node upperLeft, Node lowerRight, String name,
			DisplayOptions display) {

		/* 748 */return newRect(upperLeft, lowerRight, name, display,
				new RectProperties());
	}

	public abstract Square newSquare(Node node, int i, String s,
			DisplayOptions displayoptions, SquareProperties squareproperties);

	public Square newSquare(Node upperLeft, int width, String name,
			DisplayOptions display) {

		/* 795 */return newSquare(upperLeft, width, name, display,
				new SquareProperties());
	}

	public abstract Triangle newTriangle(Node node, Node node1, Node node2,
			String s, DisplayOptions displayoptions,
			TriangleProperties triangleproperties);

	public abstract Variables newVariables();

	public Triangle newTriangle(Node x, Node y, Node z, String name,
			DisplayOptions display) {

		/* 852 */return newTriangle(x, y, z, name, display,
				new TriangleProperties());
	}

	public abstract DoubleArray newDoubleArray(Node node, double ad[],
			String s, ArrayDisplayOptions arraydisplayoptions,
			ArrayProperties arrayproperties);

	public DoubleArray newDoubleArray(Node upperLeft, double data[],
			String name, ArrayDisplayOptions display) {

		/* 899 */return newDoubleArray(upperLeft, data, name, display,
				new ArrayProperties());
	}

	public abstract IntArray newIntArray(Node node, int ai[], String s,
			ArrayDisplayOptions arraydisplayoptions,
			ArrayProperties arrayproperties);

	public IntArray newIntArray(Node upperLeft, int data[], String name,
			ArrayDisplayOptions display) {

		/* 945 */return newIntArray(upperLeft, data, name, display,
				new ArrayProperties());
	}

	public abstract DoubleMatrix newDoubleMatrix(Node node, double ad[][],
			String s, DisplayOptions displayoptions,
			MatrixProperties matrixproperties);

	public abstract IntMatrix newIntMatrix(Node node, int ai[][], String s,
			DisplayOptions displayoptions, MatrixProperties matrixproperties);

	public DoubleMatrix newDoubleMatrix(Node upperLeft, double data[][],
			String name, DisplayOptions display) {

		/* 1017 */return newDoubleMatrix(upperLeft, data, name, display,
				new MatrixProperties());
	}

	public IntMatrix newIntMatrix(Node upperLeft, int data[][], String name,
			DisplayOptions display) {

		/* 1039 */return newIntMatrix(upperLeft, data, name, display,
				new MatrixProperties());
	}

	public abstract StringArray newStringArray(Node node, String as[],
			String s, ArrayDisplayOptions arraydisplayoptions,
			ArrayProperties arrayproperties);

	public StringArray newStringArray(Node upperLeft, String data[],
			String name, ArrayDisplayOptions display) {

		/* 1086 */return newStringArray(upperLeft, data, name, display,
				new ArrayProperties());
	}

	public abstract StringMatrix newStringMatrix(Node node, String as[][],
			String s, DisplayOptions displayoptions,
			MatrixProperties matrixproperties);

	public StringMatrix newStringMatrix(Node upperLeft, String data[][],
			String name, DisplayOptions display) {

		/* 1133 */return newStringMatrix(upperLeft, data, name, display,
				new MatrixProperties());
	}

	public abstract ArrayMarker newArrayMarker(ArrayPrimitive arrayprimitive,
			int i, String s, DisplayOptions displayoptions,
			ArrayMarkerProperties arraymarkerproperties);

	public ArrayMarker newArrayMarker(ArrayPrimitive a, int index, String name,
			DisplayOptions display) {

		/* 1183 */return newArrayMarker(a, index, name, display,
				new ArrayMarkerProperties());
	}

	public abstract ListElement newListElement(Node node, int i,
			LinkedList linkedlist, ListElement listelement,
			ListElement listelement1, String s, DisplayOptions displayoptions,
			ListElementProperties listelementproperties);

	public ListElement newListElement(Node upperLeft, int pointers,
			LinkedList ptrLocations, ListElement prev, String name,
			DisplayOptions display, ListElementProperties lp) {

		/* 1237 */return newListElement(upperLeft, pointers, ptrLocations,
				prev, null, name, display, lp);
	}

	public ListElement newListElement(Node upperLeft, int pointers,
			LinkedList ptrLocations, ListElement prev, String name,
			DisplayOptions display) {

		/* 1261 */return newListElement(upperLeft, pointers, ptrLocations,
				prev, null, name, display, new ListElementProperties());
	}

	public abstract SourceCode newSourceCode(Node node, String s,
			DisplayOptions displayoptions,
			SourceCodeProperties sourcecodeproperties);

	public SourceCode newSourceCode(Node upperLeft, String name,
			DisplayOptions display) {

		/* 1305 */return newSourceCode(upperLeft, name, display,
				new SourceCodeProperties());
	}

	public abstract Text newText(Node node, String s, String s1,
			DisplayOptions displayoptions, TextProperties textproperties);

	public Text newText(Node upperLeft, String text, String name,
			DisplayOptions display) {

		/* 1352 */Text aText = newText(upperLeft, text, name, display,
				new TextProperties());
		/* 1353 */return aText;
	}

	public AndGate newAndGate(Node upperLeft, int width, int height,
			String name, List pins, DisplayOptions display) {

		/* 1378 */AndGate andGate = newAndGate(upperLeft, width, height, name,
				pins, display, new VHDLElementProperties());

		/* 1380 */return andGate;
	}

	public abstract AndGate newAndGate(Node node, int i, int j, String s,
			List list, DisplayOptions displayoptions,
			VHDLElementProperties vhdlelementproperties);

	public abstract ConceptualStack newConceptualStack(Node node, List list,
			String s, DisplayOptions displayoptions,
			StackProperties stackproperties);

	public ConceptualStack newConceptualStack(Node upperLeft, List content,
			String name, DisplayOptions display) {

		/* 1458 */return newConceptualStack(upperLeft, content, name, display,
				new StackProperties());
	}

	public abstract ArrayBasedStack newArrayBasedStack(Node node, List list,
			String s, DisplayOptions displayoptions,
			StackProperties stackproperties, int i);

	public ArrayBasedStack newArrayBasedStack(Node upperLeft, List content,
			String name, DisplayOptions display, int capacity) {

		/* 1519 */return newArrayBasedStack(upperLeft, content, name, display,
				new StackProperties(), capacity);
	}

	public abstract ListBasedStack newListBasedStack(Node node, List list,
			String s, DisplayOptions displayoptions,
			StackProperties stackproperties);

	public ListBasedStack newListBasedStack(Node upperLeft, List content,
			String name, DisplayOptions display) {

		/* 1573 */return newListBasedStack(upperLeft, content, name, display,
				new StackProperties());
	}

	public abstract ConceptualQueue newConceptualQueue(Node node, List list,
			String s, DisplayOptions displayoptions,
			QueueProperties queueproperties);

	public ConceptualQueue newConceptualQueue(Node upperLeft, List content,
			String name, DisplayOptions display) {

		/* 1627 */return newConceptualQueue(upperLeft, content, name, display,
				new QueueProperties());
	}

	public abstract ArrayBasedQueue newArrayBasedQueue(Node node, List list,
			String s, DisplayOptions displayoptions,
			QueueProperties queueproperties, int i);

	public ArrayBasedQueue newArrayBasedQueue(Node upperLeft, List content,
			String name, DisplayOptions display, int capacity) {

		/* 1688 */return newArrayBasedQueue(upperLeft, content, name, display,
				new QueueProperties(), capacity);
	}

	public abstract ListBasedQueue newListBasedQueue(Node node, List list,
			String s, DisplayOptions displayoptions,
			QueueProperties queueproperties);

	public ListBasedQueue newListBasedQueue(Node upperLeft, List content,
			String name, DisplayOptions display) {

		/* 1742 */return newListBasedQueue(upperLeft, content, name, display,
				new QueueProperties());
	}

	public abstract Group newGroup(LinkedList linkedlist, String s);

	public abstract void addDocumentationLink(
			DocumentationLink documentationlink);

	public abstract void addTFQuestion(TrueFalseQuestion truefalsequestion);

	public abstract void addFIBQuestion(
			FillInBlanksQuestion fillinblanksquestion);

	public abstract void addMCQuestion(
			MultipleChoiceQuestion multiplechoicequestion);

	public abstract void addMSQuestion(
			MultipleSelectionQuestion multipleselectionquestion);

	public abstract void addQuestionGroup(GroupInfo groupinfo);

	public abstract void setInteractionType(int i);

	public abstract void hideAllPrimitives();

	public abstract void hideAllPrimitivesExcept(Primitive primitive);

	public abstract void hideAllPrimitivesExcept(List list);

	public static final int INTERACTION_TYPE_NONE = 128;
	public static final int INTERACTION_TYPE_JHAVE_TEXT = 256;
	public static final int INTERACTION_TYPE_JHAVE_XML = 512;
	public static final int INTERACTION_TYPE_AVINTERACTION = 1024;
	public static final int UNDEFINED_SIZE = 0;
	public HashMap interactiveElements;
	public Vector hideInThisStep;
	public Vector showInThisStep;
}

/*
 * DECOMPILATION REPORT
 * 
 * Decompiled from:
 * /home/leo/wss/general/vaad_lab3_current/lib/Animal-current.jar Total time: 55
 * ms Jad reported messages/errors: The class file version is 50.0 (only 45.3,
 * 46.0 and 47.0 are supported) Exit status: 0 Caught exceptions:
 */