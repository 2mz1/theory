package com.gngsn.covariance;

/**
 * 전문적으로 잡지를 판매
 */
public class MagazineStore extends BookStall{

    @Override
    public Book sell(IndependentPublisher independentPublisher) {
        return new Magazine(independentPublisher);
    }
}
