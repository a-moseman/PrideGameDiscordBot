package PrideBot.Game.Responses;

public class BuyFailResponse extends BuyResponse {
    public final long MISSING_CURRENCY;

    public BuyFailResponse(String targetPlayerName, String targetItem, Currency currency, long missingCurrency) {
        super(targetPlayerName, targetItem, currency);
        this.MISSING_CURRENCY = missingCurrency;
    }
}
