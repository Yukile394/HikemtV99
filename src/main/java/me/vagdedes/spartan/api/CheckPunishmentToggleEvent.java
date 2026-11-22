/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package me.vagdedes.spartan.api;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import lombok.Generated;
import me.vagdedes.spartan.api.system.Enums;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckPunishmentToggleEvent
extends Event
implements Cancellable {
    private final CheckEnums.HackType ht;
    private final Enums.ToggleAction ta;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public CheckPunishmentToggleEvent(CheckEnums.HackType hackType, Enums.ToggleAction toggleAction) {
        this.ht = hackType;
        this.ta = toggleAction;
        this.cancelled = false;
    }

    public CheckEnums.HackType getHackType() {
        return this.ht;
    }

    public Enums.ToggleAction getToggleAction() {
        return this.ta;
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

