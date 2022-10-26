package PrideBot.Game.Responses;

public class BuySuccessResult extends BuyResult {
    public long SPENT_AMOUNT;
    public String GAINED_ITEM;

    public BuySuccessResult(String targetPlayerName, String targetItem, Currency currency, long spentAmount) {
        super(targetPlayerName, targetItem, currency);
        this.SPENT_AMOUNT = spentAmount;
        this.GAINED_ITEM = targetItem;
    }

    public BuySuccessResult(String targetPlayerName, String targetItem, Currency currency, long spentAmount, String gainedItem) {
        super(targetPlayerName, targetItem, currency);
        this.SPENT_AMOUNT = spentAmount;
        this.GAINED_ITEM = gainedItem;
    }
}
