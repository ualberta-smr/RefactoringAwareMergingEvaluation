package ca.ualberta.cs.smr.refmerge.replayOperations;

import ca.ualberta.cs.smr.refmerge.refactoringObjects.PushDownFieldObject;
import ca.ualberta.cs.smr.refmerge.refactoringObjects.RefactoringObject;
import ca.ualberta.cs.smr.refmerge.utils.Utils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.refactoring.memberPushDown.PushDownProcessor;
import com.intellij.refactoring.util.DocCommentPolicy;
import com.intellij.refactoring.util.classMembers.MemberInfo;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.UsageView;
import com.intellij.usages.UsageViewManager;

import java.util.ArrayList;
import java.util.List;

public class ReplayPushDownField {

    Project project;

    public ReplayPushDownField(Project project) {
        this.project = project;
    }

    /*
     * Replay push down field refactoring by performing push down refactoring
     */
    public void replayPushDownField(RefactoringObject refactoringObject) {

        PushDownFieldObject pushDownFieldObject = (PushDownFieldObject) refactoringObject;
        String superClassName = pushDownFieldObject.getOriginalClass();
        String superClassFile = pushDownFieldObject.getOriginalFilePath();
        String originalFieldName = pushDownFieldObject.getOriginalFieldName();

        Utils utils = new Utils(project);
        utils.addSourceRoot(superClassFile, superClassName);

        PsiClass superClass;
        // Use the super class (original) name and file path to get the class that the field is pushed down from
        superClass = utils.getPsiClassFromClassAndFileNames(superClassName, superClassFile);

        // If we cannot find the PSI class, do not try to invert the refactoring
        if(superClass == null) {
            return;
        }

        VirtualFile vFile = superClass.getContainingFile().getVirtualFile();
        // Get the PSI field using the PSI super class and field name
        PsiField psiField = Utils.getPsiField(superClass, originalFieldName);
        if(psiField == null) {
            return;
        }

        List<MemberInfo> memberInfos = new ArrayList<>();
        // Create the member info for the refactored field
        MemberInfo memberInfo = new MemberInfo(psiField);
        memberInfos.add(memberInfo);
        // Use as is policy for now
        DocCommentPolicy<PsiComment> policy = new DocCommentPolicy<>(DocCommentPolicy.ASIS);
        PushDownProcessor<MemberInfo, PsiMember, PsiClass> pushDownProcessor = new PushDownProcessor<>(superClass, memberInfos, policy);


        List<Pair<String,String>> subClasses = pushDownFieldObject.getSubClasses();
        UsageInfo[] usageInfos = new UsageInfo[subClasses.size()];
        // Get the list of subclasses that the field is pushed down to from
        for(int i = 0; i < usageInfos.length; i++) {
            Pair<String, String> subClass = subClasses.get(i);
            String className = subClass.getFirst();
            String fileName = subClass.getSecond();
            PsiClass psiSubClass = utils.getPsiClassFromClassAndFileNames(className, fileName);
            // If the class cannot be found, skip the given class
            if(psiSubClass == null) {
                continue;
            }
            UsageInfo usageInfo = new UsageInfo(psiSubClass, false);
            usageInfos[i] = usageInfo;
        }
        // Push down the method to the corresponding classes
        WriteCommandAction.runWriteCommandAction(project, () -> pushDownProcessor.pushDownToClasses(usageInfos));
        // Delete the original field
        WriteCommandAction.runWriteCommandAction(project, psiField::delete);


        UsageViewManager viewManager = UsageViewManager.getInstance(project);
        UsageView usageView = viewManager.getSelectedUsageView();
        if(usageView != null) {
            usageView.close();
        }


        // Update the virtual file that contains the refactoring
        vFile.refresh(false, true);


    }

}
