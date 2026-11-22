/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Difficulty
 *  org.bukkit.Material
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.potion.PotionEffectType
 */
package ai.idealistic.spartan.abstraction.check.implementation.misc;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.manual.vanilla.Attributes;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

public class FastEat
extends CheckRunner {
    private final CheckDetection bX = new ImplementedDetection(this, null, null, "interact", true);
    private final CheckDetection bY = new ImplementedDetection(this, null, null, "eat", true);
    private static final Material cake = Spade.get("cake");
    private static final long delay = 1000L;
    private static final long bZ = 550L;
    private long ca;
    private long cb;

    public FastEat(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    private long getDelay() {
        if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9)) {
            PlayerInventory inventory = this.protocol.getInventory();
            for (ItemStack itemStack : new ItemStack[]{inventory.getItemInHand(), inventory.getItemInOffHand()}) {
                if (itemStack == null) continue;
                Material type = itemStack.getType();
                if (MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_13) && type == Material.DRIED_KELP) {
                    return 550L;
                }
                if (!type.isEdible()) continue;
                return 1000L;
            }
        } else {
            Material type;
            ItemStack itemStack = this.protocol.getItemInHand();
            if (itemStack != null && (type = itemStack.getType()).isEdible()) {
                return 1000L;
            }
        }
        return 0L;
    }

    private void I() {
        this.bY.call(() -> {
            long time = System.currentTimeMillis() - this.ca;
            this.ca = System.currentTimeMillis();
            long delay = this.getDelay();
            if (delay > 0L && time <= delay) {
                this.bY.cancel("eat, ms: " + time);
            }
        });
    }

    private void J() {
        this.bX.call(() -> {
            long time = System.currentTimeMillis() - this.cb;
            long delay = this.getDelay();
            if (delay > 0L && time <= delay) {
                this.bX.cancel("interact, ms: " + time + ", delay: " + delay);
            }
        });
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof FoodLevelChangeEvent) {
            FoodLevelChangeEvent e = (FoodLevelChangeEvent)object;
            int lvl = e.getFoodLevel();
            if (lvl > this.protocol.bukkit().getFoodLevel()) {
                this.I();
                this.J();
                if (this.prevent()) {
                    e.setCancelled(true);
                }
            }
        } else if (object instanceof PlayerInteractEvent) {
            PlayerInteractEvent e = (PlayerInteractEvent)object;
            switch (e.getAction()) {
                case RIGHT_CLICK_BLOCK: {
                    if (e.getClickedBlock().getType() != cake) break;
                    this.protocol.getRunner(this.hackType).addDisableCause(null, null, 10);
                    break;
                }
                case RIGHT_CLICK_AIR: {
                    if (!this.protocol.getItemInHand().getType().isEdible()) break;
                    this.cb = System.currentTimeMillis();
                }
            }
        }
    }

    @Override
    protected boolean canRun() {
        return this.protocol.getWorld().getDifficulty() != Difficulty.PEACEFUL && !this.protocol.hasPotionEffect(PotionEffectType.SATURATION, 0L) && Attributes.getAmount(this.protocol, "GENERIC_MAX_ABSORPTION") == Double.MIN_VALUE;
    }
}

