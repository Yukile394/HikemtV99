/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.functionality.command;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.DetectionNotifications;
import ai.idealistic.spartan.functionality.moderation.Wave;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.PlayerInteractAtEntityListener;
import ai.idealistic.spartan.utils.java.StringUtils;
import ai.idealistic.spartan.utils.math.Distance;
import ai.idealistic.spartan.utils.minecraft.server.ConfigUtils;
import ai.idealistic.spartan.utils.minecraft.server.ProxyUtils;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

public class CommandExecution
implements CommandExecutor {
    public static final int maxConnectedArgumentLength = 4096;
    public static final String support = "Click to receive plugin support.";

    private static void buildCommand(CommandSender sender, ChatColor chatColor, String command, String description) {
        ClickableMessage.sendCommand(sender, chatColor + command, description, command);
    }

    public static boolean defaultMessage(CommandSender sender, boolean isPlayer, boolean documentation) {
        if (!isPlayer || Permissions.has((Player)sender)) {
            sender.sendMessage("");
            String command = "\u00a72Spartan AntiCheat";
            ClickableMessage.sendURL(sender, command, "Click to learn more!", "https://spartan.top");
            if (documentation) {
                ClickableMessage.sendURL(sender, "\u00a78\u00a7l<> \u00a77Required command argument", "Click to learn more!", "https://spartan.top");
                ClickableMessage.sendURL(sender, "\u00a78\u00a7l[] \u00a77Optional command argument", "Click to learn more!", "https://spartan.top");
            }
            return true;
        }
        sender.sendMessage(Config.messages.getColorfulString("unknown_command"));
        return false;
    }

    public static void completeMessage(CommandSender sender, String list) {
        boolean isPlayer = sender instanceof Player;
        PlayerProtocol protocol = isPlayer ? PluginBase.getProtocol((Player)sender) : null;
        boolean info = !(isPlayer &= protocol != null) || Permissions.has(protocol.bukkit(), Permission.INFO);
        boolean manage = !isPlayer || Permissions.has(protocol.bukkit(), Permission.MANAGE);
        switch (list) {
            case "default": {
                if (!CommandExecution.defaultMessage(sender, isPlayer, !isPlayer)) break;
                if (isPlayer) {
                    if (manage) {
                        ClickableMessage.sendCommand(sender, "\u00a7cPanic Mode \u00a77(Click)", "Click this command to toggle silent mode and disable punishments for all checks.", "/spartan panic");
                    }
                    if (Permissions.has(protocol.bukkit(), Permission.RELOAD)) {
                        ClickableMessage.sendCommand(sender, "\u00a7cReload Plugin \u00a77(Click)", "Click this command to reload the plugin's cache.", "/spartan reload");
                    }
                    if (info) {
                        ClickableMessage.sendCommand(sender, "\u00a7cPlayer Info \u00a77(Click)", "Click this command to view useful information yourself.", "/spartan info");
                    }
                    if (manage) {
                        ClickableMessage.sendCommand(sender, "\u00a7cToggle Checks \u00a77(Click)", "Click this command to toggle a check and its detections.", "/spartan syn");
                        ClickableMessage.sendCommand(sender, "\u00a7cToggle Details \u00a77(Click)", "Click this command to toggle a check's detection details.", "/spartan syn");
                        ClickableMessage.sendCommand(sender, "\u00a7cToggle Preventions \u00a77(Click)", "Click this command to toggle a check's preventions.", "/spartan syn");
                        ClickableMessage.sendCommand(sender, "\u00a7cToggle Punishments \u00a77(Click)", "Click this command to toggle a check's punishments.", "/spartan syn");
                        ClickableMessage.sendCommand(sender, "\u00a7cToggle Detections \u00a77(Click)", "Click this command to toggle a check's specific detections.", "/spartan syn");
                        ClickableMessage.sendCommand(sender, "\u00a7cToggle Settings \u00a77(Click)", "Click this command to toggle general settings.", "/spartan syn");
                        ClickableMessage.sendCommand(sender, "\u00a7cView Statistics \u00a77(Click)", "Click this command to view the plugin's statistics.", "/spartan syn");
                    }
                    if (Permissions.has(protocol.bukkit(), Permission.USE_BYPASS)) {
                        ClickableMessage.sendCommand(sender, "\u00a7cPlayer Bypass \u00a77(Click)", "Click this command to give check bypass to a player.", "/spartan bypass *");
                    }
                } else {
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan panic", "This command can be used to enable silent mode and disable punishments for all checks.", null);
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan toggle <check>", "This command can be used to toggle a check and its detections.", null);
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan toggle-prevention <check>", "This command can be used to toggle a check's preventions.", null);
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan toggle-punishment <check>", "This command can be used to toggle a check's punishments.", null);
                    CommandExecution.buildCommand(sender, ChatColor.RED, "/spartan reload", "Click this command to reload the plugin's cache.");
                }
                if (isPlayer && !info && !Permissions.has(protocol.bukkit(), Permission.KICK) && !Permissions.has(protocol.bukkit(), Permission.WARN) && !Permissions.has(protocol.bukkit(), Permission.USE_BYPASS) && !Permissions.has(protocol.bukkit(), Permission.WAVE)) break;
                CommandExecution.buildCommand(sender, ChatColor.RED, "/spartan moderation", "Click this command to view a list of moderation commands.");
                break;
            }
            case "moderation": {
                if (!CommandExecution.defaultMessage(sender, isPlayer, true)) break;
                boolean permission = false;
                if (isPlayer && DetectionNotifications.hasPermission(protocol)) {
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan notifications [ticks-frequency]", "This command can be used to receive chat messages whenever a player is suspected of using hack modules.", null);
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan verbose", "This command can be used to enable all notifications to go through instead of only important ones when disabled.", null);
                }
                if (isPlayer && info) {
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan info [player]", "This command can be used to view useful information about a player.", null);
                }
                if (!isPlayer || Permissions.has(protocol.bukkit(), Permission.USE_BYPASS)) {
                    permission = true;
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan bypass <player> <check> [seconds]", "This command can be used to cause a player to temporarily bypass a check and its detections.", null);
                }
                if (!isPlayer || Permissions.has(protocol.bukkit(), Permission.WARN)) {
                    permission = true;
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan warn <player> <reason>", "This command can be used to individually warn a player about something important.", null);
                }
                if (!isPlayer || Permissions.has(protocol.bukkit(), Permission.KICK)) {
                    permission = true;
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan kick <player> <reason>", "This command can be used to kick players from the server for a specific reason.", null);
                }
                if (!isPlayer || Permissions.has(protocol.bukkit(), Permission.WAVE)) {
                    permission = true;
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan wave <add/remove/clear/run/list> [player] [command]", "This command can be used to add a player to a list with a command representing their punishment. This list can be executed manually by a player or automatically based on the plugin's configuration, and cause added players to punished all at once and in order.\n\nExample: /spartan wave add playerName ban {player} You have been banned for hacking!", null);
                }
                if (!isPlayer || Permissions.has(protocol.bukkit(), Permission.ADMIN)) {
                    permission = true;
                    ClickableMessage.sendCommand(sender, ChatColor.RED + "/spartan proxy-false>", "This command can be used to transfer commands to the proxy/network of servers. (Example: BungeeCord)", null);
                }
                if (permission) break;
                CommandExecution.completeMessage(sender, "default");
                break;
            }
            case "conditions": {
                if (!CommandExecution.defaultMessage(sender, isPlayer, true)) break;
                if (!isPlayer || Permissions.has(protocol.bukkit(), Permission.CONDITION)) {
                    sender.sendMessage(ChatColor.RED + "/spartan <player> if <condition> equals <result> do <command>");
                    sender.sendMessage(ChatColor.RED + "/spartan <player> if <condition> contains <result> do <command>");
                    sender.sendMessage(ChatColor.RED + "/spartan <player> if <number> is-less-than <result> do <command>");
                    sender.sendMessage(ChatColor.RED + "/spartan <player> if <number> is-greater-than <result> do <command>");
                    break;
                }
                CommandExecution.completeMessage(sender, "default");
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        PlayerProtocol protocol;
        boolean isPlayer = sender instanceof Player;
        if (!label.equalsIgnoreCase("spartan") || !isPlayer && !(sender instanceof ConsoleCommandSender)) return false;
        PlayerProtocol playerProtocol = protocol = isPlayer ? PluginBase.getProtocol((Player)sender) : null;
        if (args.length == 0) {
            if (isPlayer && PlayerInteractAtEntityListener.supported && Permissions.isStaff(protocol.bukkit()) && Config.settings.getBoolean("Important.enable_npc")) {
                PlayerInteractAtEntityListener.create(protocol);
            }
            CommandExecution.completeMessage(sender, "default");
            return false;
        } else if (args.length == 1) {
            if (isPlayer && args[0].equalsIgnoreCase("Syn")) {
                PluginBase.synMenu.open(protocol);
                return false;
            } else if (args[0].equalsIgnoreCase("Charge")) {
                if (isPlayer && !Permissions.isStaff(protocol.bukkit())) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                if (PluginAddons.freeEdition) {
                    PluginBase.chargeMenu.open(protocol);
                    return false;
                } else {
                    ClickableMessage.sendURL(sender, "You have the permium edition/s of the Spartan anti-cheat.", support, "https://www.spartan.top/discord");
                }
                return false;
            } else if (args[0].equalsIgnoreCase("Panic")) {
                if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.MANAGE)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                Check.handleCheck(!Check.a);
                if (Check.a) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("panic_mode_enable"), support, "https://www.spartan.top/discord");
                    return false;
                } else {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("panic_mode_disable"), support, "https://www.spartan.top/discord");
                }
                return false;
            } else if (args[0].equalsIgnoreCase("Moderation")) {
                CommandExecution.completeMessage(sender, args[0].toLowerCase());
                return false;
            } else if (args[0].equalsIgnoreCase("Conditions")) {
                CommandExecution.completeMessage(sender, args[0].toLowerCase());
                return false;
            } else if (args[0].equalsIgnoreCase("Reload") || args[0].equalsIgnoreCase("Rl")) {
                if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.RELOAD)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                Config.reload(sender);
                return false;
            } else if (isPlayer && args[0].equalsIgnoreCase("Info")) {
                if (!Permissions.has(protocol.bukkit(), Permission.INFO)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                PluginBase.playerInfo.open(protocol, sender.getName());
                return false;
            } else if (isPlayer && args[0].equalsIgnoreCase("Notifications")) {
                if (!DetectionNotifications.hasPermission(protocol)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                DetectionNotifications.set(protocol, Integer.MIN_VALUE);
                return false;
            } else if (isPlayer && args[0].equalsIgnoreCase("Verbose")) {
                if (!DetectionNotifications.hasPermission(protocol)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                if (DetectionNotifications.isVerboseEnabled(protocol)) {
                    DetectionNotifications.removeVerbose(protocol);
                    return false;
                } else {
                    DetectionNotifications.addVerbose(protocol);
                }
                return false;
            } else {
                CommandExecution.completeMessage(sender, "default");
            }
            return false;
        } else if (args[0].equalsIgnoreCase("Proxy-Command")) {
            if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.ADMIN)) {
                ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                return true;
            }
            StringBuilder argumentsToStringBuilder = new StringBuilder();
            for (i < args.length; ++i) {
                argumentsToStringBuilder.append(args[i]).append(" ");
            }
            String argumentsToString = argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1);
            if (isPlayer ? argumentsToString.length() > protocol.getMaxChatLength() : argumentsToString.length() > 4096) {
                ClickableMessage.sendURL(sender, Config.messages.getColorfulString("massive_command_reason"), support, "https://www.spartan.top/discord");
                return true;
            }
            if (!ProxyUtils.executeCommand(isPlayer ? protocol.bukkit() : null, argumentsToString)) {
                ClickableMessage.sendURL(sender, Config.messages.getColorfulString("failed_command").replace("{command}", StringUtils.toString(args, " ")), support, "https://www.spartan.top/discord");
                return true;
            }
            ClickableMessage.sendURL(sender, Config.messages.getColorfulString("successful_command"), support, "https://www.spartan.top/discord");
            return false;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("Wave")) {
                String command = args[1];
                if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.WAVE)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                if (command.equalsIgnoreCase("Run")) {
                    if (Wave.getWaveList().length == 0) {
                        ClickableMessage.sendURL(sender, Config.messages.getColorfulString("empty_wave_list"), support, "https://www.spartan.top/discord");
                        return true;
                    }
                    if (Wave.start()) return false;
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("failed_command").replace("{command}", StringUtils.toString(args, " ")), support, "https://www.spartan.top/discord");
                    return false;
                } else if (command.equalsIgnoreCase("Clear")) {
                    Wave.clear();
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("wave_clear_message"), support, "https://www.spartan.top/discord");
                    return false;
                } else if (command.equalsIgnoreCase("List")) {
                    sender.sendMessage(ChatColor.GRAY + "Wave Queued Players" + ChatColor.DARK_GRAY + ":");
                    sender.sendMessage(Wave.getWaveListString());
                    return false;
                } else {
                    CommandExecution.completeMessage(sender, "moderation");
                }
                return false;
            } else if (isPlayer && args[0].equalsIgnoreCase("Info")) {
                if (!Permissions.has(protocol.bukkit(), Permission.INFO)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                PluginBase.playerInfo.open(protocol, ConfigUtils.replaceWithSyntax(args[1], null));
                return false;
            } else if (args[0].equalsIgnoreCase("Toggle")) {
                String check = args[1];
                if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.MANAGE)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                boolean exists = false;
                for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                    if (!hackType.getCheck().getName().equalsIgnoreCase(check)) continue;
                    check = hackType.toString();
                    exists = true;
                    break;
                }
                if (exists) {
                    CheckEnums.HackType hackType = CheckEnums.HackType.valueOf(check);
                    Check checkObj = hackType.getCheck();
                    if (checkObj.handleCheck((Check.DataType)null, (String)null)) {
                        checkObj.handleCheck((Check.DataType)null, false);
                        String message = Config.messages.getColorfulString("check_disable_message");
                        message = isPlayer ? ConfigUtils.replaceWithSyntax((OfflinePlayer)((Player)sender), message, hackType) : ConfigUtils.replaceWithSyntax(message, hackType);
                        sender.sendMessage(message);
                        return false;
                    } else {
                        checkObj.handleCheck((Check.DataType)null, true);
                        String message = Config.messages.getColorfulString("check_enable_message");
                        message = isPlayer ? ConfigUtils.replaceWithSyntax((OfflinePlayer)((Player)sender), message, hackType) : ConfigUtils.replaceWithSyntax(message, hackType);
                        sender.sendMessage(message);
                    }
                    return false;
                } else {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("non_existing_check"), support, "https://www.spartan.top/discord");
                }
                return false;
            } else if (args[0].equalsIgnoreCase("Toggle-Prevention")) {
                String check = args[1];
                if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.MANAGE)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                boolean exists = false;
                for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                    if (!hackType.getCheck().getName().equalsIgnoreCase(check)) continue;
                    check = hackType.toString();
                    exists = true;
                    break;
                }
                if (exists) {
                    CheckEnums.HackType hackType = CheckEnums.HackType.valueOf(check);
                    Check checkObj = hackType.getCheck();
                    if (checkObj.checkCheckCancellation(null, null)) {
                        checkObj.handleDataType(null, false);
                        String message = Config.messages.getColorfulString("check_silent_disable_message");
                        message = isPlayer ? ConfigUtils.replaceWithSyntax((OfflinePlayer)((Player)sender), message, hackType) : ConfigUtils.replaceWithSyntax(message, hackType);
                        sender.sendMessage(message);
                        return false;
                    } else {
                        checkObj.handleDataType(null, true);
                        String message = Config.messages.getColorfulString("check_silent_enable_message");
                        message = isPlayer ? ConfigUtils.replaceWithSyntax((OfflinePlayer)((Player)sender), message, hackType) : ConfigUtils.replaceWithSyntax(message, hackType);
                        sender.sendMessage(message);
                    }
                    return false;
                } else {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("non_existing_check"), support, "https://www.spartan.top/discord");
                }
                return false;
            } else if (args[0].equalsIgnoreCase("Toggle-Punishment")) {
                String check = args[1];
                if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.MANAGE)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                boolean exists = false;
                for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                    if (!hackType.getCheck().getName().equalsIgnoreCase(check)) continue;
                    check = hackType.toString();
                    exists = true;
                    break;
                }
                if (exists) {
                    CheckEnums.HackType hackType = CheckEnums.HackType.valueOf(check);
                    Check checkObj = hackType.getCheck();
                    if (checkObj.checkCheckCancellation(null)) {
                        checkObj.isCheckCancellation(null, false);
                        String message = Config.messages.getColorfulString("check_punishment_disable_message");
                        message = isPlayer ? ConfigUtils.replaceWithSyntax((OfflinePlayer)((Player)sender), message, hackType) : ConfigUtils.replaceWithSyntax(message, hackType);
                        sender.sendMessage(message);
                        return false;
                    } else {
                        checkObj.isCheckCancellation(null, true);
                        String message = Config.messages.getColorfulString("check_punishment_enable_message");
                        message = isPlayer ? ConfigUtils.replaceWithSyntax((OfflinePlayer)((Player)sender), message, hackType) : ConfigUtils.replaceWithSyntax(message, hackType);
                        sender.sendMessage(message);
                    }
                    return false;
                } else {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("non_existing_check"), support, "https://www.spartan.top/discord");
                }
                return false;
            } else if (isPlayer && args[0].equalsIgnoreCase("Notifications")) {
                if (!DetectionNotifications.hasPermission(protocol)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                String divisorString = args[1];
                if (Distance.validInteger(divisorString)) {
                    int frequency = Integer.parseInt(divisorString);
                    if (frequency >= 0) {
                        DetectionNotifications.set(protocol, frequency);
                        return false;
                    } else {
                        ClickableMessage.sendURL(sender, Config.messages.getColorfulString("failed_command").replace("{command}", StringUtils.toString(args, " ")), support, "https://www.spartan.top/discord");
                    }
                    return false;
                } else {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("failed_command").replace("{command}", StringUtils.toString(args, " ")), support, "https://www.spartan.top/discord");
                }
                return false;
            } else {
                CommandExecution.completeMessage(sender, "default");
            }
            return false;
        } else {
            StringBuilder argumentsToStringBuilder = new StringBuilder();
            for (i < args.length; ++i) {
                argumentsToStringBuilder.append(args[i]).append(" ");
            }
            String argumentsToString = argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1);
            if (args[0].equalsIgnoreCase("Kick")) {
                if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.KICK)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                if (isPlayer ? argumentsToString.length() > protocol.getMaxChatLength() : argumentsToString.length() > 4096) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("massive_command_reason"), support, "https://www.spartan.top/discord");
                    return true;
                }
                PlayerProtocol playerProtocol2 = PluginBase.getAnyCaseProtocol(args[1]);
                if (playerProtocol2 == null) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("player_not_found_message"), support, "https://www.spartan.top/discord");
                    return true;
                }
                if (playerProtocol2.punishments.kick(sender, argumentsToString)) return false;
                ClickableMessage.sendURL(sender, Config.messages.getColorfulString("failed_command").replace("{command}", StringUtils.toString(args, " ")), support, "https://www.spartan.top/discord");
                return false;
            } else if (args[0].equalsIgnoreCase("Warn")) {
                if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.WARN)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                if (isPlayer ? argumentsToString.length() > protocol.getMaxChatLength() : argumentsToString.length() > 4096) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("massive_command_reason"), support, "https://www.spartan.top/discord");
                    return true;
                }
                PlayerProtocol playerProtocol3 = PluginBase.getAnyCaseProtocol(args[1]);
                if (playerProtocol3 == null) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("player_not_found_message"), support, "https://www.spartan.top/discord");
                    return true;
                }
                if (playerProtocol3.punishments.warn(sender, argumentsToString)) return false;
                ClickableMessage.sendURL(sender, Config.messages.getColorfulString("failed_command").replace("{command}", StringUtils.toString(args, " ")), support, "https://www.spartan.top/discord");
                return false;
            } else if (args[0].equalsIgnoreCase("Bypass")) {
                boolean bl;
                bl = args.length == 3;
                if (bl || args.length == 4) {
                    String sec;
                    CheckEnums.HackType[] hackTypes = CheckEnums.HackType.values();
                    int maxHackTypes = hackTypes.length;
                    String[] checks = args[2].split(",", maxHackTypes);
                    String string = sec = bl ? null : args[3];
                    if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.USE_BYPASS)) {
                        ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                        return true;
                    }
                    PlayerProtocol anyCaseProtocol =  PluginBase.getAnyCaseProtocol(args[1]);
                    if (t == null) {
                        ClickableMessage.sendURL(sender, Config.messages.getColorfulString("player_not_found_message"), support, "https://www.spartan.top/discord");
                        return true;
                    }
                    ArrayList<CheckEnums.HackType> found = new ArrayList<CheckEnums.HackType>maxHackTypes;
