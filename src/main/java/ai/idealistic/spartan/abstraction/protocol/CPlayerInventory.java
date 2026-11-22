/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package ai.idealistic.spartan.abstraction.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CPlayerInventory
implements PlayerInventory {
    private final PlayerInventory inventory;

    public CPlayerInventory(PlayerInventory inventory) {
    }

    public @Nullable ItemStack @NotNull [] getArmorContents() {
        ItemStack[] itemStackArray = this.inventory == null ? new ItemStack[]{} : this.inventory.getArmorContents();
        if (itemStackArray == null) {
            CPlayerInventory.$$$reportNull$$$0(0);
        }
        return itemStackArray;
    }

    public @Nullable ItemStack @NotNull [] getExtraContents() {
        ItemStack[] itemStackArray = this.inventory == null ? new ItemStack[]{} : this.inventory.getExtraContents();
        if (itemStackArray == null) {
            CPlayerInventory.$$$reportNull$$$0(1);
        }
        return itemStackArray;
    }

    @Nullable
    public ItemStack getHelmet() {
        return this.inventory == null ? null : this.inventory.getHelmet();
    }

    @Nullable
    public ItemStack getChestplate() {
        return this.inventory == null ? null : this.inventory.getChestplate();
    }

    @Nullable
    public ItemStack getLeggings() {
        return this.inventory == null ? null : this.inventory.getLeggings();
    }

    @Nullable
    public ItemStack getBoots() {
        return this.inventory == null ? null : this.inventory.getBoots();
    }

    public int getSize() {
        return this.inventory == null ? 0 : this.inventory.getSize();
    }

    public int getMaxStackSize() {
        return this.inventory == null ? 0 : this.inventory.getMaxStackSize();
    }

    public void setMaxStackSize(int i) {
        if (this.inventory != null) {
            this.inventory.setMaxStackSize(i);
        }
    }

    @Nullable
    public ItemStack getItem(int i) {
        return this.inventory == null ? null : this.inventory.getItem(i);
    }

    public void setItem(int i, @Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setItem(i, itemStack);
        }
    }

    @NotNull
    public HashMap<Integer, ItemStack> addItem(ItemStack ... itemStacks) throws IllegalArgumentException {
        if (itemStacks == null) {
            CPlayerInventory.$$$reportNull$$$0(2);
        }
        if (this.inventory != null) {
            HashMap hashMap = this.inventory.addItem(itemStacks);
            if (hashMap == null) {
                CPlayerInventory.$$$reportNull$$$0(3);
            }
            return hashMap;
        }
        return new HashMap<Integer, ItemStack>(0);
    }

    @NotNull
    public HashMap<Integer, ItemStack> removeItem(ItemStack ... itemStacks) throws IllegalArgumentException {
        if (itemStacks == null) {
            CPlayerInventory.$$$reportNull$$$0(4);
        }
        if (this.inventory != null) {
            HashMap hashMap = this.inventory.removeItem(itemStacks);
            if (hashMap == null) {
                CPlayerInventory.$$$reportNull$$$0(5);
            }
            return hashMap;
        }
        return new HashMap<Integer, ItemStack>(0);
    }

    @NotNull
    public HashMap<Integer, ItemStack> removeItemAnySlot(ItemStack ... itemStacks) throws IllegalArgumentException {
        if (itemStacks == null) {
            CPlayerInventory.$$$reportNull$$$0(6);
        }
        if (this.inventory != null) {
            HashMap hashMap = this.inventory.removeItemAnySlot(itemStacks);
            if (hashMap == null) {
                CPlayerInventory.$$$reportNull$$$0(7);
            }
            return hashMap;
        }
        return new HashMap<Integer, ItemStack>(0);
    }

    public @Nullable ItemStack @NotNull [] getContents() {
        ItemStack[] itemStackArray = this.inventory == null ? new ItemStack[]{} : this.inventory.getContents();
        if (itemStackArray == null) {
            CPlayerInventory.$$$reportNull$$$0(8);
        }
        return itemStackArray;
    }

    public void setContents(@Nullable ItemStack @NotNull [] itemStacks) throws IllegalArgumentException {
        if (itemStacks == null) {
            CPlayerInventory.$$$reportNull$$$0(9);
        }
        if (this.inventory != null) {
            this.inventory.setContents(itemStacks);
        }
    }

    public @Nullable ItemStack @NotNull [] getStorageContents() {
        ItemStack[] itemStackArray = this.inventory == null ? new ItemStack[]{} : this.inventory.getStorageContents();
        if (itemStackArray == null) {
            CPlayerInventory.$$$reportNull$$$0(10);
        }
        return itemStackArray;
    }

    public void setStorageContents(@Nullable ItemStack @NotNull [] itemStacks) throws IllegalArgumentException {
        if (itemStacks == null) {
            CPlayerInventory.$$$reportNull$$$0(11);
        }
        if (this.inventory != null) {
            this.inventory.setStorageContents(itemStacks);
        }
    }

    public boolean contains(@NotNull Material material) throws IllegalArgumentException {
        if (material == null) {
            CPlayerInventory.$$$reportNull$$$0(12);
        }
        return this.inventory != null && this.inventory.contains(material);
    }

    public boolean contains(@Nullable ItemStack itemStack) {
        return this.inventory != null && this.inventory.contains(itemStack);
    }

    public boolean contains(@NotNull Material material, int i) throws IllegalArgumentException {
        if (material == null) {
            CPlayerInventory.$$$reportNull$$$0(13);
        }
        return this.inventory != null && this.inventory.contains(material, i);
    }

    public boolean contains(@Nullable ItemStack itemStack, int i) {
        return this.inventory != null && this.inventory.contains(itemStack, i);
    }

    public boolean containsAtLeast(@Nullable ItemStack itemStack, int i) {
        return this.inventory != null && this.inventory.containsAtLeast(itemStack, i);
    }

    @NotNull
    public HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
        if (material == null) {
            CPlayerInventory.$$$reportNull$$$0(14);
        }
        HashMap hashMap = this.inventory == null ? new HashMap(0) : this.inventory.all(material);
        if (hashMap == null) {
            CPlayerInventory.$$$reportNull$$$0(15);
        }
        return hashMap;
    }

    @NotNull
    public HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack itemStack) {
        HashMap hashMap = this.inventory == null ? new HashMap(0) : this.inventory.all(itemStack);
        if (hashMap == null) {
            CPlayerInventory.$$$reportNull$$$0(16);
        }
        return hashMap;
    }

    public int first(@NotNull Material material) throws IllegalArgumentException {
        if (material == null) {
            CPlayerInventory.$$$reportNull$$$0(17);
        }
        return this.inventory == null ? 0 : this.inventory.first(material);
    }

    public int first(@NotNull ItemStack itemStack) {
        if (itemStack == null) {
            CPlayerInventory.$$$reportNull$$$0(18);
        }
        return this.inventory == null ? 0 : this.inventory.first(itemStack);
    }

    public int firstEmpty() {
        return this.inventory == null ? 0 : this.inventory.firstEmpty();
    }

    public boolean isEmpty() {
        return this.inventory != null && this.inventory.isEmpty();
    }

    public void remove(@NotNull Material material) throws IllegalArgumentException {
        if (material == null) {
            CPlayerInventory.$$$reportNull$$$0(19);
        }
        if (this.inventory != null) {
            this.inventory.remove(material);
        }
    }

    public void remove(@NotNull ItemStack itemStack) {
        if (itemStack == null) {
            CPlayerInventory.$$$reportNull$$$0(20);
        }
        if (this.inventory != null) {
            this.inventory.remove(itemStack);
        }
    }

    public void clear(int i) {
        if (this.inventory != null) {
            this.inventory.clear(i);
        }
    }

    public void clear() {
        if (this.inventory != null) {
            this.inventory.clear();
        }
    }

    public int close() {
        return this.inventory == null ? 0 : this.inventory.close();
    }

    @NotNull
    public List<HumanEntity> getViewers() {
        List list = this.inventory == null ? new ArrayList(0) : this.inventory.getViewers();
        if (list == null) {
            CPlayerInventory.$$$reportNull$$$0(21);
        }
        return list;
    }

    @NotNull
    public InventoryType getType() {
        InventoryType inventoryType = this.inventory == null ? InventoryType.PLAYER : this.inventory.getType();
        if (inventoryType == null) {
            CPlayerInventory.$$$reportNull$$$0(22);
        }
        return inventoryType;
    }

    public void setItem(@NotNull EquipmentSlot equipmentSlot, @Nullable ItemStack itemStack) {
        if (equipmentSlot == null) {
            CPlayerInventory.$$$reportNull$$$0(23);
        }
        if (this.inventory != null) {
            this.inventory.setItem(equipmentSlot, itemStack);
        }
    }

    @NotNull
    public ItemStack getItem(@NotNull EquipmentSlot equipmentSlot) {
        if (equipmentSlot == null) {
            CPlayerInventory.$$$reportNull$$$0(24);
        }
        ItemStack itemStack = this.inventory == null ? new ItemStack(Material.AIR) : this.inventory.getItem(equipmentSlot);
        if (itemStack == null) {
            CPlayerInventory.$$$reportNull$$$0(25);
        }
        return itemStack;
    }

    public void setArmorContents(@Nullable ItemStack[] itemStacks) {
        if (this.inventory != null) {
            this.inventory.setArmorContents(itemStacks);
        }
    }

    public void setExtraContents(@Nullable ItemStack[] itemStacks) {
        if (this.inventory != null) {
            this.inventory.setExtraContents(itemStacks);
        }
    }

    public void setHelmet(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setHelmet(itemStack);
        }
    }

    public void setChestplate(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setChestplate(itemStack);
        }
    }

    public void setLeggings(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setLeggings(itemStack);
        }
    }

    public void setBoots(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setBoots(itemStack);
        }
    }

    @NotNull
    public ItemStack getItemInMainHand() {
        ItemStack itemStack = this.inventory == null ? new ItemStack(Material.AIR) : this.inventory.getItemInMainHand();
        if (itemStack == null) {
            CPlayerInventory.$$$reportNull$$$0(26);
        }
        return itemStack;
    }

    public void setItemInMainHand(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setItemInMainHand(itemStack);
        }
    }

    @NotNull
    public ItemStack getItemInOffHand() {
        ItemStack itemStack = this.inventory == null ? new ItemStack(Material.AIR) : this.inventory.getItemInOffHand();
        if (itemStack == null) {
            CPlayerInventory.$$$reportNull$$$0(27);
        }
        return itemStack;
    }

    public void setItemInOffHand(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setItemInOffHand(itemStack);
        }
    }

    @Deprecated
    @NotNull
    public ItemStack getItemInHand() {
        ItemStack itemStack = this.inventory == null ? new ItemStack(Material.AIR) : this.inventory.getItemInHand();
        if (itemStack == null) {
            CPlayerInventory.$$$reportNull$$$0(28);
        }
        return itemStack;
    }

    @Deprecated
    public void setItemInHand(@Nullable ItemStack itemStack) {
        if (this.inventory != null) {
            this.inventory.setItemInHand(itemStack);
        }
    }

    public int getHeldItemSlot() {
        return this.inventory == null ? 0 : this.inventory.getHeldItemSlot();
    }

    public void setHeldItemSlot(int i) {
        if (this.inventory != null) {
            this.inventory.setHeldItemSlot(i);
        }
    }

    @Nullable
    public HumanEntity getHolder() {
        return this.inventory == null ? null : this.inventory.getHolder();
    }

    @Nullable
    public InventoryHolder getHolder(boolean b) {
        return this.inventory == null ? null : this.inventory.getHolder(b);
    }

    @NotNull
    public ListIterator<ItemStack> iterator() {
        if (this.inventory == null) {
            ArrayList list = new ArrayList(0);
            ListIterator<ItemStack> listIterator = list.listIterator();
            if (listIterator == null) {
                CPlayerInventory.$$$reportNull$$$0(29);
            }
            return listIterator;
        }
        ListIterator listIterator = this.inventory.iterator();
        if (listIterator == null) {
            CPlayerInventory.$$$reportNull$$$0(30);
        }
        return listIterator;
    }

    @NotNull
    public ListIterator<ItemStack> iterator(int i) {
        if (this.inventory == null) {
            ArrayList list = new ArrayList(0);
            ListIterator<ItemStack> listIterator = list.listIterator(i);
            if (listIterator == null) {
                CPlayerInventory.$$$reportNull$$$0(31);
            }
            return listIterator;
        }
        ListIterator listIterator = this.inventory.iterator(i);
        if (listIterator == null) {
            CPlayerInventory.$$$reportNull$$$0(32);
        }
        return listIterator;
    }

    @Nullable
    public Location getLocation() {
        return this.inventory == null ? null : this.inventory.getLocation();
    }

    private static /* synthetic */ void $$$reportNull$$$0(int n) {
        RuntimeException runtimeException;
        Object[] objectArray;
        Object[] objectArray2;
        int n2;
        String string;
        switch (n) {
            default: {
                string = "@NotNull method %s.%s must not return null";
                break;
            }
            case 2:
            case 4:
            case 6:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            case 20:
            case 23:
            case 24: {
                string = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            }
        }
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 2:
            case 4:
            case 6:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            case 20:
            case 23:
            case 24: {
                n2 = 3;
            }
        }
        Object[] objectArray3 = new Object[n2];
        switch (n) {
            default: {
                objectArray2 = objectArray3;
                objectArray3[0] = "ai/idealistic/spartan/abstraction/protocol/CPlayerInventory";
                break;
            }
            case 2:
            case 4:
            case 6:
            case 9:
            case 11: {
                objectArray2 = objectArray3;
                objectArray3[0] = "itemStacks";
                break;
            }
            case 12:
            case 13:
            case 14:
            case 17:
            case 19: {
                objectArray2 = objectArray3;
                objectArray3[0] = "material";
                break;
            }
            case 18:
            case 20: {
                objectArray2 = objectArray3;
                objectArray3[0] = "itemStack";
                break;
            }
            case 23:
            case 24: {
                objectArray2 = objectArray3;
                objectArray3[0] = "equipmentSlot";
            }
        }
        switch (n) {
            default: {
                objectArray = objectArray2;
                objectArray2[1] = "getArmorContents";
                break;
            }
            case 1: {
                objectArray = objectArray2;
                objectArray2[1] = "getExtraContents";
                break;
            }
            case 2:
            case 4:
            case 6:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            case 20:
            case 23:
            case 24: {
                objectArray = objectArray2;
                objectArray2[1] = "ai/idealistic/spartan/abstraction/protocol/CPlayerInventory";
                break;
            }
            case 3: {
                objectArray = objectArray2;
                objectArray2[1] = "addItem";
                break;
            }
            case 5: {
                objectArray = objectArray2;
                objectArray2[1] = "removeItem";
                break;
            }
            case 7: {
                objectArray = objectArray2;
                objectArray2[1] = "removeItemAnySlot";
                break;
            }
            case 8: {
                objectArray = objectArray2;
                objectArray2[1] = "getContents";
                break;
            }
            case 10: {
                objectArray = objectArray2;
                objectArray2[1] = "getStorageContents";
                break;
            }
            case 15:
            case 16: {
                objectArray = objectArray2;
                objectArray2[1] = "all";
                break;
            }
            case 21: {
                objectArray = objectArray2;
                objectArray2[1] = "getViewers";
                break;
            }
            case 22: {
                objectArray = objectArray2;
                objectArray2[1] = "getType";
                break;
            }
            case 25: {
                objectArray = objectArray2;
                objectArray2[1] = "getItem";
                break;
            }
            case 26: {
                objectArray = objectArray2;
                objectArray2[1] = "getItemInMainHand";
                break;
            }
            case 27: {
                objectArray = objectArray2;
                objectArray2[1] = "getItemInOffHand";
                break;
            }
            case 28: {
                objectArray = objectArray2;
                objectArray2[1] = "getItemInHand";
                break;
            }
            case 29:
            case 30:
            case 31:
            case 32: {
                objectArray = objectArray2;
                objectArray2[1] = "iterator";
            }
        }
        switch (n) {
            default: {
                break;
            }
            case 2: {
                objectArray[2] = "addItem";
                break;
            }
            case 4: {
                objectArray[2] = "removeItem";
                break;
            }
            case 6: {
                objectArray[2] = "removeItemAnySlot";
                break;
            }
            case 9: {
                objectArray[2] = "setContents";
                break;
            }
            case 11: {
                objectArray[2] = "setStorageContents";
                break;
            }
            case 12:
            case 13: {
                objectArray[2] = "contains";
                break;
            }
            case 14: {
                objectArray[2] = "all";
                break;
            }
            case 17:
            case 18: {
                objectArray[2] = "first";
                break;
            }
            case 19:
            case 20: {
                objectArray[2] = "remove";
                break;
            }
            case 23: {
                objectArray[2] = "setItem";
                break;
            }
            case 24: {
                objectArray[2] = "getItem";
            }
        }
        String string2 = String.format(string, objectArray);
        switch (n) {
            default: {
                runtimeException = new IllegalStateException(string2);
                break;
            }
            case 2:
            case 4:
            case 6:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            case 20:
            case 23:
            case 24: {
                runtimeException = new IllegalArgumentException(string2);
            }
        }
        throw runtimeException;
    }
}

