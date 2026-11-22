/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityShootBowEvent
 */
package ai.idealistic.spartan.listeners.bukkit.standalone;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class BowEvent
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST)
    private void Event(EntityShootBowEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity instanceof Player) {
            PlayerProtocol p = PluginBase.getProtocol((Player)entity, true);
            CheckThread.run(() -> p.executeRunners(e.isCancelled(), e));
        }
    }
}

