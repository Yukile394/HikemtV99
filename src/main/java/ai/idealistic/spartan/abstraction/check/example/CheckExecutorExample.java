/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.example;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.check.example.DetectionExecutorExample;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;

public class CheckExecutorExample
extends CheckRunner {
    private final DetectionExecutorExample detection1 = new DetectionExecutorExample(this);
    private final CheckDetection detection2 = new ImplementedDetection(this, Check.DataType.JAVA, Check.DetectionType.PACKETS, "detection_option_name_in_checks_yml", true);

    public CheckExecutorExample(CheckEnums.HackType hackType, PlayerProtocol protocol, String playerName) {
        super(hackType, protocol);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
    }

    @Override
    protected boolean canRun() {
        this.detection1.customMethod2();
        return true;
    }
}

