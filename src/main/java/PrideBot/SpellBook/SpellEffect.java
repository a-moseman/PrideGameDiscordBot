package PrideBot.SpellBook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SpellEffect {
    protected static final SpellEffect EMPTY = new SpellEffect(0, false, false, 0);

    protected final double COLLECTION_CRIT_CHANCE; // probability amount of pride or shame gained on collection will be doubled
    protected final boolean PRIDE_FAVORED; // forces collection to give pride
    protected final boolean SHAME_FAVORED; // forces collection to give shame
    protected final long BOUNTIFULNESS; // additional pride or shame gained per collection, calculated after crit doubling

    protected SpellEffect(
            double collectionCritChance,
            boolean prideFavored,
            boolean shameFavored,
            long bountifulness
    ) {
        assert collectionCritChance <= 1;
        assert (prideFavored && !shameFavored) || (!prideFavored && shameFavored);
        assert bountifulness >= 0;
        this.COLLECTION_CRIT_CHANCE = collectionCritChance;
        this.PRIDE_FAVORED = prideFavored;
        this.SHAME_FAVORED = shameFavored;
        this.BOUNTIFULNESS = bountifulness;
    }

    protected SpellEffect sum(SpellEffect other) {
        double collectionCritChance = Math.min(0, COLLECTION_CRIT_CHANCE + other.COLLECTION_CRIT_CHANCE);
        boolean prideFavored;
        boolean shameFavored;
        if (!other.PRIDE_FAVORED && !other.SHAME_FAVORED) {
            prideFavored = PRIDE_FAVORED;
            shameFavored = SHAME_FAVORED;
        }
        else {
            prideFavored = other.PRIDE_FAVORED;
            shameFavored = other.SHAME_FAVORED;
        }
        long bountifulness = BOUNTIFULNESS + other.BOUNTIFULNESS;

        return new SpellEffect(
                collectionCritChance,
                prideFavored,
                shameFavored,
                bountifulness
        );
    }

    protected ObjectNode buildJsonNode(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();;
        node.set("collection_crit_chance", mapper.convertValue(COLLECTION_CRIT_CHANCE, JsonNode.class));
        node.set("pride_favored", mapper.convertValue(PRIDE_FAVORED, JsonNode.class));
        node.set("shame_favored", mapper.convertValue(SHAME_FAVORED, JsonNode.class));
        node.set("bountifulness", mapper.convertValue(BOUNTIFULNESS, JsonNode.class));
        return node;
    }
}
