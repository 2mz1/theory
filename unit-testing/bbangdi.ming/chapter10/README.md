데이터 베이스 테스트
- 데이터 베이스 스키마를 소스 코드와 같이 형상 관리 시스템에 저장하라
- 개발자마다 데이터베이스 인스턴스를 두게하라.(java 진영에는 메모리 db가 있고, spring data를 사용하면 db 별로 달라지는 sql을 일원화해 관리할 수 있음)
  - 책에서는 메모리 DB 사용을 지양하라고 했지만, spring data를 사용하면 메모리 DB를 사용해도 동일한 보호 수준을 갖을 수 있음
  - 