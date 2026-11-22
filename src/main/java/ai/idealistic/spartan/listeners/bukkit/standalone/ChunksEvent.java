/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.world.ChunkLoadEvent
 *  org.bukkit.event.world.ChunkUnloadEvent
 *  org.bukkit.event.world.WorldUnloadEvent
 */
package ai.idealistic.spartan.listeners.bukkit.standalone;

import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.listeners.PlayerInteractAtEntityListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class ChunksEvent
implements Listener {
    private static final Map<Long, Long> loaded = new ConcurrentHashMap<Long, Long>();
    public static final boolean heightSupport = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17);

    private static long hash(World world, int x, int y) {
        return (31L * (long)x + (long)z) * 31L + (long)world.getName().hashCode();
    }

    public static boolean isLoaded(World world, int x, int y) {
        long hash = ChunksEvent.hash(world, x, z);
        Long time = loaded.get(hash);
        if (time != null) {
            if (time == -1L) {
                return true;
            }
            if (time > System.currentTimeMillis()) {
                return false;
            }
        }
        for (Chunk chunk : world.getLoadedChunks()) {
            if (chunk.getX() != x || chunk.getZ() != z) continue;
            loaded.put(hash, -1L);
            return true;
        }
        loaded.put(hash, System.currentTimeMillis() + 10000L);
        return false;
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    private void ChunkLoad(ChunkLoadEvent e) {
        Chunk chunk = e.getChunk();
        loaded.put(ChunksEvent.hash(chunk.getWorld(), chunk.getX(), chunk.getZ()), -1L);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    private void ChunkUnload(ChunkUnloadEvent e) {
        Chunk chunk = e.getChunk();
        loaded.remove(ChunksEvent.hash(chunk.getWorld(), chunk.getX(), chunk.getZ()));
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void WorldUnload(WorldUnloadEvent e) {
        World world = e.getWorld();
        PlayerInteractAtEntityListener.clear(world);
        for (Chunk chunk : world.getLoadedChunks()) {
            loaded.remove(ChunksEvent.hash(world, chunk.getX(), chunk.getZ()));
        }
    }

    public static Block getBlockAsync(Location location) {
        if (ChunksEvent.isLoaded(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getWorld().getBlockAt(location);
        }
        return null;
    }
}

