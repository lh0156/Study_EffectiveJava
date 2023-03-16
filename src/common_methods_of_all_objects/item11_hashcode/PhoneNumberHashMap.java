package common_methods_of_all_objects.item11_hashcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneNumberHashMap {

    public static void main(String[] args) {

        Map<PhoneNumber, String> m = new HashMap<>();
        m.put(new PhoneNumber(43, 123, 534), "윤섭");

        String seop = m.get(new PhoneNumber(43, 123, 534));
        System.out.println(seop);

        List<PhoneNumber> phoneNumberList = new ArrayList<>();

        phoneNumberList.add(new PhoneNumber(43, 123, 534));

        boolean contains = phoneNumberList.contains(new PhoneNumber(43, 123, 534));
        System.out.println(contains);

        System.out.println(new PhoneNumber(43, 123, 534));
        System.out.println(new PhoneNumber(43, 123, 534));

        System.out.println("AEE-131".hashCode());

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
            return pn.areaCode == areaCode && pn.lineNum == lineNum && pn.prefix == prefix;
        }

        // hashCode 구현 - 사용 금지
//        @Override
//        public int hashCode() {
//            int result = Short.hashCode(this.areaCode);
//            result = 31 * result + Short.hashCode(this.lineNum);
//            result = 31 * result + Short.hashCode(this.prefix);
//
//            return result;
//        }

    }
}
