# 인상깊은 문구
- 단위 테스트의 핵심은 지속성과 확장성. 이를 통해 장기적으로 개발 속도를 유지 할 수 있음.
- 사람들은 종종 테스트가 많으면 많을 수록 좋다고 생각하지만 그렇지 않음. 코드는 자산이 아니라 책임임. 코드가 더 많아질수록 소프트웨어 내의 잠재적인 버그에 노출되는 표면적이 더 넓어지고 프로젝트 유지비가 증가함.
- 비용 요소는 다양한 활동에 필요한 시간에 따라 결정됨. 높은 유지 보수 비용으로 인해 순가치가 0에 가깝거나 심지어 0보다 작은 테스트를 만들쉬움. 지속 가능한 프로젝트 성장을 위해서는 고품질 테스트에만 집중해야함.
  - 기반 코드를 리팩터링할 때 테스트도 리팩터링하라
  - 각 코드 변경 시 테스트를 실행하라
  - 테스트가 잘못된 경고를 발생시킬 경우 처리하라
  - 기반 코드가 어떻게 동작하는지 이해하려고 할 때 는 테스트를 읽는 데 시간을 투자하라
- 커버리지 지표에 관한 문제. 테스트 대상 코드에 대해 각각의 결과를 철저히 검증한다해도 외부 라리브러리의 코드경로 까진 고려할 수 없음.
  - 테스트 대상 시스템의 모든 가능한 결과를 검증한다고 보장할 수 없다
  - 외부 라이브러리의 코드 경로를 고려할 수 있는 커버리지 지표는 없다
- 때문에, 코드 커버리지를 수치를 목표로 설정해선 안됨. 높은 수진의 커버리지를 다룬다라는 생각이 중요
- 성공적인 테스트 스위트
  - 개발주기에 동합돼 있다.
  - 코드베이스에서 가장 중요한 부분만을 대상으로 한다.
  - 최소한의 유지비로 최대의 가치를 끌어낸다.
    - 가치 있는 테스트 식별하기
    - 가치 있는 테스트 작성하기

# 요약
 - 코드베이스에 변경이 생길때마다 엔트로피는 증가한다. 지속적인 정리와 리팩터링 등과 같은 적절한 관리가 없으면 시스템은 점점 더 복잡해지고 흐트러진다 -> 테스트를 통해 이를 방지. 회귀에 대한 보험을 제공하는 도구
 - 좋은 단위테스트가 중요함. 그렇지 않으면 (나쁜 단위테스트, 테스트 없음) 점차 침체 됨.
 - 단위 테스트의 목표는 프로젝트가 지속적으로 성장하게 하는것
 - 테스트 스위트 내에 가치있는 테스트만 남기고 나머지는 모두 제거! 애플리케이션과 테스트 코드는 모두 자산이 아니라 부채!
 - 커버리지가 낮다는 것은 문제의 징후 이지만, 커버리지가 높다고해서 테스트 스위트의 품질이 높은것은아님(관리 비용문제)
 - 