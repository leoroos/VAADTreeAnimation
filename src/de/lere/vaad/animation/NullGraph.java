package de.lere.vaad.animation;

import algoanim.primitives.Graph;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class NullGraph extends Graph {

	public static final int[][] NO_CONNECTIONS_ADJ_MATRIX = { { 0 } };
	public static final Node[] FAKE_NODE = { new Coordinates(-100, -100) };
	public static final String [] FAKE_NAME = { "FAKE" };

	public NullGraph() {
		super(new NullGraphGen(), "NullName", NO_CONNECTIONS_ADJ_MATRIX, FAKE_NODE, FAKE_NAME, null,new GraphProperties());
	}
}
