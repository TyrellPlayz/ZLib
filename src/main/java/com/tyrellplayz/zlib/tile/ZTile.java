package com.tyrellplayz.zlib.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.INameable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ZTile extends TileEntity implements INameable {

    private ITextComponent customName;

    public ZTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public ITextComponent getName() {
        return this.customName != null ? this.customName : new TranslationTextComponent(getTranslationKey(super.getType().getRegistryName()));
    }

    public String getTranslationKey(ResourceLocation registryName) {
        return "container."+registryName.getPath();
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return this.customName;
    }

    public void setCustomName(ITextComponent customName) {
        this.customName = customName;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) { }

    public void onRemoved(World worldIn, BlockPos pos) {}

    @Override
    public void read(BlockState blockState, CompoundNBT compound) {
        super.read(blockState, compound);
        if (compound.contains("CustomName", 8)) this.customName = ITextComponent.Serializer.getComponentFromJson(compound.getString("CustomName"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (this.customName != null) compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
        return compound;
    }
}
