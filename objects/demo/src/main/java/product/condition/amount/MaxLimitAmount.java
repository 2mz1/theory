package product.condition.amount;

/**
 * 특정 옵션 섹션의 최대 옵션 선택 개수 제한
 */
public class MaxLimitAmount extends LimitAmount {
    public MaxLimitAmount(int amount) {
        super(amount);
    }

    public boolean isSatisfied(int selectedAmount) {
        return amount >= selectedAmount;
    }
}
