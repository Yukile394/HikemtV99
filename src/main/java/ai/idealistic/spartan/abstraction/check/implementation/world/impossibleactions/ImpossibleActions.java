/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions.AutoRespawn;
import ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions.GhostHand;
import ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions.Buffer;
import ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions.Tower;
import ai.idealistic.spartan.abstraction.event.CBlockPlaceEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.protocol.DeathListener;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class ImpossibleActions
extends CheckRunner {
    private final CheckDetection ex = new ImplementedDetection(this, null, null, "scaffold_direction", true);
    private final CheckDetection ey = new ImplementedDetection(this, Check.DataType.JAVA, null, "scaffold_up", true);
    private final CheckDetection ez = new ImplementedDetection(this, Check.DataType.JAVA, null, "scaffold_yaw", true);
    private final CheckDetection eA = new ImplementedDetection(this, null, null, "actions", true);
    private final Tower eB;
    private final Buffer eC;
    private final GhostHand eD;
    private final AutoRespawn eE = new AutoRespawn(this);

    public ImpossibleActions(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
        this.eD = new GhostHand(this);
        this.eB = new Tower(this);
        this.eC = new Buffer(this);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object == null) {
            this.eE.run();
            return;
        } else if (object instanceof CBlockPlaceEvent) {
            BlockFace blockFace;
            CBlockPlaceEvent event = (CBlockPlaceEvent)object;
            if (ItemsAdder.is(event.placedBlock)) return;
            Location playerLocation = this.protocol.getLocationOrVehicle();
            int playerY = playerLocation.getBlockY();
            int blockY = event.placedBlock.getY();
            this.eC.checkCheckCancellation(event.placedBlock);
            if (blockY >= playerY || (blockFace = event.placedAgainstBlock.getFace(event.placedBlock)) == null) return;
            switch (blockFace) {
                case EAST:
                case EAST_NORTH_EAST:
                case EAST_SOUTH_EAST:
                case WEST:
                case WEST_NORTH_WEST:
                case WEST_SOUTH_WEST:
                case NORTH:
                case NORTH_EAST:
                case NORTH_NORTH_EAST:
                case SOUTH:
                case SOUTH_EAST:
                case SOUTH_SOUTH_EAST: {
                    double directionDistance;
                    if (!this.ex.canCall() && !this.ey.canCall() && !this.ez.canCall()) return;
                    directionDistance = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) ? this.getDirection(blockFace).distance(playerLocation.getDirection()) : Double.MAX_VALUE;
                    if (directionDistance <= 1.25) {
                        this.ex.call(() -> this.ex.cancel("scaffold(direction), vector-distance: " + directionDistance + ", block-distance: " + ServerLocation.distance(playerLocation, event.placedAgainstBlock.getLocation())));
                        return;
                    }
                    float pitch = playerLocation.getPitch();
                    if ((double)pitch <= 0.0) {
                        this.ey.call(() -> this.ey.cancel("scaffold(up), pitch: " + pitch + ", block-distance: " + ServerLocation.distance(playerLocation, event.placedAgainstBlock.getLocation())));
                        return;
                    }
                    this.ez.call(() -> {
                        List<Map.Entry<Long, Location>> fromLocations = this.protocol.getPositionHistoryEntries();
                        int requiredComparisons = 5;
                        int requiredSize = requiredComparisons + 1;
                        if (fromLocations.size() >= requiredSize) {
                            Iterator<Map.Entry<Long, Location>> iterator = (fromLocations = fromLocations.subList(fromLocations.size() - requiredSize, fromLocations.size())).iterator();
                            if (!iterator.hasNext()) {
                                return;
                            }
                            Map.Entry<Long, Location> previousLocation = iterator.next();
                            double yawDeviation = 0.0;
                            while (iterator.hasNext()) {
                                Map.Entry<Long, Location> loopLocation = iterator.next();
                                long timePassed = System.currentTimeMillis() - loopLocation.getKey();
                                if (timePassed <= (long)(requiredComparisons * 2) * 50L) {
                                    double diff = (previousLocation.getValue().getYaw() - loopLocation.getValue().getYaw()) % 360.0f;
                                    yawDeviation += diff * diff;
                                    previousLocation = loopLocation;
                                    continue;
                                }
                                return;
                            }
                            if ((yawDeviation = Math.sqrt(yawDeviation / (double)requiredComparisons)) >= 270.0) {
                                this.ez.cancel("scaffold(yaw), deviation: " + yawDeviation + ", block-distance: " + ServerLocation.distance(playerLocation, event.placedAgainstBlock.getLocation()));
                            }
                        }
                    });
                    return;
                }
                case UP: {
                    this.eB.handleCheck(event.placedBlock, playerLocation, playerY, blockY);
                }
                default: {
                    return;
                }
            }
        } else if (object instanceof BlockBreakEvent) {
            this.eD.handleCheck((BlockBreakEvent)object);
            return;
        } else if (object instanceof PlayerInteractEvent) {
            PlayerInteractEvent event = (PlayerInteractEvent)object;
            this.eD.handleCheck(event);
            this.eA.call(() -> {
                int cases;
                Action action = event.getAction();
                if (!(action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK || Compatibility.CompatibilityType.MC_MMO.isFunctional() || (cases = this.protocol.bukkit().isSleeping() ? 1 : (this.protocol.bukkit().isDead() ? 2 : (BlockUtils.hasMaterial(this.protocol.bukkit().getOpenInventory().getCursor()) ? 3 : 0))) == 0)) {
                    String action_string = action.toString().toLowerCase().replace("_", "-");
                    Block clickedBlock =  event.getClickedBlock();
                    if (b == null) {
                        this.eA.cancel("actions, action: " + action_string + ", case: " + cases);
                    } else if (!ItemsAdder.is(b)) {
                        this.eA.cancel("actions, action: " + action_string + ", case: " + cases + ", block: " + BlockUtils.blockToString(b));
                    }
                }
            });
            return;
        } else if (object instanceof PlayerDeathEvent) {
            this.eE.N();
            return;
        } else {
            if (!PluginBase.packetsEnabled() || !(object instanceof PacketEvent)) return;
            PacketType eventType = ((PacketEvent)object).getPacketType();
            for (PacketType type : DeathListener.packetTypes) {
                if (!type.equals(eventType)) continue;
                this.eE.N();
                return;
            }
        }
    }

    private Vector getDirection(BlockFace blockFace) {
        Vector direction = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
        if (blockFace.getModX() != 0 || blockFace.getModY() != 0 || blockFace.getModZ() != 0) {
            direction.normalize();
        }
        return direction;
    }
}

