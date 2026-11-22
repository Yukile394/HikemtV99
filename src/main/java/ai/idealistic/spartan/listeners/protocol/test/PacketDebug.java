/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.listeners.protocol.test;

import ai.idealistic.spartan.Register;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketDebug
extends PacketAdapter {
    public PacketDebug() {
        super(Register.plugin, ListenerPriority.MONITOR, (Iterable)PacketType.Play.Client.getInstance());
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PacketContainer packet = event.getPacket();
        if (event.getPlayer().getName().equals("pawsashatoy")) {
            this.display(player, "i: " + packet.getType().name() + " " + packet.getStructures().getValues());
        }
    }

    private void display(Player player, String display) {
        player.sendMessage(display);
        Bukkit.getLogger().info(display);
    }
}

