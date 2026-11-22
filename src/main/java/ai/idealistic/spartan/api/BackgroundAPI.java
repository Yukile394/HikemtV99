/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.api;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.User;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.moderation.DetectionNotifications;
import ai.idealistic.spartan.functionality.moderation.Wave;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

public class BackgroundAPI {
    public static String licenseID() {
        return User.user();
    }

    public static String getVersion() {
        return Register.plugin != null ? Register.plugin.getDescription().getVersion() : "Unknown";
    }

    public static String getMessage(String path) {
        return Config.messages.getColorfulString(path);
    }

    public static boolean getSetting(String path) {
        return Config.settings.getBoolean(path);
    }

    @Deprecated
    public static String getCategory(Player p, CheckEnums.HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getCategory' has been removed.");
        return null;
    }

    public static boolean hasVerboseEnabled(Player p) {
        PlayerProtocol protocol = PluginBase.getProtocol(p);
        return DetectionNotifications.isVerboseEnabled(protocol);
    }

    public static boolean hasNotificationsEnabled(Player p) {
        PlayerProtocol protocol = PluginBase.getProtocol(p);
        return DetectionNotifications.isEnabled(protocol);
    }

    @Deprecated
    public static int getViolationResetTime() {
        AwarenessNotifications.forcefullySend("The API method 'getViolationResetTime' has been removed.");
        return 0;
    }

    public static void setVerbose(Player p, boolean value) {
        PlayerProtocol protocol = PluginBase.getProtocol(p);
        if (value) {
            DetectionNotifications.addVerbose(protocol);
        } else {
            DetectionNotifications.removeVerbose(protocol);
        }
    }

    public static void setNotifications(Player p, boolean value) {
        PlayerProtocol protocol = PluginBase.getProtocol(p);
        if (value) {
            DetectionNotifications.set(protocol, Integer.MIN_VALUE);
        } else {
            DetectionNotifications.remove(protocol);
        }
    }

    @Deprecated
    public static void setVerbose(Player p, boolean value, int frequency) {
        AwarenessNotifications.forcefullySend("The API method 'setVerbose' has been removed.");
    }

