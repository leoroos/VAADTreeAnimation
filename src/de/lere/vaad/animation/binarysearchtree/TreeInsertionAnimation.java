package de.lere.vaad.animation.binarysearchtree;

import de.lere.vaad.EndOfTheWorldException;
import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.SourceCodeWriter;
import de.lere.vaad.animation.Timings;
import de.lere.vaad.animation.locationhandler.LocationDirector;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.events.TreeEvent;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.treebuilder.events.TreeInsertEvent;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition;
import de.lere.vaad.utils.TextLoaderUtil;

public class TreeInsertionAnimation<T extends Comparable<T>>
		extends
		BinarySearchTreeAnimationsBase<T, TreeInsertEvent<T>, TreeInsertSourceCodeTraversing<T>> {

	public TreeInsertionAnimation(BinarySearchTreeSetup<T> p) {
		super(p);
	}

	private de.lere.vaad.treebuilder.Node<T> previouslyHighlightedNode;
	private de.lere.vaad.treebuilder.Node<T> codeCurrentNodePos;
	private BinaryTreeModel<T> codeCurrentHighlightNodeModel;

	private void highlightLinesAndNextNode(int... lines) {
		this.sourceWriter.highlightLines(ts.SHORT_ANIMATION,
				ts.SHORT_ANIMATION, lines);
		highlightNode(codeCurrentNodePos, codeCurrentHighlightNodeModel);
	}

	private void highlightNode(
			de.lere.vaad.treebuilder.Node<T> currentPosition,
			BinaryTreeModel<T> model) {
		graphWriter.highlightNode(model, currentPosition);
	}

	private void sourceCodeDescription(TreeInsertSourceCodeTraversing<T> ie) {
		InsertSourceCodePosition position = ie.position;
		de.lere.vaad.treebuilder.Node<T> curPos = ie.currentPosition;
		T val = ie.insertionValue;
		switch (position) {

		case Init:
			coarse("Einfügen von " + val);
			fine(("Initialisierung: die Suche wird an der Wurzel "
					+ curPos.getValue() + " begonnen "));
			break;
		case CheckingIfInsertionPossible:
			if (curPos == null) {
				fine("Nächste zu prüfende Position ist Null also wurde Platz zum Einfügen gefunden.");
			} else {
				fine(("Solange kein Platz zum Einfügen gefunden wurde suche weiter\n um "
						+ val + " einzufügen. " + "Prüfe als nächstes " + curPos
						.getValue()));
			}
			break;
		case TestingIfWhereToFromCurrent:
			fine(("Teste ob Knoten " + val + " links oder recht von "
					+ curPos.getValue() + " einzufügen ist."));
			break;
		case LookingAlongLeftChild:
			fine((val + " war kleiner deswegen suche links weiter nach Ort zum Einfügen"));

			break;
		case LookingAlongRightChild:
			fine((val + " war größer deswegen suche rechts weiter nach Ort zum Einfügen"));
			break;
		case SettingParentForNewCurrentNode:
			if (curPos == null) {
				fine(("Der Ort zur Ersetzung hatte keinen Parent also wird für "
						+ val + " null als parent gesetzt"));
			} else {
				fine((val + " bekommt einen neuen Parent: " + curPos.getValue()));
			}
			break;
		case CheckingIfNewIsRoot:
			fine(("Prüfe ob " + val + " neuer root ist."));
			break;
		case FinalIsSettingToRoot:
			fine((val + " wird neue Wurzel."));
			break;
		case FinalIsSettingAsLeftChildFrom:
			fine(("Knoten " + val + " als linkes Kind eingfügt"));
			break;
		case FinalIsSettingAsRightChildFrom:
			fine(("Knoten " + curPos.getValue() + " als rechtes Kind eingfügt"));
			break;
		case CheckingIfToSetLeftInFinalStep:
			fine(("Prüfe ob Knoten " + val + " links oder rechts von "
					+ curPos.getValue() + " eingefügt werden soll"));
			break;

		default:
			throw new EndOfTheWorldException();
		}
	}

	@Override
	protected String animatedAlgorithm() {
		return "insertAlgo.txt";
	}

	@Override
	protected String steptextOnSourceTraversingEvent(
			TreeInsertSourceCodeTraversing<T> ie) {
		if (ie.position == InsertSourceCodePosition.Init) {
			return "Insertion of " + ie.insertionValue;
		} else
			return null;
	}

	@Override
	protected String steptextOnModificationEvent(TreeInsertEvent<T> ie) {
		return "Insertion performed for " + ie.nodeOfModification;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends TreeInsertEvent<T>> getModelChangeEventClass() {
		return (Class<? extends TreeInsertEvent<T>>) TreeInsertEvent.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends TreeInsertSourceCodeTraversing<T>> getSourceTraversingClass() {
		return (Class<? extends TreeInsertSourceCodeTraversing<T>>) TreeInsertSourceCodeTraversing.class;
	}

	@Override
	protected void sourceCodeTraversion(TreeInsertSourceCodeTraversing<T> event) {
		sourceCodeTraversing(event);
		sourceCodeDescription(event);
	}

	private void sourceCodeTraversing(TreeInsertSourceCodeTraversing<T> event) {
		TreeInsertSourceCodeTraversing<T> ie = (TreeInsertSourceCodeTraversing<T>) event;
		InsertSourceCodePosition curPos = ie.position;
		codeCurrentNodePos = ie.currentPosition;
		this.codeCurrentHighlightNodeModel = ie.currentModel;
		switch (curPos) {

		case Init:
			highlightLinesAndNextNode(2, 3);
			break;
		case CheckingIfInsertionPossible:
			highlightLinesAndNextNode(4);

			break;
		case TestingIfWhereToFromCurrent:
			highlightLinesAndNextNode(6);
			break;
		case LookingAlongLeftChild:
			highlightNode(previouslyHighlightedNode,
					codeCurrentHighlightNodeModel);
			highlightLinesAndNextNode(7);
			break;
		case LookingAlongRightChild:
			highlightNode(previouslyHighlightedNode,
					codeCurrentHighlightNodeModel);
			highlightLinesAndNextNode(9);

			break;
		case SettingParentForNewCurrentNode:
			highlightLinesAndNextNode(10);

			break;
		case CheckingIfNewIsRoot:
			highlightLinesAndNextNode(11);
			break;
		case FinalIsSettingToRoot:
			highlightLinesAndNextNode(12);
			break;
		case FinalIsSettingAsLeftChildFrom:
			highlightLinesAndNextNode(14);
			break;
		case FinalIsSettingAsRightChildFrom:
			highlightLinesAndNextNode(16);
			break;
		case CheckingIfToSetLeftInFinalStep:
			highlightLinesAndNextNode(13);
			break;

		default:
			throw new EndOfTheWorldException();
		}
	}

	@Override
	protected void modelModificationHandling(TreeInsertEvent<T> event) {
		TreeInsertEvent<T> e = (TreeInsertEvent<T>) event;
		int compStats = ((TreeInsertEvent<T>) event).insertionResult.numberOfComparisons;
		coarse("Das Einfügen des Knotens " + e.nodeOfModification.getValue()
				+ " hat " + compStats + " Vergleiche benötigt.");
	}

}
