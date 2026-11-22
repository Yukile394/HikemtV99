/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.vehicle.VehicleEnterEvent
 *  org.bukkit.potion.PotionEffectType
 */
package ai.idealistic.spartan.abstraction.check.implementation.movement;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.event.CPlayerRiptideEvent;
import ai.idealistic.spartan.abstraction.event.CPlayerVelocityEvent;
import ai.idealistic.spartan.abstraction.event.SuperPositionPacketEvent;
import ai.idealistic.spartan.abstraction.protocol.ExtendedPotionEffect;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.compatibility.manual.vanilla.Attributes;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.CheckConditions;
import ai.idealistic.spartan.listeners.protocol.TeleportListener;
import ai.idealistic.spartan.utils.math.MathHelper;
import ai.idealistic.spartan.utils.math.RayLine;
import ai.idealistic.spartan.utils.math.RayUtils;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import ai.idealistic.spartan.utils.minecraft.vector.CVector2D;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.PotionEffectType;

public class Cancellation
extends CheckRunner {
    private final CheckDetection A = new ImplementedDetection(this, null, null, null, true);
    private double cY = 0.0;
    private double cZ = 0.0;
    private int cD = 0;
    private int cC = 0;
    private int dn = 0;
    private int cJ = 0;
    private int field0 = 0;
    private boolean dp = false;
    private boolean dq = true;
    private static final double dr = 0.026;
    private static final double ds = 0.02;
    private static final double dt = 0.0073;
    private double du = 0.0;
    private double dv = 0.0;
    private static final double[] dw = new double[]{0.2806, 0.1726};
    private static final double[] dx = new double[]{0.6132, 0.3601};
    private double dy = 0.0;
    private double dz = 0.0;
    private double dA = 0.0;
    private double dB = 0.0;
    private double dC = 0.0;
    private int dD = 0;
    private int dE = 0;
    private int dF = 0;
    private boolean dG = false;

    public Cancellation(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof PlayerTeleportEvent) {
            this.S();
        } else if (object instanceof SuperPositionPacketEvent) {
            SuperPositionPacketEvent e = (SuperPositionPacketEvent)object;
            this.isCheckCancellation((Boolean)e.packetEvent.getPacket().getBooleans().read(0));
        } else if (object instanceof CPlayerRiptideEvent) {
            this.T();
        } else if (object instanceof PlayerMoveEvent) {
            this.run();
        } else if (object instanceof VehicleEnterEvent) {
            this.R();
        } else if (PluginBase.packetsEnabled() && object instanceof PacketEvent) {
            PacketType eventType = ((PacketEvent)object).getPacketType();
            for (PacketType type : TeleportListener.packetTypes) {
                if (!type.equals(eventType)) continue;
                this.S();
                break;
            }
        }
    }

    @Override
    protected boolean canRun() {
        return CheckConditions.canCheckMovement(this.protocol, true, true, false, true, false);
    }

    private boolean handleCheck(PlayerProtocol p) {
        return p.wasGliding() || p.getVehicle() != null;
    }

    private void run() {
        this.A.call(() -> {
            PlayerProtocol p = this.protocol;
            if (this.handleCheck(p)) {
                this.cD = 0;
                this.cC = 0;
                this.dn = 0;
                this.cY = 0.0;
                this.cZ = 0.0;
                this.dp = false;
                this.dq = true;
                this.du = 0.0;
                this.dv = 0.0;
                this.dy = 0.0;
                this.dz = 0.0;
                this.dA = 0.0;
                this.dB = 0.0;
                this.dC = 0.0;
                this.dD = 0;
                this.dE = 0;
                this.dF = 0;
                this.dG = false;
                this.cJ = 0;
                this.field0 = 0;
                return;
            }
            boolean isOnGround = p.getVehicle() != null ? p.getVehicle().isOnGround() : p.isOnGround();
            Location location =  p.getLocation();
            Location from = this.protocol.getFromLocation();
            double dx = to.getX() - from.getX();
            double dz = to.getZ() - from.getZ();
            double speed = Math.sqrt(dx * dx + dz * dz);
            double rYaw = Math.toRadians(MathHelper.floor_float(to.getYaw()));
            RayLine yawLine = new RayLine(-Math.sin(rYaw), Math.cos(rYaw));
            RayLine moveLine = new RayLine(dx, dz);
            boolean isOnAngle = RayUtils.validRayLines(yawLine, moveLine, 46.0);
            boolean isOnAngle80 = RayUtils.validRayLines(yawLine, moveLine, 80.0);
            boolean flagged = false;
            if (!this.protocol.useItemPacket) {
                this.dn = 0;
            }
            double atr = Math.max(Attributes.getAmount(this.protocol, "GENERIC_MOVEMENT_SPEED"), 0.0) * 1.3;
            if (to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()) {
                this.isCheckCancellation(isOnGround);
                if (this.cC == 0) {
                    double limitAccel;
                    double limit;
                    this.dq = false;
                    this.dp = false;
                    double d = isOnAngle ? 0.2975 : (limit = isOnAngle80 ? 0.289 : 0.235);
                    limit = this.dn > 6 ? 0.061 : (this.dn > 3 ? 0.09 : (this.dn > 1 ? 0.17 : limit));
                    limitAccel = RayUtils.validRayLines(yawLine, moveLine, 65.0) ? 0.15 : 0.12 + atr;
                    if (limit < (limit + atr) * this.getSpeedSimulation2()) {
                        this.du = ((limit + atr) * this.getSpeedSimulation2() - limit) * 0.5;
                    }
                    if (this.cD == 1 && this.dC > limitAccel) {
                        this.du = this.dC.91;
                    }
                    limit += atr;
                    limit *= this.getSpeedSimulation2();
                    limit += this.du;
                    if (this.protocol.getComponentXZ().pistonTick && speed > limit) {
                        limit += 1.0;
                        this.du += 1.0;
                        this.dG = true;
                    }
                    if (speed < limit) {
                        this.isCheckCancellation(dx, dz);
                    }
                    if (this.du > 0.0) {
                        this.du -= 0.02;
                    }
                    if (this.du < 0.0) {
                        this.du = 0.0;
                    }
                } else if (this.cC == 1 || this.cC == 2 && !this.dp) {
                    double lim = RayUtils.validRayLines(yawLine, moveLine, 46.0) || this.dq ? dx[Math.max(0, Math.min(this.cC - 1, dx.length - 1))] : (RayUtils.validRayLines(yawLine, moveLine, 65.0) ? dw[Math.max(0, Math.min(this.cC - 1, dw.length - 1))] + dx[Math.max(0, Math.min(this.cC - 1, dx.length - 1))] / 2.0 : dw[Math.max(0, Math.min(this.cC - 1, dw.length - 1))]);
                    if (this.du > 0.146) {
                        lim += this.du.146;
                    }
                    lim += atr;
                    lim *= this.getSpeedSimulation2();
                    if (this.dn > 0) {
                        lim /= 1.2;
                    }
                    if (this.protocol.getComponentXZ().pistonTick) {
                        lim += 1.0;
                    }
                    if (speed < lim + this.dv) {
                        this.isCheckCancellation(dx, dz);
                    } else {
                        this.isCheckCancellation(-Math.sin(Math.toRadians(to.getYaw())) * lim, Math.cos(Math.toRadians(to.getYaw())) * lim);
                    }
                    if (p.isSprinting() && isOnAngle && this.cC == 1) {
                        this.dq = true;
                    }
                    this.dv /= 1.3;
                } else {
                    boolean isCorrect;
                    boolean point;
                    double pX = this.dA * (double)0.91f;
                    double pZ = this.dB * (double)0.91f;
                    this.isCheckCancellation(pX, pZ);
                    double speedAccel = Math.sqrt(Cancellation.handleDataType(dx, pX) * Cancellation.handleDataType(dx, pX) + Cancellation.handleDataType(dz, pZ) * Cancellation.handleDataType(dz, pZ));
                    double moveAngle = RayUtils.calculateRayLine(moveLine);
                    double accel = (this.dn > 2 ? 0.0073 : (this.dn > 1 ? 0.02 : 0.026)) * this.getSpeedSimulation2();
                    boolean nearbyEntities = false;
                    for (Entity entity : this.protocol.getNearbyEntities(2.4)) {
                        if (entity.getUniqueId() == this.protocol.getUUID()) continue;
                        nearbyEntities = true;
                        break;
                    }
                    point = this.handleCheck(to, from, to.clone().add(-Math.sin(moveAngle) * 0.3, 0.0, Math.cos(moveAngle) * 0.3)) || nearbyEntities;
                    if (point) {
                        double d = nearbyEntities ? accel.2 : 0.03;
                        if (speed < this.dC.2 + d) {
                            if (Cancellation.handleDataType(dx, pX) > accel) {
                                pX = dx;
                            }
                            if (Cancellation.handleDataType(dz, pZ) > accel) {
                                pZ = dz;
                            }
                        }
                    }
                    isCorrect = Cancellation.handleDataType(dx, pX) < accel && Cancellation.handleDataType(dz, pZ) < accel;
                    if (isCorrect) {
                        this.isCheckCancellation(dx, dz);
                    } else {
                        this.isCheckCancellation(-Math.sin(Math.toRadians(to.getYaw())) * 0.02, Math.cos(Math.toRadians(to.getYaw())) * 0.02);
                    }
                }
                this.handleCheck(to.getY() - from.getY(), 0.039 * this.getSpeedSimulation2(), new CVector2D(dx, dz));
                if (this.isCheckPrevention2(to) || this.isCheckPrevention2(from)) {
                    this.dD = 4;
                }
                if (dx != this.dy || dz != this.dz) {
                    double pr = Math.sqrt(this.dy * this.dy + this.dz * this.dz);
                    double d = Cancellation.handleDataType(pr, speed);
                    if (d <= 1.01 && d > 0.1 && this.protocol.getComponentXZ().pistonTick) {
                        this.isCheckCancellation(dx, dz);
                        this.dG = true;
                    }
                    if (this.protocol.getComponentXZ().pistonTick && this.dD > 0) {
                        this.dD = 0;
                        this.dE = 3;
                        this.dG = true;
                    }
                    if (d >= 0.04 && d <= 2.2 && this.dE > 0) {
                        --this.dE;
                        this.isCheckCancellation(dx, dz);
                    }
                    if (d >= 0.04 && d <= 3.1 && this.dF > 0) {
                        --this.dF;
                        this.isCheckCancellation(dx, dz);
                    }
                    if (d >= 0.02 && d <= 1.7 && this.protocol.getComponentXZ().explosionTick) {
                        this.protocol.getComponentXZ().explosionTick = false;
                        this.isCheckCancellation(dx, dz);
                    }
                    if (this.protocol.getComponentXZ().pistonTick && Cancellation.handleDataType(pr, speed) > 0.1 && Cancellation.handleDataType(pr, speed) <= 1.0) {
                        this.isCheckCancellation(dx, dz);
                        this.dG = true;
                    }
                    if (d >= 0.01 && d <= 4.3 && this.cJ > 0) {
                        this.isCheckCancellation(dx, dz);
                    }
                    if (this.checkCheckCancellation(to) && d < 0.1) {
                        this.isCheckCancellation(dx, dz);
                    }
                    if (this.protocol.predictedSlimeTicks > 0 && d < 2.75 && d > 0.01) {
                        this.isCheckCancellation(dx, dz);
                        --this.protocol.predictedSlimeTicks;
                    }
                    if (this.protocol.getVehicle() != null) {
                        this.isCheckCancellation(dx, dz);
                    }
                    pr = Math.sqrt(this.dy * this.dy + this.dz * this.dz);
                    d = Cancellation.handleDataType(pr, speed);
                    if (this.field0 > 0 && d > 1.0E-7 && d < 0.09) {
                        --this.field0;
                        this.isCheckCancellation(dx, dz);
                        d = Cancellation.handleDataType(pr, speed);
                    }
                    if (dx != this.dy || dz != this.dz) {
                        this.cZ += this.cC == 1 ? 4.0 : 1.0;
                        this.cY += d.5 + (this.cC == 1 ? 0.5 : (d > 0.04 ? 0.12 : 0.06));
                        if (this.cY > 0.05) {
                            this.du /= 2.0;
                            flagged = true;
                        }
                        if (this.cY > 1.0 || this.cY > 0.4 && this.cZ > 10.0) {
                            double difference = Math.max(this.hackType.getCheck().handleCheck("minimum_speed_difference", 1.0E-10), 1.0E-10);
                            if (this.getSpeedSimulation() != null) {
                                difference = Math.max(difference, 0.13);
                            }
                            if (d >= difference) {
                                this.A.cancel(1.d, "diff: " + d, this.protocol.getFromLocation(), 0, true);
                            }
                            this.cY -= 0.1;
                        }
                    }
                }
                if (this.dG) {
                    this.protocol.getComponentXZ().pistonTick = false;
                    this.dG = false;
                }
                if (this.cY > 0.0) {
                    this.cY -= 0.002;
                    this.cY /= 1.1;
                }
                if (this.cZ > 0.0) {
                    this.cZ -= 0.3;
                }
                if (this.cJ > 0) {
                    --this.cJ;
                }
                this.dA = dx;
                this.dB = dz;
                if (!flagged) {
                    this.dC = speed;
                }
                this.du /= 1.14;
                if (this.dD > 0) {
                    --this.dD;
                }
            }
        });
    }

    private ExtendedPotionEffect getSpeedSimulation() {
        return this.protocol.getPotionEffect(PotionEffectType.SPEED, 5L);
    }

    private double getSpeedSimulation2() {
        double m = 1.0;
        ExtendedPotionEffect speedEffect = this.getSpeedSimulation();
        boolean hasSpeed = speedEffect != null;
        Location l = this.protocol.getLocation().clone().add(0.0, -0.5, 0.0);
        Location lF = this.protocol.getFromLocation().clone().add(0.0, -0.5, 0.0);
        if (PlayerUtils.soulSpeed && this.protocol.getInventory().getBoots() != null) {
            int e = this.protocol.getInventory().getBoots().getEnchantmentLevel(Enchantment.SOUL_SPEED);
            double v = (double)(e + 1) * 0.4;
            m += v;
        }
        if (speedEffect != null) {
            m += (double)(speedEffect.bukkitEffect.getAmplifier() + 1) * 0.2;
        }
        if (this.getS(l) || this.isCheckPrevention(l) || this.getS(lF) || this.isCheckPrevention(lF)) {
            m *= 1.6;
        }
        if (this.protocol.getNearbyEntities(2.0).size() > 1) {
            m *= 1.4;
        }
        if (BlockUtils.areWebs(new ServerLocation(this.protocol.getLocation()).getBlock(this.protocol).getType())) {
            m *= 0.2;
        }
        if (this.W() || this.W()) {
            ExtendedPotionEffect dolphinEffect;
            ExtendedPotionEffect extendedPotionEffect = dolphinEffect = PlayerUtils.dolphinsGrace ? this.protocol.getPotionEffect(PotionEffectType.DOLPHINS_GRACE, 0L) : null;
            if (dolphinEffect != null) {
                m *= 6.2;
            }
        }
        if (this.getT(l)) {
            m *= 1.4;
        }
        if (this.protocol.isGliding()) {
            m *= 18.0;
        }
        if (this.protocol.isFlying() || this.protocol.flyingTicks > 0) {
            m *= 25.0;
        }
        return m *= (double)(this.protocol.bukkit().getWalkSpeed() * 5.0f);
    }

    private static double handleDataType(double x, double y) {
        return Math.abs(Math.abs(a) - Math.abs(b));
    }

    private void handleCheck(double motion, double allowedDev, CVector2D move) {
        CPlayerVelocityEvent claimed = null;
        for (CPlayerVelocityEvent velo : this.protocol.claimedVeloSpeed) {
            double pr;
            if (!this.checkCheckCancellation(motion, velo.getVelocity().getY())) continue;
            double speed = Math.sqrt(move.x * move.x + move.y * move.y);
            if (speed < (pr = Math.sqrt(velo.getVelocity().getX() * velo.getVelocity().getX() + velo.getVelocity().getZ() * velo.getVelocity().getZ())) * 2.5 + allowedDev) {
                this.dy = move.x;
                this.dz = move.y;
                this.dv = (pr + allowedDev) * 1.6;
            }
            claimed = velo;
            this.dp = true;
            break;
        }
        if (claimed != null) {
            this.protocol.claimedVeloSpeed.remove(claimed);
        }
    }

    private boolean checkCheckCancellation(double motion, double velo) {
        return Math.abs(Math.abs(motion) - Math.abs(velo)) < 0.08;
    }

    private void S() {
        this.A.call(() -> {
            this.field0 = 1;
            this.cC = 0;
        });
    }

    private void isCheckCancellation(boolean isOnGround) {
        this.A.call(() -> {
            this.cC = isOnGround ? 0 : this.cC + 1;
            this.dn = this.protocol.useItemPacket ? this.dn + 1 : 0;
            this.cD = isOnGround ? this.cD + 1 : 0;
        });
    }

    private void T() {
        this.A.call(() -> {
            this.dF = 10;
        });
    }

    private void R() {
        this.A.call(() -> {
            this.cJ = 2;
        });
    }

    private void isCheckCancellation(double x, double y) {
        this.dy = x;
        this.dz = z;
    }

    private boolean W() {
        return this.protocol.getEnvironment().isLiquid();
    }

    private boolean getS(Location location) {
        return this.protocol.getEnvironment().isIce();
    }

    private boolean handleCheck(Location ... l) {
        for (int dx = -2; dx <= 2; ++dx) {
            for (dy <= 2; ++dy) {
                for (int dz = -2; dz <= 3; ++dz) {
                    for (Location location : l) {
                        double x = location.getX();
                        double y = location.getY();
                        double z = location.getZ();
                        Material material = new ServerLocation(this.protocol.getWorld(), x.5, y.5, z.5).getBlock(this.protocol).getTypeOrNull();
                        if ((material == null || !BlockUtils.isSemiSolid(material) && !BlockUtils.isSolid(material) && !material.toString().contains("GRASS")) && !BlockUtils.isLiquid(material)) continue;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkCheckCancellation(Location location) {
        double x = location.getX();
        double y = location.getY() - 0.1;
        double z = location.getZ();
        for (int dx = -2; dx <= 2; ++dx) {
            for (double dy = 1.0; dy <= 2.0; dy += 0.5) {
                for (int dz = -2; dz <= 2; ++dz) {
                    Material material = new ServerLocation(this.protocol.getWorld(), x.5, y + dy, z.5).getBlock(this.protocol).getTypeOrNull();
                    if (material == null || !BlockUtils.isSolid(material)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getT(Location location) {
        return this.protocol.getEnvironment().isSemi();
    }

    private boolean isCheckPrevention(Location location) {
        return this.protocol.getEnvironment().isSlime();
    }

    private boolean isCheckPrevention2(Location location) {
        return this.protocol.getEnvironment().isSlimeWide();
    }
}

