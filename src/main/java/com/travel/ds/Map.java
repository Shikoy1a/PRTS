package com.travel.ds;

public interface Map<K, V> {
    interface Entry<K, V> {
        K getKey();

        V getValue();

        V setValue(V value);
    }

    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    V get(Object key);

    V put(K key, V value);

    V remove(Object key);

    void clear();

    Set<K> keySet();

    Collection<V> values();

    Set<Entry<K, V>> entrySet();

    default V getOrDefault(Object key, V defaultValue) {
        V value = get(key);
        return value != null || containsKey(key) ? value : defaultValue;
    }
}
