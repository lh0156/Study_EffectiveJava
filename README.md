# 아이템 2: 생성자에 매개변수가 많다면 빌더를 고려하라

## 빌더 패턴 23/02/16

📎 빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기에 좋다

- 각 계층의 클래스에 관련 빌더를 멤버로 정의한다.
- 추상 클래스는 추상 빌더를, 구체 클래스는 구체 빌더를 갖게 한다.

```java
package objectcreationanddestruction;

public class ITEM2_Builder {

    static class NutritionFacts {

        private final int servingSize;
        private final int servings;
        private final int calories;
        private final int fat;
        private final int sodium;
        private final int carbohydrate;

        public int getCalories() {
            return calories;
        }

        private NutritionFacts(Builder builder) {
            this.servingSize = builder.servingSize;
            this.servings = builder.servings;
            this.calories = builder.calories;
            this.fat = builder.fat;
            this.sodium = builder.sodium;
            this.carbohydrate = builder.carbohydrate;
        }

        public static class Builder {

            private final int servingSize;
            private final int servings;

            private int calories     = 0;
            private int fat          = 0;
            private int sodium       = 0;
            private int carbohydrate = 0;

            public Builder(int servingSize, int servings) {
                this.servingSize = servingSize;
                this.servings = servings;
            }

            public Builder calories(int val) {
                calories = val; return this;
            }

            public Builder fat(int val) {
                fat = val; return this;
            }

            public Builder sodium(int val) {
                sodium = val; return this;
            }

            public Builder carbohydrate(int val) {
                carbohydrate = val; return this;
            }

            public NutritionFacts build() {
                return new NutritionFacts(this);
            }

        }
    }

}
```

<aside>
💡 **생성자나 정적 팩터리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는게 더 낫다.** 매개변수 중 필수가 아니거나 같은 타입이면 특히 더 그렇다. 빌더는 점층적 생성자보다 클라이언트 코드를 읽고 쓰기가 훨씬 간결하고, 자바빈즈보다 훨씬 안전하다.

</aside>

# 아이템 3: private 생성자나 열거 타입으로 싱글턴임을 보증하라

## 싱글턴 패턴 23/02/24

📎 싱글턴 패턴이란?

- 싱글턴이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다.
- **그런데 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다.**

📎 싱글턴을 만드는 방법

- 싱글턴을 만드는 방식은 보통 둘 중 하나다.
- 두 방식 모두 생성자는 private로 감춰두고 유일한 인스턴스에 접근할 수 있는 수단으로 public static 멤버를 하나 마련해둔다.

---

📎 첫 번째 방식: public static 멤버가 final 필드인 방식

```java
package objectcreationanddestruction;

public class ITEM3_SingleTon {

    public static final ITEM3_SingleTon INSTANCE = new ITEM3_SingleTon();

    private ITEM3_SingleTon() {
        //...
    }

    private void leaveTheBuilding() {
        //...
    }

}
```

- private 생성자는 public static final 필드인 ITEM3_SingleTon.INSTANCE를 초기화할 때 딱 한번만 호출된다.
- 예외가 있다면 자바 리플렉션으로 private 생성자에 접근할 때이다.
- 이러한 공격을 방어하려면 생성자를 수정하여 두 번째 객체가 생성되려 할 때 예외를 던지게 하면 된다.
- 장점: 해당 클래스가 싱글턴임이 API에 명백히 드러난다. public static 필드가 final이니 절대로 다른 객체를 참조할 수 없다.
- 간결하다

📎 두 번째 방식: 정적 팩터리 메서드를 public static 멤버로 제공하는 방식

```java
package objectcreationanddestruction;

public class ITEM3_SingleTon {

    private static final ITEM3_SingleTon INSTANCE2 = new ITEM3_SingleTon();

    public static ITEM3_SingleTon getInstance() {
        return INSTANCE2;
    }

}
```

- 장점: API를 바구지 않고도 싱글턴이 아니게 변경할 수 있다.
- 유일한 인스턴스를 반환하던 팩터리 메서드가 호출하는 스레드별로 다른 인스턵스를 넘겨주게 할 수 있다.
- 정적 팩터리를 제너릭 싱글턴 팩터리로 만들 수 있다.
- 정적 팩터리의 메서드 참조를 공급자(supplier)로 사용할 수 있다.
가령

```java
ITEM3_SingTon::getInstance
```

를

```java
Supplier<ITEM3_SingleTon>
```

으로 사용하는 방식이다

📎 직렬화

- 둘 중 하나의 방식으로 만든 싱글턴 클래스를 직렬화 하려면 단순히 Serializable을 구현한다고 선언하는 것만으로는 부족하다.
- 모든 인스턴스 필드를 일시적(transient)이라고 선언하도 readResolve 메서드를 제공해야 한다.
- 이렇게 하지 않으면 직렬화된 인스턴스를 역직렬화 할 때마다 새로운 인스턴스가 만들어진다.
- 두번째 방법을 예로 든다면 가짜 ITEM3_SingleTon가 만들어진다는 뜻이다.
- 가짜 인스턴스의 탄생을 예방하고 싶다면 다음의 readResolve 메서드를 추가하면 된다.

```java
package objectcreationanddestruction;

public class ITEM3_SingleTon {

    private static final ITEM3_SingleTon INSTANCE2 = new ITEM3_SingleTon();

    public static ITEM3_SingleTon getInstance() {
        return INSTANCE2;
    }

		// 가짜 ITEM3_SingleTon 생성을 예방해주는 코드
    private Object readResolve() {
        // '진짜' ITEM3_SingleTon을 반환하고, 가짜 ITEM_SingleTon은 가비지 컬렉터에 맡긴다.
        return INSTANCE2;
    }

}
```

📎 세 번째 방식: 원소가 하나인 열거 타입을 선언하는 것

```java
public enum ITEM3_SingleTon {
	INSTANCE;

	public void leaveTheBuilding() {

	}
}
```

- public 필드 방식과 비슷하지만, 더 간결하고 추가 노력 없이 직렬화할 수 있고, 심지어 아주 복잡한 직렬화 상황이나 리플렉션 공격에서도 제 2의 인스턴스가 생기는 일을 막아준다.
- **대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.**
    - 단, 만들려는 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다(열거 타입이 다른 인터페이스를 구현하도록 선언할 수는 있다)
