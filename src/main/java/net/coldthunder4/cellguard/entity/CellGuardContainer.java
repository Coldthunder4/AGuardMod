package net.coldthunder4.cellguard.entity;

import com.mojang.datafixers.util.Pair;

import net.coldthunder4.cellguard.CellGuard;
import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CellGuardContainer extends AbstractContainerMenu {

    private final Container cellGuardInventory;
    private final GuardEntity cellGuard;
    public CellGuardContainer(int id, Inventory playerInventory, Container cellGuardInventory, final GuardEntity cellGuard) {
        super((MenuType<?>)null, id);
        this.cellGuardInventory = cellGuardInventory;
        this.cellGuard = cellGuard;
        cellGuardInventory.startOpen(playerInventory.player);
        this.addSlot(new Slot(cellGuardInventory, 0, 8, 9) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.canEquip(EquipmentSlot.HEAD, cellGuard) && CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }


            @Override
            public int getMaxStackSize() {
                return 1;
            }


            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                cellGuard.setItemSlot(EquipmentSlot.HEAD, stack);
            }


            @Override
            public boolean mayPickup(Player playerIn) {
                return CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET);
            }

        });
        this.addSlot(new Slot(cellGuardInventory, 1, 8, 26) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.canEquip(EquipmentSlot.CHEST, cellGuard) && CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                cellGuard.setItemSlot(EquipmentSlot.CHEST, stack);
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                return CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE);
            }
        });

        this.addSlot(new Slot(cellGuardInventory, 2, 8, 44) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.canEquip(EquipmentSlot.LEGS, cellGuard) && CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                cellGuard.setItemSlot(EquipmentSlot.LEGS, stack);
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                return CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS);
            }

        });

        this.addSlot(new Slot(cellGuardInventory, 3, 8, 62) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.canEquip(EquipmentSlot.FEET, cellGuard) && CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                cellGuard.setItemSlot(EquipmentSlot.FEET, stack);
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                return CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS);
            }

        });

        this.addSlot(new Slot(cellGuardInventory, 4, 77, 62) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }

            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                cellGuard.setItemSlot(EquipmentSlot.OFFHAND, stack);
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                return CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }

        });

        this.addSlot(new Slot(cellGuardInventory, 5, 77, 44) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return CellGuard.ownerChecker(playerInventory.player, cellGuard);
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                return CellGuard.ownerChecker(playerIn, cellGuard);
            }

            @Override
            public void set(ItemStack stack) {
                super.set(stack);
                cellGuard.setItemSlot(EquipmentSlot.MAINHAND, stack);
            }

        });

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
        }

    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.cellGuardInventory.stillValid(playerIn) && this.cellGuard.isAlive() && this.cellGuard.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = this.cellGuardInventory.getContainerSize();
            if (index < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i <= 2 || !this.moveItemStackTo(itemstack1, 2, i, false)) {
                int j = i + 27;
                int k = j + 9;
                if (index >= j && index < k) {
                    if (!this.moveItemStackTo(itemstack1, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= i && index < j) {
                    if (!this.moveItemStackTo(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.cellGuardInventory.stopOpen(playerIn);
    }

}
