package Game.Combat;

public class Item {
    private Dice attackDice;
    private Dice defenseDice;

    public Item(){

    }

    public Item(Dice attackDice, Dice defenseDice) {
        this.attackDice = attackDice;
        this.defenseDice = defenseDice;
    }

    public Dice getAttackDice() {
        return attackDice;
    }

    public Dice getDefenseDice() {
        return defenseDice;
    }
}
