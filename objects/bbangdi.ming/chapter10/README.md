## 인상적인 부분
- 중복코드를 제거해야하는 이유
    - 변경을 방해한다.
        - 중복여부를 판단하는 기준은 요구사항이 변경됐을때 같이 변경되는지 여부임
- DRY 원칙을 따라야함(Dont Repeat Yourself)
- 상속의 취약점
    - 부모 클래스 변경에 의해 자식 클래스가 영향을 받는 취약한 기반 클래스 문제
    - 부모 클래스의 세부 구현사항을 자식 클래스가 의존해야하기 때문에 캡슐화를 약화 시킴
        - 객체 지향의 핵심인 캡슐화를 통한 통제가 불가능해짐
    - 단순히 코드를 재사용하기 위해 상속을 하는것은 불필요한 오퍼레이션이 인터페이스에 스며들게 할 수 있어 위험함
    - 상속을 받은 부모클래스의 메소드가 자식클래스의 기능을 깨트릴수있음
    - 중복코드를 제거하기 위해 상속을 도입하기전 원칙
        - 두 메소드의 차이점을 메소드로 분리
        - 중복 코드를 부모 클래스의 코드를 하위로 내리지 말고 자식 클래스의 코드를 상위로 위치 시켜야함 -> 재사용성과 응집도 측면에서 도움됨
    - 상속의 오남용은 이해하고 확장하기 어렵게함
    - 상속 대신 합성
