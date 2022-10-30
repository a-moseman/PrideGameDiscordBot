package PrideBot.Combat;

public class CombatModule {
    private CombatStats combatStats;
    private CombatDice combatDice;

    public CombatModule(CombatStats combatStats, CombatDice combatDice) {
        this.combatStats = combatStats;
        this.combatDice = combatDice;
    }

    public double hurt(double damage) {
        return combatStats.hurt(damage);
    }

    public Dice getAttackDice() {
        return combatDice.getAttackDice();
    }

    public Dice getDefenseDice() {
        return combatDice.getDefenseDice();
    }
}
