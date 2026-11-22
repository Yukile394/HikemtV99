/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  ai.idealistic.spartan.abstraction.event.RotationEvent
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.Track;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.Post;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.KAMoveLength;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.KAMoveDirection;
import ai.idealistic.spartan.abstraction.event.EntityAttackPlayerEvent;
import ai.idealistic.spartan.abstraction.event.PlayerAttackEvent;
import ai.idealistic.spartan.abstraction.event.RotationEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.tracking.CheckConditions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class KillAura
extends CheckRunner {
    private final Post aF = new Post(this);
    private final KAMoveDirection aG = new KAMoveDirection(this);
    private final KAMoveLength aH = new KAMoveLength(this);
    private final Track aI = new Track(this);

    public KillAura(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    boolean handleCheck(LivingEntity entity) {
        return ProtocolLib.getVehicle((Entity)entity) == null && CheckConditions.canCheckCombat(this.protocol, entity) && !this.protocol.bukkit().equals(entity) && this.hackType.getCheck().handleCheck("detection." + (entity instanceof Player ? "players" : "entities"), true);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof PlayerMoveEvent) {
            this.aG.run();
            this.aH.run();
        } else if (object instanceof RotationEvent) {
            this.aG.handleCheck((RotationEvent)object);
        } else if (object instanceof PlayerAttackEvent) {
            PlayerAttackEvent event = (PlayerAttackEvent)object;
            LivingEntity target = event.target;
            if (this.handleCheck(target)) {
                this.aG.handleCheck((Entity)target);
                this.aF.run();
                this.aH.updateMoveLength();
                this.aI.handleCheck(event);
            }
        } else if (object instanceof EntityAttackPlayerEvent) {
            this.aH.updateMoveLength2();
        }
    }

    @Override
    protected boolean canRun() {
        return CheckConditions.canCheckCombat(this.protocol);
    }
}

