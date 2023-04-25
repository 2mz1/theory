package entity;

public class Screening {

    private Movie movie;

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public boolean isSequence(int sequence) {

        return false;
    }

    public Money getMovieFee() {
        return movie.getFee();
    }
}
