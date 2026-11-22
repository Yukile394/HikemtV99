/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.injector.temporary.TemporaryPlayer
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.compatibility.necessary.protocollib;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.BackgroundProtocolLib;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import com.comphenix.protocol.injector.temporary.TemporaryPlayer;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ProtocolLib {
    private static boolean temporaryClass = false;
    private static boolean bukkit = false;

    private static void checkClass() {
        temporaryClass = ReflectionUtils.classExists("com.comphenix.protocol.injector.temporary.TemporaryPlayer");
    }

    public static void run() {
        ProtocolLib.checkClass();
        if (!bukkit) {
            BackgroundProtocolLib.run();
        }
    }

    public static void otherwise() {
        List<PlayerProtocol> players;
        ProtocolLib.checkClass();
        bukkit = true;
        String message = AwarenessNotifications.getOptionalNotification("Anti-Cheat checks work significantly better with ProtocolLib installed as it allows the use of packets which helps identify information earlier and more accurately.");
        if (message != null && !(players = Permissions.getStaff()).isEmpty()) {
            for (PlayerProtocol p : players) {
                if (!AwarenessNotifications.canSend(p.getUUID(), "protocol-lib", 0)) continue;
                p.bukkit().sendMessage(message);
            }
        }
    }

    public static boolean isTemporary(OfflinePlayer player) {
        return temporaryClass && player instanceof TemporaryPlayer;
    }

    public static UUID getUUID(Entity entity) {
        if (entity instanceof Player) {
            if (ProtocolLib.isTemporary((OfflinePlayer)((Player)entity))) {
                return UUID.randomUUID();
            }
            return entity.getUniqueId();
        }
        return entity.getUniqueId();
    }

    public static int getEntityID(Entity entity) {
        if (entity instanceof Player) {
            if (ProtocolLib.isTemporary((OfflinePlayer)((Player)entity))) {
                return new Random().nextInt();
            }
            return entity.getEntityId();
        }
        return entity.getEntityId();
    }

    public static Location getLocationOrNull(Entity entity) {
        if (entity instanceof Player) {
            return ProtocolLib.getLocationOrNull((Player)entity);
        }
        return entity == null ? null : entity.getLocation();
    }

    public static Location getLocationOrNull(Player player) {
        if (ProtocolLib.isTemporary((OfflinePlayer)player)) {
            return null;
        }
        return player == null ? null : player.getLocation();
    }

    public static Entity getVehicle(Entity entity) {
        if (entity instanceof Player) {
            if (ProtocolLib.isTemporary((OfflinePlayer)((Player)entity))) {
                return null;
            }
            return entity.getVehicle();
        }
        return entity.getVehicle();
    }

    public static double getEyeHeight(Entity entity) {
        if (entity instanceof Player) {
            if (ProtocolLib.isTemporary((OfflinePlayer)((Player)entity))) {
                return 0.0;
            }
            return ((Player)entity).getEyeHeight();
        }
        if (entity instanceof LivingEntity) {
            return ((LivingEntity)entity).getEyeHeight();
        }
        return 0.0;
    }

    public static World getWorld(Entity entity) {
        if (entity instanceof Player) {
            if (ProtocolLib.isTemporary((OfflinePlayer)((Player)entity))) {
                return (World)Bukkit.getWorlds().get(0);
            }
            return entity.getWorld();
        }
        return entity.getWorld();
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            return !ProtocolLib.isTemporary((OfflinePlayer)player) && player.hasPermission(permission);
        }
        return sender.hasPermission(permission);
    }
}

