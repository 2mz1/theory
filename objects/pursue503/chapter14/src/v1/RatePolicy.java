package v1;

import common.Money;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}
