package com.projectkorra.projectkorra.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

public abstract class AbilityDamageEntityEvent extends Event {
    public abstract Entity getEntity();
}
