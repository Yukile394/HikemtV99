/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class SubCommandsMenu
extends InventoryMenu {
    private static final String title = "Check Commands: ";

    public SubCommandsMenu() {
        super(title, 36, Permission.MANAGE);
    }

    @Override
    public boolean internalOpen(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        if (!PluginAddons.isSyn()) {
            protocol.bukkit().closeInventory();
            String missing = "\u00a7aClick to visit https://spartan.top/syn.".replace("{feature}", title.substring(0, title.length() - 2));
            ClickableMessage.sendURL(CommandSenderprotocol.bukkit(), missing, missing + " (https://spartan.top/syn)", "https://spartan.top/syn");
            return false;
        }
        Check check = (Check)object;
        this.setTitle(protocol, title + check.getName());
        ArrayList<String> list = new ArrayList<String>();
        int startingLine = 1;
        List<String> commands = check.checkCheckCancellation();
        for (i < 10; ++i) {
            int slot = startingLine * 11 + i - (i >= 5 ? 5 : 0);
            list.clear();
            list.add("");
            if (i < commands.size()) {
                list.add("\u00a77" + commands.get(i));
                list.add("");
                list.add("\u00a7cClick to delete this punishment command.");
                this.add("\u00a7a" + check.getName() + " #" + (i + 1) + " command", list, new ItemStack(check.hackType.material, i + 1), slot);
            } else {
                list.add("\u00a7cNo punishment command set.");
                this.add("\u00a7a" + check.getName() + " #" + (i + 1) + " command", list, new ItemStack(check.hackType.material, i + 1), slot);
            }
            continue;
            ++startingLine;
        }
        this.add("\u00a74Back", null, new ItemStack(Material.ARROW), 31);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = this.itemStack.getItemMeta().getDisplayName();
        if (item.equals("\u00a74Back")) {
            PluginBase.commandsMenu.open(protocol, protocol.bukkit().getName());
        } else {
            item = item.startsWith("\u00a7") ? item.substring(2) : item;
            Check check = Config.getCheckByName(item = item.split(" ", 2)[0]);
            if (check != null) {
                check.handleCheck(this.itemStack.getAmount(), "");
                this.open(protocol, check);
            } else {
                protocol.bukkit().closeInventory();
            }
        }
        return true;
    }
}

