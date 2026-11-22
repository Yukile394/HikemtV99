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
 */
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.DeathEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class DeathListener
extends PacketAdapter {
    public static final PacketType[] packetTypes = new PacketType[]{PacketType.Play.Server.UPDATE_HEALTH};

    public DeathListener() {
        super(Register.plugin, ListenerPriority.HIGHEST, packetTypes);
    }

    public void onPacketSending(PacketEvent event) {
        PlayerProtocol protocol = PluginBase.getProtocol(event.getPlayer());
        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
            return;
        }
        PacketContainer packet = event.getPacket();
        if (packet.getType().equals(PacketType.Play.Server.UPDATE_HEALTH) && ((Float)packet.getFloat().read(0)).floatValue() <= 0.0f) {
            CheckThread.run(() -> {
                DeathEvent.event(event.getPlayer(), true, event);
                protocol.useItemPacket = false;
            });
        }
    }
}

