/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;

public abstract class CheckProcess {
    public final CheckEnums.HackType hackType;
    public final PlayerProtocol protocol;

    protected CheckProcess(CheckEnums.HackType hackType, PlayerProtocol protocol) {
    }
}

