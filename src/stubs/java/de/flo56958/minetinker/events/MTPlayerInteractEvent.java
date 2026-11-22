package de.flo56958.minetinker.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class MTPlayerInteractEvent extends Event {
    public abstract Player getPlayer();
}
