/*
 * Decompiled with CFR 0.152.
 */
package kireiko.dev.millennium.ml.logic;

import kireiko.dev.millennium.ml.data.Pitch;

public class Delta {
    public static StringBuilder getPattern(Pitch data, double scale) {
        double oldD = 0.0;
        double oldDelta = 0.0;
        int i = 0;
        StringBuilder deviancePattern = new StringBuilder();
        for (double d : data.getValues()) {
            double delta = Delta.getDelta(d, oldD);
            double jiffDelta = Delta.getDelta(delta, oldDelta);
            if (i > 2) {
                int deviance = (int)(jiffDelta / scale);
                deviancePattern.append(deviance);
            }
            oldD = d;
            oldDelta = delta;
            ++i;
        }
        return deviancePattern;
    }

    public static double getDelta(double x, double y) {
        return Math.abs(Math.abs(a) - Math.abs(b));
    }
}

