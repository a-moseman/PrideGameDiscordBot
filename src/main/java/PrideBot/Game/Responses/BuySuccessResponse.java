package PrideBot.Game.Responses;

public class BuySuccessResponse extends BuyResponse{
    public long SPENT_AMOUNT;
    public String GAINED_ITEM;

    public BuySuccessResponse(String targetPlayerName, String targetItem, Currency currency, long spentAmount) {
        super(targetPlayerName, targetItem, currency);
        this.SPENT_AMOUNT = spentAmount;
        this.GAINED_ITEM = targetItem;
    }

    public BuySuccessResponse(String targetPlayerName, String targetItem, Currency currency, long spentAmount, String gainedItem) {
        super(targetPlayerName, targetItem, currency);
        this.SPENT_AMOUNT = spentAmount;
        this.GAINED_ITEM = gainedItem;
    }
}
