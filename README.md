# 아이템 1: 생성자 대신 정적 팩토리 메소드를 고려하라

## 정적 팩토리 메소드 23/02/28

📎 해당 클래스의 인스턴스를 반환하는 정적 메서드

- boolean 기본 타입의 박싱 클래스(boxed class)인 Boolean에서 발췌한 간단한 예
- 이 메서드는 기본 탕비인 boolean 값을 받아 Boolean 객체 참조로 변환해준다.

```java
public static Boolean valueOf(boolean b) {
	return b ? Boolean.TRUE : Boolean.FALSE:
}
```

> 지금 얘기하는 정적 팩터리 메서드는 디자인 패턴[Gamma95]에서의 팩터리 메서드 (Factory Method)와 다르다. 디자인 패턴 중에는 이와 일치하는 패턴은 없다.
> 

📎 정적 팩터리 메소드와 생성자 비교

- 장점
    - 이름을 가질 수 있다.
        - 생성자에 넘기는 매개변수와 생성자 자체만으로는 반환될 객체의 특성을 제대로 설명하지 못한다.
        - 반면 정적 팩터리 메서드는 이름만 잘 지으면 반환될 객체의 특성을 쉽게 묘사할 수 있다.
        
    - 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.
        - 이 덕분에 불변 클래스(immutable class)는 인스턴스를 미리 만들어 놓거나 새로 생성한 이늣턴스를 캐싱하여 재활용 하는 식으로 불필요한 객체 생성을 피할 수 있다.
        - 대표적 예인 `Boolean.valueOf(boolean)`메서드는 객체를 아예 생성하지 않는다.
        - 따라서 (특히 생성 비용이 큰) 같은 객체가 자주 요청되는 상황이라면 성능을 상당히 끌어오려 준다.
        - 플라이웨이트 패턴(Flyweight pattern)도 이와 비슷한 기법이다.
        
    - 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.
        - 반환할 객체의 클래스를 자유롭게 선택할 수 있게 하는 **엄청난 유연성**을 선물한다
        - API를 만들 때 이 유연성을 으용하면 구현 클래스를 공개하지 않고도 그 객체를 반환할 수 있어 API를 작게 유지할 수 있다.
        
    - 입력 매개 변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.
        - 반환 타입이 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관 없다.
        - 가령 EnumSet 클래스는 public s생성자 없이 오직 정적 팩터리만 제공하는데, OpenJDK에서는 원소의 수에 따라 두 가지 하위 클래스 중 하나의 인스턴스를 반환한다.
            - 원소가 64개 이하면 원소들을 long 변수 하나로 관리하는 RegularEnumSet의 인스턴스를, 65개 이상이면 long배열로 관리하는 JumboEnumSet의 인스턴스를 반환한다.
            - 클라이언트는 이 두 클래스의 존재를 모른다.
            - RegularEnumSet이 필요 없으면 해당 릴리스를 지우면 되고 추가로 릴리스를 추가할 수도 있다. EnumSet의 하위 클래스이기만 하면 되는 것이다.
            
    - 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.
        - 이런 유연함은 서비스 제공자 프레임워크(service provider framework)를 만드는 근간이 된다.
        - 대표적인 서비스 제공자 프레임 워크로는 JDBC가 있다.
        - 서비스 제공자 프레임워크에서의 제공자(provider)는 서비스의 구현체이다.
        - 그리고 이 구현체들을 클라이언트에 제공하는 역할을 프레임워크가 통제하여, 클라이언트를 구현체로부터 분리해준다.
            - 서비스 제공자 프레임워크는 3개의 핵심 컴포넌트로 이루어진다.
            - 구현체의 동작을 정의하는 서비스 인터페이스(service Interface)
            - 제공자가 구현체를 등록할 때 사용하는 제공자 등록 API (provider registration API)
            - 클라이언트가 서비스의 인스턴스를 얻을 때 사용하는 API(service access API)가 그것이다.
        - 클라이언트는 서비스 접근 API를 사용할 때 원하는 구현체의 조건을 명시할 수 있다.
        - 조건을 명시하지 않으면 기본 구현체를 반환하거나 지원하는 구현체들을 하나씩 돌아가며 반환한다.
        - 이 서비스 접근 API가 바로 서비스 제공자 프레임워크의 근간이라고 한 ‘유연한 정적 팩터리’의 실체다.
    
- 단점
    - 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.
    
    - 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.
    

📎 정적 팩토리 메소드 명명 관례

- **from**: 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드
    - `Date d = Date.from(instant);`
- **of**: 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
    - `Set<Rank> faceCard = EnumSet.of(JACK, QUEEN, KING);`
- **valueOf**: from과 of의 더 자세한 버전
    - `BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);`
