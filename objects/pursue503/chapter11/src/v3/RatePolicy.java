package v3;

import common.Money;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}
