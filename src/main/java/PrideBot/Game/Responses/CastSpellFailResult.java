package PrideBot.Game.Responses;

public class CastSpellFailResult extends CastSpellResult {
    public final double REMAINING_COOLDOWN; // in days

    public CastSpellFailResult(String targetPlayerName, String spell, int spellRank, double remainingCooldown) {
        super(targetPlayerName, spell, spellRank);
        this.REMAINING_COOLDOWN = remainingCooldown;
    }
}
