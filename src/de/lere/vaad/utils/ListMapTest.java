package de.lere.vaad.utils;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.lere.vaad.utils.ListMap.IdentityTransformer;


public class ListMapTest {

	@Mock LMTransformer<String, Integer> sToI;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testTransformEmptyList() throws Exception {
		List<Integer> result = ListMap.map(new ArrayList<String>(), sToI); 
		assertThat(result, hasSize(0));
	}
	
	@Test
	public void testTransformertransforms() throws Exception {
		
		LMTransformer<Integer, String> t = new LMTransformer<Integer, String>(){

			@Override
			public String transform(Integer input) {
				return input.toString();
			}
			
		};
		
		String transform = t.transform(2);
		assertThat(transform, equalTo("2"));
		
	}
	
	@Test
	public void testTransformSomeStringsToInteger() throws Exception {
		ArrayList<String> arrayList = new ArrayList<String>();
		Collections.addAll(arrayList, "1","2","3","4");
		List<Integer> result = ListMap.map(arrayList, new LMTransformer<String,Integer>(){
			@Override
			public Integer transform(String input){
				return Integer.parseInt(input);
			}
		});
		assertThat(result, hasItems(1,2,3,4));
		
	}
	
}
