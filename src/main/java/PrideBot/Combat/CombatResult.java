package PrideBot.Combat;

public class CombatResult {
    public final long AMOUNT_EXCHANGED;
    public final String RESOURCE_EXCHANGED;
    public final long ATTACK_ROLL;
    public final long DEFENSE_ROLL;

    public CombatResult(long amountExchanged, String resourceExchanged, long attackRoll, long defenseRoll) {
        this.AMOUNT_EXCHANGED = amountExchanged;
        this.RESOURCE_EXCHANGED = resourceExchanged;
        this.ATTACK_ROLL = attackRoll;
        this.DEFENSE_ROLL = defenseRoll;
    }
}
