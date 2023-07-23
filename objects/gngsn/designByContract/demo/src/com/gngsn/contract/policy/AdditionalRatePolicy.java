package com.gngsn.contract.policy;

import com.gngsn.contract.Call;
import com.gngsn.contract.Money;

import java.util.List;

public abstract class AdditionalRatePolicy implements RatePolicy {
    private RatePolicy next;

    public AdditionalRatePolicy(RatePolicy next) {
        this.next = next;
    }

    @Override
    public Money calculateFee(List<Call> calls) {
        // 사전조건 (사전조건의 책임은 클라이언트인 Phone에 있다)
        assert calls != null;

        Money fee = next.calculateFee(calls);
        Money result = afterCalculated(fee);

        // 사후조건
        assert result.isGreaterThanOrEqual(Money.ZERO);
        return result;
    }

    abstract protected Money afterCalculated(Money fee);
}