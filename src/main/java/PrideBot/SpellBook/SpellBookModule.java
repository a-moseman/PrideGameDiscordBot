package PrideBot.SpellBook;

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

    public String getSpellList() {
        if (spellBook.size() == 0) {
            return "Empty.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spellBook.size(); i++) {
            sb.
                    append(i).
                    append(". ").
                    append(spellBook.get(i).NAME).
                    append(" lv. ").
                    append(spellBook.get(i).LEVEL).
                    append('\n');
        }
        return sb.toString();
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

    public String getSpellName(int spellIndex) {
        return spellBook.get(spellIndex).NAME;
    }

    public int getSpellLevel(int spellIndex) {
        return spellBook.get(spellIndex).LEVEL;
    }

    public String getSpellDescription(int spellIndex) {
        String description = "";
        Spell spell = spellBook.get(spellIndex);
        description += spell.NAME + " lv. " + spell.LEVEL + ":";
        switch (spell.NAME) {
            case "Luxuriate":
                description += "\n\tDescription: Adds additional Bountifulness to your next collection. (collection amount = 1 + [ego or guilt] + bountifulness)";
                description += "\n\tBountifulness: " + spell.SPELL_EFFECT.BOUNTIFULNESS;
                break;
            case "Empower":
                description += "\n\tDescription: Adds a chances to crit on your next collection. Higher levels also grants a bit of Bountifulness. (collection amount = (1 + [ego or guilt] + bountifulness)";
                description += "\n\tOn a crit, your collection amount will be doubled. (collection amount = 2 * (1 + [ego or guilt] + bountifulness))";
                description += "\n\tBountifulness: " + spell.SPELL_EFFECT.BOUNTIFULNESS;
                description += "\n\tCrit Chance: " + spell.SPELL_EFFECT.COLLECTION_CRIT_CHANCE;
            case "Illuminate":
                description += "\n\tDescription: Gives you the Pride Favored effect for your next collection. Pride Favored guarantees you will collect pride.";
            case "Extinguish":
                description += "\n\tDescription: Gives you the Shame Favored effect for your next collection. Shame Favored guarantees you will collect shame.";
        }
        description += "\n\tRemaining Uses: " + spell.getUses();
        return description;
    }
}
