package PrideBot.Bot;

import java.util.Locale;
import java.util.Random;

public class AdaptiveValueParser {
    private static final Random RANDOM = new Random();

    public static int parse(String in) {
        String[] operands = in.toUpperCase(Locale.ROOT).replace(" ", "").split("\\+");
        int out = 0;
        for (int i = 0; i < operands.length; i++) {
            out += parseOperand(operands[i]);
        }
        return out;
    }

    private static int parseOperand(String in) {
        if (in.contains("D")) {
            return parseDice(in);
        }
        else {
            return Integer.parseInt(in);
        }
    }

    private static int parseDice(String in) {
        String[] parts = in.split("D");
        int count = Integer.parseInt(parts[0]);
        int sides = Integer.parseInt(parts[1]);
        int out = 0;
        for (int i = 0; i < count; i++) {
            out += RANDOM.nextInt(sides) + 1;
        }
        return out;
    }
}
