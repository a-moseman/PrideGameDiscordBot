package PrideBot.Concepts.ForVersion2;

import PrideBot.Combat.Dice;

import java.util.ArrayList;

public class CombatStats {
    private Dice attackDice;
    private Dice defenseDice;
    private ArrayList<CombatItem> combatItems;

    protected CombatStats() {
        this.attackDice = Dice.D6;
        this.defenseDice = Dice.D6;
        this.combatItems = new ArrayList<>();
    }

    protected CombatStats(Dice attackDice, Dice defenseDice, ArrayList<CombatItem> combatItems) {
        this.attackDice = attackDice;
        this.defenseDice = defenseDice;
        this.combatItems = combatItems;
    }

    protected void addAttackDice(Dice addition) {
        attackDice = attackDice.add(addition);
    }

    protected void addDefenseDice(Dice addition) {
        defenseDice = defenseDice.add(addition);
    }

    protected Dice getAttackDice() {
        return attackDice;
    }

    protected Dice getDefenseDice() {
        return defenseDice;
    }

    protected ArrayList<CombatItem> getItems() {
        return combatItems;
    }
}
