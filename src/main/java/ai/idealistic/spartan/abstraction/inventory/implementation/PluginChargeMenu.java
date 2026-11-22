/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.tracking.DetectionCharge;
import ai.idealistic.spartan.utils.java.TimeUtils;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PluginChargeMenu
extends InventoryMenu {
    private static final String menu = "Spartan Charge";
    private static final String item = "\u00a77Charge 5 seconds";

    public PluginChargeMenu() {
        super(menu, 27, Permissions.staffPermissions);
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        if (!PluginAddons.freeEdition) {
            return false;
        }
        ArrayList<String> lore = new ArrayList<String>(5);
        lore.add("");
        lore.add("\u00a7ahttps://spartan.top \u00a78- \u00a72Spartan AntiCheat");
        lore.add("\u00a73The longest living Minecraft paid anti cheat!");
        lore.add("");
        lore.add("\u00a77Remaining Time\u00a78: \u00a7c" + TimeUtils.convertMilliseconds(DetectionCharge.remaining()));
        this.add(item, lore, new ItemStack(Material.REDSTONE_BLOCK), 13);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = this.itemStack.getItemMeta().getDisplayName();
        if (PluginAddons.freeEdition && item.equalsIgnoreCase(item)) {
            if (DetectionCharge.charge(protocol)) {
                this.open(protocol);
            }
        } else {
            protocol.bukkit().closeInventory();
        }
        return true;
    }
}

