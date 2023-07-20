package com.gngsn.contract.policy;

import com.gngsn.contract.Call;
import com.gngsn.contract.Money;

import java.util.List;

public abstract class BasicRatePolicy implements RatePolicy {

    @Override
    public Money calculateFee(List<Call> calls) {
        // 사전조건 (사전조건의 책임은 클라이언트인 Phone에 있다)
        assert calls != null;

        Money result = Money.ZERO;
        for(Call call : calls) {
            result.plus(calculateCallFee(call));
        }

        // 사후조건
        assert result.isGreaterThanOrEqual(Money.ZERO);
        return result;
    }

    protected abstract Money calculateCallFee(Call call);
}
