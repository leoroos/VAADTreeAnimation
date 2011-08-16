package de.lere.vaad.animation;

import java.util.List;

import javax.annotation.Nonnull;

import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.TicksTiming;
import de.lere.vaad.animation.locationhandler.LocationProvider;
import de.lere.vaad.animation.nullstubs.NullSourceCode;
import de.lere.vaad.utils.StrUtils;

/**
 * <b>Attention:</b> This source code writer is operates on <b>one based
 * code</b> lines. This has been shown to be easier to handle since most text
 * processor display text one based.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 */
public class SourceCodeWriter {

	private static int runningSourceCodeIdentifier = 0;
	private final LocationProvider<?> location;
	private final SourceCodeProperties props;
	private final Language language;
	private SourceCode lastCreatedSourceCode = new NullSourceCode();
	private final String sourceCodeName;
	private Timings ts;

	public @Nonnull
	SourceCodeWriter(Language language, SourceCodeProperties props, 
			LocationProvider<?> location, Timings timings, String sourceCodeName) {
		this.language = language;
		this.props = props;
		this.location = location;
		this.sourceCodeName = sourceCodeName;
		this.ts = timings;
	}
	
	
	private int[] previouslyHighlighted = {};
	private boolean unhighlight = false
	;
	/**
	 * Unhighlights all previously highlighted lines when a new un/highlight operation is performed
	 * 
	 * @param unhighlight whether to automatically unhighlight
	 */
	public void setAutomaticLineUnhighlighting(boolean unhighlight){
		this.unhighlight = unhighlight;		
	}
	
	public boolean isAutomaticUnhighlight() {
		return unhighlight;
	}

	public @Nonnull
	SourceCodeWriter(Language language, SourceCodeProperties props,
			LocationProvider<?> location, Timings timings) {
		this(language, props, location, timings, "DefaultSourceCodeName");
	}

	public void newSourceCode(String sourceCodeText) {
		this.newSourceCode(sourceCodeText, true);
	}

	public void newSourceCode(String sourceCodeText, boolean visible) {
		SourceCode newSource = language.newSourceCode(location.getLocation(),
				sourceCodeName + runningSourceCodeIdentifier++, null,
				this.props);
		this.lastCreatedSourceCode = newSource;
		/*
		 * You might be wondering why I hide the source code right after his
		 * creation twice. Well first of all I do it on purpose obviously. And
		 * secondly the reason for that is that if I do now twice I don't have
		 * to put the hide() statement for the source code into a separate step
		 * late when I want him to vanish again.
		 * "But why should that be necessary" you might ask with good reason.
		 * And the answer for that question is yet to be found ;).
		 */
		newSource.hide();
		newSource.hide();

		fillSourceCode(sourceCodeText, newSource);
		if (visible) {
			newSource.show();
		}
	}

	public void hide() {
		this.hide(null);
	}

	public void hide(TicksTiming delay) {
		this.lastCreatedSourceCode.hide(delay);
	}

	public void show() {
		this.show(null);
	}

	public void show(TicksTiming delay) {
		this.lastCreatedSourceCode.show(delay);
	}

	public SourceCode fillSourceCode(String codeGroupText, SourceCode sc) {
		List<String> split = StrUtils.toLines(codeGroupText);
		for (int i = 0; i < split.size(); i++) {
			sc.addCodeLine(split.get(i), "", 0, null);
		}
		return sc;
	}

	public void unhighlight(int i) {
		this.unhighlight(i, ts.NOW, ts.NOW);
	}

	public void unhighlight(int line, TicksTiming when, TicksTiming howlong) {
		internalUnhighlight(line, when, howlong);
	}

	private void internalUnhighlight(int line, TicksTiming when,
			TicksTiming howlong) {
		this.lastCreatedSourceCode.unhighlight(toAnimalLine(line), -1, false, when,
				howlong);
	}

	private int toAnimalLine(int line) {
		int animalLine = line - 1;
		return animalLine;
	}

	private void validateLine(int line) {
		if (line < 1)
			throw new IllegalArgumentException(
					"Operating on one based code lines but line to work with was "
							+ line);
	}

	public void highlight(int line) {
		this.highlight(line, ts.NOW, ts.NOW);
	}

	public void highlight(int line, TicksTiming when, TicksTiming howlong) {
		highlightLines(ts.NOW, ts.NOW, new int [] {line});
	}
	
	public void highlightLines(TicksTiming when, TicksTiming howlong, int... lines) {
		if (isAutomaticUnhighlight()) {
			for (int i : previouslyHighlighted) {
				internalUnhighlight(i, ts.NOW, when);
			}
		}
		this.previouslyHighlighted = lines;
		for (int j : lines) {
			validateLine(j);
			internalHighlight(j, when, howlong);
		}
	}

	private void internalHighlight(int line, TicksTiming when,
			TicksTiming howlong) {
		this.lastCreatedSourceCode.highlight(toAnimalLine(line), -1, false,
				when, howlong);
	}

	public void highlightLines(int[] lines) {
		this.highlightLines(ts.NOW, ts.NOW, lines);
	}
	
	public void hightlightMultipleLines(int ... lines){
		this.highlightLines(lines);
	}

}
