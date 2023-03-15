package common_methods_of_all_objects.item10_equals;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

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
