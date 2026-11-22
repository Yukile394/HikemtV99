/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 */
package ai.idealistic.spartan.abstraction.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerUseEvent
implements Cancellable {
    public final Player player;
    public final LivingEntity target;
    private boolean cancelled;

    public PlayerUseEvent(Player player, LivingEntity target, boolean cancelled) {
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}

