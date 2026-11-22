/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 */
package me.vagdedes.spartan.api.system;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import lombok.Generated;

public class Enums {

    public static enum ToggleAction {
        ENABLE(ai.idealistic.spartan.api.ToggleAction.ENABLE),
        DISABLE(ai.idealistic.spartan.api.ToggleAction.DISABLE);

        private final ai.idealistic.spartan.api.ToggleAction toggleAction;

        private ToggleAction(ai.idealistic.spartan.api.ToggleAction toggleAction) {
        }

        @Generated
        public ai.idealistic.spartan.api.ToggleAction getToggleAction() {
            return this.toggleAction;
        }
    }

    public static enum Permission {
        CONDITION(ai.idealistic.spartan.api.Permission.CONDITION),
        WAVE(ai.idealistic.spartan.api.Permission.WAVE),
        ADMIN(ai.idealistic.spartan.api.Permission.ADMIN),
        RELOAD(ai.idealistic.spartan.api.Permission.RELOAD),
        KICK(ai.idealistic.spartan.api.Permission.KICK),
        BYPASS(ai.idealistic.spartan.api.Permission.BYPASS),
        MANAGE(ai.idealistic.spartan.api.Permission.MANAGE),
        INFO(ai.idealistic.spartan.api.Permission.INFO),
        WARN(ai.idealistic.spartan.api.Permission.WARN),
        USE_BYPASS(ai.idealistic.spartan.api.Permission.USE_BYPASS),
        NOTIFICATIONS(ai.idealistic.spartan.api.Permission.NOTIFICATIONS),
        BEDROCK(ai.idealistic.spartan.api.Permission.BEDROCK);

        private final ai.idealistic.spartan.api.Permission permission;

        private Permission(ai.idealistic.spartan.api.Permission permission) {
        }

        @Generated
        public ai.idealistic.spartan.api.Permission getPermission() {
            return this.permission;
        }
    }

    public static enum HackType {
        XRay(CheckEnums.HackType.X_RAY),
        NoSwing(CheckEnums.HackType.NO_SWING),
        IrregularMovements(CheckEnums.HackType.IRREGULAR_MOVEMENTS),
        ImpossibleActions(CheckEnums.HackType.IMPOSSIBLE_ACTIONS),
        InventoryClicks(CheckEnums.HackType.INVENTORY_CLICKS),
        Criticals(CheckEnums.HackType.CRITICALS),
        SpeedSimulation(CheckEnums.HackType.SPEED_SIMULATION),
        Exploits(CheckEnums.HackType.EXPLOITS),
        GravitySimulation(CheckEnums.HackType.GRAVITY_SIMULATION),
        BlockReach(CheckEnums.HackType.BLOCK_REACH),
        FastClicks(CheckEnums.HackType.FAST_CLICKS),
        FastHeal(CheckEnums.HackType.FAST_HEAL),
        ImpossibleInventory(CheckEnums.HackType.IMPOSSIBLE_INVENTORY),
        HitReach(CheckEnums.HackType.HIT_REACH),
        FastBreak(CheckEnums.HackType.FAST_BREAK),
        FastPlace(CheckEnums.HackType.FAST_PLACE),
        MorePackets(CheckEnums.HackType.MORE_PACKETS),
        FastEat(CheckEnums.HackType.FAST_EAT),
        Velocity(CheckEnums.HackType.VELOCITY),
        KillAura(CheckEnums.HackType.KILL_AURA);

        private final CheckEnums.HackType hackType;

        private HackType(CheckEnums.HackType hackType) {
        }

        @Generated
        public CheckEnums.HackType getHackType() {
            return this.hackType;
        }
    }
}

