/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.utils.minecraft.vector;

import ai.idealistic.spartan.utils.math.MathHelper;

public class P1779531
implements Comparable {
    public static final P1779531 NULL_VECTOR = new P1779531(0, 0, 0);
    private final int x;
    private final int y;
    private final int z;

    public P1779531(int p_i46007_1_, int p_i46007_2_, int p_i46007_3_) {
        this.x = p_i46007_1_;
        this.y = p_i46007_2_;
        this.z = p_i46007_3_;
    }

    public P1779531(double p_i46008_1_, double p_i46008_3_, double p_i46008_5_) {
        this(MathHelper.floor_double(p_i46008_1_), MathHelper.floor_double(p_i46008_3_), MathHelper.floor_double(p_i46008_5_));
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof P1779531)) {
            return false;
        }
        P1779531 var2 = (P1779531)p_equals_1_;
        return this.getX() != var2.getX() ? false : (this.getY() != var2.getY() ? false : this.getZ() == var2.getZ());
    }

    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int compareTo(P1779531 p_177953_1_) {
        return this.getY() == p_177953_1_.getY() ? (this.getZ() == p_177953_1_.getZ() ? this.getX() - p_177953_1_.getX() : this.getZ() - p_177953_1_.getZ()) : this.getY() - p_177953_1_.getY();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public P1779531 crossProduct(P1779531 vec) {
        return new P1779531(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    public double distanceSq(double toX, double toY, double toZ) {
        double var7 = (double)this.getX() - toX;
        double var9 = (double)this.getY() - toY;
        double var11 = (double)this.getZ() - toZ;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public double distanceSqToCenter(double p_177957_1_, double p_177957_3_, double p_177957_5_) {
        double var7 = (double)this.getX() + 0.5 - p_177957_1_;
        double var9 = (double)this.getY() + 0.5 - p_177957_3_;
        double var11 = (double)this.getZ() + 0.5 - p_177957_5_;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public double distanceSq(P1779531 to) {
        return this.distanceSq(to.getX(), to.getY(), to.getZ());
    }

    public int compareTo(Object p_compareTo_1_) {
        return this.compareTo((P1779531)p_compareTo_1_);
    }
}

