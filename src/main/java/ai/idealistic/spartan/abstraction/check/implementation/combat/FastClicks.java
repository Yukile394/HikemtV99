/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.check.implementation.combat;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.event.PlayerLeftClickEvent;
import ai.idealistic.spartan.abstraction.event.PlayerTickEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.utils.java.ConcurrentList;
import ai.idealistic.spartan.utils.math.statistics.Expander;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.NumberConversions;

public class FastClicks
extends CheckRunner {
    private final CheckDetection B;
    private final CheckDetection C;
    private long D;
    private final Buffer.Storage E;
    private final Buffer.Storage F;
    private long G = System.currentTimeMillis();
    private final List<Integer> H = new ConcurrentList<Integer>();
    private final CheckDetection I;
    private final CheckDetection J;
    private final CheckDetection K;
    private final CheckDetection L;
    private final CheckDetection M;
    private final CheckDetection N;
    private final List<Long> O;
    private final List<Long> P;

    public FastClicks(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
        for (i < 7; ++i) {
            this.H.add(0);
        }
        this.O = new ConcurrentList<Long>();
        this.P = new ConcurrentList<Long>();
        this.I = new ImplementedDetection(this, null, Check.DetectionType.PACKETS, "cps", true);
        this.J = new ImplementedDetection(this, null, Check.DetectionType.PACKETS, "floor_tempo", true, 2000L, 30000L, 40000L, 60000L);
        this.K = new ImplementedDetection(this, null, Check.DetectionType.PACKETS, "deviation", true, 2000L, 30000L, 40000L, 60000L);
        this.L = new ImplementedDetection(this, null, Check.DetectionType.PACKETS, "skewness", true, 2000L, 30000L, 40000L, 60000L);
        this.M = new ImplementedDetection(this, null, Check.DetectionType.PACKETS, "kurtosis", true, 2000L, 30000L, 40000L, 60000L);
        this.N = new ImplementedDetection(this, null, Check.DetectionType.PACKETS, "variance", true, 2000L, 30000L, 40000L, 60000L);
        this.B = new ImplementedDetection(this, null, null, "bow_force", true, 2000L, 30000L, 40000L, 60000L);
        this.C = new ImplementedDetection(this, null, null, "bow_shots", true, 2000L, 30000L, 40000L, 60000L);
        this.E = new Buffer.Storage();
        this.F = new Buffer.Storage();
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof BlockBreakEvent || object instanceof PlayerDropItemEvent) {
            this.addDisableCause(null, null, NumberConversions.ceil((double)20.0));
        } else if (object instanceof PlayerLeftClickEvent) {
            this.handleCheck(((PlayerLeftClickEvent)object).delay);
        } else if (object instanceof PlayerTickEvent) {
            this.tick((PlayerTickEvent)object);
        } else if (object instanceof PlayerInteractEvent) {
            PlayerInventory inventory;
            PlayerInteractEvent event = (PlayerInteractEvent)object;
            if (this.prevent()) {
                event.setCancelled(true);
            }
            if (event.getAction() == Action.RIGHT_CLICK_AIR && (inventory = this.protocol.getInventory()).getItemInHand().getType() == Material.BOW && inventory.contains(Material.ARROW)) {
                this.D = System.currentTimeMillis();
            }
        } else if (object instanceof EntityShootBowEvent) {
            EntityShootBowEvent event = (EntityShootBowEvent)object;
            if (this.protocol.getItemInHand().getType() == Material.BOW) {
                this.checkCheckCancellation(event);
                this.handleCheck(event);
            }
        }
    }

    private void handleCheck(EntityShootBowEvent event) {
        this.B.call(() -> {
            double threshold;
            if (event.getForce() != 1.0f) {
                return;
            }
            long timePassed = System.currentTimeMillis() - this.D;
            if (timePassed > 500L) {
                return;
            }
            double buffer = this.E.count(1.0, 20);
            if (buffer >= (threshold = 2.0)) {
                this.E.reset();
                this.B.cancel("bow-force, ms: " + timePassed);
                if (this.prevent()) {
                    event.setCancelled(true);
                }
            }
        });
    }

    private void checkCheckCancellation(EntityShootBowEvent event) {
        this.C.call(() -> {
            double threshold;
            double buffer = this.F.count(1.0, 10);
            if (buffer >= (threshold = 7.0)) {
                this.C.cancel("bow-shots");
                if (this.prevent()) {
                    event.setCancelled(true);
                }
            }
        });
    }

    private void handleCheck(long delay) {
        if (this.I.canCall() || this.J.canCall() || this.K.canCall() || this.L.canCall() || this.M.canCall() || this.N.canCall()) {
            if (!this.isFastClicks()) {
                return;
            }
            this.O.add(delay);
            this.P.add(delay);
            if (this.O.size() >= 20) {
                double floor;
                double cps = Expander.getCps(this.O) * 50.0;
                double limit = this.hackType.getCheck().handleCheck("clicks_per_second_limit", 15);
                double variance = Expander.getVariance(this.O);
                double kurtosis = Expander.getKurtosis(this.O);
                if (cps > limit) {
                    this.I.cancel("limit (" + (int)cps + "/" + (int)limit + ")");
                }
                if ((floor = Math.abs(Math.floor(cps) - cps)) < 0.07) {
                    if (this.handleCheck(1, 2) > 3) {
                        this.J.call(() -> this.J.cancel("floor tempo (" + floor + ")"));
                        this.handleCheck(1, -1);
                    }
                } else {
                    this.handleCheck(1, -1);
                }
                if (kurtosis < 45000.0) {
                    if (this.handleCheck(4, 3) > 6) {
                        this.M.call(() -> this.M.cancel("kurtosis (" + (int)kurtosis + ")"));
                        this.handleCheck(4, -1);
                    }
                } else {
                    this.handleCheck(4, -2);
                }
                if (variance < 1100.0 && variance > 200.0 && cps > 7.0) {
                    if (this.handleCheck(5, 3) > 6) {
                        this.N.call(() -> this.N.cancel("variance (" + (int)variance + ")"));
                        this.handleCheck(5, -1);
                    }
                } else {
                    this.handleCheck(5, -3);
                }
                this.O.clear();
            }
            if (this.P.size() >= 50) {
                double standardDev = Expander.getStandardDeviation(this.P);
                double skewness = Expander.getSkewness(this.P);
                if (standardDev < 50.0) {
                    if (this.handleCheck(2, 2) > 3) {
                        this.K.call(() -> this.K.cancel("low deviation (" + standardDev + ")"));
                        this.handleCheck(2, -1);
                    }
                } else {
                    this.handleCheck(2, -1);
                }
                if (skewness < -0.01) {
                    if (this.handleCheck(3, 2) > 3) {
                        this.L.call(() -> this.L.cancel("invalid skewness (" + skewness + ")"));
                        this.handleCheck(3, -1);
                    }
                } else {
                    this.handleCheck(3, -2);
                }
                this.P.clear();
            }
        }
    }

    private void tick(PlayerTickEvent event) {
        this.G = event.time;
    }

    private int handleCheck(int x, int y) {
        this.H.set(i, Math.max(this.H.get(i) + v, 0));
        return this.H.get(i);
    }

    private boolean isFastClicks() {
        return System.currentTimeMillis() - this.G < 70L;
    }

    @Override
    protected boolean canRun() {
        return !this.protocol.getNearbyEntities(6.0, 6.0, 6.0).isEmpty();
    }
}

