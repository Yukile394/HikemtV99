/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.utils.java.OverflowMap;
import ai.idealistic.spartan.utils.math.Distance;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import ai.idealistic.spartan.utils.minecraft.entity.PotionEffectUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.NumberConversions;

public class Tower
extends CheckDetection {
    private final Map<Integer, Buffer.Storage> eJ = new OverflowMap<Integer, Buffer.Storage>(new ConcurrentHashMap(2), 124);

    Tower(CheckRunner executor) {
        super(executor, null, Check.DetectionType.BUKKIT, "tower", true);
    }

    void handleCheck(Block block, Location playerLocation, int playerY, int blockY) {
        this.call(() -> {
            Double deltaXZ = Distance.getHorizontalDistance(this.protocol.getLocation(), this.protocol.getFromLocation());
            if (deltaXZ == null) {
                return;
            }
            if (Distance.getHorizontalDistance(ServerLocation.getBlockLocation(playerLocation), block.getLocation()) <= 1.0) {
                int jumpLevel = PlayerUtils.getPotionLevel(this.protocol, PotionEffectUtils.JUMP) + 1;
                int motionTicks = PlayerUtils.getJumpTicks(jumpLevel);
                double motionSum = PlayerUtils.getJumpMotionSum(jumpLevel);
                double blockHeight = 0.4765625;
                int deltaYMinus1 = playerY - blockY;
                int possiblePlacements = NumberConversions.floor((double)(motionSum / blockHeight)) + deltaYMinus1;
                if (deltaXZ > 0.1) {
                    possiblePlacements += NumberConversions.ceil((double)(deltaXZ / 0.25));
                }
                possiblePlacements = Math.max(possiblePlacements, 2);
                Buffer.Storage buffer = this.eJ.computeIfAbsent(block.getX() * 31 + block.getZ(), k -> new Buffer.Storage());
                if (buffer.count(1.0, motionTicks) > (double)possiblePlacements) {
                    buffer.reset();
                    this.cancel("tower, placements: " + possiblePlacements + ", horizontal: " + deltaXZ + ", vertical: " + deltaYMinus1, this.protocol.getFromLocation(), 0, true);
                }
            }
        });
    }
}

