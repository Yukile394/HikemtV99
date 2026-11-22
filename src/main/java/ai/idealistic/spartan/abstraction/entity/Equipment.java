/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.ArmorStand$LockType
 *  org.bukkit.entity.EntityType
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 */
package ai.idealistic.spartan.abstraction.entity;

import ai.idealistic.spartan.abstraction.protocol.PlayerProtocol;
import ai.idealistic.spartan.abstraction.world.ServerLocation;
import ai.idealistic.spartan.functionality.server.MultiVersion;
import ai.idealistic.spartan.functionality.server.PluginBase;
import ai.idealistic.spartan.utils.minecraft.inventory.Arrow;
import ai.idealistic.spartan.utils.minecraft.inventory.InventoryUtils;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class Equipment {
    public static final boolean hasSecondHand = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_9);
    public static final boolean hasLockType = MultiVersion.isOrGreater(MultiVersion.MCVersion.V1_16);
    private static final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUIDUUID.fromString("0e6f8837-3b61-4e42-b91c-81c0222bafd9"));
    private static final String backupName = "IdealisticAI";
    public final ArmorStand armorStand;
    public final Location location;
    private double handPose;
    private double headPose;

    public Equipment(Location location) {
        Location loc = location.clone();
        loc.setX((double)loc.getBlockX() + 0.5);
        loc.setZ((double)loc.getBlockZ() + 0.5);
        this.location = loc;
        this.armorStand = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        this.armorStand.setGravity(false);
        this.armorStand.setSmall(false);
        this.armorStand.setVisible(false);
        this.armorStand.setCustomName("\u00a72Spartan \u00a7cAnti\u00a74Cheat");
        this.armorStand.setCustomNameVisible(true);
        this.armorStand.setArms(true);
        if (hasLockType) {
            this.armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
            this.armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
            this.armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING);
            this.armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING);
            this.armorStand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
            this.armorStand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        }
        if (hasSecondHand) {
            this.armorStand.getEquipment().setItemInOffHand(new ItemStack(Material.SHIELD));
        }
        this.updateBodyAndLegs();
        this.updateHead();
        ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
        meta.setColor(Color.RED);
        itemStack.setItemMeta((ItemMeta)meta);
        this.armorStand.getEquipment().setChestplate(itemStack);
        this.armorStand.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        itemStack = new ItemStack(Material.LEATHER_BOOTS);
        meta = (LeatherArmorMeta)itemStack.getItemMeta();
        meta.setColor(Color.GRAY);
        itemStack.setItemMeta((ItemMeta)meta);
        this.armorStand.getEquipment().setBoots(itemStack);
        itemStack = new ItemStack(Material.IRON_SWORD);
        itemStack.addEnchantment(Arrow.DURABILITY, 1);
        this.armorStand.getEquipment().setItemInHand(itemStack);
    }

    private void updateBodyAndLegs() {
        this.armorStand.setBodyPose(new EulerAngle(0.0, 0.0, 0.0));
        this.armorStand.setRightLegPose(new EulerAngle(0.0, 0.0, Math.toRadians(5.0)));
        this.armorStand.setLeftLegPose(new EulerAngle(0.0, 0.0, -Math.toRadians(5.0)));
    }

    public boolean animate(List<PlayerProtocol> protocols) {
        if (this.armorStand.isDead()) {
            return false;
        }
        this.updateBodyAndLegs();
        double headMax = 5.0;
        double handMax = 15.0;
        if (this.headPose >= headMax) {
            this.headPose = -headMax;
        }
        this.headPose += 0.1;
        if (this.handPose >= handMax) {
            this.handPose = -handMax;
        }
        this.handPose += 0.25;
        this.armorStand.getWorld().playEffect(this.armorStand.getLocation().clone().add(0.0, Math.ceil(this.armorStand.getEyeHeight()) * 2.0, 0.0), Effect.ENDER_SIGNAL, 16);
        this.armorStand.setHeadPose(new EulerAngle(Math.toRadians(Math.abs(this.headPose)), 0.0, 0.0));
        this.armorStand.setRightArmPose(new EulerAngle(0.0, 0.0, Math.toRadians(Math.abs(this.handPose))));
        this.armorStand.setLeftArmPose(new EulerAngle(0.0, 0.0, -Math.toRadians(Math.abs(this.handPose))));
        PlayerProtocol closest = null;
        for (PlayerProtocol protocol : protocols) {
            if (closest != null && !(ServerLocation.distance(protocol.getLocationOrVehicle(), this.location) < ServerLocation.distance(closest.getLocationOrVehicle(), this.location))) continue;
            closest = protocol;
        }
        Vector playerVec = closest.getLocation().toVector();
        Vector sheepVec = this.armorStand.getLocation().toVector();
        Vector toLookAtVec = playerVec.subtract(sheepVec);
        this.armorStand.teleport(this.armorStand.getLocation().setDirection(toLookAtVec));
        return true;
    }

    public void updateHead() {
        PluginBase.dataThread.executeIfFree(() -> this.armorStand.getEquipment().setHelmet(InventoryUtils.getSkull(offlinePlayer, backupName, true)));
    }

    public void remove() {
        this.armorStand.remove();
    }

    public UUID getUniqueId() {
        return this.armorStand.getUniqueId();
    }
}

