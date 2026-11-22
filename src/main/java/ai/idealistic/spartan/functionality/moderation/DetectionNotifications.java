/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.functionality.moderation;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DetectionNotifications {
    public static final int defaultFrequency = Integer.MIN_VALUE;
    private static final Map<UUID, Integer> notifications = new ConcurrentHashMap<UUID, Integer>();
    private static final Map<UUID, Boolean> verbose = new ConcurrentHashMap<UUID, Boolean>();

    public static List<PlayerProtocol> getPlayers() {
        if (!notifications.isEmpty()) {
            ArrayList<PlayerProtocol> list = new ArrayList<PlayerProtocol>(notifications.size());
            for (UUID uuid : notifications.keySet()) {
                PlayerProtocol protocol = PluginBase.getProtocol(uuid);
                if (protocol == null) continue;
                list.add(protocol);
            }
            return list;
        }
        return new ArrayList<PlayerProtocol>0;
    }

    public static boolean isEnabled(PlayerProtocol p) {
        return notifications.containsKey(p.getUUID());
    }

    public static boolean isVerboseEnabled(PlayerProtocol p) {
        return verbose.containsKey(p.getUUID());
    }

    public static boolean hasPermission(PlayerProtocol p) {
        return Permissions.has(p.bukkit(), Permission.NOTIFICATIONS);
    }

    public static Integer getFrequency(PlayerProtocol p) {
        return notifications.get(p.getUUID());
    }

    public static void remove(PlayerProtocol p) {
        notifications.remove(p.getUUID());
    }

    public static void removeVerbose(PlayerProtocol p) {
        UUID uuid = p.getUUID();
        if (verbose.containsKey(uuid)) {
            verbose.remove(uuid);
            p.bukkit().sendMessage(Config.messages.getColorfulString("verbose_disable"));
        }
    }

    public static void set(PlayerProtocol p, int i) {
        Integer frequency = notifications.put(p.getUUID(), i);
        if (frequency == null) {
            p.bukkit().sendMessage(Config.messages.getColorfulString("notifications_enable"));
        } else if (frequency != i) {
            p.bukkit().sendMessage(Config.messages.getColorfulString("notifications_modified"));
        } else {
            notifications.remove(p.getUUID());
            p.bukkit().sendMessage(Config.messages.getColorfulString("notifications_disable"));
        }
    }

    public static void addVerbose(PlayerProtocol p) {
        UUID uuid = p.getUUID();
        if (!verbose.containsKey(uuid)) {
            verbose.put(uuid, true);
            p.bukkit().sendMessage(Config.messages.getColorfulString("verbose_enable"));
        }
    }

    public static void runOnLeave(PlayerProtocol p) {
        if (notifications.containsKey(p.getUUID()) && !DetectionNotifications.hasPermission(p)) {
            notifications.remove(p.getUUID());
        }
    }
}

