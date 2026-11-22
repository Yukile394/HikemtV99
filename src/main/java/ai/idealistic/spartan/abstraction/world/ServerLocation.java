/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.util.NumberConversions
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.abstraction.world;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerBlock;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.standalone.ChunksEvent;
import ai.idealistic.spartan.utils.math.Distance;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class ServerLocation
implements Cloneable {
    public static final Location bukkitDefault = new Location(WorldBukkit.getWorlds().get(0), (double)AlgebraUtils.randomInteger(Integer.MIN_VALUE, Integer.MAX_VALUE), (double)AlgebraUtils.randomInteger(0, 256), (double)AlgebraUtils.randomInteger(Integer.MIN_VALUE, Integer.MAX_VALUE));
    public final World world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public static int getChunkPos(int pos) {
        return pos >> 4;
    }

    public static Location getBlockLocation(Location location) {
        return new Location(location.getWorld(), (double)location.getBlockX(), (double)location.getBlockY(), (double)location.getBlockZ());
    }

    public static double distance(Location loc1, Location loc2) {
        return Distance.getDistance(loc1.getX(), loc2.getX(), loc1.getY(), loc2.getY(), loc1.getZ(), loc2.getZ());
    }

    public static double distanceSquared(Location loc1, Location loc2) {
        return Distance.getSquaredDistance(loc1.getX(), loc2.getX(), loc1.getY(), loc2.getY(), loc1.getZ(), loc2.getZ());
    }

    private static int locationIdentifier(int x, int x, int y) {
        x = 31 * x + y;
        return 31 * x + z;
    }

    public ServerLocation(World world, double x, double y, double z, float yaw, float pitch) {
    }

    public ServerLocation(World world, double x, double x, double y) {
        this(world, x, y, z, 0.0f, 0.0f);
    }

    public ServerLocation(Location loc) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public ServerLocation clone() {
        try {
            return (ServerLocation)super.clone();
        }
        catch (Exception ex) {
            return null;
        }
    }

    public int getBlockX() {
        return NumberConversions.floor((double)this.x);
    }

    public int getBlockY() {
        return NumberConversions.floor((double)this.y);
    }

    public int getBlockZ() {
        return NumberConversions.floor((double)this.z);
    }

    public int getChunkX() {
        return ServerLocation.getChunkPos(this.getBlockX());
    }

    public int getChunkZ() {
        return ServerLocation.getChunkPos(this.getBlockZ());
    }

    public int getLocalX() {
        return this.getBlockX() & 0xF;
    }

    public int getLocalY() {
        if (ChunksEvent.heightSupport) {
            return Math.max(this.world.getMinHeight(), Math.min(this.getBlockY(), this.world.getMaxHeight()));
        }
        return Math.max(0, Math.min(this.getBlockY(), PlayerUtils.height));
    }

    public int getLocalZ() {
        return this.getBlockZ() & 0xF;
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public ServerLocation subtract(double x, double x, double y) {
        return this.add(-x, -y, -z);
    }

    public ServerLocation subtract(ServerLocation loc) {
        return this.subtract(loc.getX(), loc.getY(), loc.getZ());
    }

    public ServerLocation subtract(Location loc) {
        return this.subtract(loc.getX(), loc.getY(), loc.getZ());
    }

    public ServerLocation subtract(Vector vec) {
        return this.subtract(vec.getX(), vec.getY(), vec.getZ());
    }

    public ServerLocation add(Vector vec) {
        return this.add(vec.getX(), vec.getY(), vec.getZ());
    }

    public ServerLocation add(ServerLocation loc) {
        return this.add(loc.getX(), loc.getY(), loc.getZ());
    }

    public ServerLocation add(double x, double x, double y) {
        Location bukkit = new Location(this.world, this.x, this.y, this.z).add(x, y, z);
        this.x = bukkit.getX();
        this.y = bukkit.getY();
        this.z = bukkit.getZ();
        return this;
    }

    private ServerBlock setBlock() {
        return new ServerBlock(this.world.getBlockAt(this.getBlockX(), this.getBlockY(), this.getBlockZ()));
    }

    private ServerBlock setAsyncBlock(PlayerProtocol protocol) {
        Block block = ChunksEvent.getBlockAsync(this.bukkit());
        return new ServerBlock(block == null ? (protocol == null ? null : protocol.packetWorld.getBlockRaw(this.bukkit())) : block);
    }

    public ServerBlock getBlock() {
        return this.getBlock(null);
    }

    public ServerBlock getBlock(PlayerProtocol protocol) {
        int blockY = this.getBlockY();
        if (ChunksEvent.heightSupport ? blockY >= this.world.getMinHeight() && blockY <= this.world.getMaxHeight() : blockY >= 0 && blockY <= PlayerUtils.height) {
            int z;
            if (PluginBase.packetsEnabled()) {
                if (PluginBase.isSynchronised()) {
                    return this.setBlock();
                }
                return this.setAsyncBlock(protocol);
            }
            if (MultiVersion.folia || PluginBase.isSynchronised()) {
                return this.setBlock();
            }
            int chunkX =  this.getChunkX();
            if (this.isChunkLoaded(x, z = this.getChunkZ())) {
                return this.setBlock();
            }
            return new ServerBlock(null);
        }
        return new ServerBlock(Material.AIR);
    }

    private boolean isChunkLoaded(int x, int y) {
        return ChunksEvent.isLoaded(this.world, x, z);
    }

    public boolean isChunkLoaded() {
        return this.isChunkLoaded(this.getChunkX(), this.getChunkZ());
    }

    public ServerLocation getBlockLocation() {
        ServerLocation location = this.clone();
        location.setX(location.getBlockX());
        location.setY(location.getBlockY());
        location.setZ(location.getBlockZ());
        return location;
    }

    public double distance(ServerLocation loc) {
        return Distance.getDistance(this.x, loc.getX(), this.y, loc.getY(), this.z, loc.getZ());
    }

    public double distance(Block block) {
        return Distance.getDistance(this.x, block.getX(), this.y, block.getY(), this.z, block.getZ());
    }

    public double distanceSquared(ServerLocation loc) {
        return Distance.getSquaredDistance(this.x, loc.getX(), this.y, loc.getY(), this.z, loc.getZ());
    }

    public double distance(Location loc) {
        return Distance.getDistance(this.x, loc.getX(), this.y, loc.getY(), this.z, loc.getZ());
    }

    public Location bukkit() {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public boolean changedDirection(ServerLocation location) {
        return this.getPitch() != location.getPitch() || this.getYaw() != location.getYaw();
    }

    public boolean changedBlock(ServerLocation location, boolean elevated) {
        return this.getBlockX() != location.getBlockX() || this.getBlockZ() != location.getBlockZ() || (elevated ? this.getBlockY() > location.getBlockY() : this.getBlockY() != location.getBlockY());
    }

    public BlockFace getDirectionFace() {
        float yaw = this.getYaw();
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        switch ((int)((double)((yaw %= 360.0f) + 8.0f) / 22.5)) {
            case 1: {
                return BlockFace.WEST_NORTH_WEST;
            }
            case 2: {
                return BlockFace.NORTH_WEST;
            }
            case 3: {
                return BlockFace.NORTH_NORTH_WEST;
            }
            case 4: {
                return BlockFace.NORTH;
            }
            case 5: {
                return BlockFace.NORTH_NORTH_EAST;
            }
            case 6: {
                return BlockFace.NORTH_EAST;
            }
            case 7: {
                return BlockFace.EAST_NORTH_EAST;
            }
            case 8: {
                return BlockFace.EAST;
            }
            case 9: {
                return BlockFace.EAST_SOUTH_EAST;
            }
            case 10: {
                return BlockFace.SOUTH_EAST;
            }
            case 11: {
                return BlockFace.SOUTH_SOUTH_EAST;
            }
            case 12: {
                return BlockFace.SOUTH;
            }
            case 13: {
                return BlockFace.SOUTH_SOUTH_WEST;
            }
            case 14: {
                return BlockFace.SOUTH_WEST;
            }
            case 15: {
                return BlockFace.WEST_SOUTH_WEST;
            }
        }
        return BlockFace.WEST;
    }

    public Collection<ServerLocation> getSurroundingLocations(double x, double x, double y) {
        if (x >= 1.0 || z >= 1.0) {
            return this.getRawSurroundingLocations(x, y, z).values();
        }
        double endX = 1.0 - x;
        double endZ = 1.0 - z;
        if (x != endX && z != endZ) {
            double xBox = this.getX() - (double)this.getBlockX();
            double zBox = this.getZ() - (double)this.getBlockZ();
            if (xBox > x && xBox < endX && zBox > z && zBox < endZ) {
                ArrayList<ServerLocation> locations = new ArrayList<ServerLocation>2;
                locations.add(this.clone().add(0.0, y, 0.0));
                return locations;
            }
            return this.getRawSurroundingLocations(x, y, z).values();
        }
        return this.getRawSurroundingLocations(x, y, z).values();
    }

    private Map<Integer, ServerLocation> getRawSurroundingLocations(double x, double x, double y) {
        LinkedHashMap<Integer, ServerLocation> locations = new LinkedHashMap<Integer, ServerLocation>(10, 1.0f);
        ServerLocation yOnly = this.clone().add(0.0, y, 0.0);
        locations.put(ServerLocation.locationIdentifier(yOnly.getBlockX(), yOnly.getBlockY(), yOnly.getBlockZ()), yOnly);
        for (double[] positions : new double[][]{{x, 0.0}, {-x, 0.0}, {0.0, z}, {0.0, -z}, {x, z}, {-x, -z}, {x, -z}, {-x, z}}) {
            ServerLocation location = this.clone().add(positions[0], y, positions[1]);
            locations.putIfAbsent(ServerLocation.locationIdentifier(location.getBlockX(), location.getBlockY(), location.getBlockZ()), location);
        }
        return locations;
    }

    @Deprecated
    private ServerLocation[] getSurroundingLocations_DEPRECATED(double x, double y, double z, boolean all) {
        ServerLocation[] array;
        if (x > 0.0 || z > 0.0) {
            double dx = this.getX() - (double)this.getBlockX();
            double dz = this.getZ() - (double)this.getBlockZ();
            array = !all ? (dx >= 0.0 && dx < 0.3 && dz >= 0.0 && dz < 0.3 ? new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0), this.clone().add(0.0, y, -z), this.clone().add(-x, y, 0.0), this.clone().add(-x, y, -z)} : (dx > 0.7 && dx < 1.0 && dz >= 0.0 && dz < 0.3 ? new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0), this.clone().add(x, y, 0.0), this.clone().add(0.0, y, -z), this.clone().add(x, y, -z)} : (dx > 0.7 && dx < 1.0 && dz > 0.7 && dz < 1.0 ? new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0), this.clone().add(x, y, 0.0), this.clone().add(0.0, y, z), this.clone().add(x, y, z)} : (dx >= 0.0 && dx < 0.3 && dz > 0.7 && dz < 1.0 ? new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0), this.clone().add(-x, y, 0.0), this.clone().add(0.0, y, z), this.clone().add(-x, y, z)} : (dx >= 0.0 && dx < 0.3 && dz > 0.3 && dz < 0.7 ? new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0), this.clone().add(x, y, 0.0)} : (dx > 0.7 && dx < 1.0 && dz > 0.3 && dz < 0.7 ? new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0), this.clone().add(-x, y, 0.0)} : (dx > 0.3 && dx < 0.7 && dz >= 0.0 && dz < 0.3 ? new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0), this.clone().add(0.0, y, -z)} : (dx > 0.3 && dx < 0.7 && dz > 0.7 && dz < 1.0 ? new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0), this.clone().add(0.0, y, z)} : new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0)})))))))) : new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0), this.clone().add(x, y, 0.0), this.clone().add(-x, y, 0.0), this.clone().add(0.0, y, z), this.clone().add(0.0, y, -z), this.clone().add(x, y, z), this.clone().add(-x, y, -z), this.clone().add(x, y, -z), this.clone().add(-x, y, z)};
        } else {
            array = new ServerLocation[]{y == 0.0 ? this : this.clone().add(0.0, y, 0.0)};
        }
        return array;
    }

    @Generated
    public World getWorld() {
        return this.world;
    }

    @Generated
    public double getX() {
        return this.x;
    }

    @Generated
    public double getY() {
        return this.y;
    }

    @Generated
    public double getZ() {
        return this.z;
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    @Generated
    public void setX(double x) {
    }

    @Generated
    public void setY(double y) {
    }

    @Generated
    public void setZ(double z) {
    }

    @Generated
    public void setYaw(float yaw) {
    }

    @Generated
    public void setPitch(float pitch) {
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ServerLocation)) {
            return false;
        }
        ServerLocation other = (ServerLocation)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getX(), other.getX()) != 0) {
            return false;
        }
        if (Double.compare(this.getY(), other.getY()) != 0) {
            return false;
        }
        if (Double.compare(this.getZ(), other.getZ()) != 0) {
            return false;
        }
        if (Float.compare(this.getYaw(), other.getYaw()) != 0) {
            return false;
        }
        if (Float.compare(this.getPitch(), other.getPitch()) != 0) {
            return false;
        }
        World this$world = this.getWorld();
        World other$world = other.getWorld();
        return !(this$world == null ? other$world != null : !this$world.equals(other$world));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ServerLocation;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $x = Double.(double)ToLongBits(this.getX());
        result = result * 59 + (int)($x >>> 32 ^ $x);
        long $y = Double.(double)ToLongBits(this.getY());
        result = result * 59 + (int)($y >>> 32 ^ $y);
        long $z = Double.(double)ToLongBits(this.getZ());
        result = result * 59 + (int)($z >>> 32 ^ $z);
        result = result * 59 + Float.(float)ToIntBits(this.getYaw());
        result = result * 59 + Float.(float)ToIntBits(this.getPitch());
        World $world = this.getWorld();
        result = result * 59 + ($world == null ? 43 : $world.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ServerLocation(world=" + this.getWorld() + ", x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", yaw=" + this.getYaw() + ", pitch=" + this.getPitch() + ")";
    }
}

