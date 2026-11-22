/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerVelocityEvent
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.event.PlayerTransactionEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.tracking.CheckConditions;
import ai.idealistic.spartan.utils.math.RayUtils;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class Velocity
extends CheckRunner {
    private final CheckDetection ag;
    private final CheckDetection ah;
    private final double[] ai = new double[]{0.248136, 0.3332};
    private float aj = 0.0f;
    private long ak = System.currentTimeMillis();
    private double al = 1.0;
    private int am = 0;
    private boolean transactionLock = false;
    private Vector velocity = null;

    public Velocity(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
        this.ag = new ImplementedDetection(this, null, Check.DetectionType.PACKETS, "vertical", true, 2000L, 30000L, 40000L, 60000L);
        this.ah = new ImplementedDetection(this, null, Check.DetectionType.PACKETS, "horizontal", true, 2000L, 30000L, 40000L, 60000L);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof PlayerTransactionEvent) {
            this.transactionLock = false;
        } else if (object instanceof PlayerVelocityEvent) {
            PlayerVelocityEvent event = (PlayerVelocityEvent)object;
            this.transactionLock = true;
            Location[] locationsToCheck = new Location[]{this.protocol.getLocation().clone().add(event.getVelocity()), this.protocol.getLocation().clone().add(event.getVelocity()).add(0.0, 1.0, 0.0)};
            boolean allClear = true;
            for (Location loc : locationsToCheck) {
                if (!this.handleCheck(loc)) continue;
                allClear = false;
                break;
            }
            if (allClear) {
                this.velocity = event.getVelocity();
                this.al = 1.0;
                this.am = 0;
            }
            if (this.aj > 0.0f) {
                this.aj -= 5.0f;
            }
        } else if (object instanceof PlayerMoveEvent && (this.ah.canCall() || this.ag.canCall())) {
            long delay = System.currentTimeMillis() - this.ak;
            PlayerMoveEvent event = (PlayerMoveEvent)object;
            Location from = event.getFrom();
            Location to = event.getTo();
            double x = to.getX() - from.getX();
            double y = to.getY() - from.getY();
            double z = to.getZ() - from.getZ();
            if (this.velocity != null) {
                if (Math.abs(Math.abs(y) - Math.abs(this.velocity.getY())) < this.al) {
                    this.al = y;
                }
                if (Math.abs(Math.abs(y) - Math.abs(this.velocity.getY())) < 1.0E-4) {
                    double xDiff = x - this.velocity.getX();
                    double zDiff = z - this.velocity.getZ();
                    double total = Math.abs(xDiff) + Math.abs(zDiff);
                    double multi = 1.0;
                    if (!this.protocol.isOnGround() && !this.protocol.isOnGroundFrom()) {
                        if (total > 0.05 * multi) {
                            this.handleCheck("velocity manipulation [xz] (air deviation: " + total + ")", 14.0, this.ah);
                        }
                    } else if (total > 0.2 * multi) {
                        this.handleCheck("velocity manipulation [xz] (ground deviation: " + total + ")", 30.0, this.ah);
                    }
                    this.velocity = null;
                    this.am = 0;
                } else if (delay > 25L) {
                    if (this.am < 2) {
                        if (!this.transactionLock) {
                            ++this.am;
                        }
                    } else {
                        if (this.velocity.getY() != 0.003) {
                            this.handleCheck("velocity manipulation [y] (motion: " + Velocity.isCheckPrevention2(this.al) + ", velocity: " + Velocity.isCheckPrevention2(this.velocity.getY()) + ")", this.getT(this.al) ? 12.0 : 25.0, this.ag);
                        }
                        this.velocity = null;
                    }
                }
            }
            this.ak = System.currentTimeMillis();
        }
    }

    private void handleCheck(String reason, double vl, CheckDetection e) {
        e.call(() -> {
            this.aj += (float)vl;
            if (this.aj > 60.0f) {
                e.cancel(reason);
                this.aj = 50.0f;
            }
        });
    }

    private boolean getT(double v) {
        for (double d : this.ai) {
            if (d != Velocity.isCheckPrevention2(v)) continue;
            return true;
        }
        return false;
    }

    private static double isCheckPrevention2(double v) {
        return RayUtils.scaleVal(v, 6.0);
    }

    private boolean handleCheck(Location location) {
        double x = location.getX();
        double y = location.getY() + 0.1;
        double z = location.getZ();
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -1; dz <= 1; ++dz) {
                    Material material = new ServerLocation(new Location(this.protocol.getWorld(), x.3, y.3, z.3)).getBlock(this.protocol).getTypeOrNull();
                    if ((material == null || !BlockUtils.isSemiSolid(material) && !BlockUtils.isSolid(material) && !material.toString().contains("GRASS")) && !BlockUtils.isLiquid(material)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean canRun() {
        return this.protocol.getVehicle() == null && !this.protocol.getEnvironment().isSlime() && !this.protocol.getEnvironment().isSlimeWide() && !this.protocol.getEnvironment().isSlimeHeight() && CheckConditions.canCheckCombat(this.protocol) && !this.protocol.isOutsideOfTheBorder(-1.0);
    }
}

