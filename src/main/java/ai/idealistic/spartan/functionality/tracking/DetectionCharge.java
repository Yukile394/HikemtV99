/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.functionality.tracking;

import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.utils.java.OverflowMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DetectionCharge {
    private static final long notify = 3600000L;
    private static final long add = 5000L;
    private static long charge = System.currentTimeMillis() + 0x6DDD00L;
    private static final Map<UUID, Buffer.Storage> map = new OverflowMap<UUID, Buffer.Storage>(new ConcurrentHashMap(), 128);

    public static boolean charge(PlayerProtocol protocol) {
        Buffer.Storage buffer;
        if (PluginAddons.freeEdition && (buffer = map.computeIfAbsent(protocol.getUUID(), uuid -> new Buffer.Storage())).count(1.0, 20) <= 10.0) {
            charge = charge > System.currentTimeMillis() ? (charge += 5000L) : System.currentTimeMillis() + 5000L;
            return true;
        }
        return false;
    }

    public static boolean has() {
        return !PluginAddons.freeEdition || charge > System.currentTimeMillis();
    }

    public static long remaining() {
        return Math.max(charge - System.currentTimeMillis(), 0L);
    }

    public static boolean isLow() {
        return PluginAddons.freeEdition && DetectionCharge.remaining() <= 3600000L;
    }

    public static void notify(PlayerProtocol protocol) {
        if (DetectionCharge.isLow() && Permissions.isStaff(protocol.bukkit())) {
            AwarenessNotifications.forcefullySend(protocol, "detections are running low on charge, run \\\"/spartan charge\" in-game to add more.", true);
        }
    }
}

