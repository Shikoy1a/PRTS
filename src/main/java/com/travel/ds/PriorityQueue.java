package com.travel.ds;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PriorityQueue<E> implements Queue<E> {
    private final ArrayList<E> heap;
    private final Comparator<? super E> comparator;

    public PriorityQueue() {
        this(null);
    }

    public PriorityQueue(Comparator<? super E> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return heap.contains(o);
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException("PriorityQueue does not permit null elements");
        }
        heap.add(e);
        siftUp(heap.size() - 1);
        return true;
    }

    @Override
    public E poll() {
        if (heap.isEmpty()) {
            return null;
        }
        E root = heap.get(0);
        E tail = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, tail);
            siftDown(0);
        }
        return root;
    }

    @Override
    public E peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    @Override
    public boolean remove(Object o) {
        int index = heap.indexOf(o);
        if (index < 0) {
            return false;
        }
        int last = heap.size() - 1;
        if (index == last) {
            heap.remove(last);
            return true;
        }
        E replacement = heap.remove(last);
        heap.set(index, replacement);
        siftDown(index);
        siftUp(index);
        return true;
    }

    @Override
    public void clear() {
        heap.clear();
    }

    @Override
    public Object[] toArray() {
        return heap.toArray();
    }

    @Override
    public Iterator<E> iterator() {
        return heap.iterator();
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            E value = heap.get(index);
            E parentValue = heap.get(parent);
            if (compare(value, parentValue) >= 0) {
                break;
            }
            heap.set(index, parentValue);
            heap.set(parent, value);
            index = parent;
        }
    }

    private void siftDown(int index) {
        int half = heap.size() >>> 1;
        while (index < half) {
            int left = (index << 1) + 1;
            int right = left + 1;
            int smallest = left;

            if (right < heap.size() && compare(heap.get(right), heap.get(left)) < 0) {
                smallest = right;
            }
            if (compare(heap.get(index), heap.get(smallest)) <= 0) {
                break;
            }
            E tmp = heap.get(index);
            heap.set(index, heap.get(smallest));
            heap.set(smallest, tmp);
            index = smallest;
        }
    }
}
