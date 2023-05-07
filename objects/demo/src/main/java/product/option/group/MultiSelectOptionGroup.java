package product.option.group;

import exception.InvalidLimitAmountException;
import product.condition.amount.LimitAmount;
import product.option.Option;

import java.math.BigDecimal;
import java.util.List;

/**
 * 여러 개의 옵션을 추가할 수 있는 옵션 그룹
 */
public class MultiSelectOptionGroup implements OptionGroup {
    private final String name;
    private final LimitAmount limitAmount;
    private final List<Option> options;

    public MultiSelectOptionGroup(String name, LimitAmount limitAmount, List<Option> options) {
        if (notValidAmountCondition()) {
            throw new InvalidLimitAmountException("cannot add option by limit condition");
        }

        this.name = name;
        this.limitAmount = limitAmount;
        this.options = options;
    }

    @Override
    public BigDecimal getTotalCost() {
        BigDecimal total = BigDecimal.ZERO;

        for (Option option: options) {
            total = total.add(option.getCost());
        }

        return total;
    }

    private boolean notValidAmountCondition() {
        return !limitAmount.isSatisfied(options.size());
    }

    public String getName() {
        return name;
    }

    public List<Option> getOptions() {
        return options;
    }
}
