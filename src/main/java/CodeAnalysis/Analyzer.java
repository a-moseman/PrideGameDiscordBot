package CodeAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Analyzer {
    private static Analysis analyzeFile(String path) {
        File file = new File(path);
        List<String> contents = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                contents.add(scanner.nextLine());
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        int nonEmptyLines = 0;
        int codeLines = 0;
        for (int i = 0; i < contents.size(); i++) {
            if (!contents.get(i).equals("")) {
                nonEmptyLines++;
                if (!isCommentLine(contents.get(i))) {
                    codeLines++;
                }
            }
        }
        return new Analysis(file.getName(), contents.size(), nonEmptyLines, codeLines, file.isFile());
    }

    private static boolean isCommentLine(String line) {
        String l = line.replace(" ", "");
        return l.startsWith("/") || l.startsWith("*");
    }

    public static Analysis analyzeDirectory(String path) {
        File directory = new File(path);
        File[] contents = directory.listFiles();
        Analysis analysis = new Analysis(directory.getName(), 0, 0, 0, directory.isFile());
        for (int i = 0; i < contents.length; i++) {
            if (contents[i].isFile()) {
                analysis.addInnerAnalysis(analyzeFile(contents[i].getPath()));

            }
            else {
                analysis.addInnerAnalysis(analyzeDirectory(contents[i].getPath()));
            }
        }
        return analysis;
    }
}
