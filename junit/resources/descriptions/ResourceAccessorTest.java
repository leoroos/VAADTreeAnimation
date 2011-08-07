package resources.descriptions;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ResourceAccessorTest extends TestCase {

	@Test
	public void testGetResource() {
		ResourceAccessor[] values = ResourceAccessor.values();
		for (ResourceAccessor resourceAccessor : values) {
			InputStreamReader resource = resourceAccessor.getResource();
			Assert.assertNotNull("Enum must point to valid resource. Missing in:" + resourceAccessor, resource);		
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
