/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.event.PlayerTransactionEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.MovementEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;

public class PacketLatencyHandler
extends PacketAdapter {
    public PacketLatencyHandler() {
        PacketType[] packetTypeArray = new PacketType[2];
        PacketType packetType = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17) ? PacketType.Play.Client.PONG : (packetTypeArray[0] = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_12) ? PacketType.Play.Client.TRANSACTION : PacketType.Play.Client.KEEP_ALIVE);
        packetTypeArray[1] = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17) ? PacketType.Play.Server.PING : (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_12) ? PacketType.Play.Server.TRANSACTION : PacketType.Play.Server.KEEP_ALIVE);
        super(Register.plugin, ListenerPriority.MONITOR, packetTypeArray);
    }

    public void onPacketReceiving(PacketEvent event) {
        int id;
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        PacketContainer packet = event.getPacket();
        if (!packet.getShorts().getFields().isEmpty()) {
            id = ((Short)packet.getShorts().read(0)).(short)Value();
        } else if (!packet.getIntegers().getFields().isEmpty()) {
            id = (Integer)packet.getIntegers().read(0);
        } else if (!packet.getLongs().getFields().isEmpty()) {
            id = Math.toIntExact(Longpacket.getLongs().read(0));
        } else {
            return;
        }
        if (id <= -1939 && id >= -1945) {
            CheckThread.run(() -> {
                protocol.transactionPing = System.currentTimeMillis() - protocol.transactionTime;
                protocol.transactionLastTime = System.currentTimeMillis();
                protocol.transactionSentKeep = false;
                MovementEvent.transaction(new PlayerTransactionEvent(protocol));
                protocol.packetWorld.transactionLock = false;
            });
            CheckThread.later(() -> PacketLatencyHandler.sendTransaction(protocol, protocol.transactionId), 10L);
        }
    }

    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        CheckThread.run(() -> {
            protocol.transactionSentKeep = true;
            protocol.transactionTime = System.currentTimeMillis();
        });
    }

    public static void startChecking(PlayerProtocol protocol) {
        protocol.transactionId = -1939;
        protocol.transactionBoot = false;
        PacketLatencyHandler.sendTransaction(protocol, protocol.transactionId);
    }

    public static void sendTransaction(PlayerProtocol protocol, short id) {
        PacketContainer packet = new PacketContainer(MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17) ? PacketType.Play.Server.PING : (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_12) ? PacketType.Play.Server.TRANSACTION : PacketType.Play.Server.KEEP_ALIVE));
        if (!packet.getShorts().getFields().isEmpty()) {
            packet.getShorts().write(0, id);
            if (packet.getType().equals(PacketType.Play.Server.TRANSACTION)) {
                packet.getIntegers().write(0, (Object)0);
                packet.getBooleans().write(0, false);
            }
        } else if (!packet.getIntegers().getFields().isEmpty()) {
            packet.getIntegers().write(0, id);
        } else if (!packet.getLongs().getFields().isEmpty()) {
            packet.getLongs().write(0, id);
        } else {
            return;
        }
        ProtocolLibrary.getProtocolManager().sendServerPacket(protocol.bukkit(), packet);
        protocol.transactionId = (short)(protocol.transactionId - 1);
        if (protocol.transactionId < -1945) {
            protocol.transactionId = -1939;
        }
    }
}

