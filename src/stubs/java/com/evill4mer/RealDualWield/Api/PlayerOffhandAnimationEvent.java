package com.evill4mer.RealDualWield.Api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class PlayerOffhandAnimationEvent extends Event {
    public abstract Player getPlayer();
}
