package PrideBot.Combat;

public class CombatDice {
    private Dice attackDice;
    private Dice defenseDice;

    public CombatDice(Dice attackDice, Dice defenseDice) {
        this.attackDice = attackDice;
        this.defenseDice = defenseDice;
    }

    public void addAttackDice(Dice dice) {
        attackDice = attackDice.add(dice);
    }

    public void addDefenseDice(Dice dice) {
        defenseDice = defenseDice.add(dice);
    }

    public Dice getAttackDice() {
        return attackDice;
    }

    public Dice getDefenseDice() {
        return defenseDice;
    }
}
