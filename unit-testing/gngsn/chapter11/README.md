# CHAPTER 11. 단위 테스트 안티 패턴

**TL;DR**


<br/><br/>

---

<br/>

## 1. 비공개 메서드 단위 테스트

### 1.1 비공개 메서드와 테스트 취약성

- 비공개 메서드를 노출하면 테스트가 구현 세부사항과 결합되고 결과적으로 리팩터링 내성이 떨어짐
('4대 요소 되짚어보기: 회귀방지, 리팩터링내성, 빠른피드백, 유지보수성)

- 비공개 메서드를 직접 테스트하는 대신, 포괄적인 식별할 수 있는 동작으로서 간접적으로 테스트하는 것이 좋음

<br/>

### 1.2 비공개 메서드와 불필요한 커버리지

- 죽은 코드
  - 테스트에서 벗어난 코드가 어디에도 사용되지 않는다면, 리팩터링 후에도 남아서 관계없는 코드일 수 있음
  - 삭제하는 것이 좋음
- 추상화 누락
  - 비공개 메서드가 너무 복잡해서 클래스의 공개 API를 통해 테스트하기 어렵다면, 별도의 클래스로 도출해야 하는 추상화가 누락됐다는 징후

  
```csharp
public class Order {
    private Customer _customer;
    private List<Product> _products;

    public string GenerateDescription() {
        return $"Customer name: {_customer.Name}, " +
            $"total number of products: {_products.Count}, " +
            $"total price: {GetPrice()}";                       // 복잡한 비공개 메서드를 간단한 공개 메서드에서 사용
    }

    private decimal GetPrice() {                                // 복잡한 비공개 메서드
        decimal basePrice = /* Calculate based on _products */;
        decimal discounts = /* Calculate based on _customer */;
        decimal taxes = /* Calculate based on _products */;
        return basePrice - discounts + taxes;
    }
}
```

1. `GenerateDescription()` method
- 매우 간단, 주문 내용 출력
- GetPrice() 사용

2. `GetPrice()` method
- 중요한 비즈니스 로직이 있기 때문에 테스트를 철저히 해야함
- 이 로직은 추상화 누락
- GetPrice메서드를 노출하기 보다는 다음 예제와 같이추상화를 별도의 클래스로 도출해서 명시적으로 작성하는 것이 좋음

```csharp
public class Order {
    private Customer _customer;
    private List<Product> _products;

    public string GenerateDescription() {
        var calc = new PriceCalculator();

        return $"Customer name: {_customer.Name}, " +
            $"total number of products: {_products.Count}, " +
            $"total price: {calc.Calculate(_customer, _products)}";
    }
}

public class PriceCalculator {
    public decimal Calculate(Customer customer, List<Product> products) {
        decimal basePrice = /* Calculate based on products */;
        decimal discounts = /* Calculate based on customer */;
        decimal taxes = /* Calculate based on products */;
        return basePrice - discounts + taxes;
    }
}
```

- `Order` 와 별개로 `PriceCalculator`를 테스트할 수 있음
- `PriceCalculator` 에는 숨은 입출력이 없으므로 출력 기반 (함수형) 스타일의 단위테스트 가능


<br/>

### 1.3 비공개 메서드 테스트가 타당한 경우

**비공개 메서드를 절대 테스트하지 말라는 규칙**에도 **예외가 존재**


코드의 공개 여부와 목적의 관계는 아래와 같음

|     | 식별할 수 있는 동작 | 구현 세부 사항 |
|-----|-------------|----------|
| 공개  | 좋음          | 나쁨       |
| 비공개 | 해당없음        | 좋음       |

- 식별할 수 있는 동작을 공개로 하고 구현 세부사항을 비공개로 하면 API가 잘 설계됐다고 할 수 있음
- 구현 세부사항이 유출되면 코드 캡슐화를 해침

- 비공개 메서드를 테스트하는 것 자체는 나쁘지 않음
- 비공개 메서드가 구현 세부사항의 프록시에 해당하므로 나쁜 것
- 구현 세부사항을 테스트하면 궁극적으로 테스트가 깨지기 쉽기 때문

<br/>

_신용 조회를 관리하는 시스템_

```csharp
public class Inquiry {
    public bool IsApproved { get; private set; }
    public DateTime? TimeApproved { get; private set; }

    private Inquiry(bool isApproved, DateTime? timeApproved) {  // 비공개 생성자
        if (isApproved && !timeApproved.HasValue)
            throw new Exception();

        IsApproved = isApproved;
        TimeApproved = timeApproved;
    }

    public void Approve(DateTime now) {
        if (IsApproved)
            return;

        IsApproved = true;
        TimeApproved = now;
    }
}
```

ORM은 공개 생성자가 필요하지 않으며, 비공개 생성자로 잘 작동할 수 있음

- 승인 로직은 분명히 중요하므로 단위 테스트를 거쳐야 함 vs 생성자를 공개하는 것은 비공개 메서드를 노출하지 않는 규칙 위반

<br/>

**Inquiry 생성자는** 비공개**이면서** 식별할 수 있는 동작**인 메서드의 예시**

- Inquiry 생성자를 공개한다고해서 테스트가 쉽게 깨지지는 않음
    - 실제로 클래스 API가 잘 설계된 API에 가까워지는 것임은 분명
- 생성자가 캡슐화를 지키는 데 필요한 전제 조건이 모두 포함 돼 있는지 확인하라


<br/><br/>
