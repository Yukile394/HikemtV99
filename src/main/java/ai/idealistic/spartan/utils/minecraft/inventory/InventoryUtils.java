/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.SkullType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package ai.idealistic.spartan.utils.minecraft.inventory;

import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryUtils {
    private static final boolean bukkitProfile = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) && ReflectionUtils.classExists("org.bukkit.profile.PlayerProfile");

    public static void add(Inventory inv, String name, List<String> lore, ItemStack item, int slot) {
        ItemMeta itemMeta =  item.getItemMeta();
        am.setDisplayName(name);
        if (lore != null) {
            am.setLore(lore);
        }
        item.setItemMeta(am);
        if (slot != -1) {
            inv.setItem(slot, item);
        } else {
            inv.addItem(new ItemStack[]{item});
        }
    }

    public static ItemStack get(String name, ArrayList<String> lore, ItemStack item) {
        ItemMeta itemMeta =  item.getItemMeta();
        am.setDisplayName(name);
        if (lore != null) {
            am.setLore(lore);
        }
        item.setItemMeta(am);
        return item;
    }

    public static ItemStack getHead() {
        return new ItemStack(MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) ? Material.PLAYER_HEAD : Material.getMaterial("SKULL_ITEM"), 1, (short)SkullType.PLAYER.ordinal());
    }

    public static ItemStack getSkull(OfflinePlayer offlinePlayer, String backupName, boolean create) {
        ItemStack skull = InventoryUtils.getHead();
        SkullMeta meta = (SkullMeta)skull.getItemMeta();
        String name = null;
        if (offlinePlayer != null) {
            name = offlinePlayer.getName();
        }
        if (name != null) {
            meta.setOwner(name);
        } else if (backupName != null) {
            meta.setOwner(backupName);
        }
        skull.setItemMeta((ItemMeta)meta);
        return skull;
    }
}

