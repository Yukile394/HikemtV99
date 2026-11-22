/*
 * Decompiled with CFR 0.152.
 */
package ai.idealistic.spartan.compatibility.manual.vanilla;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.utils.java.ReflectionUtils;

public class Attributes {
    public static final boolean classExists = ReflectionUtils.classExists("org.bukkit.attribute.Attribute") && ReflectionUtils.classExists("org.bukkit.attribute.AttributeInstance") && ReflectionUtils.classExists("org.bukkit.attribute.AttributeModifier");
    public static final String GENERIC_MAX_HEALTH = "GENERIC_MAX_HEALTH";
    public static final String GENERIC_FOLLOW_RANGE = "GENERIC_FOLLOW_RANGE";
    public static final String GENERIC_LUCK = "GENERIC_LUCK";
    public static final String ZOMBIE_SPAWN_REINFORCEMENTS = "ZOMBIE_SPAWN_REINFORCEMENTS";
    public static final String GENERIC_GRAVITY = "GENERIC_GRAVITY";
    public static final String GENERIC_MAX_ABSORPTION = "GENERIC_MAX_ABSORPTION";
    public static final String GENERIC_MOVEMENT_SPEED = "GENERIC_MOVEMENT_SPEED";
    public static final String GENERIC_FLYING_SPEED = "GENERIC_FLYING_SPEED";
    public static final String GENERIC_ATTACK_DAMAGE = "GENERIC_ATTACK_DAMAGE";
    public static final String GENERIC_ATTACK_SPEED = "GENERIC_ATTACK_SPEED";
    public static final String GENERIC_ATTACK_KNOCKBACK = "GENERIC_ATTACK_KNOCKBACK";
    public static final String GENERIC_KNOCKBACK_RESISTANCE = "GENERIC_KNOCKBACK_RESISTANCE";
    public static final String GENERIC_ARMOR = "GENERIC_ARMOR";
    public static final String GENERIC_ARMOR_TOUGHNESS = "GENERIC_ARMOR_TOUGHNESS";
    public static final String GENERIC_FALL_DAMAGE_MULTIPLIER = "GENERIC_FALL_DAMAGE_MULTIPLIER";
    public static final String GENERIC_SAFE_FALL_DISTANCE = "GENERIC_SAFE_FALL_DISTANCE";
    public static final String GENERIC_SCALE = "GENERIC_SCALE";
    public static final String GENERIC_STEP_HEIGHT = "GENERIC_STEP_HEIGHT";
    public static final String HORSE_JUMP_STRENGTH = "HORSE_JUMP_STRENGTH";
    public static final String GENERIC_JUMP_STRENGTH = "GENERIC_JUMP_STRENGTH";
    public static final String PLAYER_BLOCK_INTERACTION_RANGE = "PLAYER_BLOCK_INTERACTION_RANGE";
    public static final String PLAYER_ENTITY_INTERACTION_RANGE = "PLAYER_ENTITY_INTERACTION_RANGE";
    public static final String PLAYER_BLOCK_BREAK_SPEED = "PLAYER_BLOCK_BREAK_SPEED";

    public static double getAmount(PlayerProtocol p, String attributeString) {
        return Double.MIN_VALUE;
    }
}

