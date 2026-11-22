/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.vagdedes.spartan.api;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerViolationEvent
extends Event
implements Cancellable {
    private final Player p;
    private final CheckEnums.HackType h;
    private final String m;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public PlayerViolationEvent(Player player, CheckEnums.HackType HackType2, String message) {
        this.p = player;
        this.h = HackType2;
        this.m = message;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return this.p;
    }

    public CheckEnums.HackType getHackType() {
        return this.h;
    }

    public String getMessage() {
        return this.m;
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

