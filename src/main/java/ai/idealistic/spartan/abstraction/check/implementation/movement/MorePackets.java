/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Location
 *  org.bukkit.event.player.PlayerTeleportEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.movement;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.data.OtherTimerBalancer;
import ai.idealistic.spartan.abstraction.event.PlayerTickEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.CheckConditions;
import ai.idealistic.spartan.listeners.bukkit.standalone.ChunksEvent;
import ai.idealistic.spartan.listeners.protocol.TeleportListener;
import ai.idealistic.spartan.utils.math.RayUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MorePackets
extends CheckRunner {
    private static final double dg = 1.05;
    private int dh = 0;
    private final ImplementedDetection di = new ImplementedDetection(this, Check.DataType.JAVA, Check.DetectionType.PACKETS, "positive_net", true);
    private final ImplementedDetection dj = new ImplementedDetection(this, Check.DataType.JAVA, Check.DetectionType.PACKETS, "positive_latency", true);
    private final ImplementedDetection dk = new ImplementedDetection(this, Check.DataType.JAVA, Check.DetectionType.PACKETS, "positive_balance", true);
    private final ImplementedDetection dl = new ImplementedDetection(this, Check.DataType.JAVA, Check.DetectionType.PACKETS, "negative", true);
    private long dm;

    public MorePackets(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        block5: {
            block4: {
                double multiply;
                OtherTimerBalancer timerBalancer;
                long delay;
                block8: {
                    block7: {
                        block6: {
                            if (!(object instanceof PlayerTickEvent)) break block4;
                            if (!this.dk.canCall() && !this.dj.canCall() && !this.di.canCall() && !this.dl.canCall()) break block5;
                            PlayerTickEvent event = (PlayerTickEvent)object;
                            delay = event.getDelay();
                            timerBalancer = event.protocol.timerBalancer;
                            multiply = RayUtils.scaleVal(50.0 / (double)delay, 2.0);
                            if (!timerBalancer.isNegativeTimer() || delay <= 50L) break block6;
                            this.dl.call(() -> {
                                if (multiply < 1.0) {
                                    this.handleCheck(this.dl, "negative, multiply: " + multiply, 1.0 / multiply);
                                }
                            });
                            break block5;
                        }
                        if (delay >= 50L || timerBalancer.getResult() <= 45L) break block7;
                        this.dk.call(() -> {
                            if (multiply >= 1.05) {
                                this.handleCheck(this.dk, "positive(balance), multiply: " + multiply, multiply);
                            }
                        });
                        break block5;
                    }
                    if (delay >= 50L || timerBalancer.getForced() <= 8) break block8;
                    this.di.call(() -> {
                        if (multiply >= 1.05) {
                            this.handleCheck(this.di, "positive(NET), multiply: " + multiply, multiply);
                        }
                    });
                    break block5;
                }
                if (delay >= 50L || timerBalancer.getLatency() <= 50) break block5;
                this.dj.call(() -> {
                    if (multiply >= 1.05) {
                        this.handleCheck(this.dj, "positive(latency), multiply: " + multiply, multiply);
                    }
                });
                break block5;
            }
            if (object instanceof PlayerTeleportEvent) {
                this.S();
            } else if (PluginBase.packetsEnabled() && object instanceof PacketEvent) {
                PacketType eventType = ((PacketEvent)object).getPacketType();
                for (PacketType type : TeleportListener.packetTypes) {
                    if (!type.equals(eventType)) continue;
                    this.S();
                    break;
                }
            }
        }
    }

    private void S() {
        this.dm = System.currentTimeMillis() + 200L;
        this.dh = 0;
    }

    private void handleCheck(CheckDetection executor, String info, double amplitude) {
        this.dh += 70;
        if (this.dh > 100) {
            executor.cancel(amplitude, info, this.protocol.getFromLocation(), 0, true);
            this.dh -= 10;
        } else if (this.dh > 0) {
            --this.dh;
        }
    }

    @Override
    protected boolean canRun() {
        if (!this.protocol.isAFK() && this.dm < System.currentTimeMillis() && CheckConditions.canCheckMovement(this.protocol, false, false, true, true, true)) {
            Location location = this.protocol.bukkit().getLocation();
            return ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4) && ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() + 0, location.getBlockZ() >> 4) && ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() + 0) && ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() - 0, location.getBlockZ() >> 4) && ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() - 0) && ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() + 0, location.getBlockZ() + 0) && ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() - 0, location.getBlockZ() - 0) && ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() + 0, location.getBlockZ() - 0) && ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() - 0, location.getBlockZ() + 0);
        }
        return false;
    }
}

