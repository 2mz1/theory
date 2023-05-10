package entity.discount;

import entity.Screening;

public interface DiscountCondition {
	boolean isSatisfiedBy(Screening screening);
}
