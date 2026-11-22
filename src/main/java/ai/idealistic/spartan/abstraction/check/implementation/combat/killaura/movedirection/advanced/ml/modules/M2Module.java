/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules;

import kireiko.dev.millennium.math.Simplification;
import kireiko.dev.millennium.ml.data.Class2;
import kireiko.dev.millennium.ml.data.module.FlagType;
import kireiko.dev.millennium.ml.data.module.Interface;
import kireiko.dev.millennium.ml.data.module.ModuleResultML;

public class M2Module
implements Interface {
    private static final double bW = 2.0;

    @Override
    public String getName() {
        return "m2";
    }

    @Override
    public ModuleResultML getResult(Class2 resultML) {
        Class2.CheckResultML stats = resultML.statisticsResult;
        double UNUSUAL = stats.UNUSUAL / 2.0;
        double SUSPECTED = stats.SUSPECTED / 2.0;
        FlagType type = UNUSUAL > 0.5 || UNUSUAL > 0.4 && SUSPECTED > 0.15 ? FlagType.SUSPECTED : (UNUSUAL > 0.4 ? FlagType.STRANGE : (UNUSUAL > 0.3 ? FlagType.UNUSUAL : FlagType.NORMAL));
        int score = type == FlagType.NORMAL ? 0 : 10;
        String message = String.valueOf(Simplification.scaleVal(UNUSUAL, 3.0));
        return new ModuleResultML(score, type, message);
    }

    @Override
    public int getParameterBuffer() {
        return 15;
    }
}

