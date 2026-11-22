/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package ai.idealistic.spartan.abstraction.check;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.Location;

public class CheckPrevention {
    boolean u = false;
    private final Location location;
    private final boolean v;
    private final double w;
    private final long expiration;

    CheckPrevention() {
        this.location = null;
        this.v = false;
        this.w = 0.0;
        this.expiration = 0L;
    }

    CheckPrevention(Location location, int cancelTicks, boolean groundTeleport, double damage) {
        this.v = groundTeleport && Config.settings.getBoolean("Detections.ground_teleport_on_detection");
        this.w = Config.settings.getBoolean("Detections.damage_on_detection") ? damage : 0.0;
        this.expiration = cancelTicks <= 1 ? Long.MAX_VALUE : System.currentTimeMillis() + (long)cancelTicks * 50L;
    }

    boolean isCheckPrevention() {
        return this.u && System.currentTimeMillis() <= this.expiration;
    }

    boolean isCheckPrevention2() {
        return this.location != null || this.v || this.w > 0.0;
    }

    void handleCheck(CheckDetection detection) {
        if (PluginBase.isSynchronised()) {
            detection.prevention(this.location, this.v, this.w).run();
        } else {
            PluginBase.transferTask(detection.protocol, detection.prevention(this.location, this.v, this.w));
        }
    }
}

