package de.lere.vaad.utils;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class GraphObject {

	public static class GraphEdge {
		public GraphEdge(int from, int to) {
			this.from = from;
			this.to = to;
		}
		int from; 
		int to;
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null)
				return false;
			if(obj.getClass() == this.getClass()){
				GraphEdge o = (GraphEdge) obj;
				return o.from == this.from && o.to == this.to;
			}
			return false;
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
		}

	}

	public static class GraphNode {
		String label;
		int x, y;
		public GraphNode(String label, int x, int y) {			
			this.label = label;
			this.x = x;
			this.y = y;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null)
				return false;
			if(obj.getClass() == this.getClass()){
				GraphNode o = (GraphNode) obj;
				return o.x == this.x && o.y == this.y && label.equals(o.label);
			}
			return false;
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
		}
	}

	public static GraphObject parse(String graphLine) {
		GraphObject go = new GraphObject();
		go.name = parseName(graphLine);
		go.size = parseInteger(graphLine, "size");
		go.depth = parseInteger(graphLine, "depth");
		go.color = parseColor(graphLine, "color");
		go.bgColor = parseColor(graphLine, "bgColor");
		go.edgeFontColor = parseColor(graphLine, "edgeFontColor");
		go.elemHighlightColor = parseColor(graphLine, "elemHighlightColor");
		go.nodeFontColor = parseColor(graphLine, "nodeFontColor");
		go.highlightColor = parseColor(graphLine, "highlightColor");
		go.nodes = parseNodes(graphLine);
		go.edges = parseEdges(graphLine);

		return go;
	}

	private static List<GraphEdge> parseEdges(String graphLine) {		
		List<GraphEdge> edgeLst = new ArrayList<GraphObject.GraphEdge>();
		Pattern compile = Pattern.compile(".*edges\\s*\\{(.*?)\\}.*");
		Matcher matcher = compile.matcher(graphLine);
		assertMatch(matcher, "edges");
		String edges = matcher.group(1);
		Pattern p = Pattern.compile("\\(\\s*(\\d*)\\s*,\\s*(\\d*)\\s*(,\\s*(\".*?\")\\s*)?\\)");
		Matcher eFinder = p.matcher(edges);
		while(eFinder.find()){
			int count = eFinder.groupCount();
			
			if(count >= 2){
				int from = Integer.parseInt(eFinder.group(1));
				int to = Integer.parseInt(eFinder.group(2));
				GraphEdge edge = new GraphEdge(from, to);
				edgeLst.add(edge);
			}
			else {
				throw new IllegalArgumentException("Expected at least two edge-coordinates");
			}
		}
					
		return edgeLst;
	}

	private static List<GraphNode> parseNodes(String graphLine) {
		ArrayList<GraphNode> list = new ArrayList<GraphNode>();
		Pattern compile = Pattern.compile(".*nodes\\s*\\{(.*?)\\}.*");
		Matcher matcher = compile.matcher(graphLine);
		assertMatch(matcher, "edges");
		String nodes = matcher.group(1);
		Pattern p = Pattern.compile("\"(.*?)\"\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
		Matcher nFinder = p.matcher(nodes);
		while(nFinder.find()){
			if(nFinder.groupCount() == 3){
				String label = nFinder.group(1);
				int x = asInt(nFinder.group(2));
				int y = asInt(nFinder.group(3));
				list.add(new GraphNode(label, x, y));
			}
			else {
				throw new IllegalArgumentException("Expected excactly three values for a node but was " + nFinder.group(0));				
			}
		}
		return list;
	}

	private static Color parseColor(String graphLine, String colProp) {
		Pattern compile = Pattern
				.compile(".*\\s"+colProp+"\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\).*");
		Matcher matcher = compile.matcher(graphLine);
		assertMatch(matcher, colProp);
		String r = matcher.group(1);
		String g = matcher.group(2);
		String b = matcher.group(3);
		return new Color(asInt(r), asInt(g), asInt(b));
	}

	private static int asInt(String r) {
		int ri = Integer.parseInt(r);
		return ri;
	}

	private static Integer parseInteger(String graphLine, String property) {
		Pattern pattern = Pattern.compile(".*" + property + "\\s*(\\d+).*");
		Matcher matcher = pattern.matcher(graphLine);
		assertMatch(matcher, property);
		String group = matcher.group(1);
		return asInt(group);
	}

	private static String parseName(String graphLine) {
		Pattern pattern = Pattern.compile("graph\\s*\"(.*?)\".*");
		Matcher matcher = pattern.matcher(graphLine);
		assertMatch(matcher, "name");
		return matcher.group(1);
	}

	private static void assertMatch(Matcher matcher, String matchName) {
		if (!matcher.matches())
			throw new IllegalArgumentException("No match for " + matchName
					+ " found");
	}

	public String name;
	public Integer size;
	public Integer depth;
	public Color color;
	public Color bgColor;

	public Color highlightColor;
	public Color elemHighlightColor;
	public Color nodeFontColor;
	public Color edgeFontColor;
	public List<GraphNode> nodes;
	public List<GraphEdge> edges;

}
