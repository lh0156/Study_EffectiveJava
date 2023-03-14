package common_methods_of_all_objects.item10_equals;

public class ColorPoint2 {
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