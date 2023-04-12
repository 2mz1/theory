## Objects
<small>Author: [Kyeongseon Park](https://github.com/gngsn)</small>

<br/>

<details>
<summary><b>CHAPTER 01. 객체, 설계</b></summary>

**TL;DR**

- 소프트웨어 모듈 목적은 '제대로된 실행 동작', '변경 용이성', '코드를 읽는 사람과의 의사소통' 이다.
- 객체는 자신의 데이터를 스스로 처리하는 자율적인 존재여야 한다.
- 객체는 캡슐화를 이용해 의존성을 적절히 관리하여 결합도를 낮추는 것이다.
- 설계는 여러 방법이 될 수 있는, 트레이드오프의 산물이다.
- 훌륭한 객체지향 설계는 모든 객체들이 자율적으로 행동하며, 내일의 변경을 매끄럽게 수용할 수 있는 설계이다.

**QUESTION**

- 이해하기 쉬운 코드를 위해서라면, 아래와 같이 수정하는 게 낫지 않을까?
    1. Theater -> TicketOffice
    2. TicketOffice -> TicketBox (TicketSeller가 TicketBox를 속성으로 포함)
    3. Ticket에 Theater 위치 속성 추가 -> Audience에 moveTo 메소드 추가

- TicketSeller가 TicketOffice를 가지고 있다는 사실이 어색함
- Theater과 TicketOffice는 개별된 공간이라고 했으니, Theater를 TicketOffice라고 바꾸는 게 낫지 않을까?
</details>
