/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.inventory;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.api.Permission;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.minecraft.inventory.InventoryUtils;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class InventoryMenu {
    protected String title;
    protected final Permission[] permissions;
    protected ItemStack itemStack;
    protected ClickType clickType;
    protected int slot;
    protected int size;
    protected Inventory inventory;

    public InventoryMenu(String title, int size, Permission[] permissions) {
        this.itemStack = null;
        this.clickType = null;
        this.inventory = null;
        this.slot = 0;
    }

    public InventoryMenu(String title, int size, Permission permissions) {
        this(title, size, new Permission[]{permissions});
    }

    protected Inventory setInventory(PlayerProtocol protocol, String title, int size) {
        this.inventory = protocol.createInventory(size, title);
        return this.inventory;
    }

    protected Inventory setSize(PlayerProtocol protocol, int size) {
        this.inventory = protocol.createInventory(size, this.title);
        return this.inventory;
    }

    protected Inventory setTitle(PlayerProtocol protocol, String title) {
        this.inventory = protocol.createInventory(this.size, title);
        return this.inventory;
    }

    protected void add(String name, List<String> lore, ItemStack item, int slot) {
        InventoryUtils.add(this.inventory, name, lore, item, slot);
    }

    public boolean open(PlayerProtocol protocol, boolean permissionMessage) {
        return this.open(protocol, permissionMessage, null);
    }

    public boolean open(PlayerProtocol protocol, Object object) {
        return this.open(protocol, true, object);
    }

    public boolean open(PlayerProtocol protocol) {
        return this.open(protocol, true, null);
    }

    public boolean open(PlayerProtocol protocol, boolean permissionMessage, Object object) {
        boolean access;
        if (this.permissions.length == 0) {
            access = true;
        } else {
            boolean check = false;
            for (Permission permission : this.permissions) {
                if (!Permissions.has(protocol.bukkit(), permission)) continue;
                check = true;
                break;
            }
            access = check;
        }
        if (access) {
            this.inventory = protocol.createInventory(this.size, this.title);
            if (this.internalOpen(protocol, permissionMessage, object)) {
                PluginBase.transferTask(protocol, () -> protocol.bukkit().openInventory(this.inventory));
                return true;
            }
            return false;
        }
        if (permissionMessage) {
            protocol.bukkit().sendMessage(Config.messages.getColorfulString("no_permission"));
        }
        PluginBase.transferTask(protocol, () -> ((Player)protocol.bukkit()).closeInventory());
        return false;
    }

    protected abstract boolean internalOpen(PlayerProtocol var1, boolean var2, Object var3);

    public boolean handle(PlayerProtocol protocol, String title, ItemStack itemStack, ClickType clickType, int slot) {
        if (title.equals(this.title)) {
            boolean access;
            if (this.permissions.length == 0) {
                access = true;
            } else {
                boolean check = false;
                for (Permission permission : this.permissions) {
                    if (!Permissions.has(protocol.bukkit(), permission)) continue;
                    check = true;
                    break;
                }
                if (!(access = check)) {
                    protocol.bukkit().closeInventory();
                }
            }
            if (access) {
                return this.internalHandle(protocol);
            }
        }
        return false;
    }

    protected abstract boolean internalHandle(PlayerProtocol var1);
}

