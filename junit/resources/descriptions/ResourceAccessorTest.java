package resources.descriptions;

import java.io.InputStreamReader;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class ResourceAccessorTest extends TestCase {

	@Test
	public void testGetResource() {
		ResourceAccessor[] values = ResourceAccessor.values();
		for (ResourceAccessor resourceAccessor : values) {
			InputStreamReader resource = resourceAccessor.getResource();
			Assert.assertNotNull(
					"Enum must point to valid resource. Missing in:"
							+ resourceAccessor, resource);
		}
	}

	@Test
	public void testReturnActualtext() throws Exception {
		ResourceAccessor zigStep = ResourceAccessor.ZIG_STEP;
		List<String> text = zigStep.getText();
		for (String string : text) {
			assertNotNull(string);
		}
	}
}
