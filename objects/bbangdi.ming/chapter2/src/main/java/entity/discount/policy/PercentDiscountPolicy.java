package entity.discount.policy;

import entity.Money;
import entity.Screening;
import entity.discount.condition.DiscountCondition;

public class PercentDiscountPolicy extends DiscountPolicy {
    public PercentDiscountPolicy(DiscountCondition... conditions) {
        super(conditions);
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return null;
    }
}
