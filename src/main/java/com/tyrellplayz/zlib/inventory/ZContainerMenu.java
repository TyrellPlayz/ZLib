package com.tyrellplayz.zlib.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Makes creating container menus easy. Mostly when it comes to slots.
 */
public abstract class ZContainerMenu extends AbstractContainerMenu {

    public static final int TOTAL_PLAYER_SLOTS = 36;

    protected final Container container;
    protected final ContainerData data;
    protected final Level level;

    public ZContainerMenu(MenuType<?> type, int containerId, Inventory inventory, Container container, ContainerData data) {
        super(type, containerId);
        this.container = container;
        this.data = data;
        this.level = inventory.player.level;

        createSlots(inventory,container);
        this.addDataSlots(data);
    }

    protected abstract void createSlots(Inventory inventory, Container container);

    /**
     * Creates the slots for the players inventory.
     * To be called in the constructor after the containers inventory slots.
     * @param xPos
     * @param yPos
     * @param inventory
     */
    protected void createPlayerSlots(int xPos, int yPos, Inventory inventory) {
        // Main inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory,x+y*9+9,xPos+x*18,yPos+y*18));
            }
        }
        // Hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory,x,xPos+x*18,yPos+58));
        }
        //  9   10  11  12  13  14  15  16  17
        //  18  19  20  21  22  23  24  25  26
        //  27  28  29  30  31  32  33  34  35
        //
        //  0   1   2   3   4   5   6   7   8
    }

    private int getPlayerInventoryStartIndex() {
        return container.getContainerSize();
    }

    private int getPlayerHotbarStartIndex() {
        return getPlayerInventoryStartIndex() + (TOTAL_PLAYER_SLOTS-9);
    }

    private int getPlayerInventoryEndIndex() {
        return getPlayerInventoryStartIndex() + (TOTAL_PLAYER_SLOTS-1);
    }
    public boolean slotWithinPlayerInventory(int slotIndex) {
        return slotIndex >= getPlayerInventoryStartIndex() && slotIndex <= getPlayerHotbarStartIndex()-1;
    }

    public boolean slotWithinPlayerHotbar(int slotIndex) {
        return slotIndex >= getPlayerHotbarStartIndex() && slotIndex <= getPlayerInventoryEndIndex();
    }

    /**
     * An easier way to handle moving items to slots in the container.
     * Moving items to the players inventory is automatically handled.
     * @param stack The stack to be moved to the container
     * @param slotIndex The stack inventory slot index.
     * @return
     */
    public abstract MoveResponse quickMoveStackToContainer(ItemStack stack, int slotIndex);


    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {

            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();

            // If the slot is in the containers inventory then move it to the players inventory.
            if(slotIndex < getPlayerInventoryStartIndex()) {
                if (!this.moveItemStackTo(itemStack1, getPlayerInventoryStartIndex(), getPlayerInventoryEndIndex()+1, false)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemStack1, itemstack);

                // Else the slot is in the players inventory so move it into a valid slot in the containers inventory.
            }else {
                MoveResponse transferResponse = quickMoveStackToContainer(itemstack,slotIndex);
                // If the response is not null then the ItemStack can be transferred to a slot.
                if(transferResponse != null) {
                    if (!this.moveItemStackTo(itemStack1, transferResponse.startIndex, transferResponse.endIndex, false)) {
                        // The items cant be transferred into the slot/s.
                        if(transferResponse.elseFullStartIndex == -1 && transferResponse.elseFullEndIndex == -1) {
                            return ItemStack.EMPTY;
                        }
                        if(!this.moveItemStackTo(itemStack1,transferResponse.elseFullStartIndex,transferResponse.elseFullEndIndex,false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                // If the ItemStack can't be moved into the containers inventory then move it around the players inventory.
                // If the slot is the players main inventory then move it into the players hot-bar.
                else if (slotWithinPlayerInventory(slotIndex)) {
                    if (!this.moveItemStackTo(itemStack1, getPlayerHotbarStartIndex(), getPlayerInventoryEndIndex()+1, false)) {
                        return ItemStack.EMPTY;
                    }
                    // If the slot is in the players hot-bar then move it into the players main inventory.
                } else if (slotWithinPlayerHotbar(slotIndex)) {
                    if (!this.moveItemStackTo(itemStack1, getPlayerInventoryStartIndex(), getPlayerHotbarStartIndex(), false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack1);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    protected static class MoveResponse {
        private final int startIndex;
        private final int endIndex;

        private int elseFullStartIndex = -1;
        private int elseFullEndIndex = -1;

        public MoveResponse(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public MoveResponse(int... slots) {
            List<Integer> list = Arrays.asList(ArrayUtils.toObject(slots));
            this.startIndex = Collections.min(list);
            this.endIndex = Collections.max(list)+1;
        }

        public MoveResponse elseIfFull(int... slots) {
            List<Integer> list = Arrays.asList(ArrayUtils.toObject(slots));
            this.elseFullStartIndex = Collections.min(list);
            this.elseFullEndIndex = Collections.max(list)+1;
            return this;
        }

        public MoveResponse elseIfFull(int startIndex, int endIndex) {
            this.elseFullStartIndex = startIndex;
            this.elseFullEndIndex = endIndex;
            return this;
        }

    }

}
