/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.wrappers.BlockPosition
 *  com.comphenix.protocol.wrappers.EnumWrappers$Direction
 *  com.comphenix.protocol.wrappers.EnumWrappers$Hand
 *  com.comphenix.protocol.wrappers.MovingObjectPositionBlock
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.event.ServerBlockChange;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.BackgroundProtocolLib;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.PlaceEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.ChunksEvent;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.MovingObjectPositionBlock;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockPlaceListener
extends PacketAdapter {
    public BlockPlaceListener() {
        super(Register.plugin, ListenerPriority.HIGHEST, BlockPlaceListener.init());
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol = PluginBase.getProtocol(player);
        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && protocol.isBedrockPlayer()) {
            return;
        }
        PacketContainer packet = event.getPacket();
        CheckThread.run(() -> {
            if (this.isPlacingBlock(packet)) {
                if (!packet.getBlockPositionModifier().getValues().isEmpty() || !packet.getMovingBlockPositions().getValues().isEmpty()) {
                    ItemStack itemInHand;
                    boolean isMainHand;
                    BlockPosition blockPosition = new BlockPosition(0, 0, 0);
                    EnumWrappers.Direction direction = null;
                    if (!packet.getMovingBlockPositions().getValues().isEmpty()) {
                        blockPosition = ((MovingObjectPositionBlock)packet.getMovingBlockPositions().read(0)).getBlockPosition();
                        direction = ((MovingObjectPositionBlock)packet.getMovingBlockPositions().read(0)).getDirection();
                    }
                    if (!packet.getBlockPositionModifier().getValues().isEmpty()) {
                        blockPosition = (BlockPosition)packet.getBlockPositionModifier().read(0);
                        if (packet.getDirections().getValues().isEmpty()) {
                            int directionInt = (Integer)packet.getIntegers().read(0);
                            direction = EnumWrappers.Direction.values()[directionInt];
                        } else {
                            direction = (EnumWrappers.Direction)packet.getDirections().read(0);
                        }
                    }
                    Location l = new Location(protocol.getWorld(), (double)blockPosition.toVector().getBlockX(), (double)blockPosition.toVector().getBlockY(), (double)blockPosition.toVector().getBlockZ());
                    if (direction == null) {
                        return;
                    }
                    l.add(this.getDirection(BlockFace.valueOf((String)direction.name())));
                    World world = player.getWorld();
                    Block block = ChunksEvent.getBlockAsync(new Location(world, (double)((int)l.getX()), (double)((int)l.getY()), (double)((int)l.getZ())));
                    if (block == null) {
                        return;
                    }
                    isMainHand = !MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) || event.getPacket().getHands().read(0) == EnumWrappers.Hand.MAIN_HAND;
                    ItemStack itemStack = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) ? (isMainHand ? protocol.getInventory().getItemInMainHand() : protocol.getInventory().getItemInOffHand()) : (itemInHand = player.getItemInHand());
                    if (itemInHand.getType().isBlock()) {
                        if (!this.isInPlayer(protocol.getLocation(), block.getLocation())) {
                            Block blockAgainst = player.getLocation().getBlock();
                            Location blockLoc = new Location(world, (double)blockPosition.getX(), (double)blockPosition.getY(), (double)blockPosition.getY());
                            if (!BlockUtils.isSurroundedByAir(protocol, blockLoc) || !BlockUtils.isSurroundedByAir(protocol, blockLoc.clone().add(0.0, 1.0, 0.0))) {
                                Material material = itemInHand.getType();
                                protocol.packetWorld.worldChange(new ServerBlockChange(blockPosition, material));
                                protocol.packetWorld.worldChange(new ServerBlockChange(new BlockPosition(blockPosition.getX(), blockPosition.getY() + 1, blockPosition.getZ()), material));
                            }
                            PlaceEvent.event(protocol, block, blockAgainst, event, true);
                            protocol.rightClickCounter = 0;
                        } else {
                            ++protocol.rightClickCounter;
                        }
                    } else {
                        ++protocol.rightClickCounter;
                    }
                }
            } else {
                ++protocol.rightClickCounter;
            }
        });
    }

    private boolean isPlacingBlock(PacketContainer packet) {
        BlockPosition blockPosition = new BlockPosition(0, 0, 0);
        if (packet.getHands().getValues().isEmpty()) {
            if (!packet.getMovingBlockPositions().getValues().isEmpty()) {
                blockPosition = ((MovingObjectPositionBlock)packet.getMovingBlockPositions().read(0)).getBlockPosition();
            }
            if (!packet.getBlockPositionModifier().getValues().isEmpty()) {
                blockPosition = (BlockPosition)packet.getBlockPositionModifier().read(0);
            }
            return blockPosition.getY() != -1;
        }
        return (packet.getType().equals(PacketType.Play.Client.USE_ITEM) || packet.getType().toString().contains("USE_ITEM_ON")) && ((EnumWrappers.Hand)packet.getHands().read(0)).equals(EnumWrappers.Hand.MAIN_HAND);
    }

    private boolean isInPlayer(Location player, Location block) {
        double playerX = player.getX();
        double playerY = player.getY();
        double playerZ = player.getZ();
        double playerWidth = 1.0;
        double playerHeight = 2.0;
        double minX = playerX - playerWidth / 2.0;
        double maxX = playerX + playerWidth / 2.0;
        double minY = playerY;
        double maxY = playerY + playerHeight;
        double minZ = playerZ - playerWidth / 2.0;
        double maxZ = playerZ + playerWidth / 2.0;
        double blockX = block.getX();
        double blockY = block.getY();
        double blockZ = block.getZ();
        return blockX >= minX && blockX <= maxX && blockY >= minY && blockY <= maxY && blockZ >= minZ && blockZ <= maxZ;
    }

    private Vector getDirection(BlockFace face) {
        Vector direction = new Vector(face.getModX(), face.getModY(), face.getModZ());
        if (face.getModX() != 0 || face.getModY() != 0 || face.getModZ() != 0) {
            direction.normalize();
        }
        return direction;
    }

    private static List<PacketType> init() {
        ArrayList<PacketType> listeners = new ArrayList<PacketType>();
        listeners.add(PacketType.Play.Client.BLOCK_PLACE);
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
            listeners.add(PacketType.Play.Client.USE_ITEM);
        }
        if (BackgroundProtocolLib.isPacketSupported("USE_ITEM_ON")) {
            listeners.add(PacketType.Play.Client.USE_ITEM_ON);
        }
        return listeners;
    }
}