    public static void setNotifications(Player p, int frequency) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            PlayerProtocol protocol = PluginBase.getProtocol(p);
            DetectionNotifications.set(protocol, Math.abs(frequency));
        }
    }

    public static int getPing(Player p) {
        return PluginBase.getProtocol(p).getPing();
    }

    @Deprecated
    public static double getTPS() {
        AwarenessNotifications.forcefullySend("The API method 'getTPS' has been removed.");
        return 0.0;
    }

    public static boolean isEnabled(CheckEnums.HackType HackType2) {
        return HackType2.getCheck().handleCheck((Check.DataType)null, (String)null);
    }

    public static boolean isSilent(CheckEnums.HackType HackType2) {
        return HackType2.getCheck().checkCheckCancellation(null, null);
    }

    public static int getVL(Player p, CheckEnums.HackType hackType) {
        return NumberConversions.round((double)BackgroundAPI.getDecimalVL(p, hackType));
    }

    public static double getCertainty(Player p, CheckEnums.HackType hackType) {
        PlayerProtocol protocol = PluginBase.getProtocol(p);
        Collection<CheckDetection> detections = protocol.getRunner(hackType).getDetections();
        double avg = 0.0;
        double punishment = 0.0;
        for (CheckDetection detection : detections) {
            avg += (double)detection.getLevel(protocol.getDataType());
            punishment += (double)detection.getPunishmentLevel(protocol.getDataType());
        }
        return NumberConversions.round((double)(avg / (double)detections.size() / (punishment / (double)detections.size())));
    }

    public static double getDecimalVL(Player p, CheckEnums.HackType hackType) {
        PlayerProtocol protocol = PluginBase.getProtocol(p);
        Collection<CheckDetection> detections = protocol.getRunner(hackType).getDetections();
        double avg = 0.0;
        for (CheckDetection detection : detections) {
            avg += (double)detection.getLevel(protocol.getDataType());
        }
        return avg / (double)detections.size();
    }

    public static int getVL(Player p) {
        return NumberConversions.round((double)BackgroundAPI.getDecimalVL(p));
    }

    public static double getDecimalVL(Player p) {
        PlayerProtocol protocol = PluginBase.getProtocol(p);
        double avg = 0.0;
        double detectionsTotal = 0.0;
        for (CheckRunner runner : protocol.getRunners()) {
            Collection<CheckDetection> detections = runner.getDetections();
            for (CheckDetection detection : detections) {
                avg += (double)detection.getLevel(protocol.getDataType());
            }
            detectionsTotal += (double)detections.size();
        }
        return avg / detectionsTotal;
    }

    @Deprecated
    public static void setVL(Player p, CheckEnums.HackType HackType2, int amount) {
        AwarenessNotifications.forcefullySend("The API method 'setVL' has been removed.");
    }

    @Deprecated
    public static int getCancelViolation(CheckEnums.HackType hackType, String worldName) {
        return BackgroundAPI.getCancelViolation(hackType);
    }

    @Deprecated
    public static int getCancelViolation(CheckEnums.HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getCancelViolation' has been removed.");
        return 0;
    }

    @Deprecated
    public static int getViolationDivisor(Player p, CheckEnums.HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getViolationDivisor' has been removed.");
        return 0;
    }

    public static void reloadConfig() {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Config.reload(null);
        }
    }

    @Deprecated
    public static void reloadPermissions() {
        AwarenessNotifications.forcefullySend("The API method 'reloadPermissions' has been removed.");
    }

    @Deprecated
    public static void reloadPermissions(Player p) {
        AwarenessNotifications.forcefullySend("The API method 'reloadPermissions' has been removed.");
    }

    public static void enableCheck(CheckEnums.HackType HackType2) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            HackType2.getCheck().handleCheck((Check.DataType)null, true);
        }
    }

    public static void disableCheck(CheckEnums.HackType HackType2) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            HackType2.getCheck().handleCheck((Check.DataType)null, false);
        }
    }

    public static void cancelCheck(Player p, CheckEnums.HackType hackType, int ticks) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            PlayerProtocol protocol = PluginBase.getProtocol(p);
            protocol.getRunner(hackType).addDisableCause("Developer-API", null, ticks);
        }
    }

    public static void cancelCheckPerVerbose(Player p, String string, int ticks) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            PlayerProtocol protocol = PluginBase.getProtocol(p);
            for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                protocol.getRunner(hackType).addDisableCause("Developer-API", string, ticks);
            }
        }
    }

    public static void enableSilentChecking(CheckEnums.HackType HackType2) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            HackType2.getCheck().handleDataType(null, true);
        }
    }

    public static void disableSilentChecking(CheckEnums.HackType HackType2) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            HackType2.getCheck().handleDataType(null, false);
        }
    }

    public static void enableSilentChecking(Player p, CheckEnums.HackType hackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            PlayerProtocol protocol = PluginBase.getProtocol(p);
            protocol.getRunner(hackType).addSilentCause("Developer-API", null, 0);
        }
    }

    public static void disableSilentChecking(Player p, CheckEnums.HackType hackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            PlayerProtocol protocol = PluginBase.getProtocol(p);
            protocol.getRunner(hackType).removeSilentCause();
        }
    }

    public static void startCheck(Player p, CheckEnums.HackType hackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            PlayerProtocol protocol = PluginBase.getProtocol(p);
            protocol.getRunner(hackType).removeDisableCause();
        }
    }

    public static void stopCheck(Player p, CheckEnums.HackType hackType) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            PlayerProtocol protocol = PluginBase.getProtocol(p);
            protocol.getRunner(hackType).addDisableCause("Developer-API", null, 0);
        }
    }

    public static void resetVL() {
        Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
        if (!protocols.isEmpty()) {
            for (PlayerProtocol protocol : protocols) {
                for (CheckRunner runner : protocol.getRunners()) {
                    for (CheckDetection detection : runner.getDetections()) {
                        detection.clearData(protocol.getDataType());
                    }
                }
            }
        }
    }

    public static void resetVL(Player p) {
        PlayerProtocol protocol = PluginBase.getProtocol(p);
        for (CheckRunner runner : protocol.getRunners()) {
            for (CheckDetection detection : runner.getDetections()) {
                detection.clearData(protocol.getDataType());
            }
        }
    }

    public static boolean isBypassing(Player p) {
        return Permissions.isBypassing(p, null);
    }

    @Deprecated
    public static void banPlayer(UUID uuid, String reason) {
        AwarenessNotifications.forcefullySend("The API method 'banPlayer' has been removed.");
    }

    @Deprecated
    public static boolean isBanned(UUID uuid) {
        AwarenessNotifications.forcefullySend("The API method 'isBanned' has been removed.");
        return false;
    }

    @Deprecated
    public static void unbanPlayer(UUID uuid) {
        AwarenessNotifications.forcefullySend("The API method 'unbanPlayer' has been removed.");
    }

    @Deprecated
    public static String getBanReason(UUID uuid) {
        AwarenessNotifications.forcefullySend("The API method 'getBanReason' has been removed.");
        return null;
    }

    @Deprecated
    public static String getBanPunisher(UUID uuid) {
        AwarenessNotifications.forcefullySend("The API method 'getBanPunisher' has been removed.");
        return null;
    }

    @Deprecated
    public static boolean isHacker(Player p) {
        AwarenessNotifications.forcefullySend("The API method 'isHacker' has been removed.");
        return false;
    }

    @Deprecated
    public static boolean isLegitimate(Player p) {
        AwarenessNotifications.forcefullySend("The API method 'isLegitimate' has been removed.");
        return false;
    }

    @Deprecated
    public static boolean hasMiningNotificationsEnabled(Player p) {
        return BackgroundAPI.hasNotificationsEnabled(p);
    }

    @Deprecated
    public static void setMiningNotifications(Player p, boolean value) {
        BackgroundAPI.setNotifications(p, value);
    }

    @Deprecated
    public static int getCPS(Player p) {
        AwarenessNotifications.forcefullySend("The API method 'getCPS' has been removed.");
        return 0;
    }

    @Deprecated
    public static UUID[] getBanList() {
        AwarenessNotifications.forcefullySend("The API method 'getBanList' has been removed.");
        return new UUID[0];
    }

    public static boolean addToWave(UUID uuid, String command) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Wave.add(uuid, command);
            return true;
        }
        return false;
    }

    public static void removeFromWave(UUID uuid) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Wave.remove(uuid);
        }
    }

    public static void clearWave() {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Wave.clear();
        }
    }

    public static void runWave() {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            Wave.start();
        }
    }

    public static UUID[] getWaveList() {
        return Wave.getWaveList();
    }

    public static int getWaveSize() {
        return Wave.getWaveList().length;
    }

    public static boolean isAddedToTheWave(UUID uuid) {
        return Wave.getCommand(uuid) != null;
    }

    public static void warnPlayer(Player p, String reason) {
        AwarenessNotifications.forcefullySend("The API method 'warnPlayer' has been removed.");
    }

    @Deprecated
    public static void addPermission(Player p, Permission permission) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            AwarenessNotifications.forcefullySend("The API method 'addPermission' has been removed.");
        }
    }

    @Deprecated
    public static void sendClientSidedBlock(Player p, Location loc, Material m, byte b) {
        AwarenessNotifications.forcefullySend("The API method 'sendClientSidedBlock' has been removed.");
    }

    @Deprecated
    public static void destroyClientSidedBlock(Player p, Location loc) {
        AwarenessNotifications.forcefullySend("The API method 'destroyClientSidedBlock' has been removed.");
    }

    @Deprecated
    public static void removeClientSidedBlocks(Player p) {
        AwarenessNotifications.forcefullySend("The API method 'removeClientSidedBlocks' has been removed.");
    }

    @Deprecated
    public static boolean containsClientSidedBlock(Player p, Location loc) {
        AwarenessNotifications.forcefullySend("The API method 'containsClientSidedBlock' has been removed.");
        return false;
    }

    @Deprecated
    public static Material getClientSidedBlockMaterial(Player p, Location loc) {
        AwarenessNotifications.forcefullySend("The API method 'getClientSidedBlockMaterial' has been removed.");
        return null;
    }

    @Deprecated
    public static byte getClientSidedBlockData(Player p, Location loc) {
        AwarenessNotifications.forcefullySend("The API method 'getClientSidedBlockData' has been removed.");
        return 0;
    }

    public static String getConfiguredCheckName(CheckEnums.HackType hackType) {
        return hackType.getCheck().getName();
    }

    public static void setConfiguredCheckName(CheckEnums.HackType hackType, String name) {
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            hackType.getCheck().handleCheck(name);
        }
    }

    @Deprecated
    public static void disableVelocityProtection(Player p, int ticks) {
        AwarenessNotifications.forcefullySend("The API method 'disableVelocityProtection' has been removed.");
    }

    @Deprecated
    public static void setOnGround(Player p, int ticks) {
        AwarenessNotifications.forcefullySend("The API method 'setOnGround' has been removed.");
    }

    @Deprecated
    public static int getMaxPunishmentViolation(CheckEnums.HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getMaxPunishmentViolation' has been removed.");
        return 0;
    }

    @Deprecated
    public static int getMinPunishmentViolation(CheckEnums.HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'getMinPunishmentViolation' has been removed.");
        return 0;
    }

    @Deprecated
    public static boolean mayPunishPlayer(Player p, CheckEnums.HackType hackType) {
        AwarenessNotifications.forcefullySend("The API method 'mayPunishPlayer' has been removed.");
        return false;
    }
}

