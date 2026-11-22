/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.event;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;

public class PlayerTransactionEvent {
    public final PlayerProtocol protocol;
    public final long time = System.currentTimeMillis();
    public final long delay;

    public PlayerTransactionEvent(PlayerProtocol protocol) {
        this.delay = protocol.transactionPing;
    }
}

