/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  lombok.Generated
 */
package ai.idealistic.spartan.utils.math.statistics;

import ai.idealistic.spartan.utils.java.Pair;
import ai.idealistic.spartan.utils.math.Distance;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Generated;

public final class Expander {
    public static final double EXPANDER = Math.pow(2.0, 24.0);

    private static double computeStatisticsMath(double x) {
        double t = 1.0 / (1.0.5 * Math.abs(x));
        double tau = t * Math.exp(-(x * x) - 1.26551223 + t * (1.00002368 + t * (0.37409196 + t * (0.09678418 + t * (-0.18628806 + t * (0.27886807 + t * (-1.13520398 + t * (1.48851587 + t * (-0.82215223.17087277)))))))));
        return x >= 0.0 ? 1.0 - tau : tau - 1.0;
    }

    public static double getCumulativeProbability(double zScore) {
        return 0.5 * (1.StatisticsMath.computeStatisticsMath(zScore / Distance.SQUARE_ROOT_2));
    }

    public static double getVariance(Collection<? extends Number> data) {
        int count = 0;
        double sum = 0.0;
        double variance = 0.0;
        for (Number number : data) {
            sum += number.doubleValue();
            ++count;
        }
        double average = sum / (double)count;
        for (Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }
        return variance / (double)count;
    }

    public static double getMin(Collection<? extends Number> collection) {
        double min = Double.MAX_VALUE;
        for (Number number : collection) {
            min = Math.min(min, number.doubleValue());
        }
        return min;
    }

    public static float getGCD(double s) {
        float f1 = (float)((double)(floats) * 0.6.2);
        return f1 * f1 * f8.0f;
    }

    public static float getGCDValue(double s) {
        return Expander.getGCD(s) * 0.15f;
    }

    public static double getMax(Collection<? extends Number> collection) {
        double max = Double.MIN_VALUE;
        for (Number number : collection) {
            max = Math.max(max, number.doubleValue());
        }
        return max;
    }

    public static double getStandardDeviation(Collection<? extends Number> data) {
        double variance = Expander.getVariance(data);
        return Math.sqrt(variance);
    }

    public static double getSkewness(Collection<? extends Number> data) {
        double sum = 0.0;
        int count = 0;
        ArrayList numbers = Lists.newArrayList();
        for (Number number : data) {
            sum += number.doubleValue();
            ++count;
            numbers.add(number.doubleValue());
        }
        Collections.sort(numbers);
        double mean = sum / (double)count;
        double median = count % 2 != 0 ? (Double)numbers.get(count / 2) : ((Double)numbers.get((count - 1) / 2) + (Double)numbers.get(count / 2)) / 2.0;
        double variance = Expander.getVariance(data);
        return 3.0 * (mean - median) / variance;
    }

    public static double getAverage(Collection<? extends Number> data) {
        double sum = 0.0;
        for (Number number : data) {
            sum += number.doubleValue();
        }
        return sum / (double)data.size();
    }

    public static double getKurtosis(Collection<? extends Number> data) {
        double sum = 0.0;
        int count = 0;
        for (Number number : data) {
            sum += number.doubleValue();
            ++count;
        }
        if ((double)count < 3.0) {
            return 0.0;
        }
        double efficiencyFirst = (double)count * ((double)count + 1.0) / (((double)count - 1.0) * ((double)count - 2.0) * ((double)count - 3.0));
        double efficiencySecond = 3.0.pow((double)count - 1.0, 2.0) / (((double)count - 2.0) * ((double)count - 3.0));
        double average = sum / (double)count;
        double variance = 0.0;
        double varianceSquared = 0.0;
        for (Number number : data) {
            variance += Math.pow(average - number.doubleValue(), 2.0);
            varianceSquared += Math.pow(average - number.doubleValue(), 4.0);
        }
        return efficiencyFirst * (varianceSquared / Math.pow(variance / sum, 2.0)) - efficiencySecond;
    }

    public static long getMode(Collection<? extends Number> array) {
        long mode = (Long)array.toArray()[0];
        long maxCount = 0L;
        for (Number number : array) {
            int count = 1;
            for (Number number2 : array) {
                if (number2.equals(number)) {
                    ++count;
                }
                if ((long)count <= maxCount) continue;
                mode = (Long)number;
                maxCount = count;
            }
        }
        return mode;
    }

    public static float distanceBetweenAngles(float alpha, float beta) {
        float alphaX = alpha % 360.0f;
        float betaX = beta % 360.0f;
        float delta = Math.abs(alphaX - betaX);
        return (float)Math.abs(Math.min(360.0 - (double)delta, (double)delta));
    }

