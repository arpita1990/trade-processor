package com.cme.trading.cache;

public interface Cache<K, V> {

    V put(K key, V value);

    V get(K key);

    V take(K key);

    int size();

    boolean isEmpty();

    boolean containsKey(K key);

    boolean containsValue(V value);

    void clear();
}