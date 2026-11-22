/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  io.papermc.paper.threadedregions.scheduler.ScheduledTask
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package ai.idealistic.spartan.functionality.server;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

class ServerSchedulers {
    ServerSchedulers() {
    }

    static void transfer(Runnable runnable) {
        if (Register.isPluginEnabled()) {
            if (!MultiVersion.folia) {
                Bukkit.getScheduler().runTask(Register.plugin, runnable);
            } else {
                Bukkit.getGlobalRegionScheduler().run(Register.plugin, consumer -> runnable.run());
            }
        }
    }

    static void run(PlayerProtocol protocol, Runnable runnable, boolean sync) {
        if (!MultiVersion.folia) {
            if (sync && !Bukkit.isPrimaryThread()) {
                if (Register.isPluginEnabled()) {
                    Bukkit.getScheduler().runTask(Register.plugin, runnable);
                }
            } else {
                runnable.run();
            }
        } else if (protocol != null) {
            if (Register.isPluginEnabled()) {
                Location location = protocol.getLocation();
                Bukkit.getRegionScheduler().execute(Register.plugin, location.getWorld(), ServerLocation.getChunkPos(location.getBlockX()), ServerLocation.getChunkPos(location.getBlockZ()), runnable);
            }
        } else {
            Bukkit.getGlobalRegionScheduler().run(Register.plugin, consumer -> runnable.run());
        }
    }

    static void run(World world, int x, int z, Runnable runnable, boolean sync) {
        if (!MultiVersion.folia) {
            if (sync && !Bukkit.isPrimaryThread()) {
                if (Register.isPluginEnabled()) {
                    Bukkit.getScheduler().runTask(Register.plugin, runnable);
                }
            } else {
                runnable.run();
            }
        } else if (Register.isPluginEnabled()) {
            Bukkit.getRegionScheduler().execute(Register.plugin, world, x, z, runnable);
        }
    }

    static Object schedule(PlayerProtocol protocol, Runnable runnable, long start, long repetition) {
        if (Register.isPluginEnabled()) {
            if (!MultiVersion.folia) {
                if (repetition == -1L) {
                    return Bukkit.getScheduler().scheduleSyncDelayedTask(Register.plugin, runnable, start);
                }
                return Bukkit.getScheduler().scheduleSyncRepeatingTask(Register.plugin, runnable, start, repetition);
            }
            if (repetition == -1L) {
                if (protocol != null) {
                    Location location = protocol.getLocation();
                    return Bukkit.getRegionScheduler().runDelayed(Register.plugin, location.getWorld(), ServerLocation.getChunkPos(location.getBlockX()), ServerLocation.getChunkPos(location.getBlockZ()), consumer -> runnable.run(), start);
                }
                return Bukkit.getGlobalRegionScheduler().runDelayed(Register.plugin, consumer -> runnable.run(), start);
            }
            if (protocol != null) {
                Location location = protocol.getLocation();
                return Bukkit.getRegionScheduler().runAtFixedRate(Register.plugin, location.getWorld(), ServerLocation.getChunkPos(location.getBlockX()), ServerLocation.getChunkPos(location.getBlockZ()), consumer -> runnable.run(), start, repetition);
            }
            return Bukkit.getGlobalRegionScheduler().runAtFixedRate(Register.plugin, consumer -> runnable.run(), start, repetition);
        }
        return null;
    }

    static void cancel(Object task) {
        if (!MultiVersion.folia) {
            if (task instanceof Integer) {
                Bukkit.getScheduler().cancelTask(((Integer)task).(int)Value());
            }
        } else if (task instanceof ScheduledTask) {
            ((ScheduledTask)task).cancel();
        }
    }
}

