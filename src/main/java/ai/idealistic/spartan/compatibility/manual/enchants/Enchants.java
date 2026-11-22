/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  me.darrionat.CustomEnchantsAPI
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package ai.idealistic.spartan.compatibility.manual.enchants;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import me.darrionat.CustomEnchantsAPI;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Enchants {
    public static boolean has(PlayerProtocol protocol) {
        if (Compatibility.CompatibilityType.CUSTOM_ENCHANTS_PLUS.isFunctional()) {
            PlayerInventory inventory = protocol.getInventory();
            for (ItemStack armor : inventory.getArmorContents()) {
                if (armor == null || !Enchants.has(armor)) continue;
                return true;
            }
            return Enchants.has(inventory.getItemInHand()) || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) && Enchants.has(inventory.getItemInOffHand());
        }
        return false;
    }

    private static boolean has(ItemStack item) {
        if (item.hasItemMeta()) {
            for (Enchantment enchantment : item.getItemMeta().getEnchants().keySet()) {
                if (!CustomEnchantsAPI.isCustomEnchantment((Enchantment)enchantment)) continue;
                return true;
            }
        }
        return false;
    }
}

