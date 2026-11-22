/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package ai.idealistic.spartan.utils.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class ConcurrentList<E>
implements List<E> {
    private final Map<Integer, E> map;

    public ConcurrentList(int size) {
        this.map = new ConcurrentHashMap<Integer, E>size;
    }

    public ConcurrentList() {
        this(2);
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
    public boolean contains(Object o) {
        return this.map.containsValue(o);
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        Iterator<E> iterator = this.map.values().iterator();
        if (iterator == null) {
            ConcurrentList.$$$reportNull$$$0(0);
        }
        return iterator;
    }

    @Override
    @NotNull
    public @NotNull Object @NotNull [] toArray() {
        Object[] objectArray = this.map.values().toArray();
        if (objectArray == null) {
            ConcurrentList.$$$reportNull$$$0(1);
        }
        return objectArray;
    }

    @Override
    @NotNull
    public <T> @NotNull T @NotNull [] toArray(@NotNull @NotNull T @NotNull [] a) {
        if (a == null) {
            ConcurrentList.$$$reportNull$$$0(2);
        }
        T[] TArray = this.map.values().toArray(a);
        if (TArray == null) {
            ConcurrentList.$$$reportNull$$$0(3);
        }
        return TArray;
    }

    @Override
    public boolean add(E e) {
        this.map.put(this.map.size(), e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (Map.Entry<Integer, E> entry : this.map.entrySet()) {
            if (!entry.getValue().equals(o)) continue;
            this.map.remove(entry.getKey());
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        if (c == null) {
            ConcurrentList.$$$reportNull$$$0(4);
        }
        for (Object o : c) {
            if (this.contains(o)) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        if (c == null) {
            ConcurrentList.$$$reportNull$$$0(5);
        }
        for (E e : c) {
            this.add(e);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        if (c == null) {
            ConcurrentList.$$$reportNull$$$0(6);
        }
        for (E e : c) {
            this.add(index++, e);
        }
        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        if (c == null) {
            ConcurrentList.$$$reportNull$$$0(7);
        }
        for (Object o : c) {
            this.remove(o);
        }
        return true;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        if (c == null) {
            ConcurrentList.$$$reportNull$$$0(8);
        }
        boolean modified = false;
        Iterator<E> it = this.map.values().iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (c.contains(e)) continue;
            it.remove();
            modified = true;
        }
        return modified;
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        List list = (List)o;
        if (this.size() != list.size()) {
            return false;
        }
        for (i < this.size(); ++i) {
            if (this.get(i).equals(list.get(i))) continue;
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public E get(int index) {
        return this.map.get(index);
    }

    @Override
    public E set(int index, E element) {
        return this.map.put(index, element);
    }

    @Override
    public void add(int index, E element) {
        for (int i = this.map.size(); i > index; --i) {
            this.map.put(i, this.map.get(i - 1));
        }
        this.map.put(index, element);
    }

    @Override
    public E remove(int index) {
        return this.map.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        for (Map.Entry<Integer, E> entry : this.map.entrySet()) {
            if (!entry.getValue().equals(o)) continue;
            return entry.getKey();
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int lastIndex = -1;
        for (Map.Entry<Integer, E> entry : this.map.entrySet()) {
            if (!entry.getValue().equals(o)) continue;
            lastIndex = entry.getKey();
        }
        return lastIndex;
    }

    @Override
    @NotNull
    public ListIterator<E> listIterator() {
        ListIterator<E> listIterator = new ArrayList<E>(this.map.values()).listIterator();
        if (listIterator == null) {
            ConcurrentList.$$$reportNull$$$0(9);
        }
        return listIterator;
    }

    @Override
    @NotNull
    public ListIterator<E> listIterator(int index) {
        ListIterator<E> listIterator = new ArrayList<E>(this.map.values()).listIterator(index);
        if (listIterator == null) {
            ConcurrentList.$$$reportNull$$$0(10);
        }
        return listIterator;
    }

    @Override
    @NotNull
    public List<E> subList(int fromIndex, int toIndex) {
        List<E> list = new ArrayList<E>(this.map.values()).subList(fromIndex, toIndex);
        if (list == null) {
            ConcurrentList.$$$reportNull$$$0(11);
        }
        return list;
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                n2 = 3;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "ai/idealistic/spartan/utils/java/ConcurrentList";
                break;
            }
            case 2: {
                objectArray2 = objectArray3;
                objectArray3[0] = "a";
                break;
            }
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                objectArray2 = objectArray3;
                objectArray3[0] = "c";
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "iterator";
                break;
            }
            case 1:
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "toArray";
                break;
            }
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                objectArray = objectArray2;
                objectArray2[1] = "ai/idealistic/spartan/utils/java/ConcurrentList";
                break;
            }
            case 9:
            case 10: {
                objectArray = objectArray2;
                objectArray2[1] = "listIterator";
                break;
            }
            case 11: {
                objectArray = objectArray2;
                objectArray2[1] = "subList";
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 2: {
                objectArray[2] = "toArray";
                break;
            }
            case 4: {
                objectArray[2] = "containsAll";
                break;
            }
            case 5:
            case 6: {
                objectArray[2] = "addAll";
                break;
            }
            case 7: {
                objectArray[2] = "removeAll";
                break;
            }
            case 8: {
                objectArray[2] = "retainAll";
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 2:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                runtimeException = new IllegalArgumentException(string2);
            }
        }
        throw runtimeException;
    }
}

