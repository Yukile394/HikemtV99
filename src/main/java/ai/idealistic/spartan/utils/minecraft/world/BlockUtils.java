/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.Waterlogged
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.utils.minecraft.world;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerBlock;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import java.util.HashSet;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.inventory.ItemStack;

public final class BlockUtils {
    public static final int[][] DIRS;
    public static boolean blockDataExists;
    public static final long sensitiveBlockBreakTime = 250L;
    public static final Material head;
    public static final Material iron_bars;
    public static final Material cake;
    public static final Material magma;
    public static final Material beetroot_block;
    public static final Set<Material> air;
    public static final Set<Material> solid;
    public static final Set<Material> sensitive;
    public static final Set<Material> editable;
    public static final Set<Material> chest;
    public static final Set<Material> plate;
    public static final Set<Material> ice;
    public static final Set<Material> blue_ice;
    public static final Set<Material> glass;
    public static final Set<Material> glass_pane;
    public static final Set<Material> slabs;
    public static final Set<Material> climbable;
    public static final Set<Material> climbableOriginal;
    public static final Set<Material> door;
    public static final Set<Material> entity_blocks;
    public static final Set<Material> trap_door;
    public static final Set<Material> liquid;
    public static final Set<Material> banner;
    public static final Set<Material> carpet;
    public static final Set<Material> bed;
    public static final Set<Material> shulker_box;
    public static final Set<Material> stairs;
    public static final Set<Material> fence;
    public static final Set<Material> fence_gate;
    public static final Set<Material> heads;
    public static final Set<Material> leaves;
    public static final Set<Material> egg;
    public static final Set<Material> coral_fan;
    public static final Set<Material> pot;
    public static final Set<Material> anvil;
    public static final Set<Material> cobble_walls;
    public static final Set<Material> terracotta;
    public static final Set<Material> concrete;
    public static final Set<Material> candle;
    public static final Set<Material> candleCake;
    public static final Set<Material> dripleaf;
    public static final Set<Material> ores;
    public static final Set<Material> wood;
    public static final Set<Material> wool;
    public static final Set<Material> wire;
    public static final Set<Material> semi_solid;
    public static final Set<Material> changeable;
    public static final Set<Material> walls;
    public static final Set<Material> (int)eractive_bushes;
    public static final Set<Material> scaffolding;
    public static final Set<Material> (int)eractive_snow;
    public static final Set<Material> (int)eractive_and_passable;
    public static final Set<Material> honey_block;
    public static final Set<Material> slime_block;
    public static final Set<Material> web;
    public static final Set<Material> piston;

    public static boolean endsWith(String s, String ending) {
        return s.endsWith(ending) && !s.contains("LEGACY_");
    }

    private static boolean startsWith(String s, String start) {
        return s.startsWith(start) && !s.contains("LEGACY_");
    }

    private static boolean contains(String s, String start) {
        return s.contains(start) && !s.contains("LEGACY_");
    }

