package product;

import product.option.group.OptionGroup;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product Entity
 */
public class Product {
    private final String name;
    private final String desc;
    private final List<OptionGroup> optionGroups;
    private final BigDecimal price;

    public Product(String name, String desc, BigDecimal price, List<OptionGroup> options) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.optionGroups = options;
    }

    public BigDecimal getTotalCost() {
        BigDecimal total = price;

        for (OptionGroup optionGroup: optionGroups) {
            total = total.add(optionGroup.getTotalCost());
        }

        return total;
    }

    public String getName() {
        return name;
    }
}

