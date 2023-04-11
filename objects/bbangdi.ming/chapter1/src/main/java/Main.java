import entity.*;

public class  Main {
    public static void main(String[] args) {
        TicketOffice ticketOffice = new TicketOffice(1_000_000L, new Ticket(10_000L));
        TicketSeller ticketSeller = new TicketSeller(ticketOffice);
        Theater theater = new Theater(ticketSeller);

        theater.enter(new Audience(new Bag(10_000L)));
    }
}
