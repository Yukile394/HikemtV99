/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.example;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;

public class DetectionExecutorExample
extends CheckDetection {
    DetectionExecutorExample(CheckRunner executor) {
        super(executor, null, null, "detection_option_name_in_checks_yml", true);
    }

    void customMethod1() {
    }

    boolean customMethod2() {
        return true;
    }
}

