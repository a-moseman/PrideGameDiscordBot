package PrideBot.Combat;

public class CombatStats {
    private double health;
    private double shield;
    private double healthRegen; // per hour
    private double shieldRegen; // per hour
    private double maxHealth;
    private double maxShield;
    private long lastUpdate; // time of last update in milliseconds

    /**
     * Constructor used when module unlocked.
     */
    public CombatStats() {
        this.maxHealth = 100;
        this.maxShield = 0;
        this.health = maxHealth;
        this.shield = maxShield;
        this.healthRegen = 1;
        this.shieldRegen = 1;
    }

    /**
     * Constructor used when loading from persistent data.
     */
    public CombatStats(double health, double maxHealth, double healthRegen, double shield, double maxShield, double shieldRegen) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.healthRegen = healthRegen;
        this.shield = shield;
        this.maxShield = maxShield;
        this.shieldRegen = shieldRegen;
    }

    /**
     * Updates combat stats. Should be done everytime the stats are relevant.
     */
    public void update() {
        // TODO: notice, this approach likely results in floating-point errors impacting regen rates
        long now = System.currentTimeMillis();
        long millis = now - lastUpdate;
        double hours = (double) millis / 1000 / 60/ 60;
        health += healthRegen * hours;
        shield += shieldRegen * hours;
        if (health > maxHealth) {
            health = maxHealth;
        }
        if (shield > maxShield) {
            shield = maxShield;
        }
        lastUpdate = now;
    }

    public double hurt(Damage damage) {
        // TODO: test
        update();
        double dmg = damage.DAMAGE;
        if (dmg * damage.SHIELD_PENETRATION <= shield) {
            shield -= dmg * damage.SHIELD_PENETRATION;
            return 0;
        }
        dmg -= shield / damage.SHIELD_PENETRATION;
        shield = 0;
        health -= dmg;
        if (health < 0) {
            health = 0;
        }
        return dmg;
    }

    public void heal(double amount) {
        update();
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public Stats getStats() {
        update();
        return new Stats(
                health,
                maxHealth,
                healthRegen,
                shield,
                maxShield,
                shieldRegen
        );
    }

    public class Stats {
        public final double HEALTH;
        public final double MAX_HEALTH;
        public final double HEALTH_REGEN;
        public final double SHIELD;
        public final double MAX_SHIELD;
        public final double SHIELD_REGEN;

        public Stats(double health, double maxHealth, double healthRegen, double shield, double maxShield, double shieldRegen) {
            this.HEALTH = health;
            this.MAX_HEALTH = maxHealth;
            this.HEALTH_REGEN = healthRegen;
            this.SHIELD = shield;
            this.MAX_SHIELD = maxShield;
            this.SHIELD_REGEN = shieldRegen;
        }
    }
}
