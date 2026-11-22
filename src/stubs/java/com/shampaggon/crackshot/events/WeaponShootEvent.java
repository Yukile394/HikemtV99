package com.shampaggon.crackshot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class WeaponShootEvent extends Event {
    public abstract Player getPlayer();
}
