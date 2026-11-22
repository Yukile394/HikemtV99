/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package ai.idealistic.spartan.functionality.moderation;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.Config;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;

public class AwarenessNotifications {
    private static final Map<UUID, Map<String, Long>> map = new LinkedHashMap<UUID, Map<String, Long>>();
    private static final String disableMessage = " You can disable Awareness Notifications via " + Config.settings.getFile().getName() + ".";
    public static final UUID uuid = UUID.randomUUID();

    public static void clear() {
        map.clear();
    }

    public static void refresh() {
        map.remove(uuid);
    }

    private static boolean areEnabled() {
        return Config.settings.getBoolean("Notifications.awareness_notifications");
    }

    public static boolean canSend(UUID uuid, String key, int secondsCooldown) {
        boolean send;
        Map<String, Long> childMap = map.get(uuid);
        if (childMap != null) {
            Long time = childMap.get(key);
            if (time == null) {
                childMap.put(key, System.currentTimeMillis() + (long)secondsCooldown * 1000L);
                send = true;
            } else if (time == 0L) {
                send = false;
            } else {
                long currentTime = System.currentTimeMillis();
                if (currentTime >= time) {
                    childMap.put(key, currentTime + (long)secondsCooldown * 1000L);
                    send = true;
                } else {
                    send = false;
                }
            }
        } else {
            childMap = new LinkedHashMap<String, Long>();
            childMap.put(key, System.currentTimeMillis() + (long)secondsCooldown * 1000L);
            map.put(uuid, childMap);
            send = true;
        }
        return send;
    }

    private static String getNotification(String s, boolean disableMessage) {
        return Config.messages.getColorfulString("awareness_notification").replace(":", "\u00a78:\u00a77").replace("{info}", s) + (disableMessage ? AwarenessNotifications.disableMessage : "");
    }

    public static String getNotification(String s) {
        return AwarenessNotifications.getNotification(s, false);
    }

    public static String getOptionalNotification(String s) {
        return !AwarenessNotifications.areEnabled() || s == null ? null : AwarenessNotifications.getNotification(s, true);
    }

    public static void forcefullySend(PlayerProtocol protocol, String message, boolean console) {
        message = AwarenessNotifications.getNotification(message);
        if (protocol != null) {
            protocol.bukkit().sendMessage(message);
            if (console) {
                Bukkit.getConsoleSender().sendMessage("(" + protocol.bukkit().getName() + ") " + message);
            }
        } else if (console) {
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    public static void forcefullySend(String message) {
        Bukkit.getConsoleSender().sendMessage(AwarenessNotifications.getNotification(message));
    }

    public static void optionallySend(String message) {
        if ((message = AwarenessNotifications.getOptionalNotification(message)) != null) {
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }
}

