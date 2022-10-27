package PrideBot.Game.Results;

public class CastSpellFailResult extends CastSpellResult {
    public final double REMAINING_COOLDOWN; // in days

    public CastSpellFailResult(String spell, int spellRank, double remainingCooldown) {
        super(spell, spellRank);
        this.REMAINING_COOLDOWN = remainingCooldown;
    }
}
