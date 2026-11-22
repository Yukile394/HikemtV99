/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.movedirection;

import org.bukkit.entity.Entity;

public class AttackHandle {
    final Entity aL;
    final long time;

    AttackHandle(Entity target, long timeMillis) {
        this.time = timeMillis;
        this.aL = target;
    }
}

