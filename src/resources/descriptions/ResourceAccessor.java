package resources.descriptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

public enum ResourceAccessor {
	
	DESCRIPTION("description"),
	INTRO("intro"),
	BEHAVIOUR("operationBehaviour"),
	ZIG_STEP("zigStep"),
	ZIGZAG_STEP("zigzagStep"),
	ZIGZIG_STEP("zigzigStep");
	
	ResourceAccessor(String name){
		this.name = name;
	}
	
	private String name;
	private List<String> text;
	
	public InputStream getResource(){
		return this.getClass().getResourceAsStream(name);
	}
	
	public void init() throws IOException{
		text = getTextFromResource(this);
	}
	
	public List<String> getText() {
		assert text != null : "Not properly initialised.";
		return text;
	}
	
	private List<String> getTextFromResource(ResourceAccessor resource) throws IOException {
		InputStream file = resource.getResource();
		List<String> readLines = IOUtils.readLines(file);
		return readLines;
	}
}
