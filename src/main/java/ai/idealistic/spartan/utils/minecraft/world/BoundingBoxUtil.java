/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.data.Bisected$Half
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.type.Slab
 *  org.bukkit.block.data.type.Slab$Type
 *  org.bukkit.block.data.type.Stairs
 *  org.bukkit.material.MaterialData
 *  org.bukkit.material.Stairs
 *  org.bukkit.material.Step
 *  org.bukkit.material.WoodenStep
 */
package ai.idealistic.spartan.utils.minecraft.world;

import ai.idealistic.spartan.utils.minecraft.entity.AxisMatrix;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;

public class BoundingBoxUtil {
    public static List<AxisMatrix> getBoundingBoxes(Block block) {
        ArrayList<AxisMatrix> boxes = new ArrayList<AxisMatrix>();
        double x = block.getX();
        double y = block.getY();
        double z = block.getZ();
        BlockData data = block.getBlockData();
        if (data instanceof Slab) {
            Slab slab = (Slab)data;
            if (slab.getType() == Slab.Type.TOP) {
                boxes.add(BoundingBoxUtil.wrap(new AxisMatrix(x, y.5, z, x + 1.0, y + 1.0, z + 1.0)));
            } else {
                boxes.add(BoundingBoxUtil.wrap(new AxisMatrix(x, y, z, x + 1.0, y.5, z + 1.0)));
            }
        } else if (data instanceof Stairs) {
            AxisMatrix step;
            AxisMatrix base;
            Stairs stairs = (Stairs)data;
            boolean isTop = false;
            isTop = stairs.getHalf() == Bisected.Half.TOP;
            BlockFace facing = stairs.getFacing();
            if (!isTop) {
                base = new AxisMatrix(x, y, z, x + 1.0, y.5, z + 1.0);
                switch (facing) {
                    case NORTH: {
                        step = new AxisMatrix(x, y.5, z.5, x + 1.0, y + 1.0, z + 1.0);
                        break;
                    }
                    case SOUTH: {
                        step = new AxisMatrix(x, y.5, z, x + 1.0, y + 1.0, z.5);
                        break;
                    }
                    case EAST: {
                        step = new AxisMatrix(x, y.5, z, x.5, y + 1.0, z + 1.0);
                        break;
                    }
                    case WEST: {
                        step = new AxisMatrix(x.5, y.5, z, x + 1.0, y + 1.0, z + 1.0);
                        break;
                    }
                    default: {
                        step = new AxisMatrix(x, y.5, z, x + 1.0, y + 1.0, z + 1.0);
                        break;
                    }
                }
            } else {
                base = new AxisMatrix(x, y.5, z, x + 1.0, y + 1.0, z + 1.0);
                switch (facing) {
                    case NORTH: {
                        step = new AxisMatrix(x, y, z.5, x + 1.0, y.5, z + 1.0);
                        break;
                    }
                    case SOUTH: {
                        step = new AxisMatrix(x, y, z, x + 1.0, y.5, z.5);
                        break;
                    }
                    case EAST: {
                        step = new AxisMatrix(x, y, z, x.5, y.5, z + 1.0);
                        break;
                    }
                    case WEST: {
                        step = new AxisMatrix(x.5, y, z, x + 1.0, y.5, z + 1.0);
                        break;
                    }
                    default: {
                        step = new AxisMatrix(x, y, z, x + 1.0, y.5, z + 1.0);
                    }
                }
            }
            boxes.add(BoundingBoxUtil.wrap(base));
            boxes.add(BoundingBoxUtil.wrap(step));
        } else {
            if (BoundingBoxUtil.ignore(block.getType().toString().toLowerCase())) {
                return null;
            }
            boxes.add(BoundingBoxUtil.wrap(new AxisMatrix(x, y, z, x + 1.0, y + 1.0, z + 1.0)));
        }
        return boxes;
    }

    public static List<AxisMatrix> getBoundingBoxesLegacy(Block block) {
        ArrayList<AxisMatrix> boxes = new ArrayList<AxisMatrix>();
        double x = block.getX();
        double y = block.getY();
        double z = block.getZ();
        MaterialData data = block.getState().getData();
        if (data instanceof Step || data instanceof WoodenStep) {
            byte dataValue = block.getData();
            if ((dataValue & 8) != 0) {
                boxes.add(BoundingBoxUtil.wrap(new AxisMatrix(x, y.5, z, x + 1.0, y + 1.0, z + 1.0)));
            } else {
                boxes.add(BoundingBoxUtil.wrap(new AxisMatrix(x, y, z, x + 1.0, y.5, z + 1.0)));
            }
        } else if (data instanceof org.bukkit.material.Stairs) {
            AxisMatrix step;
            AxisMatrix base;
            boolean isTop;
            org.bukkit.material.Stairs stairs = (org.bukkit.material.Stairs)data;
            isTop = (block.getData() & 4) != 0;
            if (!isTop) {
                base = new AxisMatrix(x, y, z, x + 1.0, y.5, z + 1.0);
                switch (stairs.getFacing()) {
                    case NORTH: {
                        step = new AxisMatrix(x, y.5, z.5, x + 1.0, y + 1.0, z + 1.0);
                        break;
                    }
                    case SOUTH: {
                        step = new AxisMatrix(x, y.5, z, x + 1.0, y + 1.0, z.5);
                        break;
                    }
                    case EAST: {
                        step = new AxisMatrix(x, y.5, z, x.5, y + 1.0, z + 1.0);
                        break;
                    }
                    case WEST: {
                        step = new AxisMatrix(x.5, y.5, z, x + 1.0, y + 1.0, z + 1.0);
                        break;
                    }
                    default: {
                        step = new AxisMatrix(x, y.5, z, x + 1.0, y + 1.0, z + 1.0);
                        break;
                    }
                }
            } else {
                base = new AxisMatrix(x, y.5, z, x + 1.0, y + 1.0, z + 1.0);
                switch (stairs.getFacing()) {
                    case NORTH: {
                        step = new AxisMatrix(x, y, z.5, x + 1.0, y.5, z + 1.0);
                        break;
                    }
                    case SOUTH: {
                        step = new AxisMatrix(x, y, z, x + 1.0, y.5, z.5);
                        break;
                    }
                    case EAST: {
                        step = new AxisMatrix(x, y, z, x.5, y.5, z + 1.0);
                        break;
                    }
                    case WEST: {
                        step = new AxisMatrix(x.5, y, z, x + 1.0, y.5, z + 1.0);
                        break;
                    }
                    default: {
                        step = new AxisMatrix(x, y, z, x + 1.0, y.5, z + 1.0);
                    }
                }
            }
            boxes.add(BoundingBoxUtil.wrap(base));
            boxes.add(BoundingBoxUtil.wrap(step));
        } else {
            if (BoundingBoxUtil.ignore(block.getType().toString().toLowerCase())) {
                return null;
            }
            boxes.add(BoundingBoxUtil.wrap(new AxisMatrix(x, y, z, x + 1.0, y + 1.0, z + 1.0)));
        }
        return boxes;
    }

    public static boolean ignore(String block) {
        return block.matches(".*(snow|step|frame|table|slab|stair|ladder|vine|waterlily|wall|carpet|fence|rod|bed|skull|pot|hopper|door|bars|piston|lily).*");
    }

    public static AxisMatrix wrap(AxisMatrix bb) {
        return bb.expand(-1.0E-4, -1.0E-4, -1.0E-4);
    }
}

