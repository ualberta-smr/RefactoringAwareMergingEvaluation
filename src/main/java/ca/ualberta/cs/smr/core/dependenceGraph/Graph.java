package ca.ualberta.cs.smr.core.dependenceGraph;

import ca.ualberta.cs.smr.utils.sortingUtils.Pair;
import gr.uom.java.xmi.diff.RenameClassRefactoring;
import gr.uom.java.xmi.diff.RenameOperationRefactoring;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringType;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private List<Node> nodes;

    public Graph(List<Pair> pairs) {
        this.nodes = new ArrayList<>();
        if(pairs.size() == 0) {
            return;
        }
        if(pairs.size() == 1) {
            Node node = new Node(pairs.get(0).getValue());
            addNode(node);
            return;
        }
        for(int i = pairs.size() - 1; i > -1; i--) {
            Pair pair = pairs.get(i);
            Refactoring refactoring = pair.getValue();
            Node node = new Node(refactoring);
            traverseGraph(node);
            addNode(node);
        }

    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void addEdge(Node from, int weight, Node to) {
        Edge edge = new Edge(from, weight, to);
        from.addEdge(edge);
    }

    void traverseGraph(Node newNode) {
        for(Node node : nodes) {
            if(hasDependence(node, newNode)) {
                addEdge(node, 1, newNode);
                return;
            }
            else {
                if(node.hasNeighbors() || newNode.hasNeighbors()) {
                    continue;
                }
                addEdge(node, 0, newNode);
            }
        }

    }

    public boolean hasDependence(Node node1, Node node2) {
        Refactoring refactoring1 = node1.getRefactoring();
        Refactoring refactoring2 = node2.getRefactoring();
        if(refactoring1.getRefactoringType() == RefactoringType.RENAME_CLASS && refactoring2.getRefactoringType() == RefactoringType.RENAME_METHOD) {
            String refactoring1Class = ((RenameClassRefactoring) refactoring1).getOriginalClassName();
            String refactoring2Class = ((RenameOperationRefactoring) refactoring2).getOriginalOperation().getClassName();
            return refactoring1Class.equals(refactoring2Class);
        }
        else if(refactoring1.getRefactoringType() == RefactoringType.RENAME_METHOD && refactoring2.getRefactoringType() == RefactoringType.RENAME_CLASS) {
            String refactoring1Class = ((RenameOperationRefactoring) refactoring1).getOriginalOperation().getClassName();
            String refactoring2Class = ((RenameClassRefactoring) refactoring2).getOriginalClassName();
            return refactoring1Class.equals(refactoring2Class);
        }
        return false;
    }

    public void printGraph() {

        for(Node node : nodes) {
            for(Edge edge : node.getEdges()) {
                if(edge.getWeight() == 1) {
                    System.out.println(edge.getSource().getRefactoring().toString() +
                            " <== " + edge.getDestination().getRefactoring().toString());
                }
            }
            for(Edge edge : node.getEdges()) {
                if(edge.getWeight() == 0) {
                    System.out.println(edge.getSource().getRefactoring().toString() +
                            " <-- " + edge.getDestination().getRefactoring().toString());
                }
            }
        }

    }


}