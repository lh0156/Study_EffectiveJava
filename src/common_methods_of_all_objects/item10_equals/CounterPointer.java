package common_methods_of_all_objects.item10_equals;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterPointer extends Point {

    private static final AtomicInteger counter = new AtomicInteger();

    public CounterPointer(int x, int y) {
        super(x, y);
        counter.incrementAndGet();
    }

    public static int numberCreate() {
        return counter.get();
    }

}
