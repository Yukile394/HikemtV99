/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package ai.idealistic.spartan;

import ai.idealistic.spartan.functionality.command.CommandExecution;
import ai.idealistic.spartan.functionality.command.CommandTab;
import ai.idealistic.spartan.functionality.connection.JarVerification;
import ai.idealistic.spartan.functionality.moderation.AwarenessNotifications;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.PlayerInteractAtEntityListener;
import ai.idealistic.spartan.listeners.bukkit.CombatEvent;
import ai.idealistic.spartan.listeners.bukkit.DeathEvent;
import ai.idealistic.spartan.listeners.bukkit.MovementEvent;
import ai.idealistic.spartan.listeners.bukkit.PlaceEvent;
import ai.idealistic.spartan.listeners.bukkit.TeleportEvent;
import ai.idealistic.spartan.listeners.bukkit.TridentEvent;
import ai.idealistic.spartan.listeners.bukkit.VehicleEvent;
import ai.idealistic.spartan.listeners.bukkit.VelocityEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.BowEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.ChatEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.ChunksEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.ElytraEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.HealthEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.InventoryEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.JoinEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.LeaveEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.PluginEvent;
import ai.idealistic.spartan.listeners.bukkit.standalone.EntityDeathListener;
import ai.idealistic.spartan.listeners.bukkit.standalone.WorldEvent;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import ai.idealistic.spartan.utils.minecraft.server.ProxyUtils;
import java.util.LinkedHashSet;
import java.util.Set;
import kireiko.dev.millennium.ml.Model;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Register
extends JavaPlugin {
    public static Plugin plugin = null;
    public static final String mainPackage = "ai.idealistic.spartan";
    public static final String pluginName = "Spartan";
    public static final String command = "spartan";
    private static final Set<Class<?>> listeners = new LinkedHashSet(2);

    public void onEnable() {
        plugin = this;
        if (MultiVersion.serverVersion == MultiVersion.MCVersion.OTHER) {
            AwarenessNotifications.forcefullySend("The server's version or type is not supported. Please contact the plugin's developer if you think this is an error.");
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        Model.run();
        if (!MultiVersion.folia) {
            JarVerification.run((Plugin)this);
        }
        Config.create();
        Config.settings.runOnLogin();
        Register.enable(new JoinEvent());
        Register.enable(new LeaveEvent());
        Register.enable(new ChatEvent());
        Register.enable(new PluginEvent());
        Register.enable(new InventoryEvent());
        Register.enable(new BowEvent());
        Register.enable(new ChunksEvent());
        Register.enable(new EntityDeathListener());
        Register.enable(new HealthEvent());
        Register.enable(new CombatEvent());
        Register.enable(new PlaceEvent());
        Register.enable(new MovementEvent());
        Register.enable(new TeleportEvent());
        Register.enable(new DeathEvent());
        Register.enable(new VehicleEvent());
        Register.enable(new VelocityEvent());
        Register.enable(new WorldEvent());
        if (PlayerInteractAtEntityListener.supported) {
            Register.enable(new PlayerInteractAtEntityListener());
        }
        if (PlayerUtils.elytra) {
            Register.enable(new ElytraEvent());
            if (PlayerUtils.trident) {
                Register.enable(new TridentEvent());
            }
        }
        ProxyUtils.register();
        this.getCommand(command).setExecutor((CommandExecutor)new CommandExecution());
        this.getCommand(command).setTabCompleter((TabCompleter)new CommandTab());
    }

    public void onDisable() {
        plugin = this;
        ProxyUtils.unregister();
        listeners.clear();
        PluginBase.disable();
    }

    public static void enable(Listener l) {
        if (Register.isPluginEnabled() && listeners.add(l.getClass())) {
            Bukkit.getPluginManager().registerEvents(l, plugin);
        }
    }

    public static boolean isPluginEnabled() {
        return plugin != null && plugin.isEnabled();
    }
}

