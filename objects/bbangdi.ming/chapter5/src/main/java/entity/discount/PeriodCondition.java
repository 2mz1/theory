package entity.discount;

import entity.Screening;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class PeriodCondition implements DiscountCondition {
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;

	private String max = "a";

	public PeriodCondition(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public boolean isSatisfiedBy(Screening screening) {
		return screening.getWhenScreened().getDayOfWeek().equals(dayOfWeek) &&
				startTime.compareTo(screening.getWhenScreened().toLocalTime()) <= 0 &&
				endTime.compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
	}

	public String getMax() {
		return max;
	}
}
