/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  ai.idealistic.spartan.abstraction.event.RotationEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.ml;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.HeuristicComponent;
import ai.idealistic.spartan.abstraction.event.RotationEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import kireiko.dev.millennium.ml.Model;
import kireiko.dev.millennium.ml.FactoryML;
import kireiko.dev.millennium.ml.data.Pitch;
import kireiko.dev.millennium.ml.data.Class2;
import kireiko.dev.millennium.ml.data.module.FlagType;
import kireiko.dev.millennium.ml.data.module.Interface;
import kireiko.dev.millennium.ml.data.module.ModuleResultML;
import kireiko.dev.millennium.ml.logic.MillenniumScheduler;
import kireiko.dev.millennium.types.Pair;
import kireiko.dev.millennium.types.Delta;

public final class Pair
implements HeuristicComponent {
    private static final boolean bO = false;
    private static final int bP = 0;
    private static final Pair<Integer, String> bQ = new Pair<Integer, String>(0, "plugins/Spartan/models/m_huge1.dat");
    private static final double bR = 1.0;
    private static final double bS = 1.0;
    private static final double bT = 1.0;
    private static final int bm = 600;
    private static final int bU = 2500;
    private final CheckDetection A;
    private final PlayerProtocol profile;
    private final List<Delta> be;

    public Pair(CheckDetection detection, PlayerProtocol protocol) {
        this.A = detection;
        this.profile = protocol;
        this.be = new ArrayList<Delta>();
    }

    @Override
    public void process(RotationEvent rotationUpdate) {
        if (this.profile.isCinematic()) {
            return;
        }
        if (rotationUpdate.getAbsDelta().getY() == 0.0f && rotationUpdate.getAbsDelta().getY() == 0.0f) {
            return;
        }
        this.be.add(new Delta(rotationUpdate.getAbsDelta().getX(), rotationUpdate.getAbsDelta().getY()));
        if (this.be.size() >= 600) {
            this.H();
        }
    }

    private void checkCheckCancellation(List<Pitch> objectML) {
        MillenniumScheduler.run(() -> {
            ArrayList<Class2> results = new ArrayList<Class2>();
            HashSet<String> modelsThatFlagged = new HashSet<String>();
            ModuleResultML finalModuleResult = new ModuleResultML(0, FlagType.NORMAL, null);
            for (i < Model.MODEL_LIST.size(); ++i) {
                Class2 resultML = FactoryML.getModel(i).checkData(objectML);
                Interface moduleML = Model.MODEL_LIST.get(i);
                ModuleResultML moduleResultML = moduleML.getResult(resultML);
                if (moduleResultML.getType() != FlagType.NORMAL) {
                    modelsThatFlagged.add(moduleML.getName());
                }
                if (finalModuleResult.getInfo() == null) {
                    finalModuleResult = moduleResultML;
                } else {
                    boolean isPrioritySmaller;
                    int tempLevel;
                    int finalLevel = finalModuleResult.getType().getLevel();
                    boolean isLevelSmaller = finalLevel < (tempLevel = moduleResultML.getType().getLevel());
                    isPrioritySmaller = finalLevel == tempLevel && finalModuleResult.getPriority() < moduleResultML.getPriority();
                    if (isLevelSmaller || isPrioritySmaller) {
                        finalModuleResult = moduleResultML;
                    }
                }
                results.add(resultML);
            }
            if (finalModuleResult.getType() != FlagType.NORMAL) {
                double vl;
                FlagType type = finalModuleResult.getType();
                switch (type) {
                    case UNUSUAL: {
                        vl = 1.0;
                        break;
                    }
                    case STRANGE: {
                        vl = 1.0;
                        break;
                    }
                    case SUSPECTED: {
                        vl = 1.0;
                        break;
                    }
                    default: {
                        vl = 0.0;
                    }
                }
                String reason = "Result: " + (Object)type + " " + Arrays.toString(modelsThatFlagged.toArray());
                this.A.call(() -> this.A.cancel(vl, "ML (" + reason + ")", this.profile.getFromLocation()));
            }
        });
    }

    private void H() {
        ArrayList<Pitch> objectMLStack = new ArrayList<Pitch>();
        Pitch yaw = new Pitch(new ArrayList<Double>());
        Pitch pitch = new Pitch(new ArrayList<Double>());
        for (Delta rot : this.be) {
            yaw.getValues().add(Double.valueOf(rot.getX()));
            pitch.getValues().add(Double.valueOf(rot.getY()));
        }
        objectMLStack.add(yaw);
        objectMLStack.add(pitch);
        this.checkCheckCancellation(objectMLStack);
        this.be.clear();
    }
}

