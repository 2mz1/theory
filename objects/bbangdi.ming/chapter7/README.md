## 인상적인 부분
 - 불필요한 정보를 제거하고 현재의 문제 해결에 필요한 핵심만 남기는 작업을 추상화
 - 큰 문제를 해결 가능한 작은 문제로 나누는 작업을 분해(decomposition)라고 함
 - 최상위 문장을 기술하고 구체화하는 과정을 통해 추상화 수준을 감소시켜야함
 - 모듈의 장점과 한계
     - 모듈 내부의 변수가 변경되더라도 모듈 내부에만 영향을 미친다
     - 비즈니스 로직과 사용자 인터페이스에 대한 관심사를 분리한다
     - 전역 변수와 전역 함수를 제거함으로써 네임스페이스 오염을 방지한다
 - 설계는 변경과 관련된 것이다.
     - 설계의 유용성은 변경의 방향성과 발생 빈도에 따라 결정된다.
     - 타입 추가라는 변경의 압력이 더 강한 경우에는 객체지향이 유리함
     - 오퍼레이션 추가라는 변경의 압력이 더 강한 경우에는 추상 데이터 타입이 유리함
 - 객체지향적인 접근법이 모든 경우에 올바른 해결 방법인 것은 아님
 - 객체지향 == 역할, 책임, 협력
     - 객체가 참여할 협력을 결정하고 협력에 필요한 책임을 수행하기 위해 어떤 객체가 필요한지 고민하라
     - 그 책임을 다양한 방식으로 수행해야 할 때만 타입 계층 안에 각 절차를 추상화하라
     - 타입 계층과 다형성은 협력이라는 문맥 안에서 책임을 수행하는 방법에 관해 고민한 결과물이어야 하며 그 자체가 목적이 되어서는 안됨