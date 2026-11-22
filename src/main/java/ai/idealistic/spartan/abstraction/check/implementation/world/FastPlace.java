/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package ai.idealistic.spartan.abstraction.check.implementation.world;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.event.CBlockPlaceEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.compatibility.manual.building.PrinterMode;
import ai.idealistic.spartan.compatibility.manual.entity.VehicleEnterListener;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import org.bukkit.Material;

public class FastPlace
extends CheckRunner {
    private final CheckDetection A = new ImplementedDetection(this, null, null, null, true);
    private long time;
    private final Buffer.Storage aB = new Buffer.Storage();

    public FastPlace(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof CBlockPlaceEvent) {
            this.A.call(() -> {
                Material material;
                CBlockPlaceEvent event = (CBlockPlaceEvent)object;
                if (!(ItemsAdder.is(event.placedBlock) || BlockUtils.isScaffoldingBlock(event.placedBlock.getType()) || (material = event.placedBlock.getType()) == Material.FIRE || material == Material.AIR || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) && (material == Material.CAVE_AIR || material == Material.VOID_AIR))) {
                    double amplitude;
                    long timePassed = System.currentTimeMillis() - this.time;
                    this.time = System.currentTimeMillis();
                    int ticks = 20;
                    long max = 150L;
                    if (timePassed <= max && (timePassed > 50L || !this.protocol.isBedrockPlayer()) && this.aB.count(amplitude = (double)max / (double)timePassed, ticks) >= 9.0) {
                        this.A.cancel("remaining-ticks: " + this.aB.ticksRemaining(ticks) + ", place-ms: " + timePassed + ", block: " + BlockUtils.materialToString(material), null, ticks);
                    }
                }
            });
        }
    }

    @Override
    protected boolean canRun() {
        return !this.protocol.isFlying() && !PrinterMode.isUsing(this.protocol) && !VehicleEnterListener.has(this.protocol, new String[]{"drill", "tractor"});
    }
}

