package me.rampen88.drills.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class DrillBreakEvent extends Event {
    public abstract Player getPlayer();
}
