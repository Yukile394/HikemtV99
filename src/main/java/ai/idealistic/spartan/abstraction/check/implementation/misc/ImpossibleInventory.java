/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Material
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.check.implementation.misc;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.listeners.protocol.DeathListener;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import ai.idealistic.spartan.utils.minecraft.world.BlockPatternUtils;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Material;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class ImpossibleInventory
extends CheckRunner {
    private final CheckDetection ce = new ImplementedDetection(this, null, null, "auto_totem", true);
    private final CheckDetection cf = new ImplementedDetection(this, null, null, "closed_inventory", true);
    private final CheckDetection cg = new ImplementedDetection(this, null, null, "portal_inventory", true);
    private final CheckDetection ch = new ImplementedDetection(this, null, null, "cursor_usage", true);
    private static final Material ci = Spade.get("nether_portal");
    private static final Material cj = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_11) ? Material.getMaterial("TOTEM_OF_UNDYING") : null;
    private static final double[] ck = new double[]{1.0, -1.0, 1.0};
    private long cl;

    public ImpossibleInventory(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        block5: {
            block6: {
                block4: {
                    if (!(object instanceof PlayerDeathEvent)) break block4;
                    this.N();
                    break block5;
                }
                if (!(object instanceof InventoryClickEvent)) break block6;
                InventoryClickEvent e = (InventoryClickEvent)object;
                ItemStack item = e.getCurrentItem();
                this.handleCheck(e.getClick());
                this.checkCheckCancellation(item);
                this.handleCheck(item);
                if (!this.prevent()) break block5;
                e.setCancelled(true);
                break block5;
            }
            if (object instanceof PlayerMoveEvent) {
                this.ch.call(() -> {
                    int cases;
                    ItemStack cursor;
                    if (!this.protocol.isSwimming() && this.protocol.getVehicle() == null && BlockUtils.hasMaterial(cursor = this.protocol.bukkit().getOpenInventory().getCursor()) && (cases = this.O()) != 0) {
                        this.ch.cancel("cursor-usage, case: " + cases, this.protocol.getFromLocation(), 0, false);
                    }
                });
            } else if (PluginBase.packetsEnabled() && object instanceof PacketEvent) {
                PacketType eventType = ((PacketEvent)object).getPacketType();
                for (PacketType type : DeathListener.packetTypes) {
                    if (!type.equals(eventType)) continue;
                    this.N();
                    break;
                }
            }
        }
    }

    private void handleCheck(ItemStack itemStack) {
        this.ce.call(() -> {
            if (cj != null && itemStack != null && itemStack.getType() == cj) {
                this.cl = System.currentTimeMillis() + 150L;
            }
        });
    }

    private void N() {
        long ticks = this.cl - System.currentTimeMillis();
        if (ticks > 0L) {
            this.ce.cancel("auto-totem, remaining-ticks: " + ticks);
        }
    }

    private void handleCheck(ClickType click) {
        this.cf.call(() -> {
            int cases = this.O();
            if (!(cases == 0 || click.isCreativeAction() || click.isKeyboardClick() || this.protocol.isSwimming() || this.protocol.getVehicle() != null || this.protocol.bukkit().getOpenInventory().countSlots() <= 46 || BlockPatternUtils.isBlockPattern(ck, this.protocol.getLocationOrVehicle(), true, BlockUtils.ice, BlockUtils.blue_ice))) {
                this.cf.cancel("closed-inventory, case: " + cases);
            }
        });
    }

    private void checkCheckCancellation(ItemStack item) {
        this.cg.call(() -> {
            ServerLocation loc;
            if (BlockUtils.hasMaterial(item) && ((loc = new ServerLocation(this.protocol.getLocation())).getBlock(this.protocol).getType() == ci || loc.clone().add(0.0, 1.0, 0.0).getBlock(this.protocol).getType() == ci)) {
                this.cg.cancel("portal-inventory");
            }
        });
    }

    private int O() {
        if (this.protocol.isFlying() || this.protocol.isLowEyeHeight()) {
            return 0;
        }
        Check check = this.hackType.getCheck();
        return check.handleCheck("check_sneaking", true) && this.protocol.isSneaking() ? 1 : (!MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_15) && check.handleCheck("check_sprinting", true) && this.protocol.isSprinting() ? 2 : (check.handleCheck("check_sleeping", true) && this.protocol.bukkit().isSleeping() ? 3 : (check.handleCheck("check_dead", true) && this.protocol.bukkit().isDead() ? 5 : (check.handleCheck("check_sprint_jumping", true) && this.protocol.isSprinting() && this.protocol.isJumping(this.P()) ? 6 : (check.handleCheck("check_walk_jumping", true) && !this.protocol.isSprinting() && this.protocol.isJumping(this.P()) ? 7 : (check.handleCheck("check_jumping", true) && this.protocol.justJumped(this.P()) ? 8 : 0))))));
    }

    private double P() {
        return this.protocol.getLocation().getY() - this.protocol.getFromLocation().getY();
    }
}

