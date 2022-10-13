package PrideBot.Game;

import java.util.Random;

public class Util {
    private static final Random RANDOM = new Random();

    protected static double millisecondsToDays(long milliseconds) {
        return (double) milliseconds / 1000 / 60 / 60 / 24;
    }

    protected static double randomDouble() {
        return RANDOM.nextDouble();
    }

    protected static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }
}
