/**
 * 관람객이 가지고 있을 가방
 * 초대장, 현금, 티켓
 */
public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    /*
        생성자 조건
        조건1. 이벤트 당첨자는 초대장이 존재
        조건2. 이벤트 미당첨자는 초대장이 존재 안함
     */

    public Bag(long amount) {
        this(null, amount);
    }

    public Bag(Invitation invitation, long amount) {
        this.invitation = invitation;
        this.amount = amount;
    }

    public boolean hasInvitation() {
        return invitation != null;
    }

    public boolean hasTicket() {
        return ticket != null;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void minusAmount(Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(Long amount) {
        this.amount += amount;
    }

}