- **instance 혹은 getInstance**: (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만 같은 인스턴스임을 보장하지는 않는다.
    - `StackWalker luke = StackWalker.getInstance(options);`
- **create 혹은 newInstance**: instance 혹은 getInstance와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 보장한다.
    - `Object newArray = Array.newInstance(classObject, arryLen);`
- **getType**: getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. “Type”은 팩터리 메서드가 반환할 개체의 타입이다.
    - `FileStore fs = Files.getFilesTore(path)`
- **newType**: newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. “Type”은 팩터리 메서드가 반환할 객ㄱ체의 타입이다.
    - `BufferedReader br = Files.newBUfferedReader(path);`
- **type**: getType과 newType의 간결한 버전
    - `List<Complaint> litany = Collection.list(legacyLitary);`

> **핵심 정리**
정적 팩터리 메서드와 public 생성자는 각자의 쓰임새가 있으니 상대적인 ㅏㅈㅇ단점을 이해하고 사용하는 것이 좋다. 그렇다고 하더라도 정적 팩터리를 사용하는 게 유리한 경우가 더 많으므로 무작정 public 생성자를 제공하던 습관이 있다면 고치자.
>
<br><br>
# 아이템 2: 생성자에 매개변수가 많다면 빌더를 고려하라

## 빌더 패턴 23/02/16

📎 빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기에 좋다

- 각 계층의 클래스에 관련 빌더를 멤버로 정의한다.
- 추상 클래스는 추상 빌더를, 구체 클래스는 구체 빌더를 갖게 한다.

```java
package object_creation_and_destruction;

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

            private int calories = 0;
            private int fat = 0;
            private int sodium = 0;
            private int carbohydrate = 0;

            public Builder(int servingSize, int servings) {
                this.servingSize = servingSize;
                this.servings = servings;
            }

            public Builder calories(int val) {
                calories = val;
                return this;
            }

            public Builder fat(int val) {
                fat = val;
                return this;
            }

            public Builder sodium(int val) {
                sodium = val;
                return this;
            }

            public Builder carbohydrate(int val) {
                carbohydrate = val;
                return this;
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
<br><br>
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
package object_creation_and_destruction;

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
package object_creation_and_destruction;

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
package object_creation_and_destruction;

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


<br><br>
# 아이템 4: 인스턴스화를 막으려거든 private 생성자를 사용하라

## private 생성자 23/02/24

📎 private 생성자를 사용하라

- 정적 메서드와 정적 필드만을 담은 클래스를 만들고 싶을 때가 있다.
    - java.lang.Math와 java.util.Arrays처럼 기본 타입 값이나 배열 관련 메서드들을 모아 놓을 때
    - java.util.Collections처럼 특정 인터페이스를 구현하는 객체를 생성해주는 정적 메서드(혹은 팩토리)를 모아 놓을 때
        - Java 8부터는 이런 메서드를 인터페이스에 넣을 수 있다.
    - final 클래스와 관련한 메서드들을 모아놓을 때
        - final 클래스를 상속해서 하위 클래스에 메서드를 넣는 건 불가능하기 때문
- 이러한 유틸리티 클래스는 인스턴스로 만들어 쓰려고 설계한 게 아닌데, 생성자를 명시하지 않으면 컴파일러가 자동으로 기본 생성자를 만들어준다.
- **추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다.**
    - 하위 클래스를 만들어 인스턴스화 하면 그만이다.
    - 이를 본 사용자는 상속해서 쓰라는 뜻으로 오해할 수 있으므로 더 큰 문제이다.

- 이를 해결하기 위해 private 생성자를 추가하면 클래스의 인스턴스화를 막을 수 있다.
    - 이 방식은 상속을 불가능하게 하는 효과도 있다.
    - 모든 생성자는 명시적이든 묵시적이든 상위 클래스의 생성자를 호출하게 되는데, 이를 private으로 선언했으니 하위 클래스가 상위 클래스의 생성자에 접근할 길이 막혀버린다.
    
<br><br>
# 아이템 5: 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

## 의존 객체  23/02/27

📎 정적 유틸리티를 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다.

```java
public class SpellChecker {

	private static final Lexicon dictionary = ...;

	private SpellChecker() {}

	public static boolean isValid(String word) {...}
	public static List<String> suggestions(String typo) {...}
	
}
```

📎 싱글턴을 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다

```java
public class SpellChecker {
	private static final Lexicon dictionary = ...;

	private SpellChecker() {}
	private static SpellChecker INSTANCE = new SpellChecker(..);

	public static boolean isValid(String word) {...}
	public static List<String> suggestions(String typo) {...}
}
```

- 두 방식 모두 사전을 단 하나만 사용한다고 가정한다는 점에서 그리 훌룡해 보이지 않다.
- 실전에서는 사전이 언어별로 따로 있고 특수 어휘용 사전을 별도로 두기도 한다.
- 심지어 테스트용 사전도 필요할 수 있다.
- 사전 하나로 이 모든 쓰임에 대응할 수 있기를 바라는 건 너무 순진한 생각이다.

📎 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식

```java
public class SpellChecker {
	private static final Lexicon dictionary;

	public SpellChecker(Lexicon dictionary) {
		this.dictionary = Object.requireNotNull(dictionary);
	}

	public static boolean isValid(String word) {...}
	public static List<String> suggestions(String typo) {...}
}
```

- 자원이 몇개든 상관 없이 동작한다.
- 이 방식은 불변을 보장하여 여러 클라이언트가 의존 객체들을 안심하고 공유할 수 있다.
- 정적 팩터리, 빌더 모두에 똑같이 응용할 수 있다.

📎 생성자에 자원 팩터리를 넘겨주는 방식

- 팩터리란 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체를 말한다.
- 즉 팩터리 메서드 패턴(Factory Method pattern)을 구현한 것이다.
- Java 8에서는 Supplier<T> 인터페이스가 팩터리를 표현한 완벽한 예이다.
- Supplier<T>를 입력으로 받는 메서드는 일반적으로 한정적 와일드 카드 타입을 사용해 팩터리의 타입 매개변수를 제한해야 한다.
- 이 방식을 사용해 클라이언트는 자신이 명시한 타입의 하위 타입이라면 무엇이든 생성할 수 있는 팩터리를 넘길 수 있다.

```java
Mosaic create(Supplier<? extends Til> tileFactory) { ... }
```

- 의존 객체 주입이 유연성과 테스트 용이성을 개선해주긴 하지만, 의존성이 수천 개나 되는 큰 프로젝트에서는 코드를 어지럽게 만들기도 한다.
- Spring 같은 의존 객체 주입 프레임웤르르 사용하면 이런 어질러짐을 해결할 수 있다.

> 핵심 정리
클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다. 이 자원들을 클래스가 직접 만들게 해서도 안 된다. 대신 필요한 자원을 (혹은 그 자원을 만들어주는 팩터리를) 생성자에 (혹은 정적 팩터리나 빌더에) 넘겨주자. 의존 객체 주입이라 하는 이 기법은 클래스의 유연성, 재사용성, 테스트 용이성을 개선해준다.
>

<br><br>
# 아이템 6: 불필요한 객체 생성을 피하라

## 객체 재사용 23/02/28

📎 객체를 매번 생성하지 말 것

- 똑같은 기능의 객체를 매번 생성하기 보다는 객체 하나를 재사용하는 편이 나을 때가 많다.
- 재사용은 빠르고 세련되다.
- 특히 불변 객체는 언제든 재사용 할 수 있다.

📎 하지 말아야 할 극단적인 예

```java
String s = new String("oh my god");
```

- 이 문장은 실행될 때마다 인스턴스를 새로 만든다.
- 생성자에 넘겨진 값 자체가 이 생성자로 만들어내려는 String과 기능이 완전히 똑같다.

📎 개선된 버전

```java
String s = "oh my god";
```

- 이 코드는 새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용한다.
- 나아가 이방식을 사용하면 같은 가상 머신 안에서 이와 똑같은 문자열 리터럴을 사용하는 모든 코드가 같은 객체를 재사용 함이 보장된다
    - JLS, 3.10.5

📎 정적 팩터리 메서드 제공 불변 클래스

- 가령 `Boolean(String)` 생성자 대신 `Boolean.valudOf(String)` 팩터리 메서들르 사용 하는 것이 좋다
    - 그래서 이 생성자는 자바 9에서 사용 자제(deprecated) API로 지정되었다
- 생성자는 호출할 때마다 새로운 객체를 만들지만, 패거틸 메서드는 전혀 그렇지 않다.
- 불변 객체만이 아니라 가변 객체라 해도 사용 중에 변경되지 않을 것임을 안다면 재사용 할 수 있다.

📎 생성 비용이 비싼 객체

- 비싼 객체가 반복해서 필요하다면 캐싱하여 재사용하길 권한다.
- 객체가 비싼 객체인지를 매번 정확히 알수는 없다.
- 다음은 정규 표현식을 활용한 가장 쉬운 해법이다.

```java
static boolean isRomanNumeral(String s) {
	return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
	        + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}
```

- 이 방식의 문제는 String.matches 메서드를 사용한다는 데 있다.
- **String.matches는 정규표현식으로 문자열 형태를 확인하는 가장 쉬운 방법이지만, 성능이 중요한 상황에서 반복해 사용하기엔 적합하지 않다.**
- 이 메서드가 내부에서 만드는 정규표현식용 Pattern 인스턴스는, 한 번 쓰고 버려져서 곧바로 가비지 컬렉션 대상이 된다.
- Pattern은 입력받은 정규표현식에 해당하는 유한 상태 버신(finite state machine)을 만들기 때문에 인스턴스 생성 비용이 높다.

📎 성능을 개선하는 법 - 불변 Pattern 인스턴스 직접 생성

- 해당 성능을 개선하려면 필요한 정규표현식을 표현하는 (불변인) Pattern 인스턴스를 클래스 초기화 (정적 초기화) 과정에서 직접 생성해 캐싱해둔다.
- 이후 나중에 isRomanNumeral 메서드가 호출될 때마다 이 인스턴스를 재사용한다.

```java
//값비싼 객체를 재사용해 성능을 개선한다.
public class RomanNumerals {
    
    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})" 
                    +"(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    
    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }
    
    
}
```

- 이렇게 개선하면 isRomanNumeral이 빈번히 호출되는 상황에서 성능을 상당히 끌어올릴 수 있다.
    - 길이가 8인 문자열을 입력했을 시
        - 개선 전 = 1.1ms
        - 개선 후 = 0.17ms
- 객체가 불변이라면 재사용해도 안전함이 명백하다.
- 하지만 덜 명확하거나 심지어 직관에 반대되는 상황도 있다.

📎 어댑터(Adapter)[Gamma95]

- 어댑터의 실제 작업은 뒷단 객체에 위임하고, 자신은 제 2의 인터페이스 역할을 해주는 객체다.
- 어댑터는 뒷단 객체만 관리하면 된다. 즉, 뒷단 객체 외에는 관리할 상태가 없으므로 뒷단 객체 하나당 어댑터 하나씩만 만들어지면 충분하다.
- 예컨대 Map 인터페이스의 KeySet 메서드는 Map 객체 안의 키 전부를 담은 Set뷰를 반환한다.
- keySet을 호출할 때마다 새로운 Set 인스턴스가 만들어지리라고 생각할 수 있지만, 사실은 매번 같은 Set 인스턴스를 반환할지도 모른다. 반환된 Set 인스턴스가 일반적으로 가변이더라도 반환된 인스턴스들은 기능적으로 모두 똑같다.
- 모두가 똑같은 Map인스턴스를 대변하기 때문이다. 따라서 KeySet메소드가 매번 다른 Set을 반환할 이유가 없다.

📎 오토박싱(auto boxing)

- 불필요한 객체를 만들어내는 또 다른 예이다.
- 오토박싱은 프로그래머가 기본 타입과 박싱된 기본 타입을 섞어 ㄷ쓸 때 자동으로 상호 변환해주는 기술이다.
- 오토박싱은 기본 타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.
- 의미상으로는 별 다를 것 없지만 성능에서는 그렇지 않다.(아이템 61)
- 다음 메서드는 모든양의 정수의 총합을 구하는 메서드로, int는 충분히 크지 않으니 long을 사용해 계산하고 있다.

```jsx
//끔찍이 느리다!
private static long sum() {
    Long sum = 0L;
    
    for (long i = 0; i <= Integer.MAX_VALUE; ++i) {
        sum += i;
    }
    
    return sum;
}
```

- sum 변수를 long이 아닌 Long으로 선언해서 불필요한 Long 인스턴스가 약 231개나 만들어진 것이다. (대략, long 타입인 i가 Long 타입인 sum에 더해질 때마다)
- 교훈은 명확하다. 박싱된 기본 타입보다는 기본 타입을 사용하고, 의도치 않은 오토박싱이 숨어있지 않도록 주의하자.

📎 이번 아이템을 “객체 생성은 비싸니 피해야 한다”로 오해하면 안 된다.

- 특히나 요즘의 JVM에서는 별다른 일을 하지 않는 작은 객체를 생성하고 회수하는 일이 크게 부담되지 않는다.
- 프로그램의 명확성, 간결성, 기능을 위해서 객체를 추가로 생성하는 것이라면 일반적으로 좋은 일이다.

📎 거꾸로, 단순히 객체 생성을 피하고자 여러분만의 객체 풀(pool)을 만들지는 말자.

- 아주 무거운 객체가 아닌 이상 말이다.
- 물론 객체 풀을 만드는 게 나은 예가 있긴 하다.
- 데이터베이스 연결 같은 경우 생성 비용이 워낙 비싸니 재사용하는 편이 낫다.
- 하지만 일반적으로는 자체 객체 풀은 코드를 헷갈리게 만들고 메모리 사용량을 늘리고 성능을 떨어뜨린다.
- 이번 아이템은 방어적 복사(defensive copy)를 다루는 아이템 50과 대조적이다.
- 이번 아이템이 “기존 객체를 재사용해야 한다면 새로운 객체를 만들지 마라”라면, 아이템 50은 “새로운 객체를 만들어야 한다면 기존 객체를 재사용하지 마라”다.
- 방어적 복사가 필요한 상황에서 객체를 재사용했을 때의 피해가, 필요 없는 객체를 반복 생성했을 때의 피해보다 훨씬 크다는 사실을 기억하자.
- 이렇게 개선하면 isRomanNumeral이 빈번히 호출되는 상황에서 성능을 상당히 끌어올릴 수 있다.

# 아이템 7: 다 쓴 객체 참조를 해제하라

## 객체 참조 해제 23/03/05

📎 자바도 메모리 관리를 신경써야한다

- 잘못된 Stack 구현 예제

```java
package object_creation_and_destruction.item7_dereferencing_an_object;

