package Game.Combat;

import java.util.ArrayList;

public class CombatStats {
    private Dice attackDice;
    private Dice defenseDice;
    private ArrayList<Item> items;

    public CombatStats() {
        this.attackDice = Dice.D6;
        this.defenseDice = Dice.D6;
        this.items = new ArrayList<>();
    }

    public CombatStats(Dice attackDice, Dice defenseDice, ArrayList<Item> items) {
        this.attackDice = attackDice;
        this.defenseDice = defenseDice;
        this.items = items;
    }

    public void addAttackDice(Dice addition) {
        attackDice = attackDice.add(addition);
    }

    public void addDefenseDice(Dice addition) {
        defenseDice = defenseDice.add(addition);
    }

    public Dice getAttackDice() {
        return attackDice;
    }

    public Dice getDefenseDice() {
        return defenseDice;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
