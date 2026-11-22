/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Pose
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.event.PlayerAttackEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.functionality.tracking.CheckConditions;
import ai.idealistic.spartan.listeners.bukkit.standalone.ChunksEvent;
import ai.idealistic.spartan.utils.java.ConcurrentList;
import ai.idealistic.spartan.utils.math.ClientMath;
import ai.idealistic.spartan.utils.math.Sin;
import ai.idealistic.spartan.utils.math.RayUtils;
import ai.idealistic.spartan.utils.math.VanillaMath;
import ai.idealistic.spartan.utils.math.statistics.Expander;
import ai.idealistic.spartan.utils.minecraft.entity.AxisMatrix;
import ai.idealistic.spartan.utils.minecraft.entity.MovingObjectPosition;
import ai.idealistic.spartan.utils.minecraft.vector.Vec3;
import ai.idealistic.spartan.utils.minecraft.world.BoundingBoxUtil;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;

public class HitReach
extends CheckRunner {
    private final CheckDetection Q;
    private final CheckDetection R;
    private final CheckDetection S;
    private static final boolean T = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_14);
    private final List<Double> U = new ConcurrentList<Double>();
    private int V = 0;
    private static final boolean[] BOOLEANS = new boolean[]{true, false};
    private static final double W = 3.001;
    private static final double aa = 10.0;
    private double ab = -1.0;
    private double ac = 0.0;
    private double ad = 0.0;
    private final Sin ae = new Sin();
    private final VanillaMath af = new VanillaMath();

    public HitReach(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
        this.Q = new ImplementedDetection(this, null, null, "hitbox_raytrace", true, 2000L, 30000L, 40000L, 60000L);
        this.S = new ImplementedDetection(this, null, null, "simple", true, 2000L, 30000L, 40000L, 60000L);
        this.R = new ImplementedDetection(this, null, null, "throughblocks", true, 2000L, 30000L, 40000L, 60000L);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof PlayerAttackEvent) {
            PlayerAttackEvent event = (PlayerAttackEvent)object;
            if (!(event.target instanceof Player)) {
                return;
            }
            if (event.target.getVehicle() != null || this.protocol.getVehicle() != null) {
                return;
            }
            if (this.protocol.packetsEnabled()) {
                if (CheckConditions.canCheckCombat(this.protocol, (LivingEntity)PluginBase.getProtocol((Player)event.target).bukkit()) && !this.protocol.getGameMode().equals(GameMode.CREATIVE)) {
                    double dev;
                    boolean is1_8;
                    PlayerProtocol target = PluginBase.getProtocol((Player)event.target);
                    List<Location> h = target.getPositionHistory();
                    HashSet<AxisMatrix> cachedBlocks = new HashSet<AxisMatrix>();
                    HashSet ignored = new HashSet();
                    Location hardcodedTargetLoc = target.getLocation().clone();
                    Location hardcodedPlayerLoc = this.protocol.getLocation().clone();
                    if (HitReach.handleCheck(hardcodedPlayerLoc, hardcodedTargetLoc) > 0.5) {
                        Location location =  target.getLocation();
                        for (int dx = -3; dx <= 3; ++dx) {
                            for (int dy = -3; dy <= 3; ++dy) {
                                for (int dz = -3; dz <= 3; ++dz) {
                                    List<AxisMatrix> bb;
                                    Location location = new Location(target.bukkit().getWorld(), t.getX() + (double)dx, t.getY() + (double)dy, t.getZ() + (double)dz);
                                    Block block = ChunksEvent.getBlockAsync(location);
                                    if (block == null) continue;
                                    Material material = block.getType();
                                    List<AxisMatrix> list = bb = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) ? BoundingBoxUtil.getBoundingBoxes(block) : BoundingBoxUtil.getBoundingBoxesLegacy(block);
                                    if (!material.isSolid() || bb == null) continue;
                                    cachedBlocks.addAll(bb);
                                }
                            }
                        }
                    }
                    float hitbox = (is1_8 = this.protocol.isUsingVersion(MultiVersion.MCVersion.V1_8)) ? 0.4f : 0.3f;
                    AtomicReference<Double> minDistance = new AtomicReference<Double>(DoubleDouble.MAX_VALUE);
                    HashSet<Double> minBlockDistanceSet = new HashSet<Double>();
                    for (boolean fastMath : BOOLEANS) {
                        ClientMath buildSpeed = fastMath ? this.ae : this.af;
                        Location location =  this.protocol.getLocation();
                        Location fromLocation =  this.protocol.getFromLocation();
                        Vec3[] possibleEyeRotation = new Vec3[]{HitReach.handleCheck(l.getYaw(), l.getPitch(), buildSpeed), HitReach.handleCheck(lf.getYaw(), lf.getPitch(), buildSpeed), HitReach.handleCheck(lf.getYaw(), lf.getPitch(), buildSpeed)};
                        for (boolean sneaking : BOOLEANS) {
                            for (Vec3 eyeRotation : possibleEyeRotation) {
                                Vec3 eyePos = new Vec3(l.getX(), l.getY() + (double)this.checkCheckCancellation(sneaking), l.getZ());
                                Vec3 endReachRay = eyePos.addVector(eyeRotation.xCoord * 6.0, eyeRotation.yCoord * 6.0, eyeRotation.zCoord * 6.0);
                                LinkedList<Location> positions = new LinkedList<Location>h;
                                positions.add(target.getLocation());
                                positions.add(target.getFromLocation());
                                positions.add(target.bukkit().getLocation().clone());
                                for (Location position : positions) {
                                    double range;
                                    AxisMatrix axisAlignedBB = new AxisMatrix(position.getX() - (double)hitbox, position.getY() - (double)0.1f, position.getZ() - (double)hitbox, position.getX() + (double)hitbox, position.getY() + (double)1.9f, position.getZ() + (double)hitbox);
                                    MovingObjectPosition intercept = axisAlignedBB.calculateIntercept(eyePos, endReachRay);
                                    if (intercept == null || !((range = intercept.hitVec.distanceTo(eyePos)) < minDistance.get())) continue;
                                    minDistance.set(range);
                                }
                                double minBlockDist = Double.MAX_VALUE;
                                for (AxisMatrix block : cachedBlocks) {
                                    Vec3 e;
                                    Vec3 s;
                                    MovingObjectPosition intercept = block.calculateIntercept(s = new Vec3(l.getX(), l.getY() + (double)this.handleDataType(sneaking), l.getZ()), e = s.addVector(eyeRotation.xCoord * 6.0, eyeRotation.yCoord * 6.0, eyeRotation.zCoord * 6.0));
                                    double d = intercept != null ? intercept.hitVec.distanceTo(eyePos) : 0.0;
                                    double v = d;
                                    if (!(v < minDistance.get()) || v >= minBlockDist || intercept == null || !(v < 6.0)) continue;
                                    minBlockDist = v;
                                }
                                minBlockDistanceSet.add(minBlockDist);
                            }
                        }
                    }
                    if (minDistance.get() > 10.0) {
                        double d;
                        this.ab += 1.0;
                        if (d > 3.0) {
                            this.Q.cancel("Missing HitBox of " + target.bukkit().getName());
                            event.setCancelled(true);
                        }
                    } else {
                        this.ab = Math.max(this.ab - 5.0, 0.0);
                    }
                    if (minDistance.get() > 3.001 && minDistance.get() < 10.0) {
                        double d = this.ac;
                        this.ac = d + 1.0;
                        if (d > 2.5) {
                            this.Q.cancel("Distance " + minDistance.get());
                            event.setCancelled(true);
                        }
                    } else if (minDistance.get() < 3.001 && this.ac > 0.0) {
                        this.ac -= 0.05;
                    }
                    if ((dev = Expander.getMax(minBlockDistanceSet) + 0.3.003 - minDistance.get()) < -0.669 && RayUtils.scaleVal(Math.abs(dev), 6.0) < 10.0) {
                        this.ad += 1.0;
                        this.R.cancel("Hitting through block (dev: " + RayUtils.scaleVal(Math.abs(dev), 6.0) + ")");
                        event.setCancelled(true);
                    } else if (this.ad > 0.0) {
                        this.ad -= 0.5;
                    }
                }
            } else {
                LivingEntity target = event.target;
                if (CheckConditions.canCheckCombat(this.protocol, target) && !this.protocol.getGameMode().equals(GameMode.CREATIVE)) {
                    double bruteforceRayTrace = RayUtils.bruteforceRayTrace(this.protocol, (Entity)target);
                    double diff = bruteforceRayTrace.4;
                    this.U.add(bruteforceRayTrace);
                    if (this.U.size() >= 10) {
                        this.handleCheck(this.protocol.getLocation(), bruteforceRayTrace);
                    }
                }
            }
        }
    }

    private static Vec3 handleCheck(float yaw, float pitch, ClientMath clientMath) {
        float f = clientMath.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f1 = clientMath.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f2 = -clientMath.cos(-pitch * ((float)Math.PI / 180));
        float f3 = clientMath.sin(-pitch * ((float)Math.PI / 180));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    private float checkCheckCancellation(boolean sneaking) {
        float eyeHeight = 1.62f;
        if (sneaking) {
            eyeHeight -= 0.08f;
        }
        if (T && this.protocol.bukkit().getPose().equals(Pose.SWIMMING)) {
            eyeHeight -= 1.0f;
        }
        return eyeHeight;
    }

    private float handleDataType(boolean sneaking) {
        float eyeHeight;
        if (T && this.protocol.bukkit().getPlayer().getPose().equals(Pose.SWIMMING)) {
            eyeHeight = 0.4f;
        } else {
            eyeHeight = 1.62f;
            if (sneaking) {
                eyeHeight -= 0.35f;
            }
        }
        return eyeHeight;
    }

    private static double handleCheck(Location loc1, Location loc2) {
        double deltaX = loc1.getX() - loc2.getX();
        double deltaY = loc1.getY() - loc2.getY();
        double deltaZ = loc1.getZ() - loc2.getZ();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    private void handleCheck(Location to, double bruteForce) {
        String punish = null;
        double globalValue = 0.0;
        for (Double value : this.U) {
            globalValue += value.(double)Value();
        }
        double result = globalValue / (double)this.U.size();
        double d = this.protocol.packetsEnabled() ? 0.43 : 0.5;
        if (result > d) {
            punish = "simple";
        }
        int percent = (int)(result / 0.400.0);
        if (punish != null) {
            this.V += 100;
            this.S.cancel(punish + ", hitbox-size-analysis: " + result + ", hitbox-size-last: " + bruteForce + ", percent-of-deviation: " + percent + "%, reach: " + RayUtils.scaleVal(result / 0.12.0, 2.0) + " blocks", to);
        } else if (this.V > 0) {
            this.V -= 50;
        }
        this.U.clear();
    }

    @Override
    protected boolean canRun() {
        return CheckConditions.canCheckCombat(this.protocol);
    }
}

