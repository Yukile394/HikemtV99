/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.vagdedes.spartan.api;

import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFoundOreEvent
extends Event
implements Cancellable {
    private final Player p;
    private final String me;
    private final Location l;
    private final Material ma;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public PlayerFoundOreEvent(Player player, String message, Location location, Material material) {
        this.p = player;
        this.me = message;
        this.l = location;
        this.ma = material;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return this.p;
    }

    public String getMessage() {
        return this.me;
    }

    public Location getLocation() {
        return this.l;
    }

    public Material getMaterial() {
        return this.ma;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Generated
    public void setCancelled(boolean cancelled) {
    }

    @Generated
    public boolean isCancelled() {
        return this.cancelled;
    }
}

