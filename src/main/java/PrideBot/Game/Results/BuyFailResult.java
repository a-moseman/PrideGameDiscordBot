package PrideBot.Game.Results;

public class BuyFailResult extends BuyResult {
    public final long MISSING_CURRENCY;

    public BuyFailResult(String targetItem, Currency currency, long missingCurrency) {
        super(targetItem, currency);
        this.MISSING_CURRENCY = missingCurrency;
    }
}
