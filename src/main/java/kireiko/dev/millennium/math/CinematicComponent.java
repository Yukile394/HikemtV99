/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  ai.idealistic.spartan.abstraction.event.RotationEvent
 *  com.google.common.collect.Lists
 */
package kireiko.dev.millennium.math;

import ai.idealistic.spartan.abstraction.event.RotationEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.utils.GraphUtil;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import kireiko.dev.millennium.math.MathCollection;

public final class CinematicComponent {
    private final List<Double> yawSamples = Lists.newArrayList();
    private final List<Double> pitchSamples = Lists.newArrayList();
    private final PlayerProtocol profile;
    private long lastSmooth = 0L;
    private long lastHighRate = 0L;
    private double lastDeltaYaw = 0.0;
    private double lastDeltaPitch = 0.0;
    private int isTotallyNotCinematic = 0;

    public CinematicComponent(PlayerProtocol profile) {
    }

    public void process(RotationEvent rotationUpdate) {
        boolean cinematic;
        long now = System.currentTimeMillis();
        double deltaYaw = rotationUpdate.getDelta().getX();
        double deltaPitch = rotationUpdate.getDelta().getY();
        double differenceYaw = Math.abs(deltaYaw - this.lastDeltaYaw);
        double differencePitch = Math.abs(deltaPitch - this.lastDeltaPitch);
        double joltYaw = Math.abs(differenceYaw - deltaYaw);
        double joltPitch = Math.abs(differencePitch - deltaPitch);
        cinematic = now - this.lastHighRate > 250L || now - this.lastSmooth < 9000L;
        if (joltYaw > 1.0 && joltPitch > 1.0) {
            this.lastHighRate = now;
        }
        this.yawSamples.add(deltaYaw);
        this.pitchSamples.add(deltaPitch);
        if (this.yawSamples.size() >= 20 && this.pitchSamples.size() >= 20) {
            HashSet<Double> shannonYaw = new HashSet<Double>();
            HashSet<Double> shannonPitch = new HashSet<Double>();
            ArrayList<Double> stackYaw = new ArrayList<Double>();
            ArrayList<Double> stackPitch = new ArrayList<Double>();
            for (Double yawSample : this.yawSamples) {
                stackYaw.add(yawSample);
                stackPitch.add(yawSample);
                if (stackYaw.size() < 10 || stackPitch.size() < 10) continue;
                shannonYaw.add(MathCollection.getShannonEntropy(stackYaw));
                shannonPitch.add(MathCollection.getShannonEntropy(stackPitch));
                stackYaw.clear();
                stackPitch.clear();
            }
            if (shannonYaw.size() != 1 || shannonPitch.size() != 1 || ((Double)shannonYaw.toArray()[0]).doubleValue() != ((Double)shannonPitch.toArray()[0]).doubleValue()) {
                this.isTotallyNotCinematic = 20;
            }
            GraphUtil.GraphResult resultsYaw = GraphUtil.getGraph(this.yawSamples);
            GraphUtil.GraphResult resultsPitch = GraphUtil.getGraph(this.pitchSamples);
            int negativesYaw = resultsYaw.getNegatives();
            int negativesPitch = resultsPitch.getNegatives();
            int positivesYaw = resultsYaw.getPositives();
            int positivesPitch = resultsPitch.getPositives();
            if (positivesYaw > negativesYaw || positivesPitch > negativesPitch) {
                this.lastSmooth = now;
            }
            this.yawSamples.clear();
            this.pitchSamples.clear();
        }
        if (this.isTotallyNotCinematic > 0) {
            --this.isTotallyNotCinematic;
            this.profile.setCinematic(false);
        } else {
            this.profile.setCinematic(cinematic);
        }
        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
    }
}

