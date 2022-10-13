package PrideBot.Game.ForVersion2;

import java.util.Random;

public class Dice {
    private static final Random RANDOM = new Random();
    private static final int DICE_COUNT = 7;
    private static final int D2_INDEX = 0;
    private static final int D4_INDEX = 1;
    private static final int D6_INDEX = 2;
    private static final int D8_INDEX = 3;
    private static final int D10_INDEX = 4;
    private static final int D12_INDEX = 5;
    private static final int D20_INDEX = 6;
    private static final int[] DICE_SIDES = {2, 4, 6, 8, 10, 12, 20};

    private final long[] DICE_COUNTS;
    private final long[] DICE_MODS;
    private final long TOTAL_MOD;

    protected static final Dice D2 = new Dice(new long[]{1, 0, 0, 0, 0, 0, 0}, new long[DICE_COUNT], 0);
    protected static final Dice D4 = new Dice(new long[]{1, 1, 0, 0, 0, 0, 0}, new long[DICE_COUNT], 0);
    protected static final Dice D6 = new Dice(new long[]{0, 0, 1, 0, 0, 0, 0}, new long[DICE_COUNT], 0);
    protected static final Dice D8 = new Dice(new long[]{0, 0, 0, 1, 0, 0, 0}, new long[DICE_COUNT], 0);
    protected static final Dice D10 = new Dice(new long[]{0, 0, 0, 0, 1, 0, 0}, new long[DICE_COUNT], 0);
    protected static final Dice D12 = new Dice(new long[]{0, 0, 0, 0, 0, 1, 0}, new long[DICE_COUNT], 0);
    protected static final Dice D20 = new Dice(new long[]{0, 0, 0, 0, 0, 0, 1}, new long[DICE_COUNT], 0);


    protected Dice(long[] diceCounts, long[] diceMods, long totalMod) {
        assert diceCounts.length == DICE_COUNT && diceMods.length == DICE_COUNT;
        this.DICE_COUNTS = diceCounts;
        this.DICE_MODS = diceMods;
        this.TOTAL_MOD = totalMod;
    }

    protected Dice add(Dice other) {
        long[] diceCountsSum = new long[DICE_COUNT];
        long[] diceModsSum = new long[DICE_COUNT];
        for (int i = 0; i < DICE_COUNT; i++) {
            diceCountsSum[i] = DICE_COUNTS[i] + other.getDiceCount(i);
            diceModsSum[i] = DICE_MODS[i] + other.getDiceMod(i);
        }
        long totalMod = TOTAL_MOD + other.getTotalMod();
        return new Dice(diceCountsSum, diceModsSum, totalMod);
    }

    protected long roll() {
        long sum = 0;
        for (int i = 0; i < DICE_COUNT; i++) {
            for (int j = 0; j < DICE_COUNTS[i]; j++) {
                sum += rollDice(i) + DICE_MODS[i];
            }
        }
        return sum + TOTAL_MOD;
    }

    private int rollDice(int index) {
        return RANDOM.nextInt(DICE_SIDES[index]) + 1;
    }

    //__Getters___\\

    protected long getDiceCount(int index) {
        return DICE_COUNTS[index];
    }

    protected long getD2Count() {
        return DICE_COUNTS[D2_INDEX];
    }

    protected long getD4Count() {
        return DICE_COUNTS[D4_INDEX];
    }

    protected long getD6Count() {
        return DICE_COUNTS[D6_INDEX];
    }

    protected long getD8Count() {
        return DICE_COUNTS[D8_INDEX];
    }

    protected long getD10Count() {
        return DICE_COUNTS[D10_INDEX];
    }

    protected long getD12Count() {
        return DICE_COUNTS[D12_INDEX];
    }

    protected long getD20Count() {
        return DICE_COUNTS[D20_INDEX];
    }

    protected long getDiceMod(int index) {
        return DICE_MODS[index];
    }

    protected long getD2Mod() {
        return DICE_MODS[D2_INDEX];
    }

    protected long getD4Mod() {
        return DICE_MODS[D4_INDEX];
    }

    protected long getD6Mod() {
        return DICE_MODS[D6_INDEX];
    }

    protected long getD8Mod() {
        return DICE_MODS[D8_INDEX];
    }

    protected long getD10Mod() {
        return DICE_MODS[D10_INDEX];
    }

    public long getD12Mod() {
        return DICE_MODS[D12_INDEX];
    }

    protected long getD20Mod() {
        return DICE_MODS[D20_INDEX];
    }

    protected long getTotalMod() {
        return TOTAL_MOD;
    }
}