/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.data;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerBlock;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import java.util.Collection;
import java.util.HashSet;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.NumberConversions;

public class EnvironmentData {
    private static final Material MAGMA_BLOCK = Spade.get("magma");
    private boolean ice = false;
    private boolean slime = false;
    private boolean slimeWide = false;
    private boolean slimeHeight = false;
    private boolean liquid = false;
    private boolean semi = false;
    private boolean glide = false;
    private boolean jumpModify = false;
    private boolean climb = false;
    private boolean bubble = false;

    public EnvironmentData(PlayerProtocol protocol) {
        Location location = protocol.getVehicle() == null ? protocol.getLocation() : protocol.getVehicle().getLocation();
        double x = location.getX();
        double y = location.getY() - 0.1;
        double z = location.getZ();
        for (int dx = -2; dx <= 2; ++dx) {
            for (int dy = -2; dy <= 2; ++dy) {
                for (int dz = -2; dz <= 2; ++dz) {
                    Material materialBigFrom;
                    Material materialBig2;
                    boolean h = Math.abs(dx) < 2 && Math.abs(dz) < 2;
                    Material material = new ServerLocation(protocol.getWorld(), x.3, y.5, z.3).getBlock(protocol).getTypeOrNull();
                    Material materialWide = new ServerLocation(protocol.getWorld(), x.5, y.55.3, z.5).getBlock(protocol).getTypeOrNull();
                    Material materialTop = new ServerLocation(protocol.getWorld(), x.5, y.6.0, z.5).getBlock(protocol).getTypeOrNull();
                    Material materialFrom = new ServerLocation(protocol.getWorld(), x.3, y.5, z.3).getBlock(protocol).getTypeOrNull();
                    ServerBlock b = new ServerLocation(new Location(protocol.getWorld(), x.3, y.5, z.3)).getBlock();
                    if (material != null && (BlockUtils.areHoneyBlocks(material) || BlockUtils.areBeds(material) || BlockUtils.areSlimeBlocks(material))) {
                        this.slime = true;
                    }
                    if (material != null && Math.abs(dx) < 2 && Math.abs(dz) < 2 && (BlockUtils.areInteractiveBushes(material) || BlockUtils.areBeds(material) || BlockUtils.isPowderSnow(material))) {
                        this.jumpModify = true;
                    }
                    if (material != null && BlockUtils.canClimb(material, false) || materialTop != null && BlockUtils.canClimb(materialTop, false)) {
                        this.climb = true;
                    }
                    if (Math.abs(dy) < 2 && h && (material != null && BlockUtils.isPowderSnow(material) || materialTop != null && BlockUtils.isPowderSnow(materialTop) || material != null && BlockUtils.areHoneyBlocks(material) || material != null && BlockUtils.areWebs(material) || materialTop != null && BlockUtils.areWebs(materialTop) || material != null && BlockUtils.areInteractiveBushes(material) || materialTop != null && BlockUtils.areInteractiveBushes(materialTop))) {
                        this.glide = true;
                    }
                    if (material != null && !BlockUtils.canClimb(material, false) && (BlockUtils.isSemiSolid(material) || materialFrom != null && BlockUtils.isSemiSolid(materialFrom) || BlockUtils.isSemiSolid(materialWide) || materialFrom != null && BlockUtils.areWalls(materialFrom) || materialFrom != null && BlockUtils.areCobbleWalls(materialFrom) || BlockUtils.areSlimeBlocks(material) || BlockUtils.areHoneyBlocks(material) || BlockUtils.areWebs(material) || materialTop != null && BlockUtils.areWebs(materialTop))) {
                        this.semi = true;
                    }
                    if (material != null && MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) && material.equals(Material.BUBBLE_COLUMN)) {
                        this.bubble = true;
                    }
                    if (BlockUtils.isLiquidOrWaterLogged(b, true) || BlockUtils.isLiquid(new ServerLocation(protocol.getWorld(), x.3, y, z.3).getBlock(protocol).getType()) || material != null && BlockUtils.isLiquid(material) || BlockUtils.isWaterLogged(b)) {
                        this.liquid = true;
                        for (double i = 0.0; i < Math.ceil(protocol.getEyeHeight()); i += 1.0) {
                            boolean broke = false;
                            for (ServerLocation locationModified : new ServerLocation(location).getSurroundingLocations(0.3, i, 0.3)) {
                                if (!this.calculateBubbleWater(protocol, locationModified)) continue;
                                broke = true;
                                break;
                            }
                            if (broke) break;
                        }
                    }
                    if (material != null && BlockUtils.areIceBlocks(material)) {
                        this.ice = true;
                    }
                    double xF = protocol.getFromLocation().getX();
                    double yF = protocol.getFromLocation().getY();
                    double zF = protocol.getFromLocation().getZ();
                    Material materialBig = new ServerLocation(protocol.getWorld(), x + (double)dx, y + (double)dy, z + (double)dz).getBlock(protocol).getTypeOrNull();
                    if (materialBig != null && (BlockUtils.areHoneyBlocks(materialBig) || BlockUtils.areBeds(materialBig) || BlockUtils.areSlimeBlocks(materialBig))) {
                        protocol.slime0Tick = 3;
                    }
                    if ((materialBig2 = new ServerLocation(protocol.getWorld(), xF + (double)dx, yF + (double)dy - 1.0, zF + (double)dz).getBlock(protocol).getTypeOrNull()) != null && BlockUtils.areSlimeBlocks(materialBig2)) {
                        protocol.slime0Tick = 3;
                    }
                    if ((materialBigFrom = new ServerLocation(protocol.getWorld(), xF + (double)dx, yF + (double)dy.5, zF + (double)dz).getBlock(protocol).getTypeOrNull()) != null && BlockUtils.areSlimeBlocks(materialBigFrom)) {
                        protocol.slime0Tick = 3;
                    }
                    if (protocol.slime0Tick <= 0) continue;
                    --protocol.slime0Tick;
                    this.slimeHeight = true;
                    this.slimeWide = true;
                    this.slime = true;
                }
            }
        }
    }

    private boolean calculateBubbleWater(PlayerProtocol protocol, ServerLocation location) {
        int minY;
        int blockY;
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) && (blockY = location.getBlockY()) > (minY = BlockUtils.getMinHeight(protocol.getWorld()))) {
            ServerLocation locationModified = location.clone();
            int max = blockY - minY;
            int playerHeight = NumberConversions.ceil((double)protocol.getEyeHeight());
            HashSet<Integer> emptyNonLiquid = new HashSet<Integer>(max);
            HashSet<Integer> fullNonLiquid = new HashSet<Integer>(max);
            for (int i = 0; i <= max; ++i) {
                Collection<ServerLocation> locations = locationModified.clone().add(0.0, -i, 0.0).getSurroundingLocations(0.3, 0.0, 0.3);
                for (ServerLocation loc : locations) {
                    ServerBlock block = loc.getBlock();
                    Material type = block.getType();
                    if (type == Material.SOUL_SAND) {
                        protocol.soulSandWater = System.currentTimeMillis();
                        protocol.lastVelocity = System.currentTimeMillis();
                        return true;
                    }
                    if (type == MAGMA_BLOCK) {
                        protocol.magmaCubeWater = System.currentTimeMillis();
                        protocol.lastVelocity = System.currentTimeMillis();
                        return true;
                    }
                    if (BlockUtils.isSolid(type)) {
                        if (block.isWaterLogged()) continue;
                        fullNonLiquid.add(i);
                        if (fullNonLiquid.size() != playerHeight) continue;
                        return false;
                    }
                    if (block.isWaterLogged()) continue;
                    emptyNonLiquid.add(i);
                    if (emptyNonLiquid.size() != 8) continue;
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isAllFalse() {
        return !this.isIce() && !this.isSemi() && !this.isSlime() && !this.isSlimeHeight() && !this.isLiquid() && !this.isJumpModify() && !this.isBubble();
    }

    @Generated
    public boolean isIce() {
        return this.ice;
    }

    @Generated
    public boolean isSlime() {
        return this.slime;
    }

    @Generated
    public boolean isSlimeWide() {
        return this.slimeWide;
    }

    @Generated
    public boolean isSlimeHeight() {
        return this.slimeHeight;
    }

    @Generated
    public boolean isLiquid() {
        return this.liquid;
    }

    @Generated
    public boolean isSemi() {
        return this.semi;
    }

    @Generated
    public boolean isGlide() {
        return this.glide;
    }

    @Generated
    public boolean isJumpModify() {
        return this.jumpModify;
    }

    @Generated
    public boolean isClimb() {
        return this.climb;
    }

    @Generated
    public boolean isBubble() {
        return this.bubble;
    }

    @Generated
    public void setIce(boolean ice) {
    }

    @Generated
    public void setSlime(boolean slime) {
    }

    @Generated
    public void setSlimeWide(boolean slimeWide) {
    }

    @Generated
    public void setSlimeHeight(boolean slimeHeight) {
    }

    @Generated
    public void setLiquid(boolean liquid) {
    }

    @Generated
    public void setSemi(boolean semi) {
    }

    @Generated
    public void setGlide(boolean glide) {
    }

    @Generated
    public void setJumpModify(boolean jumpModify) {
    }

    @Generated
    public void setClimb(boolean climb) {
    }

    @Generated
    public void setBubble(boolean bubble) {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EnvironmentData)) {
            return false;
        }
        EnvironmentData other = (EnvironmentData)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isIce() != other.isIce()) {
            return false;
        }
        if (this.isSlime() != other.isSlime()) {
            return false;
        }
        if (this.isSlimeWide() != other.isSlimeWide()) {
            return false;
        }
        if (this.isSlimeHeight() != other.isSlimeHeight()) {
            return false;
        }
        if (this.isLiquid() != other.isLiquid()) {
            return false;
        }
        if (this.isSemi() != other.isSemi()) {
            return false;
        }
        if (this.isGlide() != other.isGlide()) {
            return false;
        }
        if (this.isJumpModify() != other.isJumpModify()) {
            return false;
        }
        if (this.isClimb() != other.isClimb()) {
            return false;
        }
        return this.isBubble() == other.isBubble();
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof EnvironmentData;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        result = result * 59 + (this.isIce() ? 79 : 97);
        result = result * 59 + (this.isSlime() ? 79 : 97);
        result = result * 59 + (this.isSlimeWide() ? 79 : 97);
        result = result * 59 + (this.isSlimeHeight() ? 79 : 97);
        result = result * 59 + (this.isLiquid() ? 79 : 97);
        result = result * 59 + (this.isSemi() ? 79 : 97);
        result = result * 59 + (this.isGlide() ? 79 : 97);
        result = result * 59 + (this.isJumpModify() ? 79 : 97);
        result = result * 59 + (this.isClimb() ? 79 : 97);
        result = result * 59 + (this.isBubble() ? 79 : 97);
        return result;
    }

    @Generated
    public String toString() {
        return "EnvironmentData(ice=" + this.isIce() + ", slime=" + this.isSlime() + ", slimeWide=" + this.isSlimeWide() + ", slimeHeight=" + this.isSlimeHeight() + ", liquid=" + this.isLiquid() + ", semi=" + this.isSemi() + ", glide=" + this.isGlide() + ", jumpModify=" + this.isJumpModify() + ", climb=" + this.isClimb() + ", bubble=" + this.isBubble() + ")";
    }

    @Generated
    public EnvironmentData() {
    }
}

