/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 */
package ai.idealistic.spartan.functionality.tracking;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.compatibility.manual.building.Stack;
import ai.idealistic.spartan.compatibility.manual.vanilla.Attributes;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class CheckConditions {
    public static boolean canCheckMovement(PlayerProtocol protocol, boolean vehicle, boolean elytra, boolean flight, boolean playerAttributes, boolean environmentalAttributes) {
        if (!(!elytra && protocol.isGliding() || !flight && protocol.wasFlying() || !vehicle && protocol.getVehicle() != null || !playerAttributes && (Attributes.getAmount(protocol, "GENERIC_MOVEMENT_SPEED") != Double.MIN_VALUE || Attributes.getAmount(protocol, "GENERIC_JUMP_STRENGTH") != Double.MIN_VALUE) || !environmentalAttributes && (Attributes.getAmount(protocol, "GENERIC_STEP_HEIGHT") != Double.MIN_VALUE || Attributes.getAmount(protocol, "GENERIC_GRAVITY") != Double.MIN_VALUE))) {
            List<Entity> entities;
            if ((Compatibility.CompatibilityType.MYTHIC_MOBS.isFunctional() || Compatibility.CompatibilityType.ITEMS_ADDER.isFunctional()) && !(entities = protocol.getNearbyEntities(6.0, 6.0, 6.0)).isEmpty()) {
                for (Entity entity : entities) {
                    if (!Stack.is(entity) && !ItemsAdder.is(entity)) continue;
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean canCheckCombat(PlayerProtocol protocol) {
        if (!(protocol.isLowEyeHeight() || protocol.wasGliding() || protocol.wasFlying() || protocol.getVehicle() != null || Attributes.getAmount(protocol, "GENERIC_ARMOR") != Double.MIN_VALUE || Attributes.getAmount(protocol, "GENERIC_ATTACK_SPEED") != Double.MIN_VALUE || Attributes.getAmount(protocol, "GENERIC_KNOCKBACK_RESISTANCE") != Double.MIN_VALUE || Attributes.getAmount(protocol, "PLAYER_ENTITY_INTERACTION_RANGE") != Double.MIN_VALUE)) {
            GameMode gameMode = protocol.getGameMode();
            return gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE || gameMode == GameMode.CREATIVE;
        }
        return false;
    }

    public static boolean canCheckCombat(PlayerProtocol protocol, LivingEntity entity) {
        return !protocol.bukkit().getName().equals(entity.getName()) && !Stack.is((Entity)entity) && !ItemsAdder.is((Entity)entity);
    }
}

