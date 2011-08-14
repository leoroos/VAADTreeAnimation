package de.lere.vaad.binarysearchtree;

import de.lere.vaad.EndOfTheWorldException;
import de.lere.vaad.binarysearchtree.TreeDeleteSourceTraversingEvent.DeleteTraversingPosition;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.treebuilder.events.TreeDeleteEvent;
import de.lere.vaad.treebuilder.events.TreeSourceTraversingEvent;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition;

public class TreeDeleteAnimation<T extends Comparable<T>>
		extends
		BinarySearchTreeAnimationsBase<T, TreeDeleteEvent<T>, TreeDeleteSourceTraversingEvent<T>> {

	public TreeDeleteAnimation(BinarySearchTreeSetup<T> p) {
		super(p);
	}

	@Override
	protected String animatedAlgorithm() {
		return "deleteAlgo.txt";
	}

	@Override
	protected void sourceCodeTraversion(TreeDeleteSourceTraversingEvent<T> event) {
		DeleteTraversingPosition position = event.traversingPosition;
		T delVal = event.delVal;
		Node<T> nodeOne = event.currentPosition;
		String nodeOneVal = getDefaultedVal(nodeOne, "NIL");		
		Node<T> nodeTwo = event.optional;
		String nodeTwoVal = getDefaultedVal(nodeTwo, "NIL");
		BinaryTreeModel<T> model = event.model;
		switch(position){
		
		case DeleteInit:
			coarse("Löschen von " + delVal);
			fine("Die Lösch Methode wird mit dem Baum\n" +
					" und dem zu löschenden Wert aufgerufen");
			hl(1);
			hlNode(nodeOne, model);
			break;
		case TestIfLeftChildNull:
			blinkNode(nodeOne, model);
			hl(2);
			break;
		case NoLeftChild:
			fine("Der zu löschende Knoten hat kein linkes Kind\n" +
					"kann also mit seinem rechten Kind " + nodeOneVal + " ersetzt werden");
			hl(3);
			break;
		case FinishAfterTransplantWithRightChild:
			fine("Keine Knoz mehr muss pause mac...................................");
			break;
		case TestIfRightChildNull:
			blinkNode(nodeOne, model);
			hl(4);
			break;
		case NoRightChild:
			break;
		case FinishAfterTransplantWithLeftChild:
			break;
		case GetMinimumOfRight:
			break;
		case TestIfParentOfMinNotDeletee:
			break;
		case TransplantingSuccessorWithItsRightChild:
			break;
		case SettingDeleteesRightToSuccessorsRightAndSettingNewRightsParent:
			break;
		case TransplantingDeleteeWithSuccessor:
			break;
		case SettingSuccessorLeftWithDeleteeLeftAndSettingsNewLeftsParent:
			break;
		case StartingTransplant:
			break;
		case TestTransplantIfOldHasParent:
			break;
		case TransplantReplacesRoot:
			break;
		case TestIfOldWasLeftChild:
			break;
		case SettingNewNodeAsLeftToParentOfOldNode:
			break;
		case TestIfOldWasRightChild:
			break;
		case SettingNewNodeAsRightToParentOfOldNode:
			break;
		case TransplantSetsParentOfOldToNew:
			break;
		
		default:
			throw new EndOfTheWorldException();
		
		}
	}

	private String getDefaultedVal(Node<T> nodeOne, String defaultVal) {
		if(nodeOne != null)
			return nodeOne.getValue().toString();
		return defaultVal;
	}

	private void hlNode(Node<T> nodeOne, BinaryTreeModel<T> model) {
		graphWriter.highlightNode(model, nodeOne);
	}

	private void blinkNode(Node<T> nodeOne, BinaryTreeModel<T> model) {
		graphWriter.blinkNode(model, nodeOne, ts.NOW, ts.SHORT_ANIMATION);
	}

	private void hl(int line) {
		sourceWriter.highlight(line);
	}

	@Override
	protected void modelModificationHandling(TreeDeleteEvent<T> ie) {
		boolean successful = ie.statistics.successful;
		if (successful) {
			coarse("Das Löschen des Knotens "
					+ ie.nodeOfModification.getValue() + " benötigte \n"
					+ +ie.statistics.numberOfComparisons + " Vergleiche \n"
					+ ie.statistics.transplantations
					+ " Knoten Vertauschung(en) (möglicherweise mit Null im Fall eines Blattes ).");
		} else
			coarse("Knoten "
					+ ie.deleteValue
					+ "war nicht im Baum vorhanden, konnte dementsprechend nicht gelöscht werden");
	}

	@Override
	protected String steptextOnSourceTraversingEvent(
			TreeDeleteSourceTraversingEvent<T> ie) {
		if (ie.traversingPosition == DeleteTraversingPosition.DeleteInit) {
			return "Deletion of " + ie.delVal;
		} else
			return null;
	}

	@Override
	protected String steptextOnModificationEvent(TreeDeleteEvent<T> ie) {
		if (ie.statistics.successful) {
			return "Successful deletion of " + ie.deleteValue;
		} else
			return "Unsuccessful deletion of " + ie.deleteValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends TreeDeleteEvent<T>> getModelChangeEventClass() {
		return (Class<? extends TreeDeleteEvent<T>>) TreeDeleteEvent.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Class<? extends TreeDeleteSourceTraversingEvent<T>> getSourceTraversingClass() {
		return (Class<? extends TreeDeleteSourceTraversingEvent<T>>) TreeDeleteSourceTraversingEvent.class;
	}

}
