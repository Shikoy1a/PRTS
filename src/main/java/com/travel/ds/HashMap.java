package com.travel.ds;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity) {
        int cap = 1;
        while (cap < Math.max(1, initialCapacity)) {
            cap <<= 1;
        }
        table = (Node<K, V>[]) new Node[cap];
        threshold = (int) (cap * LOAD_FACTOR);
    }

    private static final class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry<?, ?> entry)) {
                return false;
            }
            return eq(key, entry.getKey()) && eq(value, entry.getValue());
        }
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
    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    @Override
    public V get(Object key) {
        Node<K, V> node = getNode(hash(key), key);
        return node == null ? null : node.value;
    }

    @Override
    public V put(K key, V value) {
        return putVal(hash(key), key, value);
    }

    @Override
    public V remove(Object key) {
        Node<K, V> node = removeNode(hash(key), key);
        return node == null ? null : node.value;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> keys = new HashSet<>();
        for (Node<K, V> bucket : table) {
            for (Node<K, V> n = bucket; n != null; n = n.next) {
                keys.add(n.key);
            }
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        ArrayList<V> values = new ArrayList<>(size);
        for (Node<K, V> bucket : table) {
            for (Node<K, V> n = bucket; n != null; n = n.next) {
                values.add(n.value);
            }
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet entries = new EntrySet();
        for (Node<K, V> bucket : table) {
            for (Node<K, V> n = bucket; n != null; n = n.next) {
                entries.add(new Node<>(n.hash, n.key, n.value, null));
            }
        }
        return entries;
    }

    private static final class EntrySet<K, V> implements Set<Map.Entry<K, V>> {
        private final ArrayList<Map.Entry<K, V>> entries = new ArrayList<>();

        @Override
        public int size() {
            return entries.size();
        }

        @Override
        public boolean isEmpty() {
            return entries.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return entries.contains(o);
        }

        @Override
        public boolean add(Map.Entry<K, V> e) {
            if (!contains(e)) {
                return entries.add(e);
            }
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return entries.remove(o);
        }

        @Override
        public void clear() {
            entries.clear();
        }

        @Override
        public Object[] toArray() {
            return entries.toArray();
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return entries.iterator();
        }
    }

    private V putVal(int hash, K key, V value) {
        int index = indexFor(hash, table.length);
        for (Node<K, V> n = table[index]; n != null; n = n.next) {
            if (n.hash == hash && eq(n.key, key)) {
                V old = n.value;
                n.value = value;
                return old;
            }
        }
        table[index] = new Node<>(hash, key, value, table[index]);
        if (++size > threshold) {
            resize();
        }
        return null;
    }

    private Node<K, V> getNode(int hash, Object key) {
        int index = indexFor(hash, table.length);
        for (Node<K, V> n = table[index]; n != null; n = n.next) {
            if (n.hash == hash && eq(n.key, key)) {
                return n;
            }
        }
        return null;
    }

    private Node<K, V> removeNode(int hash, Object key) {
        int index = indexFor(hash, table.length);
        Node<K, V> prev = null;
        Node<K, V> curr = table[index];
        while (curr != null) {
            if (curr.hash == hash && eq(curr.key, key)) {
                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size--;
                return curr;
            }
            prev = curr;
            curr = curr.next;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[oldTable.length << 1];
        for (Node<K, V> head : oldTable) {
            Node<K, V> node = head;
            while (node != null) {
                Node<K, V> next = node.next;
                int index = indexFor(node.hash, newTable.length);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newTable.length * LOAD_FACTOR);
    }

    private static int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private static int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private static boolean eq(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }
}
