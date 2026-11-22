/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package me.vagdedes.spartan.api;

import java.util.UUID;
import me.vagdedes.spartan.api.BackgroundAPI;
import me.vagdedes.spartan.api.system.Enums;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class API {
    public static String licenseID() {
        return ai.idealistic.spartan.api.BackgroundAPI.licenseID();
    }

    public static String getVersion() {
        return ai.idealistic.spartan.api.BackgroundAPI.getVersion();
    }

    public static String getMessage(String path) {
        return ai.idealistic.spartan.api.BackgroundAPI.getMessage(path);
    }

    public static boolean getSetting(String path) {
        return ai.idealistic.spartan.api.BackgroundAPI.getSetting(path);
    }

    @Deprecated
    public static String getCategory(Player p, Enums.HackType hackType) {
        return BackgroundAPI.getCategory(p, hackType);
    }

    public static boolean hasVerboseEnabled(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.hasVerboseEnabled(p);
    }

    public static boolean hasNotificationsEnabled(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.hasNotificationsEnabled(p);
    }

    @Deprecated
    public static int getViolationResetTime() {
        return ai.idealistic.spartan.api.BackgroundAPI.getViolationResetTime();
    }

    public static void setVerbose(Player p, boolean value) {
        ai.idealistic.spartan.api.BackgroundAPI.setVerbose(p, value);
    }

    public static void setNotifications(Player p, boolean value) {
        ai.idealistic.spartan.api.BackgroundAPI.setNotifications(p, value);
    }

    @Deprecated
    public static void setVerbose(Player p, boolean value, int frequency) {
        ai.idealistic.spartan.api.BackgroundAPI.setVerbose(p, value, frequency);
    }

    public static void setNotifications(Player p, int frequency) {
        ai.idealistic.spartan.api.BackgroundAPI.setNotifications(p, frequency);
    }

    public static int getPing(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.getPing(p);
    }

    @Deprecated
    public static double getTPS() {
        return ai.idealistic.spartan.api.BackgroundAPI.getTPS();
    }

    public static boolean hasPermission(Player p, Enums.Permission Permission2) {
        return BackgroundAPI.hasPermission(p, Permission2);
    }

    public static boolean isEnabled(Enums.HackType HackType2) {
        return BackgroundAPI.isEnabled(HackType2);
    }

    public static boolean isSilent(Enums.HackType HackType2) {
        return BackgroundAPI.isSilent(HackType2);
    }

    public static int getVL(Player p, Enums.HackType HackType2) {
        return BackgroundAPI.getVL(p, HackType2);
    }

    public static double getCertainty(Player p, Enums.HackType HackType2) {
        return BackgroundAPI.getCertainty(p, HackType2);
    }

    public static double getDecimalVL(Player p, Enums.HackType hackType) {
        return BackgroundAPI.getDecimalVL(p, hackType);
    }

    public static int getVL(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.getVL(p);
    }

    public static double getDecimalVL(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.getDecimalVL(p);
    }

    @Deprecated
    public static void setVL(Player p, Enums.HackType HackType2, int amount) {
        BackgroundAPI.setVL(p, HackType2, amount);
    }

    @Deprecated
    public static int getCancelViolation(Enums.HackType hackType, String worldName) {
        return BackgroundAPI.getCancelViolation(hackType, worldName);
    }

    @Deprecated
    public static int getCancelViolation(Enums.HackType hackType) {
        return BackgroundAPI.getCancelViolation(hackType);
    }

    @Deprecated
    public static int getViolationDivisor(Player p, Enums.HackType hackType) {
        return BackgroundAPI.getViolationDivisor(p, hackType);
    }

    public static void reloadConfig() {
        ai.idealistic.spartan.api.BackgroundAPI.reloadConfig();
    }

    @Deprecated
    public static void reloadPermissions() {
        ai.idealistic.spartan.api.BackgroundAPI.reloadPermissions();
    }

    @Deprecated
    public static void reloadPermissions(Player p) {
        ai.idealistic.spartan.api.BackgroundAPI.reloadPermissions(p);
    }

    public static void enableCheck(Enums.HackType HackType2) {
        BackgroundAPI.enableCheck(HackType2);
    }

    public static void disableCheck(Enums.HackType HackType2) {
        BackgroundAPI.disableCheck(HackType2);
    }

    public static void enableSilentChecking(Player p, Enums.HackType HackType2) {
        BackgroundAPI.enableSilentChecking(p, HackType2);
    }

    public static void disableSilentChecking(Player p, Enums.HackType HackType2) {
        BackgroundAPI.disableSilentChecking(p, HackType2);
    }

    public static void enableSilentChecking(Enums.HackType HackType2) {
        BackgroundAPI.enableSilentChecking(HackType2);
    }

    public static void disableSilentChecking(Enums.HackType HackType2) {
        BackgroundAPI.disableSilentChecking(HackType2);
    }

    public static void cancelCheck(Player p, Enums.HackType HackType2, int ticks) {
        BackgroundAPI.cancelCheck(p, HackType2, ticks);
    }

    public static void cancelCheckPerVerbose(Player p, String string, int ticks) {
        ai.idealistic.spartan.api.BackgroundAPI.cancelCheckPerVerbose(p, string, ticks);
    }

    public static void startCheck(Player p, Enums.HackType HackType2) {
        BackgroundAPI.startCheck(p, HackType2);
    }

    public static void stopCheck(Player p, Enums.HackType HackType2) {
        BackgroundAPI.stopCheck(p, HackType2);
    }

    public static void resetVL() {
        ai.idealistic.spartan.api.BackgroundAPI.resetVL();
    }

    public static void resetVL(Player p) {
        ai.idealistic.spartan.api.BackgroundAPI.resetVL(p);
    }

    public static boolean isBypassing(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.isBypassing(p);
    }

    public static boolean isBypassing(Player p, Enums.HackType HackType2) {
        return BackgroundAPI.isBypassing(p, HackType2);
    }

    @Deprecated
    public static void banPlayer(UUID uuid, String reason) {
        ai.idealistic.spartan.api.BackgroundAPI.banPlayer(uuid, reason);
    }

    @Deprecated
    public static boolean isBanned(UUID uuid) {
        return ai.idealistic.spartan.api.BackgroundAPI.isBanned(uuid);
    }

    @Deprecated
    public static void unbanPlayer(UUID uuid) {
        ai.idealistic.spartan.api.BackgroundAPI.unbanPlayer(uuid);
    }

    @Deprecated
    public static String getBanReason(UUID uuid) {
        return ai.idealistic.spartan.api.BackgroundAPI.getBanReason(uuid);
    }

    @Deprecated
    public static String getBanPunisher(UUID uuid) {
        return ai.idealistic.spartan.api.BackgroundAPI.getBanPunisher(uuid);
    }

    @Deprecated
    public static boolean isHacker(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.isHacker(p);
    }

    @Deprecated
    public static boolean isLegitimate(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.isLegitimate(p);
    }

    @Deprecated
    public static boolean hasMiningNotificationsEnabled(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.hasMiningNotificationsEnabled(p);
    }

    @Deprecated
    public static void setMiningNotifications(Player p, boolean value) {
        ai.idealistic.spartan.api.BackgroundAPI.setMiningNotifications(p, value);
    }

    @Deprecated
    public static int getCPS(Player p) {
        return ai.idealistic.spartan.api.BackgroundAPI.getCPS(p);
    }

    @Deprecated
    public static UUID[] getBanList() {
        return ai.idealistic.spartan.api.BackgroundAPI.getBanList();
    }

    public static boolean addToWave(UUID uuid, String command) {
        return ai.idealistic.spartan.api.BackgroundAPI.addToWave(uuid, command);
    }

    public static void removeFromWave(UUID uuid) {
        ai.idealistic.spartan.api.BackgroundAPI.removeFromWave(uuid);
    }

    public static void clearWave() {
        ai.idealistic.spartan.api.BackgroundAPI.clearWave();
    }

    public static void runWave() {
        ai.idealistic.spartan.api.BackgroundAPI.runWave();
    }

    public static UUID[] getWaveList() {
        return ai.idealistic.spartan.api.BackgroundAPI.getWaveList();
    }

    public static int getWaveSize() {
        return ai.idealistic.spartan.api.BackgroundAPI.getWaveSize();
    }

    public static boolean isAddedToTheWave(UUID uuid) {
        return ai.idealistic.spartan.api.BackgroundAPI.isAddedToTheWave(uuid);
    }

    @Deprecated
    public static void warnPlayer(Player p, String reason) {
        ai.idealistic.spartan.api.BackgroundAPI.warnPlayer(p, reason);
    }

    @Deprecated
    public static void addPermission(Player p, Enums.Permission permission) {
        BackgroundAPI.addPermission(p, permission);
    }

    @Deprecated
    public static void sendClientSidedBlock(Player p, Location loc, Material m, byte b) {
        ai.idealistic.spartan.api.BackgroundAPI.sendClientSidedBlock(p, loc, m, b);
    }

    @Deprecated
    public static void destroyClientSidedBlock(Player p, Location loc) {
        ai.idealistic.spartan.api.BackgroundAPI.destroyClientSidedBlock(p, loc);
    }

    @Deprecated
    public static void removeClientSidedBlocks(Player p) {
        ai.idealistic.spartan.api.BackgroundAPI.removeClientSidedBlocks(p);
    }

    @Deprecated
    public static boolean containsClientSidedBlock(Player p, Location loc) {
        return ai.idealistic.spartan.api.BackgroundAPI.containsClientSidedBlock(p, loc);
    }

    @Deprecated
    public static Material getClientSidedBlockMaterial(Player p, Location loc) {
        return ai.idealistic.spartan.api.BackgroundAPI.getClientSidedBlockMaterial(p, loc);
    }

    @Deprecated
    public static byte getClientSidedBlockData(Player p, Location loc) {
        return ai.idealistic.spartan.api.BackgroundAPI.getClientSidedBlockData(p, loc);
    }

    @Deprecated
    public static void disableVelocityProtection(Player p, int ticks) {
        ai.idealistic.spartan.api.BackgroundAPI.disableVelocityProtection(p, ticks);
    }

    public static String getConfiguredCheckName(Enums.HackType hackType) {
        return BackgroundAPI.getConfiguredCheckName(hackType);
    }

    public static void setConfiguredCheckName(Enums.HackType hackType, String name) {
        BackgroundAPI.setConfiguredCheckName(hackType, name);
    }

    @Deprecated
    public static void setOnGround(Player p, int ticks) {
        ai.idealistic.spartan.api.BackgroundAPI.setOnGround(p, ticks);
    }

    @Deprecated
    public static int getMaxPunishmentViolation(Enums.HackType hackType) {
        return BackgroundAPI.getMaxPunishmentViolation(hackType);
    }

    @Deprecated
    public static int getMinPunishmentViolation(Enums.HackType hackType) {
        return BackgroundAPI.getMinPunishmentViolation(hackType);
    }

    @Deprecated
    public static boolean mayPunishPlayer(Player p, Enums.HackType hackType) {
        return BackgroundAPI.mayPunishPlayer(p, hackType);
    }
}

