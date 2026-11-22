/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Event
 */
package ai.idealistic.spartan.functionality.server;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.configuration.ConfigurationBuilder;
import ai.idealistic.spartan.abstraction.configuration.implementation.Advanced;
import ai.idealistic.spartan.abstraction.configuration.implementation.Messages;
import ai.idealistic.spartan.abstraction.configuration.implementation.Sql;
import ai.idealistic.spartan.abstraction.configuration.implementation.Settings;
import ai.idealistic.spartan.api.SpartanReloadEvent;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.moderation.Wave;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

public class Config {
    public static final Settings settings = new Settings();
    public static final Sql sql = new Sql();
    public static final Messages messages = new Messages();
    public static final Advanced advanced = new Advanced();
    public static final Compatibility compatibility = new Compatibility();
    public static final ConfigurationBuilder[] configurations = new ConfigurationBuilder[]{settings, sql, messages, advanced};

    public static Check getCheckByName(String s) {
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            Check check = hackType.getCheck();
            String checkName = check.getName();
            if (checkName == null || !checkName.equals(s)) continue;
            return check;
        }
        return null;
    }

    public static void create() {
        boolean enabledPlugin = Register.isPluginEnabled();
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            hackType.resetCheck();
        }
        if (enabledPlugin) {
            for (ConfigurationBuilder configurationBuilder : configurations) {
                configurationBuilder.create();
            }
            Compatibility.create();
            Wave.create();
            AwarenessNotifications.refresh();
        } else {
            for (ConfigurationBuilder configurationBuilder : configurations) {
                configurationBuilder.clear();
            }
            compatibility.clearCache();
            Wave.clearCache();
            AwarenessNotifications.clear();
        }
        ResearchEngine.refresh(enabledPlugin);
    }

    public static void reload(CommandSender sender) {
        if (settings.getBoolean("Important.enable_developer_api")) {
            SpartanReloadEvent event = new SpartanReloadEvent();
            Bukkit.getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                return;
            }
        }
        if (sender != null) {
            sender.sendMessage(messages.getColorfulString("config_reload"));
        }
        Config.create();
    }

    public static void enableChecks() {
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            hackType.getCheck().handleCheck((Check.DataType)null, true);
        }
    }

    public static void disableChecks() {
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            hackType.getCheck().handleCheck((Check.DataType)null, false);
        }
    }

    public static void enableSilentChecking() {
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            hackType.getCheck().handleDataType(null, true);
        }
    }

    public static void disableSilentChecking() {
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            hackType.getCheck().handleDataType(null, false);
        }
    }

    public static boolean isEnabled(Check.DataType dataType) {
        for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
            if (!hackType.getCheck().handleCheck(dataType, null)) continue;
            return true;
        }
        return false;
    }
}

