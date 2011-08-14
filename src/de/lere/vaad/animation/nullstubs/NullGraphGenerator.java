package de.lere.vaad.animation.nullstubs;

import java.awt.Color;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.GraphGenerator;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;
import algoanim.util.Timing;

public class NullGraphGenerator implements GraphGenerator {

	@Override
	public void changeColor(Primitive primitive, String s, Color color,
			Timing timing, Timing timing1) {

	}

	@Override
	public void exchange(Primitive primitive, Primitive primitive1) {
	}

	@Override
	public Language getLanguage() {
		return null;
	}

	@Override
	public void hide(Primitive primitive, Timing timing) {

	}

	@Override
	public void moveBy(Primitive primitive, String s, int i, int j,
			Timing timing, Timing timing1) {

	}

	@Override
	public void moveTo(Primitive primitive, String s, String s1, Node node,
			Timing timing, Timing timing1) throws IllegalDirectionException {

	}

	@Override
	public void moveVia(Primitive primitive, String s, String s1,
			Primitive primitive1, Timing timing, Timing timing1)
			throws IllegalDirectionException {

	}

	@Override
	public void rotate(Primitive primitive, Primitive primitive1, int i,
			Timing timing, Timing timing1) {

	}

	@Override
	public void rotate(Primitive primitive, Node node, int i, Timing timing,
			Timing timing1) {
	}

	@Override
	public void show(Primitive primitive, Timing timing) {

	}

	@Override
	public void create(Graph graph) {
	}

	@Override
	public void hideEdge(Graph graph, int i, int j, Timing timing,
			Timing timing1) {

	}

	@Override
	public void hideEdgeWeight(Graph graph, int i, int j, Timing timing,
			Timing timing1) {

	}

	@Override
	public void hideNode(Graph graph, int i, Timing timing, Timing timing1) {
	}

	@Override
	public void hideNodes(Graph graph, int[] ai, Timing timing, Timing timing1) {

	}

	@Override
	public void highlightEdge(Graph graph, int i, int j, Timing timing,
			Timing timing1) {

	}

	@Override
	public void highlightNode(Graph graph, int i, Timing timing, Timing timing1) {

	}

	@Override
	public void setEdgeWeight(Graph graph, int i, int j, String s,
			Timing timing, Timing timing1) {

	}

	@Override
	public void showEdge(Graph graph, int i, int j, Timing timing,
			Timing timing1) {

	}

	@Override
	public void showEdgeWeight(Graph graph, int i, int j, Timing timing,
			Timing timing1) {

	}

	@Override
	public void showNode(Graph graph, int i, Timing timing, Timing timing1) {

	}

	@Override
	public void showNodes(Graph graph, int[] ai, Timing timing, Timing timing1) {

	}

	@Override
	public void translateNode(Graph graph, int i, Node node, Timing timing,
			Timing timing1) {

	}

	@Override
	public void translateNodes(Graph graph, int[] ai, Node node, Timing timing,
			Timing timing1) {

	}

	@Override
	public void translateWithFixedNodes(Graph graph, int[] ai, Node node,
			Timing timing, Timing timing1) {

	}

	@Override
	public void unhighlightEdge(Graph graph, int i, int j, Timing timing,
			Timing timing1) {

	}

	@Override
	public void unhighlightNode(Graph graph, int i, Timing timing,
			Timing timing1) {

	}

}
