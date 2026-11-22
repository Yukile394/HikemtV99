/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules;

import kireiko.dev.millennium.math.Simplification;
import kireiko.dev.millennium.ml.data.Class2;
import kireiko.dev.millennium.ml.data.module.FlagType;
import kireiko.dev.millennium.ml.data.module.Interface;
import kireiko.dev.millennium.ml.data.module.ModuleResultML;

public class M3Module
implements Interface {
    private static final double bW = 2.0;

    @Override
    public String getName() {
        return "m3";
    }

    @Override
    public ModuleResultML getResult(Class2 resultML) {
        Class2.CheckResultML stats = resultML.statisticsResult;
        double UNUSUAL = stats.UNUSUAL / 2.0;
        double STRANGE = stats.STRANGE / 2.0;
        double SUSPECTED = stats.SUSPECTED / 2.0;
        double SUSPICIOUSLY = stats.SUSPICIOUSLY / 2.0;
        String scaledUnusual = String.valueOf(Simplification.scaleVal(UNUSUAL, 3.0));
        if (UNUSUAL > 0.3 && STRANGE > 0.2 && SUSPECTED > 0.14 && SUSPICIOUSLY > 0.07) {
            return new ModuleResultML(20, FlagType.SUSPECTED, scaledUnusual);
        }
        if (UNUSUAL > 0.34 && STRANGE > 0.12 && SUSPECTED > 0.1 && SUSPICIOUSLY > 0.0 || UNUSUAL > 0.25 && STRANGE > 0.12 && SUSPECTED > 0.12 || UNUSUAL > 0.1 && STRANGE > 0.14 && SUSPECTED > 0.08 && SUSPICIOUSLY > 0.0) {
            return new ModuleResultML(20, FlagType.STRANGE, scaledUnusual);
        }
        return new ModuleResultML(0, FlagType.NORMAL, scaledUnusual);
    }

    @Override
    public int getParameterBuffer() {
        return 15;
    }
}

