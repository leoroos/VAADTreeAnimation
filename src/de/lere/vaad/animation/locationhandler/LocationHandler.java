package de.lere.vaad.animation.locationhandler;

import java.util.LinkedList;
import java.util.List;

import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.graphics.meta.Location;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.animation.SourceCodeWriter;
import de.lere.vaad.utils.StrUtils;
import edu.umd.cs.findbugs.annotations.Nullable;

public class LocationHandler {

	private static int runninggroupidentifier = 0;

	private final BinaryTreeProperties btProps;
	private Language language;

	public LocationHandler(Language language, BinaryTreeProperties props) {
		this.language = language;
		this.btProps = props;
	}

	/**
	 * Puts the a new action on a location
	 * 
	 * @param action
	 * @param location
	 */
	public <T extends Node> void nextStateOnLocation(Action<T> action,
			LocationDirector<T> location) {
		location.nextState(action);
	}

	public <T extends Node> void nextStateOnLocation(final List<String> text,
			LocationDirector<T> location) {
		nextStateOnLocation(text, location, null);
	}

	/**
	 * creates a default action for the given List of strings, i.e. a group of
	 * Texts that will be placed on the location parameter and hidden if someone
	 * else uses the location.
	 */
	public <T extends Node> void nextStateOnLocation(final List<String> text,
			LocationDirector<T> location, final TextProperties tps) {
		ActionAdapter<T> aAction = new ActionAdapter<T>() {

			@Override
			public void activateOn(LocationProvider<T> location) {
				Group aTextGroup = getTextGroup(text, location.getLocation(),
						tps);
				hideOnDeactivate(aTextGroup);
			}
		};
		nextStateOnLocation(aAction, location);
	}

	/**
	 * @see #nextStateOnLocation(List, Location, TextProperties)
	 */
	public <T extends Node> void nextStateOnLocation(final String newText,
			LocationDirector<T> location) {
		nextStateOnLocation(StrUtils.toLines(newText), location, null);
	}

	/**
	 * @see #nextStateOnLocation(List, Location, TextProperties)
	 */
	public <T extends Node> void nextStateOnLocation(final String newText,
			LocationDirector<T> location, TextProperties tps) {
		nextStateOnLocation(StrUtils.toLines(newText), location, tps);
	}

	public @Nullable
	Group getTextGroup(List<String> readLines, Node anchor, TextProperties tps) {
		if (readLines.isEmpty()) {
			return null;
		}
		if (tps == null) {
			tps = btProps.getTextProperties();
		}
		Node nextTextPos = anchor;
		int groupId = runninggroupidentifier++;
		LinkedList<Primitive> texts = new LinkedList<Primitive>();
		for (int i = 0; i < readLines.size(); i++) {
			Text text = language.newText(nextTextPos, readLines.get(i), groupId
					+ "id" + i, null, tps);
			nextTextPos = new Offset(0, this.btProps.verticalTextGap, text,
					"SW");
			texts.add(text);
		}
		Group introGroup = language.newGroup(texts, "group" + groupId);
		return introGroup;
	}

	public @Nullable
	Group createTextGroup(String text, Node location) {
		List<String> stringToLinesAtDelimiter = StrUtils.toLines(text);
		return getTextGroup(stringToLinesAtDelimiter, location, null);
	}

	public @Nullable
	Group createTextGroup(String text, Node location, TextProperties tps) {
		List<String> stringToLinesAtDelimiter = StrUtils.toLines(text);
		return getTextGroup(stringToLinesAtDelimiter, location, tps);
	}

	/**
	 * Fills the {@link SourceCode} object {@code sc} with the lines provided by
	 * {@code codeGroupText}.
	 * 
	 * FIXME Should already have been moved to {@link SourceCodeWriter}
	 */
	@Deprecated
	public SourceCode getFilledSourceCode(String codeGroupText, SourceCode sc) {
		List<String> split = StrUtils.toLines(codeGroupText);
		for (int i = 0; i < split.size(); i++) {
			sc.addCodeLine(split.get(i), "", 0, null);
		}
		return sc;
	}

}
