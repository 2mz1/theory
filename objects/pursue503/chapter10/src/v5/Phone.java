package v5;

import v1.Call;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Phone extends AbstractPhone {
    private Money amount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    private double taxRate;


    public Phone(Money amount, Duration seconds, List<Call> calls, double taxRate) {
        this.amount = amount;
        this.seconds = seconds;
        this.calls = calls;
        this.taxRate = taxRate;
    }

    public Money getAmount() {
        return amount;
    }

    public Duration getSeconds() {
        return seconds;
    }

    public List<Call> getCalls() {
        return calls;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }

}
