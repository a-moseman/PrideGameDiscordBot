package PrideBot.Game.Results;

public class CastSpellSuccessResult extends CastSpellResult {
    public final int REMAINING_SPELL_USES;

    public CastSpellSuccessResult(String spell, int spellRank, int remainingSpellUses) {
        super(spell, spellRank);
        this.REMAINING_SPELL_USES = remainingSpellUses;
    }
}
