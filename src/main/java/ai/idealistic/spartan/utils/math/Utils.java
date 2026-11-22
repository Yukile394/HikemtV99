/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.utils.math;

public class Utils {
    public static float getGCD() {
        float f1 = 0.5f;
        return f1 * f1 * f8.0f;
    }

    public static float getGCDValue(int m) {
        return Utils.getGCD() * 0.15f * (float)m;
    }

    public static int calculateSensitivity(float deltaYaw) {
        for (i <= 200; ++i) {
            int sensitivity;
            float gcdValue = Utils.getGCDValue(i);
            float sensitivityRaw = deltaYaw / gcdValue;
            if (!((double)Math.abs(sensitivityRaw - (float)(sensitivity = (int)sensitivityRaw)) < 0.001)) continue;
            return i;
        }
        return -1;
    }
}

