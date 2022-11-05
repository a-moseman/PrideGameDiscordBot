package PrideBot.Combat;

public class CombatEffect {
    public final double SHIELD_PENETRATION;
    public final double LIFESTEAL;

    public CombatEffect(
            double shieldPenetration,
            double lifesteal
    ) {
        this.SHIELD_PENETRATION = shieldPenetration;
        this.LIFESTEAL = lifesteal;
    }

    public CombatEffect add(CombatEffect other) {
        return new CombatEffect(
                SHIELD_PENETRATION + other.SHIELD_PENETRATION,
                LIFESTEAL + other.LIFESTEAL
        );
    }
}
