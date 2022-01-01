package tyrellplayz.zlib.util;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

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

}
