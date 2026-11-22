package wtf.choco.veinminer.api.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class PlayerVeinMineEvent extends Event {
    public abstract Player getPlayer();
}
