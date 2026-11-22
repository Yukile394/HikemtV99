/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package ai.idealistic.spartan.abstraction.event;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import lombok.Generated;

public class PlayerTickEvent {
    public final PlayerProtocol protocol;
    public final long time = System.currentTimeMillis();
    public final boolean legacy;
    public final boolean onGround;
    private long delay;

    public PlayerTickEvent(PlayerProtocol protocol, boolean legacy, boolean onGround) {
        this.delay = -1L;
    }

    public PlayerTickEvent build() {
        this.delay = this.time - this.protocol.tickTime;
        if (this.legacy) {
            if (this.delay > 1020L && this.delay < 1060L) {
                this.delay -= 1000L;
            } else if (this.delay > 950L) {
                this.delay -= 950L;
            }
        }
        this.protocol.tickTime = this.time;
        return this;
    }

    @Generated
    public long getDelay() {
        return this.delay;
    }
}

