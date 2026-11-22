/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package ai.idealistic.spartan.functionality.connection;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.connection.CloudBase;
import ai.idealistic.spartan.functionality.connection.CloudConnections;
import ai.idealistic.spartan.functionality.connection.User;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.java.RequestUtils;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class JarVerification {
    static final String website = "aHR0cHM6Ly93d3cudmFnZGVkZXMuY29tL21pbmVjcmFmdC9jbG91ZC8=";
    private static boolean valid = true;

    public static void run(Plugin plugin) {
        if (!User.enabled) {
            PluginBase.connectionThread.execute(() -> {
                int userID = CloudConnections.getUserIdentification();
                if (userID <= 0) {
                    valid = false;
                }
            });
        }
        if (User.enabled) {
            if (JarVerification.isValid(plugin)) {
                PluginAddons.refresh();
            } else {
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        } else if (!PluginAddons.refresh() && !JarVerification.isValid(plugin)) {
            String message = "This version of " + plugin.getName() + " does not have a license. If this download is pirated, please consider purchasing the plugin when your server starts making enough money. We also sell on BuiltByBit which supports many payment methods for all countries including yours.";
            List<PlayerProtocol> staff = Permissions.getStaff();
            if (!staff.isEmpty()) {
                for (PlayerProtocol protocol : staff) {
                    protocol.sendImportantMessage(AwarenessNotifications.getNotification(message));
                }
            }
            AwarenessNotifications.forcefullySend(message);
        }
    }

    private static boolean isValid(Plugin plugin) {
        boolean b;
        block3: {
            b = valid && plugin.getDescription().getAuthors().toString().startsWith("[Evangelos Dedes @Vagdedes");
            try {
                String line;
                String[] results = RequestUtils.get("https://www.vagdedes.com/minecraft/cloud/?" + CloudBase.identification() + "&action=add&data=userVerification");
                if (results.length > 0 && (line = results[0]).equalsIgnoreCase("false")) {
                    valid = false;
                    return false;
                }
            }
            catch (Exception e) {
                if (!User.canAdvertise()) break block3;
                AwarenessNotifications.forcefullySend(e.getMessage());
            }
        }
        return b;
    }
}