    public static int getMaxHeight(World world) {
        return MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17) ? world.getMaxHeight() : 256;
    }

    public static int getMinHeight(World world) {
        return MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17) ? world.getMinHeight() : 0;
    }

    public static boolean areCandles(Material m) {
        return candle.contains(m);
    }

    public static boolean areCandleCakes(Material m) {
        return candleCake.contains(m);
    }

    public static boolean isScaffoldingBlock(Material m) {
        return scaffolding.contains(m);
    }

    public static boolean areWalls(Material m) {
        return walls.contains(m);
    }

    public static boolean areHeads(Material m) {
        return heads.contains(m);
    }

    public static boolean areFlowerPots(Material m) {
        return pot.contains(m);
    }

    public static boolean isLiquid(Block block) {
        return BlockUtils.isLiquid(block.getType()) || block.isLiquid();
    }

    public static boolean isLiquid(Material m) {
        return liquid.contains(m);
    }

    public static boolean isWaterLogged(BlockData blockData) {
        return blockData instanceof Waterlogged && WaterloggedblockData.isWaterlogged();
    }

    public static boolean isWaterLogged(Block block) {
        return blockDataExists && BlockUtils.isWaterLogged(block.getBlockData());
    }

    public static boolean isWaterLogged(ServerBlock block) {
        return blockDataExists && BlockUtils.isWaterLogged(block.getType().createBlockData());
    }

    public static boolean isLiquidOrWaterLogged(Object block, boolean lava) {
        return block instanceof Block ? BlockUtils.isLiquidOrWaterLogged((Block)block, lava) : blockDataExists && block instanceof BlockData && BlockUtils.isLiquidOrWaterLogged(BlockDatablock, lava);
    }

    public static boolean isLiquidOrWaterLogged(Block block, boolean lava) {
        return !(!BlockUtils.isLiquid(block) && !BlockUtils.isWaterLogged(block) || !lava && block.getType() == Spade.get("lava"));
    }

    public static boolean isLiquidOrWaterLogged(BlockData blockData, boolean lava) {
        return (blockDataExists && blockData instanceof Waterlogged && WaterloggedblockData.isWaterlogged() || BlockUtils.isLiquid(blockData.getMaterial())) && (lava || blockData.getMaterial() != Spade.get("lava"));
    }

    public static boolean areStairs(Material m) {
        return stairs.contains(m);
    }

    public static boolean areCobbleWalls(Material m) {
        return cobble_walls.contains(m);
    }

    public static boolean areSlabs(Material m) {
        return slabs.contains(m);
    }

    public static boolean areLeaves(Material m) {
        return leaves.contains(m);
    }

    public static boolean areCarpets(Material m) {
        return carpet.contains(m);
    }

    public static boolean areBeds(Material m) {
        return bed.contains(m);
    }

    public static boolean areEggs(Material m) {
        return egg.contains(m);
    }

    public static boolean areTrapdoors(Material m) {
        return trap_door.contains(m);
    }

    public static boolean areFences(Material m) {
        return fence.contains(m);
    }

    public static boolean areFenceGates(Material m) {
        return fence_gate.contains(m);
    }

    public static boolean isPowderSnow(Material m) {
        return (int)eractive_snow.contains(m);
    }

    public static boolean areDripLeafs(Material m) {
        return dripleaf.contains(m);
    }

    public static boolean areAnvils(Material m) {
        return anvil.contains(m);
    }

    public static boolean areWires(Material m) {
        return wire.contains(m);
    }

    public static boolean areChests(Material m) {
        return chest.contains(m);
    }

    public static boolean areDoors(Material m) {
        return door.contains(m);
    }

    public static boolean areGlasses(Material m) {
        return glass.contains(m);
    }

    public static boolean areStainedGlasses(Material m) {
        return glass_pane.contains(m);
    }

    public static boolean arePlates(Material m) {
        return plate.contains(m);
    }

    public static boolean areIceBlocks(Material m) {
        return ice.contains(m) || blue_ice.contains(m);
    }

    public static boolean areRegularIceBlocks(Material m) {
        return ice.contains(m);
    }

    public static boolean areBlueIceBlocks(Material m) {
        return blue_ice.contains(m);
    }

    public static boolean areWools(Material m) {
        return wool.contains(m);
    }

    public static boolean areEntityBlocks(Material m) {
        return entity_blocks.contains(m) || BlockUtils.areAnvils(m);
    }

    public static boolean areWebs(Material m) {
        return web.contains(m);
    }

    public static boolean arePistons(Material m) {
        return piston.contains(m);
    }

    public static boolean areShulkerBoxes(Material m) {
        return shulker_box.contains(m);
    }

    public static boolean areBanners(Material m) {
        return banner.contains(m);
    }

    public static boolean areCoralFans(Material m) {
        return coral_fan.contains(m);
    }

    public static boolean areTerracotta(Material m) {
        return terracotta.contains(m);
    }

    public static boolean areOres(Material m) {
        return ores.contains(m);
    }

    public static boolean areAir(Material m) {
        return air.contains(m);
    }

    public static boolean areWoods(Material m) {
        return wood.contains(m);
    }

    public static boolean isChangeable(Material m) {
        return changeable.contains(m);
    }

    public static boolean areConcrete(Material m) {
        return concrete.contains(m);
    }

    public static boolean areHoneyBlocks(Material m) {
        return honey_block.contains(m);
    }

    public static boolean areSlimeBlocks(Material m) {
        return slime_block.contains(m);
    }

    public static boolean areBouncingBlocks(Material material) {
        return honey_block.contains(material) || slime_block.contains(material) || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_12) && bed.contains(material);
    }

    public static boolean areInteractiveBushes(Material m) {
        return (int)eractive_bushes.contains(m);
    }

    public static boolean canClimb(Material m, boolean original) {
        return original ? climbableOriginal.contains(m) : climbable.contains(m);
    }

    public static boolean isSensitive(Material m) {
        return sensitive.contains(m);
    }

    private static boolean isSensitive(Material m, long time) {
        return time >= 0L && time <= 250L || sensitive.contains(m);
    }

    public static boolean isSensitive(PlayerProtocol p, Material m) {
        return BlockUtils.isSensitive(m, p == null ? -1L : Spade.getBlockBreakTime(p, p.getItemInHand(), m));
    }

    public static boolean isSolid(Material m) {
        return solid.contains(m) || semi_solid.contains(m) || m.isSolid();
    }

    public static boolean isFullSolid(Material m) {
        return solid.contains(m) && !semi_solid.contains(m);
    }

    public static boolean isSemiSolid(Material m) {
        return semi_solid.contains(m);
    }

    public static boolean isInteractiveAndPassable(PlayerProtocol p, Material material) {
        return (int)eractive_and_passable.contains(material) || p != null && p.isFrozen();
    }

    public static boolean hasMaterial(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() != Material.AIR;
    }

    public static String environmentToString(World.Environment e) {
        return BlockUtils.toString(e);
    }

    public static String blockToString(Block b) {
        return BlockUtils.toString(b.getType().toString());
    }

    public static String blockToString(ServerBlock b) {
        return BlockUtils.toString(b.getType().toString());
    }

    public static String materialToString(Material m) {
        return BlockUtils.toString(m);
    }

    private static String toString(Object obj) {
        return obj.toString().toLowerCase().replace("_", "-");
    }

    public static boolean isSlime(PlayerProtocol p, ServerLocation loc, int blocks) {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)) {
            if (p.isOnGround()) {
                return loc.getBlock().getType() == Material.SLIME_BLOCK;
            }
            ServerLocation loopLocation = loc.clone();
            if (!BlockUtils.isSolid(loopLocation.getBlock().getType())) {
                int y;
                for (int i = y = loc.getBlockY(); i > Math.max(BlockUtils.getMinHeight(p.getWorld()), y - blocks); --i) {
                    Material m = loopLocation.add(0.0, -1.0, 0.0).getBlock().getType();
                    if (m == Material.SLIME_BLOCK) {
                        return true;
                    }
                    if (BlockUtils.isSolid(m) && m != Material.SNOW && !BlockUtils.areCarpets(m) && !BlockUtils.areSlabs(m) && !BlockUtils.areStairs(m) && !BlockUtils.areWalls(m) && !BlockUtils.canClimb(m, false)) break;
                }
            }
        }
        return false;
    }

    public static boolean isBed(PlayerProtocol p, ServerLocation loc, int blocks) {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_12)) {
            if (p.isOnGround()) {
                return BlockUtils.areBeds(loc.getBlock().getType());
            }
            ServerLocation loopLocation = loc.clone();
            if (!BlockUtils.isSolid(loopLocation.getBlock().getType())) {
                int y;
                for (int i = y = loc.getBlockY(); i > Math.max(BlockUtils.getMinHeight(p.getWorld()), y - blocks); --i) {
                    Material m = loopLocation.add(0.0, -1.0, 0.0).getBlock().getType();
                    if (BlockUtils.areBeds(m)) {
                        return true;
                    }
                    if (BlockUtils.isSolid(m)) break;
                }
            }
        }
        return false;
    }

    public static boolean isBouncingBlock(PlayerProtocol p, ServerLocation loc, int blocks) {
        return BlockUtils.isSlime(p, loc, blocks) || BlockUtils.isBed(p, loc, blocks);
    }

    public static float getSlipperiness(ServerBlock block) {
        if (BlockUtils.areSlimeBlocks(block.getType())) {
            return 0.8f;
        }
        if (BlockUtils.areRegularIceBlocks(block.getType())) {
            return 0.98f;
        }
        if (BlockUtils.areBlueIceBlocks(block.getType())) {
            return 0.989f;
        }
        return 0.6f;
    }

    public static boolean isSurroundedByAir(Location loc) {
        World world = loc.getWorld();
        int blockX =  loc.getBlockX();
        int blockY =  loc.getBlockY();
        int blockZ =  loc.getBlockZ();
        for (int[] d : DIRS) {
            Block relative = world.getBlockAt((int)x[d], y + d[1], z + d[2]);
            if (relative.getType() == Material.AIR) continue;
            return false;
        }
        return true;
    }

    public static boolean isSurroundedByAir(PlayerProtocol protocol, Location loc) {
        World world = loc.getWorld();
        int blockX =  loc.getBlockX();
        int blockY =  loc.getBlockY();
        int blockZ =  loc.getBlockZ();
        for (int[] d : DIRS) {
            if (new ServerLocation(world, (double)x[d], y + d[1], z + d[2]).getBlock(protocol).getType() == Material.AIR) continue;
            return false;
        }
        return true;
    }

    @Generated
    private BlockUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        String s;
        DIRS = new int[][]{{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, 1, 0}, {0, -1, 0}};
        blockDataExists = ReflectionUtils.classExists("org.bukkit.block.data.BlockData");
        head = Spade.get("head");
        iron_bars = Spade.get("iron_bars");
        cake = Spade.get("cake");
        magma = Spade.get("magma");
        beetroot_block = Spade.get("beetroot_block");
        HashSet<Material> builder = new HashSet<Material>();
        HashSet<Material> helper = new HashSet<Material>();
        Material[] materials = Material.values();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.BUBBLE_COLUMN);
            builder.add(Material.KELP);
            builder.add(Material.KELP_PLANT);
            builder.add(Material.SEAGRASS);
            builder.add(Material.TALL_SEAGRASS);
            builder.add(Material.SEA_PICKLE);
            builder.add(Material.WATER);
            builder.add(Material.LAVA);
        } else {
            builder.add(Material.getMaterial("STATIONARY_WATER"));
            builder.add(Material.getMaterial("STATIONARY_LAVA"));
        }
        liquid = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)) {
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
                    builder.add(Material.GLOW_ITEM_FRAME);
                }
                builder.add(Material.SMITHING_TABLE);
                builder.add(Material.GRINDSTONE);
                builder.add(Material.FLETCHING_TABLE);
                builder.add(Material.STONECUTTER);
                builder.add(Material.CARTOGRAPHY_TABLE);
                builder.add(Material.BLAST_FURNACE);
                builder.add(Material.SMOKER);
                builder.add(Material.LOOM);
                builder.add(Material.BARREL);
                builder.add(Material.BELL);
            }
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_BUTTON")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("WOOD_BUTTON"));
            builder.add(Material.STONE_BUTTON);
        }
        builder.add(Material.ITEM_FRAME);
        builder.add(Material.HOPPER);
        builder.add(Material.JUKEBOX);
        builder.add(Material.NOTE_BLOCK);
        builder.add(Material.DROPPER);
        builder.add(Material.BREWING_STAND);
        builder.add(Material.LEVER);
        builder.add(Material.ANVIL);
        builder.add(Spade.get("crafting_table"));
        builder.add(Spade.get("repeater_on"));
        builder.add(Spade.get("repeater_off"));
        builder.add(Spade.get("comparator_on"));
        builder.add(Spade.get("comparator_off"));
        builder.add(Spade.get("enchanting_table"));
        builder.add(Spade.get("end_portal_frame"));
        builder.add(Spade.get("furnace"));
        editable = new HashSet<Material>(builder);
        builder.clear();
        builder.add(Material.CHEST);
        builder.add(Material.TRAPPED_CHEST);
        builder.add(Material.ENDER_CHEST);
        chest = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!s.contains("_PLATE")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("GOLD_PLATE"));
            builder.add(Material.getMaterial("IRON_PLATE"));
            builder.add(Material.getMaterial("STONE_PLATE"));
            builder.add(Material.getMaterial("WOOD_PLATE"));
        }
        plate = new HashSet<Material>(builder);
        builder.clear();
        builder.add(Spade.get("web"));
        web = new HashSet<Material>(builder);
        builder.clear();
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.contains(s, "WIRE")) continue;
            builder.add(m);
        }
        wire = new HashSet<Material>(builder);
        builder.clear();
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.endsWith(s, "_ORE")) continue;
            builder.add(m);
        }
        ores = new HashSet<Material>(builder);
        builder.clear();
        builder.add(Material.ICE);
        builder.add(Material.PACKED_ICE);
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
            builder.add(Material.FROSTED_ICE);
        }
        ice = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) && MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.BLUE_ICE);
        }
        blue_ice = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.GLASS_PANE);
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_STAINED_GLASS_PANE")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("THIN_GLASS"));
            builder.add(Material.getMaterial("STAINED_GLASS_PANE"));
        }
        glass_pane = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_20)) {
                builder.add(Material.BAMBOO_BLOCK);
                builder.add(Material.STRIPPED_BAMBOO_BLOCK);
                builder.add(Material.BAMBOO_MOSAIC);
                builder.add(Material.BAMBOO_PLANKS);
            }
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_LOG") && !BlockUtils.endsWith(s, "_WOOD")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("LOG"));
            builder.add(Material.getMaterial("LOG_2"));
        }
        wood = new HashSet<Material>(builder);
        builder.clear();
        builder.add(Material.AIR);
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_AIR")) continue;
                builder.add(m);
            }
        }
        air = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_STAINED_GLASS")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("STAINED_GLASS"));
        }
        builder.add(Material.GLASS);
        glass = new HashSet<Material>(builder);
        builder.clear();
        if (!MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.getMaterial("STEP"));
            builder.add(Material.getMaterial("WOOD_STEP"));
        }
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.contains(s, "_SLAB")) continue;
            builder.add(m);
        }
        slabs = new HashSet<Material>(builder);
        builder.clear();
        builder.add(Material.LADDER);
        builder.add(Material.VINE);
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.endsWith(s, "_VINES") && !BlockUtils.endsWith(s, "_VINES_PLANT")) continue;
            builder.add(m);
        }
        climbableOriginal = new HashSet<Material>(builder);
        builder.clear();
        builder.addAll(climbableOriginal);
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
                builder.add(Material.POWDER_SNOW);
            }
            builder.add(Material.SCAFFOLDING);
        }
        climbable = new HashSet<Material>(builder);
        builder.clear();
        builder.add(Material.SAND);
        builder.add(Material.GRAVEL);
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_12)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
                for (Material m : materials) {
                    s = m.toString();
                    if (!BlockUtils.endsWith(s, "_CONCRETE_POWDER")) continue;
                    builder.add(m);
                }
            } else {
                builder.add(Material.getMaterial("CONCRETE_POWDER"));
            }
        }
        entity_blocks = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.TERRACOTTA);
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_TERRACOTTA")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("STAINED_CLAY"));
        }
        terracotta = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_WOOL")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("WOOL"));
        }
        wool = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_12)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_CONCRETE")) continue;
                builder.add(m);
            }
        }
        concrete = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16)) {
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_19)) {
                    if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_20)) {
                        builder.add(Material.BAMBOO_DOOR);
                    }
                    builder.add(Material.MANGROVE_DOOR);
                }
                builder.add(Material.WARPED_DOOR);
                builder.add(Material.CRIMSON_DOOR);
            }
            builder.add(Material.IRON_DOOR);
            builder.add(Material.OAK_DOOR);
        } else {
            builder.add(Material.getMaterial("IRON_DOOR_BLOCK"));
            builder.add(Material.getMaterial("WOODEN_DOOR"));
        }
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)) {
            builder.add(Material.ACACIA_DOOR);
            builder.add(Material.BIRCH_DOOR);
            builder.add(Material.DARK_OAK_DOOR);
            builder.add(Material.JUNGLE_DOOR);
            builder.add(Material.SPRUCE_DOOR);
        }
        door = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_TRAPDOOR")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("TRAP_DOOR"));
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)) {
                builder.add(Material.IRON_TRAPDOOR);
            }
        }
        trap_door = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_CARPET")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("CARPET"));
        }
        carpet = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_BED")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("BED_BLOCK"));
        }
        bed = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_BANNER")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("STANDING_BANNER"));
            builder.add(Material.getMaterial("WALL_BANNER"));
        }
        banner = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_11)) {
            for (Material m : materials) {
                s = m.toString();
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
                    builder.add(Material.SHULKER_BOX);
                }
                if (!BlockUtils.endsWith(s, "_SHULKER_BOX")) continue;
                builder.add(m);
            }
        }
        shulker_box = new HashSet<Material>(builder);
        builder.clear();
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.endsWith(s, "_STAIRS")) continue;
            builder.add(m);
        }
        stairs = new HashSet<Material>(builder);
        builder.clear();
        if (!MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.getMaterial("FENCE"));
        }
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.endsWith(s, "_FENCE")) continue;
            builder.add(m);
        }
        fence = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "CANDLE")) continue;
                builder.add(m);
            }
        }
        candle = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "CANDLE_CAKE")) continue;
                builder.add(m);
            }
        }
        candleCake = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.contains(s, "DRIPLEAF")) continue;
                builder.add(m);
            }
        }
        dripleaf = new HashSet<Material>(builder);
        builder.clear();
        if (!MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.getMaterial("FENCE_GATE"));
        }
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.endsWith(s, "_FENCE_GATE")) continue;
            builder.add(m);
        }
        fence_gate = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.SKELETON_SKULL);
            builder.add(Material.SKELETON_WALL_SKULL);
            builder.add(Material.WITHER_SKELETON_SKULL);
            builder.add(Material.WITHER_SKELETON_WALL_SKULL);
            builder.add(Material.CREEPER_HEAD);
            builder.add(Material.CREEPER_WALL_HEAD);
            builder.add(Material.DRAGON_HEAD);
            builder.add(Material.DRAGON_WALL_HEAD);
            builder.add(Material.PLAYER_HEAD);
            builder.add(Material.PLAYER_WALL_HEAD);
            builder.add(Material.ZOMBIE_HEAD);
            builder.add(Material.ZOMBIE_WALL_HEAD);
        } else {
            builder.add(Material.getMaterial("SKULL"));
        }
        heads = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_LEAVES")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("LEAVES"));
            builder.add(Material.getMaterial("LEAVES_2"));
        }
        leaves = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_SPAWN_EGG")) continue;
                builder.add(m);
            }
        } else {
            builder.add(Material.getMaterial("MONSTER_EGG"));
        }
        egg = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.BRAIN_CORAL_FAN);
            builder.add(Material.BRAIN_CORAL);
            builder.add(Material.DEAD_BRAIN_CORAL_FAN);
            builder.add(Material.DEAD_BRAIN_CORAL);
            builder.add(Material.DEAD_BRAIN_CORAL_WALL_FAN);
            builder.add(Material.BUBBLE_CORAL_FAN);
            builder.add(Material.BUBBLE_CORAL);
            builder.add(Material.DEAD_BUBBLE_CORAL_FAN);
            builder.add(Material.DEAD_BUBBLE_CORAL);
            builder.add(Material.DEAD_BUBBLE_CORAL_WALL_FAN);
            builder.add(Material.FIRE_CORAL_FAN);
            builder.add(Material.FIRE_CORAL);
            builder.add(Material.DEAD_FIRE_CORAL_FAN);
            builder.add(Material.DEAD_FIRE_CORAL);
            builder.add(Material.DEAD_FIRE_CORAL_WALL_FAN);
            builder.add(Material.HORN_CORAL_FAN);
            builder.add(Material.HORN_CORAL);
            builder.add(Material.DEAD_HORN_CORAL_FAN);
            builder.add(Material.DEAD_HORN_CORAL);
            builder.add(Material.DEAD_HORN_CORAL_WALL_FAN);
            builder.add(Material.TUBE_CORAL_FAN);
            builder.add(Material.TUBE_CORAL);
            builder.add(Material.DEAD_TUBE_CORAL_FAN);
            builder.add(Material.DEAD_TUBE_CORAL);
            builder.add(Material.DEAD_TUBE_CORAL_WALL_FAN);
        }
        coral_fan = new HashSet<Material>(builder);
        builder.clear();
        builder.add(Material.FLOWER_POT);
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.startsWith(s, "POTTED_")) continue;
                builder.add(m);
            }
        }
        pot = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.ANVIL);
            builder.add(Material.CHIPPED_ANVIL);
            builder.add(Material.DAMAGED_ANVIL);
        } else {
            builder.add(Material.ANVIL);
        }
        anvil = new HashSet<Material>(builder);
        builder.clear();
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.endsWith(s, "_WALL")) continue;
            builder.add(m);
        }
        cobble_walls = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
            builder.add(Material.POWDER_SNOW);
        }
        (int)eractive_snow = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)) {
            builder.add(Material.SCAFFOLDING);
        }
        scaffolding = new HashSet<Material>(builder);
        builder.clear();
        for (Material m : materials) {
            if (!BlockUtils.areDripLeafs(m) && !BlockUtils.areWebs(m) && !BlockUtils.isPowderSnow(m) && !BlockUtils.isScaffoldingBlock(m)) continue;
            builder.add(m);
        }
        (int)eractive_and_passable = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            builder.add(Material.PISTON);
            builder.add(Material.STICKY_PISTON);
        } else {
            builder.add(Material.getMaterial("PISTON_BASE"));
            builder.add(Material.getMaterial("PISTON_STICKY_BASE"));
        }
        piston = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)) {
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16)) {
                    if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
                        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_19)) {
                            helper.add(Material.SCULK_SENSOR);
                            helper.add(Material.SCULK_SHRIEKER);
                            helper.add(Material.MUD);
                        }
                        helper.add(Material.DIRT_PATH);
                        helper.add(Material.LIGHTNING_ROD);
                        helper.add(Material.POINTED_DRIPSTONE);
                        helper.add(Material.SMALL_AMETHYST_BUD);
                        helper.add(Material.MEDIUM_AMETHYST_BUD);
                        helper.add(Material.LARGE_AMETHYST_BUD);
                        helper.add(Material.AMETHYST_CLUSTER);
                        helper.add(Material.POWDER_SNOW_CAULDRON);
                        helper.add(Material.LAVA_CAULDRON);
                        helper.add(Material.WATER_CAULDRON);
                        helper.add(Material.CAVE_VINES);
                        helper.add(Material.CAVE_VINES_PLANT);
                    }
                    helper.add(Material.SOUL_CAMPFIRE);
                    helper.add(Material.TWISTING_VINES);
                    helper.add(Material.TWISTING_VINES_PLANT);
                    helper.add(Material.WEEPING_VINES);
                    helper.add(Material.WEEPING_VINES_PLANT);
                    helper.add(Material.CHAIN);
                    helper.add(Material.SOUL_LANTERN);
                }
                helper.add(Material.BELL);
                helper.add(Material.LANTERN);
                helper.add(Material.CAMPFIRE);
                helper.add(Material.COMPOSTER);
                helper.add(Material.LECTERN);
                helper.add(Material.GRINDSTONE);
                helper.add(Material.STONECUTTER);
                helper.add(Material.BAMBOO);
            }
            helper.add(Material.PISTON_HEAD);
            helper.add(Material.COBBLESTONE_WALL);
            helper.add(Material.MOSSY_COBBLESTONE_WALL);
            helper.add(Material.IRON_BARS);
            helper.add(Material.PISTON);
            helper.add(Material.STICKY_PISTON);
            helper.add(Material.FARMLAND);
            helper.add(Material.TURTLE_EGG);
            helper.add(Material.CONDUIT);
        } else {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
                helper.add(Material.CHORUS_PLANT);
                helper.add(Material.END_CRYSTAL);
                helper.add(Material.END_ROD);
                helper.add(Material.getMaterial("GRASS_PATH"));
                helper.add(beetroot_block);
            }
            helper.add(Material.getMaterial("PISTON_EXTENSION"));
            helper.add(Material.getMaterial("COBBLE_WALL"));
            helper.add(Material.getMaterial("IRON_FENCE"));
            helper.add(Material.getMaterial("SOIL"));
            helper.add(Material.getMaterial("PISTON_BASE"));
            helper.add(Material.getMaterial("PISTON_STICKY_BASE"));
        }
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.endsWith(s, "_HANGING_SIGN")) continue;
            helper.add(m);
        }
        helper.add(Spade.get("web"));
        helper.add(Material.COCOA);
        helper.add(Material.DRAGON_EGG);
        helper.add(Material.HOPPER);
        helper.add(Material.FLOWER_POT);
        helper.add(Material.BREWING_STAND);
        helper.add(Material.CAULDRON);
        helper.add(Material.LADDER);
        helper.add(Material.VINE);
        helper.add(Material.SNOW);
        helper.add(Material.SNOW_BLOCK);
        helper.add(Material.CACTUS);
        helper.add(Material.SOUL_SAND);
        helper.add(Spade.get("daylight_detector_1"));
        helper.add(Spade.get("daylight_detector_2"));
        helper.add(Spade.get("repeater_on"));
        helper.add(Spade.get("repeater_off"));
        helper.add(Spade.get("comparator_on"));
        helper.add(Spade.get("comparator_off"));
        helper.add(Spade.get("enchanting_table"));
        helper.add(Spade.get("lily_pad"));
        helper.add(Spade.get("end_portal_frame"));
        helper.add(Spade.get("cake"));
        for (Material m : materials) {
            if (!BlockUtils.areFenceGates(m) && !BlockUtils.areFences(m) && !BlockUtils.areStairs(m) && !BlockUtils.areSlabs(m) && !BlockUtils.areTrapdoors(m) && !BlockUtils.areDoors(m) && !BlockUtils.areStainedGlasses(m) && !BlockUtils.areChests(m) && !BlockUtils.arePlates(m) && !BlockUtils.areShulkerBoxes(m) && !BlockUtils.areLeaves(m) && !BlockUtils.areHeads(m) && !BlockUtils.areCoralFans(m) && !BlockUtils.areAnvils(m) && !BlockUtils.areCarpets(m) && !BlockUtils.areBeds(m) && !BlockUtils.areFlowerPots(m) && !BlockUtils.areCobbleWalls(m) && !BlockUtils.areCandles(m) && !BlockUtils.areCandleCakes(m) && !BlockUtils.isInteractiveAndPassable(null, m) && !helper.contains(m)) continue;
            builder.add(m);
        }
        semi_solid = new HashSet<Material>(builder);
        builder.clear();
        for (Material m : materials) {
            if (!editable.contains(m) && !BlockUtils.areFenceGates(m) && !BlockUtils.areTrapdoors(m) && !BlockUtils.areChests(m) && !BlockUtils.areDoors(m) && !BlockUtils.areShulkerBoxes(m) && !BlockUtils.areBeds(m) && !BlockUtils.areAnvils(m)) continue;
            builder.add(m);
        }
        changeable = new HashSet<Material>(builder);
        builder.clear();
        helper.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)) {
            helper.add(Material.SCAFFOLDING);
            helper.add(Material.WITHER_ROSE);
            helper.add(Material.SWEET_BERRY_BUSH);
            helper.add(Material.CORNFLOWER);
            helper.add(Material.LILY_OF_THE_VALLEY);
        } else {
            helper.add(Material.getMaterial("SIGN"));
        }
        for (Material m : materials) {
            s = m.toString();
            if (!BlockUtils.endsWith(s, "GRASS")) continue;
            helper.add(m);
        }
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16)) {
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
                    if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_19)) {
                        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_20)) {
                            helper.add(Material.PINK_PETALS);
                        }
                        helper.add(Material.SCULK_VEIN);
                    }
                    helper.add(Material.GLOW_LICHEN);
                    helper.add(Material.SPORE_BLOSSOM);
                    helper.add(Material.HANGING_ROOTS);
                    helper.add(Material.CAVE_VINES);
                    helper.add(Material.CAVE_VINES_PLANT);
                    helper.add(Material.POWDER_SNOW);
                    helper.add(Material.LIGHT);
                }
                helper.add(Material.SOUL_TORCH);
                helper.add(Material.SOUL_WALL_TORCH);
                helper.add(Material.SOUL_FIRE);
                helper.add(Material.TWISTING_VINES);
                helper.add(Material.TWISTING_VINES_PLANT);
                helper.add(Material.WEEPING_VINES);
                helper.add(Material.WEEPING_VINES_PLANT);
                helper.add(Material.WARPED_ROOTS);
                helper.add(Material.CRIMSON_ROOTS);
                helper.add(Material.WARPED_FUNGUS);
                helper.add(Material.CRIMSON_FUNGUS);
                helper.add(Material.NETHER_SPROUTS);
            }
            helper.add(Material.GRASS_BLOCK);
            helper.add(Material.REDSTONE_TORCH);
            helper.add(Material.REDSTONE_WALL_TORCH);
            helper.add(Material.WALL_TORCH);
            helper.add(Material.RAIL);
            helper.add(Material.END_PORTAL);
            helper.add(Material.NETHER_PORTAL);
            helper.add(Material.MOVING_PISTON);
            helper.add(Material.DANDELION);
            helper.add(Material.POPPY);
            helper.add(Material.TALL_GRASS);
            helper.add(Material.POTATOES);
            helper.add(Material.CARROTS);
            helper.add(Material.NETHER_WART);
            helper.add(Material.ACTIVATOR_RAIL);
            helper.add(Material.DETECTOR_RAIL);
            helper.add(Material.POWERED_RAIL);
            helper.add(Material.BEETROOT_SEEDS);
            helper.add(Material.MELON_SEEDS);
            helper.add(Material.PUMPKIN_SEEDS);
            helper.add(Material.WHEAT);
            helper.add(Material.WHEAT_SEEDS);
            helper.add(Material.BUBBLE_COLUMN);
            helper.add(Material.CAVE_AIR);
            helper.add(Material.VOID_AIR);
            helper.add(Material.KELP);
            helper.add(Material.KELP_PLANT);
            helper.add(Material.BEETROOTS);
            helper.add(Material.FERN);
            helper.add(Material.LARGE_FERN);
            helper.add(Material.SUNFLOWER);
            helper.add(Material.AZURE_BLUET);
            helper.add(Material.ATTACHED_MELON_STEM);
            helper.add(Material.ATTACHED_PUMPKIN_STEM);
            helper.add(Material.ROSE_BUSH);
            helper.add(Material.ALLIUM);
            helper.add(Material.OXEYE_DAISY);
            helper.add(Material.BLUE_ORCHID);
            helper.add(Material.LILAC);
            helper.add(Material.PEONY);
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.endsWith(s, "_BUTTON") && (BlockUtils.startsWith(s, "POTTED_") || !BlockUtils.endsWith(s, "_SAPLING") && !BlockUtils.endsWith(s, "_TULIP"))) continue;
                helper.add(m);
            }
        } else {
            helper.add(Material.getMaterial("REDSTONE_TORCH_ON"));
            helper.add(Material.getMaterial("REDSTONE_TORCH_OFF"));
            helper.add(Material.getMaterial("SEEDS"));
            helper.add(Material.getMaterial("RAILS"));
            helper.add(Material.getMaterial("ENDER_PORTAL"));
            helper.add(Material.getMaterial("PORTAL"));
            helper.add(Material.getMaterial("PISTON_MOVING_PIECE"));
            helper.add(Material.getMaterial("CROPS"));
            helper.add(Material.getMaterial("SIGN_POST"));
            helper.add(Material.getMaterial("RED_ROSE"));
            helper.add(Material.getMaterial("FLOWER"));
            helper.add(Material.getMaterial("YELLOW_FLOWER"));
            helper.add(Material.getMaterial("SUGAR_CANE_BLOCK"));
            helper.add(Material.getMaterial("SAPLING"));
            helper.add(Material.getMaterial("LONG_GRASS"));
            helper.add(Material.getMaterial("NETHER_WARTS"));
            helper.add(Material.getMaterial("DOUBLE_PLANT"));
            helper.add(Material.getMaterial("NETHER_WARTS"));
            helper.add(Material.getMaterial("WOOD_BUTTON"));
            helper.add(Material.getMaterial("GRASS"));
            helper.add(Material.STONE_BUTTON);
        }
        for (Material m : materials) {
            s = m.toString();
            if ((!BlockUtils.endsWith(s, "_SIGN") || BlockUtils.contains(s, "_HANGING_")) && !BlockUtils.endsWith(s, "_CROP")) continue;
            helper.add(m);
        }
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_10)) {
                    helper.add(Material.STRUCTURE_VOID);
                }
                helper.add(Material.BEETROOT_SEEDS);
                helper.add(Material.END_GATEWAY);
            }
            helper.add(Material.ARMOR_STAND);
        }
        helper.add(Material.SUGAR_CANE);
        helper.add(Material.AIR);
        helper.add(Material.BROWN_MUSHROOM);
        helper.add(Material.RED_MUSHROOM);
        helper.add(Material.TORCH);
        helper.add(Material.TRIPWIRE);
        helper.add(Material.TRIPWIRE_HOOK);
        helper.add(Material.REDSTONE_WIRE);
        helper.add(Material.ACTIVATOR_RAIL);
        helper.add(Material.DETECTOR_RAIL);
        helper.add(Material.POWERED_RAIL);
        helper.add(Material.MELON_SEEDS);
        helper.add(Material.PUMPKIN_SEEDS);
        helper.add(Material.PUMPKIN_STEM);
        helper.add(Material.MELON_STEM);
        helper.add(Material.CARROT);
        helper.add(Material.FIRE);
        helper.add(Material.POTATO);
        helper.add(Material.LEVER);
        helper.add(Material.DEAD_BUSH);
        helper.add(Material.VINE);
        for (Material m : materials) {
            if (BlockUtils.areFenceGates(m) || BlockUtils.areTrapdoors(m) || BlockUtils.arePlates(m) || BlockUtils.isLiquid(m) || BlockUtils.areDripLeafs(m) || BlockUtils.areBanners(m) || helper.contains(m)) continue;
            builder.add(m);
        }
        solid = new HashSet<Material>(builder);
        builder.clear();
        helper.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
            helper.add(Material.END_CRYSTAL);
            helper.add(Material.CHORUS_PLANT);
        }
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)) {
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_15)) {
                    if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16)) {
                        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
                            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_19)) {
                                helper.add(Material.MUD);
                            }
                            helper.add(Material.GLOW_LICHEN);
                            helper.add(Material.GLOW_ITEM_FRAME);
                            helper.add(Material.AZALEA);
                            helper.add(Material.FLOWERING_AZALEA);
                            helper.add(Material.TINTED_GLASS);
                            helper.add(Material.CALCITE);
                        }
                        helper.add(Material.WARPED_WART_BLOCK);
                        helper.add(Material.NETHER_WART_BLOCK);
                        helper.add(Material.WARPED_NYLIUM);
                        helper.add(Material.CRIMSON_NYLIUM);
                    }
                    helper.add(Material.HONEY_BLOCK);
                }
                helper.add(Material.BAMBOO);
            }
            helper.add(Material.TURTLE_EGG);
            helper.add(Material.BROWN_MUSHROOM);
            helper.add(Material.RED_MUSHROOM);
            helper.add(Material.BROWN_MUSHROOM_BLOCK);
            helper.add(Material.RED_MUSHROOM_BLOCK);
            helper.add(Material.MUSHROOM_STEM);
            helper.add(Material.CUT_SANDSTONE);
            helper.add(Material.CUT_RED_SANDSTONE);
            helper.add(Material.PODZOL);
            for (Material m : materials) {
                s = m.toString();
                if (!BlockUtils.startsWith(s, "INFESTED_")) continue;
                helper.add(m);
            }
        } else {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)) {
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
                    if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_10)) {
                        helper.add(magma);
                    }
                    helper.add(Material.END_ROD);
                    helper.add(beetroot_block);
                }
                helper.add(Material.SLIME_BLOCK);
                helper.add(Material.RED_SANDSTONE);
                helper.add(Material.RED_SANDSTONE_STAIRS);
            }
            helper.add(Material.getMaterial("HUGE_MUSHROOM_1"));
            helper.add(Material.getMaterial("HUGE_MUSHROOM_2"));
        }
        helper.add(Material.ITEM_FRAME);
        helper.add(Material.LADDER);
        helper.add(Material.TNT);
        helper.add(Material.COCOA);
        helper.add(Material.NETHERRACK);
        helper.add(Material.GLASS);
        helper.add(Material.SANDSTONE);
        helper.add(Material.SANDSTONE_STAIRS);
        helper.add(Material.QUARTZ_BLOCK);
        helper.add(Material.QUARTZ_STAIRS);
        helper.add(Material.SNOW);
        helper.add(Material.SNOW_BLOCK);
        helper.add(Material.SOUL_SAND);
        helper.add(Material.PUMPKIN);
        helper.add(Spade.get("lily_pad"));
        helper.add(Spade.get("daylight_detector_1"));
        helper.add(Spade.get("daylight_detector_2"));
        helper.add(Spade.get("repeater_on"));
        helper.add(Spade.get("repeater_off"));
        helper.add(Spade.get("comparator_on"));
        helper.add(Spade.get("comparator_off"));
        helper.add(Spade.get("enchanting_table"));
        for (Material m : materials) {
            if (BlockUtils.isSolid(m) && !BlockUtils.areGlasses(m) && !BlockUtils.areStainedGlasses(m) && !BlockUtils.areIceBlocks(m) && !BlockUtils.areCoralFans(m) && !BlockUtils.areLeaves(m) && !BlockUtils.areHeads(m) && !BlockUtils.areBeds(m) && !BlockUtils.areCarpets(m) && !BlockUtils.areFlowerPots(m) && !BlockUtils.isLiquid(m) && !BlockUtils.isScaffoldingBlock(m) && !BlockUtils.isPowderSnow(m) && !BlockUtils.areDripLeafs(m) && !BlockUtils.areCandles(m) && !BlockUtils.areCandleCakes(m) && !helper.contains(m)) continue;
            builder.add(m);
        }
        sensitive = new HashSet<Material>(builder);
        builder.clear();
        helper.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_15)) {
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16)) {
                    if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
                        helper.add(Material.LIGHTNING_ROD);
                    }
                    helper.add(Material.CHAIN);
                }
                helper.add(Material.HONEY_BLOCK);
            }
            helper.add(Material.CHORUS_PLANT);
            helper.add(Material.END_ROD);
        }
        helper.add(Material.CACTUS);
        helper.add(Material.COCOA);
        helper.add(Material.DRAGON_EGG);
        helper.add(iron_bars);
        helper.add(cake);
        helper.add(head);
        for (Material m : materials) {
            if (!BlockUtils.areStainedGlasses(m) && !BlockUtils.areDoors(m) && !BlockUtils.areTrapdoors(m) && !BlockUtils.areChests(m) && !BlockUtils.areFences(m) && !BlockUtils.areFenceGates(m) && !BlockUtils.areCobbleWalls(m) && !BlockUtils.areFlowerPots(m) && !BlockUtils.areAnvils(m) && !helper.contains(m)) continue;
            builder.add(m);
        }
        walls = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14)) {
            builder.add(Material.SWEET_BERRY_BUSH);
        }
        (int)eractive_bushes = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_15)) {
            builder.add(Material.HONEY_BLOCK);
        }
        honey_block = new HashSet<Material>(builder);
        builder.clear();
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8)) {
            builder.add(Material.SLIME_BLOCK);
        }
        slime_block = new HashSet<Material>(builder);
        builder.clear();
        helper.clear();
    }
}

