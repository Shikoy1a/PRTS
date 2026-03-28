package com.travel.ds;

public interface Queue<E> extends Collection<E> {
    boolean offer(E e);

    E poll();

    E peek();
}
