package PrideBot.Combat;

public class Damage {
    public final double DAMAGE;
    public final double SHIELD_PENETRATION; // damage does more to shield by this amount

    public Damage(double damage, double shieldPenetration) {
        this.DAMAGE = damage;
        this.SHIELD_PENETRATION = shieldPenetration;
    }
}
