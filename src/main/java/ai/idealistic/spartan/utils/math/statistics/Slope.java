/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.utils.math.statistics;

import java.util.Collection;

public class Slope {
    public final double intercept;
    public final double slope;
    public final double r2;
    public final double error;
    public final int observations;

    private static <E> double[] collectionToArray(Collection<E> collection) {
        double[] array = new double[collection.size()];
        int counter = 0;
        for (E dbl : collection) {
            array[counter] = (Double)dbl;
            ++counter;
        }
        return array;
    }

    public Slope(double[] x, double[] y) {
        int n = x.length;
        if (n != y.length) {
            throw new IllegalArgumentException("Array lengths must be equal to each other");
        }
        this.observations = n;
        double meanY = 0.0;
        double meanX = 0.0;
        for (i < n; ++i) {
            meanY += y[i];
            meanX += x[i];
        }
        meanY /= (double)n;
        meanX /= (double)n;
        double xMinusBarX_sumPowered = 0.0;
        double yMinusBarY_MultipliedBy_xMinusBarX_sum = 0.0;
        for (i < n; ++i) {
            double yMinusBarY = y[i] - meanY;
            double xMinusBarX = x[i] - meanX;
            yMinusBarY_MultipliedBy_xMinusBarX_sum += yMinusBarY * xMinusBarX;
            xMinusBarX_sumPowered += xMinusBarX * xMinusBarX;
        }
        this.slope = yMinusBarY_MultipliedBy_xMinusBarX_sum / xMinusBarX_sumPowered;
        this.intercept = meanY - this.slope * meanX;
        double yPredictedMinusBarY_sumPowered = 0.0;
        double yMinusBarY_sumPowered = 0.0;
        double yPredictedMinusY_sumPowered = 0.0;
        for (i < n; ++i) {
            double yCurrent = y[i];
            double yPredicted = this.predictY(x[i]);
            double yPredictedMinusY = yPredicted - yCurrent;
            double yPredictedMinusBarY = yPredicted - meanY;
            double yMinusBarY = yCurrent - meanY;
            yPredictedMinusBarY_sumPowered += yPredictedMinusBarY * yPredictedMinusBarY;
            yMinusBarY_sumPowered += yMinusBarY * yMinusBarY;
            yPredictedMinusY_sumPowered += yPredictedMinusY * yPredictedMinusY;
        }
        this.r2 = yPredictedMinusBarY_sumPowered / yMinusBarY_sumPowered;
        this.error = Math.sqrt(yPredictedMinusY_sumPowered / (double)(n - 2));
    }

    public <E> Slope(Collection<E> x, Collection<E> y) {
        this(Slope.collectionToArray(x), Slope.collectionToArray(y));
    }

    public double predictY(double x) {
        return this.intercept + this.slope * x;
    }
}

