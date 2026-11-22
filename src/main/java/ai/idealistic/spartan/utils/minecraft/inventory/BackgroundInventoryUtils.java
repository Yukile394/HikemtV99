/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.profile.PlayerProfile
 */
package ai.idealistic.spartan.utils.minecraft.inventory;

import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.java.ReflectionUtils;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

public class BackgroundInventoryUtils {
    private static final boolean paperProfile = ReflectionUtils.classExists("com.destroystokyo.paper.profile.PlayerProfile");

    static ItemStack getSkull_v1_13(OfflinePlayer offlinePlayer, String backupName, boolean create) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        if (offlinePlayer != null) {
            SkullMeta meta = (SkullMeta)skull.getItemMeta();
            if (create && !offlinePlayer.hasPlayedBefore() && MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_18)) {
                String name = offlinePlayer.getName();
                if (name == null) {
                    name = backupName;
                    Object profile = paperProfile ? (name == null ? Bukkit.createProfile((UUID)offlinePlayer.getUniqueId()) : Bukkit.createProfileExact((UUID)offlinePlayer.getUniqueId(), (String)name)) : (name == null ? Bukkit.createPlayerProfile((UUID)offlinePlayer.getUniqueId()) : Bukkit.createPlayerProfile((UUID)offlinePlayer.getUniqueId(), (String)name));
                    meta.setOwnerProfile((PlayerProfile)profile);
                }
            } else {
                meta.setOwningPlayer(offlinePlayer);
            }
            skull.setItemMeta((ItemMeta)meta);
        }
        return skull;
    }
}

