package com.tyrellplayz.zlib.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

public class FuelSlot extends Slot {

    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final FuelType fuelType;

    public FuelSlot(Container container, RecipeType<? extends AbstractCookingRecipe> recipeType, FuelType fuelType, int slot, int x, int y) {
        super(container, slot, x, y);
        this.recipeType = recipeType;
        this.fuelType = fuelType;
    }

    public boolean mayPlace(ItemStack stack) {
        return (this.isFuel(stack) && fuelType != FuelType.LIQUID) || (isBucket(stack) && fuelType != FuelType.SOLID);
    }

    public int getMaxStackSize(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.is(Items.BUCKET);
    }

    private boolean isFuel(ItemStack itemStack) {
        return ForgeHooks.getBurnTime(itemStack,recipeType) > 0;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public enum FuelType {
        BOTH,
        SOLID,
        LIQUID
    }

}
