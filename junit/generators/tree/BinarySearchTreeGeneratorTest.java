package generators.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import generators.framework.properties.tree.PropertiesTreeModel;

import org.junit.Test;


public class BinarySearchTreeGeneratorTest {

	
	@Test
	public void shouldNotCatchFireSmokeTest() throws Exception {
		BinarySearchTreeGenerator bstg = new BinarySearchTreeGenerator();
		bstg.init();
		PropertiesTreeModel ptm = createPtm();
		String generate = bstg.generate(ptm.getPropertiesContainer(), ptm.getPrimitivesContainer());
		assertThat(generate, notNullValue());
	}
	
	private PropertiesTreeModel createPtm(){
		BinarySearchTreeGenerator generator = new BinarySearchTreeGenerator();
		generator.init();
		PropertiesTreeModel ptm = new PropertiesTreeModel();
		String cleanString = PropertiesTreeModel.cleanString("generators/tree/"+BinarySearchTreeGenerator.class.getSimpleName()+".xml", "xml");
		ptm.loadFromXMLFile(cleanString, true);
		return ptm;
	}
	
}
