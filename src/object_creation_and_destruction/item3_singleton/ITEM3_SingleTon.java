package object_creation_and_destruction.item3_singleton;

public class ITEM3_SingleTon {

    public static final ITEM3_SingleTon INSTANCE = new ITEM3_SingleTon();

    private ITEM3_SingleTon() {
        //...
    }

    private static final ITEM3_SingleTon INSTANCE2 = new ITEM3_SingleTon();

    // 방법 2
    public static ITEM3_SingleTon getInstance() {
        return INSTANCE2;
    }

    // 가짜 ITEM3_SingleTon 생성을 예방해주는 코드
    private Object readResolve() {
        // '진짜' ITEM3_SingleTon을 반환하고, 가짜 ITEM_SingleTon은 가비지 컬렉터에 맡긴다.
        return INSTANCE2;
    }
}
