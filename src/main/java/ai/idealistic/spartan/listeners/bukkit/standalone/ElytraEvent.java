/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityToggleGlideEvent
 */
package ai.idealistic.spartan.listeners.bukkit.standalone;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class ElytraEvent
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void Event(EntityToggleGlideEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            PlayerProtocol protocol = PluginBase.getProtocol((Player)entity);
            CheckThread.run(() -> protocol.executeRunners(e.isCancelled(), e));
        }
    }
}

