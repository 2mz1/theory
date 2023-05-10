package entity.discount;

import entity.Screening;

public interface DiscountCondition {
	String max = "bbbbb";
	boolean isSatisfiedBy(Screening screening);

	default void test() {
		System.out.println("test");
	}
}
