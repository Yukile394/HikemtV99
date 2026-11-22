/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules;

import kireiko.dev.millennium.math.Simplification;
import kireiko.dev.millennium.ml.data.Class2;
import kireiko.dev.millennium.ml.data.module.FlagType;
import kireiko.dev.millennium.ml.data.module.Interface;
import kireiko.dev.millennium.ml.data.module.ModuleResultML;

public class M5Module
implements Interface {
    private static final double bW = 2.0;

    @Override
    public String getName() {
        return "m5";
    }

    @Override
    public ModuleResultML getResult(Class2 resultML) {
        Class2.CheckResultML checkResult = resultML.statisticsResult;
        double ab1 = checkResult.UNUSUAL / 2.0;
        double ab2 = checkResult.STRANGE / 2.0;
        double ab3 = checkResult.SUSPECTED / 2.0;
        double ab4 = checkResult.SUSPICIOUSLY / 2.0;
        FlagType type = FlagType.NORMAL;
        double percent = Math.min(Simplification.scaleVal(ab1.2500.0, 2.0), 100.0);
        if (ab1 > 0.95 || ab1 > 0.7 && ab2 > 0.345 && ab3 > 0.125 && ab4 > 0.035 && percent > 77.0) {
            type = FlagType.SUSPECTED;
        } else if (ab1 > 0.825 && percent > 77.0) {
            type = FlagType.SUSPECTED;
        } else if (ab1 > 0.475 && ab2 < 0.145 && ab3 > 0.1) {
            type = FlagType.STRANGE;
        } else if (ab1 < 0.25 && ab2 < 0.033 && ab2 != 0.0 && ab3 == 0.0 && ab4 == 0.0) {
            type = FlagType.STRANGE;
        }
        return new ModuleResultML(15, type, checkResult.toString());
    }

    @Override
    public int getParameterBuffer() {
        return 10;
    }
}

