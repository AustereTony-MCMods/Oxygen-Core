package austeretony.oxygen_core.common.util.objects;

public class Pair<T, V> {

    private final T key;
    private final V value;

    private Pair(T key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <T, V> Pair<T, V> of(T key, V value) {
        return new Pair<>(key, value);
    }

    public T getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
