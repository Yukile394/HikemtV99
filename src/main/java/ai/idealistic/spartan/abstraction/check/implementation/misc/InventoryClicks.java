/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 */
package ai.idealistic.spartan.abstraction.check.implementation.misc;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.CheckDetection;
import ai.idealistic.spartan.abstraction.check.CheckEnums;
import ai.idealistic.spartan.abstraction.check.CheckRunner;
import ai.idealistic.spartan.abstraction.check.definition.ImplementedDetection;
import ai.idealistic.spartan.abstraction.data.Buffer;
import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.utils.minecraft.world.BlockUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClicks
extends CheckRunner {
    private final CheckDetection cm = new ImplementedDetection(this, Check.DataType.JAVA, null, "shift", true);
    private final CheckDetection cn = new ImplementedDetection(this, Check.DataType.JAVA, null, "slow", true);
    private final CheckDetection co = new ImplementedDetection(this, Check.DataType.JAVA, null, "medium", true);
    private final CheckDetection cp = new ImplementedDetection(this, Check.DataType.JAVA, null, "fast", true);
    private long time;
    private int cq;
    private final Buffer.Storage cr = new Buffer.Storage();
    private final Buffer.Storage cs = new Buffer.Storage();
    private final Buffer.Storage ct = new Buffer.Storage();
    private final Buffer.Storage cu = new Buffer.Storage();

    public InventoryClicks(CheckEnums.HackType hackType, PlayerProtocol protocol) {
        super(hackType, protocol);
    }

    @Override
    protected void handleInternal(boolean cancelled, Object object) {
        if (object instanceof InventoryClickEvent && (this.cm.canCall() || this.cn.canCall() || this.co.canCall() || this.cp.canCall())) {
            Material material;
            InventoryClickEvent event = (InventoryClickEvent)object;
            ItemStack item = event.getCurrentItem();
            ClickType click = event.getClick();
            if (!(click.isCreativeAction() || click.isKeyboardClick() || item == null || item.getItemMeta() == null || BlockUtils.isSensitive(this.protocol, material = item.getType()))) {
                boolean quick = this.protocol.bukkit().getOpenInventory().countSlots() <= 46;
                boolean shift = click.isShiftClick();
                String type = BlockUtils.materialToString(material);
                int newHash = type.hashCode();
                int oldHash = this.cq;
                this.cq = newHash;
                if (shift && !quick && !BlockUtils.hasMaterial(this.protocol.bukkit().getOpenInventory().getCursor()) && this.cu.count(1.0, 5) == 4.0) {
                    this.cu.reset();
                    this.cm.call(() -> this.cm.cancel("shift, item: " + type));
                } else {
                    long timePassed = System.currentTimeMillis() - this.time;
                    this.time = System.currentTimeMillis();
                    if ((!shift && !quick || newHash != oldHash) && timePassed < 150L) {
                        this.cn.call(() -> {
                            if (this.cr.count(1.0, 10) >= 8.0) {
                                this.cn.cancel("slow, ms: " + timePassed + ", item: " + type);
                            }
                        });
                        if (timePassed < 100L) {
                            this.co.call(() -> {
                                if (this.cs.count(1.0, 5) >= 4.0) {
                                    this.co.cancel("medium, ms: " + timePassed + ", item: " + type);
                                }
                            });
                            this.cp.call(() -> {
                                if (timePassed < 50L && this.ct.count(1.0, 5) >= 4.0) {
                                    this.cp.cancel("fast, ms: " + timePassed + ", item: " + type);
                                }
                            });
                        }
                    }
                }
            }
            if (this.prevent()) {
                event.setCancelled(true);
            }
        }
    }
}

