/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.event.entity.EntityToggleGlideEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.movement.irregularmovements;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.implementation.movement.irregularmovements.IMFoodSprint;
import ai.idealistic.spartan.abstraction.check.implementation.movement.irregularmovements.IMHeadPosition;
import ai.idealistic.spartan.abstraction.check.implementation.movement.irregularmovements.Elytra;
import ai.idealistic.spartan.abstraction.check.implementation.movement.irregularmovements.VehiclePrediction;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.CheckConditions;
import ai.idealistic.spartan.listeners.protocol.TeleportListener;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class IrregularMovements
extends CheckRunner {
    private final IMFoodSprint eh = new IMFoodSprint(this);
    private final IMHeadPosition ei = new IMHeadPosition(this);
    private final Elytra ej = new Elytra(this);
    private final VehiclePrediction ek = new VehiclePrediction(this);

    public IrregularMovements(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof PlayerTeleportEvent) {
            this.S();
        } else if (object instanceof PlayerMoveEvent) {
            this.eh.run();
            this.ei.run();
            this.ej.at();
            this.ek.run();
        } else if (PlayerUtils.elytra && object instanceof EntityToggleGlideEvent) {
            this.ej.as();
        } else if (PluginBase.packetsEnabled() && object instanceof PacketEvent) {
            PacketType eventType = ((PacketEvent)object).getPacketType();
            for (PacketType type : TeleportListener.packetTypes) {
                if (!type.equals(eventType)) continue;
                this.S();
                break;
            }
        }
    }

    private void S() {
        this.ej.S();
        this.ek.S();
    }

    @Override
    protected boolean canRun() {
        return CheckConditions.canCheckMovement(this.protocol, true, true, false, true, false);
    }
}

