package SpellBook;

public class EmpowerSpell extends Spell {
    private static final double LEVEL_1_PROB = 1;
    private static final double LEVEL_2_PROB = 0.5;
    private static final double LEVEL_3_PROB = 0.25;
    private static final double LEVEL_4_PROB = 0.125;
    private static final double LEVEL_5_PROB = 0.0625;

    protected EmpowerSpell(int level) {
        super("Empower lv. " + level, 1, convertLevelToSpellEffect(level), 5);
    }

    private static SpellEffect convertLevelToSpellEffect(int level) {
        switch (level) {
            case 1:
                return new SpellEffect(
                        0.1,
                        false,
                        false,
                        0
                );
            case 2:
                return new SpellEffect(
                        0.2,
                        false,
                        false,
                        0
                );
            case 3:
                return new SpellEffect(
                        0.3,
                        false,
                        false,
                        0
                );
            case 4:
                return new SpellEffect(
                        0.4,
                        false,
                        false,
                        3
                );
            case 5:
                return new SpellEffect(
                        0.5,
                        false,
                        false,
                        5
                );
        }
        return null;
    }
}
