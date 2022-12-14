package PrideBot.Game;

import PrideBot.SpellBook.Spell;
import PrideBot.SpellBook.SpellBookModule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

public class Player implements Comparable<Player> {
    protected final String UUID;
    private PlayerStats stats;
    private long lastCollectionTime;
    private String name; // DO NOT STORE PERSISTENTLY

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    private SpellBookModule spellBookModule;

    /**
     * Instantiate a new player.
     * @param uuid The UUID of the player.
     */
    protected Player(String uuid) {
        this.name = "[missing]";
        this.UUID = uuid;
        this.stats = new PlayerStats();
        this.lastCollectionTime = 0;
        this.spellBookModule = null;
        retroactivelyAddModules();
    }

    /**
     * Instantiate a level 0 player.
     * @param uuid The UUID of the player.
     * @param playerStats The player's stats.
     * @param lastCollectionTime The last time the player collected.
     */
    protected Player(String uuid, PlayerStats playerStats, long lastCollectionTime) {
        this.name = "[missing]";
        this.UUID = uuid;
        this.stats = playerStats;
        this.lastCollectionTime = lastCollectionTime;
        this.spellBookModule = null;
        retroactivelyAddModules();
    }

    /**
     * Instantiate a level 1 player.
     * @param uuid The UUID of the player.
     * @param playerStats The player's stats.
     * @param lastCollectionTime The last time the player collected.
     * @param spellBookModule The spell book module of the player.
     */
    protected Player(String uuid, PlayerStats playerStats, long lastCollectionTime, SpellBookModule spellBookModule) {
        this.name = "[missing]";
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
            Spell spell = SpellBookModule.generateSpell();
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
            Spell spell = SpellBookModule.generateSpell();
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

            int currentAffinity = currentAffinity();
            if (currentAffinity == 0) {
                if (Util.randomBoolean()) {
                    collectDailyPride();
                }
                else {
                    collectDailyShame();
                }
            }
            if (Util.randomDouble() < Configuration.COLLECTION_MISFIRE_PROBABILITY) {
                currentAffinity = -currentAffinity;
            }
            else if (currentAffinity > 0) {
                collectDailyPride();
            }
            else {
                collectDailyShame();
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
                return Long.compare(stats.getPride(), stats.getShame());
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

    @Override
    public int compareTo(@NotNull Player o) {
        if (stats.getLevel() > o.getStats().getLevel()) {
            return 1;
        }
        else if (stats.getLevel() < o.getStats().getLevel()) {
            return -1;
        }
        else {
            if (stats.getTierOne() > o.getStats().getTierOne()) {
                return 1;
            }
            else if (stats.getTierOne() < o.getStats().getTierOne()) {
                return -1;
            }
            else {
                if (stats.getTierZero() > o.getStats().getTierZero()) {
                    return 1;
                }
                else if (stats.getTierZero() < o.stats.getTierZero()) {
                    return -1;
                }
                else  {
                    return 0;
                }
            }
        }
    }
}
