package de.lere.vaad.splaytree.resources.descriptions;

import java.io.InputStreamReader;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import de.lere.vaad.splaytree.resources.descriptions.SplayTreeResourceAccessor;

public class ResourceAccessorTest extends TestCase {

	@Test
	public void testGetResource() {
		SplayTreeResourceAccessor[] values = SplayTreeResourceAccessor.values();
		for (SplayTreeResourceAccessor resourceAccessor : values) {
			InputStreamReader resource = resourceAccessor.getResource();
			Assert.assertNotNull(
					"Enum must point to valid resource. Missing in:"
							+ resourceAccessor, resource);
		}
	}

	@Test
	public void testReturnActualtext() throws Exception {
		SplayTreeResourceAccessor zigStep = SplayTreeResourceAccessor.ZIG_STEP;
		List<String> text = zigStep.getText();
		for (String string : text) {
			assertNotNull(string);
		}
	}
}
