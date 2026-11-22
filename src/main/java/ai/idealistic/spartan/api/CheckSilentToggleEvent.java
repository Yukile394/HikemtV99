/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package ai.idealistic.spartan.api;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.api.ToggleAction;
import lombok.Generated;
import me.vagdedes.spartan.api.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckSilentToggleEvent
extends Event
implements Cancellable {
    private final CheckEnums.HackType ht;
    private final ToggleAction ta;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public CheckSilentToggleEvent(CheckEnums.HackType hackType, ToggleAction toggleAction) {
        this.ht = hackType;
        this.ta = toggleAction;
        this.cancelled = false;
        me.vagdedes.spartan.api.CheckSilentToggleEvent event = new me.vagdedes.spartan.api.CheckSilentToggleEvent(hackType, Enums.ToggleAction.valueOf(toggleAction.toString()));
        event.setCancelled(this.isCancelled());
        Bukkit.getPluginManager().callEvent((Event)event);
    }

    public CheckEnums.HackType getHackType() {
        return this.ht;
    }

    public ToggleAction getToggleAction() {
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

