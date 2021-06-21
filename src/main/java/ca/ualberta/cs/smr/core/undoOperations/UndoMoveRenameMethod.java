package ca.ualberta.cs.smr.core.undoOperations;

import ca.ualberta.cs.smr.core.refactoringObjects.*;
import ca.ualberta.cs.smr.core.refactoringObjects.typeObjects.MethodSignatureObject;
import ca.ualberta.cs.smr.utils.Utils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.refactoring.*;
import com.intellij.usageView.UsageInfo;

public class UndoMoveRenameMethod {

    Project project;

    public UndoMoveRenameMethod(Project project) {
        this.project = project;
    }

    /*
     * Undo the rename method refactoring that was performed in the commit
     */
    public void undoMoveRenameMethod(RefactoringObject ref) {
        MoveRenameMethodObject moveRenameMethodObject = (MoveRenameMethodObject) ref;
        MethodSignatureObject original = moveRenameMethodObject.getOriginalMethodSignature();
        MethodSignatureObject renamed = moveRenameMethodObject.getDestinationMethodSignature();
        String originalMethodName = original.getName();
        String originalClassName = moveRenameMethodObject.getOriginalClassName();
        String destinationClassName = moveRenameMethodObject.getDestinationClassName();
        // get the PSI class using original the qualified class name
        String filePath = moveRenameMethodObject.getDestinationFilePath();
        Utils utils = new Utils(project);
        utils.addSourceRoot(filePath);
        PsiClass psiClass = utils.getPsiClassFromClassAndFileNames(destinationClassName, filePath);
        assert psiClass != null;
        VirtualFile vFile = psiClass.getContainingFile().getVirtualFile();
        PsiMethod psiMethod = Utils.getPsiMethod(psiClass, renamed);
        assert psiMethod != null;

        // If the operation was renamed, undo the method rename by performing a rename method refactoring to rename it
        // to the original name
        if(moveRenameMethodObject.isRenameMethod()) {
            RefactoringFactory factory = JavaRefactoringFactory.getInstance(project);
            RenameRefactoring renameRefactoring = factory.createRename(psiMethod, originalMethodName, true, true);
            UsageInfo[] refactoringUsages = renameRefactoring.findUsages();
            renameRefactoring.doRefactoring(refactoringUsages);
        }
        // If the operation was moved, undo the move method by performing a move method refactoring to move it to the
        // original class
        if(moveRenameMethodObject.isMoveMethod()) {
            JavaRefactoringFactory refactoringFactory = JavaRefactoringFactory.getInstance(project);
            String visibility = original.getVisibility();
            PsiMember[] psiMembers = new PsiMember[1];
            psiMembers[0] = psiMethod;
            MoveMembersRefactoring moveMethodRefactoring = refactoringFactory.createMoveMembers(psiMembers,
                    originalClassName, visibility);
            UsageInfo[] refactoringUsages = moveMethodRefactoring.findUsages();
            moveMethodRefactoring.doRefactoring(refactoringUsages);
            psiClass = moveMethodRefactoring.getTargetClass();
            // If the method was not moved to the correct location within the class, move it to the correct location
            moveMethodToOriginalLocation(psiClass, psiMethod,  moveRenameMethodObject.getStartOffset());
        }
        // Update the virtual file that contains the refactoring
        vFile.refresh(false, true);

    }

    /*
     * Move the method to the correct location within the class using the text offset detected by RefMiner.
     */
    private void moveMethodToOriginalLocation(PsiClass psiClass, PsiMethod psiMethod,  int startOffset) {
        // Get all of the methods inside of the class.
        PsiMethod[] psiMethods = psiClass.getMethods();

        if(!psiClass.isValid() || !psiMethod.isValid()) {
            return;
        }

        // Get the physical copy of the PSI method so we can delete it
        for(PsiMethod method : psiMethods) {
            if(method.getSignature(PsiSubstitutor.UNKNOWN).equals(psiMethod.getSignature(PsiSubstitutor.UNKNOWN))) {
                psiMethod = method;
                break;
            }
        }
        PsiMethod psiMethodBefore = null;
        // Find which method comes before the moved method
        for(PsiMethod otherMethod : psiMethods) {
            int otherMethodStartOffset = otherMethod.getTextOffset();
            int otherMethodEndOffset = otherMethod.getTextLength() + otherMethodStartOffset;
            if(otherMethodEndOffset < startOffset) {
                psiMethodBefore = otherMethod;
            }
        }
        final PsiMethod newMethod = PsiElementFactory.getInstance(project).createMethodFromText(psiMethod.getText(), psiClass);
        PsiMethod finalPsiMethod = psiMethod;
        // if it's the first method in the class
        PsiMethod psiMethodAfter = null;
        if(psiMethodBefore == null) {
            for(PsiMethod otherMethod : psiMethods) {
                if(!otherMethod.isConstructor()) {
                    psiMethodAfter = otherMethod;
                    break;
                }
            }
            PsiMethod finalPsiMethodAfter = psiMethodAfter;
            WriteCommandAction.runWriteCommandAction(project, () -> {
                psiClass.addAfter(newMethod, finalPsiMethodAfter);
                finalPsiMethod.delete();
            });
            return;
        }
        PsiMethod finalPsiMethodBefore = psiMethodBefore;
        WriteCommandAction.runWriteCommandAction(project, () -> {
            psiClass.addAfter(newMethod, finalPsiMethodBefore);
            finalPsiMethod.delete();
        });
        psiMethods = psiClass.getMethods();
        for(PsiMethod m : psiMethods) {
            System.out.println(m.getName());
        }

        psiMethods = psiClass.getMethods();
        for(PsiMethod m : psiMethods) {
            System.out.println(m.getName());
        }
    }

}
