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

	private BinaryTreeModel<T> tmpPriorModel;
	private Node[] prev;
	private BinaryTreeModel<T> prevModel;

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
		switch (position) {

		case DeleteInit:
			graphWriter.setAutomaticUnhighlightNodes(true);
			coarse("Löschen von " + delVal);
			fine("Die Lösch Methode wird mit dem Baum\n"
					+ " und dem zu löschenden Wert aufgerufen");
			hl(1);
			hlNode(nodeOne, model);
			break;
		case TestIfLeftChildNull:
			blinkNode(nodeOne, model);
			hl(2);
			break;
		case NoLeftChild:
			fine("Der zu löschende Knoten hat kein linkes Kind\n"
					+ "kann also mit seinem rechten Kind " + nodeOneVal
					+ " ersetzt werden");
			hl(3);
			break;
		case FinishAfterTransplantWithRightChild:
			blinkNode(nodeOne, model);
			break;
		case TestIfRightChildNull:
			blinkNode(nodeOne, model);
			hl(4);
			break;
		case NoRightChild:
			fine("Der zu löschende Knoten hat kein rechtes Kind\n"
					+ "kann also mit seinem linken Kind " + nodeOneVal
					+ " ersetzt werden");
			break;
		case FinishAfterTransplantWithLeftChild:
			blinkNode(nodeOne, model);
			break;
		case GetMinimumOfRight:
			fine("Der zu löschende Knoten hat linkes und rechtes Kind.\n"
					+ "Es wird versucht ihn mit seinem Nachfolger"+nodeOneVal+" zu ersetzen.");
			hl(7);
			hlNode(nodeOne, model);
			break;
		case TestIfParentOfMinNotDeletee:
			fine("Es wird geprüft ob Vater des Nachfolgers "+nodeOneVal+" der zu löschende Knoten ist");
			hl(8);
			break;
		case TransplantingSuccessorWithItsRightChild:
			fine("Ist der zu löschende Knoten nicht direkt mit dem Nachfolger verbunden\n" +
					"wird der Nachfolger als nächstes mit seinem rechten Kind ausgetauscht " + nodeTwoVal + " asugetauscht.");
			hl(9);
			break;
		case SettingDeleteesRightToSuccessorsRightAndSettingNewRightsParent:
			fine("Es folgt die Anpassung der Kind und Elternknoten.");
			hl(10,11);
			break;
		case TransplantingDeleteeWithSuccessor:
			fine("Der zu löschende Knoten wird schließlich mit seinem Nachfolger " + nodeOneVal + " ausgetauscht");
			hl(12);
			break;
		case SettingSuccessorLeftWithDeleteeLeftAndSettingsNewLeftsParent:
			fine("Nach dem Austausch werden wieder die Kind und Elternknoten des Nachfolgers angepasst.");
			hl(13,14);
			break;
		case StartingTransplant:
			fine("Knoten "+ nodeOneVal + " soll mit " + nodeTwoVal + " ausgetauscht werden");
			graphWriter.highlightNode(model, ts.NOW, ts.SHORT_ANIMATION, nodeOne, nodeTwo);
			hl(16);
			break;
		case TestTransplantIfOldHasParent:
			fine("Es wird getestet ob alter Knoten root war");
			hlNode(nodeOne, model);
			hl(17);
			break;
		case TransplantReplacesRoot:
			fine("Zu ersetzender Knoten war root. Neuer Root wird der ersetzende Knoten" + nodeOne);
			hlNode(nodeOne, model);
			hl(18);
			break;
		case TestIfOldWasLeftChild:
			fine("Test ob alter Knoten "+nodeOneVal+" linkes Kind ist.");
			blinkNode(nodeOne, model);
			Node<T> nodeByID = model.getNodeByID(nodeOne);
			graphWriter.highlightNode(model, ts.NOW, ts.SHORT_ANIMATION, nodeOne, nodeByID.getParent());
			this.tmpPriorModel = model;
			hl(19);
			break;
		case SettingNewNodeAsLeftToParentOfOldNode:
			fine("Zu löschender Knoten ist linkes Kind.\n" + " Auszutauschender Knoten wird als linkes Kind von " +nodeOneVal + " gesetzt");
			prev = new Node[] { nodeOne, nodeTwo };
			prevModel = model;
			graphWriter.highlightNode(model, ts.NOW, ts.SHORT_ANIMATION,prev);
			hl(20);
			break;
		case SettingNewNodeAsRightToParentOfOldNode:
			fine("Zu löschender Knoten ist rechtes Kind.\n" + " Auszutauschender Knoten wird als rechtes Kind von " +nodeOneVal + " gesetzt");
			prev = new Node[] { nodeOne, nodeTwo };
			prevModel = model;
//			for (Node iterable_element : prev) {
//				graphWriter.unhighlightNode(model, iterable_element);				
//			}
			graphWriter.highlightNode(model, ts.NOW, ts.SHORT_ANIMATION,prev);
			hl(22);
			step();
			break;
		case TransplantSetsParentOfOldToNew:
			fine("Falls zu ersetzender Knoten ungleich NIL ist wird sein parent\nmit dem parent des zu löschenden Knotens " + nodeOneVal + " ausgetauscht");
			hl(23,24);
			graphWriter.highlightNode(model, ts.NOW, ts.SHORT_ANIMATION);
			for (Node pn : prev) {
				graphWriter.unhighlightNode(prevModel, pn);
			}
			break;			
		default:
			throw new EndOfTheWorldException();
		}
	}

	private String getDefaultedVal(Node<T> nodeOne, String defaultVal) {
		if (nodeOne != null)
			return nodeOne.getValue().toString();
		return defaultVal;
	}

	private void hlNode(Node<T> nodeOne, BinaryTreeModel<T> model) {
		graphWriter.highlightNode(model, nodeOne);
	}

	private void blinkNode(Node<T> nodeOne, BinaryTreeModel<T> model) {
		graphWriter.blinkNode(model, nodeOne, ts.NOW, ts.SHORT_ANIMATION);
	}

	private void hl(int ... line ) {
		sourceWriter.highlightLines(line);
	}

	@Override
	protected void modelModificationHandling(TreeDeleteEvent<T> ie) {		
		boolean successful = ie.statistics.successful;
		if (successful) {
			coarse("Das Löschen des Knotens "
					+ ie.nodeOfModification.getValue()
					+ " benötigte \n"
					+ +ie.statistics.numberOfComparisons
					+ " Vergleiche \n"
					+ ie.statistics.transplantations
					+ " Knoten Vertauschung(en) (möglicherweise mit Null im Fall eines Blattes ).");
		} else
			coarse("Knoten "
					+ ie.deleteValue
					+ "war nicht im Baum vorhanden, konnte dementsprechend nicht gelöscht werden");
		graphWriter.setAutomaticUnhighlightNodes(true);
	}

	@Override
	protected String steptextOnSourceTraversingEvent(
			TreeDeleteSourceTraversingEvent<T> ie) {
		if (ie.traversingPosition == DeleteTraversingPosition.DeleteInit) {
			Node<T> currentPosition = ie.currentPosition;
			String childs = "";
			if (currentPosition != null) {
				childs = " with ";
				if (currentPosition.hasLeftChild()
						&& currentPosition.hasRightChild()) {
					childs += " two children Nodes.";
				} else if (currentPosition.hasLeftChild()
						|| currentPosition.hasRightChild()) {
					childs += " one child Node.";
				} else {
					childs = " which is a Leaf.";
				}
			}

			return "Deletion of " + ie.delVal + childs;
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
