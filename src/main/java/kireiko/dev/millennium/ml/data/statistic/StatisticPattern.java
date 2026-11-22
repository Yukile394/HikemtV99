/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml.data.statistic;

import java.io.Serializable;
import lombok.Generated;

public class StatisticPattern
implements Serializable {
    private static final long serialVersionUID = 1L;
    public int legit = 0;
    public int detected = 0;
    private final int kurtosis;
    private final int outliersX;
    private final int outliersY;
    private final double iqr;
    private final double shannon;
    private final int outliersGeneric;
    private final double jiff;
    private final double kTest;
    private final int distinct;

    public boolean compare(StatisticPattern other, double scale) {
        return StatisticPattern.computeStatisticPattern(this.kurtosis, other.kurtosis) < scale * 3.0 && StatisticPattern.computeStatisticPattern(this.outliersX, other.outliersX) < scale * 4.0 && StatisticPattern.computeStatisticPattern(this.outliersY, other.outliersY) < scale * 4.0 && StatisticPattern.computeStatisticPattern(this.outliersGeneric, other.outliersGeneric) < scale * 2.1.0 && StatisticPattern.computeStatisticPattern(this.iqr, other.iqr) < scale * 15.0 && StatisticPattern.computeStatisticPattern(this.shannon, other.shannon) < scale / 100.0 && StatisticPattern.computeStatisticPattern(this.jiff, other.jiff) < scale * 4.0 && StatisticPattern.computeStatisticPattern(this.kTest, other.kTest) < scale * 2.0 && StatisticPattern.computeStatisticPattern(this.distinct, other.distinct) < scale * 2.0;
    }

    public void optimize() {
        if (this.legit + this.detected > 25) {
            this.legit /= 2;
            this.detected /= 2;
        }
    }

    private static double computeStatisticPattern(double v1, double v2) {
        return Math.abs(Math.abs(v1) - Math.abs(v2));
    }

    @Generated
    public int getLegit() {
        return this.legit;
    }

    @Generated
    public int getDetected() {
        return this.detected;
    }

    @Generated
    public int getKurtosis() {
        return this.kurtosis;
    }

    @Generated
    public int getOutliersX() {
        return this.outliersX;
    }

    @Generated
    public int getOutliersY() {
        return this.outliersY;
    }

    @Generated
    public double getIqr() {
        return this.iqr;
    }

    @Generated
    public double getShannon() {
        return this.shannon;
    }

    @Generated
    public int getOutliersGeneric() {
        return this.outliersGeneric;
    }

    @Generated
    public double getJiff() {
        return this.jiff;
    }

    @Generated
    public double getKTest() {
        return this.kTest;
    }

    @Generated
    public int getDistinct() {
        return this.distinct;
    }

    @Generated
    public void setLegit(int legit) {
    }

    @Generated
    public void setDetected(int detected) {
    }

    @Generated
    public StatisticPattern(int kurtosis, int outliersX, int outliersY, double iqr, double shannon, int outliersGeneric, double jiff, double kTest, int distinct) {
    }
}

