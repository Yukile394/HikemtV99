/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.util.NumberConversions
 *  wtf.choco.veinminer.api.event.player.PlayerClientActivateVeinMinerEvent
 *  wtf.choco.veinminer.api.event.player.PlayerVeinMineEvent
 */
package ai.idealistic.spartan.compatibility.manual.building;

import ai.idealistic.spartan.Register;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.functionality.server.Config;
import ai.idealistic.spartan.functionality.server.PluginBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.NumberConversions;
import wtf.choco.veinminer.api.event.player.PlayerClientActivateVeinMinerEvent;
import wtf.choco.veinminer.api.event.player.PlayerVeinMineEvent;

public class PlayerVeinMineListener
implements Listener {
    public static void reload() {
        Register.enable(new PlayerVeinMineListener());
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void Event(PlayerVeinMineEvent e) {
        if (Compatibility.CompatibilityType.VEIN_MINER.isFunctional()) {
            Config.compatibility.evadeFalsePositives(PluginBase.getProtocol(e.getPlayer()), Compatibility.CompatibilityType.VEIN_MINER, new CheckEnums.HackType[]{CheckEnums.HackType.NO_SWING, CheckEnums.HackType.FAST_BREAK, CheckEnums.HackType.IMPOSSIBLE_ACTIONS, CheckEnums.HackType.BLOCK_REACH, CheckEnums.HackType.FAST_PLACE}, NumberConversions.round((double)20.0));
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void Event(PlayerClientActivateVeinMinerEvent e) {
        if (Compatibility.CompatibilityType.VEIN_MINER.isFunctional()) {
            Config.compatibility.evadeFalsePositives(PluginBase.getProtocol(e.getPlayer()), Compatibility.CompatibilityType.VEIN_MINER, new CheckEnums.HackType[]{CheckEnums.HackType.NO_SWING, CheckEnums.HackType.FAST_BREAK, CheckEnums.HackType.IMPOSSIBLE_ACTIONS, CheckEnums.HackType.BLOCK_REACH, CheckEnums.HackType.FAST_PLACE}, NumberConversions.round((double)20.0));
        }
    }
}

