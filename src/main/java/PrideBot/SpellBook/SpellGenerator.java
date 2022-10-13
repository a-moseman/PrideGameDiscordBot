package PrideBot.SpellBook;

import java.util.Random;

public class SpellGenerator {
    private static final Random RANDOM = new Random();
    private static final double SPELL_PROBABILITY = 0.33;
    private static final double EMPOWER_SPELL_THRESHOLD = 0.5;
    private static final double LUXURIATE_SPELL_THRESHOLD = 0.8;
    private static final double ILLUMINATE_SPELL_THRESHOLD = 0.9;
    private static final double EXTINGUISH_SPELL_THRESHOLD = 1;

    protected static Spell get() {
        if (RANDOM.nextDouble() < SPELL_PROBABILITY) {
            return randomSpell();
        }
        return null;
    }

    private static Spell randomSpell() {
        double r = RANDOM.nextDouble();
        if (r < EMPOWER_SPELL_THRESHOLD) {
            return new EmpowerSpell(RANDOM.nextInt(5) + 1);
        }
        if (r < LUXURIATE_SPELL_THRESHOLD) {
            return new LuxuriateSpell(RANDOM.nextInt(5) + 1);
        }
        if (r < ILLUMINATE_SPELL_THRESHOLD) {
            return new IlluminateSpell();
        }
        if (r < EXTINGUISH_SPELL_THRESHOLD) {
            return new ExtinguishSpell();
        }
        return null;
    }
}
