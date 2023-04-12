public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    // 극장에서는 관람객이 오면 판매원한테 안내만 해주도록함
    public void enter(Audience audience) {
        ticketSeller.sellTo(audience);
    }

}
