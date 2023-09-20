# 인상 깊었던 부분
- 단위 테스트의 중요한 속성
    1. 작은 코드 조각을 검증하고,
    2. 빠르게 수행하고,
    3. 격리된 방식으로 처리하는 자동화된 테스트
- 격리 범위에 따른 테스트
  - 런던파 : 테스트 대상을 단위를 서로 분리해야함. 단일 클래스 단위! 모든 의존성을 stub으로
    - 장점 : 단위마다 테스트를 하기 때문에 테스트 실패 후 버그가 있는 기능을 쉽게 찾을 수 있음
    - 단점 : 테스트를 위해 stub을 만들어야 하기 때문에 코드가 늘어남
  - 고전파 : 동작 단위로 분리. 따라서 공유 의존성만  stub으로
    - 장점 : 최종 사용자의 관점에서 시스템을 검증함. 대부분 직접 접근하면서 실질적인 검증이 가능함
    - 단점 : 단위테스트 기준을 충족하지 못함