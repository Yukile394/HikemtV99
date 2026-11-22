/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Creeper
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Skeleton
 *  org.bukkit.entity.Villager
 *  org.bukkit.entity.Zombie
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.utils.math;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerBlock;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.math.RayLine;
import ai.idealistic.spartan.utils.minecraft.entity.AxisMatrix;
import ai.idealistic.spartan.utils.minecraft.entity.MovingObjectPosition;
import ai.idealistic.spartan.utils.minecraft.vector.Vec3;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.util.Vector;

public final class RayUtils {
    private static final boolean[] BOOLEANS = new boolean[]{true, false};

    public static double scaleVal(double value, double scale) {
        double scale2 = Math.pow(10.0, scale);
        return Math.ceil(value * scale2) / scale2;
    }

    public static boolean validRayLines(RayLine a, RayLine b, double radi) {
        double angleB;
        double angleA = Math.atan2(a.z, a.x);
        double angleDiff = Math.abs(angleA - (angleB = Math.atan2(b.z, b.x)));
        if (angleDiff > Math.PI) {
            angleDiff = Math.PI * 2 - angleDiff;
        }
        return angleDiff <= Math.toRadians(radi);
    }

    public static double calculateRayLines(RayLine a, RayLine b) {
        double angleB;
        double angleA = Math.atan2(a.z, a.x);
        double angleDiff = Math.abs(angleA - (angleB = Math.atan2(b.z, b.x)));
        if (angleDiff > Math.PI) {
            angleDiff = Math.PI * 2 - angleDiff;
        }
        return Math.toDegrees(angleDiff);
    }

    public static double calculateRayLine(RayLine a) {
        return Math.atan2(a.z, a.x);
    }

    public static float masterCast(float num) {
        return RayUtils.sinusoidOnlyMin(RayUtils.castTo360(num));
    }

    public static float castTo360(float num) {
        float value = Math.abs((num + 360.0f) % 360.0f - 180.0f);
        return value;
    }

    public static float castTo360WithLimit(float num) {
        float value = Math.abs((num + 360.0f) % 360.0f - 180.0f);
        int wrapShitMath = (int)Math.floor(value / 360.0f);
        return value - (float)(wrapShitMath * 360);
    }

    public static float sinusoidOnlyMin(float num) {
        int wrapShitMath = (int)Math.floor(num / 180.0f);
        return num - (float)(wrapShitMath * 180);
    }

    public static float set360Limit(float num) {
        int wrapShitMath = (int)Math.floor(num / 360.0f);
        return num - (float)(wrapShitMath * 360);
    }

