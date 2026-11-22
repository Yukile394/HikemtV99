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
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ExplosionListener
extends PacketAdapter {
    public ExplosionListener() {
        super(Register.plugin, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Server.EXPLOSION});
    }

    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        List d = packet.getDoubles().getValues();
        if (d.size() >= 3) {
            Player player = event.getPlayer();
            PlayerProtocol protocol = PluginBase.getProtocol(player);
            if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
                return;
            }
            Location l = new Location(player.getWorld(), ((Double)d.get(0)).(double)Value(), ((Double)d.get(1)).(double)Value(), ((Double)d.get(2)).(double)Value());
            if (ServerLocation.distanceSquared(l, protocol.getLocation()) < 10.0) {
                CheckThread.run(() -> {
                    protocol.getComponentY().explosionTick = true;
                    protocol.getComponentXZ().explosionTick = true;
                });
            }
        }
    }
}

