/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 */
package ai.idealistic.spartan.abstraction.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class CBlockPlaceEvent
implements Cancellable {
    private boolean cancelled;
    public final Block placedBlock;
    public final Block placedAgainstBlock;
    public final Player player;

    public CBlockPlaceEvent(Player player, Block placedBlock, Block placedAgainst, boolean cancelled) {
        this.placedAgainstBlock = placedAgainst;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}

