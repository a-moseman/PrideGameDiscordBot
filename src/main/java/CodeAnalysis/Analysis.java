package CodeAnalysis;

import java.util.ArrayList;
import java.util.List;

public class Analysis {
    private String name;
    private int lines;
    private int nonEmptyLines;
    private int codeLines;
    private String type;
    private List<Analysis> innerAnalysisList;

    public Analysis(String name, int lines, int nonEmptyLines, int codeLines, boolean isFile) {
        this.name = name;
        this.lines = lines;
        this.nonEmptyLines = nonEmptyLines;
        this.codeLines = codeLines;
        this.type = isFile ? "Class" : "Package";
        this.innerAnalysisList = new ArrayList<>();
    }

    public void addInnerAnalysis(Analysis analysis) {
        lines += analysis.lines;
        nonEmptyLines += analysis.nonEmptyLines;
        codeLines += analysis.codeLines;
        innerAnalysisList.add(analysis);
    }

    public int getInnerAnalysisListSize() {
        return innerAnalysisList.size();
    }

    public Analysis getInnerAnalysis(int index) {
        return innerAnalysisList.get(index);
    }

    public String getName() {
        return name;
    }

    public int getLines() {
        return lines;
    }

    public int getNonEmptyLines() {
        return nonEmptyLines;
    }

    public int getCodeLines() {
        return codeLines;
    }

    public String toString() {
        return toString(0, 0);
    }

    public String toString(int depth) {
        return toString(0, depth);
    }

    public String toString(int indentation, int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append(indentation(indentation)).append(name).append(":\n");
        sb.append(indentation(indentation + 1)).append("Lines: ").append(lines).append('\n');
        sb.append(indentation(indentation + 1)).append("Non Empty Lines: ").append(nonEmptyLines).append('\n');
        sb.append(indentation(indentation + 1)).append("Code Lines: ").append(codeLines).append('\n');
        sb.append(indentation(indentation + 1)).append("Type: ").append(type).append('\n');
        if (innerAnalysisList.size() > 0) {
            if (depth == 0) {
                return sb.toString();
            }
            sb.append(indentation(indentation + 1)).append("Contents:").append('\n');
            for (Analysis analysis : innerAnalysisList) {
                sb.append(analysis.toString(indentation + 2, depth - 1));
            }
        }
        return sb.toString();
    }

    private String indentation(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append('\t');
        }
        return sb.toString();
    }
}
