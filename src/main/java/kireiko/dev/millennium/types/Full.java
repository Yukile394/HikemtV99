/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.types;

import java.util.Collection;
import java.util.LinkedList;
import lombok.Generated;

public final class Full<T>
extends LinkedList<T> {
    private final int maxSize;

    public Full(int maxSize) {
    }

    public Full(Collection<? extends T> c, int maxSize) {
        super(c);
    }

    @Override
    public boolean add(T t) {
        if (this.size() >= this.getMaxSize()) {
            this.removeFirst();
        }
        return super.add(t);
    }

    public boolean isFull() {
        return this.size() >= this.getMaxSize();
    }

    @Generated
    public int getMaxSize() {
        return this.maxSize;
    }
}

