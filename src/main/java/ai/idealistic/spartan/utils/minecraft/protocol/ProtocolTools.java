/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.InternalStructure
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package ai.idealistic.spartan.utils.minecraft.protocol;

import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.listeners.protocol.MovementListener;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ProtocolTools {
    public static Location readLocation(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        if (packet.getDoubles().size() >= 3) {
            return new Location(ProtocolLib.getWorld((Entity)event.getPlayer()), ((Double)packet.getDoubles().read(0)).(double)Value(), ((Double)packet.getDoubles().read(1)).(double)Value(), ((Double)packet.getDoubles().read(2)).(double)Value());
        }
        return null;
    }

    public static boolean isFlying(PacketEvent event, Location to, Location from) {
        PacketType p = event.getPacket().getType();
        if (p.equals(PacketType.Play.Client.POSITION) && to.toVector().equals((Object)from.toVector())) {
            return true;
        }
        return p.equals((Object)(MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17) ? PacketType.Play.Client.GROUND : PacketType.Play.Client.FLYING));
    }

    public static boolean onGroundPacketLevel(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        return packet.getBooleans().size() > 0 && Booleanpacket.getBooleans().read(0) != false;
    }

    public static Set<MovementListener.tpFlags> getTeleportFlags(PacketEvent event) {
        String s = ((InternalStructure)event.getPacket().getStructures().getValues().get(0)).toString();
        HashSet<MovementListener.tpFlags> flags = new HashSet<MovementListener.tpFlags>3;
        if ((s = s.replace("X_ROT", "").replace("Y_ROT", "")).contains("X")) {
            flags.add(MovementListener.tpFlags.X);
        }
        if (s.contains("Y")) {
            flags.add(MovementListener.tpFlags.Y);
        }
        if (s.contains("Z")) {
            flags.add(MovementListener.tpFlags.Z);
        }
        return flags;
    }

    public static boolean isLoadLocation(Location location) {
        return location.getX() == 1.0 && location.getY() == 1.0 && location.getZ() == 1.0;
    }

    public static Location getLoadLocation(Player player) {
        return new Location(ProtocolLib.getWorld((Entity)player), 1.0, 1.0, 1.0);
    }

    public static boolean hasPosition(PacketType type) {
        return type.equals(PacketType.Play.Client.POSITION) || type.equals(PacketType.Play.Client.POSITION_LOOK);
    }

    public static boolean hasRotation(PacketType type) {
        return type.equals(PacketType.Play.Client.LOOK) || type.equals(PacketType.Play.Client.POSITION_LOOK);
    }
}

