/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.World$Environment
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package ai.idealistic.spartan.functionality.tracking;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.profiling.MiningHistory;
import ai.idealistic.spartan.abstraction.profiling.PlayerProfile;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.GeneralThread;
import ai.idealistic.spartan.functionality.connection.CloudBase;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.AntiCheatLogs;
import ai.idealistic.spartan.utils.java.ConcurrentList;
import ai.idealistic.spartan.utils.math.Distance;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class ResearchEngine {
    private static boolean firstLoad = false;
    public static final Map<Integer, Double> averageViolationTime = new ConcurrentHashMap<Integer, Double>();
    public static Map<CheckEnums.HackType, Collection<Check.DataType>> violationFired = new ConcurrentHashMap<CheckEnums.HackType, Collection<Check.DataType>>();
    private static long schedulerTicks = 0L;
    private static final Map<String, PlayerProfile> playerProfiles = new ConcurrentHashMap<String, PlayerProfile>();
    private static final GeneralThread.ThreadPool statisticsThread = new GeneralThread.ThreadPool(1L);

    public static Collection<PlayerProfile> getPlayerProfiles() {
        return playerProfiles.values();
    }

    public static PlayerProfile getPlayerProfile(String name) {
        PlayerProfile playerProfile = playerProfiles.get(name);
        if (playerProfile == null) {
            playerProfile = new PlayerProfile(name);
            playerProfiles.put(name, playerProfile);
        }
        return playerProfile;
    }

    public static PlayerProfile getAnyCasePlayerProfile(String name) {
        if (!playerProfiles.isEmpty()) {
            for (Map.Entry<String, PlayerProfile> entry : playerProfiles.entrySet()) {
                if (!entry.getKey().equalsIgnoreCase(name)) continue;
                return entry.getValue();
            }
        }
        return null;
    }

    public static PlayerProfile getPlayerProfile(PlayerProtocol protocol) {
        PlayerProfile playerProfile = playerProfiles.get(protocol.bukkit().getName());
        if (playerProfile == null) {
            playerProfile = new PlayerProfile(protocol);
            playerProfiles.put(protocol.bukkit().getName(), playerProfile);
        }
        return playerProfile;
    }

    private static void createPlayerProfile(PlayerProtocol protocol) {
        PlayerProfile profile = new PlayerProfile(protocol);
        profile.update(protocol);
        playerProfiles.put(protocol.bukkit().getName(), new PlayerProfile(protocol));
    }

    public static void resetData(CheckEnums.HackType hackType) {
        if (firstLoad) {
            statisticsThread.execute(() -> {
                Collection<File> files;
                String hackTypeString = hackType.toString();
                if (!playerProfiles.isEmpty()) {
                    for (PlayerProfile playerProfile : playerProfiles.values()) {
                        CheckRunner runner = playerProfile.getRunner(hackType);
                        if (runner != null) {
                            for (CheckDetection exception : runner.getDetections()) {
                                for (Check.DataType dataType : Check.DataType.values()) {
                                    exception.clearData(dataType);
                                }
                            }
                        } else {
                            for (String string : hackType.getDetections()) {
                                for (Check.DataType dataType : Check.DataType.values()) {
                                    playerProfile.clearTimeDifferences(hackType, dataType, string);
                                }
                            }
                        }
                        playerProfile.getContinuity().clear();
                    }
                    ResearchEngine.updateCache(true);
                }
                if (Config.sql.isEnabled()) {
                    Config.sql.update("DELETE FROM " + Config.sql.getTable() + " WHERE functionality = '" + hackTypeString + "';");
                }
                if (!(files = ResearchEngine.getFiles()).isEmpty()) {
                    for (File file : files) {
                        YamlConfiguration configuration = YamlConfiguration.loadConfiguration((File)file);
                        for (String key : configuration.getKeys(false)) {
                            String value = configuration.getString(key);
                            if (value == null || !value.contains(hackTypeString)) continue;
                            configuration.set(key, null);
                        }
                        try {
                            configuration.save(file);
                        }
                        catch (Exception exception) {}
                    }
                }
            });
        }
    }

    public static void resetData(String playerName) {
        if (firstLoad) {
            PlayerProtocol player = PluginBase.getProtocol(playerName);
            if (ResearchEngine.isStorageMode()) {
                statisticsThread.execute(() -> {
                    Collection<File> files;
                    if (player == null) {
                        playerProfiles.remove(playerName);
                    } else {
                        ResearchEngine.createPlayerProfile(player);
                    }
                    if (Config.sql.isEnabled()) {
                        Config.sql.update("DELETE FROM " + Config.sql.getTable() + " WHERE information LIKE '%" + playerName + "%';");
                    }
                    if (!(files = ResearchEngine.getFiles()).isEmpty()) {
                        for (File file : files) {
                            YamlConfiguration configuration = YamlConfiguration.loadConfiguration((File)file);
                            for (String key : configuration.getKeys(false)) {
                                String value = configuration.getString(key);
                                if (value == null || !value.contains(playerName)) continue;
                                configuration.set(key, null);
                            }
                            try {
                                configuration.save(file);
                            }
                            catch (Exception exception) {}
                        }
                    }
                });
            } else if (player == null) {
                playerProfiles.remove(playerName);
            } else {
                ResearchEngine.createPlayerProfile(player);
            }
            PluginBase.playerInfo.refresh(playerName);
        }
    }

    public static String findInformation(String s, String pattern) {
        String find = "(" + pattern + " ";
        int index = s.indexOf(find);
        if (index > -1) {
            s = s.substring(index + find.length());
            index = s.indexOf(")");
            return s.substring(0, index);
        }
        return null;
    }

    private static Check.DataType findDataType(String s) {
        if ((s = ResearchEngine.findInformation(s, "Java:")) != null) {
            for (Check.DataType dataType : Check.DataType.values()) {
                if (!s.equals(dataType.toString())) continue;
                return dataType;
            }
        }
        return Check.DataType.JAVA;
    }

    private static boolean isStorageMode() {
        return Config.settings.getBoolean("Logs.log_file") || Config.sql.isEnabled();
    }

    private static Collection<File> getFiles() {
        File[] files = new File(AntiCheatLogs.folderPath).listFiles();
        if (files != null && files.length > 0) {
            TreeMap<Integer, File> map = new TreeMap<Integer, File>();
            String start = "log";
            String end = ".yml";
            int startLength = start.length();
            int endLength = end.length();
            for (File file : files) {
                Integer integer;
                String name;
                if (!file.isFile() || !(name = file.getName()).startsWith(start) || !name.endsWith(end) || (integer = Distance.returnValidInteger(name.substring(startLength, name.length() - endLength))) == null) continue;
                map.put(integer, file);
            }
            LinkedList<File> list = new LinkedList<File>(map.values());
            Collections.reverse(list);
            return list;
        }
        return new ArrayList<File>(0);
    }

    private static Map<String, String> getLogs() {
        Collection<File> files;
        boolean continueWithYAML;
        boolean isFull;
        LinkedHashMap<String, String> cache;
        block10: {
            cache = new LinkedHashMap<String, String>();
            int byteSize = 0;
            isFull = false;
            continueWithYAML = false;
            if (Config.sql.isEnabled()) {
                try {
                    ResultSet rs = Config.sql.query("SELECT creation_date, information FROM " + Config.sql.getTable() + " ORDER BY id DESC LIMIT " + PluginBase.maxSQLRows + ";");
                    if (rs != null) {
                        while (rs.next()) {
                            String data = rs.getString("information");
                            String date = rs.getString("creation_date");
                            cache.put(date, data);
                            if ((byteSize += date.length() + data.length()) < PluginBase.maxBytes) continue;
                            isFull = true;
                            break;
                        }
                        rs.close();
                        if (cache.isEmpty()) {
                            continueWithYAML = true;
                        }
                        break block10;
                    }
                    continueWithYAML = true;
                }
                catch (Exception ex) {
                    continueWithYAML = true;
                }
            } else {
                continueWithYAML = true;
            }
        }
        if (!isFull && continueWithYAML && !(files = ResearchEngine.getFiles()).isEmpty()) {
            for (File file : files) {
                YamlConfiguration c = YamlConfiguration.loadConfiguration((File)file);
                for (String key : c.getKeys(false)) {
                    String data = c.getString(key);
                    if (data == null) continue;
                    cache.put(key, data);
                    if ((byteSize += key.length() + data.length()) < PluginBase.maxBytes) continue;
                    isFull = true;
                    break;
                }
                if (!isFull) continue;
            }
        }
        return cache;
    }

    public static void refresh(boolean enabledPlugin) {
        Runnable runnable = () -> {
            Config.sql.refreshDatabase();
            if (enabledPlugin) {
                ResearchEngine.buildCache();
                CloudBase.refresh();
            }
        };
        if (firstLoad) {
            statisticsThread.executeIfFree(runnable);
        } else {
            statisticsThread.execute(runnable);
            firstLoad = true;
        }
    }

    private static void buildCache() {
        Map<String, String> logs;
        if (ResearchEngine.isStorageMode() && !(logs = ResearchEngine.getLogs()).isEmpty()) {
for (Map.Entry<String, String> entry : logs.entrySet()) {
                String fullDate = entry.getKey();
                String data = entry.getValue();
                String detection = ResearchEngine.findInformation(data, "Detection:");
                if (detection != null) {
                    String player;
                    String hackTypeString = ResearchEngine.findInformation(data, "Check:");
                    if (hackTypeString == null || (player = ResearchEngine.findInformation(data, "Player:")) == null) continue;
                    for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                        if (!hackTypeString.equals(hackType.toString())) continue;
                        Check.DataType dataType = ResearchEngine.findDataType(data);
                        PlayerProfile profile = ResearchEngine.getPlayerProfile(player);
                        profile.setLastDataType(dataType);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSSXXX");
                        try {
                            hackType.addDetection(detection, 500L);
                            profile.addTimeDifference(hackType, dataType, detection, sdf.parse(fullDate).getTime());
                        }
                        catch (Exception exception) {}
                        continue block4;
                    }
                    continue;
                }
                String oreString = ResearchEngine.findInformation(data, "Ore: ");
                if (oreString != null) {
                    World.Environment environment;
                    MiningHistory.MiningOre ore;
                    Material material;
                    String environmentString;
                    String player;
                    String amount = ResearchEngine.findInformation(data, "Amount: ");
                    if (amount == null || !Distance.validInteger(amount) || (player = ResearchEngine.findInformation(data, "Player:")) == null || (environmentString = ResearchEngine.findInformation(data, "Environment: ")) == null || (material = Spade.findMaterial(oreString.toUpperCase().replace("-", "_"))) == null || (ore = MiningHistory.getMiningOre(material)) == null || (environment = Spade.findEnvironment(environmentString.toUpperCase().replace("-", "_"))) == null) continue;
                    ResearchEngine.getPlayerProfile(player).getMiningHistory(ore).increaseMines(environment, Integer.parseInt(amount));
                    continue;
                }
                int index = data.indexOf(" was active for: ");
                if (index == -1) continue;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSSXXX");
                try {
                    ResearchEngine.getPlayerProfile(data.split(" ", 2)[0]).getContinuity().setActiveTime(sdf.parse(fullDate).getTime(), Long.parseLong(data.substring(index + 17)), false);
                }
                catch (Exception exception) {
                }
            }
        }
        for (PlayerProfile profile : playerProfiles.values()) {
            for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                for (String detection : hackType.getDetections()) {
                    profile.sortTimeDifferences(hackType, detection);
                }
            }
        }
        ResearchEngine.updateCache(true);
    }

    private static void updateCache(boolean force) {
        if (!(!force && violationFired.isEmpty() || playerProfiles.isEmpty())) {
            Collection<PlayerProfile> profiles = playerProfiles.values();
            for (CheckEnums.HackType hackType : force ? Arrays.asList(CheckEnums.HackType.values()) : violationFired.keySet()) {
                Collection<Check.DataType> dataTypes = violationFired.get(hackType);
                for (Check.DataType dataType : force ? Arrays.asList(Check.DataType.values()) : (dataTypes == null || dataTypes.isEmpty() ? Arrays.asList(Check.DataType.values()) : dataTypes)) {
                    LinkedHashMap<String, List> statistics = new LinkedHashMap<String, List>();
                    if (hackType.getCheck().handleCheck(dataType, null)) {
                        for (PlayerProfile playerProfile : profiles) {
                            for (String detectionExecutor : hackType.getDetections()) {
                                statistics.computeIfAbsent(detectionExecutor, k -> new ArrayList()).add(playerProfile.getTimeDifferences(hackType, dataType, detectionExecutor));
                            }
                        }
                        if (statistics.isEmpty()) {
                            for (PlayerProfile playerProfile : profiles) {
                                for (String detection : hackType.getDetections()) {
                                    playerProfile.clearTimeDifferences(hackType, dataType, detection);
                                    averageViolationTime.remove(CheckDetection.generalHashCode(hackType, detection) * 31 + dataType.hashCode());
                                }
                            }
                            continue;
                        }
                        for (Map.Entry entry : statistics.entrySet()) {
                            long time;
                            Iterator iterator;
                            String detection;
                            List lists = (List)entry.getValue();
                            detection = (String)entry.getKey();
                            double mean = 0.0;
                            double realizedMean = 0.0;
                            double realizedTotal = 0.0;
                            double total = 0.0;
                            for (List list : lists) {
                                if (list.isEmpty()) continue;
                                iterator = list.iterator();
                                while (iterator.hasNext()) {
                                    time = (Long)iterator.next();
                                    realizedMean += (double)time;
                                }
                                realizedTotal += (double)list.size();
                            }
                            if (realizedTotal > 0.0) {
                                realizedMean /= realizedTotal;
                                for (List list : lists) {
                                    if (list.isEmpty()) {
                                        mean += realizedMean;
                                        total += 1.0;
                                        continue;
                                    }
                                    iterator = list.iterator();
                                    while (iterator.hasNext()) {
                                        time = (Long)iterator.next();
                                        mean += (double)time;
                                    }
                                    total += (double)list.size();
                                }
                            } else {
                                realizedMean = hackType.getDefaultAverageTime(detection);
                            }
                            for (List list : lists) {
                                iterator = list.iterator();
                                while (iterator.hasNext()) {
                                    time = (Long)iterator.next();
                                    mean += (double)time;
                                }
                                total += (double)list.size();
                            }
                            mean = total > 0.0 ? (mean /= total) : realizedMean;
                            averageViolationTime.put(CheckDetection.generalHashCode(hackType, detection) * 31 + dataType.hashCode(), Math.min(mean, (double)(hackType.getDefaultAverageTime(detection) * 3L)));
                        }
                        continue;
                    }
                    for (PlayerProfile playerProfile : profiles) {
                        for (String detection : hackType.getDetections()) {
                            playerProfile.clearTimeDifferences(hackType, dataType, detection);
                            averageViolationTime.remove(CheckDetection.generalHashCode(hackType, detection) * 31 + dataType.hashCode());
                        }
                    }
                }
            }
            violationFired.clear();
        }
    }

    public static void queueToCache(CheckEnums.HackType hackType, Check.DataType dataType) {
        violationFired.computeIfAbsent(hackType, k -> new ConcurrentList()).add(dataType);
    }

    public static double getAverageViolationTime(CheckDetection detection, Check.DataType dataType) {
        double average = averageViolationTime.getOrDefault(detection.generalHashCode() * 31 + dataType.hashCode(), Double.valueOf(detection.defaultAverageTime));
        return average > 0.0 ? average : (double)detection.defaultAverageTime;
    }

    static {
        PluginBase.runRepeatingTask(() -> {
            if (firstLoad) {
                if (schedulerTicks == 0L) {
                    schedulerTicks = 1200L;
                    if (Config.sql.isEnabled()) {
                        ResearchEngine.refresh(Register.isPluginEnabled());
                    } else {
                        statisticsThread.executeIfFree(() -> ResearchEngine.updateCache(false));
                    }
                } else {
                    --schedulerTicks;
                    statisticsThread.executeIfFree(() -> ResearchEngine.updateCache(false));
                }
            }
        }, 1L, 1L);
    }
}

