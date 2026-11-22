/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.functionality.moderation;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.moderation.DetectionNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import ai.idealistic.spartan.utils.java.OverflowMap;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrossServerNotifications {
    private static final int rowLimit = CheckEnums.HackType.values().length;
    private static final Map<Long, Boolean> processed = new OverflowMap<Long, Boolean>(new ConcurrentHashMap(), rowLimit);

    private static void run(List<PlayerProtocol> protocols) {
        block7: {
            ResultSet rs = Config.sql.query("SELECT id, player_name, server_name, notification, information, functionality FROM " + Config.sql.getTable() + " WHERE notification IS NOT NULL ORDER BY id DESC LIMIT " + rowLimit + ";");
            try {
                if (rs == null) break block7;
while (rs.next()) {
                    long long =  rs.getLong("id");
                    if (processed.containsKey(id)) continue;
                    String functionality = rs.getString("functionality");
                    String playerName = rs.getString("player_name");
                    if (playerName == null) continue;
                    for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                        if (!hackType.toString().equals(functionality)) continue;
                        String notification = rs.getString("notification");
                        String serverName = rs.getString("server_name");
                        String detection = ResearchEngine.findInformation(rs.getString("information"), "Detection:");
                        String levelString = ResearchEngine.findInformation(rs.getString("information"), "Level:");
                        String notificationString = ResearchEngine.findInformation(rs.getString("information"), "Notification-Level:");
                        if (levelString == null || notificationString == null) continue;
                        notification = "\u00a7l[" + serverName + "]\u00a7r " + notification;
                        for (PlayerProtocol protocol : protocols) {
                            CheckDetection staffDetection = protocol.getRunner(hackType).getDetection(detection);
                            if (staffDetection == null || !staffDetection.canSendNotification(System.currentTimeMillis(), Integer.parseInt(levelString), Integer.parseInt(notificationString))) continue;
                            protocol.bukkit().sendMessage(notification);
                            processed.put(id, true);
                        }
                        continue block3;
                    }
                }
            }
            finally {
                if (Collections.singletonList(rs).get(0) != null) {
                    rs.close();
                }
            }
        }
    }

    public static String getServerName() {
        return Config.settings.getString("Important.server_name");
    }

    static {
        PluginBase.runRepeatingTask(() -> PluginBase.connectionThread.executeIfFree(() -> {
            List<PlayerProtocol> protocols;
            if (Config.sql.isEnabled() && !(protocols = DetectionNotifications.getPlayers()).isEmpty()) {
                CrossServerNotifications.run(protocols);
            }
        }), 1L, 1L);
    }
}

