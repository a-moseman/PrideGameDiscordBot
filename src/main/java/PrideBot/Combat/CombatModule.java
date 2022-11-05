package PrideBot.Combat;

public class CombatModule {
    private CombatStats combatStats;
    private CombatDice combatDice;

    public CombatModule(CombatStats combatStats, CombatDice combatDice) {
        this.combatStats = combatStats;
        this.combatDice = combatDice;
    }

    public double hurt(Damage damage) {
        return combatStats.hurt(damage);
    }

    public void heal(double amount) {

    }

    public Dice getAttackDice() {
        return combatDice.getAttackDice();
    }

    public Dice getDefenseDice() {
        return combatDice.getDefenseDice();
    }

    public CombatStats.Stats getStats() {
        return combatStats.getStats();
    }

    public CombatEffect getCurrentEffects() {
        // TODO: implement
        return null;
    }
}
