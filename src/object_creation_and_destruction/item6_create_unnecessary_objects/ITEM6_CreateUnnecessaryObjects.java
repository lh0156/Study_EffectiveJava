package object_creation_and_destruction.item6_create_unnecessary_objects;

public class ITEM6_CreateUnnecessaryObjects {

    public static void main(String[] args) {

        //따라 하지 말 것
        String sV1 = new String("temp");

        //개선된 버전
        String sV2 = "temp";



    }


    static boolean isRomanNumeral(String s) {

        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
                + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    }

    //끔찍이 느리다!
    private static long sum() {
        Long sum = 0L;

        for (long i = 0; i <= Integer.MAX_VALUE; ++i) {
            sum += i;
        }

        return sum;
    }

}
