/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.api;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.api.BackgroundAPI;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.User;
import ai.idealistic.spartan.functionality.moderation.Wave;
import ai.idealistic.spartan.functionality.server.Permissions;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ApiUtils {
    public static String licenseID() {
        return User.user();
    }

    public static String getMessage(String path) {
        return BackgroundAPI.getMessage(path);
    }

    public static boolean getSetting(String path) {
        return BackgroundAPI.getSetting(path);
    }

    @Deprecated
    public static String getCategory(Player p, CheckEnums.HackType hackType) {
        return BackgroundAPI.getCategory(p, hackType);
    }

    public static boolean hasVerboseEnabled(Player p) {
        return BackgroundAPI.hasVerboseEnabled(p);
    }

    public static boolean hasNotificationsEnabled(Player p) {
        return BackgroundAPI.hasNotificationsEnabled(p);
    }

    @Deprecated
    public static int getViolationResetTime() {
        return BackgroundAPI.getViolationResetTime();
    }

    public static void setVerbose(Player p, boolean value) {
        BackgroundAPI.setVerbose(p, value);
    }

    public static void setNotifications(Player p, boolean value) {
        BackgroundAPI.setNotifications(p, value);
    }

    @Deprecated
    public static void setVerbose(Player p, boolean value, int frequency) {
        BackgroundAPI.setVerbose(p, value, frequency);
    }

    public static void setNotifications(Player p, int frequency) {
        BackgroundAPI.setNotifications(p, frequency);
    }

    public static int getPing(Player p) {
        return BackgroundAPI.getPing(p);
    }

    @Deprecated
    public static double getTPS() {
        return BackgroundAPI.getTPS();
    }

    public static boolean hasPermission(Player p, Permission Permission2) {
        return Permissions.has(p, Permission2);
    }

    public static boolean isEnabled(CheckEnums.HackType HackType2) {
        return BackgroundAPI.isEnabled(HackType2);
    }

    public static boolean isSilent(CheckEnums.HackType HackType2) {
        return BackgroundAPI.isSilent(HackType2);
    }

    public static int getVL(Player p, CheckEnums.HackType HackType2) {
        return BackgroundAPI.getVL(p, HackType2);
    }

    public static double getCertainty(Player p, CheckEnums.HackType HackType2) {
        return BackgroundAPI.getCertainty(p, HackType2);
    }

    public static double getDecimalVL(Player p, CheckEnums.HackType hackType) {
        return BackgroundAPI.getDecimalVL(p, hackType);
    }

    public static int getVL(Player p) {
        return BackgroundAPI.getVL(p);
    }

    public static double getDecimalVL(Player p) {
        return BackgroundAPI.getDecimalVL(p);
    }

    @Deprecated
    public static void setVL(Player p, CheckEnums.HackType HackType2, int amount) {
        BackgroundAPI.setVL(p, HackType2, amount);
    }

    @Deprecated
    public static int getCancelViolation(CheckEnums.HackType hackType, String worldName) {
        return BackgroundAPI.getCancelViolation(hackType, worldName);
    }

    @Deprecated
    public static int getCancelViolation(CheckEnums.HackType hackType) {
        return BackgroundAPI.getCancelViolation(hackType);
    }

    @Deprecated
    public static int getViolationDivisor(Player p, CheckEnums.HackType hackType) {
        return BackgroundAPI.getViolationDivisor(p, hackType);
    }

    public static void reloadConfig() {
        BackgroundAPI.reloadConfig();
    }

    @Deprecated
    public static void reloadPermissions() {
        BackgroundAPI.reloadPermissions();
    }

    @Deprecated
    public static void reloadPermissions(Player p) {
        BackgroundAPI.reloadPermissions(p);
    }

    public static void enableCheck(CheckEnums.HackType HackType2) {
        BackgroundAPI.enableCheck(HackType2);
    }

    public static void disableCheck(CheckEnums.HackType HackType2) {
        BackgroundAPI.disableCheck(HackType2);
    }

    public static void enableSilentChecking(Player p, CheckEnums.HackType HackType2) {
        BackgroundAPI.enableSilentChecking(p, HackType2);
    }

    public static void disableSilentChecking(Player p, CheckEnums.HackType HackType2) {
        BackgroundAPI.disableSilentChecking(p, HackType2);
    }

    public static void enableSilentChecking(CheckEnums.HackType HackType2) {
        BackgroundAPI.enableSilentChecking(HackType2);
    }

    public static void disableSilentChecking(CheckEnums.HackType HackType2) {
        BackgroundAPI.disableSilentChecking(HackType2);
    }

    public static void cancelCheck(Player p, CheckEnums.HackType HackType2, int ticks) {
        BackgroundAPI.cancelCheck(p, HackType2, ticks);
    }

    public static void cancelCheckPerVerbose(Player p, String string, int ticks) {
        BackgroundAPI.cancelCheckPerVerbose(p, string, ticks);
    }

    public static void startCheck(Player p, CheckEnums.HackType HackType2) {
        BackgroundAPI.startCheck(p, HackType2);
    }

    public static void stopCheck(Player p, CheckEnums.HackType HackType2) {
        BackgroundAPI.stopCheck(p, HackType2);
    }

    public static void resetVL() {
        BackgroundAPI.resetVL();
    }

    public static void resetVL(Player p) {
        BackgroundAPI.resetVL(p);
    }

    public static boolean isBypassing(Player p) {
        return BackgroundAPI.isBypassing(p);
    }

    public static boolean isBypassing(Player p, CheckEnums.HackType HackType2) {
        return Permissions.isBypassing(p, HackType2);
    }

    @Deprecated
    public static void banPlayer(UUID uuid, String reason) {
        BackgroundAPI.banPlayer(uuid, reason);
    }

    @Deprecated
    public static boolean isBanned(UUID uuid) {
        return BackgroundAPI.isBanned(uuid);
    }

    @Deprecated
    public static void unbanPlayer(UUID uuid) {
        BackgroundAPI.unbanPlayer(uuid);
    }

    @Deprecated
    public static String getBanReason(UUID uuid) {
        return BackgroundAPI.getBanReason(uuid);
    }

    @Deprecated
    public static String getBanPunisher(UUID uuid) {
        return BackgroundAPI.getBanPunisher(uuid);
    }

    @Deprecated
    public static boolean isHacker(Player p) {
        return BackgroundAPI.isHacker(p);
    }

    @Deprecated
    public static boolean isLegitimate(Player p) {
        return BackgroundAPI.isLegitimate(p);
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
        return BackgroundAPI.getCPS(p);
    }

    @Deprecated
    public static UUID[] getBanList() {
        return BackgroundAPI.getBanList();
    }

    public static boolean addToWave(UUID uuid, String command) {
        return BackgroundAPI.addToWave(uuid, command);
    }

    public static void removeFromWave(UUID uuid) {
        BackgroundAPI.removeFromWave(uuid);
    }

    public static void clearWave() {
        BackgroundAPI.clearWave();
    }

    public static void runWave() {
        BackgroundAPI.runWave();
    }

    public static UUID[] getWaveList() {
        return Wave.getWaveList();
    }

    public static int getWaveSize() {
        return BackgroundAPI.getWaveSize();
    }

    public static boolean isAddedToTheWave(UUID uuid) {
        return BackgroundAPI.isAddedToTheWave(uuid);
    }

    @Deprecated
    public static void warnPlayer(Player p, String reason) {
        BackgroundAPI.warnPlayer(p, reason);
    }

    @Deprecated
    public static void addPermission(Player p, Permission permission) {
        BackgroundAPI.addPermission(p, permission);
    }

    @Deprecated
    public static void sendClientSidedBlock(Player p, Location loc, Material m, byte b) {
        BackgroundAPI.sendClientSidedBlock(p, loc, m, b);
    }

    @Deprecated
    public static void destroyClientSidedBlock(Player p, Location loc) {
        BackgroundAPI.destroyClientSidedBlock(p, loc);
    }

    @Deprecated
    public static void removeClientSidedBlocks(Player p) {
        BackgroundAPI.removeClientSidedBlocks(p);
    }

    @Deprecated
    public static boolean containsClientSidedBlock(Player p, Location loc) {
        return BackgroundAPI.containsClientSidedBlock(p, loc);
    }

    @Deprecated
    public static Material getClientSidedBlockMaterial(Player p, Location loc) {
        return BackgroundAPI.getClientSidedBlockMaterial(p, loc);
    }

    @Deprecated
    public static byte getClientSidedBlockData(Player p, Location loc) {
        return BackgroundAPI.getClientSidedBlockData(p, loc);
    }

    @Deprecated
    public static void disableVelocityProtection(Player p, int ticks) {
        BackgroundAPI.disableVelocityProtection(p, ticks);
    }

    public static String getConfiguredCheckName(CheckEnums.HackType hackType) {
        return BackgroundAPI.getConfiguredCheckName(hackType);
    }

    public static void setConfiguredCheckName(CheckEnums.HackType hackType, String name) {
        BackgroundAPI.setConfiguredCheckName(hackType, name);
    }

    @Deprecated
    public static void setOnGround(Player p, int ticks) {
        BackgroundAPI.setOnGround(p, ticks);
    }

    @Deprecated
    public static int getMaxPunishmentViolation(CheckEnums.HackType hackType) {
        return BackgroundAPI.getMaxPunishmentViolation(hackType);
    }

    @Deprecated
    public static int getMinPunishmentViolation(CheckEnums.HackType hackType) {
        return BackgroundAPI.getMinPunishmentViolation(hackType);
    }

    @Deprecated
    public static boolean mayPunishPlayer(Player p, CheckEnums.HackType hackType) {
        return BackgroundAPI.mayPunishPlayer(p, hackType);
    }
}

