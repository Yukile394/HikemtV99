/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.abstraction.protocol;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.moderation.DetectionNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.minecraft.server.ConfigUtils;
import java.util.Collection;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PlayerPunishments {
    private final PlayerProtocol parent;
    private long kickCooldown;
    private long warnCooldown;

    public PlayerPunishments(PlayerProtocol protocol) {
        this.parent = protocol;
    }

    public boolean kick(CommandSender punisher, String reason) {
        if (this.kickCooldown < System.currentTimeMillis()) {
            this.kickCooldown = System.currentTimeMillis() + 1000L;
            Player target = this.parent.bukkit();
            String punisherName = punisher instanceof ConsoleCommandSender ? Config.messages.getColorfulString("console_name") : punisher.getName();
            String kick = ConfigUtils.replaceWithSyntax((OfflinePlayer)target, Config.messages.getColorfulString("kick_reason").replace("{reason}", reason).replace("{punisher}", punisherName), null);
            String announcement = ConfigUtils.replaceWithSyntax((OfflinePlayer)target, Config.messages.getColorfulString("kick_broadcast_message").replace("{reason}", reason).replace("{punisher}", punisherName), null);
            Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
            if (!protocols.isEmpty()) {
                for (PlayerProtocol protocol : protocols) {
                    if (!DetectionNotifications.hasPermission(protocol)) continue;
                    protocol.bukkit().sendMessage(announcement);
                }
            }
            PluginBase.transferTask(this.parent, () -> {
                PlayerProtocol protocol;
                if (target.isOnline() && (protocol = PluginBase.getProtocol(target.getUniqueId())) != null) {
                    target.kickPlayer(kick);
                }
            });
            return true;
        }
        return false;
    }

    public boolean warn(CommandSender punisher, String reason) {
        if (this.warnCooldown < System.currentTimeMillis()) {
            this.warnCooldown = System.currentTimeMillis() + 1000L;
            Player target = this.parent.bukkit();
            String punisherName = punisher instanceof ConsoleCommandSender ? Config.messages.getColorfulString("console_name") : punisher.getName();
            String warning = ConfigUtils.replaceWithSyntax((OfflinePlayer)target, Config.messages.getColorfulString("warning_message").replace("{reason}", reason).replace("{punisher}", punisherName), null);
            String feedback = ConfigUtils.replaceWithSyntax((OfflinePlayer)target, Config.messages.getColorfulString("warning_feedback_message").replace("{reason}", reason).replace("{punisher}", punisherName), null);
            target.sendMessage(warning);
            punisher.sendMessage(feedback);
            return true;
        }
        return false;
    }
}

