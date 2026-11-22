/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.utils.minecraft.entity;

import ai.idealistic.spartan.utils.minecraft.vector.Vec3;
import ai.idealistic.spartan.utils.minecraft.world.BlockPos;
import ai.idealistic.spartan.utils.minecraft.world.EnumFacing;

public class MovingObjectPosition {
    private BlockPos blockPos;
    public MovingObjectType typeOfHit;
    public EnumFacing sideHit;
    public Vec3 hitVec;

    public MovingObjectPosition(Vec3 hitVecIn, EnumFacing facing, BlockPos blockPosIn) {
        this(MovingObjectType.BLOCK, hitVecIn, facing, blockPosIn);
    }

    public MovingObjectPosition(Vec3 p_i45552_1_, EnumFacing facing) {
        this(MovingObjectType.BLOCK, p_i45552_1_, facing, BlockPos.ORIGIN);
    }

    public MovingObjectPosition(MovingObjectType typeOfHitIn, Vec3 hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn) {
        this.typeOfHit = typeOfHitIn;
        this.blockPos = blockPosIn;
        this.sideHit = sideHitIn;
        this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
    }

    public MovingObjectPosition(Vec3 hitVecIn) {
        this.typeOfHit = MovingObjectType.ENTITY;
        this.hitVec = hitVecIn;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public String toString() {
        return "HitResult{type=" + (Object)this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + (Object)this.sideHit + ", pos=" + this.hitVec + '}';
    }

    public static enum MovingObjectType {
        MISS,
        BLOCK,
        ENTITY;
    }
}

