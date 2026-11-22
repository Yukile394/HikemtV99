/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.World$Environment
 *  org.bukkit.block.Block
 *  org.bukkit.event.Event
 */
package ai.idealistic.spartan.abstraction.profiling;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.profiling.PlayerProfile;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.PlayerFoundOreEvent;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.tracking.AntiCheatLogs;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

public class MiningHistory {
    public static final String environmentIdentifier = "Environment: ";
    public static final String amountIdentifier = "Amount: ";
    public static final String oreIdentifier = "Ore: ";
    private final PlayerProfile profile;
    public final MiningOre ore;
    private final int[] count;

    public static void log(PlayerProtocol protocol, Block block, boolean cancelled) {
        MiningOre ore;
        if (protocol.getGameMode() == GameMode.SURVIVAL && PlayerUtils.isPickaxeItem(protocol.getItemInHand().getType()) && (ore = MiningHistory.getMiningOre(block.getType())) != null) {
            PlayerFoundOreEvent event;
            World.Environment environment = block.getWorld().getEnvironment();
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            int amount = 1;
            String key = ore.toString();
            String log = "(Player:" + protocol.bukkit().getName() + "), (" + amountIdentifier + amount + "), (" + oreIdentifier + key + "), (" + environmentIdentifier + BlockUtils.environmentToString(environment) + "), (W-XYZ: " + block.getWorld().getName() + " " + x + " " + y + " " + z + ")";
            if (Config.settings.getBoolean("Important.enable_developer_api")) {
                event = new PlayerFoundOreEvent(protocol.bukkit(), log, block.getLocation(), block.getType());
                Bukkit.getPluginManager().callEvent((Event)event);
            } else {
                event = null;
            }
            if (event == null || !event.isCancelled()) {
                AntiCheatLogs.logInfo(protocol, null, log, false, block.getType(), null, System.currentTimeMillis());
                MiningHistory miningHistory = protocol.profile().getMiningHistory(ore);
                if (miningHistory != null) {
                    String pluralKey = key.endsWith("s") ? key + "es" : key + "s";
                    miningHistory.increaseMines(environment, amount);
                    protocol.getRunner(CheckEnums.HackType.X_RAY).handle(cancelled, new Object[]{environment, miningHistory, ore, pluralKey});
                }
            }
        }
    }

    public static MiningOre getMiningOre(Material material) {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16)) {
            if (material == Material.ANCIENT_DEBRIS) {
                return MiningOre.ANCIENT_DEBRIS;
            }
            if (material == Material.GILDED_BLACKSTONE || material == Material.NETHER_GOLD_ORE) {
                return MiningOre.GOLD;
            }
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17)) {
                if (material == Material.DEEPSLATE_DIAMOND_ORE) {
                    return MiningOre.DIAMOND;
                }
                if (material == Material.DEEPSLATE_EMERALD_ORE) {
                    return MiningOre.EMERALD;
                }
                if (material == Material.DEEPSLATE_GOLD_ORE) {
                    return MiningOre.GOLD;
                }
            }
        }
        switch (material) {
            case DIAMOND_ORE: {
                return MiningOre.DIAMOND;
            }
            case EMERALD_ORE: {
                return MiningOre.EMERALD;
            }
            case GOLD_ORE: {
                return MiningOre.GOLD;
            }
        }
        return null;
    }

    MiningHistory(PlayerProfile profile, MiningOre ore) {
        World.Environment[] environments = World.Environment.values();
        this.count = new int[environments.length];
        for (int i = 0; i < environments.length; ++i) {
            this.count[i] = 0;
        }
    }

    public double getMinesToTimeRatio(World.Environment environment) {
        int mines = this.count[environment.ordinal()];
        if (mines > 0) {
            long onlineTime = this.profile.getContinuity().getOnlineTime();
            if (onlineTime > 0L) {
                return (double)mines / (double)(onlineTime /= 1000L);
            }
            return 0.0;
        }
        return 0.0;
    }

    public void increaseMines(World.Environment environment, int amount) {
        int n = environment.ordinal();
        this.count[n] = this.count[n] + amount;
    }

    public static enum MiningOre {
        ANCIENT_DEBRIS,
        DIAMOND,
        EMERALD,
        GOLD;

        private final String string = this.name().toLowerCase().replace("_", "-");

        public String toString() {
            return this.string;
        }
    }
}

