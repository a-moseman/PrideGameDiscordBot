package PrideBot.Game.Responses;

public class CastSpellResponse extends Response {
    public final String SPELL;
    public final int SPELL_RANK;

    public CastSpellResponse(String targetPlayerName, String spell, int spellRank) {
        super(targetPlayerName);
        this.SPELL = spell;
        this.SPELL_RANK = spellRank;
    }
}
