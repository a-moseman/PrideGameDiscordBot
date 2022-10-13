package PrideBot.Game.SaveDataTranslators;

import PrideBot.SpellBook.Spell;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Translate1_0To2_0 implements Translator {
    public static ObjectNode convert(ObjectMapper mapper, JsonNode in) {
        ObjectNode root = in.deepCopy();
        ArrayNode playersNode = root.get("players").deepCopy();
        ObjectNode playerNode;
        for (int i = 0; i < playersNode.size(); i++) {
            playerNode = playersNode.get(i).deepCopy();
            playerNode.set("spell_book", generateEmptySpellBookNode(mapper));
            playersNode.set(i, playerNode);
        }
        root.set("players", playersNode);
        return root;
    }

    private static ObjectNode generateEmptySpellBookNode(ObjectMapper mapper) {
        ObjectNode spellBookNode = mapper.createObjectNode();
        spellBookNode.set("spells", mapper.convertValue(new ArrayList<Spell>(), JsonNode.class));
        spellBookNode.set("current_spell_effect", mapper.convertValue(generateEmptySpellEffectNode(mapper), JsonNode.class));
        return spellBookNode;
    }

    private static ObjectNode generateEmptySpellEffectNode(ObjectMapper mapper) {
        ObjectNode currentSpellEffectNode = mapper.createObjectNode();
        currentSpellEffectNode.set("collection_crit_chance", mapper.convertValue(0.0d, JsonNode.class));
        currentSpellEffectNode.set("pride_favored", mapper.convertValue(false, JsonNode.class));
        currentSpellEffectNode.set("shame_favored", mapper.convertValue(false, JsonNode.class));
        currentSpellEffectNode.set("bountifulness", mapper.convertValue(0l, JsonNode.class));
        return currentSpellEffectNode;
    }
}
