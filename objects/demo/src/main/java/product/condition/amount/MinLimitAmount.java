package product.condition.amount;

/**
 * 특정 옵션 섹션의 최소 옵션 선택 개수 제한
 */
public class MinLimitAmount extends LimitAmount {

    public MinLimitAmount(int amount) {
        super(amount);
    }

    public boolean isSatisfied(int selectedAmount) {
        return amount <= selectedAmount;
    }
}
