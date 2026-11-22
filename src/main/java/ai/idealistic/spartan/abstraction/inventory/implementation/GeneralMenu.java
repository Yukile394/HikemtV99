/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.inventory.implementation;

import ai.idealistic.spartan.abstraction.inventory.InventoryMenu;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.connection.PluginAddons;
import ai.idealistic.spartan.functionality.moderation.clickable.ClickableMessage;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.minecraft.inventory.Arrow;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class GeneralMenu
extends InventoryMenu {
    public static final String title = "General Functionality";

    public GeneralMenu() {
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
        list.add("");
        ItemStack itemStack = new ItemStack(Material.BOOK);
        if (Config.settings.getBoolean("Logs.log_file")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7aLogs >> File", list, itemStack, 11);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.PAPER);
        if (Config.settings.getBoolean("Logs.log_console")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7eLogs >> Console", list, itemStack, 12);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.LEVER);
        if (Config.settings.getBoolean("Notifications.individual_only_notifications")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7eNotifications >> Individual Only", list, itemStack, 13);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.COMPASS);
        if (Config.settings.getBoolean("Notifications.enable_notifications_on_login")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7eNotifications >> Enable On Login", list, itemStack, 14);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.ENDER_PEARL);
        if (Config.settings.getBoolean("Notifications.awareness_notifications")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7cNotifications >> Awareness", list, itemStack, 15);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.BEDROCK);
        if (Config.settings.getBoolean("Important.op_bypass")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7cImportant >> Op Bypass", list, itemStack, 20);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Spade.get("enchanting_table"));
        if (Config.settings.getBoolean("Important.bedrock_on_protocollib")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7cImportant >> Bedrock On ProtocolLib", list, itemStack, 21);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.CRAFTING_TABLE);
        if (Config.settings.getBoolean("Important.bedrock_client_permission")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7eImportant >> Bedrock Client Permission", list, itemStack, 22);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Spade.get("piston"));
        if (Config.settings.getBoolean("Important.enable_developer_api")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7cImportant >> Enable Developer API", list, itemStack, 23);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.NAME_TAG);
        if (Config.settings.getBoolean("Important.enable_watermark")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7aImportant >> Enable Watermark", list, itemStack, 24);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.IRON_HELMET);
        if (Config.settings.getBoolean("Important.enable_npc")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7aImportant >> Enable NPC", list, itemStack, 30);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.STONE);
        if (Config.settings.getBoolean("Detections.ground_teleport_on_detection")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7cDetections >> Ground Teleport", list, itemStack, 31);
        list.clear();
        list.add("");
        itemStack = new ItemStack(Material.REDSTONE);
        if (Config.settings.getBoolean("Detections.damage_on_detection")) {
            itemStack.addUnsafeEnchantment(Arrow.DURABILITY, 1);
            list.add("\u00a72Enabled");
        } else {
            list.add("\u00a74Disabled");
        }
        this.add("\u00a7cDetections >> Damage", list, itemStack, 32);
        this.add("\u00a74Back", null, new ItemStack(Material.ARROW), 49);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = this.itemStack.getItemMeta().getDisplayName();
        String string = item = item.startsWith("\u00a7") ? item.substring(2) : item;
        if (item.equals("Back")) {
            PluginBase.synMenu.open(protocol, protocol.bukkit().getName());
        } else if (item.equals("Logs >> File")) {
            Config.settings.setOption("Logs.log_file", !Config.settings.getBoolean("Logs.log_file"));
            this.open(protocol);
        } else if (item.equals("Logs >> Console")) {
            Config.settings.setOption("Logs.log_console", !Config.settings.getBoolean("Logs.log_console"));
            this.open(protocol);
        } else if (item.equals("Notifications >> Individual Only")) {
            Config.settings.setOption("Notifications.individual_only_notifications", !Config.settings.getBoolean("Notifications.individual_only_notifications"));
            this.open(protocol);
        } else if (item.equals("Notifications >> Enable On Login")) {
            Config.settings.setOption("Notifications.enable_notifications_on_login", !Config.settings.getBoolean("Notifications.enable_notifications_on_login"));
            this.open(protocol);
        } else if (item.equals("Notifications >> Awareness")) {
            Config.settings.setOption("Notifications.awareness_notifications", !Config.settings.getBoolean("Notifications.awareness_notifications"));
            this.open(protocol);
        } else if (item.equals("Important >> Op Bypass")) {
            Config.settings.setOption("Important.op_bypass", !Config.settings.getBoolean("Important.op_bypass"));
            this.open(protocol);
        } else if (item.equals("Important >> Bedrock On ProtocolLib")) {
            Config.settings.setOption("Important.bedrock_on_protocollib", !Config.settings.getBoolean("Important.bedrock_on_protocollib"));
            this.open(protocol);
        } else if (item.equals("Important >> Bedrock Client Permission")) {
            Config.settings.setOption("Important.bedrock_client_permission", !Config.settings.getBoolean("Important.bedrock_client_permission"));
            this.open(protocol);
        } else if (item.equals("Important >> Enable Developer API")) {
            Config.settings.setOption("Important.enable_developer_api", !Config.settings.getBoolean("Important.enable_developer_api"));
            this.open(protocol);
        } else if (item.equals("Important >> Enable Watermark")) {
            Config.settings.setOption("Important.enable_watermark", !Config.settings.getBoolean("Important.enable_watermark"));
            this.open(protocol);
        } else if (item.equals("Important >> Enable NPC")) {
            Config.settings.setOption("Important.enable_npc", !Config.settings.getBoolean("Important.enable_npc"));
            this.open(protocol);
        } else if (item.equals("Detections >> Ground Teleport")) {
            Config.settings.setOption("Detections.ground_teleport_on_detection", !Config.settings.getBoolean("Detections.ground_teleport_on_detection"));
            this.open(protocol);
        } else if (item.equals("Detections >> Damage")) {
            Config.settings.setOption("Detections.damage_on_detection", !Config.settings.getBoolean("Detections.damage_on_detection"));
            this.open(protocol);
        }
        return true;
    }
}

