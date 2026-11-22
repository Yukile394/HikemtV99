/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml.data.statistic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import kireiko.dev.millennium.math.MathCollection;
import kireiko.dev.millennium.ml.data.Pitch;
import kireiko.dev.millennium.ml.data.statistic.StatisticPattern;
import lombok.Generated;

public class OtherStatisticTable
implements Serializable {
    private static final long serialVersionUID = 1L;
    private int stackSize;
    private static final int MIN_VALUES = 10;
    private static final double KURTOSIS_DIVISOR = 10.0;
    private static final double AVERAGE_DIVISOR = 3.0;
    private static final double OUTLIERS_DIVISOR = 8.0;
    private Set<StatisticPattern> parameters;
    private double scale;
    private int fixed;
    private int learned;

    public OtherStatisticTable(double scale, int stackSize) {
        this.fixed = 0;
        this.learned = 0;
        this.parameters = Collections.synchronizedSet(new HashSet());
    }

    public double checkData(Pitch data) {
        int index;
        this.validateData(data);
        double vl = 0.0;
        List<Double> values = data.getValues();
        this.fixed = 0;
for (i <= values.size() / this.stackSize && (index = i * this.stackSize) + this.stackSize <= values.size(); ++i) {
            List<Double> stack = values.subList(index, index + this.stackSize);
            List<Double> stackDelta = this.calculateStackDelta(stack);
            StatisticPattern statisticPattern = this.createStatisticPattern(stackDelta);
            for (StatisticPattern pattern : this.parameters) {
                if (!pattern.compare(statisticPattern, this.scale)) continue;
                double localVl = Math.min((double)pattern.detected / ((double)pattern.legit + 1.0), 5.0) / (double)(this.stackSize * 4);
                vl += localVl;
                continue block0;
            }
        }
        return vl;
    }

    public void pushData(Pitch data, boolean isMustBeBlocked) {
        int index;
        this.validateData(data);
        List<Double> values = data.getValues();
        this.fixed = 0;
        for (i <= values.size() / this.stackSize && (index = i * this.stackSize) + this.stackSize <= values.size(); ++i) {
            List<Double> stack = values.subList(index, index + this.stackSize);
            List<Double> stackDelta = this.calculateStackDelta(stack);
            StatisticPattern statisticPattern = this.createStatisticPattern(stackDelta);
            this.updatePatterns(statisticPattern, isMustBeBlocked);
        }
    }

    private void validateData(Pitch data) {
        if (data == null || data.getValues() == null || data.getValues().size() < 10) {
            throw new IllegalArgumentException("Data must contain at least 10 values.");
        }
    }

    private List<Double> calculateStackDelta(List<Double> stack) {
        ArrayList<Double> stackDelta = new ArrayList<Double>();
        double previousValue = stack.get(0);
        for (double currentValue : stack) {
            stackDelta.add(Math.abs(currentValue - previousValue));
            previousValue = currentValue;
        }
        return stackDelta;
    }

    private StatisticPattern createStatisticPattern(List<Double> stackDelta) {
        int kurtosis = (int)(MathCollection.getKurtosis(stackDelta) / (10.0.scale));
        int outliersX = (int)(MathCollection.getStandardDeviation((Collection<? extends Number>)MathCollection.getOutliers(stackDelta).getX()) / (8.0.scale));
        int outliersY = (int)(MathCollection.getStandardDeviation((Collection<? extends Number>)MathCollection.getOutliers(stackDelta).getY()) / (8.0.scale));
        double iqr = MathCollection.getIQR(stackDelta);
        double entropy = MathCollection.getShannonEntropy(stackDelta);
        int outliersGeneric = MathCollection.getZScoreOutliers(stackDelta, 0.5).size();
        double jiff = MathCollection.getStandardDeviation(MathCollection.getJiffDelta(stackDelta, 3));
        double kTest = MathCollection.kolmogorovSmirnovTest(stackDelta, Function.identity()) / 10.0;
        int distinct = MathCollection.getDistinct(stackDelta);
        return new StatisticPattern(kurtosis, outliersX, outliersY, iqr, entropy, outliersGeneric, jiff, kTest, distinct);
    }

    private void updatePatterns(StatisticPattern statisticPattern, boolean blocked) {
        for (StatisticPattern pattern : this.parameters) {
            pattern.optimize();
            if (!pattern.compare(statisticPattern, this.scale)) continue;
            if (blocked) {
                ++pattern.detected;
            } else {
                ++pattern.legit;
            }
            ++this.fixed;
            return;
        }
        this.parameters.add(statisticPattern);
        ++this.learned;
    }

    @Generated
    public int getStackSize() {
        return this.stackSize;
    }

    @Generated
    public Set<StatisticPattern> getParameters() {
        return this.parameters;
    }

    @Generated
    public double getScale() {
        return this.scale;
    }

    @Generated
    public int getFixed() {
        return this.fixed;
    }

    @Generated
    public int getLearned() {
        return this.learned;
    }

    @Generated
    public void setStackSize(int stackSize) {
    }

    @Generated
    public void setParameters(Set<StatisticPattern> parameters) {
    }

    @Generated
    public void setScale(double scale) {
    }

    @Generated
    public void setFixed(int fixed) {
    }

    @Generated
    public void setLearned(int learned) {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OtherStatisticTable)) {
            return false;
        }
        OtherStatisticTable other = (OtherStatisticTable)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getStackSize() != other.getStackSize()) {
            return false;
        }
        if (Double.compare(this.getScale(), other.getScale()) != 0) {
            return false;
        }
        if (this.getFixed() != other.getFixed()) {
            return false;
        }
        if (this.getLearned() != other.getLearned()) {
            return false;
        }
        Set<StatisticPattern> this$parameters = this.getParameters();
        Set<StatisticPattern> other$parameters = other.getParameters();
        return !(this$parameters == null ? other$parameters != null : !((Object)this$parameters).equals(other$parameters));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof OtherStatisticTable;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        result = result * 59 + this.getStackSize();
        long $scale = Double.(double)ToLongBits(this.getScale());
        result = result * 59 + (int)($scale >>> 32 ^ $scale);
        result = result * 59 + this.getFixed();
        result = result * 59 + this.getLearned();
        Set<StatisticPattern> $parameters = this.getParameters();
        result = result * 59 + ($parameters == null ? 43 : (Object$parameters).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "StatisticML(stackSize=" + this.getStackSize() + ", parameters=" + this.getParameters() + ", scale=" + this.getScale() + ", fixed=" + this.getFixed() + ", learned=" + this.getLearned() + ")";
    }
}

