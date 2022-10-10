package SpellBook;

public class ExtinguishSpell extends Spell {
    protected ExtinguishSpell() {
        super("Extinguish", 1, new SpellEffect(0, false, true, 0), 3);
    }
}
