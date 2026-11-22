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
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MultiBlockListener
extends PacketAdapter {
    public MultiBlockListener() {
        super(Register.plugin, ListenerPriority.HIGHEST, new PacketType[]{PacketType.Play.Server.MULTI_BLOCK_CHANGE});
    }

    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        World world = player.getWorld();
        PacketContainer packet = event.getPacket();
        BlockPosition b = (BlockPosition)packet.getSectionPositions().read(0);
        int chunkX = b.getX();
        int chunkZ = b.getZ();
        int chunkY = b.getY();
        short[] records = (short[])packet.getShortArrays().read(0);
        Object[] blockData = (Object[])packet.getSpecificModifier(Object[].class).read(0);
        CheckThread.run(() -> {
            for (int i = 0; i < records.length; ++i) {
                short record = records[i];
                int relX = record >> 8;
                int relY = record & 0xF;
                int relZ = record >> 4;
                int worldX = chunkX << 4 | relX;
                int worldY = chunkY << 4 | relY;
                int worldZ = chunkZ << 4 | relZ;
                Location loc = new Location(world, (double)(chunkX * 16 + worldX) / 2.0, (double)worldY, (double)(chunkZ * 16 + worldZ) / 2.0);
                Object nmsData = blockData[i];
                if (!nmsData.toString().toLowerCase().contains("slime_block") || !(loc.distance(protocol.getLocation()) < 14.0)) continue;
                CheckThread.run(() -> {
                    protocol.predictedSlimeTicks = 6;
                    return 6;
                });
            }
        });
    }
}

