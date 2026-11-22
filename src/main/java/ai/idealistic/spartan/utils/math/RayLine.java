/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package ai.idealistic.spartan.utils.math;

import lombok.Generated;

public class RayLine {
    public final double x;
    public final double z;

    @Generated
    public double getX() {
        return this.x;
    }

    @Generated
    public double getZ() {
        return this.z;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RayLine)) {
            return false;
        }
        RayLine other = (RayLine)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getX(), other.getX()) != 0) {
            return false;
        }
        return Double.compare(this.getZ(), other.getZ()) == 0;
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof RayLine;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $x = Double.(double)ToLongBits(this.getX());
        result = result * 59 + (int)($x >>> 32 ^ $x);
        long $z = Double.(double)ToLongBits(this.getZ());
        result = result * 59 + (int)($z >>> 32 ^ $z);
        return result;
    }

    @Generated
    public String toString() {
        return "RayLine(x=" + this.getX() + ", z=" + this.getZ() + ")";
    }

    @Generated
    public RayLine(double x, double y) {
    }
}

