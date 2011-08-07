package de.lere.vaad.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

import java.awt.Color;
import java.util.List;

import org.junit.Test;

import de.lere.vaad.utils.GraphObject.GraphEdge;
import de.lere.vaad.utils.GraphObject.GraphNode;


public class GraphObjectTest {	
	
	@Test
	public void testParsesName(){
		String toParse= "graph \"TBD\" size 2  color (1, 2, 3) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {} edges {}  depth 1";		
		GraphObject parse = GraphObject.parse(toParse);		
		assertThat(parse.name, equalTo("TBD"));
	}
	
	public void testParsesSize(){
		String toParse= "graph \"TBD\" size 2  color (1, 2, 3) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {} edges {}  depth 1";		
		GraphObject parse = GraphObject.parse(toParse);		
		assertThat(parse.size, equalTo(2));

	}
	
	@Test
	public void parsesColor() throws Exception {
		String toParse= "graph \"TBD\" size 2  color (1, 2, 3) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {} edges {}  depth 1";		
		GraphObject parse = GraphObject.parse(toParse);		
		assertThat(parse.color, equalTo(new Color(1,2,3)));

	}
	
	@Test
	public void parsesBgColor() throws Exception {
		String toParse= "graph \"TBD\" size 2  color (1, 2, 3) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {} edges {}  depth 1";		
		GraphObject parse = GraphObject.parse(toParse);		
		assertThat(parse.bgColor, equalTo(Color.WHITE));

	}
	
	@Test
	public void parsesHighlightColor() throws Exception {
		String toParse= "graph \"TBD\" size 2  color (1, 2, 3) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {} edges {}  depth 1";		
		GraphObject parse = GraphObject.parse(toParse);		
		assertThat(parse.highlightColor, equalTo(Color.BLACK));

	}
	
	@Test
	public void parsesElementHighlightColor() throws Exception {
		String toParse= "graph \"TBD\" size 2  color (1, 2, 3) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {} edges {}  depth 1";		
		GraphObject parse = GraphObject.parse(toParse);				
		assertThat(parse.elemHighlightColor, equalTo(Color.BLACK));
	}
	
	@Test
	public void parsesNodeFontColor() throws Exception {
		String toParse= "graph \"TBD\" size 2  color (1, 2, 3) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {} edges {}  depth 1";		
		GraphObject parse = GraphObject.parse(toParse);		
		assertThat(parse.nodeFontColor, equalTo(Color.BLACK));
	}
	
	@Test
	public void parsesEdgeFontColor() throws Exception {
		String toParse= "graph \"TBD\" size 2  color (1, 2, 3) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {} edges {}  depth 1";		
		GraphObject parse = GraphObject.parse(toParse);		
		assertThat(parse.edgeFontColor, equalTo(Color.BLACK));
		
	}
	
	@Test
	public void parsesDepth() throws Exception {
		String toParse= "graph \"TBD\" size 2  color (1, 2, 3) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {} edges {}  depth 1";		
		GraphObject parse = GraphObject.parse(toParse);		
		assertThat(parse.depth, equalTo(1));	
	}
	
	@Test
	public void testParseNodes() throws Exception {
		String toParse= "graph \"Usage\" size 2  color (0, 0, 0) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {\"A\" (10, 10), \"B\" (20, 20) } edges {(0, 1, \"1\") (1, 0, \"1\") }  depth 1";
		
		GraphObject parse = GraphObject.parse(toParse);
		List<GraphNode> nodes = parse.nodes;
		
		assertThat(nodes, hasItems(new GraphNode("A", 10, 10), new GraphNode("B", 20, 20)));
	}
	
	@Test
	public void testParseEdges() throws Exception {
		String toParse= "graph \"Usage\" size 2  color (0, 0, 0) bgColor (255, 255, 255) highlightColor (0, 0, 0) elemHighlightColor (0, 0, 0) nodeFontColor (0, 0, 0) edgeFontColor (0, 0, 0)nodes {\"A\" (10, 10), \"B\" (20, 20) } edges {(0, 1, \"1\") (1, 0, \"1\") }  depth 1";
		
		GraphObject parse = GraphObject.parse(toParse);
		List<GraphEdge> edges = parse.edges;
		
		assertThat(edges, hasItems(new GraphEdge(0, 1), new GraphEdge(1, 0)));	
	}
	
}
