## Objects
<small>Author: [Gyeongseon Park](https://github.com/gngsn)</small>

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

<br/>
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

<br/>
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
<details>
<summary><b>CHAPTER 04. 설계 품질과 트레이드오프</b></summary>

<br/>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter4"> 🔗 link </a>
<br/>

**TL;DR**
- 좋은 설계란 오늘의 기능을 수행하면서 내일의 변경을 수용할 수 있는 설계이다.
- 객체지향 프로그램을 통해 전반적으로 얻을 수 있는 장점은 오직 설계 과정 동안 캡슐화를 목표로 인식할 때만 달성될 수 있다.
- 추측의 의한 설계 전략은 접근자와 수정자에 과도하게 의존하게 하는 설계 방식이다.
- 결론: 데이터 중심의 설계는 **너무 이른 시기에 데이터에 대해 고민**하기 때문에 **캡슐화에 실패**
- 객체의 구현을 먼저 결정하고 협력을 고민하기 때문에 이미 구현된 객체의 인터페이스를 억지로 끼워맞출 수 밖에 없다.

<br/>
</details>
<details>
<summary><b>CHAPTER 05. 책임 할당하기</b></summary>

<br/>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter5"> 🔗 link </a>
<br/>

**TL;DR**
- **GRASP Pattern**: General Responsibility Assignment Software Pattern, 책임 할당을 위한 소프트웨어 패턴
  - : 책임을 수행하는 데 필요한 메시지를 결정하고, 책임을 수행할 정보 전문가에게 책임을 할당하라
  - **INFORMATION EXPERT 패턴**: 책임을 정보 전문가(책임을 수행하는 데 필요한 정보를 가지고 있는 객체)에게 할당하라
  - **LOW COUPLING 패턴**: 설계의 전체적인 결합도가 낮게 유지되도록 책임을 할당하라
  - **HIGH COHESION 패턴**: 높은 응집도를 유지할 수 있게 책임을 할당하라
  - **CREATOR 패턴**: 연결되거나 관련될 필요가 있는 객체에게 객체 생성 책임을 할당하라 (잘 알고 있거나/어차피 사용해야 하는 객체)
  - **POLYMORPHISM 패턴**: 타입을 명시적으로 정의하고 각 타입에 다형적으로 행동하는 책임을 할당하라
  - **PROTECTED VARIATIONS 패턴**: 변화가 예상되는 불안정한 지점들을 식별하고 그 주위에 안정된 인터페이스를 형성하도록 책임을 할당하라
- **리팩터링을 고려할 시점 2가지**
  - 클래스의 속성이 서로 다른 시점에 초기화되거나 일부만 초기화된다는 것은 응집도가 낮다는 증거
  - 메서드들이 사용하는 속성에 따라 그룹이 나뉜다면 클래스의 응집도가 낮다는 증거
- 주석을 추가하는 대신 **메서드를 작게 분해**해서 각 메서드의 **응집도를 높여라**
  - Benefit: 재활용될 확률 증가 / 메소드 이름으로 주석을 읽는 느낌을 줌 / 오버라이딩하기 용이
- 처음부터 책임 주도 설계 방법을 따르는 것보다 동작하는 **코드를 작성한 후, 리팩터링하는 것이 더 훌륭한 결과물을 낳을 수도 있음**

<br/>
</details>
<details>
<summary><b>CHAPTER 06. 메시지와 인터페이스</b></summary>

<br/>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter6"> 🔗 link </a>
<br/>

**TL;DR**
- **Law of Demeter**: 디미터 법칙. 객체의 내부 구조에 강하게 결합되지 않도록 **협력 경로를 제한**하라
  - Use only one dot.
  - 특정 조건의 클래스에게만 메시지 전송: **① 메서드의 인자**로 전달된 클래스, **② 해당 메서드를 가진 클래스** 자체, **③ 해당 메서드를 가진 클래스의 인스턴스 변수 클래스**
  - Shy Code: 부끄럼타는 코드, 디미터 법칙에서 보이는 패턴, 불필요한 어떤 것도 다른 객체에게 보여주지 않으며, 다른 객체의 구현에 의존하지 않는 코드
  - Train Wreck: 기차 충돌, 디미터 법칙 위반 패턴, 여러 대의 기차가 한 줄로 늘어진 것처럼 보이는 코드로 내부 구현이 외부로 노출됐을 때 나타나는 전형적인 형태.
- **Tell, Don't Ask**: 묻지 말고 시켜라. **객체의 내부 구조를 묻는 메시지**가 아니라 **수신자에게 무언가를 시키는 메시지**를 강조하는 법칙
- **의도를 드러내는 선택자(Intention Revealing Selector)**: 무엇을 하느냐에 따라 메서드의 이름을 짓는 패턴
- **명령-쿼리 분리**
  - Command: 객체의 **상태를 수정**하는 오퍼레이션
  - Query: 객체와 관련된 **정보를 반환**하는 오퍼레이션
- 원칙을 맹신하지 마라, **원칙이 적절한 상황과 부적절한 상황을 판단할 수 있는 안목을 길러라.**
- Design By Contract: 계약에 의한 설계. 협력을 위해 클라이언트와 서버가 준수해야 하는 제약을 코드 상에 명시적으 로 표현하고 강제할 수 있는 방법

<br/>
</details>
<details>
<summary><b>CHAPTER 07. 객체 분해</b></summary>

<br/>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter7"> 🔗 link </a>
<br/>

**TL;DR**
- 추상화: 불필요한 정보를 제거하고 현재의 문제 해결에 필요한 핵심만 남기는 작업
- 분해(decomposition): 큰 문제를 해결 가능한 작은 문제로 나누는 작업
- 소프트웨어는 **데이터**를 이용해 정보를 표현하고 **프로시저**를 이용해 데이터를 조작
  - 프로시저 추상화: = 기능 분해, 알고리즘 분해. 소프트웨어가 무엇을 해야 하는지를 추상화.
  - 데이터 추상화: 소프트웨어가 무엇을 알아야 하는지를 추상화
- 모듈은 퍼블릭 인터페이스를 외부에 제공해서 복잡성과 변경 가능성을 감춰야 한다.
- 클래스는 절차를 추상화(procedural abstraction), 추상 데이터 타입은 타입을 추상화(type abstraction).
- 추상 데이터 타입과 객체지향 설계의 유용성은 설계에 요구되는 변경의 압력이 '타입 추가'에 관한 것인지, 아니면 '오퍼레이션 추가'에 관한 것인지에 따라 달라짐
  - 변경의 압력이 **타입 추가하는 것**이라면 더 강한 경우에는 객체지향
  - 변경의 압력이 **오퍼레이션을 추가하는 것**이라면 추상 데이터 타입

<br/>
</details>
<details>
<summary><b>CHAPTER 08. 의존성 관리하기</b></summary>

<br/>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter8"> 🔗 link </a>
<br/>

**TL;DR**

- 의존성 전이: 의존하는 대상의 의존성에 대해서도 연쇄적으로 의존하게 되는 것
  - **직접 의존성**(direct dependency): 한 요소가 다른 요소에 직접 의존하는 경우
  - **간접 의존성**(indirect dependency): 직접적인 관계는 존재하지 않지만 의존성 전이에 의해 영향이 전파되는 경우
- **컴파일타임 의존성**: 클래스(작성한 코드)의 구조
- **런타임 의존성**: 객체 사이의 의존성
- 컨텍스트 독립성: 각 객체가 해당 객체를 실행하는 시스템에 관해 아무것도 알지 못하도록 함
  - 클래스가 사용 될 **특정 문맥**에 **최소한의 가정만**으로 이뤄져 있다면 **다른 문맥에서 재사용하기가 더 수월**
- 의존성 해결을 위한 방법: 생성자, setter 메서드, 메서드 실행 인자
- 결합도의 정도: 한 요소가 자신이 의존하고 있는 **다른 요소에 대해 알고 있는 정보의 양**으로 결정
- 경계해야 할 것은 의존성 자체가 아니라 의존성을 감추는 것
- new는 해롭다: 외부로부터 이미 생성된 인스턴스를 전달받자
- 선언적인 정의: 작은 객체들의 행동을 조합함으로써 새로운 행동을 이끌어낼 수 있는 설계
  - 방법(how)이 아니라 목적(what)에 집중할 수 있어 시스템의 행위를 변경에 용이

<br/>
</details>

<details>
<summary><b>CHAPTER 09. 유연한 설계 </b></summary>

<br/>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter9"> 🔗 link </a>
<br/>

**TL;DR**

- 개방-폐쇄 원칙
  - 정의: 소프트웨어 개체(클래스, 모듈, 함수 등등)는 확장에 대해 열려 있어야 하고, 수정에 대해서는 닫혀 있어야 한다.
  - 컴파일타임 의존성은 유지하면서, 런타임 의존성의 가능성을 확장하고 수정할 수 있는 구조.
- 생성과 사용을 분리하라 (separating use from creation)
- FACTORY: 생성과 사용을 분리하기 위해 객체 생성에 특화된 객체.
- 추가하려는 행동을 책임질만한 도메인 개념이 존재하지 않는다면, PURE FABRICATION을 추가하고 책임을 할당하라.
  - PURE FABRICATION: 순수한 가공물. 책임을 할당하기 위해 창조되는 도메인과 무관한 인공적인 객체.
  - 도메인 개념의 객체와 순수하게 창조된 가공의 객체들이 모여 자신의 역할과 책임을 다하고 조화롭게 협력하는 애플리케이션을 설계하는 것이 목표여야 한다.
- SERVICE LOCATOR 패턴: 의존성을 해결할 객체들을 보관하는 저장소. SERVICE LOCATOR 에게 의존성을 해결해줄 것을 요청. (의존성을 감춘다는 큰 단점, 글쓴이의 지양 패턴)
- SEPARATED INTERFACE 패턴: 인터페이스와 그 구현을 별개의 패키지에 위치시키는 패턴.
- 잘 설계된 객체지향 애플리케이션에서는 **인터페이스의 소유권을 서버가 아닌 클라이언트에 위치**시킨다.

<br/>
</details>

<br/><br/>