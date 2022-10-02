package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Player implements Comparable<Player> {
    private static final Random RANDOM = new Random();
    private Stats stats;
    private ArrayList<Artifact> artifacts;
    private long lastPrideCollectionTime;
    private long pride; // The atomic value of the game
    private long ego; // Determines the rate of pride gain per day
    private static final int MAX_PRESTIGE_LEVEL = 1; // TODO: update as new prestige levels are added
    private int prestige; // Used for a level up system, giving access to new content per level

    private CombatModule combatModule; // Unlocked at prestige 1

    protected Player() {
        // Needed for Jackson JSON library serialization/deserialization or something
        // i.e. don't remove or code breaks
        this.stats = new Stats(1);
        this.artifacts = new ArrayList<>();

    }

    protected Player(long lastPrideCollectionTime, long pride, long ego, int prestige, ArrayList<Artifact> artifacts, CombatModule combatModule) {
        this.stats = new Stats(1);
        this.lastPrideCollectionTime = lastPrideCollectionTime;
        this.pride = pride;
        this.ego = ego;
        this.prestige = prestige;
        this.artifacts = artifacts;
        this.combatModule = combatModule;
    }

    protected Stats getEffectiveStats() {
        Stats effectiveStats = stats.clone();
        for (Artifact artifact : artifacts) {
            effectiveStats = effectiveStats.add(artifact.getStats());
        }
        return effectiveStats;
    }

    protected void addPride(long pride) {
        this.pride += pride;
    }

    protected boolean collectPride() {
        if (Conversions.millisecondsToDays(System.currentTimeMillis() - lastPrideCollectionTime) >= 1) {
            lastPrideCollectionTime = System.currentTimeMillis();
            Stats effectiveStats = getEffectiveStats();
            long amount = effectiveStats.PRIDE_PER_COLLECTION;
            if (effectiveStats.COLLECTION_CRIT_PROBABILITY > 0 && RANDOM.nextDouble() < effectiveStats.COLLECTION_CRIT_PROBABILITY) { // crit!
                amount *= 2;
            }
            pride += amount + ego;
            return true;
        }
        return false;
    }

    protected boolean levelUpEgo(long levels) {
        long initialEgo = this.ego;
        while (Conversions.egoToPride(this.ego + 1) <= this.pride && levels > 0) {
            this.pride -= Conversions.egoToPride(this.ego + 1);
            this.ego++;
            levels--;
        }
        if (this.ego > initialEgo) {
            return true;
        }
        return false;
    }

    protected boolean levelUpPrestige() {
        if (Conversions.prestigeToEgo(prestige + 1) <= ego) {
            ego = 0;
            prestige++;
            onPrestigeLevelUp();
            return true;
        }
        return false;
    }

    protected boolean isMaxPrestige() {
        return prestige == MAX_PRESTIGE_LEVEL;
    }

    private void onPrestigeLevelUp() {
        // TODO: implement
        switch (prestige) { // new prestige level
            case 1:
                combatModule = new CombatModule();
                break;
            case 2:
                // TODO: implement, farming perhaps?
                break;
        }
    }

    protected void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
    }

    // GETTERS
    protected long getLastPrideCollectionTime() {
        return lastPrideCollectionTime;
    }

    protected ArrayList<Artifact> getArtifacts() {
        return artifacts;
    }

    protected ArrayList<Artifact> getSortedArtifacts() {
        ArrayList<Artifact> sortedArtifacts = new ArrayList<>(artifacts);
        Collections.sort(sortedArtifacts);
        return sortedArtifacts;
    }

    protected long getPride() {
        return pride;
    }

    protected long getEgo() {
        return ego;
    }

    protected int getPrestige() {
        return prestige;
    }

    protected boolean hasCombatModule() {
        return combatModule != null;
    }

    protected CombatStats getCombatStats() {
        return combatModule.getCombatStats();
    }

    @Override
    public int compareTo(Player player) {
        int prestigeComparison = Math.min(Math.max(prestige - player.getPrestige(), -1), 1);
        int egoComparison = (int) Math.min(Math.max(ego - player.getEgo(), -1), 1);
        int prideComparison = (int) Math.min(Math.max(pride - player.getPride(), -1), 1);
        if (prestigeComparison == 0) {
            if (egoComparison == 0) {
                return prideComparison;
            }
            return egoComparison;
        }
        return prestigeComparison;
    }
}
