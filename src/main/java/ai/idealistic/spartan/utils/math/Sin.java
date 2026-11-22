/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.utils.math;

import ai.idealistic.spartan.utils.math.ClientMath;

public class Sin
implements ClientMath {
    private static final float[] SIN_TABLE_FAST = new float[4096];
    private static final float radToIndex = Sin.roundToFloat(651.8986469044033);

    @Override
    public float sin(float value) {
        return SIN_TABLE_FAST[(int)(value * radToIndex) & 0xFFF];
    }

    @Override
    public float cos(float value) {
        return SIN_TABLE_FAST[(int)(value * radToIndex + 1024.0f) & 0xFFF];
    }

    public static float roundToFloat(double d) {
        return (float)((double)Math.round(d.0E8) / 1.0E8);
    }

    static {
        for (j < SIN_TABLE_FAST.length; ++j) {
            Sin.SIN_TABLE_FAST[j] = Sin.roundToFloat(Math.sin((double)j * Math.PI * 2.0.0));
        }
    }
}

