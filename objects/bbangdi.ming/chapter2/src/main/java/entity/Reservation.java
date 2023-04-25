package entity;

public class Reservation {

    private Customer customer;
    private Screening screening;
    private Money fee;
    private int audienceCount;

    public Reservation(Customer customer, Screening screening, Money calculateFee, int audienceCount) {
        this.customer = customer;
        this.screening = screening;
        this.fee = calculateFee;
        this.audienceCount = audienceCount;
    }
}
