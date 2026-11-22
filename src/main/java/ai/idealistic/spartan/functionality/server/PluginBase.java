/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.functionality.server;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.inventory.implementation.CommandsMenu;
import ai.idealistic.spartan.abstraction.inventory.implementation.Datatype;
import ai.idealistic.spartan.abstraction.inventory.implementation.DetectionsMenu;
import ai.idealistic.spartan.abstraction.inventory.implementation.EditionMenu;
import ai.idealistic.spartan.abstraction.inventory.implementation.GeneralMenu;
import ai.idealistic.spartan.abstraction.inventory.implementation.PlayerInfo;
import ai.idealistic.spartan.abstraction.inventory.implementation.PluginChargeMenu;
import ai.idealistic.spartan.abstraction.inventory.implementation.PreventionsMenu;
import ai.idealistic.spartan.abstraction.inventory.implementation.PunishmentsMenu;
import ai.idealistic.spartan.abstraction.inventory.implementation.Title;
import ai.idealistic.spartan.abstraction.inventory.implementation.SubCommandsMenu;
import ai.idealistic.spartan.abstraction.inventory.implementation.SynMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.concurrent.GeneralThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.ServerSchedulers;
import ai.idealistic.spartan.listeners.PlayerInteractAtEntityListener;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

public class PluginBase {
    public static final GeneralThread.ThreadPool connectionThread = new GeneralThread.ThreadPool(50L);
    public static final GeneralThread.ThreadPool dataThread = new GeneralThread.ThreadPool(1L);
    public static final PlayerInfo playerInfo = new PlayerInfo();
    public static final PluginChargeMenu chargeMenu = new PluginChargeMenu();
    public static final Datatype detailsMenu = new Datatype();
    public static final EditionMenu editionMenu = new EditionMenu();
    public static final PreventionsMenu preventionsMenu = new PreventionsMenu();
    public static final PunishmentsMenu punishmentsMenu = new PunishmentsMenu();
    public static final SynMenu synMenu = new SynMenu();
    public static final DetectionsMenu detectionsMenu = new DetectionsMenu();
    public static final GeneralMenu generalMenu = new GeneralMenu();
    public static final Title statisticsMenu = new Title();
    public static final CommandsMenu commandsMenu = new CommandsMenu();
    public static final SubCommandsMenu subCommandsMenu = new SubCommandsMenu();
    public static final InventoryMenu[] menus = new InventoryMenu[]{playerInfo, chargeMenu, detailsMenu, editionMenu, preventionsMenu, punishmentsMenu, synMenu, detectionsMenu, generalMenu, statisticsMenu, commandsMenu, subCommandsMenu};
    public static final int hashCodeMultiplier = 31;
    public static final int maxBytes = NumberConversions.round((double)((double)Runtime.getRuntime().maxMemory() * 0.05));
    public static final int maxSQLRows = maxBytes / 1024;
    private static final Map<UUID, PlayerProtocol> playerProtocol = new ConcurrentHashMap<UUID, PlayerProtocol>();
    public static final Class<?> craftPlayer = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17) ? null : ReflectionUtils.getClass(ReflectionUtils.class.getPackage().getName().substring(0, 19) + "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().substring(23) + ".entity.CraftPlayer");

    public static int getPlayerCount() {
        return playerProtocol.size();
    }

    public static boolean hasPlayerCount() {
        return !playerProtocol.isEmpty();
    }

    public static Collection<PlayerProtocol> getProtocols() {
        return playerProtocol.values();
    }

    public static Set<Map.Entry<UUID, PlayerProtocol>> getPlayerEntries() {
        return playerProtocol.entrySet();
    }

    public static boolean isSynchronised() {
        return Bukkit.isPrimaryThread() || !Register.isPluginEnabled();
    }

    public static Object getCraftPlayerMethod(Player p, String path) {
        if (craftPlayer != null) {
            try {
                Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke((Object)p, new Object[0]);
                return handle.getClass().getDeclaredField(path).get(handle);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return -1;
    }

    public static boolean packetsEnabled() {
        return Compatibility.CompatibilityType.PROTOCOL_LIB.isFunctional();
    }

    public static PlayerProtocol getProtocol(Player player, boolean updateLastInteraction) {
        if (ProtocolLib.isTemporary((OfflinePlayer)player)) {
            return new PlayerProtocol(player);
        }
        PlayerProtocol protocol = playerProtocol.get(player.getUniqueId());
        if (protocol == null) {
            protocol = new PlayerProtocol(player);
            playerProtocol.put(player.getUniqueId(), protocol);
        } else {
            protocol.updateBukkit(player);
        }
        if (updateLastInteraction) {
            protocol.setLastInteraction();
        }
        return protocol;
    }

    public static PlayerProtocol getProtocol(Player player) {
        return PluginBase.getProtocol(player, false);
    }

    public static PlayerProtocol getProtocol(String name) {
        if (!playerProtocol.isEmpty()) {
            for (PlayerProtocol protocol : playerProtocol.values()) {
                if (!protocol.bukkit().getName().equals(name)) continue;
                return protocol;
            }
        }
        return null;
    }

    public static PlayerProtocol getAnyCaseProtocol(String name) {
        if (!playerProtocol.isEmpty()) {
            for (PlayerProtocol protocol : playerProtocol.values()) {
                if (!protocol.bukkit().getName().equalsIgnoreCase(name)) continue;
                return protocol;
            }
        }
        return null;
    }

    public static PlayerProtocol getProtocol(int entityID) {
        for (PlayerProtocol protocol : playerProtocol.values()) {
            if (protocol.getEntityId() != entityID) continue;
            return protocol;
        }
        return null;
    }

    public static PlayerProtocol getProtocol(UUID uuid) {
        if (PluginBase.isSynchronised()) {
            Player player = Bukkit.getPlayer((UUID)uuid);
            if (player != null) {
                return PluginBase.getProtocol(player);
            }
            return playerProtocol.get(uuid);
        }
        return playerProtocol.get(uuid);
    }

    public static PlayerProtocol deleteProtocol(Player player) {
        if (ProtocolLib.isTemporary((OfflinePlayer)player)) {
            return null;
        }
        PlayerProtocol protocol = playerProtocol.remove(player.getUniqueId());
        if (protocol != null && !protocol.isAFK()) {
            protocol.profile().getContinuity().setActiveTime(System.currentTimeMillis(), protocol.getActiveTimePlayed(), true);
        }
        return protocol;
    }

    public static Object runDelayedTask(PlayerProtocol protocol, Runnable runnable, long start) {
        return ServerSchedulers.schedule(protocol, runnable, start, -1L);
    }

    public static Object runRepeatingTask(PlayerProtocol protocol, Runnable runnable, long start, long repetition) {
        return ServerSchedulers.schedule(protocol, runnable, start, repetition);
    }

    public static Object runDelayedTask(Runnable runnable, long start) {
        return ServerSchedulers.schedule(null, runnable, start, -1L);
    }

    public static Object runRepeatingTask(Runnable runnable, long start, long repetition) {
        return ServerSchedulers.schedule(null, runnable, start, repetition);
    }

    public static void runTask(PlayerProtocol protocol, Runnable runnable) {
        ServerSchedulers.run(protocol, runnable, false);
    }

    public static void runTask(World world, int x, int z, Runnable runnable) {
        ServerSchedulers.run(world, x, z, runnable, false);
    }

    public static void transferTask(PlayerProtocol protocol, Runnable runnable) {
        ServerSchedulers.run(protocol, runnable, true);
    }

    public static void transferTask(World world, int x, int z, Runnable runnable) {
        ServerSchedulers.run(world, x, z, runnable, true);
    }

    public static void cancelTask(Object task) {
        ServerSchedulers.cancel(task);
    }

    public static void runCommand(String command) {
        ServerSchedulers.transfer(() -> Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)command));
    }

    public static void disable() {
        if (!playerProtocol.isEmpty()) {
            for (PlayerProtocol protocol : playerProtocol.values()) {
                if (protocol == null || protocol.isAFK()) continue;
                protocol.profile().getContinuity().setActiveTime(System.currentTimeMillis(), protocol.getActiveTimePlayed(), true);
            }
        }
        playerProtocol.clear();
        GeneralThread.disable();
        PlayerInteractAtEntityListener.clear();
        Config.create();
    }
}

