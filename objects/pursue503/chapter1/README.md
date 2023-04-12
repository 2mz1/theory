# Chapter 1 객체 설계


# 글래스의 주장 (이론이 먼저일까? 실무가 먼저일까?)

- 이론보다는 실무가 우선
- 이론은 실무에서 반복적으로 사용되는 기법을 이론화 한것
- 특히 설계 분야에서 실무는 이론을 압도함
- 설계에 관해 설명할 때 제일 유용한 도구는 `코드` 그 자체


# 티켓 판매 애플리케이션 구현하기

**배경**

작은 소극장을 운영중 매출이 오르는 상황이다. 소극장 홍보겸 이벤트를 기획
추첨을 통해 선정된 관람객에게 무료로 공연을 관람하는 초대장 발송

**조건**

- 이벤트 당첨 관란객과 일반 입장 관란객을 다른 방식으로 입장시킴
- 이벤트 당첨 관람객은 초대장 → 티켓 `교환` 후 입장
- 이벤트에 당첨 안된 관람객은 티켓을 `구매`해야만 입장 할 수 있음
- Flow
    - 이벤트 당첨자인지 확인
    - 당첨자가 아닐경우 티켓 판매 후 입장

**구현된 클래스**

```java
import java.time.LocalDateTime;

public class Invitation {
    private LocalDateTime when;
}
```

```java
public class Ticket {
    private Long fee;

    public Long getFee() {
        return fee;
    }

}
```

```java
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
```

