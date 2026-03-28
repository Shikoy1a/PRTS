package com.travel.ds;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashSet<E> implements Set<E> {
    private static final Object PRESENT = new Object();
    private final HashMap<E, Object> map;

    public HashSet() {
        this.map = new HashMap<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Object[] toArray() {
        return map.keySet().toArray();
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<Map.Entry<E, Object>> it = map.entrySet().iterator();
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return it.next().getKey();
            }
        };
    }
}
