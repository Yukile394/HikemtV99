/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package kireiko.dev.millennium.ml;

import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules.M1Module;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules.M2Module;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules.M3Module;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules.M4Module;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules.M5Module;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules.MHuge1;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml.modules.MHuge2;
import java.util.Arrays;
import java.util.List;
import kireiko.dev.millennium.ml.FactoryML;
import kireiko.dev.millennium.ml.data.module.Interface;
import kireiko.dev.millennium.ml.logic.Logger;
import kireiko.dev.millennium.ml.logic.ModelVer;
import lombok.Generated;

public final class Model {
    public static final boolean DEV_MODE = false;
    public static final String CLIENT_NAME = "001-premium-complex-18k";
    private static final int TABLE_SIZE = 2;
    public static final List<Interface> MODEL_LIST = Arrays.asList(new M1Module(), new M2Module(), new M3Module(), new M4Module(), new M5Module(), new MHuge1(), new MHuge2());

    public static void run() {
        for (int i = 0; i < MODEL_LIST.size(); ++i) {
            Interface moduleML = MODEL_LIST.get(i);
            FactoryML.loadFromResources(i, MODEL_LIST.get(i).getName() + ".dat", 2, moduleML.getParameterBuffer(), ModelVer.VERSION_4_5);
        }
        Logger.info("001-premium-complex-18k loaded!");
    }

    @Generated
    private Model() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

