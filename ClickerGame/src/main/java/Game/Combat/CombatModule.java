package Game.Combat;

public class CombatModule {
    private long timeOfLastAttack;
    private CombatStats combatStats;

    public CombatModule() {

    }

    public CombatModule(long timeOfLastAttack, CombatStats combatStats) {
        this.timeOfLastAttack = timeOfLastAttack;
        this.combatStats = combatStats;
    }

    public boolean isOffCooldown() {
        return (double) (System.currentTimeMillis() - timeOfLastAttack) / 1000 / 60 / 60 / 24 >= 1;
    }

    public void updateCooldown() {
        timeOfLastAttack = System.currentTimeMillis();
    }

    public long getTimeOfLastAttack() {
        return timeOfLastAttack;
    }

    public CombatStats getCombatStats() {
        return combatStats;
    }
}
