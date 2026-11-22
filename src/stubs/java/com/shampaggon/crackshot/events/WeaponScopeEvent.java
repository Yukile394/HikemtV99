package com.shampaggon.crackshot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class WeaponScopeEvent extends Event {
    public abstract Player getPlayer();
    public abstract boolean isZoomIn();
    public abstract boolean isCancelled();
}
