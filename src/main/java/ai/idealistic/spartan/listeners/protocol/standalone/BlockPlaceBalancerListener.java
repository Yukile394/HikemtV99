/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.listeners.protocol.standalone;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.BackgroundProtocolLib;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

public class BlockPlaceBalancerListener
extends PacketAdapter {
    public BlockPlaceBalancerListener() {
        super(Register.plugin, ListenerPriority.HIGHEST, BlockPlaceBalancerListener.resolvePacketTypes());
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
            return;
        }
        CheckThread.run(() -> protocol.timerBalancer.addBalance(50L));
    }

    private static PacketType[] resolvePacketTypes() {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
            return new PacketType[]{PacketType.Play.Client.USE_ITEM, BackgroundProtocolLib.isPacketSupported("USE_ITEM_ON") ? PacketType.Play.Client.USE_ITEM_ON : PacketType.Play.Client.BLOCK_PLACE};
        }
        return new PacketType[]{BackgroundProtocolLib.isPacketSupported("USE_ITEM_ON") ? PacketType.Play.Client.USE_ITEM_ON : PacketType.Play.Client.BLOCK_PLACE};
    }
}

