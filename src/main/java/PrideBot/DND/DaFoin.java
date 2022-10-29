package PrideBot.DND;

import java.util.Random;

public class DaFoin {
    private final static Random RANDOM = new Random();

    public static int getDireness() {
        return RANDOM.nextInt(20) + 1;
    }

    public static String getFish() {
        return FishList.FISH[RANDOM.nextInt(FishList.FISH.length - 1)];
    }
}
