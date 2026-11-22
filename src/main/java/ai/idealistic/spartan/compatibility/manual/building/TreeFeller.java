/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.block.Block
 */
package ai.idealistic.spartan.compatibility.manual.building;

import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import org.bukkit.block.Block;

public class TreeFeller {
    public static boolean canCancel(Block b) {
        return Compatibility.CompatibilityType.TREE_FELLER.isFunctional() && BlockUtils.areWoods(b.getType());
    }
}

