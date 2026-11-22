/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.World$Environment
 *  org.bukkit.event.block.BlockBreakEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.world;

import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.profiling.MiningHistory;
import ai.idealistic.spartan.abstraction.profiling.PlayerProfile;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.functionality.tracking.ResearchEngine;
import ai.idealistic.spartan.utils.math.Distance;
import ai.idealistic.spartan.utils.math.statistics.Expander;
import java.util.Collection;
import org.bukkit.World;
import org.bukkit.event.block.BlockBreakEvent;

public class XRay
extends CheckRunner {
    private static final double eu = 0.9999;
    private final ImplementedDetection[] ev = new ImplementedDetection[World.Environment.values().length];

    private static double[] handleCheck(World.Environment environment, MiningHistory.MiningOre ore) {
        double minesToTimeRatio;
        Collection<PlayerProfile> profiles = ResearchEngine.getPlayerProfiles();
        int factorRequirement = 10;
        if (profiles.size() < factorRequirement) {
            return new double[0];
        }
        int profileCount = 0;
        double mean = 0.0;
        double variance = 0.0;
        for (PlayerProfile profile : profiles) {
            minesToTimeRatio = profile.getMiningHistory(ore).getMinesToTimeRatio(environment);
            if (!(minesToTimeRatio > 0.0)) continue;
            ++profileCount;
            mean += minesToTimeRatio;
        }
        mean /= (double)profileCount;
        for (PlayerProfile profile : profiles) {
            minesToTimeRatio = profile.getMiningHistory(ore).getMinesToTimeRatio(environment);
            if (!(minesToTimeRatio > 0.0)) continue;
            double difference = minesToTimeRatio - mean;
            variance += difference * difference;
        }
        double stdDev = Math.sqrt(variance / (double)profileCount);
        if (profileCount >= factorRequirement) {
            return new double[]{mean / (double)profileCount, Math.sqrt(stdDev / (double)profileCount)};
        }
        return new double[0];
    }

    public XRay(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
        for (World.Environment environment : World.Environment.values()) {
            this.ev[environment.ordinal()] = new ImplementedDetection(this, null, null, environment.name().toLowerCase(), true, 1L, 1L, 1L, 1L);
        }
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof Object[]) {
            Object[] objects = (Object[])object;
            World.Environment environment = World.Environment.valueOf((String)objects[0].toString());
            ImplementedDetection detection = this.ev[environment.ordinal()];
            detection.call(() -> {
                double probability;
                double individual;
                double[] statistics = XRay.handleCheck(environment, (MiningHistory.MiningOre)objects[2]);
                if (statistics.length > 0 && (individual = (MiningHistoryobjects[1]).getMinesToTimeRatio(environment)) > 0.0 && (probability = Expander.getCumulativeProbability((individual - statistics[0]) / statistics[1])) >= 0.9999) {
                    detection.cancel("Unfair gathering of " + objects[3] + " in " + environment.name().toLowerCase() + " world, surpassed " + Distance.computeAlgebra(probability * 100.0, 2.0) + "% of players");
                }
            });
        } else if (object instanceof BlockBreakEvent) {
            BlockBreakEvent event = (BlockBreakEvent)object;
            if (this.prevent()) {
                event.setCancelled(true);
            }
        }
    }
}

