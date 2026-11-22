/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 */
package ai.idealistic.spartan.functionality.tracking;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.math.Distance;
import java.util.Collection;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Piston {
    private static final double horizontalDistance = 3.0;
    private static final double verticalDistance = 2.0;

    public static void run(Block block, List<Block> blocks) {
        Collection<PlayerProtocol> protocols = PluginBase.getProtocols();
        if (!protocols.isEmpty()) {
            boolean runBlocks = !blocks.isEmpty();
            World world = block.getWorld();
for (PlayerProtocol protocol : protocols) {
                double preZ;
                double diffY;
                Location location;
                double preX;
                if (!protocol.getWorld().equals(world) || Piston.run(protocol, preX = Distance.getSquare((location = protocol.getLocationOrVehicle()).getX(), block.getX()), diffY = location.getY() - (double)block.getY(), preZ = Distance.getSquare(location.getZ(), block.getZ())) || !runBlocks || !(Math.sqrt(preX + diffY * diffY + preZ) <= 16.0)) continue;
                for (Block affected : blocks) {
                    preX = Distance.getSquare(location.getX(), affected.getX());
                    if (!Piston.run(protocol, preX, diffY = location.getY() - (double)block.getY(), preZ = Distance.getSquare(location.getZ(), affected.getZ()))) continue;
                    continue block0;
                }
            }
        }
    }

    private static boolean run(PlayerProtocol protocol, double preX, double diffY, double preZ) {
        if (Math.sqrt(preX + preZ) <= 3.0 && Math.abs(diffY) <= 2.0) {
            protocol.lastVelocity = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}

