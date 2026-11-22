/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.vehicle.VehicleExitEvent
 */
package ai.idealistic.spartan.listeners.bukkit.standalone;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class EntityDeathListener
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void Event(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity instanceof Vehicle) {
            Entity[] entityArray;
            if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) {
                entityArray = entity.getPassengers().toArray(new Entity[0]);
            } else {
                Entity[] entityArray2 = new Entity[1];
                entityArray = entityArray2;
                entityArray2[0] = entity.getPassenger();
            }
            if (entityArray.length > 0) {
                for (Entity passenger : entityArray) {
                    if (!(passenger instanceof Player)) continue;
                    PlayerProtocol p = PluginBase.getProtocol((Player)passenger, true);
                    CheckThread.run(() -> p.executeRunners(false, new VehicleExitEvent((Vehicle)entity, (LivingEntity)(Player)passenger, false)));
                }
            }
        }
    }
}

