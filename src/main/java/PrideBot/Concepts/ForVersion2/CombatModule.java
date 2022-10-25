package PrideBot.Concepts.ForVersion2;

public class CombatModule {
    private long timeOfLastAttack;
    private CombatStats combatStats;

    protected CombatModule() {

    }

    protected CombatModule(long timeOfLastAttack, CombatStats combatStats) {
        this.timeOfLastAttack = timeOfLastAttack;
        this.combatStats = combatStats;
    }

    protected boolean isOffCooldown() {
        return (double) (System.currentTimeMillis() - timeOfLastAttack) / 1000 / 60 / 60 / 24 >= 1;
    }

    protected void updateCooldown() {
        timeOfLastAttack = System.currentTimeMillis();
    }

    protected long getTimeOfLastAttack() {
        return timeOfLastAttack;
    }

    protected CombatStats getCombatStats() {
        return combatStats;
    }
}
