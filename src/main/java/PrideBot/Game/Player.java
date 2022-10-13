package PrideBot.Game;

import PrideBot.SpellBook.Spell;
import PrideBot.SpellBook.SpellBookModule;
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

    private SpellBookModule spellBookModule;

    /**
     * Instantiate a new player.
     * @param uuid
     */
    protected Player(String uuid) {
        this.UUID = uuid;
        this.stats = new PlayerStats();
        this.lastCollectionTime = 0;
        this.spellBookModule = null;
        retroactivelyAddModules();
    }

    /**
     * Instantiate a level 0 player.
     * @param uuid
     * @param playerStats
     * @param lastCollectionTime
     */
    protected Player(String uuid, PlayerStats playerStats, long lastCollectionTime) {
        this.UUID = uuid;
        this.stats = playerStats;
        this.lastCollectionTime = lastCollectionTime;
        this.spellBookModule = null;
        retroactivelyAddModules();
    }

    /**
     * Instantiate a level 1 player.
     * @param uuid
     * @param playerStats
     * @param lastCollectionTime
     */
    protected Player(String uuid, PlayerStats playerStats, long lastCollectionTime, SpellBookModule spellBookModule) {
        this.UUID = uuid;
        this.stats = playerStats;
        this.lastCollectionTime = lastCollectionTime;
        this.spellBookModule = spellBookModule;
        retroactivelyAddModules();
    }

    protected void retroactivelyAddModules() {
        if (stats.getLevel() > 0 && spellBookModule == null) {
            spellBookModule = new SpellBookModule();
        }
    }

    protected boolean buySpell() {
        if (spellBookModule == null) {
            return false;
        }

        if (stats.getPride() == stats.getShame()) {
            if (Util.randomBoolean()) { // pride
                return buySpellWithPride();
            }
            else { // shame
                return buySpellWithShame();
            }
        }
        else if (stats.getPride() > stats.getShame()) {
            return buySpellWithPride();
        }
        else {
            return buySpellWithShame();
        }
    }

    private boolean buySpellWithPride() {
        if (stats.getPride() >= SpellBookModule.SPELL_COST) {
            Spell spell = spellBookModule.generateSpell();
            if (spell != null) {
                spellBookModule.addSpell(spell);
                stats.addPride(-SpellBookModule.SPELL_COST);
                return true;
            }
        }
        return false;
    }

    private boolean buySpellWithShame() {
        if (stats.getShame() >= SpellBookModule.SPELL_COST) {
            Spell spell = spellBookModule.generateSpell();
            if (spell != null) {
                spellBookModule.addSpell(spell);
                stats.addShame(-SpellBookModule.SPELL_COST);
                return true;
            }
        }
        return false;
    }

    protected boolean collect() {
        if (Util.millisecondsToDays(System.currentTimeMillis() - lastCollectionTime) >= 1) {
            if (hasSpellBookModule()) {
                if (spellBookModule.isPrideFavored()) {
                    collectDailyPride();
                }
                if (spellBookModule.isShameFavored()) {
                    collectDailyShame();
                }
            }

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
        long amount = 1 + stats.getEgo();
        if (hasSpellBookModule()) {
            amount += spellBookModule.getBountifullness();
            if (Util.randomDouble() < spellBookModule.getCritChance()) {
                amount *= 2;
            }
        }
        stats.addPride(amount);
    }

    private void collectDailyShame() {
        long amount = 1 + stats.getGuilt();
        if (hasSpellBookModule()) {
            amount += spellBookModule.getBountifullness();
            if (Util.randomDouble() < spellBookModule.getCritChance()) {
                amount *= 2;
            }
        }
        stats.addShame(amount);
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
                return -1;
            }
        }
        if (stats.getHonor() > stats.getDishonor()) {
            return 1;
        } else {
            return -1;
        }
    }

    protected PlayerStats getStats() {
        return stats;
    }

    protected boolean hasSpellBookModule() {
        return spellBookModule != null;
    }

    protected SpellBookModule getSpellBookModule() {
        return spellBookModule;
    }

    protected ObjectNode buildJsonNode(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.set("uuid", mapper.convertValue(UUID, JsonNode.class));
        node.set("last_collection_time", mapper.convertValue(lastCollectionTime, JsonNode.class));
        node.set("stats", stats.buildObjectNode(mapper));
        if (spellBookModule == null) {
            node.set("spell_book", null);
        }
        else {
            node.set("spell_book", spellBookModule.buildJsonNode(mapper));
        }
        return node;
    }
}
