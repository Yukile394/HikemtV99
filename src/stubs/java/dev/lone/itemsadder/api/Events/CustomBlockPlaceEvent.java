package dev.lone.itemsadder.api.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class CustomBlockPlaceEvent extends Event {
    public abstract Player getPlayer();
}
