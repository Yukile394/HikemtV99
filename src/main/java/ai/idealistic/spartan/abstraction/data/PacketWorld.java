/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.abstraction.data;

import ai.idealistic.spartan.abstraction.event.PlayerTickEvent;
import ai.idealistic.spartan.abstraction.event.ServerBlockChange;
import ai.idealistic.spartan.listeners.bukkit.standalone.ChunksEvent;
import ai.idealistic.spartan.utils.java.ConcurrentList;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PacketWorld {
    private final List<ServerBlockChange> query;
    private final Player player;
    private int lagTick;
    public boolean transactionLock;

    public PacketWorld(Player player) {
        this.query = new ConcurrentList<ServerBlockChange>();
        this.lagTick = 0;
    }

    public void tick(PlayerTickEvent tickEvent) {
        if (tickEvent.getDelay() < 12L) {
            this.lagTick = 3;
        } else if (this.lagTick > 0) {
            if (!this.transactionLock) {
                --this.lagTick;
            }
        } else {
            this.query.removeIf(change -> --change.tick == 0);
        }
    }

    public List<ServerBlockChange> getLocalWorld() {
        return new ArrayList<ServerBlockChange>(this.query);
    }

    public Material getBlockRaw(Location location) {
        for (ServerBlockChange change : this.query) {
            Location lL = change.position.toLocation(this.player.getWorld());
            if (!(Math.abs(lL.getX() - location.getX()) <= 1.3) || !(Math.abs(lL.getY() - location.getY()) <= 1.3) || !(Math.abs(lL.getZ() - location.getZ()) <= 1.3)) continue;
            return change.getData();
        }
        return null;
    }

    public Material getBlock(Location location) {
        Material raw = this.getBlockRaw(location);
        if (raw != null) {
            return raw;
        }
        Block blockAsync =  ChunksEvent.getBlockAsync(location);
        return b == null ? null : b.getType();
    }

    public void worldChange(ServerBlockChange blockChange) {
        if (BlockUtils.areAir(blockChange.getData())) {
            Block b = ChunksEvent.getBlockAsync(blockChange.position.toLocation(this.player.getWorld()));
            if (b == null || BlockUtils.areAir(b.getType())) {
                return;
            }
            blockChange.setData(b.getType());
        }
        long hash = blockChange.hashCode();
        for (ServerBlockChange c : this.query) {
            if (c.generateHash() != hash) continue;
            this.query.remove(c);
            break;
        }
        this.query.add(blockChange);
        this.transactionLock = true;
        this.lagTick = 3;
    }
}

