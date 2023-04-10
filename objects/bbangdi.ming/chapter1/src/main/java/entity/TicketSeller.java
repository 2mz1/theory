package entity;

import lombok.Getter;

public class TicketSeller {
    @Getter
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        if (audience.getBag().hasInvitation()) {
            Ticket ticket = this.ticketOffice.getTicket();
            audience.getBag().setTicket(ticket);
            System.out.println("초대장을 가지고 있으므로 티켓을 받습니다.");
        } else {
            Ticket ticket = this.ticketOffice.getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            this.ticketOffice.plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
            System.out.println("현금 " + ticket.getFee() + "원을 지불하고 티켓을 받습니다.");
        }
    }
}
