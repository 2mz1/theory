package product.option;

import java.math.BigDecimal;

/**
 * Option Entity
 */
public class Option {
    private final String name;
    private final BigDecimal cost;

    public Option(String name, BigDecimal cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
