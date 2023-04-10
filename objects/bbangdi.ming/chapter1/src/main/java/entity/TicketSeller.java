package entity;

import lombok.Getter;

public class TicketSeller {
    @Getter
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }
}
