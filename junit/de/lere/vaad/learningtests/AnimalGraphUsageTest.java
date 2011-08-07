package de.lere.vaad.learningtests;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;


public class AnimalGraphUsageTest {
	
	private AnimalScript lang;
	
	@Before
	public void setUp() {
		lang = new AnimalScript("GraphUsage", "Leo Roos, Rene Hertling", 640, 480);
	}
	
	@Test
	public void checkForExpectedGraphProperties() throws Exception {
		GraphProperties props = new GraphProperties();
		String expectedString = "elementColor, weighted, fillColor, highlightColor, color, hidden, directed, nodeColor, edgeColor, name, elemHighlight, depth";
		String[] expected = expectedString.split(",\\s*");
		Set<String> actual = props.getAllPropertyNames();
		assertThat(actual, contains(expected));
	}
	
	@Test
	public void assertFillColorSetsBgColor(){
		GraphProperties testProperties = new GraphProperties();
		testProperties.set("fillColor", Color.white);
		createExampleGraph(lang,testProperties);
		String code = lang.getAnimationCode();
		Pattern compile = Pattern.compile(".*bgColor\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\).*", Pattern.DOTALL);
		Matcher matcher = compile.matcher(code);
		assertTrue(matcher.matches());
		String r = matcher.group(1);
		String g = matcher.group(2);
		String b = matcher.group(3);
		assertThat(new String[]{r,g,b}, arrayContaining("255","255","255"));
	}

	private Graph createExampleGraph(Language lang, GraphProperties props) {
		int [][] adj = { {0,1}, {1,0} };
		Node [] nodes = {new Coordinates(10, 10), new Coordinates(20, 20)};
		String [] labels = { "A", "B" };
		Graph usage = lang.newGraph("Usage", adj, nodes, labels, null, props);
		return usage;
	}
	
	@Test
	public void createGraphWithoutLabels() throws Exception {
		int [][] adj = { {0,1}, {1,0} };
		Node [] nodes = {new Coordinates(10, 10), new Coordinates(20, 20)};
		String [] labels = {"", null};
		Graph usage = lang.newGraph("Usage", adj, nodes, labels, null);
		assertThat(lang.getAnimationCode(), containsString("\"null\""));
		assertThat(lang.getAnimationCode(), containsString("\"\""));
		System.out.println(lang.getAnimationCode());
	}
}
