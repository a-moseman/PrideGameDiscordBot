package PrideBot.Game.Responses;

public class CastSpellFailResponse extends CastSpellResponse {
    public final double REMAINING_COOLDOWN; // in days

    public CastSpellFailResponse(String targetPlayerName, String spell, int spellRank, double remainingCooldown) {
        super(targetPlayerName, spell, spellRank);
        this.REMAINING_COOLDOWN = remainingCooldown;
    }
}