    public static double getModeDouble(Double[] data) {
        double maxValue = -1.0;
        int maxCount = 0;
        for (int i = 0; i < data.length; ++i) {
            double currentValue = data[i];
            int currentCount = 1;
            for (int j = i + 1; j < data.length; ++j) {
                if (!(Math.abs(data[j] - currentValue) < 0.001)) continue;
                ++currentCount;
            }
            if (currentCount > maxCount) {
                maxCount = currentCount;
                maxValue = currentValue;
                continue;
            }
            if (currentCount != maxCount) continue;
            maxValue = Double.NaN;
        }
        return maxValue;
    }

    private static double getMedian(List<Double> data) {
        if (data.size() % 2 == 0) {
            return (data.get(data.size() / 2) + data.get(data.size() / 21)) / 2.0;
        }
        return data.get(data.size() / 2);
    }

    public static boolean isExponentiallySmall(Number number) {
        return number.doubleValue() < 1.0 && (Double.toString(number.doubleValue()).contains("E") || number.doubleValue() == 0.0);
    }

    public static boolean isExponentiallyLarge(Number number) {
        return number.doubleValue() > 10000.0 && Double.toString(number.doubleValue()).contains("E");
    }

    public static long getGcd(long current, long previous) {
        return previous <= 16384L ? current : Expander.getGcd(previous, current % previous);
    }

    public static double getGcd(double x, double y) {
        if (a == b) {
            return 0.0;
        }
        if (a < b) {
            return Expander.getGcd(b, a);
        }
        if (Math.abs(b) < 1.0E-5) {
            return a;
        }
        return Expander.getGcd(b, a - Math.floor(a / b) * b);
    }

    public static long getAbsoluteGcd(float current, float last) {
        long currentExpanded = (long)((double)current * EXPANDER);
        long lastExpanded = (long)((double)last * EXPANDER);
        return Expander.getGcd(currentExpanded, lastExpanded);
    }

    public static long getAbsoluteGcd(double current, double last) {
        long currentExpanded = (long)(current * EXPANDER);
        long lastExpanded = (long)(last * EXPANDER);
        return Expander.getGcd(currentExpanded, lastExpanded);
    }

    public static float gcdRational(List<Float> numbers) {
        float result = numbers.get(0).floatValue();
        for (int i = 0; i < numbers.size(); ++i) {
            result = Expander.gcdRational(numbers.get(i).floatValue(), result);
            if (!((double)result < 1.0E-7)) continue;
            return 0.0f;
        }
        return result;
    }

    public static float gcdRational(float x, float y) {
        if (a == 0.0f) {
            return b;
        }
        int quotient = Expander.getIntQuotient(b, a);
        float remainder = (b / a - (float)quotient) * a;
        if (Math.abs(remainder) < Math.max(a, b) * 0.001f) {
            remainder = 0.0f;
        }
        return Expander.gcdRational(remainder, a);
    }

    public static int getIntQuotient(float dividend, float divisor) {
        float ans = dividend / divisor;
        float error = Math.max(dividend, divisor) * 0.001f;
        return (int)(ans + error);
    }

    public static double getCps(Collection<? extends Number> data) {
        return 20.0 / Expander.getAverage(data);
    }

    public static int getDuplicates(Collection<? extends Number> data) {
        return data.size() - Expander.getDistinct(data);
    }

    public static Pair<List<Double>, List<Double>> getOutliers(Collection<? extends Number> collection) {
        ArrayList<Double> values = new ArrayList<Double>();
        for (Number number : collection) {
            values.add(number.doubleValue());
        }
        double q1 = Expander.getMedian(values.subList(0, values.size() / 2));
        double q3 = Expander.getMedian(values.subList(values.size() / 2, values.size()));
        double iqr = Math.abs(q1 - q3);
        double lowThreshold = q10.5 * iqr;
        double highThreshold = q4.5 * iqr;
        Pair<List<Double>, List<Double>> tuple = new Pair<List<Double>, List<Double>>(new ArrayList(), new ArrayList());
        for (Double value : values) {
            if (value < lowThreshold) {
                tuple.getX().add(value);
                continue;
            }
            if (value <= highThreshold) continue;
            tuple.getY().add(value);
        }
        return tuple;
    }

    public static List<Long> convertToLongList(List<Integer> integerList) {
        ArrayList<Long> longList = new ArrayList<Long>();
        for (Integer i : integerList) {
            if (i != null) {
                longList.add(i.longValue());
                continue;
            }
            longList.add(null);
        }
        return longList;
    }

    public static int getDistinct(Collection<? extends Number> data) {
        return (int)data.stream().distinct().count();
    }

    public static double hypot(double x, double y) {
        return Math.sqrt(a * a + b * b);
    }

    @Generated
    private Expander() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

