package austeretony.oxygen_core.common.util.objects;

public class Triple<T, V, K> {

    private final T first;
    private final V second;
    private final K third;

    private Triple(T first, V second, K third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <T, V, K> Triple<T, V, K> of(T first, V second, K third) {
        return new Triple<>(first, second, third);
    }

    public T getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public K getThird() {
        return third;
    }
}
