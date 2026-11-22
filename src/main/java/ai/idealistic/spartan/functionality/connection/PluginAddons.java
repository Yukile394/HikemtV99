/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package ai.idealistic.spartan.functionality.connection;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.functionality.connection.CloudConnections;
import ai.idealistic.spartan.functionality.connection.User;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import ai.idealistic.spartan.utils.math.Distance;
import java.io.File;
import org.bukkit.Bukkit;

public class PluginAddons {
    public static final String pluginURL = "https://spartan.top";
    private static final boolean[] ownedChecks = new boolean[CheckEnums.HackType.values().length];
    private static final boolean[] ownsEditions = new boolean[Check.DataType.values().length];
    private static boolean freeEdition = true;
    private static boolean syn = true;
    public static final String synCommand = "spartan syn";
    public static final String synName = "Spartan Syn";
    public static final String synURL = "https://spartan.top/syn";
    public static final String synMissingPlaceholder = "{feature}";
    public static final String synClick = "\u00a7aClick to visit https://spartan.top/syn.";
    public static final String synMissingCustom = "You must purchase Spartan Syn to use {feature}.\nVisit https://spartan.top/syn for more information.";
    public static final String synMissingDefault = "You must purchase Spartan Syn to use {feature}.\nVisit https://spartan.top/syn for more information.".replace("{feature}", "this feature");

    public static boolean refresh() {
        if (!User.enabled) {
            return PluginAddons.judgeFromCloud();
        }
        return false;
    }

    private static boolean judgeFromCloud() {
        int n;
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            PluginAddons.ownedChecks[hackType.ordinal()] = true;
        }
        if (Bukkit.getMotd().contains("Spartan")) {
            freeEdition = false;
            syn = true;
            for (Enum enum_ : Check.DataType.values()) {
                PluginAddons.ownsEditions[enum_.ordinal()] = true;
            }
            return true;
        }
        String[] editions = CloudConnections.getOwnedEditions();
        if (editions == null) {
            int var3_12 = 0;
            freeEdition = true;
            Check.DataType[] dataTypeArray = Check.DataType.values();
            n = dataTypeArray.length;
            while (var3_12 < n) {
                Check.DataType dataType = dataTypeArray[var3_12];
                PluginAddons.ownsEditions[dataType.ordinal()] = true;
                ++var3_12;
            }
        } else {
            int var3_16 = 0;
            int var3_14 = 0;
            Check.DataType[] dataTypeArray = Check.DataType.values();
            n = dataTypeArray.length;
            while (var3_14 < n) {
                Check.DataType dataType = dataTypeArray[var3_14];
                PluginAddons.ownsEditions[dataType.ordinal()] = false;
                ++var3_14;
            }
            n = editions.length;
            while (var3_16 < n) {
                String string = editions[var3_16];
                for (Check.DataType dataType : Check.DataType.values()) {
                    if (!string.equals(dataType.toString())) continue;
                    PluginAddons.ownsEditions[dataType.ordinal()] = true;
                    freeEdition = false;
                    break;
                }
                ++var3_16;
            }
            if (freeEdition) {
                int var3_18 = 0;
                Check.DataType[] dataTypeArray2 = Check.DataType.values();
                n = dataTypeArray2.length;
                while (var3_18 < n) {
                    Check.DataType dataType = dataTypeArray2[var3_18];
                    PluginAddons.ownsEditions[dataType.ordinal()] = true;
                    ++var3_18;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean ownsCheck(CheckEnums.HackType hackType) {
        return ownedChecks[hackType.ordinal()];
    }

    public static boolean ownsEdition(Check.DataType dataType) {
        return ownsEditions[dataType.ordinal()];
    }

    public static boolean isSyn() {
        return syn || freeEdition;
    }

    private static /* synthetic */ void lambda$refresh$3() {
        String pluginsFolder = Register.plugin.getDataFolder().toString().replace("Spartan", "");
        for (File file : new File(pluginsFolder).listFiles()) {
            if (!file.isFile() || !file.getName().endsWith(".jar")) continue;
            String clazz = User.class.getCanonicalName();
            Object object = ReflectionUtils.getFieldFromJar(file.getAbsolutePath(), clazz, "user");
            if (object == null || !object.toString().equals("644411")) continue;
            object = ReflectionUtils.getFieldFromJar(file.getAbsolutePath(), clazz, "resource");
            if (object == null || object.toString().equals("11196")) break;
            syn = true;
            break;
        }
    }

    private static /* synthetic */ void lambda$refresh$2(String java, String bedrock) {
        String pluginsFolder = Register.plugin.getDataFolder().toString().replace("Spartan", "");
        File[] files = new File(pluginsFolder).listFiles();
        if (files != null) {
            String clazz = User.class.getCanonicalName();
            for (File file : files) {
                String path;
                Object object;
                if (!file.isFile() || !file.getName().endsWith(".jar") || (object = ReflectionUtils.getFieldFromJar(path = file.getAbsolutePath(), clazz, "user")) == null || !object.toString().equals("644411")) continue;
                object = ReflectionUtils.getFieldFromJar(path, clazz, "resource");
                if (object == null || object.toString().equals("11196")) break;
                if (object.toString().equals(java)) {
                    PluginAddons.ownsEditions[Check.DataType.JAVA.ordinal()] = true;
                    break;
                }
                if (!object.toString().equals(bedrock)) break;
                PluginAddons.ownsEditions[Check.DataType.BEDROCK.ordinal()] = true;
                break;
            }
        }
    }

    private static /* synthetic */ void lambda$refresh$1(String combat, String movement, String world) {
        String pluginsFolder = Register.plugin.getDataFolder().toString().replace("Spartan", "");
        for (File file : new File(pluginsFolder).listFiles()) {
            if (!file.isFile() || !file.getName().endsWith(".jar")) continue;
            String clazz = User.class.getCanonicalName();
            Object object = ReflectionUtils.getFieldFromJar(file.getAbsolutePath(), clazz, "user");
            if (object == null || !object.toString().equals("644411")) continue;
            object = ReflectionUtils.getFieldFromJar(file.getAbsolutePath(), clazz, "resource");
            if (object == null || object.toString().equals("11196")) break;
            if (object.toString().equals(combat)) {
                for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                    if (hackType.category != CheckEnums.HackCategoryType.COMBAT) continue;
                    PluginAddons.ownedChecks[hackType.ordinal()] = true;
                }
            } else if (object.toString().equals(movement)) {
                for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                    if (hackType.category != CheckEnums.HackCategoryType.MOVEMENT) continue;
                    PluginAddons.ownedChecks[hackType.ordinal()] = true;
                }
            } else {
                if (!object.toString().equals(world)) break;
                for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                    if (hackType.category != CheckEnums.HackCategoryType.WORLD) continue;
                    PluginAddons.ownedChecks[hackType.ordinal()] = true;
                }
            }
            break;
        }
    }

    private static /* synthetic */ void lambda$refresh$0() {
        File directory = new File(Register.plugin.getDataFolder() + "/addons");
        if (directory.isFile()) {
            directory.delete();
        }
        directory.mkdirs();
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                if (ownedChecks[hackType.ordinal()]) continue;
                for (File file : files) {
                    Object object;
                    if (!file.isFile() || !file.getName().endsWith(".jar") || (object = ReflectionUtils.getFieldFromJar(file.getAbsolutePath(), "ai.idealistic.spartan.addon." + hackType.name(), "user")) == null || !object.toString().equals("644411") && Distance.validInteger(object.toString())) continue;
                    PluginAddons.ownedChecks[hackType.ordinal()] = true;
                    continue block0;
                }
            }
        }
    }
}

