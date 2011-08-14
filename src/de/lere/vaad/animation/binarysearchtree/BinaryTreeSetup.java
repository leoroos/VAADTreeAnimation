package de.lere.vaad.animation.binarysearchtree;

import java.lang.reflect.Field;

import algoanim.primitives.generators.Language;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.EndOfTheWorldException;
import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.StepWriter;
import de.lere.vaad.animation.Timings;
import de.lere.vaad.treebuilder.BinaryTreeLayout;

public class BinaryTreeSetup<T extends Comparable<T>> {

	private GraphWriter<T> graphWriter;
	private Language language;
	private StepWriter stepWriter;
	private BinaryTreeProperties binaryTreeProperties;

	protected void bouncer(Object o) {
		if (o == null)
			throw new IllegalArgumentException("I don't like nulls");
	}
	
	public void setWriter(GraphWriter<T> writer) {
		bouncer(writer);
		this.graphWriter = writer;
	}

	public void setLang(Language lang) {
		bouncer(lang);
		this.language = lang;
	}

	public void setStepWriter(StepWriter stepWriter) {
		bouncer(stepWriter);
		this.stepWriter = stepWriter;
	}

	/**
	 * iterates over all fields and checks for non null setting
	 */
	public void validate() {
		Field[] fields = getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object object = field.get(this);
				if (object == null)
					throw new IllegalArgumentException(field.getName()
							+ " was null although null is not valid");
			} catch (IllegalArgumentException e) {
				throw e;
			} catch (IllegalAccessException e) {
				throw new EndOfTheWorldException(
						"I only work with maximum privileges.", e);
			}
		}
	}

	public GraphWriter<T> getWriter() {
		bouncer(graphWriter);
		return graphWriter;
	}

	public Language getLanguage() {
		bouncer(language);
		return language;
	}

	public StepWriter getStepWriter() {
		bouncer(stepWriter);
		return stepWriter;
	}

	public void setBinaryTreeProperties(BinaryTreeProperties animationProperties) {
		bouncer(animationProperties);
		this.binaryTreeProperties = animationProperties;
	}

	public BinaryTreeProperties getBinaryTreeProperties() {
		bouncer(binaryTreeProperties);
		return binaryTreeProperties;
	}
	
	private BinaryTreeLayout layout;
	
	public void setLayout(BinaryTreeLayout layout) {
		bouncer(layout);
		this.layout = layout;
	}
	
	public BinaryTreeLayout getLayout() {
		bouncer(layout);
		return layout;
	}

}