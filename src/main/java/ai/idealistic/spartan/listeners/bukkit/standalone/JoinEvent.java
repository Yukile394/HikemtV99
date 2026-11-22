/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package ai.idealistic.spartan.listeners.bukkit.standalone;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.connection.CloudBase;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.DetectionCharge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST)
    private void Event(PlayerJoinEvent e) {
        Player player =  e.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(n);
        if ((PluginAddons.freeEdition || Config.settings.getBoolean("Important.enable_watermark")) && !Permissions.isStaff(n)) {
            n.sendMessage("");
            AwarenessNotifications.forcefullySend(protocol, "\nThis server is protected by the Spartan AntiCheat", false);
            n.sendMessage("");
        }
        PluginBase.runDelayedTask(protocol, () -> {
            Config.settings.runOnLogin(protocol);
            CloudBase.announce(protocol);
            DetectionCharge.notify(protocol);
        }, 10L);
    }
}

