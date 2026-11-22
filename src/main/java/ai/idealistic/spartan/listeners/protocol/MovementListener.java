/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Boat
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package ai.idealistic.spartan.listeners.protocol;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.data.EnvironmentData;
import ai.idealistic.spartan.abstraction.event.PlayerTickEvent;
import ai.idealistic.spartan.abstraction.event.SuperPositionPacketEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.necessary.protocollib.ProtocolLib;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.bukkit.MovementEvent;
import ai.idealistic.spartan.listeners.protocol.PacketLatencyHandler;
import ai.idealistic.spartan.utils.minecraft.protocol.ProtocolTools;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener
extends PacketAdapter {
    public MovementListener() {
        super(Register.plugin, ListenerPriority.LOWEST, new PacketType[]{PacketType.Play.Server.POSITION, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK, MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_17) ? PacketType.Play.Client.GROUND : PacketType.Play.Client.FLYING});
    }

    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol =  PluginBase.getProtocol(player);
        Location tp = ProtocolTools.readLocation(event);
        if (tp == null) {
            return;
        }
        if (!event.getPacket().getFloat().getValues().isEmpty()) {
            tp.setYaw(((Float)event.getPacket().getFloat().read(0)).(float)Value());
            tp.setPitch(((Float)event.getPacket().getFloat().read(1)).(float)Value());
        }
        Location loc = p.getLocation().clone();
        Location result = tp.clone();
        Set<tpFlags> flags = ProtocolTools.getTeleportFlags(event);
        for (tpFlags flag : flags) {
            if (flag.equals(tpFlags.X)) {
                result.setX(loc.getX() + tp.getX());
            }
            if (flag.equals(tpFlags.Y)) {
                result.setY(loc.getY() + tp.getY());
            }
            if (!flag.equals(tpFlags.Z)) continue;
            result.setZ(loc.getZ() + tp.getZ());
        }
        p.setTeleportTick(true);
        p.setTeleport(result.clone());
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PlayerProtocol protocol =  PluginBase.getProtocol(player);
        PacketContainer packet = event.getPacket();
        if (!Config.settings.getBoolean("Important.bedrock_on_protocollib") && p.isBedrockPlayer()) {
            return;
        }
        CheckThread.run(() -> {
            double[] v;
            Location location =  p.getLocation();
            p.setFrom(l.clone());
            boolean onGround = ProtocolTools.onGroundPacketLevel(event);
            p.setOnGround(onGround);
            Location r = ProtocolTools.readLocation(event);
            if (r == null) {
                return;
            }
            Location c = ProtocolTools.readLocation(event);
            if (c == null) {
                return;
            }
            if (ProtocolTools.hasRotation(event.getPacket().getType())) {
                c.setYaw(((Float)event.getPacket().getFloat().read(0)).(float)Value());
                c.setPitch(((Float)event.getPacket().getFloat().read(1)).(float)Value());
            }
            double[] dArray = v = new double[]{c.getX(), c.getY(), c.getZ(), c.getYaw(), c.getPitch()};
            int n = dArray.length;
            for (i < n; ++i) {
                Double check = dArray[i];
                if (!check.isNaN() && !check.isInfinite() && !(Math.abs(check) > 3.0E8)) continue;
                PluginBase.getProtocol((Player)player).punishments.kick((CommandSender)Bukkit.getConsoleSender(), "Invalid packet");
                return;
            }
            boolean legacy = ProtocolTools.isFlying(event, l, r);
            if (c.toVector().distance(p.getFrom().toVector()) > 1.0E-5 && !p.isTeleportTick()) {
                PlayerTickEvent tickEvent = new PlayerTickEvent(p, legacy, onGround).build();
                MovementEvent.tick(tickEvent);
                if (tickEvent.getDelay() > 65L) {
                    p.lagTick = tickEvent.getDelay();
                } else if (tickEvent.getDelay() > 10L && p.lagTick != 0L) {
                    p.lagTick = 0L;
                }
                if (p.isDesync() && tickEvent.getDelay() > 40L && tickEvent.getDelay() < 60L) {
                    p.transactionVl = p.transactionVl + (p.isBlatantDesync() ? 2 : 1);
                    if (p.transactionVl > 40 && !p.isAFK()) {
                        AwarenessNotifications.optionallySend(player.getName() + " moves faster than the transaction response (" + tickEvent.getDelay() + "ms > " + (System.currentTimeMillis() - p.transactionTime) + "ms).");
                    }
                } else if (p.transactionVl > 0) {
                    p.transactionVl -= 2;
                }
            }
            if (p.transactionBoot) {
                PacketLatencyHandler.startChecking(p);
            }
            if (!ProtocolLib.getWorld((Entity)player).getName().equals(p.fromWorld)) {
                p.fromWorld = ProtocolLib.getWorld((Entity)player).getName();
                p.setLocation(ProtocolTools.getLoadLocation(player));
            }
            if (legacy) {
                p.executeRunners(event.isCancelled(), new SuperPositionPacketEvent(p, event));
                return;
            }
            boolean hasPosition = ProtocolTools.hasPosition(packet.getType());
            boolean hasRotation = ProtocolTools.hasRotation(packet.getType());
            if (hasPosition) {
                p.addRawLocation(r);
            }
            if (ProtocolTools.isLoadLocation(p.getLocation())) {
                p.setLocation(r);
                p.setFrom(r);
            } else {
                block31: {
                    if (hasPosition) {
                        Location teleport =  p.getTeleport();
                        if (to != null) {
                            if (to.getX() == r.getX() && to.getY() == r.getY() && to.getZ() == r.getZ()) {
                                p.setLocation(to.clone());
                                p.setFrom(to.clone());
                                p.setTeleport(null);
                                break block31;
                            } else {
                                Entity entity;
                                Iterator<Entity> iterator = p.getNearbyEntities(5.0).iterator();
                                do {
                                    if (iterator.hasNext()) continue;
                                    event.setCancelled(true);
                                    PluginBase.runTask(p, () -> {
                                        Location teleport = p.getTeleport();
                                        if (teleport != null) {
                                            Location build = teleport.clone();
                                            build.setYaw(p.getLocation().getYaw());
                                            build.setPitch(p.getLocation().getPitch());
                                            p.teleport(build);
                                        }
                                    });
                                    return;
                                } while (!((entity = iterator.next()) instanceof Boat));
                                return;
                            }
                        }
                        l.setX(r.getX());
                        l.setY(r.getY());
                        l.setZ(r.getZ());
                    }
                }
                if (hasRotation) {
                    l.setYaw(((Float)packet.getFloat().read(0)).(float)Value());
                    l.setPitch(((Float)packet.getFloat().read(1)).(float)Value());
                }
            }
            if (p.useItemPacket) {
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
                    if (!p.getInventory().getItemInMainHand().getType().isEdible() && !p.getInventory().getItemInOffHand().getType().isEdible()) {
                        p.useItemPacket = false;
                    }
                } else if (!p.getInventory().getItemInHand().getType().isEdible()) {
                    p.useItemPacket = false;
                }
                if (player.getFoodLevel() == 20 || p.useItemPacketReset) {
                    p.useItemPacket = false;
                }
            }
            PlayerMoveEvent moveEvent = new PlayerMoveEvent(player, p.getFromLocation(), p.getLocation());
            moveEvent.setCancelled(event.isCancelled());
            p.setEnvironment(new EnvironmentData(p));
            MovementEvent.event(moveEvent, true);
            if (p.flyingTicks > 0) {
                --p.flyingTicks;
            }
            p.setTeleportTick(false);
        });
    }

    public static enum tpFlags {
        X,
        Y,
        Z,
        Y_ROT,
        X_ROT;
    }
}

