package SpellBook;

public class EmpowerSpell extends Spell {
    protected EmpowerSpell(int level) {
        super("Empower", level, 1, convertLevelToSpellEffect(level), 5);
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
