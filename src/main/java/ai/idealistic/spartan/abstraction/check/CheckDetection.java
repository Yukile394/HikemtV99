/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Event
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.check;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckCancellation;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckPrevention;
import ai.idealistic.spartan.abstraction.check.CheckProcess;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.BackgroundAPI;
import ai.idealistic.spartan.api.CheckCancelEvent;
import ai.idealistic.spartan.api.PlayerViolationCommandEvent;
import ai.idealistic.spartan.api.PlayerViolationEvent;
import ai.idealistic.spartan.functionality.connection.CloudConnections;
import ai.idealistic.spartan.functionality.moderation.DetectionNotifications;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.AntiCheatLogs;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import ai.idealistic.spartan.utils.math.Distance;
import ai.idealistic.spartan.utils.minecraft.server.ConfigUtils;
import java.lang.reflect.Method;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.util.NumberConversions;

public abstract class CheckDetection
extends CheckProcess {
    private static long lastPrevention = 0L;
    private static Method tpsMethod = null;
    public static final String javaPlayerIdentifier = "Java:";
    public static final String checkIdentifier = "Check:";
    public static final String detectionIdentifier = "Detection:";
    public static final String levelIdentifier = "Level:";
    public static final String notificationLevelIdentifier = "Notification-Level:";
    public static final String preventionLevelIdentifier = "Prevention-Level:";
    public static final String punishmentLevelIdentifier = "Punishment-Level:";
    public static final long DEFAULT_AVERAGE_TIME = 500L;
    public static final long TIME_TO_PUNISH = 60000L;
    public static final long TIME_TO_PREVENT = 40000L;
    public static final long TIME_TO_NOTIFY = 30000L;
    CheckPrevention prevention;
    public final CheckRunner executor;
    public final String name;
    public final boolean hasName;
    private final Boolean def;
    public final Check.DataType forcedDataType;
    public final Check.DetectionType detectionType;
    public final long defaultAverageTime;
    public final long timeToNotify;
    public final long timeToPrevent;
    public final long timeToPunish;
    private final long[] accumulatedTime;
    private final long[] previousTime;
    private long notifications;

    private static void setLastPrevention() {
        lastPrevention = System.currentTimeMillis();
    }

    public static long getLastPrevention() {
        return System.currentTimeMillis() - lastPrevention;
    }

    public static int generalHashCode(CheckEnums.HackType hackType, String name) {
        return hackType.hashCode() * 31 + (name == null ? 0 : name.hashCode());
    }

    public CheckDetection(CheckRunner executor, Check.DataType forcedDataType, Check.DetectionType detectionType, String name, Boolean def, long defaultAverageTime, long timeToNotify, long timeToPrevent, long timeToPunish) {
        super(executor.hackType, executor.protocol);
        this.name = name == null ? Integer.toString(this.getClass().getName().hashCode()) : name;
        this.hasName = name != null;
        this.prevention = new CheckPrevention();
        this.notifications = 0L;
        this.accumulatedTime = new long[Check.DataType.values().length];
        this.previousTime = new long[Check.DataType.values().length];
        for (Check.DataType dataType : Check.DataType.values()) {
            this.accumulatedTime[dataType.ordinal()] = 0L;
            this.previousTime[dataType.ordinal()] = -1L;
        }
        this.isEnabled();
        if (executor.addDetection(this.name, this) != null) {
            throw new IllegalArgumentException("Detection '" + this.name + "' already exists for enum '" + executor.hackType.toString() + "'.");
        }
        this.hackType.addDetection(this.name, defaultAverageTime);
    }

    public CheckDetection(CheckRunner executor, Check.DataType forcedDataType, Check.DetectionType detectionType, String name, Boolean def) {
        this(executor, forcedDataType, detectionType, name, def, 500L, 30000L, 40000L, 60000L);
    }

    public final int generalHashCode() {
        return CheckDetection.generalHashCode(this.hackType, this.name);
    }

    public final boolean isEnabled() {
        return !this.hasName || this.def == null || this.hackType.getCheck().handleCheck("check_" + this.name, this.def);
    }

    public final boolean canCall() {
        return !(!this.executor.canCall() || !this.isEnabled() || this.forcedDataType != null && this.forcedDataType != this.protocol.getDataType() || this.detectionType != null && this.detectionType != this.protocol.detectionType);
    }

    public final void call(Runnable runnable) {
        if (this.canCall()) {
            runnable.run();
        }
    }

    public final void clearData(Check.DataType dataType) {
        this.protocol.profile().clearTimeDifferences(this.hackType, dataType, this.name);
        this.accumulatedTime[this.protocol.getDataType().ordinal()] = 0L;
        this.previousTime[this.protocol.getDataType().ordinal()] = -1L;
    }

    private void increaseAccumulationTime(double amplitude, long time) {
        long add;
        double average = ResearchEngine.getAverageViolationTime(this, this.protocol.getDataType());
        double tpsLost = tpsMethod != null ? 20.0 - Bukkit.getTPS()[0] : 0.0;
        double ratioFromMax = (20.0 - tpsLost * 2.0) / 20.0;
        long previousTime = this.previousTime[this.protocol.getDataType().ordinal()];
        if (previousTime == -1L) {
            add = NumberConversions.round((double)(average * ratioFromMax * amplitude));
        } else {
            long difference = time - previousTime;
            this.protocol.profile().addTimeDifference(this.hackType, this.protocol.getDataType(), this.name, time);
            double multiplier = Math.min(average / (double)difference, 1.0);
            add = NumberConversions.round((double)(average * ratioFromMax * amplitude * multiplier));
        }
        this.previousTime[this.protocol.getDataType().ordinal()] = time;
        if (this.accumulatedTime[this.protocol.getDataType().ordinal()] <= System.currentTimeMillis()) {
            this.accumulatedTime[this.protocol.getDataType().ordinal()] = System.currentTimeMillis() + add;
        } else {
            int n = this.protocol.getDataType().ordinal();
            this.accumulatedTime[n] = this.accumulatedTime[n] + add;
        }
    }

    private long getAccumulatedTime(Check.DataType dataType) {
        return Math.max(this.accumulatedTime[dataType.ordinal()] - System.currentTimeMillis(), 0L);
    }

    public final int getLevel(Check.DataType dataType) {
        return NumberConversions.floor((double)((double)this.getAccumulatedTime(dataType) / ResearchEngine.getAverageViolationTime(this, dataType)));
    }

    public final int getNotificationLevel(Check.DataType dataType) {
        return NumberConversions.ceil((double)((double)this.timeToNotify / ResearchEngine.getAverageViolationTime(this, dataType)));
    }

    public final int getPreventionLevel(Check.DataType dataType) {
        return NumberConversions.ceil((double)((double)this.timeToPrevent / ResearchEngine.getAverageViolationTime(this, dataType)));
    }

    public final int getPunishmentLevel(Check.DataType dataType) {
        return NumberConversions.ceil((double)((double)this.timeToPunish / ResearchEngine.getAverageViolationTime(this, dataType)));
    }

    public final boolean canSendNotification(long time, int level, int notification) {
        boolean canSend;
        int ticks = this.executor.getNotificationTicksCooldown(this.protocol);
        canSend = level >= notification || DetectionNotifications.isVerboseEnabled(this.protocol);
        if (ticks == 0) {
            return canSend;
        }
        if (canSend && this.notifications <= time) {
            this.notifications = System.currentTimeMillis() + (long)ticks * 50L;
            return true;
        }
        return false;
    }

    private void notify(double amplitude, int level, int notification, int prevention, int punishment, long time, String information) {
        String notificationMsg = ConfigUtils.replaceWithSyntax(this.protocol, Config.messages.getColorfulString("detection_notification").replace("{info}", this.hackType.getCheck().handleCheck(this.protocol.getDataType()) || this.name == null ? information : this.name.toLowerCase().replace("_", "-")).replace("{detection:level}", (level >= punishment ? "\u00a74" : (level >= prevention ? "\u00a76" : (level >= notification ? "\u00a73" : "\u00a72"))) + NumberConversions.floor((double)((double)level / (double)punishment * 100.0)) + "/100"), this.hackType);
        Location location = this.protocol.getLocation();
        information = "(Player: " + this.protocol.bukkit().getName() + "), (" + checkIdentifier + " " + (Object)this.hackType + "), (" + javaPlayerIdentifier + " " + !this.protocol.isBedrockPlayer() + "), (" + detectionIdentifier + " " + this.name + "), (Level-Ratio: " + (double)level / (double)punishment + "), (Amplitude: " + Distance.computeAlgebra(amplitude, 2.0) + "),(" + levelIdentifier + " " + level + "),(" + notificationLevelIdentifier + " " + notification + "),(" + preventionLevelIdentifier + " " + prevention + "),(" + punishmentLevelIdentifier + " " + punishment + "),(Server-Version: " + MultiVersion.serverVersion.toString() + "), (Plugin-Version: " + BackgroundAPI.getVersion() + "), (Silent: " + this.hackType.getCheck().checkCheckCancellation(this.protocol.getDataType(), this.protocol.getWorld().getName()) + "), (Punish: " + this.hackType.getCheck().checkCheckCancellation(this.protocol.getDataType()) + "), (Packets: " + this.protocol.packetsEnabled() + "), (Ping: " + this.protocol.getPing() + "ms), (W-XYZ: " + location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + "), (Data: " + information + ")";
        AntiCheatLogs.logInfo(this.protocol, notificationMsg, information, level >= prevention, null, this.hackType, time);
        if (level >= notification) {
            CloudConnections.executeDiscordWebhook("checks", this.protocol.getUUID(), this.protocol.bukkit().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), "Detection", notificationMsg);
        }
        String command = Config.settings.getString("Notifications.message_clickable_command").replace("{player}", this.protocol.bukkit().getName());
        if (Config.settings.getBoolean("Notifications.individual_only_notifications")) {
            if (this.canSendNotification(time, level, notification)) {
                ClickableMessage.sendCommand((CommandSender)this.protocol.bukkit(), notificationMsg, command, command);
            }
        } else {
            List<PlayerProtocol> protocols = DetectionNotifications.getPlayers();
            if (!protocols.isEmpty()) {
                for (PlayerProtocol staff : protocols) {
                    CheckDetection detection = staff.getRunner(this.hackType).getDetection(this.name);
                    if (detection == null || !detection.canSendNotification(time, level, notification)) continue;
                    ClickableMessage.sendCommand((CommandSender)staff.bukkit(), notificationMsg, command, command);
                }
            }
        }
    }

    private void punish(int level, int max) {
        List<String> commands;
        Check check = this.hackType.getCheck();
        if (level >= max && check.checkCheckCancellation(this.protocol.getDataType()) && !(commands = check.checkCheckCancellation()).isEmpty()) {
            this.accumulatedTime[this.protocol.getDataType().ordinal()] = 0L;
            this.previousTime[this.protocol.getDataType().ordinal()] = -1L;
            int index = 0;
            boolean enabledDeveloperAPI = Config.settings.getBoolean("Important.enable_developer_api");
            for (String command : commands) {
                String modifiedCommand = ConfigUtils.replaceWithSyntax(this.protocol, command.replaceAll("\\{detections}|\\{detection}", check.getName()), null);
                commands.set(index++, modifiedCommand);
                if (enabledDeveloperAPI) {
                    Runnable runnable = () -> {
                        PlayerViolationCommandEvent event = new PlayerViolationCommandEvent(this.protocol.bukkit(), this.hackType, modifiedCommand);
                        Bukkit.getPluginManager().callEvent((Event)event);
                        if (!event.isCancelled()) {
                            PluginBase.runCommand(modifiedCommand);
                        }
                    };
                    if (PluginBase.isSynchronised()) {
                        runnable.run();
                        continue;
                    }
                    PluginBase.transferTask(this.protocol, runnable);
                    continue;
                }
                PluginBase.runCommand(modifiedCommand);
            }
        }
    }

    public final boolean prevent() {
        if (this.prevention.isCheckPrevention()) {
            if (PluginBase.isSynchronised() && Config.settings.getBoolean("Important.enable_developer_api")) {
                CheckCancelEvent checkCancelEvent = new CheckCancelEvent(this.protocol.bukkit(), this.hackType);
                Bukkit.getPluginManager().callEvent((Event)checkCancelEvent);
                if (checkCancelEvent.isCancelled()) {
                    return false;
                }
                this.prevention.handleCheck(this);
                return true;
            }
            this.prevention.handleCheck(this);
            return true;
        }
        return false;
    }

    protected Runnable prevention(Location location, boolean groundTeleport, double damage) {
        return () -> {
            if (location != null || groundTeleport) {
                if (location != null && this.protocol.teleport(location)) {
                    CheckDetection.setLastPrevention();
                }
                if (groundTeleport && this.protocol.groundTeleport()) {
                    CheckDetection.setLastPrevention();
                }
            }
            if (damage > 0.0) {
                this.protocol.bukkit().damage(damage);
            }
        };
    }

    public final void cancel(double amplitude, String information, Location location, int cancelTicks, boolean groundTeleport, double damage) {
        if (!this.executor.canCancel() || !this.executor.canRun()) {
            return;
        }
        CheckCancellation disableCause = this.executor.getDisableCause();
        if (disableCause != null && !disableCause.isCheckCancellation() && disableCause.checkCheckCancellation(information)) {
            return;
        }
        long time = System.currentTimeMillis();
        CheckPrevention newPrevention = new CheckPrevention(location, cancelTicks, groundTeleport, damage);
        boolean event = Config.settings.getBoolean("Important.enable_developer_api");
        CheckCancellation silentCause = this.executor.getSilentCause();
        double amplitudeFinal = Math.min(amplitude, 1.0);
        Runnable runnable = () -> {
            if (event) {
                PlayerViolationEvent playerViolationEvent = new PlayerViolationEvent(this.protocol.bukkit(), this.hackType, information);
                Bukkit.getPluginManager().callEvent((Event)playerViolationEvent);
                if (playerViolationEvent.isCancelled()) {
                    return;
                }
            }
            this.increaseAccumulationTime(amplitudeFinal, time);
            ResearchEngine.queueToCache(this.hackType, this.protocol.getDataType());
            int max = this.getPunishmentLevel(this.protocol.getDataType());
            int notification = this.getNotificationLevel(this.protocol.getDataType());
            int prevention = this.getPreventionLevel(this.protocol.getDataType());
            int level = Math.min(this.getLevel(this.protocol.getDataType()), max);
            this.notify(amplitudeFinal, level, notification, prevention, max, time, information);
            this.prevention = newPrevention;
            boolean bl = this.prevention.u = level >= prevention && !this.hackType.getCheck().checkCheckCancellation(this.protocol.getDataType(), this.protocol.getWorld().getName()) && (silentCause == null || silentCause.isCheckCancellation() || !silentCause.checkCheckCancellation(information));
            if (this.prevention.isCheckPrevention2()) {
                this.prevent();
            }
            this.punish(level, max);
        };
        if (PluginBase.isSynchronised() || !event) {
            runnable.run();
        } else {
            PluginBase.transferTask(this.protocol, runnable);
        }
    }

    public final void cancel(double amplitude, String information, Location location, int cancelTicks, boolean groundTeleport) {
        this.cancel(amplitude, information, location, cancelTicks, groundTeleport, 0.0);
    }

    public final void cancel(String information, Location location, int cancelTicks, boolean groundTeleport) {
        this.cancel(1.0, information, location, cancelTicks, groundTeleport, 0.0);
    }

    public final void cancel(double amplitude, String information, Location location, int cancelTicks) {
        this.cancel(amplitude, information, location, cancelTicks, false, 0.0);
    }

    public final void cancel(String information, Location location, int cancelTicks) {
        this.cancel(1.0, information, location, cancelTicks, false, 0.0);
    }

    public final void cancel(double amplitude, String information, Location location) {
        this.cancel(amplitude, information, location, 0, false, 0.0);
    }

    public final void cancel(String information, Location location) {
        this.cancel(1.0, information, location, 0, false, 0.0);
    }

    public final void cancel(double amplitude, String information) {
        this.cancel(amplitude, information, null, 0, false, 0.0);
    }

    public final void cancel(String information) {
        this.cancel(1.0, information, null, 0, false, 0.0);
    }

    static {
        try {
            tpsMethod = Bukkit.class.getMethod("getTPS", new Class[0]);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

