package com.gngsn.chapter2.v1;

import java.time.LocalDateTime;

/**
 * Version1.
 */
public class Screening {
    private Movie movie;
    private int sequence;
    private LocalDateTime whenSreened;

    public Screening(Movie movie, int sequence, LocalDateTime whenSreened) {
        this.movie = movie;
        this.sequence = sequence;
        this.whenSreened = whenSreened;
    }

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
    }

    private Money calculateFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public LocalDateTime getStartTime() {
        return whenSreened;
    }

    public boolean isSequence(int sequence) {
        return this.sequence == sequence;
    }

    public Money getMovieFee() {
        return movie.getFee();
    }
}