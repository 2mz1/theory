package v2;

import common.Money;
import v1.Phone;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}
