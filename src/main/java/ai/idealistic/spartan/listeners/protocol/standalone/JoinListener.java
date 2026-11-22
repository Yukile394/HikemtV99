/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.listeners.protocol.standalone;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.connection.IntegerDelays;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

public class JoinListener
extends PacketAdapter {
    public JoinListener() {
        super(Register.plugin, ListenerPriority.HIGHEST, new PacketType[]{PacketType.Play.Server.LOGIN});
    }

    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
            return;
        }
        IntegerDelays.newPacket(protocol.getEntityId());
        CheckThread.run(() -> {
            protocol.transactionBoot = true;
            PluginBase.runDelayedTask(protocol, () -> {
                if (protocol.getVehicle() != null) {
                    protocol.entityHandle = true;
                }
            }, 1L);
        });
    }
}

