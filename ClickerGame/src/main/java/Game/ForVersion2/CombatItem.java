package Game.ForVersion2;

public class CombatItem {
    private Dice attackDice;
    private Dice defenseDice;

    protected CombatItem(Dice attackDice, Dice defenseDice) {
        this.attackDice = attackDice;
        this.defenseDice = defenseDice;
    }

    protected Dice getAttackDice() {
        return attackDice;
    }

    protected Dice getDefenseDice() {
        return defenseDice;
    }
}
