package PrideBot.ARS;

import java.util.Random;

public class Util {
    public final static Random RANDOM = new Random();

    public static String getRandom(String[] arr) {
        return arr[RANDOM.nextInt(arr.length) - 1];
    }

    public static double getDouble() {
        return RANDOM.nextDouble();
    }
}
