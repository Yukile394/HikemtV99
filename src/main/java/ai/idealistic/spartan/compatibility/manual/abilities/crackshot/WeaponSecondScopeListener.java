/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  me.DeeCaaD.CrackShotPlus.Events.WeaponSecondScopeEvent
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 */
package ai.idealistic.spartan.compatibility.manual.abilities.crackshot;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.java.OverflowMap;
import java.util.LinkedHashMap;
import me.DeeCaaD.CrackShotPlus.Events.WeaponSecondScopeEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class WeaponSecondScopeListener
implements Listener {
    private static final Buffer buffers = new Buffer(new OverflowMap<String, Buffer.Storage>(new LinkedHashMap(), 512));

    @EventHandler(priority=EventPriority.HIGHEST)
    private void WeaponScope(WeaponSecondScopeEvent e) {
        if (Compatibility.CompatibilityType.CRACK_SHOT_PLUS.isFunctional()) {
            PlayerProtocol protocol = PluginBase.getProtocol(e.getPlayer());
            if (!e.isCancelled()) {
                Config.compatibility.evadeFalsePositives(protocol, Compatibility.CompatibilityType.CRACK_SHOT_PLUS, new CheckEnums.HackCategoryType[]{CheckEnums.HackCategoryType.MOVEMENT, CheckEnums.HackCategoryType.COMBAT}, 20);
                if (e.isZoomIn()) {
                    buffers.set(protocol.getUUID() + "=crackshotplus=compatibility=scope", 1);
                } else {
                    buffers.remove(protocol.getUUID() + "=crackshotplus=compatibility=scope");
                }
            } else {
                buffers.remove(protocol.getUUID() + "=crackshotplus=compatibility=scope");
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void EntityDamage(EntityDamageEvent e) {
        PlayerProtocol p;
        Entity entity;
        if (Compatibility.CompatibilityType.CRACK_SHOT_PLUS.isFunctional() && (entity = e.getEntity()) instanceof Player && WeaponSecondScopeListener.isUsingScope(p = PluginBase.getProtocol((Player)entity))) {
            Config.compatibility.evadeFalsePositives(p, Compatibility.CompatibilityType.CRACK_SHOT_PLUS, new CheckEnums.HackCategoryType[]{CheckEnums.HackCategoryType.MOVEMENT, CheckEnums.HackCategoryType.COMBAT}, 60);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void EntityByEntityDamage(EntityDamageByEntityEvent e) {
        PlayerProtocol p;
        Entity entity;
        if (Compatibility.CompatibilityType.CRACK_SHOT_PLUS.isFunctional() && (entity = e.getDamager()) instanceof Player && WeaponSecondScopeListener.isUsingScope(p = PluginBase.getProtocol((Player)entity))) {
            Config.compatibility.evadeFalsePositives(p, Compatibility.CompatibilityType.CRACK_SHOT_PLUS, new CheckEnums.HackCategoryType[]{CheckEnums.HackCategoryType.MOVEMENT, CheckEnums.HackCategoryType.COMBAT}, 30);
        }
    }

    static boolean isUsingScope(PlayerProtocol p) {
        return Compatibility.CompatibilityType.CRACK_SHOT_PLUS.isFunctional() && buffers.get(p.getUUID() + "=crackshotplus=compatibility=scope") != 0.0;
    }
}

