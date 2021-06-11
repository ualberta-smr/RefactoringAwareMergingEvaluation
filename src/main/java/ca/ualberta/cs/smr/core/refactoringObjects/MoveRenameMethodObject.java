package ca.ualberta.cs.smr.core.refactoringObjects;

import gr.uom.java.xmi.UMLOperation;
import gr.uom.java.xmi.diff.RenameOperationRefactoring;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringType;


/*
 * Represents a rename method refactoring. Contains the necessary information for logic checks and performing the
 * refactoring using the IntelliJ refactoring engine.
 */
public class MoveRenameMethodObject implements RefactoringObject {

    private final RefactoringType refactoringType;
    private String originalFilePath;
    private String destinationFilePath;
    private MethodSignatureObject originalMethodSignature;
    private String originalClassName;
    private String destinationClassName;
    private MethodSignatureObject destinationMethodSignature;
    private boolean isRenameMethod;
    private boolean isMoveMethod;

    /*
     * Use the provided information to create the rename method object for testing.
     */
    public MoveRenameMethodObject(String originalFilePath, String originalClassName, MethodSignatureObject originalMethodSignature,
                                  String destinationFilePath, String destinationClassName, MethodSignatureObject destinationMethodSignature) {
        this.refactoringType = RefactoringType.RENAME_METHOD;
        this.originalFilePath = originalFilePath;
        this.originalClassName = originalClassName;
        this.originalMethodSignature = originalMethodSignature;
        this.destinationFilePath = destinationFilePath;
        this.destinationClassName = destinationClassName;
        this.destinationMethodSignature = destinationMethodSignature;

    }

    /*
     * Creates the rename method object and takes the information that we need from the RefMiner refactoring object.
     */
    public MoveRenameMethodObject(Refactoring refactoring) {
        RenameOperationRefactoring renameOperationRefactoring = (RenameOperationRefactoring) refactoring;
        UMLOperation originalOperation = renameOperationRefactoring.getOriginalOperation();
        UMLOperation destinationOperation = renameOperationRefactoring.getRenamedOperation();
        this.isMoveMethod = false;
        this.isRenameMethod = false;
        this.refactoringType = refactoring.getRefactoringType();
        setType(refactoringType);
        this.originalFilePath = originalOperation.getLocationInfo().getFilePath();
        this.destinationFilePath = destinationOperation.getLocationInfo().getFilePath();
        this.originalClassName = originalOperation.getClassName();
        this.destinationClassName = destinationOperation.getClassName();
        this.originalMethodSignature = new MethodSignatureObject(originalOperation.getName(), originalOperation.getParameters(),
                originalOperation.isConstructor(), originalOperation.getVisibility(), originalOperation.isStatic());
        this.destinationMethodSignature = new MethodSignatureObject(destinationOperation.getName(), destinationOperation.getParameters(),
                originalOperation.isConstructor(), originalOperation.getVisibility(), originalOperation.isStatic());
    }

    public RefactoringType getRefactoringType() {
        return this.refactoringType;
    }

    public RefactoringOrder getRefactoringOrder() {
        return RefactoringOrder.MOVE_RENAME_METHOD;
    }

    public void setOriginalFilePath(String originalFilePath) {
        this.originalFilePath = originalFilePath;
    }

    public String getOriginalFilePath() {
        return this.originalFilePath;
    }

    public void setDestinationFilePath(String destinationFilePath) {
        this.destinationFilePath = destinationFilePath;
    }

    public String getDestinationFilePath() {
        return this.destinationFilePath;
    }

    public void setOriginalClassName(String originalClassName) {
        this.originalClassName = originalClassName;
    }

    public String getOriginalClassName() {
        return this.originalClassName;
    }

    public void setDestinationClassName(String destinationClassName) {
        this.destinationClassName = destinationClassName;
    }

    public String getDestinationClassName() {
        return this.destinationClassName;
    }

    public MethodSignatureObject getOriginalMethodSignature() {
        return originalMethodSignature;
    }

    public void setOriginalMethodSignature(MethodSignatureObject originalMethodSignature) {
        this.originalMethodSignature = originalMethodSignature;
    }

    public MethodSignatureObject getDestinationMethodSignature() {
        return destinationMethodSignature;
    }

    public void setDestinationMethodSignature(MethodSignatureObject destinationMethodSignature) {
        this.destinationMethodSignature = destinationMethodSignature;
    }

    public void setType(RefactoringType refactoringType) {
        if(refactoringType.equals(RefactoringType.RENAME_METHOD)) {
            this.isRenameMethod = true;
        }
        else if(refactoringType.equals(RefactoringType.MOVE_OPERATION)) {
            this.isMoveMethod = true;
        }
    }

    public boolean isRenameMethod() {
        return isRenameMethod;
    }

    public boolean isMoveMethod() {
        return isMoveMethod;
    }
}
