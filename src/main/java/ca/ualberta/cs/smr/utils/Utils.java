package ca.ualberta.cs.smr.utils;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.DumbServiceImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.util.FileContentUtil;
import gr.uom.java.xmi.UMLOperation;
import gr.uom.java.xmi.UMLParameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    /*
     * Runs a command such as "cp -r ..." or "git merge-files ..."
     */
    public static void runSystemCommand(String... commands) {
        try {
            ProcessBuilder pb = new ProcessBuilder(commands);
            Process p = pb.start();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * Save the content of one directory to another
     */
    public static void saveContent(Project project, String dir) {
        // Save project to temporary directory
        String path = System.getProperty("user.home") + "/temp/" + dir;
        File file = new File(path);
        file.mkdirs();
        runSystemCommand("cp", "-r", project.getBasePath(), path);
    }

    /*
     * Remove the temp files
     */
    public static void clearTemp() {
        String path = System.getProperty("user.home") + "/temp/right";
        File file = new File(path);
        file.mkdirs();
        runSystemCommand("rm", "-rf", path);
        path = System.getProperty("user.home") + "/temp/left";
        runSystemCommand("rm", "-rf", path);
        path = System.getProperty("user.home") + "/temp/base";
        runSystemCommand("rm", "-rf", path);
    }

    public static void dumbServiceHandler(Project project) {
        if(DumbService.isDumb(project)) {
            DumbServiceImpl dumbService = DumbServiceImpl.getInstance(project);
            // Waits for the task to finish
            dumbService.completeJustSubmittedTasks();
        }
    }

    public static void reparsePSIClasses(Project project) {
        File file = new File(project.getBasePath());
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
        assert virtualFile != null;
        VirtualFile[] vFileArray = virtualFile.getChildren();
        ArrayList<VirtualFile> vFileCollection = new ArrayList<>(Arrays.asList(vFileArray));
        vFileCollection.add(virtualFile);
        FileContentUtil.reparseFiles(project, vFileCollection, true);
    }

    public static boolean ifSameMethods(PsiMethod method, UMLOperation operation) {
        PsiParameter[] psiParameterList = method.getParameterList().getParameters();
        List<UMLParameter> umlParameters = operation.getParameters();
        String umlName = operation.getName();
        String psiName = method.getName();
        // Check if the method names are the same
        if(!umlName.equals(psiName)) {
            return false;
        }
        // If the number of parameters are different, the methods are different
        // Subtract 1 from umlParameters because umlParameters includes return type
        if(umlParameters.size() - 1 != psiParameterList.length) {
            return false;
        }
        PsiType psiReturnType = method.getReturnType();
        assert psiReturnType != null;
        String psiType = psiReturnType.getPresentableText();
        UMLParameter umlParameter = umlParameters.get(0);
        String umlType = umlParameter.getType().toString();
        // Check if the return types are the same
        if(!psiType.equals(umlType)) {
            return false;
        }
        // Check if the parameters are the same
        for(int i = 1; i < umlParameters.size(); i++) {
            int j = i - 1;
            umlParameter = umlParameters.get(i);
            PsiParameter psiParameter = psiParameterList[j];
            umlType = umlParameter.getType().toString();
            psiType = psiParameter.getType().getPresentableText();
            if(!umlType.equals(psiType)) {
                return false;
            }

        }
        return true;
    }
}

