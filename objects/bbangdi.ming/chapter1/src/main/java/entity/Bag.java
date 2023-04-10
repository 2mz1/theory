package entity;

public class Bag {
    private Long money;
    private Invitation invitation;
    private Ticket ticket;

    public Bag(Long money) {
        this(null, money);
    }

    public Bag(Invitation invitation, Long money) {
        this.invitation = invitation;
        this.money = money;
    }

    public boolean hasInvitation() {
        return invitation != null;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void minusAmount(Long amount) {
        this.money -= amount;
    }

    public boolean hasTicket() {
        return ticket != null;
    }
}
