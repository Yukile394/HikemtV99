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
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

public class AbilitiesListener
extends PacketAdapter {
    public AbilitiesListener() {
        super(Register.plugin, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.ABILITIES});
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        if (player.getAllowFlight()) {
            CheckThread.run(() -> {
                PluginBase.getProtocol((Player)player).flyingTicks = 2;
                return 2;
            });
        }
    }
}

