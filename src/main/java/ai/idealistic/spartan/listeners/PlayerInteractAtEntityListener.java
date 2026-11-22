/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerInteractAtEntityEvent
 */
package ai.idealistic.spartan.listeners;

import ai.idealistic.spartan.abstraction.entity.Equipment;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.Permissions;
import ai.idealistic.spartan.functionality.server.PluginBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractAtEntityListener
implements Listener {
    public static final boolean supported = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8);
    private static final List<Equipment> list = Collections.synchronizedList(new ArrayList());

    public static void clear() {
        if (!list.isEmpty()) {
            List<Equipment> list = PlayerInteractAtEntityListener.list;
            synchronized (list) {
                for (Equipment npc : PlayerInteractAtEntityListener.list) {
                    npc.remove();
                }
                PlayerInteractAtEntityListener.list.clear();
            }
        }
    }

    public static void clear(World world) {
        if (!list.isEmpty()) {
            List<Equipment> list = PlayerInteractAtEntityListener.list;
            synchronized (list) {
                for (Equipment npc : PlayerInteractAtEntityListener.list) {
                    if (!world.equals((Object)npc.armorStand.getWorld())) continue;
                    npc.remove();
                }
                PlayerInteractAtEntityListener.list.clear();
            }
        }
    }

    public static void create(PlayerProtocol protocol) {
        if (supported) {
            Location location = protocol.getLocationOrVehicle();
            if (!list.isEmpty()) {
                List<Equipment> list = PlayerInteractAtEntityListener.list;
                synchronized (list) {
                    for (Equipment npc : PlayerInteractAtEntityListener.list) {
                        if (!(ServerLocation.distance(npc.location, location) <= 16.0)) continue;
                        protocol.teleport(npc.location);
                        return;
                    }
                    PlayerInteractAtEntityListener.list.add(new Equipment(location));
                }
            }
            List<Equipment> list = PlayerInteractAtEntityListener.list;
            synchronized (list) {
                PlayerInteractAtEntityListener.list.add(new Equipment(location));
            }
        }
    }

    @EventHandler
    public void Interact(PlayerInteractAtEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (entity instanceof ArmorStand) {
            UUID uuid = entity.getUniqueId();
            if (!list.isEmpty()) {
                List<Equipment> list = PlayerInteractAtEntityListener.list;
                synchronized (list) {
                    for (Equipment npc : PlayerInteractAtEntityListener.list) {
                        if (!npc.getUniqueId().equals(uuid)) continue;
                        e.setCancelled(true);
                        npc.updateHead();
                        PlayerProtocol protocol = PluginBase.getProtocol(e.getPlayer());
                        PluginBase.playerInfo.open(protocol, protocol.bukkit().getName());
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void Damage(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof ArmorStand) {
            UUID uuid = entity.getUniqueId();
            if (!list.isEmpty()) {
                List<Equipment> list = PlayerInteractAtEntityListener.list;
                synchronized (list) {
                    for (Equipment npc : PlayerInteractAtEntityListener.list) {
                        if (!npc.getUniqueId().equals(uuid)) continue;
                        e.setCancelled(true);
                        npc.updateHead();
                        Entity damager = e.getDamager();
                        if (!(damager instanceof Player)) break;
                        PlayerProtocol protocol = PluginBase.getProtocol((Player)damager);
                        PluginBase.playerInfo.open(protocol, protocol.bukkit().getName());
                        break;
                    }
                }
            }
        }
    }

    static {
        PluginBase.runRepeatingTask(() -> {
            if (!list.isEmpty()) {
                List<PlayerProtocol> staff = Permissions.getStaff();
                if (staff.isEmpty()) {
                    PlayerInteractAtEntityListener.clear();
                } else {
                    List<Equipment> list = list;
                    synchronized (list) {
                        Iterator<Equipment> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            Equipment npc = iterator.next();
                            boolean animate = false;
                            for (PlayerProtocol player : staff) {
                                if (!(ServerLocation.distance(npc.location, player.getLocationOrVehicle()) <= 16.0)) continue;
                                animate = true;
                                break;
                            }
                            if (animate) {
                                if (npc.animate(staff)) continue;
                                iterator.remove();
                                continue;
                            }
                            npc.remove();
                            iterator.remove();
                        }
                    }
                }
            }
        }, 1L, 1L);
    }
}

