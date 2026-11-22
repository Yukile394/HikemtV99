package me.DeeCaaD.CrackShotPlus.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class WeaponSecondScopeEvent extends Event {
    public abstract Player getPlayer();
    public abstract boolean isZoomIn();
    public abstract boolean isCancelled();
}
