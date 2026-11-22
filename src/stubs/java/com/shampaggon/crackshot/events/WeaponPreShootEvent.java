package com.shampaggon.crackshot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class WeaponPreShootEvent extends Event {
    public abstract Player getPlayer();
}
