/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Material
 */
package ai.idealistic.spartan.utils.minecraft.world;

import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Material;

public class GroundUtils {
    private static final List<Double> blockHeights = new ArrayList<Double>();
    private static final Set<Material> abstractMaterials = new HashSet<Material>();
    private static final boolean v1_9 = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9);
    private static final boolean v1_15 = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_15);
    public static final double maxBoundingBox = 0.9375;
    public static final double minBoundingBox = 0.015625;
    public static final double maxPlayerStep = 0.5625;
    public static final double maxHeightLengthRatio;
    public static final int maxHeightLength;

    public static Collection<Double> getHeights() {
        return blockHeights;
    }

    public static boolean isAbstract(Material m) {
        return abstractMaterials.contains(m);
    }

    public static boolean blockHeightExists(double d) {
        return blockHeights.contains(d);
    }

    @Generated
    public static Set<Material> getAbstractMaterials() {
        return abstractMaterials;
    }

    static {
        blockHeights.add(0.9375);
        blockHeights.add(0.015625);
        blockHeights.add(0.5625);
        blockHeights.add(0.0);
        blockHeights.add(0.6625);
        blockHeights.add(0.1875);
        blockHeights.add(0.8125);
        blockHeights.add(0.375);
        blockHeights.add(0.0625);
        blockHeights.add(0.125);
        blockHeights.add(0.4375);
        blockHeights.add(0.875);
        blockHeights.add(0.6875);
        blockHeights.add(0.09375);
        blockHeights.add(0.3125);
        blockHeights.add(0.625);
        blockHeights.add(0.25);
        blockHeights.add(0.75);
        blockHeights.add(0.5);
        for (Material m : Material.values()) {
            if (!(m == Material.SNOW || BlockUtils.areShulkerBoxes(m) || BlockUtils.isInteractiveAndPassable(null, m) || v1_9 && m == Material.END_ROD || v1_15 && m == Material.HONEY_BLOCK) && (!MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_19) || m != Material.MUD)) continue;
            abstractMaterials.add(m);
        }
        int maxHeightLengthLocal = 0;
        for (double height : blockHeights) {
            maxHeightLengthLocal = Math.max(maxHeightLengthLocal, Double.toString(height).length() - 2);
        }
        maxHeightLength = maxHeightLengthLocal;
        maxHeightLengthRatio = 1.0 / Math.pow(10.0, maxHeightLength);
    }
}

