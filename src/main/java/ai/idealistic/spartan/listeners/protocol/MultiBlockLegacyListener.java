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
 *  com.comphenix.protocol.wrappers.MultiBlockChangeInfo
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MultiBlockLegacyListener
extends PacketAdapter {
    public MultiBlockLegacyListener() {
        super(Register.plugin, ListenerPriority.HIGHEST, new PacketType[]{PacketType.Play.Server.MULTI_BLOCK_CHANGE});
    }

    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        World world = player.getWorld();
        PacketContainer packet = event.getPacket();
        if (packet.getMultiBlockChangeInfoArrays().getValues().isEmpty()) {
            return;
        }
        List mList = packet.getMultiBlockChangeInfoArrays().getValues();
        CheckThread.run(() -> {
            Iterator iterator = mList.iterator();
            while (iterator.hasNext()) {
                MultiBlockChangeInfo[] m;
                for (MultiBlockChangeInfo info : m = (MultiBlockChangeInfo[])iterator.next()) {
                    Material material = info.getData().getType();
                    if (!material.toString().equals("SLIME_BLOCK") || !(info.getLocation(world).distance(protocol.getLocation()) < 6.0)) continue;
                    protocol.predictedSlimeTicks = 6;
                }
            }
        });
    }
}

