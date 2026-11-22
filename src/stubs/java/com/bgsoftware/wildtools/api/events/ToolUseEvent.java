package com.bgsoftware.wildtools.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class ToolUseEvent extends Event {
    public abstract Player getPlayer();
}
