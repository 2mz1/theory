# 인상 깊었던 부분
 - 모든 단위 테스트는 AAA 패턴을 따라야한다.(arrange, act, assert)
 - 여러개의 검증 구절은 나눠서 진행
 - 테스트 내 if 문을 사용하지 마라
 - 실행 구절이 하나 이상일때 의심해라
 - 아래 코드의 경우 불변 위반임.
   - 잠재적 모순으로부터 코드를 보호하기 위해 캡슐화를 해야함
   
```csharp
public void Purchase_succeeds_when_enough_inventory() {
    // Arrange
    var store = new Store();
    store.AddInventory (Product .Shampoo, 10); 
    var customer = new Customer ( ) ;
    
    // Act
    bool success = customer. Purchase ( s t o r e , Product.Shampoo, 5); 
    store. RemoveInventory (success, Product.Shampoo, 5);
    
    // Assert
    Assert. True(success);
    Assert. Equal (5, store.GetInventory (Product .Shampoo));
}

```
 - 단위 테스트 명명법 -> 결국 목적은 표현력 있고 읽기 쉽게 하기 위함
   - 테스트대상메서드_시나리오_예상결과
 - 유사한 테스트는 묶어서 표현(매개변수로 달라지는 부분들)
 - Assertion!

# 궁금한거
 - 테스트 대상을 sut로 하라? 테스트 대상을 찾는 데 시간을 너무 많이 들일 필요가 없으므로??
 - 테스트 간 테스트 픽스처 재사용이 결합도를 높인다?
   - junit에선 @BeforEach 와 같은 메소드를 통해 테스트 픽스처을 항상 동일하게 유지시켜 줄 수 있음
   - 책에서 이야기 하는 부분은 test framework마다 다르게 구현되어 있어 극복할 수 있는 부분이 존재함
