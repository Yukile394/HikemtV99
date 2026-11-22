/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.vagdedes.spartan.api;

import ai.idealistic.spartan.functionality.server.Permissions;
import me.vagdedes.spartan.api.system.Enums;
import org.bukkit.entity.Player;

public class BackgroundAPI {
    @Deprecated
    static String getCategory(Player p, Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.getCategory(p, hackType.getHackType());
    }

    static boolean hasPermission(Player p, Enums.Permission Permission2) {
        return Permissions.has(p, Permission2.getPermission());
    }

    static boolean isEnabled(Enums.HackType HackType2) {
        return ai.idealistic.spartan.api.BackgroundAPI.isEnabled(HackType2.getHackType());
    }

    static boolean isSilent(Enums.HackType HackType2) {
        return ai.idealistic.spartan.api.BackgroundAPI.isSilent(HackType2.getHackType());
    }

    static int getVL(Player p, Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.getVL(p, hackType.getHackType());
    }

    static double getCertainty(Player p, Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.getCertainty(p, hackType.getHackType());
    }

    static double getDecimalVL(Player p, Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.getDecimalVL(p, hackType.getHackType());
    }

    @Deprecated
    static void setVL(Player p, Enums.HackType HackType2, int amount) {
        ai.idealistic.spartan.api.BackgroundAPI.setVL(p, HackType2.getHackType(), amount);
    }

    @Deprecated
    static int getCancelViolation(Enums.HackType hackType, String worldName) {
        return ai.idealistic.spartan.api.BackgroundAPI.getCancelViolation(hackType.getHackType(), worldName);
    }

    @Deprecated
    static int getCancelViolation(Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.getCancelViolation(hackType.getHackType());
    }

    @Deprecated
    static int getViolationDivisor(Player p, Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.getViolationDivisor(p, hackType.getHackType());
    }

    static void enableCheck(Enums.HackType HackType2) {
        ai.idealistic.spartan.api.BackgroundAPI.enableCheck(HackType2.getHackType());
    }

    static void disableCheck(Enums.HackType HackType2) {
        ai.idealistic.spartan.api.BackgroundAPI.disableCheck(HackType2.getHackType());
    }

    static void cancelCheck(Player p, Enums.HackType hackType, int ticks) {
        ai.idealistic.spartan.api.BackgroundAPI.cancelCheck(p, hackType.getHackType(), ticks);
    }

    static void enableSilentChecking(Enums.HackType HackType2) {
        ai.idealistic.spartan.api.BackgroundAPI.enableSilentChecking(HackType2.getHackType());
    }

    static void disableSilentChecking(Enums.HackType HackType2) {
        ai.idealistic.spartan.api.BackgroundAPI.disableSilentChecking(HackType2.getHackType());
    }

    static void enableSilentChecking(Player p, Enums.HackType hackType) {
        ai.idealistic.spartan.api.BackgroundAPI.enableSilentChecking(p, hackType.getHackType());
    }

    static void disableSilentChecking(Player p, Enums.HackType hackType) {
        ai.idealistic.spartan.api.BackgroundAPI.disableSilentChecking(p, hackType.getHackType());
    }

    static void startCheck(Player p, Enums.HackType hackType) {
        ai.idealistic.spartan.api.BackgroundAPI.startCheck(p, hackType.getHackType());
    }

    static void stopCheck(Player p, Enums.HackType hackType) {
        ai.idealistic.spartan.api.BackgroundAPI.stopCheck(p, hackType.getHackType());
    }

    static boolean isBypassing(Player p, Enums.HackType HackType2) {
        return Permissions.isBypassing(p, HackType2.getHackType());
    }

    @Deprecated
    static void addPermission(Player p, Enums.Permission permission) {
        ai.idealistic.spartan.api.BackgroundAPI.addPermission(p, permission.getPermission());
    }

    static String getConfiguredCheckName(Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.getConfiguredCheckName(hackType.getHackType());
    }

    static void setConfiguredCheckName(Enums.HackType hackType, String name) {
        ai.idealistic.spartan.api.BackgroundAPI.setConfiguredCheckName(hackType.getHackType(), name);
    }

    @Deprecated
    static int getMaxPunishmentViolation(Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.getMaxPunishmentViolation(hackType.getHackType());
    }

    @Deprecated
    static int getMinPunishmentViolation(Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.getMinPunishmentViolation(hackType.getHackType());
    }

    @Deprecated
    static boolean mayPunishPlayer(Player p, Enums.HackType hackType) {
        return ai.idealistic.spartan.api.BackgroundAPI.mayPunishPlayer(p, hackType.getHackType());
    }
}

