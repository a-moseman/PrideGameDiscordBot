package PrideBot.SpellBook;

public class LuxuriateSpell extends Spell {
    protected LuxuriateSpell(int level) {
        super("Luxuriate", level, 1, convertLevelToSpellEffect(level), 3);
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
