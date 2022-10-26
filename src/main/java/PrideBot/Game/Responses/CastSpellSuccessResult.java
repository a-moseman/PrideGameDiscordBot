package PrideBot.Game.Responses;

public class CastSpellSuccessResult extends CastSpellResult {
    public final int REMAINING_SPELL_USES;

    public CastSpellSuccessResult(String targetPlayerName, String spell, int spellRank, int remainingSpellUses) {
        super(targetPlayerName, spell, spellRank);
        this.REMAINING_SPELL_USES = remainingSpellUses;
    }
}
