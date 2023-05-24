package com.gngsn.chapter5.v1;

import com.gngsn.chapter5.v1.discount.DiscountCondition;

import java.time.Duration;
import java.util.List;

/**
 * v1. 영화
 */
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;

    public Money getFee() {
        return fee;
    }

    /**
     * Message: 가격을 계산하라
     * From: Screening Entity
     */
    public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }

        return fee;
    }

    private boolean isDiscountable(Screening screening) {
        return discountConditions.stream()
                .anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    public Money calculateDiscountAmount() {
        switch (movieType) {
            case AMOUNT_DISCOUNT -> calculateAmountDiscountFee();
            case PERCENT_DISCOUNT -> calculatePercentDiscountFee();
            case NONE_DISCOUNT -> calculateNoneDiscountFee();
        }

        throw new IllegalStateException();
    }

    public Money calculateAmountDiscountFee() {
        return discountAmount;
    }

    public Money calculatePercentDiscountFee() {
        return fee.times(discountPercent);
    }

    public Money calculateNoneDiscountFee() {
        return Money.ZERO;
    }

    public List<DiscountCondition> getDiscountConditions() {
        return discountConditions;
    }

    public MovieType getMovieType() {
        return movieType;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
