package com.tyrellplayz.zlib.client;

import com.tyrellplayz.zlib.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onDebugOverlay(final RenderGameOverlayEvent.Text event){
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.options.renderDebug) {

            if (Config.CLIENT.blockEntityInformation.get()) {
                Entity entity = minecraft.getCameraEntity();

                HitResult block = entity.pick(20.0D, 0.0F, false);

                if(block.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockpos = ((BlockHitResult)block).getBlockPos();
                    BlockEntity blockEntity = minecraft.level.getBlockEntity(blockpos);
                    if(blockEntity != null) {
                        event.getRight().add("");
                        event.getRight().add(ChatFormatting.UNDERLINE + "Targeted Block Entity: " + blockpos.getX() + ", " + blockpos.getY() + ", " + blockpos.getZ());
                        event.getRight().add(String.valueOf(blockEntity.getType().getRegistryName()));
                    }
                }
            }

        }

    }

}
