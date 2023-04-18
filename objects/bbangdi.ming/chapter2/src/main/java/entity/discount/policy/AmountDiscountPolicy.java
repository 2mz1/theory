package entity.discount.policy;

import entity.Money;
import entity.Screening;
import entity.discount.condition.DiscountCondition;

public class AmountDiscountPolicy extends DiscountPolicy {
    public AmountDiscountPolicy(DiscountCondition... conditions) {
        super(conditions);
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return null;
    }
}
