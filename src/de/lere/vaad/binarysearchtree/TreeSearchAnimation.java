package de.lere.vaad.binarysearchtree;

import de.lere.vaad.EndOfTheWorldException;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.treebuilder.events.TreeEvent;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.treebuilder.events.TreeInsertEvent;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing;
import de.lere.vaad.treebuilder.events.TreeSearchCodeTraversingEvent;
import de.lere.vaad.treebuilder.events.TreeSearchEvent;
import de.lere.vaad.treebuilder.events.TreeSourceTraversingEvent;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition;
import de.lere.vaad.treebuilder.events.TreeSearchCodeTraversingEvent.SearchTraversingPosition;

public class TreeSearchAnimation<T extends Comparable<T>>
		extends
		BinarySearchTreeAnimationsBase<T, TreeSearchEvent<T>, TreeSearchCodeTraversingEvent<T>> {

	public TreeSearchAnimation(BinarySearchTreeSetup<T> p) {
		super(p);
	}

	@Override
	protected String animatedAlgorithm() {
		return "iterativeSearchAlgo.txt";
	}

	@Override
	protected String steptextOnModificationEvent(TreeSearchEvent<T> ie) {
		if(ie.statistics.successful)
			return "Finished successful search for " + ie.searchVal;
		else
			return "Finished unsuccessful search for " + ie.searchVal;
	}

	@Override
	protected String steptextOnSourceTraversingEvent(
			TreeSearchCodeTraversingEvent<T> ie) {
		if (ie.traversingPosition == SearchTraversingPosition.Init) {
			return "Search of " + ie.searchVal;
		} else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends TreeSearchEvent<T>> getModelChangeEventClass() {
		return (Class<? extends TreeSearchEvent<T>>) TreeSearchEvent.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends TreeSearchCodeTraversingEvent<T>> getSourceTraversingClass() {
		return (Class<? extends TreeSearchCodeTraversingEvent<T>>) TreeSearchCodeTraversingEvent.class;
	}

	@Override
	protected void sourceCodeTraversion(TreeSearchCodeTraversingEvent<T> cast) {
		TreeSearchCodeTraversingEvent<T> ie = cast;
		BinaryTreeModel<T> currentModel = ie.currentModel;
		Node<T> curNode = ie.currentPos;
		String nodeVal = "null";
		if (curNode != null)
			nodeVal = ie.currentPos.getValue().toString();
		SearchTraversingPosition codePos = ie.traversingPosition;
		T searchVal = ie.searchVal;
		switch (codePos) {
		case Init:
			coarse("Suche nach Knoten mit Wert " + searchVal);
			fine("Die Suche beginnt am übergebenen Knoten."
					+ " x ist zu Beginn die Wurzel: " + nodeVal);
			sourceWriter.highlight(1);
			graphWriter.highlightNode(currentModel, curNode);
			break;
		case TestIfGoOnSearching:
			fine("Teste ob weiter gesucht werden muss.\n"
					+ "Das heißt ist der aktuelle Knoten Nil oder hat er den gesuchten Wert.");
			sourceWriter.highlight(2);
			graphWriter.highlightNode(currentModel, curNode);
			break;
		case TestIfSearchAlongLeftChild:
			fine("Teste ob links oder rechts vom aktuellen Knoten "
					+ nodeVal + " weiter gesucht werden soll.");
			sourceWriter.highlight(3);
			graphWriter.highlightNode(currentModel, curNode);
			break;
		case GoOnSearchingAlongLeftChild:
			fine("Der gesuchte Wert " + searchVal
					+ " war kleiner also wird auf Knoten " + nodeVal
					+ " links weiter gesucht");
			sourceWriter.highlight(4);
			graphWriter.highlightNode(currentModel, curNode);
			break;
		case GoOnSearchingAlongRightChild:
			fine("Der gesuchte Wert " + searchVal
					+ " war kleiner also wird auf Knoten " + nodeVal
					+ " rechts weiter gesucht");
			sourceWriter.highlight(6);
			graphWriter.highlightNode(currentModel, curNode);
			break;
		case FinalReturnSearchResult:
			if (curNode == null)
				fine("Der Schlüssel wurde nicht im Baum gefunen, deswegen wird NIL zurückgegeben");
			else
				fine("Die Suche war erfolgreich, der entsprechende Wert "
						+ nodeVal + " wird zurück gegeben.");
			sourceWriter.highlight(7);
			graphWriter.highlightNode(currentModel, curNode);
			break;
		default:
			throw new EndOfTheWorldException("Sollte nie hierher kommen");
		}
	}

	@Override
	protected void modelModificationHandling(TreeSearchEvent<T> event) {
		int compStats = event.statistics.numberOfComparisons;
		coarse("Das Suchen des Wertes " + event.searchVal + " hat " + compStats
				+ " Vergleiche benötigt.");
		graphWriter.buildGraph(event.beforeChange,
				ts.DEFAULT_ANIMATION);		
	}

}
