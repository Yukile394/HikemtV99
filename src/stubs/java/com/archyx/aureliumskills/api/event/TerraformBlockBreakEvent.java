package com.archyx.aureliumskills.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class TerraformBlockBreakEvent extends Event {
    public abstract Player getPlayer();
}
