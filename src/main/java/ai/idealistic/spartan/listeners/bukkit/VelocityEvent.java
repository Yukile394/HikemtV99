/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerVelocityEvent
 */
package ai.idealistic.spartan.listeners.bukkit;

import ai.idealistic.spartan.abstraction.event.CPlayerVelocityEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;

public class VelocityEvent
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST)
    private void Event(PlayerVelocityEvent e) {
        CheckThread.run(() -> VelocityEvent.event(new CPlayerVelocityEvent(e.getPlayer(), e.getVelocity()), false));
    }

    public static void event(CPlayerVelocityEvent e, boolean packets) {
        PlayerProtocol protocol = PluginBase.getProtocol(e.getPlayer());
        if (protocol.packetsEnabled() == packets) {
            protocol.claimedVelocity = e;
            protocol.claimedVeloGravity.add(e);
            if (protocol.claimedVeloGravity.size() > 2) {
                protocol.claimedVeloGravity.remove(0);
            }
            protocol.claimedVeloSpeed.add(e);
            if (protocol.claimedVeloSpeed.size() > 2) {
                protocol.claimedVeloSpeed.remove(0);
            }
            protocol.executeRunners(e.isCancelled(), e);
        }
    }
}

