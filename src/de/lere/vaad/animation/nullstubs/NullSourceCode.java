package de.lere.vaad.animation.nullstubs;

import java.awt.Color;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Timing;

public class NullSourceCode extends SourceCode {

	public NullSourceCode() {
		super(new NullSourceCodeGenerator(), new Coordinates(0, 0), null, new Hidden(), null);
	}

	@Override
	public void setName(String newName) {

	}

	@Override
	public void registerLabel(String label, int lineNo) {

	}

	@Override
	public void registerLabel(String label, int lineNo, int rowNo) {

	}

	@Override
	public SourceCodeProperties getProperties() {

		return null;
	}

	@Override
	public Node getUpperLeft() {

		return null;
	}

	@Override
	public int addCodeLine(String code, String label, int indentation,
			Timing delay) throws NullPointerException {

		return 0;
	}

	@Override
	public int addCodeElement(String code, String label,
			boolean noSpaceSeparator, int indentation, Timing delay)
			throws NullPointerException {

		return 0;
	}

	@Override
	public int addCodeElement(String code, String label, int indentation,
			Timing delay) throws NullPointerException {

		return 0;
	}

	@Override
	public void highlight(String label) throws LineNotExistsException {

	}

	@Override
	public void highlight(String label, boolean context)
			throws LineNotExistsException {

	}

	@Override
	public void highlight(String label, boolean context, Timing delay,
			Timing duration) throws LineNotExistsException {

	}

	@Override
	public void highlight(int lineNo) throws LineNotExistsException {

	}

	@Override
	public void highlight(int lineNo, int colNo, boolean context)
			throws LineNotExistsException {

	}

	@Override
	public void highlight(int lineNo, int colNo, boolean context, Timing delay,
			Timing duration) throws LineNotExistsException {

	}

	@Override
	public void unhighlight(String label) throws LineNotExistsException {

	}

	@Override
	public void unhighlight(String label, boolean context)
			throws LineNotExistsException {

	}

	@Override
	public void unhighlight(String label, boolean context, Timing delay,
			Timing duration) throws LineNotExistsException {

	}

	@Override
	public void unhighlight(int lineNo) throws LineNotExistsException {

	}

	@Override
	public void unhighlight(int lineNo, int colNo, boolean context)
			throws LineNotExistsException {

	}

	@Override
	public void unhighlight(int lineNo, int colNo, boolean context,
			Timing delay, Timing duration) throws LineNotExistsException {

	}

	@Override
	public void toggleHighlight(String label) {

	}

	@Override
	public void toggleHighlight(String oldLabel, String newLabel)
			throws LineNotExistsException {

	}

	@Override
	public void toggleHighlight(String oldLabel, boolean switchToContextMode,
			String newLabel) throws LineNotExistsException {

	}

	@Override
	public void toggleHighlight(String oldLabel, boolean switchToContextMode,
			String newLabel, Timing delay, Timing duration)
			throws LineNotExistsException {

	}

	@Override
	public void toggleHighlight(int oldLine, int newLine)
			throws LineNotExistsException {

	}

	@Override
	public void toggleHighlight(int oldLine, int oldColumn,
			boolean switchToContextMode, int newLine, int newColumn)
			throws LineNotExistsException {
	}

	@Override
	public void toggleHighlight(int oldLine, int oldColumn,
			boolean switchToContextMode, int newLine, int newColumn,
			Timing delay, Timing duration) throws LineNotExistsException {
	}

	@Override
	public void changeColor(String colorType, Color newColor, Timing t, Timing d) {

	}

	@Override
	public void exchange(Primitive q) {

	}

	@Override
	public DisplayOptions getDisplayOptions() {

		return null;
	}

	@Override
	public String getName() {

		return null;
	}

	@Override
	public void hide() {

		super.hide();
	}

	@Override
	public void hide(Timing t) {

		super.hide(t);
	}

	@Override
	public void moveBy(String moveType, int dx, int dy, Timing delay,
			Timing duration) {

		super.moveBy(moveType, dx, dy, delay, duration);
	}

	@Override
	public void moveTo(String direction, String moveType, Node target,
			Timing delay, Timing duration) throws IllegalDirectionException {

		super.moveTo(direction, moveType, target, delay, duration);
	}

	@Override
	public void moveVia(String direction, String moveType, Primitive via,
			Timing delay, Timing duration) throws IllegalDirectionException {

		super.moveVia(direction, moveType, via, delay, duration);
	}

	@Override
	public void rotate(Primitive around, int degrees, Timing t, Timing d) {

		super.rotate(around, degrees, t, d);
	}

	@Override
	public void rotate(Node center, int degrees, Timing t, Timing d) {

		super.rotate(center, degrees, t, d);
	}

	@Override
	public void show() {

		super.show();
	}

	@Override
	public void show(Timing t) {

	}

}
