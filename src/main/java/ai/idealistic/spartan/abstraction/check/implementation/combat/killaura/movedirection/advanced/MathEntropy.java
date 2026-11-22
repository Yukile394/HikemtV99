/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  ai.idealistic.spartan.abstraction.event.RotationEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.HeuristicComponent;
import ai.idealistic.spartan.abstraction.event.RotationEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import java.util.ArrayList;
import java.util.List;
import kireiko.dev.millennium.math.MathCollection;
import kireiko.dev.millennium.types.Vec2;
import kireiko.dev.millennium.types.Delta;
import kireiko.dev.millennium.types.GeneratedCompare;

public final class MathEntropy
implements HeuristicComponent {
    private static final int bj = 30;
    private static final float bk = 4.0f;
    private static final int bm = 10;
    private static final int bn = 7;
    private final CheckDetection A;
    private final PlayerProtocol profile;
    private final List<GeneratedCompare> bo = new ArrayList<GeneratedCompare>();
    private final List<Delta> be = new ArrayList<Delta>();
    private final List<Vec2> bp = new ArrayList<Vec2>();
    private double bq = 0.0;
    private double br = 0.0;
    private float bs = 0.0f;
    private float bt = 0.0f;
    private float bu = 0.0f;
    private float bv = 0.0f;
    private float bw = 0.0f;

    public MathEntropy(CheckDetection detection, PlayerProtocol protocol) {
        this.A = detection;
        this.profile = protocol;
    }

    @Override
    public void process(RotationEvent rotationUpdate) {
        Delta delta = new Delta(rotationUpdate.getAbsDelta().getX(), rotationUpdate.getAbsDelta().getY());
        if (delta.getY() == 0.0f && delta.getX() == 0.0f) {
            return;
        }
        this.be.add(delta);
        double gcdValue = MathCollection.getGCDValue(0.5) * 3.0f;
        this.bo.add(new GeneratedCompare((int)((double)delta.getX() / gcdValue), (int)((double)delta.getY() / gcdValue)));
        if (this.bo.size() >= 10) {
            this.updateAimComplex2();
        }
        if (this.be.size() >= 10) {
            this.updateAimComplex();
        }
    }

    private void updateAimComplex() {
        boolean valid;
        int sens = this.profile.calculateSensitivity();
        int sensTemp = this.profile.getSensitivityProcessor().totalSensitivityClient;
        ArrayList<Float> x = new ArrayList<Float>();
        ArrayList<Float> y = new ArrayList<Float>();
        for (Delta vec2 : this.be) {
            x.add(Float.valueOf(vec2.getX()));
            y.add(Float.valueOf(vec2.getY()));
        }
        int disX = MathCollection.getDistinct(x);
        double shannonYaw = MathCollection.getShannonEntropy(x);
        double shannonPitch = MathCollection.getShannonEntropy(y);
        valid = sens >= 60 && sens <= 150 && sensTemp >= 60 && sensTemp < 150;
        if (valid && MathEntropy.handleCheck(shannonYaw, this.bq) < 1.0E-5 && MathEntropy.handleCheck(shannonPitch, this.br) < 1.0E-5) {
            this.bs = Math.min(200.0f, this.bs + 1.0f);
            if (this.bs > 30.0f) {
                this.A.call(() -> this.A.cancel("Analysis (Perfect Entropy)", this.profile.getFromLocation()));
                this.bs = 29.0f;
            }
        } else {
            this.bs = Math.max(0.0f, this.bs - 2.0f);
        }
        if (valid && MathEntropy.handleCheck(shannonYaw, shannonPitch) < 1.0E-5) {
            this.bt = Math.min(200.0f, this.bt + 1.0f);
            if (this.bt > 30.0f) {
                this.A.call(() -> this.A.cancel("Analysis (Similar Entropy)", this.profile.getFromLocation()));
                this.bt = 29.0f;
            }
        } else {
            this.bt = Math.max(0.0f, this.bt - 2.0f);
        }
        if (disX < 8 && Math.abs(MathCollection.getAverage(x)) > 2.5) {
            this.bu = Math.min(200.0f, this.bu + 1.7f);
            if (this.bu >= 4.0f) {
                this.A.call(() -> this.A.cancel("Flaw (Invalid Distinct)", this.profile.getFromLocation()));
                this.bu = Math.max(0.0f, this.bu.5f);
            }
        } else {
            this.bu = Math.max(0.0f, this.bu.35f);
        }
        this.bq = shannonYaw;
        this.br = shannonPitch;
        this.be.clear();
    }

    private void updateAimComplex2() {
        ArrayList<Integer> gcdYaw = new ArrayList<Integer>();
        ArrayList<Integer> gcdPitch = new ArrayList<Integer>();
        for (GeneratedCompare vec2i : this.bo) {
            gcdYaw.add(vec2i.getX());
            gcdPitch.add(vec2i.getY());
        }
        this.bo.clear();
        if (gcdYaw.isEmpty()) {
            return;
        }
        Vec2 kireikoGenericVec = new Vec2(MathCollection.getKireikoGeneric(gcdYaw), MathCollection.getKireikoGeneric(gcdPitch));
        this.bp.add(kireikoGenericVec);
        if (this.bp.size() >= 7) {
            ArrayList<Double> x = new ArrayList<Double>();
            for (Vec2 vec2 : this.bp) {
                x.add(vec2.getX());
            }
            double xDev = MathCollection.getStandardDeviation(x);
            double xMax = MathCollection.getMax(x);
            if (xDev > 5.0 && xDev < 22.0 && xMax < 50.0) {
                this.bw += MathCollection.getAverage(x) < 6.0 ? 0.0f : (xDev < 10.0 ? 1.5f : 1.0f);
                if (this.bw >= 7.0f) {
                    this.bw = 6.0f;
                }
            } else {
                this.bw -= xDev < 40.0 || xMax < 70.0 ? -0.4f : -0.8f;
            }
            this.bw = Math.max(0.0f, this.bw);
            this.bp.clear();
        }
        double devX = MathCollection.getVariance(gcdYaw);
        double devY = MathCollection.getVariance(gcdPitch);
        double min = Math.min(devX, devY);
        double max = Math.max(devX, devY);
        if (min < 0.09 && max > 35.0 && MathCollection.getMin(gcdPitch) != 0.0 && this.profile.calculateSensitivity() > 50) {
            this.bv = Math.min(200.0f, this.bv + 1.0f);
            if (this.bv > 2.5f) {
                this.A.call(() -> this.A.cancel("Analysis (Randomizer Flaw)", this.profile.getFromLocation()));
                this.bv = 1.5f;
            }
        } else {
            this.bv = Math.max(0.0f, this.bv.4f);
        }
    }

    private static double handleCheck(double x, double y) {
        return Math.abs(Math.abs(a) - Math.abs(b));
    }
}

