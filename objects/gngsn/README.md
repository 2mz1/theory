## Objects
<small>Author: [Kyeongseon Park](https://github.com/gngsn)</small>

<br/>

<details>
<summary><b>CHAPTER 01. 객체, 설계</b></summary>

<br/>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter1"> 🔗 link </a>
<br/>

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

<details>
<summary><b>CHAPTER 02. 객체지향 프로그래밍</b></summary>

<br/>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter2"> 🔗 link </a>
<br/>

**TL;DR**
- 객체지향 패러다임 특징: 요구사항과 프로그램을 객체를 동일한 관점에서 바라볼 수 있기 때문에 도메인 개념이 프로그램 객체와 클래스로 매끄럽게 연결될 수 있음
- 프로그래머의 역할을 클래스 작성자 (class creator)와 클라이언트 프로그래머 (client programmer)로 구분
  - 클라이언트 프로그래머: 필요한 클래스들을 엮어서 애플리케이션을 빠르고 안정적으로 구축
  - 클래스 작성자: 구현 은닉 - 클라이언트 프로그래머가 내부에게 필요한 부분 만을 공개
- 객체지향 프로그램을 작성할 때는 협력의 관점에서 어떤 객체가 필요한지 결정하고, 객체들의 공통 상태와 행위를 구현하기 위해 클래스를 작성
- 객체가 다른 객체와 상호작용할 수 있는 유일한 방법은 **객체 간 메시지 전송** 뿐
- **유연한 설계**와 **이해하기 쉬운 코드 및 디버깅**은 트레이드 오프 관계: 항상 유연성과 가독성 사이에서 고민해야 함
- 추상화는 요구사항의 정책을 높은 수준에서 서술할 수 있고, 상위 정책을 쉽고 간단하게 표현함으로써 더 유연한 설계를 하게 해줌

**Impression**

- 프로그래머의 역할을 클래스 작성자 (class creator)와 클라이언트 프로그래머 (client programmer)로 구분하라
- BigDecimal: 금액이나 복잡한 숫자 계산에는 BigDecimal을 사용하는 게 분명히 좋음. 가령, 부동 소수점 관련 문제
</details>

<details>
<summary><b>CHAPTER 03. 역할, 책임, 협력</b></summary>

<br/>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter3"> 🔗 link </a>
<br/>

**TL;DR**
- 객체지향 패러다임의 관점에서 핵심: 역할(role), 책임(responsibility), 협력(collaboration)
- 메시지 전송: 객체 사이의 협력을 위해 사용할 수 있는 유일한 커뮤니케이션 수단
- 협력 > 행동 > 상태: 협력은 객체 설계의 문맥(context)을 제공하며 행동, 상태를 결정
- CRC 카드 활용: 역할 식별, 책임 할당, 협력을 명시적이고 구체적인 실용적인 설계 기법
- Information Expert (정보 전문가) 패턴: 책임을 수행하는 데 필요한 정보를 가장 잘 알고 있는 전문가에게 그 책임을 할당하는 것
- 역할 / 객체: 객체가 항상 하나의 역할을 수행한다면 둘은 동일한 것, 하지만 협력에서 **하나 이상의 객체가 동일한 책임을 수행**할 수 있으면 **역할**(서로 다른 방법으로 실행할 수 있는 책임의 집합)
- 협력 (Collaboration) -- _reference_ → 역할 (Role) -- _select from_ → 객체 (Object) -- _instance of_ → 클래스 (Class)

**Impression**

- Information Expert (정보 전문가) 패턴
- 상태를 우선시 하는 게 아니라 행동이 우선시 되어 상태를 결정 (DDD와 반대되는 개념)
- Spring에서의 협력은 **DI**, 협력을 위해 DI를 사용한다.
</details>

<br/><br/>