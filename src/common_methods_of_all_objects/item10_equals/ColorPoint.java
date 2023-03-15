package common_methods_of_all_objects.item10_equals;

import java.util.HashMap;
import java.util.Map;

public class ColorPoint extends Point{

    private final Color color;
    private Map<String, String> map = new HashMap<>();

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }


    //임의로 추가
    static class Color {

    }
}
