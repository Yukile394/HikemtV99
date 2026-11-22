/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package ai.idealistic.spartan.abstraction.check.implementation.world;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.event.CBlockPlaceEvent;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.manual.abilities.TerraformBlockBreakListener;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.compatibility.manual.building.MineBomb;
import ai.idealistic.spartan.compatibility.manual.building.PrinterMode;
import ai.idealistic.spartan.compatibility.manual.building.TreeFeller;
import ai.idealistic.spartan.compatibility.manual.vanilla.Attributes;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockReach
extends CheckRunner {
    private final CheckDetection cv = new ImplementedDetection(this, null, null, "breaking", true);
    private final CheckDetection bX = new ImplementedDetection(this, null, null, "interact", true);
    private final CheckDetection el = new ImplementedDetection(this, null, null, "place", true);
    private final CheckDetection em = new ImplementedDetection(this, null, null, "raytrace", true);

    public BlockReach(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    private boolean handleCheck(Block b) {
        return this.protocol.getWorld().equals((Object)b.getWorld());
    }

    private double getBlockReach() {
        double limit = 7.0;
        return Math.max(this.hackType.getCheck().handleCheck("overall_distance", limit), limit);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof BlockBreakEvent) {
            this.cv.call(() -> {
                double limit;
                double distance;
                BlockBreakEvent event = (BlockBreakEvent)object;
                Block block = event.getBlock();
                if (this.handleCheck(block) && !ItemsAdder.is(block) && !TreeFeller.canCancel(block) && (distance = ServerLocation.distance(this.protocol.getLocationOrVehicle(), block.getLocation())) >= (limit = this.getBlockReach())) {
                    this.cv.cancel("breaking, distance: " + distance + ", limit: " + limit + ", block: " + BlockUtils.blockToString(block));
                    if (this.cv.prevent()) {
                        event.setCancelled(true);
                    }
                }
            });
        } else if (object instanceof PlayerInteractEvent) {
            this.bX.call(() -> {
                double limit;
                double distance;
                Block block = ((PlayerInteractEvent)object).getClickedBlock();
                if (block != null && this.handleCheck(block) && !ItemsAdder.is(block) && BlockUtils.isChangeable(block.getType()) && (distance = ServerLocation.distance(this.protocol.getLocationOrVehicle(), block.getLocation())) >= (limit = this.getBlockReach())) {
                    this.bX.cancel(distance / limit, "interact, distance: " + distance + ", limit: " + limit + ", block: " + BlockUtils.blockToString(block));
                }
            });
        } else if (object instanceof CBlockPlaceEvent && (this.el.canCall() || this.em.canCall())) {
            CBlockPlaceEvent event = (CBlockPlaceEvent)object;
            if (BlockUtils.isSolid(event.placedAgainstBlock.getType()) && this.handleCheck(event.placedBlock) && !ItemsAdder.is(event.placedBlock) && BlockUtils.isSolid(event.placedBlock.getType()) && !BlockUtils.isScaffoldingBlock(event.placedBlock.getType())) {
                Location to = this.protocol.getLocationOrVehicle().clone().add(0.0, this.protocol.getEyeHeight(), 0.0);
                double distance = ServerLocation.distance(to, event.placedBlock.getLocation());
                double ab_distance = ServerLocation.distance(to, event.placedAgainstBlock.getLocation());
                if (!this.protocol.isBedrockPlayer() && BlockUtils.isFullSolid(event.placedBlock.getType()) && (!MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_8) || event.placedBlock.getType() != Material.BARRIER) && this.protocol.getVehicle() == null && distance > ab_distance.3) {
                    this.em.call(() -> this.em.cancel(distance / (ab_distance.3), "raytrace, distance: " + distance + ", block-against-distance: " + ab_distance + ", block: " + BlockUtils.blockToString(event.placedBlock)));
                } else {
                    this.el.call(() -> {
                        double limit = this.getBlockReach();
                        if (distance >= limit) {
                            this.el.cancel(distance / limit, "place, distance: " + distance + ", limit: " + limit + ", block: " + BlockUtils.blockToString(event.placedBlock));
                        }
                    });
                }
            }
        }
    }

    @Override
    protected boolean canRun() {
        return !Compatibility.CompatibilityType.MC_MMO.isFunctional() && !Compatibility.CompatibilityType.CRAFT_BOOK.isFunctional() && !PrinterMode.isUsing(this.protocol) && !TerraformBlockBreakListener.isUsing(this.protocol) && !MineBomb.isUsing(this.protocol) && Attributes.getAmount(this.protocol, "PLAYER_BLOCK_INTERACTION_RANGE") == Double.MIN_VALUE;
    }
}

