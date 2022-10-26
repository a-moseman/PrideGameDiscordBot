package PrideBot.Game.Responses;

public class BuyResult extends Result {
    public final String TARGET_ITEM;
    public final Currency CURRENCY;

    public BuyResult(String targetPlayerName, String targetItem, Currency currency) {
        super(targetPlayerName);
        this.TARGET_ITEM = targetItem;
        this.CURRENCY = currency;
    }
}
