/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules;

import kireiko.dev.millennium.math.Simplification;
import kireiko.dev.millennium.ml.data.Class2;
import kireiko.dev.millennium.ml.data.module.FlagType;
import kireiko.dev.millennium.ml.data.module.Interface;
import kireiko.dev.millennium.ml.data.module.ModuleResultML;

public class M1Module
implements Interface {
    private static final double bW = 2.0;

    @Override
    public String getName() {
        return "m1";
    }

    @Override
    public ModuleResultML getResult(Class2 resultML) {
        Class2.CheckResultML checkResult = resultML.statisticsResult;
        double UNUSUAL = checkResult.UNUSUAL / 2.0;
        double STRANGE = checkResult.STRANGE / 2.0;
        double SUSPECTED = checkResult.SUSPECTED / 2.0;
        double SUSPICIOUSLY = checkResult.SUSPICIOUSLY / 2.0;
        String scaledUnusual = String.valueOf(Simplification.scaleVal(UNUSUAL, 3.0));
        if (UNUSUAL > 0.8 && STRANGE > 0.15) {
            return new ModuleResultML(20, FlagType.SUSPECTED, "Blatant prohibition (" + scaledUnusual + ")");
        }
        if (UNUSUAL > 0.5 && STRANGE > 0.11 && SUSPICIOUSLY > 0.1) {
            return new ModuleResultML(10, FlagType.SUSPECTED, "Suspected prohibition (" + scaledUnusual + ")");
        }
        if (UNUSUAL > 0.3 && STRANGE > 0.11 && SUSPICIOUSLY > 0.11) {
            return new ModuleResultML(15, FlagType.SUSPECTED, "Suspected prohibition (" + scaledUnusual + ")");
        }
        if (UNUSUAL > 0.45 && STRANGE > 0.21 && SUSPECTED > 0.0) {
            return new ModuleResultML(10, FlagType.STRANGE, "Strange prohibition (" + scaledUnusual + ")");
        }
        if (UNUSUAL > 0.3 && STRANGE > 0.085 && SUSPECTED > 0.06 && SUSPICIOUSLY > 0.0) {
            return new ModuleResultML(10, FlagType.STRANGE, "Strange prohibition (" + scaledUnusual + ")");
        }
        if (UNUSUAL > 0.27 && STRANGE > 0.06 && SUSPECTED > 0.04 && SUSPICIOUSLY > 0.0) {
            return new ModuleResultML(8, FlagType.UNUSUAL, "Unusual prohibition (" + scaledUnusual + ")");
        }
        return new ModuleResultML(0, FlagType.NORMAL, scaledUnusual);
    }

    @Override
    public int getParameterBuffer() {
        return 15;
    }
}

