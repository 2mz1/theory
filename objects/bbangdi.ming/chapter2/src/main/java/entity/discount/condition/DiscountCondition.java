package entity.discount.condition;

import entity.Screening;

public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
