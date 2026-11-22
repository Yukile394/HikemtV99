/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.utils.math.Distance;
import org.bukkit.util.NumberConversions;

public class KAMoveLength
extends CheckDetection {
    private static final double av = Math.pow(2.0, 24.0);
    private static final double aw = 16384.0;
    private static final double ax = 0.005;
    private static final double ay = 0.001;
    private Float az = Float.valueOf(0.0f);
    private Float aA = Float.valueOf(0.0f);
    private final Buffer.Storage aB = new Buffer.Storage();
    private final Buffer.Storage aC = new Buffer.Storage();
    private long aD;
    private long aE;

    KAMoveLength(CheckRunner executor) {
        super(executor, Check.DataType.JAVA, Check.DetectionType.PACKETS, "move_length", true, 2000L, 30000L, 40000L, 60000L);
    }

    void run() {
        this.call(() -> {
            if (this.aD - System.currentTimeMillis() <= 2000L && this.aE - System.currentTimeMillis() <= 1000L) {
                float pitch = this.protocol.getLocation().getPitch();
                float pitchDifference = Math.abs(pitch - this.az.floatValue());
                if ((double)pitchDifference > 0.0 && (double)this.aA.floatValue() > 0.0) {
                    double gcd = Distance.findGCD(16384.0, (double)pitchDifference * av, (double)this.aA.floatValue() * av) / av;
                    if (gcd <= 0.005) {
                        boolean safe = gcd <= 0.001;
                        Buffer.Storage buffer = safe ? this.aC : this.aB;
                        double d = buffer.count(1.0, 20);
                        int n = safe ? 5 : 13;
                        if (d >= (double)n) {
                            buffer.reset();
                            this.cancel("move-length, pitch: " + pitch + ", previous-pitch: " + this.az + ", pitch-difference: " + pitchDifference + ", previous-pitch-difference: " + this.aA + ", greatest-common-divisor: " + gcd + ", limit: " + 0.005, null, NumberConversions.ceil((double)20.0));
                        }
                    } else {
                        this.aB.decrease(1.0);
                    }
                }
                this.aA = Float.valueOf(pitchDifference);
                this.az = Float.valueOf(pitch);
            }
        });
    }

    void updateMoveLength() {
        this.call(() -> {
            this.aD = System.currentTimeMillis();
        });
    }

    void updateMoveLength2() {
        this.call(() -> {
            this.aE = System.currentTimeMillis();
        });
    }
}