import java.util.Arrays;
import java.util.EmptyStackException;

public class ITEM7_DeReferencingAnObject {

    static class Stack {

        private Object[] elements;
        private int size = 0;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;

        public Stack() {
            elements = new Object[DEFAULT_INITIAL_CAPACITY];
        }

        public void push(Object e) {
            ensureCapacity();
            elements[size++] = e;
        }

        public Object pop() {
            if (size == 0)
                throw new EmptyStackException();
            return elements[--size];
        }

        /**
         * 원소를 위한 공간을 저겅도 하나 이상 확보한다.
         * 배열 크기를 늘려야 할 때맏 대략 두 배씩 늘린다.
         */
        private void ensureCapacity() {
            if (elements.length == size)
                elements = Arrays.copyOf(elements, 2 * size + 1);
        }

    }

}
```

- 특별한 문제는 없어 보인다(제네릭 버전은 아이템 29 참조)
- 하지만 꼭꼭 숨어 있는 문제가 있다.
- 이는 바로 ‘메모리 누수’이다.
- 이 스택을 사용하는 프로그램을 오래 실행하다 보면 점차 가비지 컬렉션 활동과 메모리 사용량이 늘어나 결국 성능이 저하될 것이다. (`OutOfMemoryError` 발생 가능성)
- 메모리 누수가 일어나는 부분은 어디일까?
    - 이 코드에서는 스택이 커졌다가 줄어들었을 때 스텍에서 꺼내진 객체들을 가비지 컬렉터가 회수하지 않는다.
    - 프로그램에서 객체들을 더 이상 사용하지 않더라도 말이다.
    - 이 스택이 그 객체들의 다 쓴 참조(obsolte reference)를 여전히 가지고 있기 때문이다.
- 해법은 간단하다. 해당 참조를 다 썻을 때 null처리 (참조 해제)하면 된다.
- 단, 객체 참조를 null 처리하는 일은 예외적인 경우여야 한다.
    - Stack 클래스는 왜 메모리 누수에 취약했는가? 바로 스택이 자기 메모리를 직접 관리하기 때문이다.
    - 이 스택은 (객체 자체가 아니라 객체 참조를 담는) elements 배열로 저장소 풀을 만들어 원소들을 관리한다.
    - 배열의 활성 영역에 속한 원소들이 사용되고 비활성 영역은 쓰이지 않는다.
    - 문제는 가비지 컬렉터는 이 사실을 알 길이 없다는 데 있다.
- 캐시 역시 메모리 누수를 일으키는 주범이다.
    - 해법은 여러가지이다. 
     * 보통 캐시 엔트리의 유효 기간을 정확히 정의하기 어렵기 때문에 시간이 지날수록 엔트리의 가치를 떨어뜨리는 방식을 흔히 사용한다.
    - 캐시 외부에서 키(key)를 참조하는 동안만(값이 아니다) 엔트리가 살아 있는 캐시가 필요한 상황이라면 WeakHashMap을 사용해 캐시를 만들자.
    - 엔트리를 ScheduledThreadPoolExecutor 같은 백그라운드 스레드를 활용해서 캐시에 새 엔트리를 추가할 때 부수 작업으로 청소해준다.
        - LinkedHashMap은 remove EldestEntry 메서드를 써서 후자의 방식으로 처리한다.
- 메모리 누수의 세 번째 주범은 바로 리스너(listener)혹은 콜백(callback)이라 부르는 것이다.
    - 클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는다면 뭔가 조치해주지 않는 한 콜백은 계속 쌓이게 될 것이다.
    - 이럴 때 콜백을 약한 참조(weak reference)로 저장함녀 가비지 컬렉터가 즉시 수거해간다.
    - 예를들어 WeakHashMap에 키로 저장하면 된다

> **핵심 정리**
메모리 누수는 겉으로 잘 드러나지 않아 시스템에 수년간 잠복하는 사례도 있다 .이런 누수는 철저한 코드 리뷰나 힙 프로파일러 같은 디버깅 도구를 동원해야만 발견되기도 한다. 그래서 이런 종류의 문제는 예방법을 익혀두는 것이 매우 중요하다.
>

# 아이템 8: finalizer와 clenaer 사용을 피하라

## 객체 소멸자 23/03/07

📎 자바는 두 가지 객체 소멸자를 제공한다.

- 그 중 **finalizer는 예측할 수 없고, 상황에 따라 위험할 수 있어 일반적으로 불필요하다.**
    - 나름의 쓰임새가 몇 가지 있긴 하지만 기본적으로 ‘쓰지 말아야’ 한다.
    - 그래서 Java 9에서는 finalizer를 사용 자제 (deprecated) API로 지정하고 clenaer를 그 대안으로 소개헀다. (하지만 자바 라이브러리에서도 finalizer를 여전히 사용한다.)
- **cleaner는 finalizer보다는 덜 위험하지만, 여전히 예측할 수 없고, 느리고, 일반적으로 불필요하다.**

📎 C++의 파괴자와 객체 소멸자

- C++의 파괴자(destructror)와 자바의 finalizer와 cleaner와는 다른 개념이다.
- C++에서의 파괴자는 (생성자의 꼭 필요한 대척점으로) 특정 객체와 관련된 자원을 회수하는 보편적인 방법이다.
- C++의 파괴자는 비메로리 자원을 회수하는 용도로도 쓰인다, 하지만 자바에서는 try-with-resource와 try-finally를 사용해 해결한다.

📎finalizer와 cleaner는 즉시 수행된다는 보장이 없다.

- **즉, finalizer와 cleaner로는 제때 실행되어야 하는 작업은 절대 할 수 없다.**
- 굼뜬 finalizer 처리는 현업에서도 실제로 문제를 일으킨다.
    - OutOfMemoryError를 내며 죽는 Application을 디버깅한 결과 어플리케이션이 죽는 시점에 그래픽스 객체 수천개가 finalizer 대기열에서 회수대기만을 기다리고 있었다.
    - finalizer 스레드는 다른 애플리케이션 스레드보다 우선 순위가 낮아서 실행될 기회를 제대로 얻지 못한 것이다.
- 따라서 프로그램 생애 주기와 상관없는, 상태를 영구적으로 수정하는 작업에서는 절대 finalizer나 clenaer에 의존해서는 안된다.

📎성능 문제도 동반한다. 

- try-with-resource를 사용할 시 12ns가 걸린것이 finalizer를 사용하면 550ns가 걸렸다.

📎finalizer를 사용한 클래스는 finalizer 공격에 노출되어 심각한 보안 문제를 일으킬수도 있다.

- finalizer 공격원리는 간단하다. 생성자나 직렬화 과정에서 예외가 발생하면, 이 생성되다 만 객체에서 악의적인 하위 클래스의 finalizer가 수행될 수 있게 된다.

📎finalizer의 적절한 쓰임새

- 자원의 소유자가 close 메서드를 호출하지 않는 것에 대비한 안전망 역할
- 네이티브 피어(native peer)와 연결된 객체
    - 네이티브 피어란 일반 자바 객체가 네이티브 메서드를 통해 기능을 위임한 네이티브 객체를 말한다.
    - 네이티브 피어는 자바 객체가 아니니 가비지 컬렉터는 그 존재를 알지 못한다.
    - 그 결과 자바 피어를 회수 할 때 네이티브 객체까지 회수하지 못한다.
    - cleaner나 finalizer가 나서서 처리하기에 적당한 작업이다.
    - 단, 성능 저하를 감당할 수 있고 네이티브 피어가 심각한 자원을 가지고 있지 않을 때에만 해당된다.
    - 그렇지 않을 경우라면 앞서 설명한 close 메서드를 사용해야 한다.

> **핵심 정리**
cleaner(자바 8까지는 finalizer)는 안전망 역할이나 중요하지 않은 네이티브 자원 회수 용으로만 사용하자. 물론 이런 경우라도 불확실성과 성능 저하에 주의해야 한다.
>

# 아이템 9: try-finally보다는 try-with-resource를 사용하라

## try-with-reousrce 23/03/07

📎 자원 닫기는 클라이언트가 놓치기 쉽다.

- 이런 자원 중 상당수가 안전망으로 finalizer를 활용하고는 있지만 finalizer는 그리 믿을만하지 못하다
- try-finally - 더 이상 자원을 회수하는 최선의 방책이 아니다

```java
//try-finally - 더 이상 자원을 회수하는 최선의 방책이 아니다
static String firstLineOfFile(String path) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        return br.readLine();
    } finally {
        br.close();
    }
}
```

- 나쁘지 않지만 자원이 추가된다면 아주 지저분해진다

```java
//자원이 둘 이상이면 try-finally 방식은 너무 지저분하다!
static void copy (String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src);
    int BUFFERED_SIZE = 1024;

    try {
        OutputStream out = new FileOutputStream(dst);
        try {
            byte[] buf = new byte[BUFFERED_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        } finally {
            out.close();
        }
    } finally {
        in.close();
    }
}
```

- 또 try-finally 문을 제대로 사용한 경우에도 미묘한 결점이 있다

```java
static String firstLineOfFileException(String path) throws IOException {
    try {
        //readLine();
        throw new IllegalArgumentException();
    } finally {
        //close();
        throw new NullPointerException();
    }
}
```

<aside>
💡

</aside>

- firstLineOfFile() 메소드를 다시 한 번 살펴보면, 시스템 문제로 인해 예외가 try와 finally 모두 발생할 수가 있다. 이 때, try-finally는 두번째 예외(finally - close())가 첫번째 예외(try - readLine())를 덮어버리게 된다.
- **실제로 스택 내역에 close()의 예외는 등장하지 않고, 두번째 예외만 등장하게 된다.**

- try-with-resource를 catch 절과 함께 쓰는 모습

```java
static String firstLineOfFileWithCatch(String path, String defaultVal) {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    } catch (IOException e) {
        return defaultVal;
    }
}
```

> **핵심 정리**
꼭 회수해야 하는 자원을 다룰 때는 try-finall 말고, try-with-resource를 사용하자. 예외는 없다. 코드는 더 짧고 분명해지고, 만들어지는 예외 정보도 훨씬 유용하다. try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도, try-with-resoruce로는 정확하고 쉽게 자원을 회수할 수 있다.
>

# 아이템 10: equals는 일반 규약을 지켜 재정의하라

## 일반 규약 23/03/08

📎 아래에 해당되는 상황이라면 재정의하지 않는것이 최선

- **각 인스턴스가 본질적으로 고유하다**.
    - 값을 표현하는 게 아니라 동작하는 개체를 표현하는 클래스가 여기 해당한다.
    - Thread가 좋은 예이다. Object의 equals 메서드는 이러한 클래스에 딱 맞게 구현되었다
- **인스턴스의 ‘논리적 동치성(logical equality)’을 검사할 일이 없다.**
    - 예컨대 java.util.regex.Pattern은 eqauls를 재정의해서 두 Pattern의 인스턴스가 같은 정규 표현식을 나타내는지를 검사하는, 즉 논리적 동치성을 검사하는 방법도 있다.
    - 하지만 설계자는 클라이언트가 이 방식을 원하지 않거나 애초에 필요하지 않다고 판단할 수도 있다.
    - 설계자가 후자로 판단했다면(필요하지 않다고 판단했다면) `Object`의 기본 `equals`만으로 해결된다.
- 상위 클래스에서 재정의한 `equals`가 하위 클래스에도 딱 들어맞는다.
    - 예컨대 대부분의 `Set` 구현체는 `AbstractSet`이 구현한 `equals`를 상속받아 쓰고, `List` 구현체들은 `AbstractList`로부터, `Map` 구현체들은 `AbstractMap`으로부터 상속받아 그대로 쓴다.
- 클래스가 `private`이거나 `packge-private`이고 `equals` 메서드를 호출할 일이 없다.
    - 여러분이 위험을 철저히 회피하는 스타일이라 `eqauls`가 실수로라도 호출되는 걸 막고 싶다면 다음처럼 구현해두자.
    
    ```java
    //호출 금지
    @Override
    public boolean equals(Object o) {
        throw new AssertionError();
    }
    ```
    

📎 `equals`를 재정의해야 하는 상황

- 객체 식별성(object identity, 두 객체가 물리적으로 같은가)이 아니라 논리적 동치성을 확인해야 하는데, 상위 클래스의 `equals`가 논리적 동치성을 비교하도록 재정의되지 않았을 때다.
- 주로 값 클래스들이 여기 해당한다.
    - 값 클래스란 `Integer`와 `String`처럼 값을 표현하는 클래스를 말한다.
- 두 값 객체를 `equals`로 비교하는 프로그래머는 객체가 같은지가 아니라 값이 같은지를 알고 싶어 할 것이다.
    - `euqlas`가 논리적 동치성을 확인하도록 재정의해두면, 그 인스턴스는 값을 비교하길 원하는 프로그래머의 기대에 부응함은 물론 `Map`의 키와 `Set`의 원소로 사용할 수 있게 된다.
- 값 클래스라 해도, 값이 같은 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스(아이템 1)라면 `equals`를 재정의하지 않아도 된다.
    - `Enum`(아이템 34)도 여기 해당한다.
- 이런 클래스에서는 어차피 논리적으로 같은 인스턴스가 2개 이상 만들어지지 않으니 논리적 동치성과 개체 식별성이 사실상 똑같은 의미가 된다.
- 따라서 `Object` 의 `equals`가 논리적 동치성까지 확인해준다고 볼 수 있다.

📎 equals 메서드 일반 규약 (Object 명세에 적혀있는 규약)

> **반사성(reflexivity)**: `null`이 아닌 모든 참조 값 x에 대해, `x.equals(x)`는 `true`이다.
> 
> - x = x
> 
> **대칭성(symmetry)**: `null`이 아닌 모든 참조 값 x, y에 대해, `x.equals(y)`가 `true`면 `y.equals(x)`도 `true`다.
> 
> - `x = y`이면 `y = x`
> 
> **추이성(transitivity)**: `null`이 아닌 모든 참조값 x, y, z에 대해, `x.equals(y)`가 `true`이고 `y(equals)(z)`도 `true`면 `x.equals(z)`도 `true`다.
> 
> - x = y이고 y=z이면 x=z
> 
> **일관성(consistency)**: `null`이 아닌 모든 참조값 x,y에 대해 `x.equals(y)`를 반복해서 호출하면 항상 `true`를 반환하거나 항상 `false`를 반환한다.
> 
> - x = y이면 항상 x = y
> 
> **null-아님**: `null`이 아닌 모든 참조 값 x에 대해, `x.equals(null)`은 `false`다
> 
> - null이 아닌 값 ≠ null
> 
> **반사성(reflexivity)**: `null`이 아닌 모든 참조 값 x에 대해, `x.equals(x)`는 `true`이다.
> 
> - x = x
> 
> **대칭성(symmetry)**: `null`이 아닌 모든 참조 값 x, y에 대해, `x.equals(y)`가 `true`면 `y.equals(x)`도 `true`다.
> 
> - x = y이면 y = x
> 
> **추이성(transitivity)**: `null`이 아닌 모든 참조값 x, y, z에 대해, `x.equals(y)`가 `true`이고 `y(equals)(z)`도 `true`면 `x.equals(z)`도 `true`다.
> 
> - x = y이고 y=z이면 x=z
> 
> **일관성(consistency)**: `null`이 아닌 모든 참조값 x,y에 대해 `x.equals(y)`를 반복해서 호출하면 항상 `true`를 반환하거나 항상 `false`를 반환한다.
> 
> - x = y이면 항상 x = y
> 
> **null-아님**: `null`이 아닌 모든 참조 값 x에 대해, `x.equals(null)`은 `false`다
> 
> - null이 아닌 값 ≠ null

📎 `Object` 명세에서 말하는 동치 관계

- 집합을 서로 같은 원소들로 이뤄진 부분집합으로 나누는 연산
- 이 부분 집합을 동치류(equivalence class: 동치 클래스)라 한다.
- `equals` 메서드가 쓸모 있으려면 모든 원소가 같은 동치류에 속한 어떤 원소와도 서로 교환할 수 있어야 한다.

📎 동치 관계를 지키기 위한 다섯 요건

- **반사성: 객체는 자기 자신과 같아야 한다.**
    - 이 요건을 어긴 클래스의 인스턴스를 컬렉션에 넣은 다음 `contains` 메서드를 호출하면 방금 넣은 인슽너스가 없다고 답할 것이다.
- **대칭성: 두 객체는 서로에 대한 동치 여부에 똑같이 답해야한다.**
    - 반사성 요건과 달리 대칭성 요건은 자칫하면 어길 수 있어 보인다. 대소문자를 구별하지 않는 문자열을 구현한 다음 클래스를 예로 살펴보자. 이 클래스에서 `toStirng` 메서드는 원본 문자열의 대소문자를 그대로 돌려주지만 equals는 대소문자를 무시한다.
    
    ```java
    public class CaseInsensitiveString {
    
        private final String s;
    
        public CaseInsensitiveString(String s) {
            this.s = Object.requireNotNull(s);
        }
        
        @Override
        public boolean equals(Object o) {
            
            if (o instanceof CaseInsensitiveString)
                return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
            if (o instanceof String)
                return s.equalsIgnoreCase((String) o);
            return false;
            
        }
    }
    ```
    
    - CaseInsensitiveString의 equals는 순진하게 일반 문자열과도 비교를 시도한다. 다음처럼 CaseInsensitiveString과 일반 String 객체가 하나씩 있다고 해보자
    - `CaseInsentiveString cis = new CaseInsensitiveString(””Polish);`
    `String s = “polish”;`
    - 예상할 수 있듯 `cis.equals(s)`는 `true`를 반환한다. 문제는 `CaseINsensitive` `String`의 `equals`는 일반 `String`을 알고 있지만 `String`의 `equals`는 `CaseINsensitiveString`의 존재를 모른다는 데 있다.
    - 따라서 `s.equals(cis)`는 `false`를 반환하여, 대칭성을 명백히 위반한다.
    - 이번에는 `CaseInsensitiveStirng`을 컬렉션에 넣어보자
    
    ```java
    List<CaseInsensitiveString> list = new ArrayList<>();
    list.add(cis);
    ```
    
    - 이 다음에 list.contains(s)를 호출하면 어떤 결과가 나올까? JDK 버전이 바뀌거나 JDK에 따라 값이 바뀔수 있고 런타임 예외를 던질 수도 있다.
    - **equals 규약을 어기면 그 객체를 사용하는 다른 객체들이 어떻게 반응할지 알 수 있다**
    - 이 문제를 버리려면 `CaseInsensitivedString`의 `equals`를 `String`과도 연동하겠다는 허황된 꿈을 버려야 한다. 그 결과 `equals`는 다음처럼 간단한 모습으로 바뀐다.
    
    ```java
    @Override
    public boolean equals(Object o) {
        return o instanceof CaseInsensitiveString && ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
    }
    ```
    

- **추이성: 첫 번째 객체와 두 번째 객체가 같고 두 번째 객체와 세 번째 객체가 같다면 첫 번쨰 객체와 세 번쨰 객체도 같다**
    - 간단하지만 자칫 어기기 쉽다.
    - 상위 클래스에는 없는 새로운 필드를 하위 클래스에 추가하는 상황을 생각해보자
    - 간단히 2차원에서의 점을 표현하는 예를 들어본다.
    
    ```java
    public class Point {
        private final int x;
        private final int y;
    
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Point))
                return false;
            Point p = (Point) o;
            return p.x == x && p.y == y;
        }
        
        // 나머지 코드 생략
    }
    ```
    
    - 이제 이 클래스를 확정해서 점에 색상을 더해보자
    
    ```java
    package common_methods_of_all_objects.item10_equals;
    
    public class ColorPoint extends Point{
    
        private final Color color;
    
        public ColorPoint(int x, int y, Color color) {
            super(x, y);
            this.color = color;
        }
    
        //임의로 추가
        static class Color {
    
        }
    }
    ```
    
    - `equals` 메서드는 어떻게 해야 할까? 그대로 둔다면 `Point`의 구현이 상속되어 색상 정보는 무시한 채 비교를 수행한다.
    - `equals` 규악을 어긴 것은 아니지만, 중요한 정보를 놓치게 되니 받아들일 수 없는 상황이다.
    - 다음 코드처럼 비교 대상이 또 다른 `ColorPoint`이고 위치와 색상이 같을 때만 `ture`를 반환하는 `equals`를 생각해보자
    
    ```java
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;
        return super.equals(o) && ((ColorPoint) o).color == color;
    }
    ```
    
    - 이 메서드는 일반 `Point`를 `Color`에 비교한 결과와 그 둘을 바꿔 비교한 결과가 다를 수 있다. `point`의 `equals`는 색상을 무시하고, `ColorPoint`의 `equals`는 입력 매개변수의 클래스 종류가 다르다며 매번 `false`만 반환할 것이다. 각각의 인스턴스를 하나씩 만들어 실제로 동작하는 모습을 확인해보자.
    
    ```java
    public static void main(String[] args) {
          Point p = new Point(1, 2);
          ColorPoint cp = new ColorPoint(1, 2, Color.red);
    
          System.out.println(p.equals(cp));//true
          System.out.println(cp.equals(p));//false
      }
    ```
    
    - 이제 `p.equals(cp)`는 `true`를, `cp.equals(p)`는 `false`를 반환한다. `ColorPoint.equals`가 `Point`와 비교할 때는 색상을 무시하도록 하면 해결될까?
    
    ```java
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;
        
        // o가 일반 Point면 색상을 무시하고 비교한다
        if (!(o instanceof ColorPoint))
            return o.equals(this);
            
        // o가 ColorPoint면 색상까지 비교한다.
        return super.equals(o) && ((ColorPoint) o).color == color;
    }
    ```
    
    - 이 방식은 대칭성은 지켜주지만, 추이성은 깨버린다.
    
    ```java
    ColorPoint cp1 = new ColorPoint(1, 2, Color.RED);
    Point p2 = new Point(1, 2);
    ColorPoint cp2 = new ColorPoint(1, 2, Color.BLUE);
    
    System.out.println(cp1.equals(p2)); //true
    System.out.println(p2.equals(cp2)); //true
    System.out.println(cp1.equals(cp2));//false
    ```
    
    - `cp1`과 `p2`, `p2`와 `cp2`비교에서는 색상을 무시했지만 `cp1`과 `cp2`에서는 색상까지 고려했기 때문이다.
    - 또한 이 방식은 무한 재귀에 빠질 위험도 잇다. Point의 또 다른 하위 클래스로 SmellPoint를 만들고, equals는 같은 방식으로 구현했다고 해보자, 그런 다음 `myColorPoint.equals(mySmellPoint)`를 호출하면 `StackOverflowError`를 일으킨다.
    - 그럼 해법은 무엇인가? 사실 이 현상은 모든 객체 지향 언어의 동치관계에서 나타나는 근본적인 문제다. **구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 존재하지 않는다.** 객체 지향적 추상화의 이점을 포기하지 않는 한은 말이다.
    - 이 말은 얼핏, `equals` 안의 `instanceof` 검사를 `getClass` 검사로 바꾸면 규약도 지키고 값도 추가하면서 구체 클래스를 상속할 수 있다는 뜻으로 들린다.
    
    ```java
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass())
            return false;
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
    ```
    
    - 이번 `equals`는 같은 구현 클래스의 객체와 비교할 때만 `ture`를 반환한다. 괜찮아 보이지만 실제로 활용할 수는 없다.
    - `Point`의 하위 클래스는 정의상 여전히 `Point`이므로 어디서든 `Point`로써 활용될 수 있어야 한다. 그런데 이방식에서는 그렇지 못하다. 예를 들어 주어진 점이 (반지름이 1인) 단위 원 안에 있는지를 판별하는 메서드가 필요하다고 해보자. 다음은 이를 구현한 코드다.
    
    ```java
    //단위 원 안의 모든 점을 포함하도록 unitCircle 초기화한다.
    //of 메소드는 jdk 1.9부 추가된 정적 팩토리 메소드이다.
    //Set.of 메소드는 인자로 전달된 요소들이 모두 null이 아니어야 하며, 최대 10개까지만 전달할 수 있다.
    //이 메소드는 불변 집합을 생성하기 때문에 반환된 집합에 대해 추가적인 수정 작업을 수행할 수 없습니다.
    private static final Set<Point> unitCircle = Set.of(
            new Point(1, 0), new Point(0, 1),
            new Point(-1, 0), new Point(0, -1));
    
    public static boolean onUnitCircle(Point p) {
        return unitCircle.contains(p);
    }
    ```
    
    - 이 기능을 구현하는 가장 빠른 방법은 아니지만, 어쨋든 동작은 한다. 이제 값을 추가하지 않는 방식으로 Point를 확장하겠다.
    - 만들어진 인스턴스의 개수를 생성자에서 세보도록 하자.
    
    ```java
    public class CounterPointer extends Point {
    
        private static final AtomicInteger counter = new AtomicInteger();
    
        public CounterPointer(int x, int y) {
            super(x, y);
            counter.incrementAndGet();
        }
        
        public static int numberCreate() {
            return counter.get();
        }
    
    }
    ```
    
    > **`AtomicInteger`**은 자바에서 제공하는 클래스 중 하나로, 멀티 스레드 환경에서 안전하게 정수 값을 증감시키기 위한 도구이다
    
    **`AtomicInteger`** 객체는 내부적으로 정수 값을 갖고 있으며, 여러 스레드에서 동시에 접근해도 안전하게 값을 증감시킬 수 있다. 내부적으로 CAS(compare-and-swap) 연산을 사용하여 값을 증감시키기 때문에 안전한 동시성 제어가 가능하다.
    > 
    > 
    > CAS(compare-and-swap) 연산은 여러 스레드에서 동시에 공유된 변수에 접근하면서 값의 일관성과 원자성을 유지하기 위해 사용되는 연산이다.
    > 
    > CAS 연산은 변수의 예상 값(expected value)과 실제 값(actual value)을 비교하여 두 값이 일치하면 새로운 값(new value)으로 변수의 값을 변경하는 작업을 수행한다. CAS 연산은 다음과 같은 형태로 구현된다.
    > 
    > ```java
    > public class AtomicInteger {
    > 
    > 	private volatile int value;
    > 	
    > 	public int incrementAndGet() {
    > 	    int expectedValue = value;
    > 	    int newValue = expectedValue + 1;
    > 	    while (!compareAndSwap(expectedValue, newValue)) {
    > 	        expectedValue = value;
    > 	        newValue = expectedValue + 1;
    > 	    }
    > 	    return newValue;
    > 	}
    > 	
    > 	private synchronized boolean compareAndSwap(int expectedValue, int newValue) {
    > 	  if (value == expectedValue) {
    > 	      value = newValue;
    > 	      return true;
    > 	  }
    > 	  return false;
    > 	}
    > 
    > }
    > ```
    > 
    
    - 리스코스 치환 원칙(Liskov subsitution principle)에 따르면, 어떤 타입에 있어 중요한 속성이라면 그 하위 타입에서도 마찬가지로 중요하다.
    - 따라서 그 타입의 모든 메서드가 하위 타입에서도 똑같이 잘 작동해야 한다. [Liskov87]
    - 이는 앞서의 “`Point`의 하위 클래스는 정의상 여전히 `Point`이므로 어디서든 `Point`로써 활용될 수 있어야 한다”라는 말을 격식 있게 표현한 말이다.
    - 그런데 `CounterPoint`의 인스턴스를 `onUnitCircle` 메서드에 넘기면 어떻게 될까? `Point` 클래스의 `equals`를 `getClass`를 사용해 작성했다면 `onUnitCircle`은 `false`를 반환할 것이다. `CounterPoint` 인스턴스의 x, y 값과는 무관하게 말이다. 왜 그럴까?
    - 원인은 컬렉션 구현체에서 주어진 원소를 담고 있는지를 확인하는 방법에 있다.
    - `onUnitCircle`에서 사용한 `Set`을 포함하여 대부분의 컬렉션은 이 작업에 `equals` 메서드를 이용하는데, `CounterPoint`의 인스턴스는 어떤 Point와도 같을 수 없기 때문이다.
    - 반면, `Point`의 `equals`를 `instanceof` 기반으로 올바로 구현했다면 `CounterPoint` 인스턴스를 건네줘도 `onUnitCircle` 메서드가 제대로 동작할 것이다.
    - 구체 클래스의 하위 클래스에서 값을 추가할 방법은 없지만 괜찮은 우회 방법이 하나 있다. **“상속 대신 컴포지션을 사용하라”**는 아이템 18의 조언을 따르면 된다. `Point`를 상속하는 대신 `Point`를 `ColorPoint`의 `private` 필드로 두고 `ColorPoint`와 같은 위치의 일반 `Point`를 반환하는 뷰(view)메서드(아이템6)를 `public`으로 추가하는 식이다.
    
    ```java
    public class ColorPoint {
    	private final Point point;
    	private final Color color;
    
    	public ColorPoint(int x, int y, Color color) {
    		point = new Point(x, y);
    		this.color = Object.requireNonNull(color);
    	}
    
    	/**
    	* 이 ColorPoint의 Point 뷰를 반환한다.
    	*/
    	public Point asPoint() {
    		return point;
    	}
    
    	@Override public boolean equals(Object o) {
    		if (!(o instanceof ColorPoint))
    			return false;
    		ColorPoint cp (ColorPoint) o;
    		return cp.point.equals(point) && cp.color.equals(color);
    	}
    	// 나머지 코드 생략
    }
    ```
    
    - 자바 라이브러리에도 구체 클래스를 확장해 값을 추가한 클래스가 종종 있다.
    - 한 가지 예로 java.sql.Timestamp는 java.util.Date를 확장한 후 `nanoseconds`필드를 추가했다. 그 결과 Timestamp의 equals는 대칭성을 위배하며, Date 객체와 한 컬렉에 넣거나 서로 섞어 사용하면 엉뚱하게 작동할 수 있다. 그래서 Timestamp의 API 설명에는 Date와 섞어 쓸 때의 주의사항을 언급하고 있다.
    - 둘을 명확히 분리해 사용하는 한 문제될 것은 없지만, 섞이지 않도록 보장해줄 수단은 없다. 자칫 실수하면 디버깅하기 어려운 이상한 오류를 경험할 수 있으니 주의하자. `Timestamp`를 이렇게 설계한 것은 실수니 절대 따라해서는 안된다.
    
    > 추상 클래스의 하위 클래스에서라면 `equals` 규약을 지키면서도 값을 추가할 수 있다. “태그 달린 클래스보다는 클래스 계층구조를 활용하라”는 아이템 23의 조언을 따르는 클래스 계층구조에서는 아주 중요한 사실이다.
    
    예컨대 아무런 값을 갖지 않는 추상클래스인 Shape를 위에 두고, 이를 확장하여 `redius` 필드를 추가한 `Circle` 클래스와, `length`와 `width` 필드를 추가한 `Rectangle` 클래스를 만들 수 있다.
    
    상위 클래스를 직접 인스턴스로 만드는 게 불가능 하다면 지금까지 이야기한 문제들은 일어나지 않는다.
    > 
    
- **일관성: 두 객체가 같다면 (어느 하나 혹은 두 객체 모두가 수정되지 않는 한) 앞으로도 영원히 같아야 한다는 뜻이다.**
    - 가변 객체는 비교 시점에 따라 서로 다를 수도 혹은 같을 수도 있는 반면, 불변 객체는 한번 다르면 끝까지 달라야 한다.
    - 클래스를 작성할 때는 불변 클래스로 만드는게 나을 지를 심사숙고하자(아이템 17).
    - 불변 클래스로 만들기로 했다면 `equals`가 한번 같다고 한 객체와는 영원히 같다고 답하고, 다르다고 한 객체와는 영원히 다르다고 답하도록 만들어야 한다.
    - 클래스가 불변이든 가변이든 **`equals`의 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안 된다.**
    - 이 제약을 어기면 일관성 조건을 만족시키기가 아주 어렵다. 예컨대 java.net.URL의 equals는 주어진 URL과 매핑된 호스트의 IP 주소를 이용해 비교한다.
    - 호스트 이름을 IP 주소로 바꾸려면 네트워크를 통해야 하는데, 그 결과가 항상 같다고 보장할 수 없다. 이는 URL의 equals가 일반 규약을 어기게 하고, 실무에서도 종종 문제를 일으킨다.
    - URL의 equals를 이렇게 구현한 것은 커다란 실수였으니 절대 따라 해서는 안 된다. 하위 호환성이 발목을 잡아 잘못된 동작을 바로잡을 수도 없다.
    - 이런 문제를 피하려면 equasl는 항시 메모리에 존재하는 객체만을 사용한 결정적(deterministic) 계산만 수행해야 한다.
- **null-아님: null-아님은 이름처럼 모든 객체가 null과 같지 않아야 한다는 뜻이다.**
    
    ```java
    // 명시적 null 검사 - 필요 없다!
    @Override public boolena equals(Object o) {
    	if (o == null) {
    		return fasle;
    	}
    }
    ```
    
    - 이러한 검사는 필요치 않다. 동치성을 검사하려면 equals는 건네받은 객체를 적절히 형변환한 후 필수 필드들의 값을 알아내야 한다. 그러려면 형변환에 앞서 instanceof 연산자로 입력 매개변수가 올바른  타입인지 검사해야 한다.
    
    ```java
    // 묵시적 null 검사 - 이쪽이 낫다
    @Override public boolean equals(Object o) {
    	if (!(o instanceof MyType))
    		return false;
    	MyType mt = (MyType) 0;
    	// ...
    }
    ```
    
    - equals가 타입을 확인하지 않으면 잘못된 타입이 인수로 주어졌을 때 `ClassCastException`을 던져서 일반 규약을 위배하게 된다.
    - 그런데 `instanceof`는 (두 번쨰 피연산자와 무관하게) 첫 번째 피연산자가 null이면 `fasle`를 반환한다(JLS, 15.20.2) 따라서 입력이 `null`이면 타입 확인 단계에서 `false`를 반환하기 때문에 null 검사를 명시적으로 하지 않아도 된다.
- **지금까지 내용 종합, 양질의 equals 메서드 구현 방법**
    1. == 연산자를 이용해 입력이 자기 자신의 참조인지 확인한다. 자기 자신이면 true를 반환한다. 이는 단순한 성능 최적화용으로, 비교 작업이 복잡한 상황일 때 값어치를 할 것이다.
    2. `instanceof` 연산자로 입력이 올바른 타입인지 확인한다. 그렇지 않다면 false를 반환한다. 이때의 올바른 타입은 equals가 정의된 클래스인 것이 보통이지만, 가끔은 그 클래스가 구현한 특정 인터페이스가 될 수도 있다. 어떤 인터페이스는 자신을 구현한 (서로 다른) 클래스끼리도 비교할 수 있도록 equals 규약을 수정하기도 한다. 이런 인터페이스를 구현한 클래스라면 equals에서 (클래스가 아닌) 해당 인터페이스를 사용해야 한다. Set, List, Map, Map.Entry등의 컬렉렉션 인터페이스들이 여기 해당한다.
        1. 아래의 코드는 HashMap을 사용하여 Map.Entry 객체를 생성하고 비교하는 예시입니다. HashMap에서는 equals 메소드가 Map.Entry를 이용하여 구현되어 있습니다.
        
        ```java
        import java.util.*;
        
        public class MapExample {
            public static void main(String[] args) {
                Map<String, Integer> map = new HashMap<>();
                map.put("Alice", 25);
                map.put("Bob", 30);
        
                Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>("Alice", 25);
        
                // entry가 Map.Entry 인터페이스를 구현했으므로 instanceof 연산자를 사용할 수 있습니다.
                if (entry instanceof Map.Entry) {
                    System.out.println("entry is an instance of Map.Entry");
                }
        
                // map에 포함된 키-값 쌍과 entry가 같은지 비교합니다.
                if (map.entrySet().contains(entry)) {
                    System.out.println("map contains the entry");
                }
            }
        }
        
        ```
        
        위 코드에서 entry는 HashMap에 저장된 키-값 쌍 중 하나인 "Alice"와 25를 가지고 있습니다. 이 때, entry 객체가 Map.Entry 인터페이스를 구현하고 있는지 instanceof 연산자를 사용하여 확인합니다. 이후, map.entrySet().contains(entry)를 사용하여 HashMap에 entry가 포함되어 있는지 비교합니다. 이때, HashMap은 Map.Entry 객체의 equals 메소드를 이용하여 비교합니다. 따라서, Map.Entry 인터페이스를 구현한 클래스는 equals 메소드에서 해당 인터페이스를 사용해야 합니다.
        
    3. 입력을 올바른 타입으로 형변환한다. 앞서 2번에서 instnaceof 검사를 했기 때문에 이 단계는 100% 성공한다.
    4. 입력 객체와 자기 자신의 대응되는 ‘핵심’필드들이 모두 일치하는지 하나씩 검사한다. 모든 필드가 일치하면 true를, 하나라도 다르면 false를 반환한다. 2단계에서 인터페이슬르 사용했다면 입력의 필드 값을 가져올 때도 그 인터페이스의 메서들르 사용해야 한다. 타입이 클래스라면 (접근 권한에 따라) 해당 필드에 직접 접근할 수도 있다.
- float와 double을 제외한 기본 타입 필드는 == 연산자로 비교하고, 참조 타입 필드는 각각의 equals 메서드로, falot와 dobule 필드는 각각 정적 메서드인 Float.compare(flaot, flaot)와 Double.compare(double, double)로 비교한다.
- float와 double을 특별 취급하는 이유는 Float.Nan, -0.0f, 특수한 부동소수 값등을 다뤄야 하기 때문이다. 자세한 설명은 [JLS 15.21.1]이나 Float.equals의 API 문서를 참고하자.
    
    [https://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html](https://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html)
    
    > **15.21.1. Numerical Equality Operators `==` and `!=`**
    > 
    > 
    > If the operands of an equality operator are both of numeric type, or one is of numeric type and the other is convertible ([§5.1.8](https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.1.8)) to numeric type, binary numeric promotion is performed on the operands ([§5.6.2](https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2)).
    > 
    > *Note that binary numeric promotion performs value set conversion ([§5.1.13](https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.1.13)) and may perform unboxing conversion ([§5.1.8](https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.1.8)).*
    > 
    > If the promoted type of the operands is `int` or `long`, then an integer equality test is performed.
    > 
    > If the promoted type is `float` or `double`, then a floating-point equality test is performed.
    > 
    > Comparison is carried out accurately on floating-point values, no matter what value sets their representing values were drawn from.
    > 
    > Floating-point equality testing is performed in accordance with the rules of the IEEE 754 standard:
    > 
    > - If either operand is NaN, then the result of `==` is `false` but the result of `!=` is `true`.
    >     
    >     Indeed, the test `x!=x` is `true` if and only if the value of `x` is NaN.
    >     
    >     *The methods `Float.isNaN` and `Double.isNaN` may also be used to test whether a value is NaN.*
    >     
    > - Positive zero and negative zero are considered equal.
    >     
    >     *For example, `-0.0==0.0` is `true`.*
    >     
    > - Otherwise, two distinct floating-point values are considered unequal by the equality operators.
    >     
    >     In particular, there is one value representing positive infinity and one value representing negative infinity; each compares equal only to itself, and each compares unequal to all other values.
    >     
    > 
    > Subject to these considerations for floating-point numbers, the following rules then hold for integer operands or for floating-point operands other than NaN:
    > 
    > - The value produced by the `==` operator is `true` if the value of the left-hand operand is equal to the value of the right-hand operand; otherwise, the result is `false`.
    > - The value produced by the `!=` operator is `true` if the value of the left-hand operand is not equal to the value of the right-hand operand; otherwise, the result is `false`.
- Float.equals와 Double.equals 메서드를 대신 사용할 수도 있지만, 이 메서드들은 오토박싱을 수반할 수 있으니 성능상 좋지 않다. 배열 필드는 원소 각각을 앞서의 지침대로 비교한다. 배열의 모든 원소가 핵심 필드라면 Arrays.equals 메서드들 중 하나를 사용하자.
- 떄론 null도 정상값으로 취급하는 참조 타입 필드도 있다. 이런 필드는 정적메서드인 Objects.equals(Object,Object)로 비교해 `NullPointerExcpetion` 발생을 예방하자.
- 앞서의 CaseInsensitiveString 예처럼 비교하기가 아주 복잡한 필드를 가진 클래스도 있다. 이럴 때는 그 필드의 표준형(canonical form)을 저장해둔 후 표준형끼리 비교하면 훨씬 경제적이다. 이 기법은 특히 불변 클래스(아이템 17)에 제격이다. 가변 객체라면 값이 바뀔때마다 표준형을 최신 상태로 갱신해줘야 한다.
- 어떤 필드를 먼저 비교하느냐가 equals의 성능을 좌우하기도 한다. 최상의 성능을 바란다면 다를 가능성이 더 크거나 비교하는 비용이 싼 (혹은 둘 다 해당하는) 필드를 먼저 비교하자. 동기화용 락(lock) 필드 같이 객체의 논리적 상태와 관련 없는 필드는 비교하면 안 된다. 핵심 필드로부터 계산해낼 수 있는 파생 필드 역시 굳이 비교할 필욘느 없지만 파생 필드를 비교하는 쪽이 더 빠를 때도 있다. 파생 필드가 객체 전체의 상태를 대표하는 상황이 그렇다.
    - 예컨대 자신의 영역을 캐시해두는 Polygon(다각형) 클래스가 있다고 해보자. 그렇다면 모든 변과 정점을 일일이 비교할 필요 없이 캐시해둔 영역만 비교하면 결과를 곧바로 알 수 있다.
- **equals를 다 구현했다면 세 가지만 자문해보자. 대칭적인가? 두이성이 있는가? 일관적인가?** 자문에서 끝내지 말고 단위 테스트를 작성해 돌려보자. 단, equals 메서드를 AutoValue를 이용해 작성했다면 테스트를 생략해도 안심할 수 있다. 세요건 중 하나라도 실패한다면 원인을 찾아서 고치자. 물론 나머지 요건인 반사성과 null-아님도 만족해야 하지만, 이 둘이 문제되는 경우는 별로 없다.
- 다음은 이상의 비법에 따라 작성해본 PhonNumber 클래스용 equals 메서드다
    
    ```java
    package common_methods_of_all_objects.item10_equals;
    
    public class PhoneNumber {
        private final short areaCode, prefix, lineNum;
    
        public PhoneNumber(int areaCode, int prefix, int lineNum) {
            this.areaCode = rangeCheck(areaCode, 999, "지역코드");
            this.prefix = rangeCheck(prefix, 999, "프리픽스");
            this.lineNum = rangeCheck(lineNum, 9999, "가입자 번호");
        }
    
        private static short rangeCheck(int val, int max, String arg) {
            if (val<0 || val > max)
                throw new IllegalArgumentException(arg + ": " + val);
            return (short) val;
        }
    
        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof PhoneNumber))
                return false;
    
            PhoneNumber pn = (PhoneNumber) o;
            return pn.lineNum == lineNum && pn.prefix == prefix && pn.areaCode == areaCode;
        }
    
    }
    ```
    
- **equals를 재정의할 땐 hasCode도 반드시 재정의하자.**
- **너무 복잡하게 해결하려 들지 말자.**
- Object외의 타입을 매개변수로 받는 equals 메서드는 선언하지 말자.
    
    > **핵심 정리**
    꼭 필요한 경우가 아니면 equals를 재정의하지 말자. 많은 경우에 Object의 equals가 여러분이 원하는 비교를 정확히 수행해준다. 재정의해야 할 때는 그 클래스의 핵심 필드 모두를 빠짐 없이, 다섯 가지 규약을 확실히 지켜가며 비교해야 한다.
    >

# 아이템 11: equals를 재정의 하려거든 hashCode도 재정의하라.

## hashCode 23/03/16

📎 **equals를 재정의한 클래스 모두에서 hashCode도 재정의해야 한다.**

- 그렇지 않으면 `hashCode` 일반 규약을 어기게 되어 해당 클래스의 인스턴스를 `HashMap`이나 `HashSet`같은 컬렉션의 원소로 사용할 떄 문제를 일으킬 것이다.

> Object 명세에서 발체한 규약

`equals` 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 `hashCode` 메서드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 한다. 단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없다.

`equals(Object)`가 두 객체를 같다고 판단했다면, 두 객체의 `hashCode`는 똑같은 값을 반환해야 한다.

`equals(Object)`가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다, 단 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.
> 
- hashCode 재정의를 잘못했을 때 크게 문제가 되는 조항은 두 번째다. 즉, 논리적으로 같은 객체는 같은 해시코드를 반환해야 한다.
- 아이템 10에서 보았듯이 equals는 물리적으로 다른 두 객체를 논리적으로는 같다고 할 수 있다. 하지만 Object의 기본 hashCode 메서드는 이 둘이 전혀 다르다고 판단하여, 규약과 달리 (무작위처럼 보이는) 서로 다른 값을 반환한다.
- 예를 들어 아이템 10의 PhoneNumber 클래스의 인스턴스를 HashMap의 원소로 사용한다고 해보자.

```java
package common_methods_of_all_objects.item11_hashcode;

