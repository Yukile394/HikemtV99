/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameRule
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.minecraft.server.PluginUtils;
import org.bukkit.GameRule;
import org.bukkit.util.NumberConversions;

public class AutoRespawn
extends CheckDetection {
    private long ew;

    AutoRespawn(CheckRunner executor) {
        super(executor, null, Check.DetectionType.BUKKIT, "auto_respawn", true);
    }

    void run() {
        this.call(() -> {
            long limit;
            if (!this.canRun()) {
                return;
            }
            long time = System.currentTimeMillis() - this.ew;
            if (time <= (limit = 750L) && !this.protocol.bukkit().isDead()) {
                this.ew = 0L;
                this.cancel("auto-respawn, ms: " + time, this.protocol.getFromLocation(), NumberConversions.ceil((double)20.0));
            }
        });
    }

    void N() {
        this.ew = System.currentTimeMillis();
    }

    private boolean canRun() {
        return !PluginUtils.contains("respawn") && (!MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_15) || (Boolean)this.protocol.getWorld().getGameRuleValue(GameRule.DO_IMMEDIATE_RESPAWN) != true);
    }
}

