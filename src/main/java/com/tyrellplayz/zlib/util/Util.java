package com.tyrellplayz.zlib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static Rotation getRotation(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return Rotation.NONE;
            }
            case EAST -> {
                return Rotation.CLOCKWISE_90;
            }
            case SOUTH -> {
                return Rotation.CLOCKWISE_180;
            }
            case WEST -> {
                return Rotation.COUNTERCLOCKWISE_90;
            }
        }
        return Rotation.NONE;
    }

    @Nullable
    public static Block getBlock(ResourceLocation location) {
        return ForgeRegistries.BLOCKS.getValue(location);
    }

    public static Date getGameTime() {
        long ticks = Minecraft.getInstance().level.getGameTime();

        int days = (int) (ticks / 24000);
        int hours = (int) ((Math.floor(ticks / 1000.0) + 7) % 24);
        int minutes = (int) Math.floor((ticks % 1000) / 1000.0 * 60);
        return new Date(0, 0,days,hours,minutes);
    }

}
