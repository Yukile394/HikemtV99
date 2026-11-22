package com.projectkorra.projectkorra.event;

import com.projectkorra.projectkorra.ability.Ability;
import org.bukkit.event.Event;

public abstract class AbilityStartEvent extends Event {
    public abstract Ability getAbility();
}
