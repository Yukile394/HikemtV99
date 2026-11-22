/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.event.Cancellable
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.check;

import ai.idealistic.spartan.abstraction.check.CheckCancellation;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckProcess;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.compatibility.manual.building.Stack;
import ai.idealistic.spartan.compatibility.manual.enchants.Enchants;
import ai.idealistic.spartan.compatibility.manual.enchants.Version;
import ai.idealistic.spartan.compatibility.manual.vanilla.Attributes;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.DetectionNotifications;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.DetectionCharge;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.util.NumberConversions;

public abstract class CheckRunner
extends CheckProcess {
    private static final boolean v1_8 = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8);
    final long creation = System.currentTimeMillis();
    private final Collection<CheckCancellation> disableCauses;
    private final Collection<CheckCancellation> silentCauses;
    private boolean cancelled;
    private final Map<String, CheckDetection> detections = new ConcurrentHashMap<String, CheckDetection>2;

    public CheckRunner(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
        this.disableCauses = Collections.synchronizedList(new ArrayList(1));
        this.silentCauses = Collections.synchronizedList(new ArrayList(1));
    }

    public final CheckDetection getDetection(String detection) {
        return this.detections.get(detection);
    }

    public final Collection<CheckDetection> getDetections() {
        return this.detections.values();
    }

    protected final CheckDetection addDetection(String name, CheckDetection detection) {
        return this.detections.putIfAbsent(name, detection);
    }

    public final void removeDetection(CheckDetection detection) {
        this.detections.remove(detection.name);
    }

    public final void handle(Object cancelled, Object object) {
        boolean result = cancelled == null ? (object instanceof Cancellable ? ((Cancellable)object).isCancelled() : false) : (cancelled instanceof Boolean ? (Boolean)cancelled : (cancelled instanceof Cancellable ? ((Cancellable)cancelled).isCancelled() : false));
        this.cancelled = result;
        this.handleInternal(result, object);
    }

    protected void handleInternal(boolean cancelled, Object object) {
    }

    protected boolean canRun() {
        return true;
    }

    final boolean canCall() {
        return !(this.protocol.npc || !DetectionCharge.has() || !this.hackType.getCheck().handleCheck(this.protocol.getDataType(), this.protocol.getWorld().getName()) || this.cancelled && !this.hackType.getCheck().g || v1_8 && this.protocol.getGameMode() == GameMode.SPECTATOR || Attributes.getAmount(this.protocol, "GENERIC_SCALE") != Double.MIN_VALUE || !PluginAddons.ownsCheck(this.hackType) || !PluginAddons.ownsEdition(this.protocol.getDataType()));
    }

    final boolean canCancel() {
        return (double)(System.currentTimeMillis() - this.creation) > 1000.0 && !ProtocolLib.isTemporary((OfflinePlayer)this.protocol.bukkit()) && !Permissions.isBypassing(this.protocol.bukkit(), this.hackType);
    }

    public final List<String> getEvidence() {
        if (this.protocol == null) {
            return new ArrayList<String>();
        }
        ArrayList<String> evidence = new ArrayList<String>();
        for (CheckDetection detection : this.getDetections()) {
            if (detection.getLevel(this.protocol.getDataType()) < detection.getNotificationLevel(this.protocol.getDataType())) continue;
            evidence.add(detection.name);
        }
        return evidence;
    }

    private CheckCancellation getLastCause(Collection<CheckCancellation> collection) {
        CheckCancellation lastCause = null;
        Iterator<CheckCancellation> iterator = collection.iterator();
        while (iterator.hasNext()) {
            CheckCancellation cause = iterator.next();
            if (cause.isCheckCancellation()) {
                iterator.remove();
                continue;
            }
            lastCause = cause;
            break;
        }
        return lastCause;
    }

    public final CheckCancellation getDisableCause() {
        CheckCancellation disableCause = this.getLastCause(this.disableCauses);
        if (disableCause == null) {
            return Stack.is(this.protocol) ? new CheckCancellation(Compatibility.CompatibilityType.MYTHIC_MOBS) : (ItemsAdder.is(this.protocol) ? new CheckCancellation(Compatibility.CompatibilityType.ITEMS_ADDER) : (Enchants.has(this.protocol) ? new CheckCancellation(Compatibility.CompatibilityType.CUSTOM_ENCHANTS_PLUS) : (Version.has(this.protocol) ? new CheckCancellation(Compatibility.CompatibilityType.ECO_ENCHANTS) : null)));
        }
        return disableCause;
    }

    public final CheckCancellation getSilentCause() {
        return this.getLastCause(this.silentCauses);
    }

    public final void addDisableCause(String reason, String pointer, int ticks) {
        if (reason == null) {
            reason = this.hackType.getCheck().getName();
        }
        this.disableCauses.add(new CheckCancellation(reason, pointer, ticks));
        PluginBase.playerInfo.refresh(this.protocol.bukkit().getName());
    }

    public final void addSilentCause(String reason, String pointer, int ticks) {
        this.silentCauses.add(new CheckCancellation(reason, pointer, ticks));
        PluginBase.playerInfo.refresh(this.protocol.bukkit().getName());
    }

    public final void removeDisableCause() {
        this.disableCauses.clear();
        PluginBase.playerInfo.refresh(this.protocol.bukkit().getName());
    }

    public final void removeSilentCause() {
        this.silentCauses.clear();
        PluginBase.playerInfo.refresh(this.protocol.bukkit().getName());
    }

    public final boolean prevent() {
        if (!this.detections.isEmpty()) {
            for (CheckDetection detection : this.getDetections()) {
                if (!detection.prevent()) continue;
                return true;
            }
        }
        return false;
    }

    final int getNotificationTicksCooldown(PlayerProtocol detected) {
        Integer frequency = DetectionNotifications.getFrequency(this.protocol);
        if (frequency != null && frequency != Integer.MIN_VALUE) {
            return frequency;
        }
        if (detected != null && (detected.equals(this.protocol) || detected.getWorld().equals((Object)this.protocol.getWorld()) && detected.getLocation().distance(this.protocol.getLocation()) <= 16.0)) {
            return NumberConversions.round((double)Math.sqrt(20.0));
        }
        return NumberConversions.round((double)20.0);
    }
}

