/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  ai.idealistic.spartan.abstraction.event.RotationEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection.advanced.HeuristicComponent;
import ai.idealistic.spartan.abstraction.event.RotationEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import java.util.ArrayList;
import java.util.List;
import kireiko.dev.millennium.math.MathCollection;

public final class AimConstantCheck
implements HeuristicComponent {
    private static final double bx = 60.0;
    private static final double by = 0.1;
    private static final float bz = 0.1f;
    private static final float bA = 20.0f;
    private final CheckDetection A;
    private final PlayerProtocol profile;
    private final List<Double> bB = new ArrayList<Double>();
    private float bC = 0.0f;
    private float lastDeltaPitch = 0.0f;
    private float bD = 0.0f;
    private float bE = 0.0f;
    private float bF = 0.0f;

    public AimConstantCheck(CheckDetection detection, PlayerProtocol protocol) {
        this.A = detection;
        this.profile = protocol;
    }

    @Override
    public void process(RotationEvent rotationUpdate) {
        boolean invalidY;
        boolean invalidX;
        double floorModuloY;
        double floorModuloX;
        double moduloY;
        double moduloX;
        boolean validDelta;
        boolean invalid;
        if (rotationUpdate.getAbsDelta().getY() == 0.0f && rotationUpdate.getAbsDelta().getY() == 0.0f) {
            return;
        }
        float deltaYaw = rotationUpdate.getAbsDelta().getX();
        float deltaPitch = rotationUpdate.getAbsDelta().getY();
        double sensitivity = this.profile.calculateSensitivity();
        boolean sensitivityTooLow = sensitivity < 50.0 && sensitivity > -1.0 || this.profile.getSensitivityProcessor().totalSensitivityClient < 50;
        double divisorYaw = MathCollection.getGcd((long)((double)deltaYaw * MathCollection.EXPANDER), (long)((double)this.bC * MathCollection.EXPANDER));
        double divisorPitch = MathCollection.getGcd((long)((double)deltaPitch * MathCollection.EXPANDER), (long)((double)this.lastDeltaPitch * MathCollection.EXPANDER));
        double constantYaw = divisorYaw / MathCollection.EXPANDER;
        double constantPitch = divisorPitch / MathCollection.EXPANDER;
        long expandedPitch = (long)(MathCollection.EXPANDER * (double)deltaPitch);
        long expandedLastPitch = (long)(MathCollection.EXPANDER * (double)this.lastDeltaPitch);
        long gcd = MathCollection.getGcd(expandedPitch, expandedLastPitch);
        boolean validAngles = deltaYaw > 0.25f && deltaPitch > 0.25f && deltaPitch < 20.0f && deltaYaw < 20.0f;
        invalid = gcd < 131072L;
        if (invalid && validAngles && !sensitivityTooLow) {
            this.bD = Math.min(this.bD + 1.0f, 200.0f);
            if (this.bD > 10.0f) {
                this.A.call(() -> this.A.cancel("Constant rotations 3", this.profile.getFromLocation()));
                this.bD = 4.0f;
            }
        } else if (this.bD > 0.0f) {
            this.bD -= 2.0f;
        }
        double currentX = (double)deltaYaw / constantYaw;
        double currentY = (double)deltaPitch / constantPitch;
        double previousX = (double)this.bC / constantYaw;
        double previousY = (double)this.lastDeltaPitch / constantPitch;
        validDelta = deltaYaw > 0.1f && deltaPitch > 0.1f && deltaYaw < 20.0f && deltaPitch < 20.0f;
        if (validDelta) {
            moduloX = currentX % previousX;
            moduloY = currentY % previousY;
            floorModuloX = Math.abs(Math.floor(moduloX) - moduloX);
            floorModuloY = Math.abs(Math.floor(moduloY) - moduloY);
            invalidX = moduloX > 60.0 && floorModuloX > 0.1;
            invalidY = moduloY > 60.0 && floorModuloY > 0.1;
            if (invalidX && invalidY && !sensitivityTooLow) {
                this.bE = Math.min(this.bE + 1.0f, 200.0f);
                if (this.bE > 6.0f) {
                    this.A.call(() -> this.A.cancel("Constant rotations 2", this.profile.getFromLocation()));
                    this.bE = 4.0f;
                }
            } else if (this.bE > 0.0f) {
                this.bE -= 2.0f;
            }
        }
        currentX = (double)deltaYaw / constantYaw;
        currentY = (double)deltaPitch / constantPitch;
        previousX = (double)this.bC / constantYaw;
        previousY = (double)this.lastDeltaPitch / constantPitch;
        validDelta = deltaYaw > 0.1f && deltaPitch > 0.1f && deltaYaw < 20.0f && deltaPitch < 20.0f;
        if (validDelta) {
            moduloX = currentX % previousX;
            moduloY = currentY % previousY;
            floorModuloX = Math.abs(Math.floor(moduloX) - moduloX);
            floorModuloY = Math.abs(Math.floor(moduloY) - moduloY);
            invalidX = moduloX > 60.0 && floorModuloX > 0.1;
            invalidY = moduloY > 60.0 && floorModuloY > 0.1;
            if (invalidX && invalidY && !sensitivityTooLow) {
                this.bF = Math.max(this.bF + (deltaPitch < 1.0f || deltaPitch > 13.0f ? 2.0f : 1.0f), 0.0f);
                float limit = 7.0f;
                float f = this.profile.calculateSensitivity() < 70 ? 8.0f : 7.0f;
                if (this.bF > f) {
                    this.A.call(() -> this.A.cancel("Constant rotations 3", this.profile.getFromLocation()));
                    this.bF = 0.0f;
                }
            } else if (this.bF > 0.0f) {
                this.bF -= 2.0f;
            }
        }
        this.bC = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
    }
}

