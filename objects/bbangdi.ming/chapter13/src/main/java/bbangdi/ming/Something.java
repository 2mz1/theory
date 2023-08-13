package bbangdi.ming;

public class Something {

    public void start() {
        int num = 0;

        preCheck(num);
        System.out.println("valid number : " + num);
    }


    private void preCheck(int num) {
        assert num > 0;
    }
}
