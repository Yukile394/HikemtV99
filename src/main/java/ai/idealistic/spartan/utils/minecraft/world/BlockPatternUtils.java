/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 */
package ai.idealistic.spartan.utils.minecraft.world;

import ai.idealistic.spartan.abstraction.world.ServerLocation;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;

public class BlockPatternUtils {
    public static boolean isBlockPattern(double[][] positions, ServerLocation location, boolean surroundings, Material match) {
        if (match != null) {
            for (double[] coordinates : positions) {
                if (surroundings) {
                    for (ServerLocation surroundingLocation : location.getSurroundingLocations(coordinates[0], coordinates[1], coordinates[2])) {
                        Material material = surroundingLocation.getBlock().getTypeOrNull();
                        if (material == null || match != material) continue;
                        return true;
                    }
                    continue;
                }
                Material material = location.clone().add(coordinates[0], coordinates[1], coordinates[2]).getBlock().getTypeOrNull();
                if (material == null || match != material) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isBlockPattern(double[][] positions, Location location, boolean surroundings, Material match) {
        return BlockPatternUtils.isBlockPattern(positions, new ServerLocation(location), surroundings, match);
    }

    @SafeVarargs
    public static boolean isBlockPattern(double[][] positions, ServerLocation location, boolean surroundings, Set<Material> ... sets) {
        for (double[] coordinates : positions) {
            if (surroundings) {
                for (ServerLocation surroundingLocation : location.getSurroundingLocations(coordinates[0], coordinates[1], coordinates[2])) {
                    Material material = surroundingLocation.getBlock().getTypeOrNull();
                    if (material == null) continue;
                    for (Set<Material> set : sets) {
                        if (!set.contains(material)) continue;
                        return true;
                    }
                }
                continue;
            }
            Material material = location.clone().add(coordinates[0], coordinates[1], coordinates[2]).getBlock().getTypeOrNull();
            if (material == null) continue;
            for (Set<Material> set : sets) {
                if (!set.contains(material)) continue;
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public static boolean isBlockPattern(double[][] positions, Location location, boolean surroundings, Set<Material> ... sets) {
        return BlockPatternUtils.isBlockPattern(positions, new ServerLocation(location), surroundings, sets);
    }

    @SafeVarargs
    public static boolean isBlockPattern(double[] coordinates, ServerLocation location, boolean surroundings, Set<Material> ... sets) {
        block4: {
            double z;
            double y;
            double x;
            block3: {
                x = coordinates[0];
                y = coordinates[1];
                z = coordinates[2];
                if (!surroundings) break block3;
                for (ServerLocation surroundingLocation : location.getSurroundingLocations(x, y, z)) {
                    Material material = surroundingLocation.getBlock().getTypeOrNull();
                    if (material == null) continue;
                    for (Set<Material> set : sets) {
                        if (!set.contains(material)) continue;
                        return true;
                    }
                }
                break block4;
            }
            Material material = location.clone().add(x, y, z).getBlock().getTypeOrNull();
            if (material == null) break block4;
            for (Set<Material> set : sets) {
                if (!set.contains(material)) continue;
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public static boolean isBlockPattern(double[] coordinates, Location location, boolean surroundings, Set<Material> ... sets) {
        return BlockPatternUtils.isBlockPattern(coordinates, new ServerLocation(location), surroundings, sets);
    }
}

