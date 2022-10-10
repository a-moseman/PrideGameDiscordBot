package SpellBook;

public class LuxuriateSpell extends Spell {
    private static final double LEVEL_1_PROB = 1;
    private static final double LEVEL_2_PROB = 0.5;
    private static final double LEVEL_3_PROB = 0.25;
    private static final double LEVEL_4_PROB = 0.125;
    private static final double LEVEL_5_PROB = 0.0625;


    protected LuxuriateSpell(int level) {
        super("Luxuriate lv. " + level, 1, convertLevelToSpellEffect(level), 3);
    }

    private static SpellEffect convertLevelToSpellEffect(int level) {
        switch (level) {
            case 1:
                return new SpellEffect(
                        0,
                        false,
                        false,
                        2
                );
            case 2:
                return new SpellEffect(
                        0,
                        false,
                        false,
                        5
                );
            case 3:
                return new SpellEffect(
                        0,
                        false,
                        false,
                        8
                );
            case 4:
                return new SpellEffect(
                        0,
                        false,
                        false,
                        12
                );
            case 5:
                return new SpellEffect(
                        0,
                        false,
                        false,
                        16
                );
        }
        return null;
    }
}
