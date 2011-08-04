package de.lere.vaad.treebuilder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import de.lere.vaad.EndOfTheWorldException;
import extras.lifecycle.checkpoint.annotation.Checkpointing;

public class NodeTest {

	@Test
	public void canAccessPerUID() throws Exception {
		Node node = createDummyNode();
		NodeUID uid = node.getUid();
		assertNotNull("UID must not be null", uid);
	}

	private Node createDummyNode() {
		return new Node("dummy");
	}

	@Test
	public void uidIsUnique() throws Exception {
		Node node1 = createDummyNode();
		Node node2 = createDummyNode();
		assertThat("UID must be different", node1, not(equalTo(node2)));
	}

	@Test
	public void buildSimpleTree() throws Exception {
		Node root = new Node("x");
		Node lc = new Node("lc");
		Node rc = new Node("rc");
		Node lrc = new Node("lrc");
		// exec
		root.setLeft(lc);
		root.setRight(rc);
		lc.setRight(lrc);
		// validate
		assertThat(rc, equalTo(root.getRight()));
		assertThat(lc, equalTo(root.getLeft()));
		assertThat(lrc, equalTo(root.getLeft().getRight()));
	}

	@Test
	public void testNodetoString() throws Exception {
		Node inorderbuild = InOrderBuilder.inorderbuild("x", "lc", "rc", null,
				"lrc");
		System.out.println(inorderbuild.toString());
	}

}
