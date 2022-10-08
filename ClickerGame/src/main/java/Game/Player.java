package Game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Finished for version 1.
 */
public class Player {
    protected final String UUID;
    private PlayerStats stats;
    private long lastCollectionTime;

    protected Player(String uuid) {
        this.UUID = uuid;
        this.stats = new PlayerStats();
        //this.lastCollectionTime = System.currentTimeMillis();
        this.lastCollectionTime = 0;
    }

    protected Player(String uuid, PlayerStats playerStats, long lastCollectionTime) {
        this.UUID = uuid;
        this.stats = playerStats;
        this.lastCollectionTime = lastCollectionTime;
    }

    protected boolean collect() {
        if (Util.millisecondsToDays(System.currentTimeMillis() - lastCollectionTime) >= 1) {
            switch (currentAffinity()) {
                case 0:
                    if (Util.randomBoolean()) {
                        collectDailyPride();
                    }
                    else {
                        collectDailyShame();
                    }
                    break;
                case 1: // pride affinity
                    collectDailyPride();
                    break;
                case -1: // shame affinity
                    collectDailyShame();
                    break;
            }
            lastCollectionTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    protected double daysUntilNextCollection() {
        return Math.max(0, 1d - Util.millisecondsToDays(System.currentTimeMillis() - lastCollectionTime));
    }

    private void collectDailyPride() {
        stats.addPride(1 + stats.getEgo());
    }

    private void collectDailyShame() {
        stats.addShame(1 + stats.getGuilt());
    }

    protected int currentAffinity() {
        if (stats.getHonor() == stats.getDishonor()) {
            if (stats.getEgo() == stats.getGuilt()) {
                if (stats.getPride() == stats.getShame()) {
                    return 0;
                }
                if (stats.getPride() > stats.getShame()) {
                    return 1;
                } else {
                    return -1;
                }
            }
            if (stats.getEgo() > stats.getGuilt()) {
                return 1;
            } else {
                return 0;
            }
        }
        if (stats.getHonor() > stats.getDishonor()) {
            return 1;
        } else {
            return 0;
        }
    }

    protected PlayerStats getStats() {
        return stats;
    }

    protected long getLastCollectionTime() {
        return lastCollectionTime;
    }

    protected ObjectNode buildJsonNode(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.set("uuid", mapper.convertValue(UUID, JsonNode.class));
        node.set("last_collection_time", mapper.convertValue(lastCollectionTime, JsonNode.class));
        node.set("stats", stats.buildObjectNode(mapper));
        return node;
    }
}
