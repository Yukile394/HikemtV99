/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffectType
 */
package ai.idealistic.spartan.utils.minecraft.entity;

import org.bukkit.potion.PotionEffectType;

public class PotionEffectUtils {
    public static final PotionEffectType JUMP;
    public static final PotionEffectType FAST_DIGGING;
    public static final PotionEffectType SLOW_DIGGING;

    static {
        PotionEffectType effect = PotionEffectType.getByName("JUMP");
        if (effect == null) {
            effect = PotionEffectType.JUMP_BOOST;
        }
        JUMP = effect;
        effect = PotionEffectType.getByName("FAST_DIGGING");
        if (effect == null) {
            effect = PotionEffectType.HASTE;
        }
        FAST_DIGGING = effect;
        effect = PotionEffectType.getByName("SLOW_DIGGING");
        if (effect == null) {
            effect = PotionEffectType.MINING_FATIGUE;
        }
        SLOW_DIGGING = effect;
    }
}

