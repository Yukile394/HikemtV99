/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.vehicle.VehicleEnterEvent
 *  org.bukkit.event.vehicle.VehicleExitEvent
 */
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.MovementEvent;
import ai.idealistic.spartan.listeners.bukkit.VehicleEvent;
import ai.idealistic.spartan.utils.minecraft.protocol.ProtocolTools;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class VehicleHandle
extends PacketAdapter {
    public VehicleHandle() {
        super(Register.plugin, ListenerPriority.HIGHEST, new PacketType[]{PacketType.Play.Client.STEER_VEHICLE, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK});
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
            return;
        }
        CheckThread.run(() -> {
            if (ProtocolTools.hasPosition(event.getPacket().getType()) && protocol.entityHandle) {
                protocol.entityHandle = false;
            }
            for (Entity entity : protocol.getNearbyEntities(4.5)) {
                if (entity.getUniqueId() == protocol.getUUID()) continue;
                if (ProtocolTools.hasPosition(event.getPacket().getType()) && protocol.entityHandle) {
                    protocol.timerBalancer.addBalance(50L);
                    VehicleExitEvent bukkitEvent = new VehicleExitEvent(null, (LivingEntity)protocol.bukkit());
                    bukkitEvent.setCancelled(event.isCancelled());
                    VehicleEvent.exit(bukkitEvent);
                    break;
                }
                if (!event.getPacket().getType().equals(PacketType.Play.Client.STEER_VEHICLE) || protocol.entityHandle) break;
                protocol.timerBalancer.addBalance(50L);
                VehicleEnterEvent bukkitEvent = new VehicleEnterEvent(null, (Entity)protocol.bukkit());
                bukkitEvent.setCancelled(event.isCancelled());
                VehicleEvent.enter(bukkitEvent);
                protocol.entityHandle = true;
                break;
            }
            if (event.getPacket().getType().equals(PacketType.Play.Client.STEER_VEHICLE) && protocol.getVehicle() != null) {
                Entity vehicle = protocol.getVehicle();
                Location location = vehicle.getLocation();
                protocol.setLocation(location);
                MovementEvent.event(new PlayerMoveEvent(player, protocol.getFromLocation(), protocol.getLocation()), true);
            }
        });
    }
}

