package PrideBot.Game.Responses;

public class CastSpellSuccessResponse extends CastSpellResponse {
    public final int REMAINING_SPELL_USES;

    public CastSpellSuccessResponse(String targetPlayerName, String spell, int spellRank, int remainingSpellUses) {
        super(targetPlayerName, spell, spellRank);
        this.REMAINING_SPELL_USES = remainingSpellUses;
    }
}
