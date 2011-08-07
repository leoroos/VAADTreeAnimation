package de.lere.vaad.treebuilder;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class NodeUIDTest {

	private NodeUID masterN;

	@Before
	public void setUp() {
		masterN = new NodeUID();
	}
	
	@Test
	public void testArbitraryNewUIDIsUnequal() throws Exception {
		NodeUID nodeUID = new NodeUID();
		assertThat(masterN, not(equalTo(nodeUID)));
	}
	
	@Test
	public void testSameUIDEqualToItself() throws Exception {
		assertThat(masterN, equalTo(masterN));
	}
	
}
