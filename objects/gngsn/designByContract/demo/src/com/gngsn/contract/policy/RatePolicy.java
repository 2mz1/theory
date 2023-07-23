package com.gngsn.contract.policy;

import com.gngsn.contract.Call;
import com.gngsn.contract.Money;

import java.util.List;

public interface RatePolicy {
    Money calculateFee(List<Call> calls);
}