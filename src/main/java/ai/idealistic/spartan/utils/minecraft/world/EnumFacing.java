/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Maps
 *  lombok.Generated
 */
package ai.idealistic.spartan.utils.minecraft.world;

import ai.idealistic.spartan.utils.math.MathHelper;
import ai.idealistic.spartan.utils.minecraft.vector.P1779531;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import lombok.Generated;

public enum EnumFacing {
    DOWN("DOWN", 0, 0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new P1779531(0, -1, 0)),
    UP("UP", 1, 1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new P1779531(0, 1, 0)),
    NORTH("NORTH", 2, 2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new P1779531(0, 0, -1)),
    SOUTH("SOUTH", 3, 3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new P1779531(0, 0, 1)),
    WEST("WEST", 4, 4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new P1779531(-1, 0, 0)),
    EAST("EAST", 5, 5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new P1779531(1, 0, 0));

    private final int index;
    private final int opposite;
    private final int horizontalIndex;
    private final String name;
    private final Axis axis;
    private final AxisDirection axisDirection;
    private final P1779531 directionVec;
    public static final EnumFacing[] VALUES;
    private static final EnumFacing[] HORIZONTALS;
    private static final Map NAME_LOOKUP;
    private static final EnumFacing[] $VALUES;

    private EnumFacing(String p_i46016_1_, int p_i46016_2_, int indexIn, int oppositeIn, int horizontalIndexIn, String nameIn, AxisDirection axisDirectionIn, Axis axisIn, P1779531 directionVecIn) {
        this.index = indexIn;
        this.horizontalIndex = horizontalIndexIn;
        this.opposite = oppositeIn;
        this.name = nameIn;
        this.axis = axisIn;
        this.axisDirection = axisDirectionIn;
        this.directionVec = directionVecIn;
    }

    public EnumFacing getOpposite() {
        return VALUES[this.opposite];
    }

    public EnumFacing rotateAround(Axis axis) {
        switch (axis) {
            case X: {
                if (this != WEST && this != EAST) {
                    return this.rotateX();
                }
                return this;
            }
            case Y: {
                if (this != UP && this != DOWN) {
                    return this.rotateY();
                }
                return this;
            }
            case Z: {
                if (this != NORTH && this != SOUTH) {
                    return this.rotateZ();
                }
                return this;
            }
        }
        throw new IllegalStateException("Unable to get CW facing for axis " + (Object)axis);
    }

    public EnumFacing rotateY() {
        switch (this) {
            case NORTH: {
                return EAST;
            }
            case EAST: {
                return SOUTH;
            }
            case SOUTH: {
                return WEST;
            }
            case WEST: {
                return NORTH;
            }
        }
        throw new IllegalStateException("Unable to get Y-rotated facing of " + (Object)this);
    }

    private EnumFacing rotateX() {
        switch (this) {
            case NORTH: {
                return DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get X-rotated facing of " + (Object)this);
            }
            case SOUTH: {
                return UP;
            }
            case UP: {
                return NORTH;
            }
            case DOWN:
        }
        return SOUTH;
    }

    private EnumFacing rotateZ() {
        switch (this) {
            case EAST: {
                return DOWN;
            }
            default: {
                throw new IllegalStateException("Unable to get Z-rotated facing of " + (Object)this);
            }
            case WEST: {
                return UP;
            }
            case UP: {
                return EAST;
            }
            case DOWN:
        }
        return WEST;
    }

    public EnumFacing rotateYCCW() {
        switch (this) {
            case NORTH: {
                return WEST;
            }
            case EAST: {
                return NORTH;
            }
            case SOUTH: {
                return EAST;
            }
            case WEST: {
                return SOUTH;
            }
        }
        throw new IllegalStateException("Unable to get CCW facing of " + (Object)this);
    }

    public int getFrontOffsetX() {
        return this.axis == Axis.X ? this.axisDirection.getOffset() : 0;
    }

    public int getFrontOffsetY() {
        return this.axis == Axis.Y ? this.axisDirection.getOffset() : 0;
    }

    public int getFrontOffsetZ() {
        return this.axis == Axis.Z ? this.axisDirection.getOffset() : 0;
    }

    public String getName2() {
        return this.name;
    }

    public static EnumFacing byName(String name) {
        return name == null ? null : (EnumFacing)NAME_LOOKUP.get(name.toLowerCase());
    }

    public static EnumFacing getFront(int index) {
        return VALUES[MathHelper.abs_int(index % VALUES.length)];
    }

    public static EnumFacing getHorizontal(int p_176731_0_) {
        return HORIZONTALS[MathHelper.abs_int(p_176731_0_ % HORIZONTALS.length)];
    }

    public static EnumFacing fromAngle(double angle) {
        return EnumFacing.getHorizontal(MathHelper.floor_double(angle / 90.0.5) & 3);
    }

    public static EnumFacing random(Random rand) {
        return EnumFacing.values()[rand.nextInt(EnumFacing.values().length)];
    }

