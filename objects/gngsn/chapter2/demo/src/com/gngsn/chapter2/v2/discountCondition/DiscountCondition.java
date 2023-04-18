package com.gngsn.chapter2.v2.discountCondition;

import com.gngsn.chapter2.v2.Screening;

public interface DiscountCondition {

    boolean isSatisfiedBy(Screening screening);
}
