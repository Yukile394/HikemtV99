/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 */
package ai.idealistic.spartan.utils.minecraft.inventory;

import org.bukkit.enchantments.Enchantment;

public class Arrow {
    public static final Enchantment ARROW_KNOCKBACK;
    public static final Enchantment DURABILITY;
    public static final Enchantment WATER_WORKER;
    public static final Enchantment DIG_SPEED;

    private static Enchantment findEnchantment(String string) {
        for (Enchantment enchantment : Enchantment.values()) {
            if (!enchantment.getName().equalsIgnoreCase(string)) continue;
            return enchantment;
        }
        return null;
    }

    static {
        Enchantment enchantment = Arrow.findEnchantment("ARROW_KNOCKBACK");
        if (enchantment == null) {
            enchantment = Enchantment.PUNCH;
        }
        ARROW_KNOCKBACK = enchantment;
        enchantment = Arrow.findEnchantment("DURABILITY");
        if (enchantment == null) {
            enchantment = Enchantment.UNBREAKING;
        }
        DURABILITY = enchantment;
        enchantment = Arrow.findEnchantment("WATER_WORKER");
        if (enchantment == null) {
            enchantment = Enchantment.AQUA_AFFINITY;
        }
        WATER_WORKER = enchantment;
        enchantment = Arrow.findEnchantment("DIG_SPEED");
        if (enchantment == null) {
            enchantment = Enchantment.EFFICIENCY;
        }
        DIG_SPEED = enchantment;
    }
}

