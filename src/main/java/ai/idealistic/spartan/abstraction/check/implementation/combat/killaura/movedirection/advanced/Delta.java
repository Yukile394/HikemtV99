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
import java.util.Arrays;
import java.util.List;
import kireiko.dev.millennium.math.MathCollection;
import kireiko.dev.millennium.types.Delta;

public final class Delta
implements HeuristicComponent {
    private final CheckDetection A;
    private final PlayerProtocol profile;
    private final List<Double> bB = new ArrayList<Double>();

    public Delta(CheckDetection detection, PlayerProtocol protocol) {
        this.A = detection;
        this.profile = protocol;
    }

    @Override
    public void process(RotationEvent event) {
        if (event.getAbsDelta().getY() == 0.0f && event.getAbsDelta().getY() == 0.0f) {
            return;
        }
        Delta delta = event.getDelta();
        double angle = Delta.handleCheck(delta) % 90.0;
        if ((double)event.getAbsDelta().getY() > 1.5 && (double)event.getAbsDelta().getX() > 0.32 || (double)event.getAbsDelta().getX() > 1.5) {
            this.bB.add(angle);
        }
        if (this.bB.size() >= 20) {
            List<Float> jiff = MathCollection.getJiffDelta(this.bB, 1);
            float prev = 999.0f;
            float prePrev = 999.0f;
            for (float f : jiff) {
                if ((double)f == 0.0 && (double)prev == 0.0 && prePrev == 0.0f) {
                    this.A.call(() -> this.A.cancel("[Smooth] Invalid smoothing " + Arrays.toString(jiff.toArray()), this.profile.getFromLocation()));
                    break;
                }
                prePrev = prev;
                prev = f;
            }
            this.bB.clear();
        }
    }

    private static double handleCheck(Delta delta) {
        double angleInRadians = Math.atan2(delta.getX(), delta.getY());
        double angleInDegrees = Math.toDegrees(angleInRadians);
        if (angleInDegrees < 0.0) {
            angleInDegrees += 360.0;
        }
        return angleInDegrees;
    }
}

