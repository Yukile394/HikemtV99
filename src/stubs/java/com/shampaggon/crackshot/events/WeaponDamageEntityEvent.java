package com.shampaggon.crackshot.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class WeaponDamageEntityEvent extends Event {
    public abstract Player getPlayer();
    public abstract Entity getVictim();
}
