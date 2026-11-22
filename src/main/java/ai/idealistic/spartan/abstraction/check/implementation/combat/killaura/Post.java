/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.functionality.connection.IntegerDelays;

public class Post
extends CheckDetection {
    private int V = 0;
    private int ao = 0;
    private int ap = 0;
    private int aq = 0;
    private int ar = 0;
    private int as = 0;
    private long at = 0L;
    private long au = 0L;

    Post(CheckRunner executor) {
        super(executor, null, Check.DetectionType.PACKETS, "hit_time", true, 2000L, 30000L, 40000L, 60000L);
    }

    void run() {
        this.call(() -> {
            long hitTime = System.currentTimeMillis() - this.at;
            int deviance = (int)Math.abs(hitTime - this.au);
            this.aq += deviance;
            ++this.ar;
            if (this.protocol.getPing() > 1000) {
                return;
            }
            if (deviance < 5 && deviance != 0) {
                ++this.as;
            }
            if (hitTime == 0L) {
                this.ap = 2;
            }
            if (this.ap > 1) {
                this.V += 55;
                if (this.V >= 300) {
                    this.cancel("switch", this.protocol.getFromLocation());
                }
            }
            if (this.ar >= 5 && IntegerDelays.getPlayerTicksDelay(this.protocol.getEntityId()) < 2) {
                if (this.aq < 35) {
                    this.ao += this.aq < 10 ? 150 : 75;
                    if (this.ao >= 300) {
                        this.cancel("post(linear-analysis)", this.protocol.getFromLocation());
                    }
                } else if (this.aq > 45 && this.aq < 55) {
                    this.ao += 150;
                    if (this.ao >= 300) {
                        this.cancel("post(default)", this.protocol.getFromLocation());
                    }
                } else if (this.as >= 3) {
                    this.ao += 100;
                    if (this.ao >= 300) {
                        this.cancel("post(low)", this.protocol.getFromLocation());
                    }
                } else if (this.ao > 0) {
                    this.ao -= 50;
                }
                this.ar = 0;
                this.aq = 0;
                this.as = 0;
            }
            if (this.V > 0) {
                this.V -= 5;
            }
            if (this.ap > 0) {
                --this.ap;
            }
            this.at = System.currentTimeMillis();
            this.au = hitTime;
        });
    }
}

