/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Material
 */
package ai.idealistic.spartan.abstraction.check;

import ai.idealistic.spartan.abstraction.check.Check;
import ai.idealistic.spartan.abstraction.check.implementation.combat.Criticals;
import ai.idealistic.spartan.abstraction.check.implementation.combat.FastClicks;
import ai.idealistic.spartan.abstraction.check.implementation.combat.HitReach;
import ai.idealistic.spartan.abstraction.check.implementation.combat.Velocity;
import ai.idealistic.spartan.abstraction.check.implementation.combat.killaura.KillAura;
import ai.idealistic.spartan.abstraction.check.implementation.misc.FastEat;
import ai.idealistic.spartan.abstraction.check.implementation.misc.FastHeal;
import ai.idealistic.spartan.abstraction.check.implementation.misc.ImpossibleInventory;
import ai.idealistic.spartan.abstraction.check.implementation.misc.InventoryClicks;
import ai.idealistic.spartan.abstraction.check.implementation.misc.NoSwing;
import ai.idealistic.spartan.abstraction.check.implementation.movement.Track;
import ai.idealistic.spartan.abstraction.check.implementation.movement.MorePackets;
import ai.idealistic.spartan.abstraction.check.implementation.movement.Cancellation;
import ai.idealistic.spartan.abstraction.check.implementation.movement.exploits.Spoof;
import ai.idealistic.spartan.abstraction.check.implementation.movement.irregularmovements.IrregularMovements;
import ai.idealistic.spartan.abstraction.check.implementation.world.BlockReach;
import ai.idealistic.spartan.abstraction.check.implementation.world.FastBreak;
import ai.idealistic.spartan.abstraction.check.implementation.world.FastPlace;
import ai.idealistic.spartan.abstraction.check.implementation.world.XRay;
import ai.idealistic.spartan.abstraction.check.implementation.world.impossibleactions.ImpossibleActions;
import ai.idealistic.spartan.utils.minecraft.inventory.Spade;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.Material;

public class CheckEnums {

    public static enum HackCategoryType {
        COMBAT(Material.IRON_SWORD),
        MOVEMENT(Material.FEATHER),
        WORLD(Material.DIAMOND_PICKAXE),
        MISCELLANEOUS(Spade.get("crafting_table"));

        public final Material material;

        private HackCategoryType(Material material) {
        }

        public String toString() {
            return super.toString().toLowerCase().replace("_", "-");
        }
    }

    public static enum HackType {
        HIT_REACH(HackCategoryType.COMBAT, HitReach.class, Material.ARROW),
        CRITICALS(HackCategoryType.COMBAT, Criticals.class, Material.REDSTONE),
        FAST_CLICKS(HackCategoryType.COMBAT, FastClicks.class, Material.LEVER),
        VELOCITY(HackCategoryType.COMBAT, Velocity.class, Material.CHAINMAIL_BOOTS),
        KILL_AURA(HackCategoryType.COMBAT, KillAura.class, Material.IRON_SWORD),
        IRREGULAR_MOVEMENTS(HackCategoryType.MOVEMENT, IrregularMovements.class, Material.HOPPER),
        GRAVITY_SIMULATION(HackCategoryType.MOVEMENT, Track.class, Material.LADDER),
        SPEED_SIMULATION(HackCategoryType.MOVEMENT, Cancellation.class, Material.FEATHER),
        EXPLOITS(HackCategoryType.MOVEMENT, Spoof.class, Material.TNT),
        MORE_PACKETS(HackCategoryType.MOVEMENT, MorePackets.class, Material.FLINT_AND_STEEL),
        X_RAY(HackCategoryType.WORLD, XRay.class, Material.GLASS),
        IMPOSSIBLE_ACTIONS(HackCategoryType.WORLD, ImpossibleActions.class, Material.BEDROCK),
        FAST_BREAK(HackCategoryType.WORLD, FastBreak.class, Material.ENCHANTED_BOOK),
        FAST_PLACE(HackCategoryType.WORLD, FastPlace.class, Material.STONE),
        BLOCK_REACH(HackCategoryType.WORLD, BlockReach.class, Material.FISHING_ROD),
        NO_SWING(HackCategoryType.MISCELLANEOUS, NoSwing.class, Material.SHEARS),
        INVENTORY_CLICKS(HackCategoryType.MISCELLANEOUS, InventoryClicks.class, Material.STONE_BUTTON),
        FAST_HEAL(HackCategoryType.MISCELLANEOUS, FastHeal.class, Material.APPLE),
        IMPOSSIBLE_INVENTORY(HackCategoryType.MISCELLANEOUS, ImpossibleInventory.class, Material.MILK_BUCKET),
        FAST_EAT(HackCategoryType.MISCELLANEOUS, FastEat.class, Material.CAKE);

        private Check check;
        public final HackCategoryType category;
        public final Class<?> executor;
        private final Map<String, Long> detections;
        public final Material material;

        private HackType(HackCategoryType category, Class<?> executor, Material material) {
            this.detections = new ConcurrentHashMap<String, Long>();
            this.check = new Check(this);
        }

        public void resetCheck() {
            if (this.check != null) {
                this.check = new Check(this);
            }
        }

        public void addDetection(String detection, long averageTime) {
            this.detections.put(detection, averageTime);
        }

        public void removeDetection(String detection) {
            this.detections.remove(detection);
        }

        public long getDefaultAverageTime(String detection) {
            return this.detections.get(detection);
        }

        public Collection<String> getDetections() {
            return this.detections.keySet();
        }

        public String toString() {
            return super.toString().toLowerCase().replace("_", "-");
        }

        @Generated
        public Check getCheck() {
            return this.check;
        }
    }
}

