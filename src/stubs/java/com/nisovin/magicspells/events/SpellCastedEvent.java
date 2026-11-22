package com.nisovin.magicspells.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

public abstract class SpellCastedEvent extends Event {
    public abstract LivingEntity getCaster();
}
