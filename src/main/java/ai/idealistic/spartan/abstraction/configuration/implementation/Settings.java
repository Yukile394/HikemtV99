/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.configuration.implementation;

import ai.idealistic.spartan.abstraction.configuration.ConfigurationBuilder;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.moderation.DetectionNotifications;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.util.Collection;

public class Settings
extends ConfigurationBuilder {
    public Settings() {
        super("settings");
    }

    @Override
    public void create() {
        this.addOption("Logs.log_file", true);
        this.addOption("Logs.log_console", true);
        this.addOption("Notifications.individual_only_notifications", false);
        this.addOption("Notifications.enable_notifications_on_login", true);
        this.addOption("Notifications.awareness_notifications", true);
        this.addOption("Notifications.message_clickable_command", "/teleport {player}");
        this.addOption("Important.server_name", "");
        this.addOption("Important.op_bypass", false);
        this.addOption("Important.bedrock_on_protocollib", true);
        this.addOption("Important.bedrock_client_permission", false);
        this.addOption("Important.bedrock_player_prefix", ".");
        this.addOption("Important.enable_developer_api", true);
        this.addOption("Important.enable_watermark", true);
        this.addOption("Important.enable_npc", false);
        this.addOption("Purchases.email_address", "");
        this.addOption("Purchases.patreon_full_name", "");
        this.addOption("Detections.ground_teleport_on_detection", true);
        this.addOption("Detections.damage_on_detection", false);
        this.addOption("Discord.webhook_hex_color", "4caf50");
        this.addOption("Discord.checks_webhook_url", "");
    }

    public void runOnLogin(PlayerProtocol p) {
        if (this.getBoolean("Notifications.enable_notifications_on_login") && DetectionNotifications.hasPermission(p) && !DetectionNotifications.isEnabled(p)) {
            DetectionNotifications.set(p, Integer.MIN_VALUE);
        }
    }

    public void runOnLogin() {
        Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
        if (!protocols.isEmpty()) {
            for (PlayerProtocol protocol : protocols) {
                this.runOnLogin(protocol);
            }
        }
    }

    @Override
    public final String getString(String path) {
        return super.getString(path);
    }

    @Override
    public final String getColorfulString(String path) {
        return super.getColorfulString(path);
    }
}

