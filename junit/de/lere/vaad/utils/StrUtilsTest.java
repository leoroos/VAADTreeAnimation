package de.lere.vaad.utils;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;


public class StrUtilsTest {

	@Test
	public void testEmptyStringReturnsEmptyList() throws Exception {
		List<String> lines = StrUtils.toLines("");
		assertThat(lines, hasSize(0));
	}
	


	@Test
	public void testOneLineString() throws Exception {
		List<String> lines = StrUtils.toLines("asdf");
		assertThat(lines, hasSize(1));
		assertThat(lines, hasItem("asdf"));
	}
	
	@Test
	public void testMultipleLineString() throws Exception {
		List<String> lines = StrUtils.toLines("asdf\nasd34\nölasd");
		assertThat(lines, contains("asdf","asd34","ölasd"));
	}
	
	
	@Test
	public void testNoLastLine() throws Exception {
		String lastLine = StrUtils.getLastLine("");
		assertThat(lastLine, equalTo(null));
	}
	
	@Test
	public void testLastLine() throws Exception {
		String lastLine = StrUtils.getLastLine("asdf\nasd34\nölasd");
		assertThat(lastLine, equalTo("ölasd"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLastLineViaNth() throws Exception {
		StrUtils.getNthLastLine("asdf\nasd34\nölasd", 0);
	}
	
	@Test
	public void testNth2LastLine() throws Exception {
		String lastLine = StrUtils.getNthLastLine("asdf\nasd34\nölasd", 1);
		assertThat(lastLine, equalTo("ölasd"));
	}
	
	@Test
	public void testNthExceedsLastLine() throws Exception {
		String lastLine = StrUtils.getNthLastLine("asdf\nasd34\nölasd", 4);
		assertThat(lastLine, equalTo(null));
	}
	
	@Test
	public void testNth3LastLine() throws Exception {
		String lastLine = StrUtils.getNthLastLine("asdf\nasd34\nölasd", 3);
		assertThat(lastLine, equalTo("asdf"));
	}
	
}
