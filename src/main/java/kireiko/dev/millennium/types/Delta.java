/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.types;

import lombok.Generated;

public final class Delta {
    private float x;
    private float y;

    public Delta(float x, float y) {
    }

    public Delta(Number x, Number y) {
        this.x = x.(float)Value();
        this.y = y.(float)Value();
    }

    public double distance(Delta vector2) {
        return Math.sqrt(Math.pow(this.x - vector2.x, 2.0) + Math.pow(this.y - vector2.y, 2.0));
    }

    public Delta add(Delta vector2) {
        return new Delta(this.x + vector2.x, this.y + vector2.y);
    }

    public Delta subtract(Delta vector2) {
        return new Delta(this.x - vector2.x, this.y - vector2.y);
    }

    public Delta scale(double factor) {
        return new Delta((double)this.x * factor, (double)this.y * factor);
    }

    public boolean compare(Delta vector2) {
        return this.x == vector2.x && this.y == vector2.y;
    }

    @Generated
    public float getX() {
        return this.x;
    }

    @Generated
    public float getY() {
        return this.y;
    }

    @Generated
    public void setX(float x) {
    }

    @Generated
    public void setY(float y) {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Delta)) {
            return false;
        }
        Delta other = (Delta)o;
        if (Float.compare(this.getX(), other.getX()) != 0) {
            return false;
        }
        return Float.compare(this.getY(), other.getY()) == 0;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        result = result * 59 + Float.(float)ToIntBits(this.getX());
        result = result * 59 + Float.(float)ToIntBits(this.getY());
        return result;
    }

    @Generated
    public String toString() {
        return "Vec2f(x=" + this.getX() + ", y=" + this.getY() + ")";
    }
}

