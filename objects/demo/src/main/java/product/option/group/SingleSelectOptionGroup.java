package product.option.group;

import product.option.Option;

import java.math.BigDecimal;

/**
 * 단일 옵션을 선택하는 옵션 그룹
 */
public class SingleSelectOptionGroup implements OptionGroup {
    private final String name;
    private Option option;

    public SingleSelectOptionGroup(String name, Option option) {
        this.name = name;
        this.option = option;
    }

    @Override
    public BigDecimal getTotalCost() {
        return option.getCost();
    }

    public void changeOption(Option option) {
        this.option = option;
    }

    public String getName() {
        return name;
    }

    public Option getOptions() {
        return option;
    }
}
