package austeretony.oxygen_core.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayListWrapper<T> {

    public final List<T> list = new ArrayList<T>();

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public int size() {
        return this.list.size();
    }

    public boolean contains(T object) {
        return this.list.contains(object);
    }

    public int indexOf(T object) {
        return this.list.indexOf(object);
    }

    public boolean add(T object) {
        return this.list.add(object);
    }

    public void add(int index, T object) {
        this.list.add(index, object);
    }

    public boolean remove(T object) {
        return this.list.remove(object);
    }

    public T remove(int index) {
        return this.list.remove(index);
    }

    public T get(int index) {
        return this.list.get(index);
    }

    public T set(int index, T object) {
        return this.list.set(index, object);
    }

    public void clear() {
        this.list.clear();
    }

    public Iterator<T> iterator() {
        return this.list.iterator();
    }
}
