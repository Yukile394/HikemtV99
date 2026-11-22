/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.abstraction.event;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CPlayerRiptideEvent {
    public final PlayerProtocol protocol;
    public final ItemStack item;
    public final Vector velocity;

    public CPlayerRiptideEvent(PlayerProtocol protocol, ItemStack item, Vector velocity) {
    }
}

