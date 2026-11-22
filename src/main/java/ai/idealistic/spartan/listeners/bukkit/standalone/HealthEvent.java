/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 */
package ai.idealistic.spartan.listeners.bukkit.standalone;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HealthEvent
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST)
    private void Food(FoodLevelChangeEvent e) {
        HumanEntity entity = e.getEntity();
        if (entity instanceof Player) {
            PlayerProtocol p = PluginBase.getProtocol((Player)entity);
            CheckThread.run(() -> p.executeRunners(e.isCancelled(), e));
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    private void Regen(EntityRegainHealthEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            PlayerProtocol p = PluginBase.getProtocol((Player)entity);
            CheckThread.run(() -> p.executeRunners(e.isCancelled(), e));
        }
    }
}

