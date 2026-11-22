/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  es.pollitoyeye.vehicles.enums.VehicleType
 *  es.pollitoyeye.vehicles.events.VehicleEnterEvent
 *  es.pollitoyeye.vehicles.events.VehicleExitEvent
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 */
package ai.idealistic.spartan.compatibility.manual.entity;

import ai.idealistic.spartan.abstraction.data.Cooldowns;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.java.OverflowMap;
import es.pollitoyeye.vehicles.enums.VehicleType;
import es.pollitoyeye.vehicles.events.VehicleEnterEvent;
import es.pollitoyeye.vehicles.events.VehicleExitEvent;
import java.util.LinkedHashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class VehicleEnterListener
implements Listener {
    private static final Cooldowns cooldowns = new Cooldowns(new OverflowMap<String, Long>(new LinkedHashMap(), 512));
    private static final String key = (Object)Compatibility.CompatibilityType.VEHICLES + "=compatibility=";
    public static final String DRILL = "drill";
    public static final String TRACTOR = "tractor";
    private static final String[] types = new String[]{"drill", "tractor"};

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void Enter(VehicleEnterEvent e) {
        if (Compatibility.CompatibilityType.VEHICLES.isFunctional()) {
            PlayerProtocol p = PluginBase.getProtocol(e.getPlayer());
            VehicleType vehicleType = e.getVehicleType();
            if (vehicleType == VehicleType.DRILL) {
                VehicleEnterListener.add(p, DRILL);
            } else if (vehicleType == VehicleType.TRACTOR) {
                VehicleEnterListener.add(p, TRACTOR);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void Exit(VehicleExitEvent e) {
        if (Compatibility.CompatibilityType.VEHICLES.isFunctional()) {
            PlayerProtocol p = PluginBase.getProtocol(e.getPlayer());
            for (String type : types) {
                cooldowns.remove(VehicleEnterListener.key(p, type));
            }
        }
    }

    private static String key(PlayerProtocol p, String type) {
        return p.getUUID() + "=" + key + type;
    }

    private static void add(PlayerProtocol p, String type) {
        cooldowns.add(VehicleEnterListener.key(p, type), 20);
    }

    public static boolean has(PlayerProtocol p, String type) {
        return !cooldowns.canDo(VehicleEnterListener.key(p, type));
    }

    public static boolean has(PlayerProtocol p, String[] types) {
        for (String type : types) {
            if (!VehicleEnterListener.has(p, type)) continue;
            return true;
        }
        return false;
    }
}

