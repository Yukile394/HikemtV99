/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package ai.idealistic.spartan.functionality.tracking;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class AntiCheatLogs {
    private static final String dateOnlyFormat = "yyyyMMdd";
    public static final String dateFormat = "yyyy/MM/dd HH:mm:ss:SSSXXX";
    public static final String folderPath = Register.plugin.getDataFolder() + "/logs";
    public static final String playerIdentifier = "Player:";
    private static File todayFile = AntiCheatLogs.getFile();
    private static YamlConfiguration fileConfiguration = YamlConfiguration.loadConfiguration((File)todayFile);
    private static final Object synchronizer = new Object();
    private static boolean saving = false;

    public static String getDate(String pattern, long time) {
        return DateTimeFormatter.ofPattern(pattern).format(Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()));
    }

    private static String getFileName() {
        return "log" + ZonedDateTime.now().format(DateTimeFormatter.ofPattern(dateOnlyFormat)) + ".yml";
    }

    private static File getFile() {
        return new File(folderPath + "/" + AntiCheatLogs.getFileName());
    }

    private static void rawSave() {
        try {
            fileConfiguration.save(todayFile);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void save() {
        if (!saving) {
            synchronized (synchronizer) {
                saving = true;
                AntiCheatLogs.rawSave();
                saving = false;
            }
        }
    }

    public static void rawLogInfo(long time, String information, boolean console, boolean store, boolean sql) {
        if (console && Config.settings.getBoolean("Logs.log_console")) {
            Bukkit.getConsoleSender().sendMessage(information);
        }
        if (Config.settings.getBoolean("Logs.log_file") && store && fileConfiguration != null) {
            String fileName = AntiCheatLogs.getFileName();
            if (!fileName.equals(todayFile.getName())) {
                AntiCheatLogs.rawSave();
                todayFile = AntiCheatLogs.getFile();
                fileConfiguration = YamlConfiguration.loadConfiguration((File)todayFile);
            }
            PluginBase.dataThread.executeIfUnknownThreadElseHere(() -> {
                fileConfiguration.set(AntiCheatLogs.getDate(dateFormat, time), information);
                AntiCheatLogs.save();
            });
        }
        if (sql) {
            Config.sql.logInfo(null, null, information, null, null, System.currentTimeMillis());
        }
    }

    public static void logInfo(PlayerProtocol p, String notification, String information, boolean console, Material material, CheckEnums.HackType hackType, long time) {
        AntiCheatLogs.rawLogInfo(time, information, console, true, false);
        Config.sql.logInfo(p, notification, information, material, hackType, time);
    }
}

