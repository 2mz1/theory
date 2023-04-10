package entity;

import lombok.Getter;

public class Audience {

    @Getter
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }
}
