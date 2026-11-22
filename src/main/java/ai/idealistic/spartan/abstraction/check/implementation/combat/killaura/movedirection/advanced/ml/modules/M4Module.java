/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules;

import kireiko.dev.millennium.math.Simplification;
import kireiko.dev.millennium.ml.data.Class2;
import kireiko.dev.millennium.ml.data.module.FlagType;
import kireiko.dev.millennium.ml.data.module.Interface;
import kireiko.dev.millennium.ml.data.module.ModuleResultML;

public class M4Module
implements Interface {
    private static final double bW = 2.0;

    @Override
    public String getName() {
        return "m4";
    }

    @Override
    public ModuleResultML getResult(Class2 resultML) {
        Class2.CheckResultML stats = resultML.statisticsResult;
        double UNUSUAL = stats.UNUSUAL / 2.0;
        double STRANGE = stats.STRANGE / 2.0;
        double SUSPECTED = stats.SUSPECTED / 2.0;
        double SUSPICIOUSLY = stats.SUSPICIOUSLY / 2.0;
        String scaledUnusual = String.valueOf(Simplification.scaleVal(UNUSUAL, 3.0));
        if (UNUSUAL > 0.25 && STRANGE > 0.07 && SUSPECTED > 0.018 && SUSPICIOUSLY > 0.0) {
            return new ModuleResultML(10, FlagType.SUSPECTED, scaledUnusual);
        }
        if (UNUSUAL > 0.25 && STRANGE > 0.04 && SUSPECTED > 0.0) {
            return new ModuleResultML(10, FlagType.STRANGE, scaledUnusual);
        }
        return new ModuleResultML(0, FlagType.NORMAL, scaledUnusual);
    }

    @Override
    public int getParameterBuffer() {
        return 15;
    }
}

