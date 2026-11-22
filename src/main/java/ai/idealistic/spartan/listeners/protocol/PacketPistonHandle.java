/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.wrappers.BlockPosition
 *  org.bukkit.Location
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
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;

public class PacketPistonHandle
extends PacketAdapter {
    public PacketPistonHandle() {
        super(Register.plugin, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Server.BLOCK_ACTION});
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        if (packet.getStructures().getValues().toString().contains("piston")) {
            PlayerProtocol protocol = PluginBase.getProtocol(event.getPlayer());
            if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
                return;
            }
            Location blockLocation = ((BlockPosition)packet.getBlockPositionModifier().read(0)).toLocation(protocol.getWorld());
            CheckThread.run(() -> {
                protocol.getComponentY().pistonHandle = true;
                if (this.isPlayerInBox(protocol.getLocation(), blockLocation, 5)) {
                    protocol.getComponentY().pistonTick = true;
                    protocol.getComponentXZ().pistonTick = true;
                    protocol.pistonTick = true;
                }
            });
        }
    }

    private boolean isPlayerInBox(Location playerLocation, Location centerLocation, int boxSize) {
        return Math.abs(playerLocation.getX() - centerLocation.getX()) <= (double)boxSize && Math.abs(playerLocation.getY() - centerLocation.getY()) <= (double)boxSize && Math.abs(playerLocation.getZ() - centerLocation.getZ()) <= (double)boxSize;
    }
}

