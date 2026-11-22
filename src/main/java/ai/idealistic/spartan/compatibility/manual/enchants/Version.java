/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.willfp.ecoenchants.enchant.EcoEnchant
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package ai.idealistic.spartan.compatibility.manual.enchants;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Version {
    public static boolean has(PlayerProtocol protocol) {
        if (Compatibility.CompatibilityType.ECO_ENCHANTS.isFunctional()) {
            PlayerInventory inventory = protocol.getInventory();
            for (ItemStack armor : inventory.getArmorContents()) {
                if (armor == null || !Version.has(armor)) continue;
                return true;
            }
            return Version.has(inventory.getItemInHand()) || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) && Version.has(inventory.getItemInOffHand());
        }
        return false;
    }

    private static boolean has(ItemStack item) {
        if (item.hasItemMeta()) {
            for (Enchantment enchantment : item.getItemMeta().getEnchants().keySet()) {
                if (!(enchantment instanceof EcoEnchant)) continue;
                return true;
            }
        }
        return false;
    }
}

