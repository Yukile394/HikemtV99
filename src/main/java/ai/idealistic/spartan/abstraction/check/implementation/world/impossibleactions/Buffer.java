/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class Buffer
extends CheckDetection {
    private Block eF;
    private long eG = System.currentTimeMillis();
    private long eH = 0L;
    private final Buffer.IndividualSimpleBuffer eI = new Buffer.IndividualSimpleBuffer();

    Buffer(CheckRunner executor) {
        super(executor, null, null, "scaffold_analysis", true);
    }

    void checkCheckCancellation(Block currentBlock) {
        this.call(() -> {
            PlayerProtocol p = this.protocol;
            if (this.eF == null) {
                this.eF = currentBlock;
                return;
            }
            Location location =  p.getLocation();
            Location lB = new Location(l.getWorld(), (double)((long)l.getX()), (double)((long)l.getY()), (double)((long)l.getZ()));
            long placeHash = (long)(lB.getX() * 31.lB.getZ());
            Location bL = currentBlock.getLocation();
            Block oldB = this.eF;
            Material downBType = new ServerLocation(bL.clone().add(0.0, -1.0, 0.0)).getBlock(this.protocol).getTypeOrNull();
            long delay = System.currentTimeMillis() - this.eG;
            boolean checkHash = false;
            boolean flag = true;
            List<Long> hashList = Arrays.asList((long)(bL.getX() * 31.bL.getZ()), (long)((bL.getX() + 0.3) * 31.bL.getZ()), (long)((bL.getX() - 0.3) * 31.bL.getZ()), (long)(bL.getX() * 31.0 + (bL.getZ() + 0.3)), (long)(bL.getX() * 31.0 + (bL.getZ() - 0.3)), (long)((bL.getX() + 0.3) * 31.0 + (bL.getZ() + 0.3)), (long)((bL.getX() + 0.3) * 31.0 + (bL.getZ() - 0.3)), (long)((bL.getX() - 0.3) * 31.0 + (bL.getZ() + 0.3)), (long)((bL.getX() - 0.3) * 31.0 + (bL.getZ() - 0.3)));
            for (Long v : hashList) {
                if (p.isOnGround()) {
                    if (v == placeHash || ServerLocation.distance(bL, l) < 1.2) {
                        checkHash = true;
                        break;
                    }
                    if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13)) continue;
                    Vector v2 = lB.toVector().clone();
                    Vector v1 = bL.toVector().clone();
                    if (v1.getX() != v2.getX() - 1.0 || v1.getZ() != v2.getZ()) continue;
                    checkHash = true;
                    break;
                }
                if (!(ServerLocation.distance(bL, l) < 2.35)) continue;
                checkHash = true;
                break;
            }
            if (delay < 5L || placeHash == this.eH || (double)((long)p.getLocation().getY()) <= bL.getY() || p.bukkit().isFlying() || !checkHash) {
                p.rightClickCounter = 0;
                this.eG = System.currentTimeMillis();
                this.eF = currentBlock;
                return;
            }
            if (bL.getY() != oldB.getLocation().getY() || !BlockUtils.areAir(downBType)) {
                p.rightClickCounter = 0;
                this.eF = currentBlock;
                if (this.eI.count > 5) {
                    this.eI.count -= 5;
                }
                return;
            }
            if (delay < 280L) {
                this.eI.count += 4;
            } else if (delay < (long)(p.isSneaking() ? 345 : 385) && p.rightClickCounter < 3) {
                this.eI.count += 2;
            } else {
                flag = false;
            }
            if (this.eI.count > 12 && flag) {
                this.cancel("scaffold analysis (" + (long)Math.abs(400.0 / (double)delay * 100.-100.0) + "% faster than possible) [VL: " + this.eI.count + "]");
                this.eI.count = 10;
            } else if (this.eI.count > 0) {
                this.eI.count -= 2;
            }
            this.eF = currentBlock;
            this.eH = placeHash;
            this.eG = System.currentTimeMillis();
            p.rightClickCounter = 0;
        });
    }
}

