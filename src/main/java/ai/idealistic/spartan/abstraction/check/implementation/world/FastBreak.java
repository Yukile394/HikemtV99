/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.block.Block
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.util.NumberConversions
 */
package ai.idealistic.spartan.abstraction.check.implementation.world;

import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.compatibility.Compatibility;
import ai.idealistic.spartan.compatibility.manual.abilities.TerraformBlockBreakListener;
import ai.idealistic.spartan.compatibility.manual.abilities.ItemsAdder;
import ai.idealistic.spartan.compatibility.manual.building.MineBomb;
import ai.idealistic.spartan.compatibility.manual.building.SuperPickaxe;
import ai.idealistic.spartan.compatibility.manual.building.TreeFeller;
import ai.idealistic.spartan.compatibility.manual.entity.VehicleEnterListener;
import ai.idealistic.spartan.compatibility.manual.vanilla.Attributes;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.NumberConversions;

public class FastBreak
extends CheckRunner {
    private final CheckDetection en = new ImplementedDetection(this, null, null, "delay", true);
    private final CheckDetection eo = new ImplementedDetection(this, null, null, "breaks_per_second", null);
    private final CheckDetection ep = new ImplementedDetection(this, null, null, "indirect_surroundings_per_second", null);
    private Block eq = null;
    private final Map<Integer, Long> er = new ConcurrentHashMap<Integer, Long>2;
    private final Buffer.Storage es = new Buffer.Storage();
    private final Buffer.Storage et = new Buffer.Storage();

    public FastBreak(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    private void handleCheck(Block block, int seconds) {
        this.en.call(() -> {
            ItemStack itemStack;
            long originalLimit;
            long limit;
            long time;
            GameMode gameMode;
            if (!(BlockUtils.isSensitive(block.getType()) || (gameMode = this.protocol.getGameMode()) != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE || (time = this.er.getOrDefault(Objects.hash(block.getWorld().getName(), block.getType(), block.getX(), block.getY(), block.getZ()), 0L).(long)Value()) <= 0L || (time = System.currentTimeMillis() - time) <= 0L || (limit = (originalLimit = Spade.getBlockBreakTime(this.protocol, itemStack = this.protocol.getItemInHand(), block.getType()))) <= -1L)) {
                limit /= 2L;
                if (block.getY() <= this.protocol.getLocation().getBlockY() && (double)Math.abs(block.getX() - this.protocol.getLocation().getBlockX()) <= 1.5 && (double)Math.abs(block.getZ() - this.protocol.getLocation().getBlockZ()) <= 1.5) {
                    limit /= 2L;
                }
                if (limit > 100L && time < limit) {
                    String name;
                    boolean emptyItem = itemStack == null;
                    Collection<PotionEffect> potionEffects = this.protocol.getActivePotionEffects();
                    Set<Object> enchantments = !emptyItem ? itemStack.getEnchantments().entrySet() : new HashSet(0);
                    StringBuilder enchantmentsString = new StringBuilder();
                    StringBuilder effectsString = new StringBuilder();
                    if (!enchantments.isEmpty()) {
                        for (Map.Entry entry : enchantments) {
                            name = (Enchantmententry.getKey()).getName().toLowerCase().replace("_", "/");
                            enchantmentsString.append(name).append("-").append(entry.getValue()).append(", ");
                        }
                        enchantmentsString = new StringBuilder(enchantmentsString.substring(0, enchantmentsString.length() - 2));
                    }
                    if (!potionEffects.isEmpty()) {
                        for (PotionEffect potionEffect : potionEffects) {
                            name = potionEffect.getType().getName().toLowerCase().replace("_", "/");
                            effectsString.append(name).append("-").append(potionEffect.getAmplifier() + 1).append(", ");
                        }
                        effectsString = new StringBuilder(effectsString.substring(0, effectsString.length() - 2));
                    }
                    String prevention = "delay, time: " + time + ", original-limit: " + originalLimit + ", limit: " + limit + ", difference: " + (limit - time) + ", block: " + BlockUtils.materialToString(block.getType()) + ", item: " + (emptyItem ? "none" : BlockUtils.materialToString(itemStack.getType())) + ", enchantments: " + (enchantments.isEmpty() ? "none" : "[" + enchantmentsString + "]") + ", effects: " + (potionEffects.isEmpty() ? "none" : "[" + effectsString + "]");
                    this.en.cancel(prevention, null, seconds);
                }
            }
        });
    }

    private void handleCheck(int seconds, Block block) {
        this.eo.call(() -> {
            double buffer;
            int limit;
            GameMode gameMode = this.protocol.getGameMode();
            if (gameMode == GameMode.CREATIVE && (limit = this.hackType.getCheck().handleCheck("max_breaks_per_second", 40)) > 0 && (buffer = this.es.count(1.0, 20)) >= (double)limit) {
                this.eo.cancel("breaks-per-second, type: " + BlockUtils.blockToString(block) + ", limit: " + limit, null, seconds);
            }
        });
    }

    private void checkCheckCancellation(int seconds, Block block) {
        this.ep.call(() -> {
            double bufferThreshold;
            GameMode gameMode = this.protocol.getGameMode();
            if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE || BlockUtils.isSensitive(this.protocol, block.getType())) return;
            int bufferTicks = NumberConversions.ceil((double)20.0);
            int limit = this.hackType.getCheck().handleCheck("indirect_surroundings_per_second", NumberConversions.round((double)(0.75)));
            if (limit <= 0) return;
            if (this.eq != null) {
                if (block.getX() == this.eq.getX() && block.getY() == this.eq.getY() && block.getZ() == this.eq.getZ() || block.getX() == this.eq.getX() + 1 && block.getY() == this.eq.getY() && block.getZ() == this.eq.getZ() || block.getX() == this.eq.getX() - 1 && block.getY() == this.eq.getY() && block.getZ() == this.eq.getZ() || block.getX() == this.eq.getX() && block.getY() == this.eq.getY() && block.getZ() == this.eq.getZ() + 1 || block.getX() == this.eq.getX() && block.getY() == this.eq.getY() && block.getZ() == this.eq.getZ() - 1 || block.getX() == this.eq.getX() + 1 && block.getY() == this.eq.getY() && block.getZ() == this.eq.getZ() + 1 || block.getX() == this.eq.getX() - 1 && block.getY() == this.eq.getY() && block.getZ() == this.eq.getZ() - 1 || block.getX() == this.eq.getX() + 1 && block.getY() == this.eq.getY() && block.getZ() == this.eq.getZ() - 1 || block.getX() == this.eq.getX() - 1 && block.getY() == this.eq.getY() && block.getZ() == this.eq.getZ() + 1 || block.getX() == this.eq.getX() && block.getY() == this.eq.getY() + 1 && block.getZ() == this.eq.getZ() || block.getX() == this.eq.getX() && block.getY() == this.eq.getY() - 1 && block.getZ() == this.eq.getZ()) return;
                bufferThreshold = this.et.count(1.0, bufferTicks);
            } else {
                bufferThreshold = this.et.count(1.0, bufferTicks);
            }
            if (bufferThreshold >= (double)limit) {
                String prevention = "indirect-surroundings-per-second, type: " + BlockUtils.blockToString(block) + ", limit: " + limit;
                this.ep.cancel(prevention, null, seconds);
            }
            this.eq = block;
        });
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        BlockBreakEvent event;
        Block b;
        if (object instanceof PlayerInteractEvent) {
            Block b2;
            PlayerInteractEvent event2 = (PlayerInteractEvent)object;
            if (event2.getAction() == Action.LEFT_CLICK_BLOCK && (b2 = event2.getClickedBlock()) != null && !ItemsAdder.is(b2)) {
                if ((double)this.er.size() == 20.0) {
                    Iterator<Integer> iterator = this.er.keySet().iterator();
                    iterator.next();
                    iterator.remove();
                }
                this.er.put(Objects.hash(b2.getWorld().getName(), b2.getType(), b2.getX(), b2.getY(), b2.getZ()), System.currentTimeMillis());
            }
        } else if (object instanceof BlockBreakEvent && !TreeFeller.canCancel(b = (event = BlockBreakEventobject).getBlock()) && !ItemsAdder.is(b)) {
            int seconds = NumberConversions.ceil((double)20.0);
            this.handleCheck(b, seconds);
            this.handleCheck(seconds, b);
            this.checkCheckCancellation(seconds, b);
            if (this.prevent()) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    protected boolean canRun() {
        return !MineBomb.isUsing(this.protocol) && !VehicleEnterListener.has(this.protocol, "drill") && !SuperPickaxe.isUsing(this.protocol) && !TerraformBlockBreakListener.isUsing(this.protocol) && !Compatibility.CompatibilityType.CRAFT_BOOK.isFunctional() && !Compatibility.CompatibilityType.ADVANCED_ENCHANTMENTS.isFunctional() && Attributes.getAmount(this.protocol, "PLAYER_BLOCK_BREAK_SPEED") == Double.MIN_VALUE;
    }
}

