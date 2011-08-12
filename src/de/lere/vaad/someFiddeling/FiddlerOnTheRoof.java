package de.lere.vaad.someFiddeling;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.splaytree.SplayTreeModel;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.ExtractedBinaryTreeAnimations;
import de.lere.vaad.treebuilder.GraphWriterImpl;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.treebuilder.Timings;
import de.lere.vaad.treebuilder.TreeEventListenerAggregator;
import de.lere.vaad.utils.NodeHelper;
import de.lere.vaad.utils.StrUtils;
import edu.umd.cs.findbugs.annotations.Nullable;

public class FiddlerOnTheRoof {

	private static final Coordinates GRAPHROOT_COORDINATES = new Coordinates(
			800, 300);

	private final Language language;

	private SourceCode sourceCode;
	private int runninggroupidentifier = 0;
	private final BinaryTreeProperties animationProperties;
	private final BinaryTreeLayout layout;

	private FiddlerOnTheRoof(Language l, BinaryTreeProperties tp) {
		this.language = l;
		this.animationProperties = tp;
		l.setStepMode(true);
		this.layout = new BinaryTreeLayout(
				NodeHelper.convertCoordinatesToAWTPoint(GRAPHROOT_COORDINATES),
				400, 60);
	}

	/**
	 * Container Object for properties of this Animation
	 */
	private BinaryTreeProperties splayProps = new BinaryTreeProperties();

	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height

		BinaryTreeProperties tp = new BinaryTreeProperties();

		tp.authors = "Rene Hertling, Leo Roos";

		tp.title = "Splaytree Animation";

		Language l = new AnimalScript(tp.title, tp.authors,
				tp.screenResolution.width, tp.screenResolution.height);
		FiddlerOnTheRoof animation = new FiddlerOnTheRoof(l, tp);
		try {
			animation.buildAnimation(tp);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		String animationCode = l.getAnimationCode();

		System.out.println(animationCode);

		File file = new File("/tmp/animationCode.asu");
		try {
			FileUtils.writeStringToFile(file, animationCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildAnimation(BinaryTreeProperties props)
			throws IOException {
		final GraphWriterImpl<Integer> writer = new GraphWriterImpl<Integer>(language,
				layout);
		TreeEventListenerAggregator<Integer> animator = new TreeEventListenerAggregator<Integer>(
				language);
		animator.addAnimatior(new ExtractedBinaryTreeAnimations<Integer>(writer));
		animator.setLayout(this.layout);		
		List<Integer> ints = createSomeInts(20);
		BinaryTreeModel<Integer> model = BinaryTreeModel
				.createTreeByInsert(ints);
		SplayTreeModel<Integer> splayTreeModel = SplayTreeModel.from(model);		
		animator.setModel(splayTreeModel);

		Collections.shuffle(ints);
		Iterator<Integer> iterator = ints.iterator();
		Text text = null;
		while(iterator.hasNext()){
			Integer next = iterator.next();
			if(text!=null){
				text.hide(Timings.NOW);
			}			
			text = language.newText(new Coordinates(0, 0), "Suche Wert: " + next, "text", null);
			splayTreeModel.search(next);
		}
		
		step();
	}

	private Text printText(Text newText, Node<Integer> search, String t) {
		if (newText != null)
			newText.hide(Timings.NOW);
		newText = language.newText(new Coordinates(0, 0), "Rotiere " + t + " um "
				+ search.getValue(), "LOL", null);
		return newText;
	}

	private void step() {
		language.nextStep();
	}

	Random r = new Random(234);

	private List<Integer> createSomeInts(int howmuch) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < howmuch; ++i) {
			list.add(r.nextInt(howmuch * 20));
		}
		return list;
	}

	private @Nullable
	Group createTextGroup(List<String> readLines, Offset anchor) {
		if (readLines.isEmpty())
			return null;
		Offset nextTextPos = anchor;
		int groupId = runninggroupidentifier++;
		LinkedList<Primitive> texts = new LinkedList<Primitive>();
		for (int i = 0; i < readLines.size(); i++) {
			Text text = language.newText(nextTextPos, readLines.get(i), groupId
					+ "id" + i, null);
			nextTextPos = new Offset(0,
					this.animationProperties.verticalTextGap, text, "SW");
			texts.add(text);
		}
		Group introGroup = language.newGroup(texts, "group" + groupId);
		return introGroup;
	}

	void setSourceCodeGroup(SourceCode sc) {
		this.sourceCode = sc;
	}

	public SourceCode getSourceCode() {
		if (sourceCode == null)
			throw new IllegalStateException("no source code has been set yet");
		else
			return sourceCode;
	}

	/**
	 * Fills the {@link SourceCode} object {@code sc} with the lines provided by
	 * {@code codeGroupText}.
	 */
	private SourceCode getFilledSourceCode(String codeGroupText, SourceCode sc) {
		List<String> split = StrUtils.toLines(codeGroupText);
		for (int i = 0; i < split.size(); i++) {
			sc.addCodeLine(split.get(i), "", 0, null);
		}
		return sc;
	}

	private @Nullable
	Group createTextGroup(String text, Offset location) {
		List<String> stringToLinesAtDelimiter = StrUtils.toLines(text);
		return createTextGroup(stringToLinesAtDelimiter, location);
	}

	private SourceCode getSourceCodeDummy(Offset node) {
		SourceCodeProperties scProps = new SourceCodeProperties();
		SourceCode sc = language.newSourceCode(node, "algorithmOperations",
				null, scProps);
		String codeGroupText = " \n \n \n";
		sc = getFilledSourceCode(codeGroupText, sc);
		return sc;
	}

}
