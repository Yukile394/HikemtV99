/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.NonNull
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package ai.idealistic.spartan.utils.java;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OverflowMap<K, V>
implements Map<K, V> {
    private final Map<K, V> map;
    private final int maxSize;

    public OverflowMap(Map<K, V> map, int maxSize) {
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.map.get(key);
    }

    @Override
    @Nullable
    public V put(K key, V value) {
        V result = this.map.put(key, value);
        if (result == null && this.map.size() > this.maxSize) {
            Iterator<K> iterator = this.map.keySet().iterator();
            iterator.next();
            iterator.remove();
        }
        return result;
    }

    @Override
    public V remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        if (m == null) {
            OverflowMap.$$$reportNull$$$0(0);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    @NotNull
    public Set<K> keySet() {
        Set<K> set = this.map.keySet();
        if (set == null) {
            OverflowMap.$$$reportNull$$$0(1);
        }
        return set;
    }

    @Override
    @NotNull
    public Collection<V> values() {
        Collection<V> collection = this.map.values();
        if (collection == null) {
            OverflowMap.$$$reportNull$$$0(2);
        }
        return collection;
    }

    @Override
    @NotNull
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.map.entrySet();
        if (set == null) {
            OverflowMap.$$$reportNull$$$0(3);
        }
        return set;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V v = this.get(key);
        if (v == null) {
            v = this.put(key, value);
        }
        return v;
    }

    @Override
    public V computeIfAbsent(K key, @NonNull Function<? super K, ? extends V> mappingFunction) {
        V newValue;
        Objects.requireNonNull(mappingFunction);
        V v = this.get(key);
        if (v == null && (newValue = mappingFunction.apply(key)) != null) {
            this.put(key, newValue);
            return newValue;
        }
        return v;
    }

    @Override
    public V computeIfPresent(K key, @NonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue = this.get(key);
        if (oldValue != null) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                this.put(key, newValue);
                return newValue;
            }
            this.remove(key);
            return null;
        }
        return null;
    }

    @Override
    public V compute(K key, @NonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue = this.get(key);
        V newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeIfExists(key, oldValue);
        } else {
            this.put(key, newValue);
        }
        return newValue;
    }

    public void removeIfExists(K key, V oldValue) {
        if (oldValue != null || this.containsKey(key)) {
            this.remove(key);
        }
    }

    @Override
    public V merge(K key, @NonNull V value, @NonNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        V newValue;
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        V oldValue = this.get(key);
        newValue = oldValue == null ? value : remappingFunction.apply(oldValue, value);
        if (newValue == null) {
            this.remove(key);
        } else {
            this.put(key, newValue);
        }
        return newValue;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 1:
            case 2:
            case 3: {
                string = "@NotNull method %s.%s must not return null";
            }
        }
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 1:
            case 2:
            case 3: {
                n2 = 2;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "m";
                break;
            }
            case 1:
            case 2:
            case 3: {
                objectArray2 = objectArray3;
                objectArray3[0] = "ai/idealistic/spartan/utils/java/OverflowMap";
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "ai/idealistic/spartan/utils/java/OverflowMap";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "keySet";
                break;
            }
            case 2: {
                objectArray = objectArray2;
                objectArray2[1] = "values";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "entrySet";
            }
        }
        switch (n) {
            default: {
                objectArray[2] = "putAll";
            }
            case 1:
            case 2:
            case 3:
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalArgumentException(string2);
                break;
            }
            case 1:
            case 2:
            case 3: {
                runtimeException = new IllegalStateException(string2);
            }
        }
        throw runtimeException;
    }
}

