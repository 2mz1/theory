package entity.discount.policy;

import entity.Money;
import entity.Screening;
import entity.discount.condition.DiscountCondition;

import java.util.Arrays;
import java.util.List;

public interface DiscountPolicy {
    Money calculateDiscountAmount(Screening screening);
}
