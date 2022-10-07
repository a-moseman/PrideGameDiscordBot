package Game;

/**
 * Finished for version 1.
 */
public class Player {
    private PlayerStats stats;
    private long lastCollectionTime;

    protected Player() {
        this.stats = new PlayerStats();
        this.lastCollectionTime = System.currentTimeMillis();
    }

    protected Player(PlayerStats playerStats, long lastCollectionTime) {
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
        if (stats.getPride() == stats.getShame()) {
            return 0;
        }
        if (stats.getPride() > stats.getShame()) {
            return 1;
        }
        else {
            return -1;
        }
    }

    protected PlayerStats getStats() {
        return stats;
    }

    protected long getLastCollectionTime() {
        return lastCollectionTime;
    }
}
