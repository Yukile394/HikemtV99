/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package ai.idealistic.spartan.listeners.bukkit;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.event.EntityAttackPlayerEvent;
import ai.idealistic.spartan.abstraction.event.PlayerAttackEvent;
import ai.idealistic.spartan.abstraction.event.PlayerUseEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.concurrent.CheckThread;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CombatEvent
implements Listener {
    private static final CheckEnums.HackType[] handledChecks = new CheckEnums.HackType[]{CheckEnums.HackType.KILL_AURA, CheckEnums.HackType.HIT_REACH, CheckEnums.HackType.NO_SWING, CheckEnums.HackType.CRITICALS, CheckEnums.HackType.FAST_CLICKS};

    @EventHandler(priority=EventPriority.HIGHEST)
    private void Event(EntityDamageByEntityEvent e) {
        CheckThread.run(() -> CombatEvent.event(e, false));
    }

    public static void event(EntityDamageByEntityEvent e, boolean packets) {
        boolean cancelled;
        Player player;
        PlayerProtocol protocol;
        boolean entityAttack;
        Entity damager = e.getDamager();
        Entity entity = e.getEntity();
        boolean damagerIsPlayer = damager instanceof Player;
        boolean entityIsPlayer = entity instanceof Player;
        entityAttack = e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK;
        if (damagerIsPlayer && (protocol = PluginBase.getProtocol(player = (Player)damager, true)).packetsEnabled() == packets && entityAttack && (entityIsPlayer || entity instanceof LivingEntity)) {
            cancelled = e.isCancelled();
            PlayerAttackEvent event = new PlayerAttackEvent(player, (LivingEntity)entity, cancelled);
            protocol.executeRunners(cancelled, event);
            for (CheckEnums.HackType hackType : handledChecks) {
                if (!protocol.getRunner(hackType).prevent()) continue;
                e.setCancelled(true);
            }
        }
        if (entityIsPlayer && (protocol = PluginBase.getProtocol(player = (Player)entity, true)).packetsEnabled() == packets && entityAttack && (damagerIsPlayer || damager instanceof LivingEntity)) {
            cancelled = e.isCancelled();
            protocol.executeRunners(cancelled, new EntityAttackPlayerEvent(player, (LivingEntity)damager, cancelled));
        }
    }

    public static void use(PlayerUseEvent e) {
        PlayerProtocol protocol = PluginBase.getProtocol(e.player, true);
        protocol.executeRunners(e.isCancelled(), new PlayerAttackEvent(e.player, e.target, e.isCancelled()));
    }
}

