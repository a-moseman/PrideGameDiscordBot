package SpellBook;

public class IlluminateSpell extends Spell {
    protected IlluminateSpell() {
        super("Illuminate", 1, new SpellEffect(0, true, false, 0), 3);
    }
}
