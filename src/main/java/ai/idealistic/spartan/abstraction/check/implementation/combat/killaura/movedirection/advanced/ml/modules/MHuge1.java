/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules;

import kireiko.dev.millennium.ml.data.Class2;
import kireiko.dev.millennium.ml.data.module.FlagType;
import kireiko.dev.millennium.ml.data.module.Interface;
import kireiko.dev.millennium.ml.data.module.ModuleResultML;

public class MHuge1
implements Interface {
    private static final double bW = 2.0;

    @Override
    public String getName() {
        return "m_huge1";
    }

    @Override
    public ModuleResultML getResult(Class2 resultML) {
        Class2.CheckResultML checkResult = resultML.statisticsResult;
        double ab1 = checkResult.UNUSUAL / 2.0;
        double ab2 = checkResult.STRANGE / 2.0;
        double ab3 = checkResult.SUSPECTED / 2.0;
        double ab4 = checkResult.SUSPICIOUSLY / 2.0;
        FlagType type = FlagType.NORMAL;
        if (ab1 > 1.0) {
            double percent = Math.min((ab10.0) * 100.0, 100.0);
            if (percent > 80.0) {
                type = FlagType.SUSPECTED;
            } else if (percent > 70.0) {
                type = FlagType.STRANGE;
            }
        } else if (ab1 < 0.75 && ab4 < 0.02 && ab4 != 0.0 && ab3 == 0.0) {
            type = FlagType.STRANGE;
        } else if (ab1 < 0.25 && ab2 < 0.12 && ab3 < 0.03 && ab4 < 0.015) {
            type = FlagType.UNUSUAL;
        }
        return new ModuleResultML(30, type, checkResult.toString());
    }

    @Override
    public int getParameterBuffer() {
        return 10;
    }
}

