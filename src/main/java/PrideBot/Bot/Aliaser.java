package PrideBot.Bot;

public class Aliaser {
    public static String translate(String in) {
        switch (in) {
            case "^":
            case "COLL":
                return "COLLECT";
            case "?":
            case "DESC":
                return "DESCRIBE";
            case "H":
                return "HELP";
            case "$":
                return "BUY";
            default:
                return in;
        }
    }
}