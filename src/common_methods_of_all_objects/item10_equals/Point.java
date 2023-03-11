package common_methods_of_all_objects.item10_equals;

import java.util.Set;

public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof Point))
//            return false;
//        Point p = (Point) o;
//        return p.x == x && p.y == y;
//    }

    //리스코프 치환 원칙 위배
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass())
            return false;
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }

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
    
}
