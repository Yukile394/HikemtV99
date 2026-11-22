/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package ai.idealistic.spartan.utils.java;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.jetbrains.annotations.NotNull;

public class OverflowList<E>
implements List<E> {
    private final List<E> list;
    private final int maxSize;
    private int overflow;

    public OverflowList(List<E> list, int maxSize) {
        this.overflow = 0;
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        Iterator<E> iterator = this.list.iterator();
        if (iterator == null) {
            OverflowList.$$$reportNull$$$0(0);
        }
        return iterator;
    }

    @Override
    @NotNull
    public @NotNull Object @NotNull [] toArray() {
        Object[] objectArray = this.list.toArray();
        if (objectArray == null) {
            OverflowList.$$$reportNull$$$0(1);
        }
        return objectArray;
    }

    @Override
    @NotNull
    public <T> @NotNull T @NotNull [] toArray(@NotNull @NotNull T @NotNull [] a) {
        if (a == null) {
            OverflowList.$$$reportNull$$$0(2);
        }
        T[] TArray = this.list.toArray(a);
        if (TArray == null) {
            OverflowList.$$$reportNull$$$0(3);
        }
        return TArray;
    }

    @Override
    public boolean add(E e) {
        if (this.list.size() == this.maxSize) {
            this.list.set(this.overflow, e);
            ++this.overflow;
            if (this.overflow == this.maxSize) {
                this.overflow = 0;
            }
            return true;
        }
        return this.list.add(e);
    }

    @Override
    public boolean remove(Object o) {
        if (this.list.remove(o)) {
            this.overflow = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        if (c == null) {
            OverflowList.$$$reportNull$$$0(4);
        }
        return new HashSet<E>(this.list).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        if (c == null) {
            OverflowList.$$$reportNull$$$0(5);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        if (c == null) {
            OverflowList.$$$reportNull$$$0(6);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        if (c == null) {
            OverflowList.$$$reportNull$$$0(7);
        }
        if (this.list.removeAll(c)) {
            this.overflow = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        if (c == null) {
            OverflowList.$$$reportNull$$$0(8);
        }
        if (this.list.retainAll(c)) {
            this.overflow = 0;
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        this.list.clear();
        this.overflow = 0;
    }

    @Override
    public boolean equals(Object o) {
        return this.list.equals(o);
    }

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public E get(int index) {
        return this.list.get(index);
    }

    @Override
    public E set(int index, E element) {
        return this.list.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        this.list.add(index, element);
    }

    @Override
    public E remove(int index) {
        return this.list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    @Override
    @NotNull
    public ListIterator<E> listIterator() {
        ListIterator<E> listIterator = this.list.listIterator();
        if (listIterator == null) {
            OverflowList.$$$reportNull$$$0(9);
        }
        return listIterator;
    }

    @Override
    @NotNull
    public ListIterator<E> listIterator(int index) {
        ListIterator<E> listIterator = this.list.listIterator(index);
        if (listIterator == null) {
            OverflowList.$$$reportNull$$$0(10);
        }
        return listIterator;
    }

    @Override
    @NotNull
    public List<E> subList(int fromIndex, int toIndex) {
        List<E> list = this.list.subList(fromIndex, toIndex);
        if (list == null) {
            OverflowList.$$$reportNull$$$0(11);
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
                objectArray3[0] = "ai/idealistic/spartan/utils/java/OverflowList";
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
                objectArray2[1] = "ai/idealistic/spartan/utils/java/OverflowList";
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

