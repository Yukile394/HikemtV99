/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package ai.idealistic.spartan.functionality.moderation;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.moderation.DetectionNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.minecraft.server.ConfigUtils;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class Wave {
    private static File file = new File(Register.plugin.getDataFolder() + "/storage.yml");
    private static final String section = "Wave";
    private static final Map<UUID, String> commands = Collections.synchronizedMap(new LinkedHashMap());

    public static void clearCache() {
        synchronized (commands) {
            commands.clear();
        }
    }

    public static void create() {
        file = new File(Register.plugin.getDataFolder() + "/storage.yml");
        if (!file.exists()) {
            ConfigUtils.add(file, "Wave." + UUID.randomUUID() + ".command", "ban {player} wave punishment example");
        }
    }

    public static UUID[] getWaveList() {
        ConfigurationSection configurationSection = YamlConfiguration.loadConfiguration((File)file).getConfigurationSection(section);
        if (configurationSection != null) {
            LinkedList<UUID> list = new LinkedList<UUID>();
            for (String key : configurationSection.getKeys(false)) {
                try {
                    list.add(UUID.fromString(key));
                }
                catch (Exception exception) {}
            }
            return list.toArray(new UUID[0]);
        }
        return new UUID[0];
    }

    public static String getWaveListString() {
        StringBuilder list = new StringBuilder();
        for (UUID uuid : Wave.getWaveList()) {
            OfflinePlayer p = Bukkit.getOfflinePlayer((UUID)uuid);
            if (!p.hasPlayedBefore()) continue;
            list.append(ChatColor.RED).append(p.getName()).append(ChatColor.GRAY).append(", ");
        }
        if (list.length() >= 2) {
            list = new StringBuilder(list.substring(0, list.length() - 2));
        } else if (list.length() == 0) {
            list = new StringBuilder(Config.messages.getColorfulString("empty_wave_list"));
        }
        return list.toString();
    }

    public static String getCommand(UUID uuid) {
        return YamlConfiguration.loadConfiguration(Filefile).getString("Wave." + uuid + ".command");
    }

    public static void add(UUID uuid, String command) {
        ConfigUtils.set(file, "Wave." + uuid + ".command", command);
        if (Wave.getWaveList().length >= Math.max(Math.min(Bukkit.getMaxPlayers(), 500), 20)) {
            Wave.start();
        }
    }

    public static void remove(UUID uuid) {
        String id = "Wave." + uuid;
        ConfigUtils.set(file, id + ".command", null);
        ConfigUtils.set(file, id, null);
    }

    public static void clear() {
        for (UUID uuid : Wave.getWaveList()) {
            Wave.remove(uuid);
        }
    }

    public static boolean start() {
        UUID[] uuids = Wave.getWaveList();
        if (uuids.length > 0) {
            synchronized (commands) {
                Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
                if (!protocols.isEmpty()) {
                    String message = Config.messages.getColorfulString("wave_start_message");
                    for (PlayerProtocol protocol : protocols) {
                        if (!DetectionNotifications.hasPermission(protocol)) continue;
                        protocol.bukkit().sendMessage(message);
                    }
                }
                PluginBase.dataThread.executeIfFreeElseHere(() -> {
                    for (UUID uuid : uuids) {
                        try {
                            String command = Wave.getCommand(uuid);
                            if (command == null) continue;
                            commands.putIfAbsent(uuid, ConfigUtils.replaceWithSyntax(Bukkit.getOfflinePlayer((UUID)uuid), command, null));
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    }
                });
            }
            return true;
        }
        return false;
    }

    private static void handleWave(int total) {
        Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
        if (!protocols.isEmpty()) {
            String message = Config.messages.getColorfulString("wave_end_message").replace("{total}", String.valueOf(total));
            for (PlayerProtocol protocol : protocols) {
                if (!DetectionNotifications.hasPermission(protocol)) continue;
                protocol.bukkit().sendMessage(message);
            }
        }
    }

    static {
        PluginBase.runRepeatingTask(() -> {
            synchronized (commands) {
                int size = commands.size();
                if (size > 0) {
                    Iterator<Map.Entry<UUID, String>> iterator = commands.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<UUID, String> entry = iterator.next();
                        iterator.remove();
                        Wave.remove(entry.getKey());
                        PluginBase.runCommand(entry.getValue());
                    }
                    Wave.handleWave(size);
                }
            }
        }, 1L, 1L);
    }
}

