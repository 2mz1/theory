## 인상깊은 문구
 - 디미터 법칙 : 객체의 내부 구조에 강하게 결합되지 않도록 협력 경로를 제한하라는 것. 낯선 자에게 말하지 말라. 오직 인접한 이웃하고만 말하라.
 - 묻지 말고 시켜라
 - .(dot) 을 여러개 사용하지 마라
 - 훌륭한 인터페이스는 객체가 어떻게 작업을 수행하는지 노출해선 안된다. 인터페이스는 객체가 어떻게 하는지가 아니라 무엇을 하는지를 서술해야한다.
 - 의도를 드러내는 인터페이스
   - 메소드에 대해 제대로 커뮤니케이션 하지 못함. 사용자가 내부 구현을 정확히 모르면 같은 행위를 하는 두개의 구현체가 동일한 행위를 하는지 파악하기 어렵다.
   - 캡슐화 위반
 - 명령과 쿼리 분리하기
   - 한번에 한가지 일만 하도록 구현해야함

## 다른 생각
 - 묻지말고 시켜라 원칙에 따르면 메소드는 항상 void 타입인가?
   - 객체에게 시키는 것이 항상 가능한 것은 아니다. 가끔씩은 물어야한다.
     - 설계는 트레이드 오프의 산물!