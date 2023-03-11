package common_methods_of_all_objects.item10_equals;

import java.awt.*;

public class EqualsTest {

    public static void main(String[] args) {

        //대칭성 위배
        Point p = new Point(1, 2);
        ColorPoint cp = new ColorPoint(1, 2, Color.red);

        System.out.println(p.equals(cp));
        System.out.println(cp.equals(p));



        //추이성 위배
        ColorPoint cp1 = new ColorPoint(1, 2, Color.RED);
        Point p2 = new Point(1, 2);
        ColorPoint cp2 = new ColorPoint(1, 2, Color.BLUE);

        System.out.println(cp1.equals(p2));
        System.out.println(p2.equals(cp2));
        System.out.println(cp1.equals(cp2));
    }
}
