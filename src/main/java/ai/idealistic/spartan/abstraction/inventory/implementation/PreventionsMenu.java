/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PreventionsMenu
extends InventoryMenu {
    public static final String title = "Check Preventions";

    public PreventionsMenu() {
        super(title, 54, Permission.MANAGE);
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        if (!PluginAddons.isSyn()) {
            protocol.bukkit().closeInventory();
            String missing = "\u00a7aClick to visit https://spartan.top/syn.".replace("{feature}", title);
            ClickableMessage.sendURL(CommandSenderprotocol.bukkit(), missing, missing + " (https://spartan.top/syn)", "https://spartan.top/syn");
            return false;
        }
        ArrayList<String> list = new ArrayList<String>();
        int startingLine = 1;
        for (CheckEnums.HackCategoryType categoryType : CheckEnums.HackCategoryType.values()) {
            int slot = startingLine * 11;
            for (CheckEnums.HackType hackType : CheckEnums.HackType.values()) {
                if (hackType.category != categoryType) continue;
                list.clear();
                list.add("\u00a76" + hackType.category.toString());
                list.add("");
                for (Check.DataType dataType : Check.DataType.values()) {
                    if (!hackType.getCheck().checkCheckCancellation(dataType, null)) {
                        list.add("\u00a7aEnabled for the " + dataType.toString() + " edition. \u00a77(Left click to toggle)");
                        continue;
                    }
                    list.add("\u00a7cDisabled for the " + dataType.toString() + " edition. \u00a77(Right click to toggle)");
                }
                this.add("\u00a73" + hackType.getCheck().getName(), list, new ItemStack(hackType.material), slot);
                ++slot;
            }
            ++startingLine;
        }
        this.add("\u00a74Back", null, new ItemStack(Material.ARROW), 49);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = this.itemStack.getItemMeta().getDisplayName();
        if (item.equals("\u00a74Back")) {
            PluginBase.synMenu.open(protocol, protocol.bukkit().getName());
        } else {
            String string = item = item.startsWith("\u00a7") ? item.substring(2) : item;
            if (this.clickType == ClickType.LEFT) {
                Check check = Config.getCheckByName(item);
                if (check != null) {
                    check.handleDataType(Check.DataType.JAVA, !check.checkCheckCancellation(Check.DataType.JAVA, null));
                }
                this.open(protocol);
            } else if (this.clickType == ClickType.RIGHT) {
                Check check = Config.getCheckByName(item);
                if (check != null) {
                    check.handleDataType(Check.DataType.BEDROCK, !check.checkCheckCancellation(Check.DataType.BEDROCK, null));
                }
                this.open(protocol);
            }
        }
        return true;
    }
}

