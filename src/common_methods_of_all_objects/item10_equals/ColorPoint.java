package common_methods_of_all_objects.item10_equals;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ColorPoint extends Point{

    private final Color color;

    public ColorPoint(int x, int y, Color color) {

        super(x, y);
        this.color = color;
    }

// 대칭성 위배
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof ColorPoint))
//            return false;
//        return super.equals(o) && ((ColorPoint) o).color == color;
//    }


//  추이성 위배
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof Point))
//            return false;
//
//        // o가 일반 Point면 색상을 무시하고 비교한다
//        if (!(o instanceof ColorPoint))
//            return o.equals(this);
//
//        // o가 ColorPoint면 색상까지 비교한다.
//        return super.equals(o) && ((ColorPoint) o).color == color;
//    }



}
