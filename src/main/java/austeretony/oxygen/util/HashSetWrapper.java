package austeretony.oxygen.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HashSetWrapper<T> {

    public final Set<T> set = new HashSet<T>();

    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    public int size() {
        return this.set.size();
    }

    public boolean contains(T object) {
        return this.set.contains(object);
    }

    public boolean add(T object) {
        return this.set.add(object);
    }

    public boolean remove(T object) {
        return this.set.remove(object);
    }

    public void clear() {
        this.set.clear();
    }

    public Iterator<T> iterator() {
        return this.set.iterator();
    }
}
