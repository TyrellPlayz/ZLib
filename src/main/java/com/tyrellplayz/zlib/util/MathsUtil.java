package com.tyrellplayz.zlib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

public class MathsUtil {

    private MathsUtil() {}

    public static int round(double d) {
        return (int) (d + 0.5D);
    }

    public static int min(int value, int min){
        return Mth.clamp(value,min,Integer.MAX_VALUE);
    }

    public static double min(double value, double min){
        return Mth.clamp(value,min,Double.MAX_VALUE);
    }

    public static int max(int value, int max){
        return Mth.clamp(value,0,max);
    }

    public static double max(double value, double max){
        return Mth.clamp(value,0,max);
    }

    public static double getDistance(BlockPos pos1, BlockPos pos2) {
        return pos1.distToCenterSqr(pos2.getX(),pos2.getY(),pos2.getZ());
        //return Math.sqrt(pos1.distSqr(pos2.getX()+0.5,pos2.getY()+0.5,pos2.getZ()+0.5,true));
    }

    @Nullable
    public static BlockPos getBlockPosPlayerIsLookingAt(Player player){
        HitResult hitResult = player.pick(20.0D, 0.0F, false);
        if(hitResult.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult)hitResult).getBlockPos();
        }
        return null;
    }

}
