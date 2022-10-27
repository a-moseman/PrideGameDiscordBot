package PrideBot.Game.Results;

public class BuySuccessResult extends BuyResult {
    public long SPENT_AMOUNT;
    public String GAINED_ITEM;

    public BuySuccessResult(String targetItem, Currency currency, long spentAmount) {
        super(targetItem, currency);
        this.SPENT_AMOUNT = spentAmount;
        this.GAINED_ITEM = targetItem;
    }

    public BuySuccessResult(String targetItem, Currency currency, long spentAmount, String gainedItem) {
        super(targetItem, currency);
        this.SPENT_AMOUNT = spentAmount;
        this.GAINED_ITEM = gainedItem;
    }
}
