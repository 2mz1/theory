import java.math.BigDecimal;

/*
    의문)
    Money 를 그냥 long type 으로 하는 것과 Money Type 의 class 로 wrapper 하는거의 차이점이 무엇일까?
    소트웍스 앤솔러지 객체지향 생활체조 원칙을 보면
    3장에 모든 원시(primitive) 타입을 wrapper 하라고 적혀 있다.
    사유: 아무리 변수명을 잘 써도 그 값의 의미를 전달 하기가 힘들다.
    원시 값을 포장 함으로 써 class 를 만들고 객체를 만듬으로써
    해당 값이 어떤 "의미" 를 가지고 있는지 명확하게 보여 줄 수 있다.

    이책에서 소트웍스 앤솔러지 객체지향 생활체조 원칙 까지 생각하며 작성한걸까?
    아니면 객체지향의 기본 원칙이여서 추가 한 것일까?

    개인적인 의견 추가
    과거에 TDD 및 clean code 등을 공부 및 강의를 병행 하면서 나온것중 하나가
    객체지향 생활체조 원칙인데 해당 과정을 진행 할 때 모든 원시타입을 포장했다

    장점: 원시타입을 포장함으로써 명확하게 해당 객체가 무엇을 의미 하는지 보여줄 수 있다.
    ex) LottoNumber -> type int -> 로또 번호를 가지는 int type 의 원시타입을 포장한 객체 -> 명확하게 해당 객체는 로또번호를 표현하려고 사용한다는걸 보여줄 수 있음

    단점: 매번 모든 원시타입을 사용할 떄 포장(wrapper) 해야함

    의문: 단점에 이어지는 의문 실무에서 해당 객체를 하나 만드는데 메모리를 소모 한다
    현대 컴퓨터들은 메모리가 과거의 컴퓨터보다 많이 가지고 있어 큰 지장은 없어보이지만 (개인적인 생각)
    동시에 많은 트래픽을 받게되면 해당 객체를 만드는데 소모하는 메모리가 많아지고 OOM 이 뜰수도 있지 않을까?
    여기서 생각해볼 수 있는게 1장에서 나온 객체지향을 하다가 언젠가는 트레이드 오프를 해야하는 상황이 올 수 있을것이다. 라는 부분일까?
    -> 수요일에 한번 의논해보고 싶은 주제
    -> 다른 분들은 여기서 어떤 선택을 할지 궁금함.
 */
public class Money {
    public static final Money ZERO = Money.wons(0);

    private final BigDecimal amount;

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money plus(Money amount) {
        return new Money(this.amount.add(amount.amount));
    }

    public Money minus(Money amount) {
        return new Money(this.amount.subtract(amount.amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(
                BigDecimal.valueOf(percent)
        ));
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }

}
