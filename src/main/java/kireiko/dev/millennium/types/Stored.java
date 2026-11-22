/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.types;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import lombok.Generated;

public final class Stored<K, V>
extends HashMap<K, V> {
    private final int size;
    private final Deque<K> storedKeys = new LinkedList<K>();

    @Override
    public boolean remove(Object key, Object value) {
        this.storedKeys.remove(key);
        return super.remove(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (!this.storedKeys.contains(key) || !this.get(key).equals(value)) {
            this.checkAndRemove();
        }
        return super.putIfAbsent(key, value);
    }

    @Override
    public V put(K key, V value) {
        this.checkAndRemove();
        this.storedKeys.addLast(key);
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        this.storedKeys.clear();
        super.clear();
    }

    @Override
    public V remove(Object key) {
        this.storedKeys.remove(key);
        return super.remove(key);
    }

    private boolean checkAndRemove() {
        if (this.storedKeys.size() >= this.size) {
            K key = this.storedKeys.removeFirst();
            this.remove(key);
            return true;
        }
        return false;
    }

    @Generated
    public Stored(int size) {
    }

    @Generated
    public int getSize() {
        return this.size;
    }
}

