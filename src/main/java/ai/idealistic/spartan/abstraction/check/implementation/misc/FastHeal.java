/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Difficulty
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.EntityRegainHealthEvent$RegainReason
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.potion.PotionEffectType
 */
package ai.idealistic.spartan.abstraction.check.implementation.misc;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.manual.vanilla.Attributes;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.minecraft.entity.PlayerUtils;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffectType;

public class FastHeal
extends CheckRunner {
    private final CheckDetection A = new ImplementedDetection(this, null, null, null, true);
    private double cd;
    private long time;
    private final Buffer.Storage aB = new Buffer.Storage();

    public FastHeal(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    private long M() {
        int level = PlayerUtils.getPotionLevel(this.protocol, PotionEffectType.REGENERATION);
        long i = 0L;
        if (level > -1) {
            if (level <= 10) {
                i = level == 0 ? 300L : 45L;
            }
        } else {
            i = !MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9) ? 3700L : 450L;
        }
        return i;
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof EntityRegainHealthEvent) {
            this.A.call(() -> {
                EntityRegainHealthEvent event = (EntityRegainHealthEvent)object;
                EntityRegainHealthEvent.RegainReason reason = event.getRegainReason();
                if (reason != EntityRegainHealthEvent.RegainReason.CUSTOM && reason != EntityRegainHealthEvent.RegainReason.MAGIC) {
                    long limit;
                    double health = this.protocol.getHealth();
                    long timePassed = System.currentTimeMillis() - this.time;
                    this.time = System.currentTimeMillis();
                    if (this.cd != health && (limit = this.M()) > 0L && timePassed < limit && this.aB.count(1.0, 10) == 3.0) {
                        this.aB.reset();
                        this.A.cancel("default, ms: " + timePassed + ", limit: " + limit);
                        if (this.prevent()) {
                            event.setCancelled(true);
                        }
                    }
                    this.cd = health;
                }
            });
        } else if (object instanceof FoodLevelChangeEvent && FoodLevelChangeEventobject.getFoodLevel() > this.protocol.bukkit().getFoodLevel() && (!MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) || this.protocol.getItemInHand().getType() != Material.DRIED_KELP)) {
            this.protocol.getRunner(this.hackType).addDisableCause(null, null, 10);
        }
    }

    @Override
    protected boolean canRun() {
        if (this.protocol.getWorld().getDifficulty() != Difficulty.PEACEFUL && this.protocol.bukkit().getFireTicks() <= 0 && Attributes.getAmount(this.protocol, "GENERIC_MAX_ABSORPTION") == Double.MIN_VALUE) {
            GameMode gameMode = this.protocol.getGameMode();
            return gameMode == GameMode.ADVENTURE || gameMode == GameMode.SURVIVAL;
        }
        return false;
    }
}

