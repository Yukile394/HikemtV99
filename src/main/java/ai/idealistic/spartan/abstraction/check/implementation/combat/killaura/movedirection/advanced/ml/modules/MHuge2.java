/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules;

import kireiko.dev.millennium.ml.data.Class2;
import kireiko.dev.millennium.ml.data.module.FlagType;
import kireiko.dev.millennium.ml.data.module.Interface;
import kireiko.dev.millennium.ml.data.module.ModuleResultML;

public class MHuge2
implements Interface {
    private static final double bW = 2.0;

    @Override
    public String getName() {
        return "m_huge2";
    }

    @Override
    public ModuleResultML getResult(Class2 resultML) {
        Class2.CheckResultML checkResult = resultML.statisticsResult;
        double ab1 = checkResult.UNUSUAL / 2.0;
        double ab2 = checkResult.STRANGE / 2.0;
        double ab3 = checkResult.SUSPECTED / 2.0;
        double ab4 = checkResult.SUSPICIOUSLY / 2.0;
        FlagType type = FlagType.NORMAL;
        if (ab1 > 0.6) {
            type = FlagType.SUSPECTED;
        } else if (ab1 > 0.5 && ab2 < 0.042) {
            type = FlagType.STRANGE;
        } else if (ab4 > 0.0 && ab2 > 0.17 && ab1 > 0.35) {
            type = FlagType.UNUSUAL;
        }
        return new ModuleResultML(30, type, checkResult.toString());
    }

    @Override
    public int getParameterBuffer() {
        return 15;
    }
}

