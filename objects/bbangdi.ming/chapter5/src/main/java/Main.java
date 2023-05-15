import entity.discount.PeriodCondition;

public class Main {

    public static void main(String[] args) {
        PeriodCondition condition = new PeriodCondition(null, null, null);
        System.out.println(condition.getMax());
    }
}
