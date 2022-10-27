package PrideBot.Game.Responses;

public class BuyResult extends Result {
    public final String TARGET_ITEM;
    public final Currency CURRENCY;

    public BuyResult(String targetItem, Currency currency) {
        this.TARGET_ITEM = targetItem;
        this.CURRENCY = currency;
    }
}
