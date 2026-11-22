/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Boat
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.vehicle.VehicleEnterEvent
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.check.implementation.movement;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.event.CPlayerRiptideEvent;
import ai.idealistic.spartan.abstraction.event.CPlayerVelocityEvent;
import ai.idealistic.spartan.abstraction.event.PlayerTickEvent;
import ai.idealistic.spartan.abstraction.protocol.ExtendedPotionEffect;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.CheckConditions;
import ai.idealistic.spartan.listeners.protocol.TeleportListener;
import ai.idealistic.spartan.utils.math.RayUtils;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import ai.idealistic.spartan.utils.minecraft.entity.PotionEffectUtils;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;

public class Track
extends CheckRunner {
    private final CheckDetection A = new ImplementedDetection(this, null, null, null, true);
    private int cC = 0;
    private int cD = 0;
    private int cE = 0;
    private int cF = 0;
    private int cG = 0;
    private int cH = 0;
    private int cI = 0;
    private int cJ = 0;
    private int cK = 0;
    private int cL = 0;
    private int cM = 0;
    private int cN = 0;
    private int cO = 0;
    private boolean slime = false;
    private boolean cP = false;
    private boolean trident = false;
    private boolean cQ = false;
    private boolean cR = false;
    private boolean cS = false;
    private boolean cT = false;
    private int cU = 0;
    private int cV = 0;
    private int cW = 0;
    private int cX = 0;
    private double cY = 0.0;
    private double cZ = 0.0;
    private int da = 0;
    private double db = 0.0;
    private double dc = 0.0;
    private double dd = 0.0;
    private int de = 0;
    private double df = 0.0;

    public Track(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof PlayerTeleportEvent) {
            this.S();
        } else if (object instanceof CPlayerRiptideEvent) {
            this.T();
        } else if (object instanceof PlayerMoveEvent) {
            this.run();
        } else if (object instanceof PlayerTickEvent) {
            PlayerTickEvent e = (PlayerTickEvent)object;
            this.getS(e.onGround);
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
        for (Entity entity : p.getNearbyEntities(2.0)) {
            if (!(entity instanceof Boat)) continue;
            this.cO = NumberConversions.ceil((double)20.0);
            return true;
        }
        if (this.cO > 0) {
            --this.cO;
            return true;
        }
        if (p.wasGliding() || p.getVehicle() != null) {
            return true;
        }
        if (p.getTimePassed(p.magmaCubeWater) <= 1000L || p.getTimePassed(p.soulSandWater) <= 1000L) {
            return true;
        }
        for (int i = 0; i < NumberConversions.ceil((double)p.getEyeHeight()); ++i) {
            Material m = new ServerLocation(p.getLocation().clone().add(0.0, (double)i, 0.0)).getBlock(this.protocol).getType();
            if (!BlockUtils.areInteractiveBushes(m) && !BlockUtils.areWebs(m)) continue;
            return true;
        }
        return false;
    }

    void run() {
        this.A.call(() -> {
            PlayerProtocol p = this.protocol;
            if (this.handleCheck(p)) {
                this.cC = 0;
                this.cD = 0;
                this.cY = 0.0;
                this.cZ = 0.0;
                this.updateBackTrack(0.0);
                this.cG = 0;
                this.cL = 0;
                this.slime = false;
                this.cP = false;
                this.trident = false;
                this.cQ = false;
                this.cR = false;
                this.cS = false;
                this.cT = false;
                this.cU = 0;
                this.cV = 0;
                this.cW = 0;
                this.cX = 0;
                this.da = 0;
                this.db = 0.0;
                this.dc = 0.0;
                this.dd = 0.0;
                this.de = 0;
                this.df = 0.0;
                return;
            }
            boolean ground = this.protocol.getVehicle() != null ? this.protocol.getVehicle().isOnGround() : this.protocol.isOnGround();
            Location location =  p.getLocation();
            Location from = this.protocol.getFromLocation();
            double dx = to.getX() - from.getX();
            double dz = to.getZ() - from.getZ();
            double speed = Math.sqrt(dx * dx + dz * dz);
            if (to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()) {
                double pr;
                ExtendedPotionEffect slowFalling;
                double waterLim;
                double waterLimClimb;
                boolean slimeEvent;
                double pr2;
                double jumpStartMotionY;
                double motion = to.getY() - from.getY();
                double gravity = 0.08;
                jumpStartMotionY = this.isGravitySimulation() && this.U() * 1.5 > motion ? motion : this.U();
                if (BlockUtils.areWebs(new ServerLocation(to).getBlock(this.protocol).getType())) {
                    jumpStartMotionY /= 10.0;
                }
                boolean isOnWater = this.W();
                if (this.isGravitySimulation3()) {
                    this.cS = true;
                }
                if (ground) {
                    if (this.protocol.getLocation().getY() > this.dc) {
                        this.cR = true;
                    }
                    if (this.cD != 1) {
                        this.updateBackTrack(0.0);
                        if (this.isCheckPrevention(motion) < 0.08 && this.isCheckPrevention(motion) > 0.0 && (this.checkCheckCancellation(to) || this.isGravitySimulation3() || this.Z())) {
                            this.updateBackTrack(motion);
                        }
                    } else {
                        pr2 = this.df -= gravity;
                        pr2 *= (double)0.98f;
                        this.updateBackTrack(0.0);
                        if (motion > pr2 && motion < 0.0) {
                            this.updateBackTrack(motion);
                        } else if (this.cQ && motion < 0.5) {
                            this.updateBackTrack(motion);
                        }
                        if (this.isGravitySimulation3()) {
                            this.slime = true;
                        }
                    }
                    if (this.Y() || this.Y()) {
                        this.da = 2;
                    }
                    if (this.da > 0 && motion != 0.0 && motion < 0.785) {
                        this.updateBackTrack(motion);
                        --this.da;
                    }
                    this.dc = this.protocol.getLocation().getY();
                } else if (this.cC == 1) {
                    if (motion > 0.0) {
                        this.updateBackTrack((float)jumpStartMotionY);
                    } else {
                        this.updateBackTrack(-gravity * (double)0.98f);
                    }
                    if (this.slime) {
                        this.cV = 3;
                    }
                    if (this.cV > 0) {
                        pr2 = this.U() + this.isCheckPrevention(this.db) * 0.09;
                        if (motion == 0.0) {
                            this.updateBackTrack(0.0);
                        } else if (pr2 > motion) {
                            this.updateBackTrack(motion);
                        }
                        --this.cV;
                    }
                } else {
                    this.cQ = this.cC == 14;
                    this.df -= gravity;
                    this.df *= (double)0.98f;
                    if (this.df < -3.92) {
                        this.df = -3.92;
                    }
                    this.slime = false;
                }
                if (p.getComponentY().pistonTick) {
                    this.cW = 2;
                }
                if (this.computeGravitySimulation(motion) > 0.01 && p.getComponentY().pistonTick && motion <= 0.25) {
                    this.updateBackTrack(motion);
                    p.getComponentY().pistonTick = false;
                }
                if (this.cW > 0) {
                    --this.cW;
                }
                if (this.computeGravitySimulation(motion) > 0.01 && p.getComponentY().pistonTick && motion <= 1.0) {
                    this.updateBackTrack(motion);
                    p.getComponentY().pistonTick = false;
                }
                slimeEvent = this.computeGravitySimulation(motion) > 0.001 && p.getComponentY().pistonHandle && this.cP;
                if (slimeEvent) {
                    p.getComponentY().pistonHandle = false;
                    this.cU = 4;
                }
                if ((this.Y() || this.Y() || this.isGravitySimulation5()) && motion != 0.0 && motion < 0.51) {
                    this.updateBackTrack(motion);
                }
                pr2 = this.isCheckPrevention(this.isCheckPrevention(motion) - this.isCheckPrevention(this.V()));
                if (!this.protocol.claimedVeloGravity.isEmpty() && pr2 > 0.001) {
                    this.isFastClicks(motion);
                }
                double devPreTotal2 = this.isCheckPrevention(this.isCheckPrevention(motion) - this.isCheckPrevention(this.V()));
                if (this.cI > 0 && devPreTotal2 > 0.001 && motion < 0.118 && motion > -0.16) {
                    this.updateBackTrack(motion);
                    --this.cI;
                } else if (devPreTotal2 > 0.001 && motion < 0.118 && motion > -0.16 && this.isGravitySimulation2()) {
                    this.updateBackTrack(motion);
                    this.cI = 2;
                }
                double dev1 = this.isCheckPrevention(this.isCheckPrevention(motion) - this.isCheckPrevention(this.V()));
                if (dev1 > 0.008 && this.checkCheckCancellation(to)) {
                    if (motion < this.V() && motion > -0.08) {
                        this.updateBackTrack(motion);
                        this.cE = 3;
                        this.cF = 0;
                    }
                } else if (dev1 > 0.008 && this.cE > 0) {
                    if (motion < this.V() && motion > -0.08) {
                        this.updateBackTrack(motion);
                        --this.cE;
                        this.cF = 0;
                    }
                } else if (this.cF > 18 && this.cE > 0) {
                    --this.cE;
                    this.cF = 0;
                }
                boolean onWaterBubble = this.X();
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) && this.protocol.bukkit().isSwimming()) {
                    this.cN = 2;
                } else if (this.cN > 0) {
                    --this.cN;
                }
                waterLimClimb = this.isCheckCancellation(to) ? 0.4 : 0.2;
                double d3 = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) ? (this.cN > 0 ? 0.4 : waterLimClimb) : (waterLim = waterLimClimb);
                if (dev1 > 0.01 && isOnWater && motion > -3.92) {
                    double d4 = onWaterBubble ? 0.8 : waterLim;
                    if (motion < d4) {
                        this.updateBackTrack(motion);
                        int n = this.cX = onWaterBubble ? 8 : 5;
                    }
                }
                if (this.isCheckPrevention(this.isCheckPrevention(motion) - this.isCheckPrevention(this.V())) > 0.01 && this.trident && motion < 4.2) {
                    this.updateBackTrack(motion);
                }
                if (this.cX > 0 && this.isCheckPrevention(this.isCheckPrevention(motion) - this.isCheckPrevention(this.V())) < 0.14) {
                    --this.cX;
                    this.updateBackTrack(motion);
                }
                if (PlayerUtils.levitation && ((slowFalling = this.protocol.getPotionEffect(PotionEffectType.LEVITATION, 0L)) != null || this.cG > 0)) {
                    if (slowFalling != null) {
                        this.cG = 2;
                        pr = 0.05 * (double)slowFalling.bukkitEffect.getAmplifier() + 1.0;
                        this.cL = slowFalling.bukkitEffect.getAmplifier() + 1;
                    } else {
                        pr = 0.05 * (double)this.cL;
                    }
                    if (motion < pr && motion >= 0.0) {
                        this.updateBackTrack(motion);
                        this.db = 0.0;
                    }
                    if (this.cG > 0) {
                        --this.cL;
                    }
                }
                if (PlayerUtils.slowFall && ((slowFalling = this.protocol.getPotionEffect(PotionEffectType.SLOW_FALLING, 0L)) != null || this.cK > 0)) {
                    if (slowFalling != null) {
                        this.cK = 2;
                        pr = 0.02 * (double)slowFalling.bukkitEffect.getAmplifier() + 0.5;
                        this.cM = slowFalling.bukkitEffect.getAmplifier() + 1;
                    } else {
                        pr = 0.02 * (double)this.cM.5;
                    }
                    if (motion < pr && motion < 0.0) {
                        this.updateBackTrack(motion);
                    }
                    if (this.cK > 0) {
                        --this.cM;
                    }
                }
                if (this.protocol.isFlying() || this.protocol.isGliding() || this.protocol.flyingTicks > 0) {
                    this.updateBackTrack(motion);
                    this.cG = 3;
                    this.cL = 3;
                }
                if (!ground) {
                    if (this.cC == 1) {
                        if (motion < 0.0) {
                            this.db = motion;
                        }
                    } else {
                        this.db = motion < 0.0 ? (this.db += motion) : 0.0;
                    }
                }
                double devPreTotal = this.isCheckPrevention(this.isCheckPrevention(motion) - this.isCheckPrevention(this.V()));
                if (this.cH > 0 && devPreTotal > 0.001 && motion < 0.0 && motion > -0.16) {
                    this.updateBackTrack(motion);
                    --this.cH;
                } else if (devPreTotal > 0.001 && motion < 0.0 && motion > -0.16 && this.Z()) {
                    this.updateBackTrack(motion);
                    this.cH = 2;
                }
                if (this.computeGravitySimulation(motion) > 0.001 && this.cU > 0 && motion > 0.0 && motion < 1.52) {
                    this.updateBackTrack(motion);
                    this.cT = true;
                    --this.cU;
                }
                if (this.computeGravitySimulation(motion) > 0.001 && this.de > 0 && this.dd > 0.0 && this.computeGravitySimulation(motion) <= this.dd) {
                    this.updateBackTrack(motion);
                }
                --this.de;
                if (this.de == 0) {
                    this.dd = 0.0;
                }
                if (this.protocol.getVehicle() != null) {
                    this.updateBackTrack(motion);
                }
                if (!this.protocol.isUsingVersionOrGreater(MultiVersion.MCVersion.V1_19) && this.cC > 5 && this.cC < 10 && motion < this.V() + 0.04 && speed < 0.1) {
                    double allowedDeviantity = 0.076;
                    PotionEffectType[] jumpEffect = this.protocol.getPotionEffect(PotionEffectUtils.JUMP, 1L);
                    if (jumpEffect != null && (allowedDeviantity += 0.04 * (double)(jumpEffect.bukkitEffect.getAmplifier() + 1)) > 0.16) {
                        allowedDeviantity = 0.16;
                    }
                    if (this.computeGravitySimulation(motion) < allowedDeviantity) {
                        this.updateBackTrack(motion);
                    }
                }
                if (this.protocol.getComponentY().explosionTick && this.computeGravitySimulation(motion) < 1.2) {
                    this.updateBackTrack(motion);
                    this.protocol.getComponentY().explosionTick = false;
                }
                if (this.cS && this.V() > motion && this.computeGravitySimulation(motion) <= 0.08) {
                    this.updateBackTrack(motion);
                    this.cS = false;
                }
                if (this.cT && this.V() > motion && this.computeGravitySimulation(motion) <= 0.1) {
                    this.updateBackTrack(motion);
                    this.cT = false;
                }
                if (this.protocol.predictedSlimeTicks > 0 && this.computeGravitySimulation(motion) < 2.5 && this.computeGravitySimulation(motion) > 0.01) {
                    this.updateBackTrack(motion);
                    this.cS = true;
                    --this.protocol.predictedSlimeTicks;
                }
                double devTotal = this.isCheckPrevention(this.isCheckPrevention(motion) - this.isCheckPrevention(this.V()));
                double d5 = this.protocol.getVehicle() != null && motion < 0.0 ? 0.36 : (this.protocol.getVehicle() != null ? 0.09 : 0.009);
                if (devTotal > d5) {
                    if (devTotal < 1.9 && this.cJ > 0) {
                        this.updateBackTrack(0.0);
                    } else if (this.protocol.getVehicle() == null) {
                        if (this.cR && this.cC == 1 && this.V() > motion && this.computeGravitySimulation(motion) <= 0.03) {
                            return;
                        }
                        if (this.handleDataType(from.clone().add(0.0, 0.5, 0.0)) && to.distance(from) < 0.3) {
                            return;
                        }
                        if (devTotal < 0.1) {
                            for (PotionEffectType type : new PotionEffectType[]{PlayerUtils.levitation ? PotionEffectType.LEVITATION : null, PlayerUtils.slowFall ? PotionEffectType.SLOW_FALLING : null, PotionEffectUtils.JUMP}) {
                                if (type == null || !p.hasPotionEffect(type, 5L)) continue;
                                return;
                            }
                        }
                        this.cY += devTotal;
                        this.cZ += 1.0;
                        double d6 = this.protocol.getVehicle() != null ? 0.6 : 0.05;
                        if (this.cY > d6) {
                            double difference;
                            StringBuilder alertBuilder = new StringBuilder(String.valueOf(RayUtils.scaleVal(devTotal, 6.0)));
                            if (devTotal < 1.0) {
                                alertBuilder.deleteCharAt(0);
                            }
                            int n = this.cC == 1 ? 0 : (this.cY > 0.3 ? 0 : (this.cY > 0.16 ? 1 : 3));
                            if (this.cZ > (double)n && devTotal > (difference = Math.max(this.hackType.getCheck().handleCheck("minimum_gravity_difference", 0.0), 0.0))) {
                                this.A.cancel(1.devTotal, "y: " + alertBuilder + ", ry: " + devTotal, this.protocol.getFromLocation());
                            }
                        }
                        this.updateBackTrack(motion);
                        if (this.cY > 0.0) {
                            this.cY -= 0.004;
                        }
                        if (this.cZ > 0.0) {
                            this.cZ -= 0.3;
                        }
                        this.cZ /= 1.08;
                        this.cY /= this.cZ < 4.0 ? 1.17 : 1.04;
                    }
                } else {
                    ++this.cF;
                    this.cY /= 1.01;
                }
            }
            boolean bl = this.cP = this.isGravitySimulation4() || this.isGravitySimulation4();
            if (this.cJ > 0) {
                --this.cJ;
            }
        });
    }

    void R() {
        this.A.call(() -> {
            this.cJ = 2;
        });
    }

    private double computeGravitySimulation(double motion) {
        return this.isCheckPrevention(this.isCheckPrevention(motion) - this.isCheckPrevention(this.V()));
    }

    private void isFastClicks(double motion) {
        CPlayerVelocityEvent claimed = null;
        for (CPlayerVelocityEvent velo : this.protocol.claimedVeloGravity) {
            if (!this.checkCheckCancellation(motion, velo.getVelocity().getY())) continue;
            this.updateBackTrack(motion);
            this.cS = true;
            claimed = velo;
            break;
        }
        if (claimed != null) {
            this.protocol.claimedVeloGravity.remove(claimed);
        }
    }

    private boolean checkCheckCancellation(double motion, double velo) {
        return this.isCheckPrevention(this.isCheckPrevention(motion) - this.isCheckPrevention(velo)) < 0.08;
    }

    void S() {
        this.A.call(() -> {
            this.cC = 0;
            this.cD = 0;
            this.dd = 0.35;
            this.de = CheckDetection.getLastPrevention() <= 100L ? 1 : 5;
            this.cY /= 1.5;
            this.updateBackTrack(0.0);
        });
    }

    private void isCheckCancellation(boolean isOnGround) {
        boolean ground = this.protocol.getVehicle() != null ? this.protocol.getVehicle().isOnGround() : isOnGround;
        this.cD = ground ? this.cD + 1 : 0;
        this.cC = ground ? 0 : this.cC + 1;
    }

    void getS(boolean isOnGround) {
        this.A.call(() -> this.isCheckCancellation(isOnGround));
    }

    void T() {
        this.A.call(() -> {
            this.trident = true;
            this.cG = 13;
            this.cL = 4;
        });
    }

    private double U() {
        double jumpPower = 0.42;
        ExtendedPotionEffect jumpEffect = this.protocol.getPotionEffect(PotionEffectUtils.JUMP, 1L);
        if (jumpEffect != null) {
            jumpPower += 0.(double)(jumpEffect.bukkitEffect.getAmplifier() + 1);
        }
        return jumpPower;
    }

    private double isCheckPrevention(double v) {
        return Math.abs(v);
    }

    private double V() {
        return this.df;
    }

    private void updateBackTrack(double v) {
        this.df = v;
    }

    private boolean W() {
        return this.protocol.getEnvironment().isLiquid();
    }

    private boolean X() {
        return this.protocol.getEnvironment().isBubble();
    }

    private boolean Y() {
        return this.protocol.getEnvironment().isSemi();
    }

    private boolean Z() {
        return this.protocol.getEnvironment().isSemi();
    }

    private boolean isGravitySimulation() {
        return this.protocol.getEnvironment().isJumpModify();
    }

    private boolean isGravitySimulation2() {
        return this.protocol.getEnvironment().isClimb();
    }

    private boolean isGravitySimulation3() {
        return this.protocol.getEnvironment().isSlime();
    }

    private boolean isGravitySimulation4() {
        return this.protocol.getEnvironment().isSlimeWide();
    }

    private boolean isGravitySimulation5() {
        return this.protocol.getEnvironment().isSlimeHeight();
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

    private boolean handleDataType(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -1; dz <= 1; ++dz) {
                    Material material = new ServerLocation(this.protocol.getWorld(), x.3, y.3, z.3).getBlock(this.protocol).getTypeOrNull();
                    if (material == null || !BlockUtils.isSolid(material)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCheckCancellation(Location location) {
        double x = location.getX();
        double y = location.getY() - 0.1;
        double z = location.getZ();
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -1; dz <= 1; ++dz) {
                    Material material = new ServerLocation(this.protocol.getWorld(), x + (double)dx, y + (double)dy, z + (double)dz).getBlock(this.protocol).getTypeOrNull();
                    if (material == null || !BlockUtils.isSolid(material)) continue;
                    return true;
                }
            }
        }
        return false;
    }
}

