package com.gngsn.covariance;

public class Customer {
    private Book book;

    public void order(BookStall bookStall) {
        // BookStall 에게 sell 메시지를 전송해 책을 구매
        this.book = bookStall.sell(new IndependentPublisher());
    }
}
