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
