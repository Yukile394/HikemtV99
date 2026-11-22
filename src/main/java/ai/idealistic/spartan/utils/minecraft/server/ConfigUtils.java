/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.utils.minecraft.server;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.BackgroundAPI;
import ai.idealistic.spartan.functionality.moderation.CrossServerNotifications;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

public class ConfigUtils {
    public static String replaceWithSyntax(String message, CheckEnums.HackType hackType) {
        message = message.replace("{space}", " ");
        message = message.replace("{online}", String.valueOf(PluginBase.getPlayerCount()));
        message = message.replace("{motd}", Bukkit.getMotd());
        message = message.replace("{server:name}", CrossServerNotifications.getServerName());
        message = message.replace("{plugin:version}", BackgroundAPI.getVersion());
        message = message.replace("{server:version}", MultiVersion.serverVersion.toString());
        message = message.replace("{line}", "\n");
        LocalDateTime now = LocalDateTime.now();
        message = message.replace("{date:time}", DateTimeFormatter.ofPattern("HH:mm:ss").format(now));
        message = message.replace("{date:d-m-y}", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(now));
        message = message.replace("{date:m-d-y}", DateTimeFormatter.ofPattern("MM/dd/yyyy").format(now));
        message = message.replace("{date:y-m-d}", DateTimeFormatter.ofPattern("yyyy/MM/dd").format(now));
        if (hackType != null) {
            Check check = hackType.getCheck();
            message = message.replace("{detection}", check.getName());
            message = message.replace("{detection:real}", hackType.toString());
        }
        return message;
    }

    public static String replaceWithSyntax(PlayerProtocol p, String message, CheckEnums.HackType hackType) {
        Location loc = p.getLocationOrVehicle();
        String worldName = p.getWorld().getName();
        message = message.replace("{player}", p.bukkit().getName());
        message = message.replace("{player:type}", p.getDataType().toString().toLowerCase());
        message = message.replace("{uuid}", p.getUUID().toString());
        message = message.replace("{ping}", String.valueOf(p.getPing()));
        message = message.replace("{world}", worldName);
        message = message.replace("{health}", String.valueOf(p.getHealth()));
        message = message.replace("{gamemode}", p.getGameMode().toString().toLowerCase());
        message = message.replace("{x}", String.valueOf(loc.getBlockX()));
        message = message.replace("{y}", String.valueOf(loc.getBlockY()));
        message = message.replace("{z}", String.valueOf(loc.getBlockZ()));
        message = message.replace("{yaw}", String.valueOf(NumberConversions.round((double)loc.getYaw())));
        message = message.replace("{pitch}", String.valueOf(NumberConversions.round((double)loc.getPitch())));
        if (hackType != null) {
            message = message.replace("{detection:silent}", String.valueOf(hackType.getCheck().checkCheckCancellation(p.getDataType(), worldName)));
            message = message.replace("{detection:punish}", String.valueOf(hackType.getCheck().checkCheckCancellation(p.getDataType())));
        }
        return ChatColor.translateAlternateColorCodes((char)'&', StringConfigUtils.replaceWithSyntax(message, hackType));
    }

    public static String replaceWithSyntax(OfflinePlayer off, String message, CheckEnums.HackType hackType) {
        boolean hasHackType;
        hasHackType = hackType != null;
        if (off.isOnline()) {
            PlayerProtocol p = PluginBase.getProtocol((Player)off);
            return ConfigUtils.replaceWithSyntax(p, message, hackType);
        }
        UUID uuid = off.getUniqueId();
        String name = off.getName();
        if (name != null) {
            message = message.replace("{player}", name);
        }
        message = message.replace("{uuid}", uuid.toString());
        if (hasHackType) {
            message = message.replace("{detection:silent}", String.valueOf(hackType.getCheck().checkCheckCancellation(null, null)));
            message = message.replace("{detection:punish}", String.valueOf(hackType.getCheck().checkCheckCancellation(null)));
        }
        return ChatColor.translateAlternateColorCodes((char)'&', StringConfigUtils.replaceWithSyntax(message, hackType));
    }

    public static void add(File file, String path, Object value) {
        YamlConfiguration filea = YamlConfiguration.loadConfiguration((File)file);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (!filea.contains(path)) {
            ConfigUtils.set(file, path, value);
        }
    }

    public static boolean has(File file, String path) {
        return YamlConfiguration.loadConfiguration((File)file).contains(path);
    }

    public static void set(File file, String path, Object value) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        YamlConfiguration filea = YamlConfiguration.loadConfiguration((File)file);
        filea.set(path, value);
        try {
            filea.save(file);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void add(String path, Object value) {
        if (!Register.plugin.getConfig().contains(path)) {
            ConfigUtils.set(path, value);
        }
    }

    public static void set(String path, Object value) {
        Register.plugin.getConfig().set(path, value);
        Register.plugin.saveConfig();
        Register.plugin.reloadConfig();
    }
}

