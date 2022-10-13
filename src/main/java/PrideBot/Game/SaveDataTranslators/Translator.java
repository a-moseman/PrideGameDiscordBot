package PrideBot.Game.SaveDataTranslators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Translator {
    public static ObjectNode convert(ObjectMapper mapper, JsonNode in) {
        return null;
    }
}
