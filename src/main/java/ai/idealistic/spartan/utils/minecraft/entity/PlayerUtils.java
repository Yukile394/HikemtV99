/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.utils.minecraft.entity;

import ai.idealistic.spartan.abstraction.protocol.ExtendedPotionEffect;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.math.Distance;
import ai.idealistic.spartan.utils.minecraft.entity.PotionEffectUtils;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;

public class PlayerUtils {
    public static final boolean slowFall = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13);
    public static final boolean dolphinsGrace = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13);
    public static final boolean soulSpeed = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16);
    public static final boolean levitation = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9);
    public static final boolean elytra = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9);
    public static final boolean trident = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13);
    private static final Material gold_sword = Spade.get("gold_sword");
    private static final Material wood_sword = Spade.get("wood_sword");
    private static final Material gold_axe = Spade.get("gold_axe");
    private static final Material wood_axe = Spade.get("wood_axe");
    private static final Material gold_pickaxe = Spade.get("gold_pickaxe");
    private static final Material wood_pickaxe = Spade.get("wood_pickaxe");
    private static final Material diamond_spade = Spade.get("diamond_spade");
    private static final Material iron_spade = Spade.get("iron_spade");
    private static final Material gold_spade = Spade.get("gold_spade");
    private static final Material stone_spade = Spade.get("stone_spade");
    private static final Material wood_spade = Spade.get("wood_spade");
    public static final double optimizationY = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) ? 0.005 : 0.003;
    public static final double airDrag = Distance.floatDouble(0.98);
    public static final double waterDrag = Distance.floatDouble(0.8);
    public static final double lavaDrag = 0.5;
    public static final double jumpAcceleration = Distance.floatDouble(0.42);
    public static final double airAcceleration = 0.08;
    public static final double airAccelerationUnloaded = Distance.floatDouble(0.098);
    public static final double slowFallAcceleration = 0.01;
    public static final double liquidAcceleration = 0.02;
    public static final double chunk = 16.0;
    public static final double climbingUpDefault = 0.12 * airDrag;
    public static final double climbingDownDefault = Distance.floatDouble(0.15);
    public static final double honeyBlockDownDefault = Distance.floatDouble(0.13) * airDrag;
    public static final double webBlockDownDefault = Distance.floatDouble(0.64) * airDrag;
    public static final double maxJumpingMotionDifference;
    public static final int playerInventorySlots = 46;
    public static final int height;
    public static final int fallDamageAboveBlocks = 3;
    private static final Map<Byte, List<Double>> jumpsValues;
    private static final Map<PotionEffectType, Long> handledPotionEffects;

    public static boolean isJumping(double d, int jump, double diff) {
        if (d > 0.0) {
            for (double value : jumpsValues.get((byte)jump)) {
                if (!(Math.abs(value - d) < diff)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean justJumped(double d, int jump, double diff) {
        if (d > 0.0) {
            for (double value : jumpsValues.get((byte)jump)) {
                if (!(Math.abs(value - d) < diff)) continue;
                return true;
            }
        }
        return false;
    }

    public static List<Double> getJumpMotions(int jump) {
        return jumpsValues.get((byte)jump);
    }

    public static double getJumpMotionSum(int jump) {
        return jumpsValues.get((byte)jump).stream().mapToDouble(Double::(double)Value).sum();
    }

    public static double getJumpMotion(int jump, int tick) {
        return jumpsValues.get((byte)jump).get(tick);
    }

    public static int getJumpTicks(int jump) {
        return PlayerUtils.getJumpMotions(jump).size();
    }

    public static double calculateTerminalVelocity(double drag, double acceleration) {
        return 1.0 / (1.0 - drag) * acceleration;
    }

    public static double calculateNextFallMotion(double motion, double acceleration, double drag) {
        double terminalVelocity = PlayerUtils.calculateTerminalVelocity(acceleration, drag);
        if (motion >= -terminalVelocity) {
            return (motion + acceleration) * drag;
        }
        return Double.MIN_VALUE;
    }

    public static boolean isSpadeItem(Material m) {
        return m == diamond_spade || m == iron_spade || m == gold_spade || m == stone_spade || m == wood_spade || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16) && m == Material.NETHERITE_HOE;
    }

    public static boolean isPickaxeItem(Material m) {
        return m == Material.DIAMOND_PICKAXE || m == Material.IRON_PICKAXE || m == Material.STONE_PICKAXE || m == gold_pickaxe || m == wood_pickaxe || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16) && m == Material.NETHERITE_PICKAXE;
    }

    public static boolean isAxeItem(Material m) {
        return m == Material.DIAMOND_AXE || m == Material.IRON_AXE || m == Material.STONE_AXE || m == gold_axe || m == wood_axe || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16) && m == Material.NETHERITE_AXE;
    }

    public static boolean isSwordItem(Material type) {
        return type == Material.DIAMOND_SWORD || type == gold_sword || type == Material.IRON_SWORD || type == Material.STONE_SWORD || type == wood_sword || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16) && type == Material.NETHERITE_SWORD;
    }

    public static int getPotionLevel(PlayerProtocol protocol, PotionEffectType potionEffectType) {
        ExtendedPotionEffect potionEffect = protocol.getPotionEffect(potionEffectType, handledPotionEffects.getOrDefault(potionEffectType, 0L));
        if (potionEffect != null) {
            return potionEffect.bukkitEffect.getAmplifier();
        }
        return -1;
    }

    static {
        jumpsValues = new LinkedHashMap<Byte, List<Double>>();
        handledPotionEffects = new LinkedHashMap<PotionEffectType, Long>();
        handledPotionEffects.put(PotionEffectUtils.JUMP, (long)NumberConversions.round((double)100.0) * 50L);
        handledPotionEffects.put(PotionEffectType.SPEED, (long)NumberConversions.round((double)40.0) * 50L);
        if (dolphinsGrace) {
            handledPotionEffects.put(PotionEffectType.DOLPHINS_GRACE, (long)NumberConversions.round((double)20.0) * 50L);
        }
        if (slowFall) {
            handledPotionEffects.put(PotionEffectType.SLOW_FALLING, 500L);
        }
        if (levitation) {
            handledPotionEffects.put(PotionEffectType.LEVITATION, 500L);
        }
        for (int jumpEffect = 0; jumpEffect < 256; ++jumpEffect) {
            ArrayList<Double> jumps = new ArrayList<Double>();
            double jump = jumpAcceleration.1;
            while (jump > 0.0) {
                jumps.add(jump);
                jump = (jump.08) * airDrag;
            }
            jumpsValues.put((byte)jumpEffect, jumps);
        }
        Iterator<Double> iterator = jumpsValues.get(0).iterator();
        double maxJumpingMotionDifferenceLocal = Double.MIN_VALUE;
        double previousJumpingMotion = iterator.next();
        while (iterator.hasNext()) {
            double currentMotion = iterator.next();
            maxJumpingMotionDifferenceLocal = Math.max(previousJumpingMotion - currentMotion, maxJumpingMotionDifferenceLocal);
            previousJumpingMotion = currentMotion;
        }
        maxJumpingMotionDifference = maxJumpingMotionDifferenceLocal;
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            List worlds = Bukkit.getWorlds();
            if (!worlds.isEmpty()) {
                int max = 256;
                for (World world : worlds) {
                    max = Math.max(world.getMaxHeight(), max);
                }
                height = max;
            } else {
                height = 256;
            }
        } else {
            height = 256;
        }
    }
}

