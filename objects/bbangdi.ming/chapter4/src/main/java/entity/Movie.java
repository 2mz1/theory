package entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class Movie {
    private String title;
    private Duration runningTime;

    @Getter
    @Setter
    private Money fee;

    @Setter
    private List<DiscountCondition> discountConditions;

    @Getter
    @Setter
    private MovieType movieType;

    @Getter
    @Setter
    private Money discountAmount;

    @Getter
    @Setter
    private double discountPercent;

    public List<DiscountCondition> getDiscountConditions() {
        return Collections.unmodifiableList(discountConditions);
    }
}
