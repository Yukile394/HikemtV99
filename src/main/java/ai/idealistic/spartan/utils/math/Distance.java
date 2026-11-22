/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.util.NumberConversions
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.utils.math;

import ai.idealistic.spartan.abstraction.world.ServerLocation;
import java.util.Random;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public final class Distance {
    public static final double SQUARE_ROOT_2 = Math.sqrt(2.0);

    public static double integersToDouble(int x, int y) {
        return (double)a + (b == 0 ? 0.0 : (double)b / Math.pow(10.0, 1 + (int)Math.log10(b)));
    }

    public static double computeAlgebra(double value, double cut) {
        cut = (int)Math.pow(10.0, cut);
        return Math.floor(value * cut) / cut;
    }

    public static float computeAlgebra(float value, double cut) {
        cut = (int)Math.pow(10.0, cut);
        return (float)(Math.floor((double)value * cut) / cut);
    }

    public static double removeBase(double d) {
        return d - Math.floor(d);
    }

    public static double roundToPortion(double dbl, double portion) {
        return Math.floor(dbl * portion) / portion;
    }

    public static double ceilToPortion(double dbl, double portion) {
        return Math.ceil(dbl * portion) / portion;
    }

    public static double floorToPortion(double dbl, double portion) {
        return Math.floor(dbl * portion) / portion;
    }

    public static int roundToNearest(Number number, int nearest) {
        return (int)((long)nearest * Math.round(number.(double)Value() / (double)nearest));
    }

    public static int floorToNearest(Number number, int nearest) {
        return (int)((double)nearest * Math.floor(number.(double)Value() / (double)nearest));
    }

    public static int ceilToNearest(Number number, int nearest) {
        return (int)((double)nearest * Math.ceil(number.(double)Value() / (double)nearest));
    }

    public static int getUniqueMultiplications(int m) {
        int total = 0;
        int loop = m - 1;
        for (i < loop; ++i) {
            total += --m;
        }
        return total;
    }

    public static int getUniqueMultiplicationsInRow(int m, int row) {
        int loop = m - 1;
        for (i < loop; ++i) {
            --m;
            if (i != row) continue;
            return m;
        }
        return 0;
    }

    public static double getSquare(double d1, double d2) {
        double d = d1 - d2;
        return d * d;
    }

    public static double getSquaredDistance(double x1, double x2, double y1, double y2, double z1, double z2) {
        return Distance.getSquare(x1, x2) + Distance.getSquare(y1, y2) + Distance.getSquare(z1, z2);
    }

    public static double getDistance(double x1, double x2, double y1, double y2, double z1, double z2) {
        return Math.sqrt(Distance.getSquaredDistance(x1, x2, y1, y2, z1, z2));
    }

    public static int generalize(double number, int level) {
        return NumberConversions.ceil((double)(number * (double)level));
    }

    public static double findGCD(double limit, double first, double second) {
        return second <= limit ? first : Distance.findGCD(limit, second, first % second);
    }

    public static double getHorizontalDistance(Location loc, Vector vec) {
        return Math.sqrt(Distance.getSquare(loc.getX(), vec.getX()) + Distance.getSquare(loc.getZ(), vec.getZ()));
    }

    public static double getHorizontalDistance(Vector vec, Location loc) {
        return Distance.getHorizontalDistance(loc, vec);
    }

    public static double getHorizontalDistance(ServerLocation loc, Vector vec) {
        return Math.sqrt(Distance.getSquare(loc.getX(), vec.getX()) + Distance.getSquare(loc.getZ(), vec.getZ()));
    }

    public static double getHorizontalDistance(Vector vec, ServerLocation loc) {
        return Distance.getHorizontalDistance(loc, vec);
    }

    public static double getHorizontalDistance(Location loc1, ServerLocation loc2) {
        return Math.sqrt(Distance.getSquare(loc1.getX(), loc2.getX()) + Distance.getSquare(loc1.getZ(), loc2.getZ()));
    }

    public static double getHorizontalDistance(ServerLocation loc1, Location loc2) {
        return Distance.getHorizontalDistance(loc2, loc1);
    }

    public static double getHorizontalDistance(Vector vec1, Vector vec2) {
        return Math.sqrt(Distance.getSquare(vec1.getX(), vec2.getX()) + Distance.getSquare(vec1.getZ(), vec2.getZ()));
    }

    public static double getHorizontalDistance(Location loc1, Location loc2) {
        return Math.sqrt(Distance.getSquare(loc1.getX(), loc2.getX()) + Distance.getSquare(loc1.getZ(), loc2.getZ()));
    }

    public static double getHorizontalDistance(ServerLocation loc1, ServerLocation loc2) {
        return Math.sqrt(Distance.getSquare(loc1.getX(), loc2.getX()) + Distance.getSquare(loc1.getZ(), loc2.getZ()));
    }

    public static double getVerticalDistance(Location loc1, ServerLocation loc2) {
        return Math.sqrt(Distance.getSquare(loc1.getY(), loc2.getY()));
    }

    public static double getVerticalDistance(ServerLocation loc1, Location loc2) {
        return Distance.getVerticalDistance(loc2, loc1);
    }

    public static double getVerticalDistance(Location loc1, Location loc2) {
        return Math.sqrt(Distance.getSquare(loc1.getY(), loc2.getY()));
    }

    public static double getVerticalDistance(ServerLocation loc1, ServerLocation loc2) {
        return Math.sqrt(Distance.getSquare(loc1.getY(), loc2.getY()));
    }

    public static float wrapAngleTo180(float value) {
        if ((value %= 360.0f) >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }

    public static double wrapAngleTo180(double value) {
        if ((value %= 360.0) >= 180.0) {
            value -= 360.0;
        }
        if (value < -180.0) {
            value += 360.0;
        }
        return value;
    }

    public static int randomInteger(int one, int two) {
        return new Random().nextInt(Math.abs(two - one)) + one;
    }

    public static double randomDouble(double one, double two) {
        return one + (two - one) * new Random().nextDouble();
    }

    public static float randomFloat(float one, float two) {
        return one + (two - one) * new Random().nextFloat();
    }

    public static Integer returnValidInteger(String str) {
        try {
            return Integer.parseInt(str);
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static boolean validInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static boolean validLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static Double returnValidDecimal(String str) {
        try {
            return Double.parseDouble(str);
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static boolean validDecimal(String str) {
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static Float returnValidFloat(String str) {
        try {
            return Float.valueOf(Float.parseFloat(str));
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static boolean validFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public static boolean validNumber(String str) {
        int i;
        int start;
        if (str == null || str.length() == 0) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        start = chars[0] == '-' ? 1 : 0;
        if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
            int i2 = start + 2;
            if (i2 == sz) {
                return false;
            }
            while (i2 < chars.length) {
                if (!(chars[i2] >= '0' && chars[i2] <= '9' || chars[i2] >= 'a' && chars[i2] <= 'f' || chars[i2] >= 'A' && chars[i2] <= 'F')) {
                    return false;
                }
                ++i2;
            }
            return true;
        }
        --sz;
        for (i = start; i < sz || i < sz + 1 && allowSigns && !foundDigit; ++i) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;
                continue;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    return false;
                }
                hasDecPoint = true;
                continue;
            }
            if (chars[i] != 'e' && chars[i] != 'E') {
                if (chars[i] != '+' && chars[i] != '-') {
                    return false;
                }
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false;
                continue;
            }
            if (hasExp) {
                return false;
            }
            if (!foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                return true;
            }
            if (chars[i] != 'e' && chars[i] != 'E') {
                if (chars[i] == '.') {
                    return !hasDecPoint && !hasExp && foundDigit;
                }
                if (allowSigns || chars[i] != 'd' && chars[i] != 'D' && chars[i] != 'f' && chars[i] != 'F') {
                    if (chars[i] != 'l' && chars[i] != 'L') {
                        return false;
                    }
                    return foundDigit && !hasExp && !hasDecPoint;
                }
                return foundDigit;
            }
            return false;
        }
        return !allowSigns && foundDigit;
    }

    public static boolean isExponentVisible(double value) {
        return value < 1.0E-4;
    }

    public static double (float)Double(double d) {
        return (float)d;
    }

    @Generated
    private Distance() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

