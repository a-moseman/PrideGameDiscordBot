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
     * Updates combat stats. Should be done everytime the stats are relevant.
     */
    public void update() {
        long now = System.currentTimeMillis();
        long millis = now - lastUpdate;
        long hours = millis / 1000 / 60/ 60;
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

    public double hurt(double damage) {
        update();
        double damageToHealth = Math.min(damage - shield, health);
        shield -= damage;
        if (shield < 0) {
            health += shield;
            shield = 0;
            if (health < 0) {
                health = 0;
            }
        }
        return damageToHealth;
    }

    public double getHealth() {
        update();
        return health;
    }

    public double getShield() {
        update();
        return shield;
    }
}
