package CodeAnalysis;

public class Main {
    public static void main(String[] args) {
        String path = "C:\\Users\\drewm\\IdeaProjects\\PrideGameDiscordBot\\src\\main\\java\\PrideBot";
        Analysis analysis = Analyzer.analyzeDirectory(path);
        System.out.println(analysis.toString(0));
    }
}
