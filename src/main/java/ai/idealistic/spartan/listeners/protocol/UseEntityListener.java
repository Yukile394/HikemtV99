/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.wrappers.EnumWrappers$EntityUseAction
 *  com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.event.vehicle.VehicleEnterEvent
 */
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class UseEntityListener
extends PacketAdapter {
    public UseEntityListener() {
        super(Register.plugin, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.USE_ENTITY});
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
            return;
        }
        PacketContainer packet = event.getPacket();
        int entityId = (Integer)packet.getIntegers().read(0);
        if (!packet.getEntityUseActions().getValues().isEmpty() ? !((EnumWrappers.EntityUseAction)packet.getEntityUseActions().read(0)).equals(EnumWrappers.EntityUseAction.ATTACK) : !((WrappedEnumEntityUseAction)packet.getEnumEntityUseActions().read(0)).getAction().equals(EnumWrappers.EntityUseAction.ATTACK)) {
            CheckThread.run(() -> {
                Entity t = null;
                for (Entity entity : protocol.getNearbyEntities(5.0)) {
                    if (entity.getEntityId() != entityId) continue;
                    t = entity;
                    break;
                }
                if (t instanceof Vehicle) {
                    protocol.executeRunners(false, new VehicleEnterEvent(null, (Entity)player));
                }
            });
        }
    }
}

