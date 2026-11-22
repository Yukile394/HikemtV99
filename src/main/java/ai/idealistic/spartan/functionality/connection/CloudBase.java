/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.functionality.connection;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.connection.CloudConnections;
import ai.idealistic.spartan.functionality.connection.User;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.util.List;

public class CloudBase {
    private static long connectionRefreshCooldown = 0L;
    private static long connectionFailedCooldown = 0L;
    private static final long refreshTime = 60000L;
    static final String separator = ">@#&!%<;=";

    public static void announce(PlayerProtocol protocol) {
        if (Permissions.isStaff(protocol.bukkit())) {
            PluginBase.connectionThread.execute(() -> {
                String[][] announcements = CloudConnections.getStaffAnnouncements();
            });
        }
    }

    public static void throwError(Exception object, String function) {
        long ms = System.currentTimeMillis();
        if (connectionFailedCooldown >= ms) {
            connectionFailedCooldown = ms + 60000L;
            String message = "(" + function + ") Failed to connect to the Game Cloud.\nError: " + object.getMessage() + "\nIn Depth: " + object;
            AwarenessNotifications.optionallySend(message);
        }
    }

    public static String identification() {
        return "identification=" + User.platform() + "|" + User.user() + "|" + User.file();
    }

    public static void refresh() {
        long ms = System.currentTimeMillis();
        if (connectionRefreshCooldown <= ms) {
            connectionRefreshCooldown = ms + 60000L;
            PluginBase.connectionThread.executeIfUnknownThreadElseHere(PluginAddons::refresh);
            PluginBase.connectionThread.executeIfUnknownThreadElseHere(() -> {
                String[][] announcements;
                List<PlayerProtocol> protocols = Permissions.getStaff();
                if (!protocols.isEmpty() && (announcements = CloudConnections.getStaffAnnouncements()).length == 3) {
                    for (String[] announcement : announcements) {
                        for (PlayerProtocol p : protocols) {
                            if (!AwarenessNotifications.canSend(p.getUUID(), "staff-announcement-" + announcement[0], Integer.parseInt(announcement[2]))) continue;
                            p.bukkit().sendMessage(AwarenessNotifications.getNotification(announcement[1]));
                        }
                    }
                }
            });
        }
    }

    static {
        PluginBase.runRepeatingTask(() -> PluginBase.connectionThread.execute(CloudBase::refresh), 1L, 60000L);
    }
}

