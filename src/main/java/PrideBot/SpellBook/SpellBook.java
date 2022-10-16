package PrideBot.SpellBook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

public class SpellBook {
    private ArrayList<Spell> spells;
    private SpellEffect currentSpellEffect;

    protected SpellBook() {
        this.spells = new ArrayList<>();
        this.currentSpellEffect = SpellEffect.EMPTY;
    }

    protected SpellBook(ArrayList<Spell> spells, SpellEffect currentSpellEffect) {
        this.spells = spells;
        this.currentSpellEffect = currentSpellEffect;
    }

    protected int size() {
        return spells.size();
    }

    protected Spell get(int index) {
        return spells.get(index);
    }

    protected void add(Spell spell) {
        spells.add(spell);
    }

    protected boolean castSpell(int index) {
        if (index >= spells.size()) {
            return false;
        }
        SpellEffect spellEffect = spells.get(index).cast();
        if (spellEffect != null) {
            currentSpellEffect = currentSpellEffect.sum(spellEffect);
            if (spells.get(index).getUses() <= 0) { // remove used up spells
                spells.remove(index);
            }
            return true;
        }
        return false;
    }

    protected SpellEffect getCurrentSpellEffect() {
        return currentSpellEffect;
    }

    protected ArrayNode buildJsonNode(ObjectMapper mapper) {
        ArrayNode node = mapper.createArrayNode();
        for (Spell spell : spells) {
            node.add(spell.buildJsonNode(mapper));
        }
        return node;
    }
}
