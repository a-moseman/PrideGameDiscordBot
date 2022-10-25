package PrideBot.Game.Responses;

public class BuyResponse extends Response {
    public final String TARGET_ITEM;
    public final Currency CURRENCY;

    public BuyResponse(String targetPlayerName, String targetItem, Currency currency) {
        super(targetPlayerName);
        this.TARGET_ITEM = targetItem;
        this.CURRENCY = currency;
    }
}
