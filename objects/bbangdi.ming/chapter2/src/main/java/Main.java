import entity.Money;
import entity.Movie;
import entity.discount.condition.PeriodCondition;
import entity.discount.condition.SequenceCondition;
import entity.discount.policy.AmountDiscountPolicy;
import entity.discount.policy.NoneDiscountPolicy;
import entity.discount.policy.PercentDiscountPolicy;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public class Main {

	public static void main(String[] args) {
		Movie avatar = new Movie("Avatar", Duration.ofMinutes(120), Money.wons(10_000), new AmountDiscountPolicy(
				Money.wons(800),
				new SequenceCondition(1),
				new SequenceCondition(10),
				new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59)),
				new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(20, 59))
		));
		avatar.changeDiscountPolicy(new PercentDiscountPolicy(0.1));

		Movie titanic = new Movie("Titanic", Duration.ofMinutes(180), Money.wons(15_000), new PercentDiscountPolicy(
			0.1,
			new PeriodCondition(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 59)),
			new SequenceCondition(2),
			new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(13, 59))
		));

		Movie starWars = new Movie("Star Wars", Duration.ofMinutes(210), Money.wons(20_000), new NoneDiscountPolicy());
	}
}
