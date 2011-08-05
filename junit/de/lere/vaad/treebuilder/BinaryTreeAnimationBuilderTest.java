package de.lere.vaad.treebuilder;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.UnhandledException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import algoanim.animalscript.AnimalScript;
import algoanim.util.Coordinates;
import de.lere.vaad.CorrectedOffset;
import de.lere.vaad.treebuilder.BinaryTreeAnimationBuilder.BinaryTreeLayout;


public class BinaryTreeAnimationBuilderTest {
	
	
	private static final int FIRST_LEVEL_WIDTH = 240;
	private static final int VERTICAL_SPACING = 60;
	
	@Spy AnimalScript language = new AnimalScript("dummynator", "terminator", 640, 480);
	
	private BinaryTreeAnimationBuilder testee;
	
	@Before
	public void setUp(){				
		MockitoAnnotations.initMocks(this);
		
		testee = new BinaryTreeAnimationBuilder(language);
		BinaryTreeModel<String> model = createInitialModel();
		BinaryTreeLayout layout = createTestLayout();
		testee.setModel(model);
		testee.setLayout(layout);
	}
	

	private BinaryTreeLayout createTestLayout() {
		algoanim.util.Node pointOfOrigin = CorrectedOffset.getOffsetForCoordinate(new Coordinates(300,275)); 
		BinaryTreeLayout layout = new BinaryTreeLayout(pointOfOrigin, FIRST_LEVEL_WIDTH, VERTICAL_SPACING);
		return layout;
	}


	private BinaryTreeModel<String> createInitialModel() {
		WideBuilder builder = new WideBuilder();
		BinaryTreeModel<String> model = builder.buildTree("0", "1", "2", "3", "4", "5", "6");
		return model;
	}

	@Test
	public void displayCurrentTree(){
		String expected = getStringFromResource("BinaryTreeAnimationBuilderTestExample6Nodes");
		
		testee.buildCurrentGraph();
		
		String animationCode = language.getAnimationCode();
		
		assertThat(animationCode, containsString(expected));
	}
	
	
	private String getStringFromResource(String name)
	{
		InputStream resourceAsStream = getClass().getResourceAsStream("resources/" + name);
		try {
			return IOUtils.toString(resourceAsStream);
		} catch (IOException e) {
			throw new UnhandledException(e);
		}
	}
	
}
