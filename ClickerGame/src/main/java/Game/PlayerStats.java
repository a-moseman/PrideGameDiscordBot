package Game;

/**
 * Finished for version 1.
 */
public class PlayerStats {
    private long pride;
    private long ego;
    private long honor;
    private long shame;
    private long guilt;
    private long dishonor;

    private long costOfNextEgo() {
        return (ego + 1) * 10;
    }

    private long costOfNextGuilt() {
        return (shame + 1) * 10;
    }

    private long costOfNextHonor() {
        return (honor + 1) * 10;
    }

    private long costOfNextDishonor() {
        return (dishonor + 1) * 10;
    }

    protected PlayerStats() {
        this.pride = 0;
        this.ego = 0;
        this.honor = 0;
        this.shame = 0;
        this.guilt = 0;
        this.dishonor = 0;
    }

    protected PlayerStats(long pride, long ego, long honor, long shame, long guilt, long dishonor) {
        this.pride = pride;
        this.ego = ego;
        this.honor = honor;
        this.shame = shame;
        this.guilt = guilt;
        this.dishonor = dishonor;
    }

    protected void addPride(long amount) {
        assert amount >= 0;
        pride += amount;
    }

    protected boolean buyEgo() {
        if (costOfNextEgo() <= pride) {
            pride -= costOfNextEgo();
            ego++;
            return true;
        }
        return false;
    }

    protected boolean buyHonor() {
        if (costOfNextHonor() <= ego) {
            ego = 0;
            honor++;
            return true;
        }
        return false;
    }

    protected void addShame(long amount) {
        assert amount >= 0;
        shame += amount;
    }

    protected boolean buyGuilt() {
        long cost = costOfNextGuilt();
        if (cost <= shame) {
            shame -= cost;
            guilt++;
            return true;
        }
        return false;
    }

    protected boolean buyDishonor() {
        if (costOfNextDishonor() <= guilt) {
            guilt = 0;
            dishonor++;
            return true;
        }
        return false;
    }

    protected long getLevel() {
        return Math.max(honor, dishonor);
    }

    protected long getPride() {
        return pride;
    }

    protected long getEgo() {
        return ego;
    }

    protected long getHonor() {
        return honor;
    }

    protected long getShame() {
        return shame;
    }

    protected long getGuilt() {
        return guilt;
    }

    protected long getDishonor() {
        return dishonor;
    }
}
