/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  ai.idealistic.spartan.abstraction.event.RotationEvent
 *  org.bukkit.Location
 *  org.bukkit.entity.Creeper
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Skeleton
 *  org.bukkit.entity.Zombie
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.AttackData;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.AttackHandle;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.PriorityData;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.PriorityType;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.Analysis;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.MathEntropy;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.AimConstantCheck;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.Delta;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.Statistics;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.HeuristicComponent;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.Pair;
import ai.idealistic.spartan.abstraction.event.RotationEvent;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.utils.java.ConcurrentList;
import ai.idealistic.spartan.utils.math.RayUtils;
import ai.idealistic.spartan.utils.minecraft.vector.CVector2F;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.util.Vector;

public class KAMoveDirection
extends CheckDetection {
    private final HeuristicComponent[] aM;
    private final CheckDetection aN = this.handleDataType("move_direction_1");
    private final CheckDetection aO = this.handleDataType("move_direction_2");
    private AttackHandle aP;
    private final List<AttackData> aQ;
    private float aj;
    private float aR;
    private String aS;
    private String aT;
    private final List<UUID> aU;
    private final List<Double> U;
    private int aV = 0;

    public KAMoveDirection(CheckRunner executor) {
        super(executor, null, null, null, true);
        CheckDetection aimConstant = this.handleDataType("aim_constant");
        CheckDetection aimSmooth = this.handleDataType("aim_smooth");
        CheckDetection aimComplex = this.handleDataType("aim_complex");
        CheckDetection aimStatistics = this.handleDataType("aim_statistics");
        CheckDetection aimAnalysis = this.handleDataType("aim_analysis");
        long punishTime = 70000L;
        ImplementedDetection aimML = new ImplementedDetection(this, "aim_machine_learning", true, punishTime - 1000L, 30000L, 40000L, punishTime);
        this.aM = new HeuristicComponent[]{new AimConstantCheck(aimConstant, this.protocol), new Delta(aimSmooth, this.protocol), new MathEntropy(aimComplex, this.protocol), new Statistics(aimStatistics, this.protocol), new Analysis(aimAnalysis, this.protocol), new Pair(aimML, this.protocol)};
        this.aQ = new ConcurrentList<AttackData>();
        this.aP = new AttackHandle(null, System.currentTimeMillis());
        this.aj = 0.0f;
        this.aR = 0.0f;
        this.aS = "";
        this.aT = "";
        this.aU = new ConcurrentList<UUID>();
        this.U = new ConcurrentList<Double>();
    }

    private CheckDetection handleDataType(String name) {
        return new ImplementedDetection(this, name, true, 2000L, 30000L, 40000L, 60000L);
    }

    public void handleCheck(RotationEvent event) {
        this.call(() -> {
            if (this.aP.time + 3000L > System.currentTimeMillis()) {
                for (HeuristicComponent component : this.aM) {
                    component.process(event);
                }
            }
        });
    }

    public void run() {
        this.call(() -> {
            Entity target;
            if (this.aP.time + 3000L > System.currentTimeMillis() && (target = this.aP.aL) != null) {
                Location targetLoc = ProtocolLib.getLocationOrNull(target);
                if (targetLoc == null) {
                    return;
                }
                double bruteforceRayTrace = RayUtils.bruteforceRayTrace(this.protocol, target);
                double diff = bruteforceRayTrace.4;
                double predict = RayUtils.castTo360(KAMoveDirection.handleCheck((Vector)this.protocol.getLocation().toVector(), (Vector)new ServerLocation((Location)targetLoc).toVector()).x);
                double yaw360 = RayUtils.castTo360(this.protocol.getLocation().getYaw());
                this.U.add(bruteforceRayTrace);
                this.aQ.add(new AttackData(Math.abs(predict - yaw360), this.protocol.getLocation().getYaw(), this.protocol.getLocation().getPitch(), System.currentTimeMillis(), ProtocolLib.getUUID(target)));
                if (this.U.size() >= 10) {
                    this.updateMoveDirection();
                }
            }
        });
    }

    private void updateMoveDirection() {
        int sens;
        this.aU.clear();
        this.aS = "";
        this.aT = "";
        ArrayList<Double> yaws = new ArrayList<Double>();
        double oldYawResult = this.aQ.get(0).yaw;
        double oldPitchResult = this.aQ.get(0).pitch;
        double oldYawChange = Math.abs((double)this.aQ.get(0).yaw - oldYawResult);
        double yawChangeFirst = Math.abs(this.aQ.get(0).yaw - this.aQ.get(1).yaw);
        int machineKnownMovement = 0;
        int constantRotations = 0;
        int gcd = 0;
        int aggressivePatternI = 0;
        int aggressivePatternD = 0;
        int aggressivePatternI2 = 0;
        int aggressivePatternD2 = 0;
        int robotizedAmount = 0;
        int aggressiveAim = 0;
        int infinitives = 0;
        int randomizerFlaw = 0;
        for (AttackData value : this.aQ) {
            double interpolation;
            if (!this.aU.contains(value.aK)) {
                this.aU.add(value.aK);
            }
            double yawChange = Math.abs((double)value.yaw - oldYawResult);
            double pitchChange = Math.abs((double)value.pitch - oldPitchResult);
            yaws.add(yawChange);
            if ((pitchChange > 1.5 || yawChange > 3.0) && (value.pitch == 0.0f || value.pitch % 0.01f == 0.0f)) {
                ++randomizerFlaw;
            }
            double robotized = Math.abs(yawChange - yawChangeFirst);
            double diffBetweenYawChanges = yawChange - oldYawChange;
            if (robotized < 2.0 && yawChange > 2.5) {
                ++robotizedAmount;
            }
            if (robotized < 0.99 && yawChange > 4.0) {
                ++machineKnownMovement;
            }
            if (robotized < 0.02 && yawChange > 3.0) {
                ++constantRotations;
            }
            if (robotized < 2.0 && yawChange > 3.0) {
                ++aggressiveAim;
            }
            if (Double.isInfinite(interpolation = RayUtils.scaleVal(yawChange / robotized, 2.0)) && yawChange > 0.0 && ++infinitives > 1 && yawChange < 0.4) {
                --infinitives;
            }
            if (yawChange == 0.1 || pitchChange == 0.1) {
                ++gcd;
            }
            if (yawChange == 0.01 || pitchChange == 0.01) {
                ++gcd;
            }
            if (diffBetweenYawChanges > 0.01 && diffBetweenYawChanges < 2.0) {
                ++aggressivePatternI;
            }
            if (diffBetweenYawChanges < -0.01 && diffBetweenYawChanges > -2.0) {
                ++aggressivePatternD;
            }
            if (diffBetweenYawChanges > 2.0) {
                ++aggressivePatternI2;
            }
            if (diffBetweenYawChanges < -2.0) {
                ++aggressivePatternD2;
            }
            oldYawResult = value.yaw;
            oldPitchResult = value.pitch;
            oldYawChange = yawChange;
        }
        double yawAverage = 0.0;
        if (!yaws.isEmpty()) {
            double sum = 0.0;
            Iterator iterator = yaws.iterator();
            while (iterator.hasNext()) {
                double d = (Double)iterator.next();
                sum += d;
            }
            yawAverage = sum / (double)yaws.size();
        }
        if ((sens = this.protocol.calculateSensitivity()) > 65) {
            if (robotizedAmount > 8) {
                this.handleCheck("heuristic(sync)", 125.0f);
            }
            if (aggressiveAim > 8) {
                this.handleCheck("heuristic(aggressive)", 50.0f);
            }
            if (machineKnownMovement > 7) {
                this.handleCheck("heuristic(aim)", 100.0f);
            }
            if (constantRotations > 3) {
                this.handleCheck("heuristic(constant)", 65.0f);
            }
        } else {
            if (machineKnownMovement > 8) {
                this.handleCheck("heuristic(aim)", 100.0f);
            }
            if (constantRotations > 6) {
                this.handleCheck("heuristic(constant)", 65.0f);
            }
        }
        if (randomizerFlaw > 0) {
            this.handleCheck("randomizer(flaw)", randomizerFlaw * 100);
        }
        if (infinitives > 1 && Math.abs(yawAverage) > 3.2) {
            this.checkCheckCancellation("heuristic(interpolation)", 55.0f);
        }
        if (gcd > 0) {
            this.handleCheck("pattern(gcd)", 1000.0f);
        }
        if (aggressivePatternI > 3 && aggressivePatternD > 3) {
            this.checkCheckCancellation("pattern(random)", 25.0f);
        }
        if (aggressivePatternI2 > 3 && aggressivePatternD2 > 3 && aggressivePatternI2 + aggressivePatternD2 > 8) {
            ++this.aV;
            if (this.aV > 2) {
                this.handleCheck("pattern(snap)", 55.0f);
            }
        } else {
            this.aV = 0;
        }
        if (!this.aS.isEmpty() && this.aj > 400.0f) {
            this.aN.cancel("(layer1) type: " + this.aS + ", rA: " + robotizedAmount + ", mKM: " + machineKnownMovement + ", aA: " + aggressiveAim + ", cR: " + constantRotations + ", gcd: " + gcd + ", aPI: " + aggressivePatternI + ", aPD: " + aggressivePatternD + ", aPI2: " + aggressivePatternI2 + ", aPD2: " + aggressivePatternD2 + ", rF: " + randomizerFlaw, this.protocol.getFromLocation());
            this.aj -= 10.0f;
        }
        if (!this.aT.isEmpty() && this.aR > 400.0f) {
            this.aO.cancel("(layer2) type: " + this.aT + ", infs: " + infinitives, this.protocol.getFromLocation());
            this.aR -= 65.0f;
        }
        if (this.aj > 0.0f) {
            this.aj -= 5.0f;
        }
        if (this.aj > 400.0f) {
            this.aj -= 10.0f;
        }
        if (this.aR > 0.0f) {
            this.aR -= 5.0f;
        }
        if (this.aR > 380.0f) {
            this.aR -= 10.0f;
        }
        this.U.clear();
        this.aQ.clear();
    }

    public void handleCheck(Entity targetHandle) {
        this.call(() -> {
            if (targetHandle instanceof Player || targetHandle instanceof Zombie || targetHandle instanceof Creeper || targetHandle instanceof Skeleton) {
                this.aP = new AttackHandle(targetHandle, System.currentTimeMillis());
            }
        });
    }

    private static CVector2F handleCheck(Vector from, Vector to) {
        Vector diff = to.subtract(from);
        double distance = Math.hypot(diff.getX(), diff.getZ());
        float yaw = (float)(Math.atan2(diff.getZ(), diff.getX()) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diff.getY(), distance) * 180.0 / Math.PI));
        return new CVector2F(yaw, pitch);
    }

    private void handleCheck(String reason, float vlAdd) {
        this.aj += vlAdd / (float)this.aU.size();
        this.aS = this.aS.isEmpty() ? reason : this.aS + ", " + reason;
    }

    private void checkCheckCancellation(String reason, float vlAdd) {
        this.aR += vlAdd;
        if (this.aT.isEmpty()) {
            this.aT = reason;
        } else if (!this.aT.contains(reason)) {
            this.aT = this.aT + ", " + reason;
        }
    }

    private static PriorityType getMoveDirection() {
        return PriorityType.CAREFUL;
    }

    private static PriorityData handleCheck(PriorityType priorityType) {
        switch (priorityType) {
            case AGGRESSIVE: {
                return new PriorityData(200.0f, 4.0f);
            }
            case LENIENT: {
                return new PriorityData(900.0f, 6.0f);
            }
            case SILENT: {
                return new PriorityData(1500.0f, 8.0f);
            }
        }
        return new PriorityData(400.0f, 5.0f);
    }
}

