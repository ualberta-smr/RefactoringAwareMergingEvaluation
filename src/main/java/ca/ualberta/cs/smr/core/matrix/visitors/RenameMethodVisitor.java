package ca.ualberta.cs.smr.core.matrix.visitors;

import ca.ualberta.cs.smr.core.dependenceGraph.Node;
import ca.ualberta.cs.smr.core.matrix.elements.ExtractMethodElement;
import ca.ualberta.cs.smr.core.matrix.elements.RenameClassElement;
import ca.ualberta.cs.smr.core.matrix.elements.RenameMethodElement;

public class RenameMethodVisitor extends RefactoringVisitor {

    /*
     * Check if rename method conflicts with rename method
     */
    @Override
    public void visit(RenameMethodElement renameMethod) {
        boolean foundConflict = renameMethod.checkRenameMethodConflict(visitorNode);
        System.out.println("Rename Method/Rename Method conflict: " + foundConflict);
    }

    /*
     * Check if rename class conflicts with rename method
     */
    @Override
    public void visit(RenameClassElement renameClass) {
        Node elementNode = renameClass.checkRenameMethodDependence(visitorNode);
        if (elementNode != null) {
            // If there is dependence between branches, the rename method needs to happen before the rename class
                graph.updateGraph(visitorNode, elementNode);
        }
    }

    @Override
    public void visit(ExtractMethodElement extractMethod) {

    }
}
