/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection;

import java.util.UUID;

public class AttackData {
    final double aJ;
    final float yaw;
    final float pitch;
    final long time;
    final UUID aK;

    AttackData(double accuracy, float yaw, float pitch, long time, UUID target) {
        this.aJ = accuracy;
        this.aK = target;
    }
}

