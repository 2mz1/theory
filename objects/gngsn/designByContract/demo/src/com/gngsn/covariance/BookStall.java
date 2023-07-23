package com.gngsn.covariance;

/**
 * 책을 판매하는 가판대
 */
public class BookStall {

    public Book sell(IndependentPublisher independentPublisher) {
        return new Book(independentPublisher);
    }
}
