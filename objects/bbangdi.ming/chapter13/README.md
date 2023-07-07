## 인상적인 부분
- 인터페이스를 클라이언트의 기대에 따라 분리함으로써 변경에 의해 영향을 제어하는 설계 원칙을 인터페이스 분리 원칙(ISP)이라고 한다.
- 서브클래싱: 다른 클래스의 코드를 재상용할 목적으로 상속을 사용하는 경우를 가리킨다. 자식 클래스와 부모 클래스의 행동이 호환되지 않기 때문에 자식 클래스의 인스턴스가 부모 클래스의 인스턴스를 대체할 수 없다.
  - 서브클래싱 -> 나쁜상속
- 서브타이핑: 타입 계층을 구성하기 위해 상속을 사용하는 경우를 가리킴
- 리스코프치환 원칙 : 서브타입은 그것의 기반 타입에 대해 대체 가능해야 한다
  ```java
    public class Rectangle {
        protected int width;
        protected int height;
        public void setWidth(int width) { this.width = width; }
        public void setHeight(int height) { this.height = height; }
        public int getArea() { return width * height; }
        public int reSize(Rectangle rectangle, int width, int height) {
            this.width = width;
            this.height = height;
            assert rectangle.getArea() == width * height;
        }
    }
  
    public class Square extends Rectangle {
        public void setWidth(int width) {
            this.width = width;
            this.height = width;
        }
        public void setHeight(int height) {
            this.width = height;
            this.height = height;
        }
    }
  
    public class Something {
        public static void main(String[] args) {
            Square square = new Square(10, 10);
            square.reSize(square, 50, 100);
            assert square.getArea() == 5000; // error
        }
    }
  ```
  - 행동 호환성과 리스코프 치환 원칙에서 대체 가능성을 결정하는 것은 클라이언트다!
    - (클라이언트 입장에서) 정사각형은 직사각형이다. (클라이언트 입장에서) 펭귄은 새다.
      - 클라이언트 입장을 배제한다면 매우 혼란이 될 수 있다.
  - is-a 관계 역시 행동이 우선이다. 단순히 is-a 관계가 된다고 해서 상속 관계로 만들지 마라
  - 계약에 의한 설계
    - TODO
  - 서브타입에 슈퍼 타입보다 더 강력한 사전 조건을 추가 할 순 없지만 약한건 문제 되지 않음.
  - 서브타입에 슈퍼 타입보다 더 강한 사후 조건을 추가할 수 있음
## 궁금한 부분
 - assert 를 활용???