    @SafeVarargs
    public static boolean onBlock(PlayerProtocol protocol, Location location, Set<Material> ... sets) {
        World world = protocol.getWorld();
        Vector playerLocation = location.toVector();
        Vector min = playerLocation.clone().add(new Vector(-0.3, -0.5, -0.3));
        Vector max = playerLocation.clone().add(new Vector(0.3, 0.1, 0.3));
        for (int blockX =  min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int blockY =  min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int blockZ =  min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    ServerBlock block = new ServerLocation(world, x, y, z).getBlock(protocol);
                    for (Set<Material> set : sets) {
                        if (!set.contains(block.getType())) continue;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean onBlock(PlayerProtocol protocol, Location location, Set<Material> set) {
        World world = protocol.getWorld();
        Vector playerLocation = location.toVector();
        Vector min = playerLocation.clone().add(new Vector(-0.3, -0.5, -0.3));
        Vector max = playerLocation.clone().add(new Vector(0.3, 0.1, 0.3));
        for (int blockX =  min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int blockY =  min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int blockZ =  min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    ServerBlock block = new ServerLocation(world, x, y, z).getBlock(protocol);
                    if (!set.contains(block.getType())) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean onBlock(PlayerProtocol protocol, Location location, Material material) {
        World world = protocol.getWorld();
        Vector playerLocation = location.toVector();
        Vector min = playerLocation.clone().add(new Vector(-0.3, -0.5, -0.3));
        Vector max = playerLocation.clone().add(new Vector(0.3, 0.1, 0.3));
        for (int blockX =  min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int blockY =  min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int blockZ =  min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    ServerBlock block = new ServerLocation(world, x, y, z).getBlock(protocol);
                    if (block.getType() != material) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean onSolidBlock(PlayerProtocol protocol, Location location) {
        World world = protocol.getWorld();
        Vector playerLocation = location.toVector();
        Vector min = playerLocation.clone().add(new Vector(-0.3, -0.5, -0.3));
        Vector max = playerLocation.clone().add(new Vector(0.3, 0.1, 0.3));
        for (int blockX =  min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int blockY =  min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int blockZ =  min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    ServerBlock block = new ServerLocation(world, x, y, z).getBlock(protocol);
                    if (!BlockUtils.isSolid(block.getType())) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSolidBlock(PlayerProtocol protocol, Location location) {
        World world = protocol.getWorld();
        Vector playerLocation = location.toVector();
        Vector min = playerLocation.clone().add(new Vector(-0.3, -0.3, -0.3));
        Vector max = playerLocation.clone().add(new Vector(0.3, 0.3, 0.3));
        for (int blockX =  min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int blockY =  min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int blockZ =  min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    ServerBlock block = new ServerLocation(world, x, y, z).getBlock(protocol);
                    if (!BlockUtils.isSolid(block.getType())) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean inBlock(PlayerProtocol protocol, Location location) {
        World world = protocol.getWorld();
        Vector playerLocation = location.toVector();
        Vector min = playerLocation.clone().add(new Vector(-0.3, 0.0, -0.3));
        Vector max = playerLocation.clone().add(new Vector(0.3, 0.5, 0.3));
        for (int blockX =  min.getBlockX(); x <= max.getBlockX(); ++x) {
            for (int blockY =  min.getBlockY(); y <= max.getBlockY(); ++y) {
                for (int blockZ =  min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                    ServerBlock block = new ServerLocation(world, x, y, z).getBlock(protocol);
                    if (!BlockUtils.isSolid(block.getType())) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static float[] getRotation(Player player1, Entity entity2) {
        Location entityLoc = ProtocolLib.getLocationOrNull(entity2);
        if (entityLoc == null) {
            return null;
        }
        Vector direction = entityLoc.toVector().subtract(PluginBase.getProtocol(player1).getLocation().toVector());
        float yaw = (float)Math.toDegrees(Math.atan2(direction.getX(), direction.getZ()));
        float pitch = (float)Math.toDegrees(Math.asin(-direction.getY()));
        return new float[]{RayUtils.castTo360(yaw), pitch};
    }

    public static boolean inHitbox(PlayerProtocol protocol, Entity target, float size) {
        boolean exempt;
        Location location = protocol.getLocation();
        boolean intersection = false;
        boolean intersection2 = false;
        if (target instanceof Player) {
            Location playerLocation = ProtocolLib.getLocationOrNull((Player)target);
            if (playerLocation == null) {
                return false;
            }
            double targetX = playerLocation.getX();
            double targetY = playerLocation.getY();
            double targetZ = playerLocation.getZ();
            AxisMatrix boundingBox = new AxisMatrix(targetX - (double)size, targetY - (double)0.1f, targetZ - (double)size, targetX + (double)size, targetY + (double)1.9f, targetZ + (double)size);
            intersection = RayUtils.isIntersection(protocol, location, intersection, boundingBox);
            exempt = target.isInsideVehicle();
        } else {
            double targetX = target.getLocation().getX();
            double targetY = target.getLocation().getY();
            double targetZ = target.getLocation().getZ();
            AxisMatrix boundingBox = new AxisMatrix(targetX - (double)size, targetY - (double)0.1f, targetZ - (double)size, targetX + (double)size, targetY + (double)1.9f, targetZ + (double)size);
            intersection = RayUtils.isIntersection(protocol, location, intersection, boundingBox);
            intersection2 = RayUtils.isIntersection(protocol, protocol.getFromLocation(), intersection2, boundingBox);
            exempt = target.isInsideVehicle() || !(target instanceof Villager) && !(target instanceof Zombie) && !(target instanceof Skeleton) && !(target instanceof Creeper);
        }
        return intersection || intersection2 || exempt;
    }

    public static boolean inHitbox(PlayerProtocol protocol, Location locationIn, Entity target, float size, float dist) {
        Location location = protocol.getLocation();
        boolean intersection = false;
        double targetX = locationIn.getX();
        double targetY = locationIn.getY();
        double targetZ = locationIn.getZ();
        AxisMatrix boundingBox = new AxisMatrix(targetX - (double)size, targetY - (double)0.1f, targetZ - (double)size, targetX + (double)size, targetY + (double)1.9f, targetZ + (double)size);
        intersection = RayUtils.isIntersection(protocol, location, intersection, boundingBox, dist);
        boolean exempt = target.isInsideVehicle();
        return intersection || exempt;
    }

    public static boolean inHitboxFrom(PlayerProtocol protocol, Location locationIn, Entity target, float size) {
        boolean intersection = false;
        double targetX = locationIn.getX();
        double targetY = locationIn.getY();
        double targetZ = locationIn.getZ();
        AxisMatrix boundingBox = new AxisMatrix(targetX - (double)size, targetY - (double)0.1f, targetZ - (double)size, targetX + (double)size, targetY + (double)1.9f, targetZ + (double)size);
        intersection = RayUtils.isIntersection(protocol, protocol.getPastTickRotation().bukkit(), intersection, boundingBox);
        boolean exempt = target.isInsideVehicle();
        return intersection || exempt;
    }

    public static boolean inHitbox(PlayerProtocol protocol, Location locationIn, float size) {
        Location location = protocol.getLocation();
        boolean intersection = false;
        double targetX = locationIn.getX();
        double targetY = locationIn.getY();
        double targetZ = locationIn.getZ();
        AxisMatrix boundingBox = new AxisMatrix(targetX - (double)size, targetY - (double)size, targetZ - (double)size, targetX + (double)size, targetY + (double)size, targetZ + (double)size);
        intersection = RayUtils.isIntersection(protocol, location, intersection, boundingBox);
        return intersection;
    }

    public static boolean inHitbox(PlayerProtocol protocol, Location locationIn, float size, double dist) {
        Location location = protocol.getLocation();
        boolean intersection = false;
        double targetX = locationIn.getX();
        double targetY = locationIn.getY();
        double targetZ = locationIn.getZ();
        AxisMatrix boundingBox = new AxisMatrix(targetX - (double)size, targetY - (double)size, targetZ - (double)size, targetX + (double)size, targetY + (double)size, targetZ + (double)size);
        intersection = RayUtils.isIntersection(protocol, location, intersection, boundingBox, dist);
        return intersection;
    }

    private static boolean isIntersection(PlayerProtocol protocol, Location location, boolean intersection, AxisMatrix boundingBox) {
        for (boolean rotation : BOOLEANS) {
            for (boolean sneak : BOOLEANS) {
                float pitch;
                float yaw = location.getYaw();
                MovingObjectPosition result = RayUtils.rayCast(yaw, pitch = location.getPitch(), sneak, boundingBox, protocol);
                intersection |= result != null && result.hitVec != null;
            }
        }
        return intersection;
    }

    private static boolean isIntersection(PlayerProtocol protocol, Location location, boolean intersection, AxisMatrix boundingBox, double dist) {
        for (boolean rotation : BOOLEANS) {
            for (boolean sneak : BOOLEANS) {
                float pitch;
                float yaw = location.getYaw();
                MovingObjectPosition result = RayUtils.rayCast(yaw, pitch = location.getPitch(), sneak, boundingBox, protocol, dist);
                intersection |= result != null && result.hitVec != null;
            }
        }
        return intersection;
    }

    private static MovingObjectPosition rayCast(float yaw, float pitch, boolean sneak, AxisMatrix bb, PlayerProtocol protocol) {
        Location position = protocol.getLocation();
        double lastX = position.getX();
        double lastY = position.getY();
        double lastZ = position.getZ();
        Vec3 vec3 = new Vec3(lastX, lastY + (double)RayUtils.getEyeHeight(sneak, protocol), lastZ);
        Vec3 vec31 = RayUtils.getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.add(new Vec3(vec31.xCoord * 3.0, vec31.yCoord * 3.0, vec31.zCoord * 3.0));
        return bb.calculateIntercept(vec3, vec32);
    }

    private static MovingObjectPosition rayCast(float yaw, float pitch, boolean sneak, AxisMatrix bb, PlayerProtocol protocol, double dist) {
        Location position = protocol.getLocation();
        double lastX = position.getX();
        double lastY = position.getY();
        double lastZ = position.getZ();
        Vec3 vec3 = new Vec3(lastX, lastY + (double)RayUtils.getEyeHeight(sneak, protocol), lastZ);
        Vec3 vec31 = RayUtils.getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.add(new Vec3(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist));
        return bb.calculateIntercept(vec3, vec32);
    }

    private static Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = (float)Math.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f1 = (float)Math.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f2 = (float)(-Math.cos(-pitch * ((float)Math.PI / 180)));
        float f3 = (float)Math.sin(-pitch * ((float)Math.PI / 180));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    public static float getEyeHeight(boolean sneak, PlayerProtocol protocol) {
        float f2 = 1.62f;
        if (protocol.bukkit().isSleeping()) {
            f2 = 0.2f;
        }
        if (sneak) {
            f2 -= 0.08f;
        }
        return f2;
    }

    public static double getOnlyScale(double value) {
        return value - Math.floor(value);
    }

    public static float bruteforceRayTrace(Player player, Entity target) {
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        float bruteForce = 0.01f;
        float bruteForceStart = 0.01f;
        boolean checked = false;
        for (i < 40; ++i) {
            if (RayUtils.inHitbox(protocol, target, bruteForceStart)) {
                bruteForce = bruteForceStart;
                checked = true;
                continue;
            }
            bruteForceStart += 0.01f;
        }
        return checked ? bruteForce : 0.4f;
    }

    public static float bruteforceRayTrace(PlayerProtocol protocol, Entity target) {
        float bruteForce = 0.01f;
        float bruteForceStart = 0.01f;
        boolean checked = false;
        for (i < 60; ++i) {
            if (RayUtils.inHitbox(protocol, target, bruteForceStart)) {
                bruteForce = bruteForceStart;
                checked = true;
                continue;
            }
            bruteForceStart += 0.01f;
        }
        return checked ? bruteForce : 0.6f;
    }

    public static float bruteforceRayTraceWithCustomLocation(PlayerProtocol protocol, Location location, Entity target) {
        float bruteForce = 0.01f;
        float bruteForceStart = 0.01f;
        boolean checked = false;
        for (i < 100; ++i) {
            if (RayUtils.inHitbox(protocol, location, target, bruteForceStart, 3.0f)) {
                bruteForce = bruteForceStart;
                checked = true;
                continue;
            }
            bruteForceStart += 0.01f;
        }
        return checked ? bruteForce : 0.6f;
    }

    public static double hitBoxRay(Player instance, Location player, Location target, double size, boolean sneak) {
        double d = 0.0;
        double increment = 0.1;
        int maxSteps = 60;
        float yaw = player.getYaw();
        double yR = Math.toRadians(yaw);
        RayLine ray = new RayLine(-Math.sin(yR), Math.cos(yR));
        double eyeHeight = RayUtils.getEyeHeight(sneak, instance);
        for (i < maxSteps; ++i) {
            double newX = player.getX() + ray.x * d;
            double newY = player.getY() + eyeHeight - 1.0;
            double newZ = player.getZ() + ray.z * d;
            Location newLocation = new Location(ProtocolLib.getWorld((Entity)instance), newX, newY, newZ);
            if (RayUtils.onBound(newLocation, target, size, 2.0, size)) {
                return d;
            }
            d += increment;
        }
        return d;
    }

    public static boolean onBound(Location point, Location target, double x, double x, double y) {
        boolean xB = point.getX() >= target.getX() - x / 2.0 && point.getX() <= target.getX() + x / 2.0;
        boolean yB = point.getY() >= target.getY() - y / 2.0 && point.getY() <= target.getY() + y / 2.0;
        boolean zB = point.getZ() >= target.getZ() - z / 2.0 && point.getZ() <= target.getZ() + z / 2.0;
        return xB && yB && zB;
    }

    public static float getEyeHeight(boolean sneak, Player player) {
        float f2 = 1.62f;
        if (player.isSleeping()) {
            f2 = 0.2f;
        }
        if (sneak) {
            f2 -= 0.08f;
        }
        return f2;
    }

    public static boolean isSaveToTeleport(PlayerProtocol protocol, Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -1; dz <= 1; ++dz) {
                    Material material = new ServerLocation(new Location(protocol.getWorld(), x.3, y.3, z.3)).getBlock(protocol).getTypeOrNull();
                    if (material == null || BlockUtils.areAir(material)) continue;
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean ifRayBound(PlayerProtocol protocol, AxisMatrix boundingBox, double dist) {
        boolean intersection = false;
        intersection = RayUtils.isIntersection(protocol, protocol.getLocation(), intersection, boundingBox, dist);
        return intersection;
    }

    public static boolean ifRayBoundClient(PlayerProtocol protocol, AxisMatrix boundingBox, double dist) {
        boolean intersection = false;
        boolean intersection2 = false;
        intersection = RayUtils.isIntersection(protocol, protocol.getLocation(), intersection, boundingBox, dist);
        intersection2 = RayUtils.isIntersection(protocol, protocol.getFromLocation(), intersection, boundingBox, dist);
        return intersection || intersection2;
    }

    @Generated
    private RayUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

