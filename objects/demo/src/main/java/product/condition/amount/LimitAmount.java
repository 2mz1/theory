package product.condition.amount;

/**
 * 특정 옵션 섹션의 옵션 선택 개수 제한
 */
public abstract class LimitAmount {
    protected int amount;

    public LimitAmount(int amount) {
        this.amount = amount;
    }

    public abstract boolean isSatisfied(int selectedAmount);
}
