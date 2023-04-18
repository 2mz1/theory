package com.gngsn.chapter2.v1.discountPolicy;

import com.gngsn.chapter2.v1.discountCondition.DiscountCondition;
import com.gngsn.chapter2.v1.Money;
import com.gngsn.chapter2.v1.Screening;

/**
 * Version1. 비율 할인 정책
 */
public class PercentDiscountPolicy extends DiscountPolicy {
    private double percent;

    public PercentDiscountPolicy(double percent, DiscountCondition... conditions) {
        super(conditions);
        this.percent = percent;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return screening.getMovieFee().times(percent);
    }
}
