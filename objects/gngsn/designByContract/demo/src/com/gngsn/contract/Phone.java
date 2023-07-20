package com.gngsn.contract;

import com.gngsn.contract.policy.RatePolicy;

import java.util.ArrayList;
import java.util.List;

public class Phone {

    private RatePolicy ratePolicy;
    private List<Call> calls = new ArrayList<>();

    public Phone(RatePolicy ratePolicy) {
        this.ratePolicy = ratePolicy;
    }

    public void call(Call call) {
        calls.add(call);
    }

    public Bill publishBill() {
        // 청구 서의 요금은 최소한 0 원보다 크거나 같아야 하므로 calculateFee 의 반환값은 0 원보다 커야 한다
        return new Bill(this, ratePolicy.calculateFee(calls));
    }
}