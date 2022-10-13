package PrideBot.SpellBook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Spell {
    protected final String NAME; // must be unique
    protected final int LEVEL;
    protected final double CAST_RATE; // per day
    private final SpellEffect SPELL_EFFECT;
    private long lastCastTime; // milliseconds
    private int uses; // how many times can you cast the spell before you lose it

    protected Spell(String name, int level, double castRate, SpellEffect spellEffect, int uses) {
        this.NAME = name;
        this.LEVEL = level;
        this.CAST_RATE = castRate;
        this.SPELL_EFFECT = spellEffect;
        this.uses = uses;
        this.lastCastTime = 0;
    }

    protected Spell(String name, int level, double castRate, SpellEffect spellEffect, int uses, long lastCastTime) {
        this.NAME = name;
        this.LEVEL = level;
        this.CAST_RATE = castRate;
        this.SPELL_EFFECT = spellEffect;
        this.uses = uses;
        this.lastCastTime = lastCastTime;
    }

    protected boolean canCast() {
        return (double) (System.currentTimeMillis() - lastCastTime) / 1000 / 60 / 60 / 24 >= CAST_RATE;
    }

    protected SpellEffect cast() {
        if (canCast()) {
            lastCastTime = System.currentTimeMillis();
            uses--;
            return SPELL_EFFECT;
        }
        return null;
    }

    protected int getUses() {
        return uses;
    }

    protected ObjectNode buildJsonNode(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.set("name", mapper.convertValue(NAME, JsonNode.class));
        node.set("level", mapper.convertValue(LEVEL, JsonNode.class));
        return node;
    }
}
