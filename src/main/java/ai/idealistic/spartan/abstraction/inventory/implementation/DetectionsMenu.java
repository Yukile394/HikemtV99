/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class DetectionsMenu
extends InventoryMenu {
    public static final String title = "Check Detections";
    private static final Map<String, Material> items = new HashMap<String, Material>53;

    public DetectionsMenu() {
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
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration((File)Config.advanced.getFile());
        Set keys = configuration.getKeys(true);
        if (!keys.isEmpty()) {
            int added = 0;
            for (String key : keys) {
                Object value;
                if (!key.contains(".") || !((value = configuration.get(key)) instanceof Boolean)) continue;
                Material material = items.get(key);
                while (material == null) {
                    int maxMaterials = Material.values().length;
                    int randomIndex = (int)(Math.random() * (double)maxMaterials);
                    Material find = Material.values()[randomIndex];
                    if (!find.isItem() || items.containsValue(find)) continue;
                    material = find;
                    items.put(key, find);
                }
                key = key.replace(".", " >> ").replace("-", " ").replace("_", "-");
                ArrayList<String> list = new ArrayList<String>();
                list.add("");
                if (((Boolean)value).booleanValue()) {
                    list.add("\u00a7aEnabled");
                    this.add("\u00a72" + key, list, new ItemStack(material), -1);
                } else {
                    list.add("\u00a7cDisabled");
                    this.add("\u00a74" + key, list, new ItemStack(material), -1);
                }
                if (++added != 53) continue;
            }
        }
        this.add("\u00a74Back", null, new ItemStack(Material.ARROW), 53);
        return true;
    }

    @Override
    public boolean internalHandle(PlayerProtocol protocol) {
        String item = this.itemStack.getItemMeta().getDisplayName();
        if (item.equals("\u00a74Back")) {
            PluginBase.synMenu.open(protocol, protocol.bukkit().getName());
        } else {
            item = item.startsWith("\u00a7") ? item.substring(2) : item;
            item = item.replace(" >> ", ".").replace("-", "_").replace(" ", "-");
            Config.advanced.setOption(item, !Config.advanced.getBoolean(item));
            this.open(protocol);
        }
        return true;
    }
}

