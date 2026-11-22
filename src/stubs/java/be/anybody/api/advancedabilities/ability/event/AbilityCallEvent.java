package be.anybody.api.advancedabilities.ability.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class AbilityCallEvent extends Event {
    public abstract Player getPlayer();
}
