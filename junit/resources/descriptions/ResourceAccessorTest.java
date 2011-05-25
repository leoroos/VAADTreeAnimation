package resources.descriptions;

import java.io.InputStream;

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ResourceAccessorTest extends TestCase {

	@Test
	public void testGetResource() {
		ResourceAccessor[] values = ResourceAccessor.values();
		for (ResourceAccessor resourceAccessor : values) {
			InputStream resource = resourceAccessor.getResource();
			Assert.assertNotNull("Enum must point to valid resource. Missing in:" + resourceAccessor, resource);		
		}
	}
}
