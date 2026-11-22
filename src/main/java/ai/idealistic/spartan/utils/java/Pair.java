/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package ai.idealistic.spartan.utils.java;

import lombok.Generated;

public final class Pair<X, Y> {
    private X x;
    private Y y;

    @Generated
    public X getX() {
        return this.x;
    }

    @Generated
    public Y getY() {
        return this.y;
    }

    @Generated
    public void setX(X x) {
    }

    @Generated
    public void setY(Y y) {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair other = (Pair)o;
        X this$x = this.getX();
        X other$x = other.getX();
        if (this$x == null ? other$x != null : !this$x.equals(other$x)) {
            return false;
        }
        Y this$y = this.getY();
        Y other$y = other.getY();
        return !(this$y == null ? other$y != null : !this$y.equals(other$y));
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        X $x = this.getX();
        result = result * 59 + ($x == null ? 43 : $x.hashCode());
        Y $y = this.getY();
        result = result * 59 + ($y == null ? 43 : $y.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Pair(x=" + this.getX() + ", y=" + this.getY() + ")";
    }

    @Generated
    public Pair(X x, Y y) {
    }
}

