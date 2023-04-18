package com.gngsn.chapter2.v2.discountPolicy;

import com.gngsn.chapter2.v2.Screening;
import com.gngsn.chapter2.v2.Money;
import com.gngsn.chapter2.v2.discountCondition.DiscountCondition;

/**
 * Version1. 금액 할인 정책
 */
public class AmountDiscountPolicy extends DefaultDiscountPolicy {
    private Money discountAmount;

    public AmountDiscountPolicy(Money discountAmount, DiscountCondition... conditions) {
        super();
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return discountAmount;
    }
}
