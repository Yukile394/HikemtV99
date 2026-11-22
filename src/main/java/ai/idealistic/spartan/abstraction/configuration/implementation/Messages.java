/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.configuration.implementation;

import ai.idealistic.spartan.abstraction.configuration.ConfigurationBuilder;
import ai.idealistic.spartan.functionality.connection.PluginAddons;

public class Messages
extends ConfigurationBuilder {
    public Messages() {
        super("messages");
    }

    @Override
    public void create() {
        this.addOption("console_name", "Console");
        this.addOption("no_permission", "&8[&2{prefix}&8]&c You don't have permission to interact with this.");
        this.addOption("player_not_found_message", "&8[&2{prefix}&8]&c Player not found.");
        this.addOption("config_reload", "&8[&2{prefix}&8]&e Config successfully reloaded.");
        this.addOption("panic_mode_enable", "&8[&2{prefix}&8]&a Panic mode enabled, all checks are set to silent and will not punish players.");
        this.addOption("panic_mode_disable", "&8[&2{prefix}&8]&c Panic mode disabled, all checks will now run per configuration standards.");
        this.addOption("kick_reason", "&c {reason}");
        this.addOption("kick_broadcast_message", "&8[&2{prefix}&8]&c {player}&7 was kicked for&4 {reason}");
        this.addOption("player_ip_limit_kick_message", "&8[&2{prefix}&8]&c The player limit of your IP has been reached.");
        this.addOption("player_violation_reset_message", "&8[&2{prefix}&8]&a Violations successfully reset for player&8: &2{player}");
        this.addOption("player_stored_data_delete_message", "&8[&2{prefix}&8]&a Stored data successfully deleted for player&8: &2{player}");
        this.addOption("detection_notification", "&8[&2{prefix}&8] &e{player} &7>> &c{detection:real}\u00a78[{detection:level}\u00a78] &7(\u00a7f{info}\u00a77)");
        this.addOption("check_stored_data_delete_message", "&8[&2{prefix}&8]&a Stored data successfully deleted for check&8: &2{check}");
        this.addOption("check_enable_message", "&8[&2{prefix}&8] &aYou enabled the check&8:&7 {detection}");
        this.addOption("check_disable_message", "&8[&2{prefix}&8] &cYou disabled the check&8:&7 {detection}");
        this.addOption("check_silent_disable_message", "&8[&2{prefix}&8] &aYou enabled preventions for the check&8:&7 {detection}");
        this.addOption("check_silent_enable_message", "&8[&2{prefix}&8] &cYou disabled preventions for the check&8:&7 {detection}");
        this.addOption("check_punishment_enable_message", "&8[&2{prefix}&8] &aYou enabled punishments for the check&8:&7 {detection}");
        this.addOption("check_punishment_disable_message", "&8[&2{prefix}&8] &cYou disabled punishments for the check&8:&7 {detection}");
        this.addOption("non_existing_check", "&8[&2{prefix}&8] &cThis check doesn't exist.");
        this.addOption("bypass_message", "&8[&2{prefix}&8] &c{player} &7is now bypassing the &4{detection} &7check for &e{time} &7seconds.");
        this.addOption("warning_message", "&c {reason}");
        this.addOption("warning_feedback_message", "&8[&2{prefix}&8]&7 You warned &c{player} &7for&8: &4{reason}");
        this.addOption("verbose_enable", "&8[&2{prefix}&8] &aYou enabled verbose.");
        this.addOption("verbose_disable", "&8[&2{prefix}&8] &cYou disabled verbose.");
        this.addOption("notifications_enable", "&8[&2{prefix}&8] &aYou enabled notifications.");
        this.addOption("notifications_modified", "&8[&2{prefix}&8] &eYou modified notifications.");
        this.addOption("notifications_disable", "&8[&2{prefix}&8] &cYou disabled notifications.");
        this.addOption("awareness_notification", "&8[&2{prefix} Notification&8]&a {info}");
        this.addOption("wave_start_message", "&8[&2{prefix}&8]&c The wave is starting.");
        this.addOption("wave_end_message", "&8[&2{prefix}&8]&c The wave has ended with a total of {total} action(s).");
        this.addOption("wave_clear_message", "&8[&2{prefix}&8]&c The wave was cleared.");
        this.addOption("wave_add_message", "&8[&2{prefix}&8]&a {player} was added to the wave.");
        this.addOption("wave_remove_message", "&8[&2{prefix}&8]&c {player} was removed from the wave.");
        this.addOption("full_wave_list", "&8[&2{prefix}&8]&c The wave list is full.");
        this.addOption("wave_not_added_message", "&8[&2{prefix}&8]&c {player} is not added to the wave.");
        this.addOption("unknown_command", "&fUnknown command. Please type \"/help\" for help.");
        this.addOption("failed_command", "&8[&2{prefix}&8]&c Command failed ({command}). Please check your arguments and try again.");
        this.addOption("successful_command", "&8[&2{prefix}&8]&a Command successful.");
        this.addOption("massive_command_reason", "&8[&2{prefix}&8]&c The length of the reason is too big.");
    }

    @Override
    public final String getString(String path) {
        return super.getString(path);
    }

    @Override
    public final String getColorfulString(String path) {
        String string = super.getColorfulString(path);
        if (PluginAddons.freeEdition && !string.contains("{prefix}")) {
            string = "\u00a74\u00a7l({prefix})\u00a7r " + string;
        }
        string = string.replace("{prefix}", "Spartan");
        return string;
    }
}

