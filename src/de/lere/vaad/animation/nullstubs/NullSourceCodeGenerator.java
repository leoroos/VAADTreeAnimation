package de.lere.vaad.animation.nullstubs;

import java.awt.Color;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.SourceCodeGenerator;
import algoanim.util.Node;
import algoanim.util.Timing;

public class NullSourceCodeGenerator implements SourceCodeGenerator {

	@Override
	public void changeColor(Primitive primitive, String s, Color color,
			Timing timing, Timing timing1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exchange(Primitive primitive, Primitive primitive1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Language getLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void hide(Primitive primitive, Timing timing) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveBy(Primitive primitive, String s, int i, int j,
			Timing timing, Timing timing1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveTo(Primitive primitive, String s, String s1, Node node,
			Timing timing, Timing timing1) throws IllegalDirectionException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveVia(Primitive primitive, String s, String s1,
			Primitive primitive1, Timing timing, Timing timing1)
			throws IllegalDirectionException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotate(Primitive primitive, Primitive primitive1, int i,
			Timing timing, Timing timing1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotate(Primitive primitive, Node node, int i, Timing timing,
			Timing timing1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show(Primitive primitive, Timing timing) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCodeElement(SourceCode sourcecode, String s, String s1,
			int i, int j, Timing timing) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCodeElement(SourceCode sourcecode, String s, String s1,
			int i, boolean flag, int j, Timing timing) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCodeLine(SourceCode sourcecode, String s, String s1, int i,
			Timing timing) {
		// TODO Auto-generated method stub

	}

	@Override
	public void create(SourceCode sourcecode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide(SourceCode sourcecode, Timing timing) {
		// TODO Auto-generated method stub

	}

	@Override
	public void highlight(SourceCode sourcecode, int i, int j, boolean flag,
			Timing timing, Timing timing1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unhighlight(SourceCode sourcecode, int i, int j, boolean flag,
			Timing timing, Timing timing1) {
		// TODO Auto-generated method stub

	}

}
