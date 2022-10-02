package Game;


public class CombatSimulator {
    protected static long simulate (Player attacker, Player defender) {
        /*
        Combat is initiated by a player, who is referred to as the attack, against another player, who is referred to as the defender.
        Procedure:
            1. Attacker rolls their attack dice.
            2. Defender rolls their defense dice.
            3. The attack roll is subtracted by the defense roll to get the result.
            4. If the result is positive, then that value is subtracted from the defender's pride and added to the attacker's pride.
            5. If the result is negative, no pride is exchanged.
            Note: features will be implemented that may change this procedure in the future.
         */
        Dice attackerAttackDice = attacker.getCombatStats().getAttackDice();
        Dice defenderDefenseDice = defender.getCombatStats().getDefenseDice();
        long attackRoll = attackerAttackDice.roll();
        long defenseRoll = defenderDefenseDice.roll();
        long result = attackRoll - defenseRoll;
        return Math.max(0, result);
    }
}