```java
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Bag getBag() {
        return bag;
    }
}
```

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicketOffice {
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketOffice(Long amount, Ticket ...tickets) {
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }

    public Ticket getTicket() {
        return tickets.remove(0);
    }

    public void minusAmount(Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(Long amount) {
        this.amount += amount;
    }

}
```

```java
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public TicketOffice getTicketOffice() {
        return ticketOffice;
    }
}
```

```java
public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        if (audience.getBag().hasInvitation()) {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().setTicket(ticket);
        } else {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }

}
```

# 무엇이 문제인가

위에 구현된 프로그램에는 문제점이 존재한다

로버트 마틴의 클린 소프트웨어에서 소프트웨어 모듈이 가져야 하는 세 가지 기능에 관해 설명한다

> 여기서 모듈이란 크기와 상관없이 클래스나 패키지, 라이브러리와 같은 프로그램을 구성하는 요소
>

- 실행 중에 제대로 동작하는 것
- 변경을 위해 존재하는 것
    - 대부분 모듈은 생명주기 동안 변경되기 떄문에 간단한 작업으로도 변경가능 해야함
    - 변경하기 어려운 모듈은 제대로 동작하더라도 개선해야함
- 코드를 읽는 사람과 의사소통 하는 것


# 예상을 빗나가는 코드

Theater 클래스의 enter 메서드가 수행하는 일을 말로 풀어보면

```
소극장은 관람객의 가방을 열어 그 안에 초대장이 들어 있는지 살펴본다.
가방 안에 초대장이 들어 있으면 판매원은 매표소에 보관돼 있는 티켓을 관람객의 가방 안으로
옮긴다. 가방 안에 초대장이 들어 있지 않다면 관람객의 가방에서 티켓 금액만큼의
현금을 꺼내 매표소에 적립한 후 매표소에 보관돼 있는 티켓을 관람객의 가방 안으로 옮긴다.
```

**문제점**

- 관람객과 판매원이 소극장의 통제를 받는 수동적인 존재임
- 관람객 임장에서 소극장이라는 제3자가 초대장을 확인 하기 위해 관람객의 가방을 마음대로 열어봄
- 판매원 입장에서 소극장이 허락도 없이 매표소에 보관 중인 티켓과 현금에 마음대로 접근할 수 있음

# 변경에 취약한 코드

관람객이 가방을 안들고 있다면? 관람객이 신용카드를 이용해 결제한다면?

모든 코드가 흔들린다

관람객이 가방을 들고 있다는 가정이 바뀔 경우

- Audience 클래스에서 Bag을 제거해야 함
- Audience의 Bag에 접근하는 Theater의 enter 메서드 역시 수정해야함

> 지나 치게 세부적인 사실에 의존하여 동작하면 무언가 하나가 바껴도 의존하는 모든 클래스를 다 바꿔야한다. 이것이 객체 사이의 `의존성` 과 관련된 문제
>


# 설계 개선하기

**문제점**

- Theater가 관람객의 가방과 판매원의 매표소에 직접 접근함

**개선점**

- Theater 가 Audience와 TicketSeller에 관해 세세한 부분까지 알지 못하도록 정보를 차단
- 관람객이 가방을 가지고 있다는 사실과 판매원이 매표소에서 티켓을 판매하는 사실을 Theater가 알필요가 없음
- Theater가 원하는 것은 관람객이 소극장에 입장하는 것 뿐
- 관람객이 스스로 가방 안의 현금과 초대장을 처리하고 판매원이 스스로 매표소의 티켓과 판매 요금을 다루게함


# 자율성을 높이기 && 개선점

각 객체가 스스로 행동 하도록 변경함  
[변경 내용](https://github.com/2mz1/theory/commit/544cbe53441df612efee95a850a54814abeac435)

# 캡슐화와 응집도

핵심은 객체 내부의 상태를 캡슐화 하고 객체 간에 오직 메시지를 통해서만 상호작용하도록 만드는 것

- Theater는 TicketSeller의 내부에 대해 알지 못하고 TicketSeller가 sellTO 메시지를 이해하고 응답하는 사실만알게함
- TicketSeller 역시 Audience의 내부에 대해 알필요가 없다. 단지 Audience가 buy 메시지에 응답할 수 있다는 것만 알게함
- 밀접하게 연관된 작업만 수행 하고 연관성 없는 작업을 다른 객체에게 위임하는 객체를 `응집도` 가 높다고 푠현
- 자신의 데이터를 `스스로` 처리하는 자율적인 객체를 만들면 겹합도를 낮추고 응집도를 높힐수 있다.


# 절차지향과 객체지향

**개선전**

Theater부터 관람객까지 모든거를 호출하면서 처리하는 로직을 `절차적 프로그래밍` 이라고 부른다.

- 우리는 관람객과 판매원이 자신의 일을 스스로 처리할 것이라고 예상함
- 절차적 프로그래밍에서는 관람객과 판매원은 수동적인 존재임

절차적 프로그래밍은 우리의 `직관` 에 위배된다.

**개선후**

- 자신의 데이터를 스스로 처리하도록 변경
- 자신의 데이터에는 자신만 접근 가도록 변경
- 데이터와 프로세스가 동일한 모듈(객체) 내부에 위치하도록 변경

이것을 `객체지향 프로그래밍` 이라고 부른다.

# 책임의 이동

**개선전**

모든 책임 및 행위들이 Theater에 집중 되어 있음

**개선후**

Theater에 몰려잇는 책임이 개별 객체로 이동 하여 각 객체는 `자신을 스스로 책임` 지고 있


# 정리

실생활로 바라보았을 때 판매자 및 관람객이 스스로 자신을 책임 지게 하였다.

이것은 우리가 세상을 바라보는 직관과도 일치한다. 따라서 이 직관에 따르는 코드는 이해하기 더 쉬워진다.

훌륭한 객체지향 설계란 소프트웨어를 구성하는 모든 객체들이 `자율적` 으로 행동하는 설계를 가리킨다.

## 좋은 설계란

> 오늘 요구하는 기능을 온전히 수행하면서 내일의 변경을 매끄럽게 수용할 수 있는 설계
>

## 객체 지향 설계

- 변경 가능한 코드
- 이해하기 쉬운 코드
- 협력하는 객체 사이의 의존성을 적절하게 관리하는 설계
- 데이터와 프로세스를 하나의 덩어리로 모으는 것