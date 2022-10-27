package PrideBot.Game.Responses;

public class CastSpellResult extends Result {
    public final String SPELL;
    public final int SPELL_RANK;

    public CastSpellResult(String spell, int spellRank) {
        this.SPELL = spell;
        this.SPELL_RANK = spellRank;
    }
}
