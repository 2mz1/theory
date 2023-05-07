package entity;

import lombok.ToString;

import java.math.BigDecimal;

@ToString
public class Money {

    public static final Money ZERO = Money.wons(0);

    private final BigDecimal amount;

    Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }


    public Money plus(int amount) {
        return new Money(this.amount.add(BigDecimal.valueOf(amount)));
    }

    public Money times(double audienceCount) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(audienceCount)));
    }

    public Money minus(Money amount) {
        return new Money(this.amount.subtract(amount.amount));
    }
}