    public static EnumFacing func_176737_a(float p_176737_0_, float p_176737_1_, float p_176737_2_) {
        EnumFacing var3 = NORTH;
        float var4 = Float.MIN_VALUE;
        EnumFacing[] var5 = EnumFacing.values();
        for (EnumFacing var8 : var5) {
            float var9 = p_176737_0_ * (float)var8.directionVec.getX() + p_176737_1_ * (float)var8.directionVec.getY() + p_176737_2_ * (float)var8.directionVec.getZ();
            if (var9 <= var4) continue;
            var4 = var9;
            var3 = var8;
        }
        return var3;
    }

    public String toString() {
        return this.name;
    }

    @Generated
    public int getIndex() {
        return this.index;
    }

    @Generated
    public int getHorizontalIndex() {
        return this.horizontalIndex;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public Axis getAxis() {
        return this.axis;
    }

    @Generated
    public AxisDirection getAxisDirection() {
        return this.axisDirection;
    }

    @Generated
    public P1779531 getDirectionVec() {
        return this.directionVec;
    }

    static {
        VALUES = new EnumFacing[6];
        HORIZONTALS = new EnumFacing[4];
        NAME_LOOKUP = Maps.newHashMap();
        $VALUES = new EnumFacing[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
        EnumFacing[] var0 = EnumFacing.values();
        int var1 = var0.length;
        for (int var2 = 0; var2 < var1; ++var2) {
            EnumFacing var3;
            EnumFacing.VALUES[var3.index] = var3 = var0[var2];
            if (var3.getAxis().isHorizontal()) {
                EnumFacing.HORIZONTALS[var3.horizontalIndex] = var3;
            }
            NAME_LOOKUP.put(var3.getName2().toLowerCase(), var3);
        }
    }

    public static enum Axis implements Predicate
    {
        X("X", 0, "X", 0, "x", Plane.HORIZONTAL),
        Y("Y", 1, "Y", 1, "y", Plane.VERTICAL),
        Z("Z", 2, "Z", 2, "z", Plane.HORIZONTAL);

        private static final Map NAME_LOOKUP;
        private final String name;
        private final Plane plane;
        private static final Axis[] $VALUES;
        private static final Axis[] $VALUES$;

        private Axis(String p_i46390_1_, int p_i46390_2_, String p_i46015_1_, int p_i46015_2_, String name, Plane plane) {
        }

        public static Axis byName(String name) {
            return name == null ? null : (Axis)NAME_LOOKUP.get(name.toLowerCase());
        }

        public String getName2() {
            return this.name;
        }

        public boolean isVertical() {
            return this.plane == Plane.VERTICAL;
        }

        public boolean isHorizontal() {
            return this.plane == Plane.HORIZONTAL;
        }

        public String toString() {
            return this.name;
        }

        public boolean apply(EnumFacing facing) {
            return facing != null && facing.getAxis() == this;
        }

        public boolean apply(Object p_apply_1_) {
            return this.apply((EnumFacing)p_apply_1_);
        }

        @Generated
        public String getName() {
            return this.name;
        }

        @Generated
        public Plane getPlane() {
            return this.plane;
        }

        static {
            NAME_LOOKUP = Maps.newHashMap();
            $VALUES = new Axis[]{X, Y, Z};
            $VALUES$ = new Axis[]{X, Y, Z};
            for (Axis var3 : Axis.values()) {
                NAME_LOOKUP.put(var3.getName2().toLowerCase(), var3);
            }
        }
    }

    public static enum AxisDirection {
        POSITIVE("POSITIVE", 0, "POSITIVE", 0, 1, "Towards positive"),
        NEGATIVE("NEGATIVE", 1, "NEGATIVE", 1, -1, "Towards negative");

        private final int offset;
        private final String description;
        private static final AxisDirection[] $VALUES;

        private AxisDirection(String p_i46391_1_, int p_i46391_2_, String p_i46014_1_, int p_i46014_2_, int offset, String description) {
        }

        public int getOffset() {
            return this.offset;
        }

        public String toString() {
            return this.description;
        }

        static {
            $VALUES = new AxisDirection[]{POSITIVE, NEGATIVE};
        }
    }

    public static enum Plane implements Predicate,
    Iterable
    {
        HORIZONTAL("HORIZONTAL", 0, "HORIZONTAL", 0),
        VERTICAL("VERTICAL", 1, "VERTICAL", 1);

        private static final Plane[] $VALUES;

        private Plane(String p_i46392_1_, int p_i46392_2_, String p_i46013_1_, int p_i46013_2_) {
        }

        public EnumFacing[] facings() {
            switch (this) {
                case HORIZONTAL: {
                    return new EnumFacing[]{NORTH, EAST, SOUTH, WEST};
                }
                case VERTICAL: {
                    return new EnumFacing[]{UP, DOWN};
                }
            }
            throw new Error("Someone's been tampering with the universe!");
        }

        public EnumFacing random(Random rand) {
            EnumFacing[] var2 = this.facings();
            return var2[rand.nextInt(var2.length)];
        }

        public boolean apply(EnumFacing facing) {
            return facing != null && facing.getAxis().getPlane() == this;
        }

        public Iterator iterator() {
            return Iterators.forArray((Object[])this.facings());
        }

        public boolean apply(Object p_apply_1_) {
            return this.apply((EnumFacing)p_apply_1_);
        }

        static {
            $VALUES = new Plane[]{HORIZONTAL, VERTICAL};
        }
    }
}

