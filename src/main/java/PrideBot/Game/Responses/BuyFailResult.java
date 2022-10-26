package PrideBot.Game.Responses;

public class BuyFailResult extends BuyResult {
    public final long MISSING_CURRENCY;

    public BuyFailResult(String targetPlayerName, String targetItem, Currency currency, long missingCurrency) {
        super(targetPlayerName, targetItem, currency);
        this.MISSING_CURRENCY = missingCurrency;
    }
}