import java.util.HashMap;
import java.util.Map;

public class PhoneNumberHashMap {

    public static void main(String[] args) {

        Map<PhoneNumber, String> m = new HashMap<>();
        m.put(new PhoneNumber(43, 123, 534), "윤섭");

    }

    static class PhoneNumber {
        private final short areaCode, prefix, lineNum;

        public PhoneNumber(int areaCode, int prefix, int lineNum) {
            this.areaCode = rangeCheck(areaCode, 999, "지역코드");
            this.prefix = rangeCheck(prefix, 999, "프리픽스");
            this.lineNum = rangeCheck(lineNum, 9999, "가입자 번호");
        }

        private static short rangeCheck(int val, int max, String arg) {
            if (val < 0 || val > max)
                throw new IllegalArgumentException(arg + ": " + val);
            return (short) val;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof PhoneNumber))
                return false;

            PhoneNumber pn = (PhoneNumber) o;
            return pn.lineNum == lineNum && pn.prefix == prefix && pn.areaCode == areaCode;
        }

    }
}
```

- 이 코드 다음에 `m.get(*new* PhoneNumber(43, 123, 534));`를 실행하면 "윤섭"이 나와야 할 것 같지만, 실제로는 null을 반환한다. 여기에는 2개의 phoneNumber 인스턴스가 사용되었다. 하나는 HashMap에 “제니”를 넣을 때 사용했고, (논리적 동치인) 두 번쨰는 이를 꺼내려할 때 사용됐다.
- `PhoneNumber` 클래스는 `hashCode`를 재정의하지 않았기 때문에 논리적 동치인 두 객체가 서로 다른 해시코드를 반환하여 두 번째 규약을 지키지 못한다. 그 결과 `get` 메서드는 엉뚱한 해시 버켓에 가서 객체를 찾으려 한 것이다.
- 설사 두 인스턴스를 같은 버킷에 담았더라도 `get` 메서드는 여전히 `null`을 반환하는데, `HashMap`은 해시코드가 다른 엔트리끼리는 동치성 비교를 시도조차 하지 않도록 최적화되어 있기 때문이다.
- 이 문제는 `PhoneNumber`에 적절한 `hashCode` 메서드만 작성해주면 해결된다. 올바른 `hashCode` 메서드는 어떤 모습이어야 할까? 안 좋게 작성하려면 아주 간단하다. 예를 들어 다음 코드는 적법하게 구현헀지만, 절대 사용해서는 안된다.

```java
@Override
public int HashCode() {
    return 42;
}
```

- 이 코드는 동치인 모든 객체에서 똑같은 해시코드를 반환하니 적법하다. 하지만 끔찍하게도 모든 객체에서 똑같은 값만 내어주므로 모든 객체가 해시테이블의 버킷 하나에 담겨 마치 연결 리스트(`linked list`)처럼 동작한다. 그 결과 평균 수행시간이 O(1)인 해시테이블이 O(n)으로 느려져서, 객체가 많아지면 도저히 쓸 수 없게된다.
- 좋은 해시 함수라면 서로 다른 인슽넌스에 서로 다른 해시코드를 반환한다. 이것이 바로 `hashCode`의 세 번째 규약이 요구하는 속성이다.
- 이상적인 해시 함수는 주어진 (서로 다른) 인스턴스들을 32비트 정수 범위에 균일하게 분배해야 한다. 이상을 완벽히 실현하기는 어렵지만 비슷하게 만들기는 그다지 어렵지 않다.
- 다음은 좋은 hashCode를 작성하는 간단한 요령이다.
    1. int 변수 result를 선언한 후 값 c로 초기화한다. 이때 c는 해당 객체의 첫번째 핵심 필드를 단계 2.a 방식으로 계산한 해시코드다(여기서 핵심 필드란 equals 비교에 사용되는 필드를 말한다. 아이템 10 참조)
    2. 해당 객체의 나머지 핵심 필드 f 각각에 대해 다음 작업을 수행한다.
        1. 해당 필드의 해시코드 c를 계산한다.
            1. 기본 타입 필드라면, `Type.hashCode`(f)를 수행한다. 여기서 `Type`은 해당 기본 타입의 박싱 클래스다.
            2. 참조 타입 필드면서 이 클래스의 equals 메서드가 이 필드의 equals를 재귀적으로 호출해 비교한다면, 이 필드의 `hashCode`를 재귀적으로 호출한다. 계산이 더 복잡해질 것 같으면, 이 필드의 표준형(ca-nonical representation)을 만들어 그 표준형의 `hashCode`를 호출한다. 필드의 값이 null이면 0을 사용한다(다른 상수도 괜찮지만 전통적으로 0을 사용한다)
            3. 필드가 배열이라면, 핵심 원소 각각을 별도 필드처럼 다룬다, 이상의 규칙을 재귀적으로 적용해 각 핵심 원소의 해시코드를 계산한 다음, 단계 2.b 방식으로 갱신한다. 배열에 핵심 원소가 하나도 없다면 단순히 상수(0을 추천한다)를 사용한다. 모든 원소가 핵심 원소라면 `Arrays.hashCode`를 사용한다.
        2. 단계 2.a에서 계산한 해시코드 c로 result를 갱신한다. 코드로는 다음과 같다.
        `result = 31 * result + c;`
        3. result를 반환한다.
        
- `hashCode`를 다 구현했다면 이 메서드가 동치인 인스턴스에 대해 똑같은 해시코드를 반환할지 자문해보자. 그리고 여러분의 직관을 검증할 단위테스트를 작성하자 (`equals`와 `hashCode`를 `AutoValue`로 생성했다면 건너뛰어도 좋다)
    - hashCode Override 전
        
        ```java
        System.out.println(new PhoneNumber(43, 123, 534));
        // common_methods_of_all_objects.item11_hashcode.PhoneNumberHashMap$PhoneNumber@1b6d3586
        System.out.println(new PhoneNumber(43, 123, 534));
        // common_methods_of_all_objects.item11_hashcode.PhoneNumberHashMap$PhoneNumber@4554617c
        ```
        
    - hashCode Override 후
        
        ```java
        System.out.println(new PhoneNumber(43, 123, 534));
        // common_methods_of_all_objects.item11_hashcode.PhoneNumberHashMap$PhoneNumber@e290
        System.out.println(new PhoneNumber(43, 123, 534));
        // common_methods_of_all_objects.item11_hashcode.PhoneNumberHashMap$PhoneNumber@e290
        ```
        
- 동치인 인스턴스가 서로 다른 해시코드를 반환한다면 원인을 찾아 해결하자.
- 파생 필드는 해시코드 계산에서 제외해도 된다. 즉, 다른 필드로부터 계산해낼 수 있는 필드는 모두 무시해도 된다. 또한 `equals` 비교에 사용되지 않은 필드는 반드시 제외해야 한다. 그렇지 않으면 `hashCode` 규약 두 번쨰를 어기게 될 위험이 있다.

- 단계 2.b의 곱셈 `31*result`는 필드를 곱하는 순서에 따라  result 값이 달라지게 한다. 그 결과 클래스에 비슷한 필드가 여러 개일 때 해시 효과를 크게 높여준다.
