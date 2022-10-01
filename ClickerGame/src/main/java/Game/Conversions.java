package Game;

public class Conversions {

    protected static long egoToPride(long ego) {
        return ego * 10;
    }

    protected static long prestigeToEgo(long prestige) {
        return prestige * 10;
    }

    protected static double millisecondsToDays(long milliseconds) {
        return (double) milliseconds / 1000 / 60 / 60 / 24;
    }
}
