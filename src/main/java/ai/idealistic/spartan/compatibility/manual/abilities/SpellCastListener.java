/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.nisovin.magicspells.events.SpellCastEvent
 *  com.nisovin.magicspells.events.SpellCastedEvent
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 */
package ai.idealistic.spartan.compatibility.manual.abilities;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import com.nisovin.magicspells.events.SpellCastEvent;
import com.nisovin.magicspells.events.SpellCastedEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SpellCastListener
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void SpellCast(SpellCastEvent e) {
        LivingEntity caster;
        if (Compatibility.CompatibilityType.MAGIC_SPELLS.isFunctional() && (caster = e.getCaster()) instanceof Player) {
            Config.compatibility.evadeFalsePositives(PluginBase.getProtocol((Player)caster), Compatibility.CompatibilityType.MAGIC_SPELLS, new CheckEnums.HackCategoryType[]{CheckEnums.HackCategoryType.MOVEMENT, CheckEnums.HackCategoryType.COMBAT}, 40);
        }
    }

    @EventHandler
    private void SpellCasted(SpellCastedEvent e) {
        LivingEntity caster;
        if (Compatibility.CompatibilityType.MAGIC_SPELLS.isFunctional() && (caster = e.getCaster()) instanceof Player) {
            Config.compatibility.evadeFalsePositives(PluginBase.getProtocol((Player)caster), Compatibility.CompatibilityType.MAGIC_SPELLS, new CheckEnums.HackCategoryType[]{CheckEnums.HackCategoryType.MOVEMENT, CheckEnums.HackCategoryType.COMBAT}, 40);
        }
    }
}

