/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.GameMode
 */
package ai.idealistic.spartan.abstraction.data;

import ai.idealistic.spartan.abstraction.event.PlayerTickEvent;
import lombok.Generated;
import org.bukkit.GameMode;

public class OtherTimerBalancer {
    private long balancer = 1000L;
    private long result = 0L;
    private int negative;
    private int forced = 0;
    private int latency = 0;

    public void pushDelay(PlayerTickEvent event) {
        GameMode gameMode = event.protocol.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE) {
            this.balancer = 1000L;
            this.result = 0L;
            this.forced = 0;
            this.latency = 0;
            return;
        }
        long delay = event.getDelay();
        this.forced = delay > 15L && delay < 48L ? this.forced + 1 : 0;
        int n = this.latency = delay > 40L && delay < 48L ? this.latency + 10 : this.latency / 2;
        if (delay > 50L) {
            this.balancer += delay - 50L;
            if (delay > 53L) {
                ++this.negative;
            }
        } else if (delay < 50L) {
            long deviance = 50L - delay;
            if (this.balancer - deviance >= 0L) {
                this.balancer -= deviance;
            } else {
                long totalDeviance = deviance - this.balancer;
                this.result += totalDeviance;
                this.balancer = 0L;
            }
            this.negative = 0;
        } else if (this.result >= 0L) {
            this.result /= 2L;
            this.balancer /= 2L;
            this.negative = 0;
        }
    }

    public boolean isNegativeTimer() {
        return this.negative > 15;
    }

    public void addBalance(long balance) {
        this.balancer += balance;
    }

    @Generated
    public long getBalancer() {
        return this.balancer;
    }

    @Generated
    public long getResult() {
        return this.result;
    }

    @Generated
    public int getNegative() {
        return this.negative;
    }

    @Generated
    public int getForced() {
        return this.forced;
    }

    @Generated
    public int getLatency() {
        return this.latency;
    }
}

