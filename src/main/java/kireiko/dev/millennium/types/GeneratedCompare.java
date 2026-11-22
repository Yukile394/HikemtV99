/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.types;

import lombok.Generated;

public final class GeneratedCompare {
    private int x;
    private int y;

    public GeneratedCompare(int x, int y) {
    }

    public GeneratedCompare(Number x, Number y) {
        this.x = x.intValue();
        this.y = y.intValue();
    }

    public double distance(GeneratedCompare vector2) {
        return Math.sqrt(Math.pow(this.x - vector2.x, 2.0) + Math.pow(this.y - vector2.y, 2.0));
    }

    public GeneratedCompare add(GeneratedCompare vector2) {
        return new GeneratedCompare(this.x + vector2.x, this.y + vector2.y);
    }

    public GeneratedCompare subtract(GeneratedCompare vector2) {
        return new GeneratedCompare(this.x - vector2.x, this.y - vector2.y);
    }

    public GeneratedCompare scale(double factor) {
        return new GeneratedCompare((double)this.x * factor, (double)this.y * factor);
    }

    public boolean compare(GeneratedCompare vector2) {
        return this.x == vector2.x && this.y == vector2.y;
    }

    @Generated
    public int getX() {
        return this.x;
    }

    @Generated
    public int getY() {
        return this.y;
    }

    @Generated
    public void setX(int x) {
    }

    @Generated
    public void setY(int y) {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GeneratedCompare)) {
            return false;
        }
        GeneratedCompare other = (GeneratedCompare)o;
        if (this.getX() != other.getX()) {
            return false;
        }
        return this.getY() == other.getY();
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        return result;
    }

    @Generated
    public String toString() {
        return "Vec2i(x=" + this.getX() + ", y=" + this.getY() + ")";
    }
}

