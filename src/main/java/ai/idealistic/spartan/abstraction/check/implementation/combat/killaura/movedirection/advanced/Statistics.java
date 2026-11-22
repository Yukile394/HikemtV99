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
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import kireiko.dev.millennium.math.MathCollection;
import kireiko.dev.millennium.types.Delta;

public final class Statistics
implements HeuristicComponent {
    private static final int bm = 25;
    private static final float bG = 11.0f;
    private static final float bH = 7.0f;
    private static final float bI = 15.0f;
    private final CheckDetection A;
    private final PlayerProtocol profile;
    private final List<Delta> bJ = new ArrayList<Delta>();
    private final List<Double> bK = new ArrayList<Double>();
    private float bL = 0.0f;
    private float bM = 0.0f;
    private float bN = 0.0f;

    public Statistics(CheckDetection detection, PlayerProtocol protocol) {
        this.A = detection;
        this.profile = protocol;
    }

    @Override
    public void process(RotationEvent rotationUpdate) {
        if (rotationUpdate.getAbsDelta().getY() == 0.0f && rotationUpdate.getAbsDelta().getX() == 0.0f) {
            return;
        }
        float deltaYaw = rotationUpdate.getAbsDelta().getX();
        float deltaPitch = rotationUpdate.getAbsDelta().getY();
        this.bJ.add(new Delta(deltaYaw, deltaPitch));
        if (this.bJ.size() >= 25) {
            this.D();
            this.bJ.clear();
        }
    }

    private void D() {
        double kTest;
        float total = 0.0f;
        ArrayList<Float> x = new ArrayList<Float>();
        ArrayList<Float> y = new ArrayList<Float>();
        for (Delta vec2 : this.bJ) {
            x.add(Float.valueOf(vec2.getX()));
            y.add(Float.valueOf(vec2.getY()));
        }
        List<Float> jiffYaw = MathCollection.getJiffDelta(x, 5);
        List<Float> jiffPitch = MathCollection.getJiffDelta(y, 5);
        ArrayList<Float> jiffOmni = new ArrayList<Float>();
        for (int i = 0; i < Math.min(jiffYaw.size(), jiffPitch.size()); ++i) {
            jiffOmni.add(Float.valueOf(jiffYaw.get(i).floatValue() / jiffPitch.get(i).floatValue()));
        }
        int infs = 0;
        Iterator iterator = jiffOmni.iterator();
        while (iterator.hasNext()) {
            float j = ((Float)iterator.next()).floatValue();
            if (!Float.isInfinite(j)) continue;
            ++infs;
        }
        double iqr = MathCollection.getIQR(jiffOmni);
        if (iqr > 12.5 && iqr < 96.0 && infs > 0) {
            this.bL += iqr > 20.0 ? 1.4f : 0.8f;
            if (this.bL > 11.0f) {
                this.A.call(() -> this.A.cancel("Statistics (IQR)", this.profile.getFromLocation()));
                this.bL = 9.0f;
            }
        } else if (iqr < 13.0 || infs == 0) {
            this.bL -= iqr < 7.0 ? 5.0f : 3.5f;
        }
        int jiffPatterns = 0;
        for (int i = 0; i < jiffYaw.size(); ++i) {
            float f = jiffYaw.get(i).floatValue();
            if (!String.valueOf(f).contains("E") || f == 0.0f) continue;
            for (int r = 0; r < jiffYaw.size(); ++r) {
                if (r == i || f != jiffYaw.get(r).floatValue()) continue;
                ++jiffPatterns;
            }
        }
        if (jiffPatterns > 2 && MathCollection.getAverage(x) > 3.0 && jiffPatterns != 6 && jiffPatterns != 12 && jiffPatterns != 4) {
            int jiffPatternsFinal = jiffPatterns;
            this.A.call(() -> this.A.cancel("Statistics (Bot Pattern " + jiffPatternsFinal + ")", this.profile.getFromLocation()));
        }
        List<Double> zFactorYaw = MathCollection.getZScoreOutliers(x, 2.0);
        boolean positive = false;
        boolean negative = false;
        for (double d : zFactorYaw) {
            if (d > 10.0) {
                positive = true;
            }
            if (!(d < -10.0)) continue;
            negative = true;
        }
        if (zFactorYaw.size() == 2 && positive && negative && MathCollection.getMax(zFactorYaw) < 55.0) {
            this.bM += 1.5f;
            if (this.bM > 4.0f) {
                total += 1.0f;
            }
            if (this.bM > 7.0f) {
                this.A.call(() -> this.A.cancel("Statistics (Suspicious zFactor)", this.profile.getFromLocation()));
                this.bM = 6.0f;
            }
        } else {
            this.bM -= 1.2f;
        }
        if ((kTest = MathCollection.kolmogorovSmirnovTest(MathCollection.getJiffDelta(x, 6), Function.identity())) > 10.0 && Math.abs(MathCollection.getAverage(x)) < 13.0) {
            total += 1.0f;
        }
        if ((double)total < 2.0) {
            this.bN -= 2.0f;
        } else if ((double)total > 2.0) {
            this.bN += 5.0f;
            if (this.bN >= 15.0f) {
                this.A.call(() -> this.A.cancel("Statistics (Improbable)", this.profile.getFromLocation()));
                this.bN = 13.0f;
            }
        }
        this.bL = Math.max(0.0f, this.bL);
        this.bM = Math.max(0.0f, this.bM);
        this.bN = Math.max(0.0f, this.bN);
    }
}

