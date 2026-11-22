/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  io.lumine.mythic.bukkit.BukkitAPIHelper
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 */
package ai.idealistic.spartan.compatibility.manual.building;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.java.StringUtils;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import java.io.File;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Stack {
    private static final HashSet<ItemStack> hm = new HashSet();

    public static void reload() {
        hm.clear();
        File[] files = new File("/plugins/MythicMobs/Items/").listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isFile() || !file.getName().endsWith(".yml")) continue;
                YamlConfiguration filea = YamlConfiguration.loadConfiguration((File)file);
                for (String key : filea.getKeys(false)) {
                    String id;
                    Material material;
                    if (!filea.contains(key + ".Id") || !filea.contains(key + ".Display") || (material = Material.getMaterial(String(id = filea.getString(key + ".Id")).toUpperCase())) == null) continue;
                    ItemStack item = new ItemStack(material);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.stripColor((String)StringUtils.getClearColorSyntaxString(filea.getString(key + ".Display"))));
                    item.setItemMeta(meta);
                    hm.add(item);
                }
            }
        }
    }

    public static boolean is(Entity entity) {
        return Compatibility.CompatibilityType.MYTHIC_MOBS.isFunctional() && new BukkitAPIHelper().isMythicMob(entity);
    }

    public static boolean is(PlayerProtocol protocol) {
        if (Compatibility.CompatibilityType.MYTHIC_MOBS.isFunctional()) {
            PlayerInventory inventory = protocol.getInventory();
            for (ItemStack armor : inventory.getArmorContents()) {
                if (armor == null || !Stack.is(armor)) continue;
                return true;
            }
            return Stack.is(inventory.getItemInHand()) || MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) && Stack.is(inventory.getItemInOffHand());
        }
        return false;
    }

    private static boolean is(ItemStack item) {
        for (ItemStack cache : hm) {
            if (cache.getType() != item.getType() || item.getItemMeta() == null || !ChatColor.stripColor((String)item.getItemMeta().getDisplayName()).equals(cache.getItemMeta().getDisplayName())) continue;
            return true;
        }
        return false;
    }
}

