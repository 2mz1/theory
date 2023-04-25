package entity;

import lombok.Getter;

public class Ticket {
    @Getter
    private Long fee;

    public Ticket(Long fee) {
        this.fee = fee;
    }
}
