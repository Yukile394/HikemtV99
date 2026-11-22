/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerAnimationEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.misc;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.event.PlayerAttackEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.manual.abilities.TerraformBlockBreakListener;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.compatibility.manual.building.MineBomb;
import ai.idealistic.spartan.compatibility.manual.building.TreeFeller;
import ai.idealistic.spartan.compatibility.manual.entity.VehicleEnterListener;
import ai.idealistic.spartan.functionality.tracking.CheckConditions;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class NoSwing
extends CheckRunner {
    private final CheckDetection cv;
    private final CheckDetection cw;
    private static final int cx = 600;
    private long cy = Long.MAX_VALUE;
    private long cz = Long.MAX_VALUE;
    private final Buffer.Storage cA = new Buffer.Storage();
    private long cB;

    public NoSwing(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
        this.cv = new ImplementedDetection(this, null, null, "breaking", true);
        this.cw = new ImplementedDetection(this, null, null, "damage", true);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof PlayerInteractEvent) {
            Block block;
            PlayerInteractEvent e = (PlayerInteractEvent)object;
            if (!(e.getAction() != Action.LEFT_CLICK_BLOCK || (block = e.getClickedBlock()) != null && ItemsAdder.is(block))) {
                this.cy = System.currentTimeMillis();
            }
        } else if (object instanceof PlayerAnimationEvent) {
            this.cz = System.currentTimeMillis();
        } else if (object instanceof BlockBreakEvent) {
            this.cv.call(() -> {
                if (MineBomb.isUsing(this.protocol) || TerraformBlockBreakListener.isUsing(this.protocol)) {
                    return;
                }
                BlockBreakEvent e = (BlockBreakEvent)object;
                Block block = e.getBlock();
                if (!(BlockUtils.isSensitive(this.protocol, block.getType()) || TreeFeller.canCancel(block) || ItemsAdder.is(block) || System.currentTimeMillis() - this.cy <= 50L)) {
                    if (this.cz == Long.MAX_VALUE) {
                        this.cv.cancel("breaking, block: " + BlockUtils.blockToString(block) + ", item: " + BlockUtils.materialToString(this.protocol.getItemInHand().getType()));
                        if (this.cv.prevent()) {
                            e.setCancelled(true);
                        }
                    } else {
                        this.Q();
                    }
                }
            });
        } else if (object instanceof PlayerAttackEvent) {
            this.cw.call(() -> {
                if (this.protocol.getAttackCooldown() != 1.0f) {
                    return;
                }
                LivingEntity entity = ((PlayerAttackEvent)object).target;
                if (!CheckConditions.canCheckCombat(this.protocol, entity)) {
                    return;
                }
                long time = System.currentTimeMillis();
                if (this.cB < time) {
                    this.cB = time + 1000L;
                    if (this.cz == Long.MAX_VALUE) {
                        if (this.cA.count(1.0, 600) >= 3.0) {
                            this.cA.reset();
                            this.cw.cancel("damage, entity: " + NoSwing.handleDataType((Entity)entity) + ", item: " + BlockUtils.materialToString(this.protocol.getItemInHand().getType()));
                        }
                    } else {
                        this.Q();
                    }
                }
            });
        }
    }

    private void Q() {
        this.cz = Long.MAX_VALUE;
    }

    @Override
    public boolean canRun() {
        return !VehicleEnterListener.has(this.protocol, "drill") && !Compatibility.CompatibilityType.ADVANCED_ENCHANTMENTS.isFunctional();
    }

    private static String handleDataType(Entity e) {
        return e.getType().toString().toLowerCase().replace("_", "-");
    }
}

