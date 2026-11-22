package dev.lone.itemsadder.api.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class CustomBlockBreakEvent extends Event {
    public abstract Player getPlayer();
}