for (String check : checks) {
                        for (CheckEnums.HackType hackType : hackTypes) {
                            if (!hackType.getCheck().getName().equalsIgnoreCase(check)) continue;
                            found.add(hackType);
                            continue block23;
                        }
                    }
                    if (!found.isEmpty()) {
                        for (CheckEnums.HackType hackType : found) {
                            int seconds;
                            seconds = bl ? 0 : Integer.parseInt(sec);
                            if (bl) {
                                t.getRunner(hackType).addDisableCause("Command-" + sender.getName(), null, 0);
                            } else {
                                if (seconds < 1 || seconds > 3600) {
                                    sender.sendMessage(ChatColor.RED + "Seconds must be between 1 and 3600.");
                                    return true;
                                }
                                t.getRunner(hackType).addDisableCause("Command-" + sender.getName(), null, seconds * NumberConversions.ceil((double)20.0));
                            }
                            String message = ConfigUtils.replaceWithSyntax(t, Config.messages.getColorfulString("bypass_message"), hackType).replace("{time}", bl ? "infinite" : String.valueOf(seconds));
                            sender.sendMessage(message);
                        }
                        return false;
                    } else {
                        ClickableMessage.sendURL(sender, Config.messages.getColorfulString("non_existing_check"), support, "https://www.spartan.top/discord");
                    }
                    return false;
                } else {
                    CommandExecution.completeMessage(sender, "moderation");
                }
                return false;
            } else if (args[0].equalsIgnoreCase("Wave")) {
                String string = args[1];
                OfflinePlayer t = Bukkit.getOfflinePlayer((String)args[2]);
                if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.WAVE)) {
                    ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                    return true;
                }
                if (string.equalsIgnoreCase("add") && args.length >= 4) {
                    if (Wave.getWaveList().length >= 100) {
                        ClickableMessage.sendURL(sender, Config.messages.getColorfulString("full_wave_list"), support, "https://www.spartan.top/discord");
                        return true;
                    }
                    argumentsToStringBuilder = new StringBuilder();
                    for (i < args.length; ++i) {
                        argumentsToStringBuilder.append(args[i]).append(" ");
                    }
                    argumentsToString = argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1);
                    if (isPlayer ? argumentsToString.length() > protocol.getMaxChatLength() : argumentsToString.length() > 4096) {
                        ClickableMessage.sendURL(sender, Config.messages.getColorfulString("massive_command_reason"), support, "https://www.spartan.top/discord");
                        return true;
                    }
                    String message = Config.messages.getColorfulString("wave_add_message");
                    message = ConfigUtils.replaceWithSyntax(t, message, null);
                    sender.sendMessage(message);
                    Wave.add(t.getUniqueId(), argumentsToString);
                    return false;
                } else if (string.equalsIgnoreCase("remove")) {
                    UUID uuid = t.getUniqueId();
                    if (Wave.getCommand(uuid) == null) {
                        String message = Config.messages.getColorfulString("wave_not_added_message");
                        message = ConfigUtils.replaceWithSyntax(t, message, null);
                        sender.sendMessage(message);
                        return true;
                    }
                    Wave.remove(uuid);
                    String message = Config.messages.getColorfulString("wave_remove_message");
                    message = ConfigUtils.replaceWithSyntax(t, message, null);
                    sender.sendMessage(message);
                    return false;
                } else {
                    CommandExecution.completeMessage(sender, "moderation");
                }
                return false;
            } else if (args.length >= 4) {
                void var9_33;
                argumentsToStringBuilder = new StringBuilder();
                while (var9_33 < args.length) {
                    argumentsToStringBuilder.append(args[var9_33]).append(" ");
                    ++var9_33;
                }
                argumentsToString = argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1);
                if (args.length >= 7) {
                    if (isPlayer && !Permissions.has(protocol.bukkit(), Permission.CONDITION)) {
                        ClickableMessage.sendURL(sender, Config.messages.getColorfulString("no_permission"), support, "https://www.spartan.top/discord");
                        return true;
                    }
                    PlayerProtocol playerProtocol4 = PluginBase.getAnyCaseProtocol(args[0]);
                    if (playerProtocol4 == null) {
                        ClickableMessage.sendURL(sender, Config.messages.getColorfulString("player_not_found_message"), support, "https://www.spartan.top/discord");
                        return true;
                    }
                    if (!args[1].equalsIgnoreCase("if") || !args[5].equalsIgnoreCase("do")) return false;
                    String condition = ConfigUtils.replaceWithSyntax(playerProtocol4, args[2], null);
                    String result = ConfigUtils.replaceWithSyntax(playerProtocol4, args[4], null);
                    argumentsToStringBuilder = new StringBuilder();
                    for (i < args.length; ++i) {
                        argumentsToStringBuilder.append(args[i]).append(" ");
                    }
                    String command = ConfigUtils.replaceWithSyntax(playerProtocol4, argumentsToStringBuilder.substring(0, argumentsToStringBuilder.length() - 1), null);
                    switch (args[3].toLowerCase()) {
                        case "equals":
                        case "=": {
                            if (!condition.equalsIgnoreCase(result)) return false;
                            PluginBase.runCommand(command);
                            return false;
                        }
                        case "not-equals":
                        case "/=": {
                            if (condition.equalsIgnoreCase(result)) return false;
                            PluginBase.runCommand(command);
                            return false;
                        }
                        case "contains": {
                            if (!condition.contains(result)) return false;
                            PluginBase.runCommand(command);
                            return false;
                        }
                        case "is-less-than":
                        case "<": {
                            if (!(Distance.validInteger(condition) && Distance.validInteger(result) && Integer.parseInt(condition) < Integer.parseInt(result) || Distance.validDecimal(condition) && Distance.validDecimal(result) && Double.parseDouble(condition) < Double.parseDouble(result) || Distance.validInteger(condition) && Distance.validDecimal(result) && (double)Integer.parseInt(condition) < Double.parseDouble(result)) && (!Distance.validDecimal(condition) || !Distance.validInteger(result) || !(Double.parseDouble(condition) < (double)Integer.parseInt(result)))) return false;
                            PluginBase.runCommand(command);
                            return false;
                        }
                        case "is-greater-than":
                        case ">": {
                            if (!(Distance.validInteger(condition) && Distance.validInteger(result) && Integer.parseInt(condition) > Integer.parseInt(result) || Distance.validDecimal(condition) && Distance.validDecimal(result) && Double.parseDouble(condition) > Double.parseDouble(result) || Distance.validInteger(condition) && Distance.validDecimal(result) && (double)Integer.parseInt(condition) > Double.parseDouble(result)) && (!Distance.validDecimal(condition) || !Distance.validInteger(result) || !(Double.parseDouble(condition) > (double)Integer.parseInt(result)))) return false;
                            PluginBase.runCommand(command);
                        }
                        default: {
                            return false;
                        }
                    }
                } else {
                    CommandExecution.completeMessage(sender, "default");
                }
                return false;
            } else {
                CommandExecution.completeMessage(sender, "default");
            }
        }
        return false;
    }
}

