/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SynMenu
extends InventoryMenu {
    public static final Permission[] permissions = new Permission[]{Permission.MANAGE, Permission.INFO};

    public SynMenu() {
        super("Spartan Syn", 45, permissions);
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("");
        list.add("\u00a77A menu that allows you to");
        list.add("\u00a77toggle the functionality");
        list.add("\u00a77of checks and their detections.");
        this.add("\u00a72Check Functionality", list, new ItemStack(Material.LEVER), 11);
        list.clear();
        list.add("");
        list.add("\u00a77A menu that allows you to");
        list.add("\u00a77toggle the details of a");
        list.add("\u00a77check's detections.");
        this.add("\u00a7eCheck Details", list, new ItemStack(Material.BOOK), 12);
        list.clear();
        list.add("");
        list.add("\u00a77A menu that allows you to");
        list.add("\u00a77toggle the preventions of a");
        list.add("\u00a77check's detections.");
        this.add("\u00a7cCheck Preventions", list, new ItemStack(Spade.get("lead")), 13);
        list.clear();
        list.add("");
        list.add("\u00a77A menu that allows you");
        list.add("\u00a77to manage the punishment");
        list.add("\u00a77commands of checks.");
        this.add("\u00a74Check Commands", list, new ItemStack(Material.REDSTONE_BLOCK), 14);
        list.clear();
        list.add("");
        list.add("\u00a77A menu that allows you to");
        list.add("\u00a77toggle the punishments of a");
        list.add("\u00a77check and view their commands.");
        this.add("\u00a74Check Punishments", list, new ItemStack(Material.IRON_AXE), 15);
        list.clear();
        list.add("\u00a77A menu that allows you to");
        list.add("\u00a77manage the detections of");
        list.add("\u00a77all checks.");
        this.add("\u00a7bCheck Detections", list, new ItemStack(Material.REDSTONE), 30);
        list.clear();
        list.add("");
        list.add("\u00a77A menu that allows you to");
        list.add("\u00a77manage the general settings");
        list.add("\u00a77of " + Register.plugin + ".");
        this.add("\u00a7aGeneral Functionality", list, new ItemStack(Material.ENDER_CHEST), 31);
        list.clear();
        list.add("");
        list.add("\u00a77A menu that allows you to");
        list.add("\u00a77view the statistics of " + Register.plugin + ".");
        this.add("\u00a76Plugin Statistics", list, new ItemStack(Material.PAPER), 32);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = this.itemStack.getItemMeta().getDisplayName();
        String string = item = item.startsWith("\u00a7") ? item.substring(2) : item;
        if (item.equals("Check Functionality")) {
            PluginBase.editionMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals("Check Details")) {
            PluginBase.detailsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals("Check Preventions")) {
            PluginBase.preventionsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals("Check Punishments")) {
            PluginBase.punishmentsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals("Check Detections")) {
            PluginBase.detectionsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals("General Functionality")) {
            PluginBase.generalMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals("Plugin Statistics")) {
            PluginBase.statisticsMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals("Check Commands")) {
            PluginBase.commandsMenu.open(protocol, protocol.bukkit().getName());
        }
        return true;
    }
}

