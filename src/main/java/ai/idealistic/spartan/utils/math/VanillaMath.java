/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.utils.math;

import ai.idealistic.spartan.utils.math.ClientMath;
import ai.idealistic.spartan.utils.math.MathHelper;

public class VanillaMath
implements ClientMath {
    @Override
    public float sin(float value) {
        return MathHelper.sin(value);
    }

    @Override
    public float cos(float value) {
        return MathHelper.cos(value);
    }

    public static float sqrt(float f) {
        return (float)Math.sqrt(f);
    }
}

