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

    public Long hold(Ticket ticket) {
        if (hasInvitation()) {
            setTicket(ticket);
            System.out.println("초대장을 가지고 있으므로 티켓을 받습니다.");
            return 0L;
        } else {
            minusAmount(ticket.getFee());
            setTicket(ticket);
            System.out.println("현금 " + ticket.getFee() + "원을 지불하고 티켓을 받습니다.");
            return ticket.getFee();
        }
    }
}
