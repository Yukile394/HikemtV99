/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.compatibility.manual.building.PrinterMode;
import ai.idealistic.spartan.compatibility.manual.building.TreeFeller;
import ai.idealistic.spartan.compatibility.manual.entity.VehicleEnterListener;
import ai.idealistic.spartan.compatibility.manual.vanilla.Attributes;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GhostHand
extends CheckDetection {
    private final CheckDetection cv;
    private final CheckDetection bX;

    public GhostHand(CheckRunner executor) {
        super(executor, null, null, null, null);
        this.cv = new ImplementedDetection(executor, null, null, "breaking", true);
        this.bX = new ImplementedDetection(executor, null, null, "interact", true);
    }

    void handleCheck(BlockBreakEvent event) {
        this.cv.call(() -> {
            Block b;
            if (!Compatibility.CompatibilityType.ADVANCED_ENCHANTMENTS.isFunctional() && this.handleCheck(b = event.getBlock()) && !ItemsAdder.is(b)) {
                double tDistance;
                long breakTime = Spade.getBlockBreakTime(this.protocol, this.protocol.getItemInHand(), b.getType());
                if (breakTime <= 500L) {
                    return;
                }
                Location location = this.protocol.getLocationOrVehicle().clone().add(0.0, this.protocol.getEyeHeight(), 0.0);
                double bDistance = ServerLocation.distance(location, b.getLocation());
                ServerLocation targetBlock =  this.protocol.getTargetBlock(bDistance, 16.0);
                if (t != null && (tDistance = t.distance(location)) < bDistance) {
                    this.cv.cancel("breaking, interacted-block: " + BlockUtils.blockToString(b) + ", blocking-block: " + BlockUtils.blockToString(t.getBlock(this.protocol)) + ", interacted-distance: " + bDistance + ", blocking-distance: " + tDistance, null, 1);
                    if (this.cv.prevent()) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }

    void handleCheck(PlayerInteractEvent event) {
        this.bX.call(() -> {
            double tDistance;
            Location location;
            double bDistance;
            ServerLocation t;
            Block b;
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (b = event.getClickedBlock()) != null && this.handleCheck(b) && BlockUtils.isChangeable(b.getType()) && (t = this.protocol.getTargetBlock(bDistance = ServerLocation.distance(location = this.protocol.getLocationOrVehicle().clone().add(0.0, this.protocol.getEyeHeight(), 0.0), b.getLocation()), 16.0)) != null && (tDistance = t.distance(location)) < bDistance) {
                this.bX.cancel("interact, interacted-block: " + BlockUtils.blockToString(b) + ", blocking-block: " + BlockUtils.blockToString(t.getBlock(this.protocol)) + ", interacted-distance: " + bDistance + ", blocking-distance: " + tDistance, null, 1);
                if (this.bX.prevent()) {
                    event.setCancelled(true);
                }
            }
        });
    }

    private boolean handleCheck(Block b) {
        return b.getWorld() == this.protocol.getWorld() && !TreeFeller.canCancel(b);
    }

    private boolean canRun() {
        if (!(this.protocol.isFlying() || Compatibility.CompatibilityType.MC_MMO.isFunctional() || VehicleEnterListener.has(this.protocol, "drill") || PrinterMode.isUsing(this.protocol) || Attributes.getAmount(this.protocol, "PLAYER_BLOCK_INTERACTION_RANGE") != Double.MIN_VALUE)) {
            GameMode gameMode = this.protocol.getGameMode();
            return gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE;
        }
        return false;
    }
}

