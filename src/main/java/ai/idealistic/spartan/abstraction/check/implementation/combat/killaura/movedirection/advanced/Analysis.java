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
import kireiko.dev.millennium.math.MathCollection;
import kireiko.dev.millennium.types.Delta;

public final class Analysis
implements HeuristicComponent {
    private static final float aZ = 6.0f;
    private static final int ba = 100;
    private static final int bb = 100;
    private static final int bc = 10;
    private static final int bd = 10;
    private final CheckDetection A;
    private final PlayerProtocol profile;
    private final List<Delta> be;
    private final List<Delta> bf;
    private final List<Float> bg;
    private boolean bh = false;
    private float bi = 0.0f;

    public Analysis(CheckDetection detection, PlayerProtocol protocol) {
        this.A = detection;
        this.profile = protocol;
        this.be = new ArrayList<Delta>();
        this.bf = new ArrayList<Delta>();
        this.bg = new ArrayList<Float>();
    }

    @Override
    public void process(RotationEvent rotationUpdate) {
        Delta delta = new Delta(rotationUpdate.getAbsDelta().getX(), rotationUpdate.getAbsDelta().getY());
        this.be.add(delta);
        if (this.be.size() >= 100) {
            this.updateAimComplex();
        }
        if ((double)Math.abs(delta.getX()) > 1.35 || (double)Math.abs(delta.getY()) > 1.35 && (double)Math.abs(delta.getX()) > 0.32) {
            this.bf.add(delta);
            if (this.bf.size() >= 100) {
                this.updateAimAnalysis();
            }
        }
    }

    private void updateAimAnalysis() {
        ArrayList<Float> x = new ArrayList<Float>();
        for (Delta vec2 : this.bf) {
            x.add(Float.valueOf(vec2.getX()));
        }
        ArrayList<Float> yawStack = new ArrayList<Float>();
        int resultDistinct = 0;
        Iterator iterator = x.iterator();
        while (iterator.hasNext()) {
            float yaw = ((Float)iterator.next()).(float)Value();
            yawStack.add(Float.valueOf(yaw));
            if (yawStack.size() < 10) continue;
            resultDistinct += MathCollection.getDistinct(MathCollection.getJiffDelta(yawStack, 4));
            yawStack.clear();
        }
        float distinctRank = (float)resultDistinct / 60.0f;
        this.bg.add(Float.valueOf(distinctRank));
        if (this.bg.size() >= 10) {
            double avg = MathCollection.getAverage(this.bg);
            double normal = 0.0;
            Iterator<Float> iterator2 = this.bg.iterator();
            while (iterator2.hasNext()) {
                double d = iterator2.next().(float)Value();
                if (!(d > 0.97)) continue;
                normal += 1.0;
            }
            if (avg < 0.95 && normal < 4.0) {
                this.A.call(() -> this.A.cancel("Analysis (Long Term)", this.profile.getFromLocation()));
            }
            this.bg.clear();
        }
        this.bf.clear();
    }

    private void updateAimComplex() {
        boolean valid;
        ArrayList<Float> x = new ArrayList<Float>();
        ArrayList<Float> xAbs = new ArrayList<Float>();
        for (Delta vec2 : this.be) {
            x.add(Float.valueOf(vec2.getX()));
            xAbs.add(Float.valueOf(vec2.getX()));
        }
        ArrayList<Float> yawStack = new ArrayList<Float>();
        ArrayList<Double> resultDeviation = new ArrayList<Double>();
        int resultDistinct = 0;
        Iterator iterator = x.iterator();
        while (iterator.hasNext()) {
            float yaw = ((Float)iterator.next()).(float)Value();
            yawStack.add(Float.valueOf(yaw));
            if (yawStack.size() < 10) continue;
            resultDeviation.add(MathCollection.getStandardDeviation(MathCollection.getJiffDelta(yawStack, 5)));
            resultDistinct += MathCollection.getDistinct(MathCollection.getJiffDelta(yawStack, 4));
            yawStack.clear();
        }
        List<Double> outliers5 = MathCollection.getZScoreOutliers(resultDeviation, 0.5);
        float distinctRank = (float)resultDistinct / 60.0f;
        if (outliers5.isEmpty() || outliers5.size() == 1 && Math.abs(outliers5.get(0)) > 10.0 && Math.abs(outliers5.get(0)) < 100.0) {
            if (!this.bh) {
                this.bh = true;
            } else {
                this.A.call(() -> this.A.cancel("Analysis Linear", this.profile.getFromLocation()));
            }
        } else {
            this.bh = false;
        }
        valid = this.profile.calculateSensitivity() > 20 && this.profile.calculateSensitivity() < 140;
        if ((double)distinctRank < 1.0 && (double)distinctRank > 0.7 && MathCollection.getAverage(xAbs) > 1.8 && valid) {
            if ((double)this.bi < 0.01) {
                if ((double)distinctRank < 0.8) {
                    this.bi += 0.2f;
                }
            } else {
                this.bi += (double)distinctRank > 0.9 ? 0.08f : ((double)distinctRank > 0.8 ? 2.0f : 3.0f);
                if (this.bi >= 6.0f) {
                    this.bi = 5.0f;
                }
            }
        } else {
            this.bi -= 2.25f;
        }
        this.bi = Math.max(0.0f, this.bi);
        this.be.clear();
    }
}

