package com.ulto.customblocks.util;

import java.util.HashSet;
import java.util.Set;

public class TriMap<K, T, V> {
    public T get1(K key) {
        T returnValue = null;
        for (Entry<K, T, V> entry : entrySet) {
            if (entry.key == key) returnValue = entry.value1;
        }
        return returnValue;
    }
    public V get2(K key) {
        V returnValue = null;
        for (Entry<K, T, V> entry : entrySet) {
            if (entry.key == key) returnValue = entry.value2;
        }
        return returnValue;
    }

    public void put(K key, T value1, V value2) {
        boolean keyExists = false;
        for (Entry<K, T, V> entry : entrySet) {
            if (entry.key == key) {
                keyExists = true;
                entry.setValue1(value1);
                entry.setValue2(value2);
                break;
            }
        }
        if (!keyExists) entrySet.add(new Entry<>(key, value1, value2));
    }

    public Set<Entry<K, T, V>> entrySet = new HashSet<>();

    public static class Entry<K, T, V> {
        K key;
        T value1;
        V value2;

        Entry(K key, T value1, V value2) {
            this.key = key;
            this.value1 = value1;
            this.value2 = value2;
        }

        public K getKey() {
            return key;
        }

        public T getValue1() {
            return value1;
        }

        public V getValue2() {
            return value2;
        }

        public void setValue1(T value1) {
            this.value1 = value1;
        }

        public void setValue2(V value2) {
            this.value2 = value2;
        }
    }
}
