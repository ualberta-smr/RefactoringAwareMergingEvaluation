package ca.ualberta.cs.smr.core.dependenceGraph;

import org.refactoringminer.api.Refactoring;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Refactoring refactoring;
    private List<Edge> adjacentNodes;
    private List<Node> dependsList;
    private Head head;
    private boolean visited;

    public Node(Refactoring refactoring) {
        this.refactoring = refactoring;
        this.adjacentNodes = new ArrayList<>();
        this.dependsList = new ArrayList<>();
        this.visited = false;
        this.head = new Head();
    }

    public Refactoring getRefactoring() {
        return refactoring;
    }

    public void addEdge(Edge edge) {
        this.adjacentNodes.add(edge);
    }

    public boolean hasEdges() {
        return !adjacentNodes.isEmpty();
    }

    public List<Edge> getEdges() {
        return adjacentNodes;
    }

    public void addToDependsList(Node node) {
        dependsList.add(node);
    }

    public List<Node> dependsOn() {
        return dependsList;
    }

    public boolean isDependent() {
        return !dependsList.isEmpty();
    }

    public void visiting() {
        visited = true;
    }

    public boolean wasVisited() {
        return visited;
    }

    public void updateHead(Node node) {
        head.update(node);
    }

    public String getDependenceChainClassHead() {
        if(head.getClassName() == null) {
            return refactoring.getInvolvedClassesBeforeRefactoring().iterator().next().getRight();
        }
        return head.getClassName();
    }
}
