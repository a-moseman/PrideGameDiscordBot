package SpellBook;

import java.util.Random;

public class SpellGenerator {
    private static final Random RANDOM = new Random();
    private static final double SPELL_PROBABILITY = 0.33;
    private static final double EMPOWER_SPELL_PROBABILITY = 0.67;
    private static final double LUXURIATE_SPELL_PROBABILITY = 0.33;

    protected static Spell get() {
        if (RANDOM.nextDouble() < SPELL_PROBABILITY) {
            return randomSpell();
        }
        return null;
    }

    private static Spell randomSpell() {
        double r = RANDOM.nextDouble();
        if (r < EMPOWER_SPELL_PROBABILITY) {
            return new EmpowerSpell(RANDOM.nextInt(5) + 1);
        }
        if (r < LUXURIATE_SPELL_PROBABILITY) {
            return new EmpowerSpell(RANDOM.nextInt(5) + 1);
        }
        return null;
    }
}
