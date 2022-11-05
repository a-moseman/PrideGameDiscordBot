package PrideBot.Combat;

import PrideBot.Game.Player;

import java.util.Random;

public class CombatSimulator {
    private static final Random RANDOM = new Random();

    public static CombatResult simulate(Player attacker, Player defender) {
        Dice attackDice = attacker.getCombatModule().getAttackDice();
        Dice defenseDice = defender.getCombatModule().getDefenseDice();

        long attackRoll = attackDice.roll();
        long defenseRoll = defenseDice.roll();

        // TODO: implement skill effects and such
        CombatEffect attackerCombatEffects = attacker.getCombatModule().getCurrentEffects();
        CombatEffect defenderCombatEffects = defender.getCombatModule().getCurrentEffects();

        double damage = (double) (defenseRoll - attackRoll);
        double healthLost = defender.getCombatModule().hurt(new Damage(damage, attackerCombatEffects.SHIELD_PENETRATION));
        // apply lifesteal
        attacker.getCombatModule().heal(attacker.getCombatModule().getCurrentEffects().LIFESTEAL * healthLost);
        long tierZeroToExchange = convertHealthToTierZero(healthLost);
        long amountExchanged;
        boolean type;
        if (defender.getStats().getPride() > defender.getStats().getShame()) {
            amountExchanged = exchangePride(attacker, defender, tierZeroToExchange);
            type = true;
        }
        else if (defender.getStats().getPride() < defender.getStats().getShame()) {
            amountExchanged = exchangeShame(attacker, defender, tierZeroToExchange);
            type = false;
        }
        else {
            if (RANDOM.nextBoolean()) {
                amountExchanged = exchangePride(attacker, defender, tierZeroToExchange);
                type = true;
            }
            else {
                amountExchanged = exchangeShame(attacker, defender, tierZeroToExchange);
                type = false;
            }
        }

        return new CombatResult(
                amountExchanged,
                type ? "Pride" : "Shame",
                attackRoll,
                defenseRoll,
                healthLost,
                0
        );
    }

    private static long convertHealthToTierZero(double health) {
        return 0; // TODO: implement
    }

    private static long exchangePride(Player attacker, Player defender, long targetAmount) {
        long amount = Math.min(defender.getStats().getPride(), targetAmount);
        defender.getStats().removePride(amount);
        attacker.getStats().addPride(amount);
        return amount;
    }

    private static long exchangeShame(Player attacker, Player defender, long targetAmount) {
        long amount = Math.min(defender.getStats().getShame(), targetAmount);
        defender.getStats().removeShame(amount);
        attacker.getStats().addShame(amount);
        return amount;
    }
}

