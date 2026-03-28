package com.travel.ds;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<E> implements Deque<E> {
    private static final int DEFAULT_CAPACITY = 16;

    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    public ArrayDeque() {
        this.elements = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int idx = head;
        for (int i = 0; i < size; i++) {
            Object element = elements[idx];
            if (o == null ? element == null : o.equals(element)) {
                return true;
            }
            idx = inc(idx);
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public void addFirst(E e) {
        if (e == null) {
            throw new NullPointerException("ArrayDeque does not support null elements");
        }
        ensureCapacity();
        head = dec(head);
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (e == null) {
            throw new NullPointerException("ArrayDeque does not support null elements");
        }
        ensureCapacity();
        elements[tail] = e;
        tail = inc(tail);
        size++;
    }

    @Override
    public E removeFirst() {
        E value = pollFirst();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E removeLast() {
        E value = pollLast();
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E value = (E) elements[head];
        elements[head] = null;
        head = inc(head);
        size--;
        return value;
    }

    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        tail = dec(tail);
        @SuppressWarnings("unchecked")
        E value = (E) elements[tail];
        elements[tail] = null;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peekFirst() {
        return isEmpty() ? null : (E) elements[head];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peekLast() {
        return isEmpty() ? null : (E) elements[dec(tail)];
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0) {
            return false;
        }
        delete(index);
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = tail = size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int idx = head;
        for (int i = 0; i < size; i++) {
            result[i] = elements[idx];
            idx = inc(idx);
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int count;
            private int cursor = head;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E value = (E) elements[cursor];
                cursor = inc(cursor);
                count++;
                return value;
            }
        };
    }

    private void ensureCapacity() {
        if (size < elements.length - 1) {
            return;
        }
        Object[] old = elements;
        Object[] newer = new Object[old.length << 1];
        int idx = head;
        for (int i = 0; i < size; i++) {
            newer[i] = old[idx];
            idx = (idx + 1) & (old.length - 1);
        }
        elements = newer;
        head = 0;
        tail = size;
    }

    private int indexOf(Object o) {
        int idx = head;
        for (int i = 0; i < size; i++) {
            Object v = elements[idx];
            if (o == null ? v == null : o.equals(v)) {
                return idx;
            }
            idx = inc(idx);
        }
        return -1;
    }

    private void delete(int index) {
        int mask = elements.length - 1;
        int front = (index - head) & mask;
        int back = (tail - index) & mask;

        if (front < back) {
            while (index != head) {
                int prev = dec(index);
                elements[index] = elements[prev];
                index = prev;
            }
            elements[head] = null;
            head = inc(head);
        } else {
            while (index != dec(tail)) {
                int next = inc(index);
                elements[index] = elements[next];
                index = next;
            }
            tail = dec(tail);
            elements[tail] = null;
        }
        size--;
    }

    private int inc(int i) {
        return (i + 1) & (elements.length - 1);
    }

    private int dec(int i) {
        return (i - 1) & (elements.length - 1);
    }
}
