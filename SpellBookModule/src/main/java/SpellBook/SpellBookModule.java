package SpellBook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class SpellBookModule {
    public static final long SPELL_COST = 2;

    private SpellBook spellBook;

    public SpellBookModule() {
        this.spellBook = new SpellBook();
    }

    public SpellBookModule(
            ArrayList<Spell> spells,
            double collectionCritChance,
            boolean prideFavored,
            boolean shameFavored,
            long bountifulness) {
        this.spellBook = new SpellBook(spells, new SpellEffect(
                collectionCritChance,
                prideFavored,
                shameFavored,
                bountifulness
        ));
    }

    public static Spell generateSpell() {
        return SpellGenerator.get();
    }

    public static Spell generateSpell(String name, int level) {
        switch (name) {
            case "Empower":
                return new EmpowerSpell(level);
            case "Luxuriate":
                return new LuxuriateSpell(level);
            case "Illuminate":
                return new IlluminateSpell();
            case "Extinguish":
                return new ExtinguishSpell();
            default:
                return null;
        }
    }

    public void addSpell(Spell spell) {
        spellBook.add(spell);
    }

    public Spell getSpell(int index) {
        return spellBook.get(index);
    }

    public int getSpellCount() {
        return spellBook.size();
    }

    public boolean castSpell(int index) {
        return spellBook.castSpell(index);
    }

    public double getCritChance() {
        return spellBook.getCurrentSpellEffect().COLLECTION_CRIT_CHANCE;
    }

    public long getBountifullness() {
        return spellBook.getCurrentSpellEffect().BOUNTIFULNESS;
    }

    public boolean isPrideFavored() {
        return spellBook.getCurrentSpellEffect().PRIDE_FAVORED;
    }

    public boolean isShameFavored() {
        return spellBook.getCurrentSpellEffect().SHAME_FAVORED;
    }

    public ObjectNode buildJsonNode(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.set("spells", spellBook.buildJsonNode(mapper));
        node.set("current_spell_effect", spellBook.getCurrentSpellEffect().buildJsonNode(mapper));
        return node;
    }
}
