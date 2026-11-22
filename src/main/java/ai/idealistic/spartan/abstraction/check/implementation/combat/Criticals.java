/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffectType
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.event.PlayerAttackEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.tracking.CheckConditions;
import ai.idealistic.spartan.utils.math.RayUtils;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import org.bukkit.potion.PotionEffectType;

public class Criticals
extends CheckRunner {
    private final CheckDetection A = new ImplementedDetection(this, null, null, null, true, 2000L, 30000L, 40000L, 60000L);

    public Criticals(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof PlayerAttackEvent) {
            this.A.call(() -> {
                double verticalMaxAccuracy = this.protocol.getLocation().getY() - this.protocol.getFromLocation().getY();
                if (RayUtils.onBlock(this.protocol, this.protocol.getLocationOrVehicle(), BlockUtils.web)) {
                    return;
                }
                if ((verticalMaxAccuracy < 0.079 || Math.abs(verticalMaxAccuracy) == 0.0625) && verticalMaxAccuracy > 0.0 && !this.protocol.isOnGround() && this.protocol.isOnGroundFrom() && !new ServerLocation(this.protocol.getLocation()).getBlock(this.protocol).getType().isBlock() && !RayUtils.onSolidBlock(this.protocol, this.protocol.getLocation().clone().add(0.0, 2.0, 0.0))) {
                    this.A.cancel("packet");
                }
            });
        }
    }

    @Override
    protected boolean canRun() {
        return CheckConditions.canCheckCombat(this.protocol) && !this.protocol.getEnvironment().isLiquid() && !this.protocol.hasPotionEffect(PotionEffectType.BLINDNESS, 0L);
    }
}

