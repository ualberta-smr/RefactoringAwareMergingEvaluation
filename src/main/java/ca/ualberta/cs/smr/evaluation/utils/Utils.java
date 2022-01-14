package ca.ualberta.cs.smr.evaluation.utils;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.DumbServiceImpl;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    Project project;

    private static final boolean LOG_TO_FILE  = true;
    private static final String LOG_FILE = "log.txt";


    public Utils(Project project) {
        this.project = project;
    }

    /*
     * Runs a command such as "cp -r ..." or "git merge-files ..."
     */
    public static void runSystemCommand(String... commands) {
        try {
            ProcessBuilder pb = new ProcessBuilder(commands);
            Process p = pb.start();
            p.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void log(String projectName, Object message) {
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z").format(new Date());
        String logMessage = timeStamp + " ";
        if (message instanceof String){
            logMessage += (String) message;
        } else if (message instanceof Exception) {
            logMessage += ((Exception) message).getMessage() + "\n";
            StringBuilder stackBuilder = new StringBuilder();
            StackTraceElement[] stackTraceElements = ((Exception) message).getStackTrace();
            for (int i = 0; i < stackTraceElements.length; i++) {
                StackTraceElement stackTraceElement = stackTraceElements[i];
                stackBuilder.append(stackTraceElement.toString());
                if (i < stackTraceElements.length - 1) stackBuilder.append("\n");
            }
            logMessage += stackBuilder.toString();
        } else {
            logMessage = message.toString();
        }
        System.out.println(logMessage);

        if (LOG_TO_FILE) {
            String logPath = LOG_FILE;
            if (projectName != null && !projectName.trim().equals("")) logPath = projectName;
            try {
                String path = System.getProperty("user.home") + "/temp/logs/";
                new File(path).mkdirs();
                Files.write(Paths.get(path + logPath), Arrays.asList(logMessage),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeContent(String path, String content) {
        try {
            Files.write(Paths.get(path), Arrays.asList(content),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * Save the content of one directory to another. Return the path
     */
    public static String saveContent(Project project, String path) {
        File file = new File(path);
        file.mkdirs();
        runSystemCommand("cp", "-r", project.getBasePath() + "/.", path);
        return path;
    }

    /*
     * Remove the temp files
     */
    public static void clearTemp(String dir) {
        //String path = System.getProperty("user.home") + "/temp/" + dir;
        File file = new File(dir);
        file.mkdirs();
        runSystemCommand("rm", "-rf", dir);
    }

    public static void dumbServiceHandler(Project project) {
        if(DumbService.isDumb(project)) {
            DumbServiceImpl dumbService = DumbServiceImpl.getInstance(project);
            // Waits for the task to finish
            dumbService.completeJustSubmittedTasks();
        }
    }

    public static void reparsePsiFiles(Project project) {
        PsiDocumentManager.getInstance(project).commitAllDocuments();
    }


    /*
     * Get each line from the input stream containing the IntelliMerge dataset.
     */
    public static ArrayList<String> getLinesFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayList<String> lines = new ArrayList<>();
        while(reader.ready()) {
            lines.add(reader.readLine());
        }
        return lines;
    }

}

