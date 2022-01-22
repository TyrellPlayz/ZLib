package com.tyrellplayz.zlib.util;

import com.google.common.collect.Lists;
import com.tyrellplayz.zlib.ZLib;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.*;

public class StructureUtil {

    private StructureUtil() {}

    /**
     * Get the structure from file.
     * @param level The server level that contains the structure file in its data folder.
     * @param location The resource location of the structure within the data folder.
     * @return The structure or null if no structure file was found.
     */
    @Nullable
    public static StructureTemplate getStructureTemplate(ServerLevel level, ResourceLocation location) {
        StructureManager structureManager = level.getStructureManager();
        Optional<StructureTemplate> optional = structureManager.get(location);

        if (optional.isPresent()) {
            return optional.get();
        }else {
            ZLib.LOGGER.error("Could not find structure file " + location + ".");
            return null;
        }
    }

    public static boolean structureInLevel(ServerLevel level, StructureTemplate template, BlockPos pos, Direction direction) {
        return structureInLevel(level,template,pos,new StructurePlaceSettings().setRotation(Util.getRotation(direction)));
    }

    /**
     * Check if the structure has been built in the world at the given position.
     * @param level The server level that contains the in world structure.
     * @param template The structure to check that is exists within the level.
     * @param pos The location of the in level structure from the font bottom left block.
     * @return True if both structures match, false if not.
     */
    public static boolean structureInLevel(ServerLevel level, StructureTemplate template, BlockPos pos, StructurePlaceSettings placeSettings) {
        List<StructureTemplate.StructureBlockInfo> structureBlockInfos = getBlockInfoFromTemplate(pos,template,placeSettings,true);
        for (StructureTemplate.StructureBlockInfo structureBlockInfo : structureBlockInfos) {
            Block inWorldBlock = level.getBlockState(structureBlockInfo.pos).getBlock();
            if(!Objects.equals(inWorldBlock.getRegistryName(), structureBlockInfo.state.getBlock().getRegistryName())) return false;
        }
        return true;
    }

    public static boolean placeInWorld(ServerLevel level, StructureTemplate template, BlockPos pos, StructurePlaceSettings placeSettings) {
        template.placeInWorld(level,pos,pos,placeSettings,new Random(),3);
        return true;
    }

    public static List<StructureTemplate.StructureBlockInfo> getBlockInfoFromTemplate(BlockPos blockPos, StructureTemplate template, StructurePlaceSettings placeSettings, boolean relativePos) {
        List<StructureTemplate.StructureBlockInfo> list = Lists.newArrayList();
        BoundingBox boundingbox = placeSettings.getBoundingBox();
        if(template.palettes.isEmpty()) {
            return Collections.emptyList();
        }else {
            for(StructureTemplate.StructureBlockInfo blockInfo : placeSettings.getRandomPalette(template.palettes, blockPos).blocks()) {
                BlockPos pos = relativePos ? StructureTemplate.calculateRelativePosition(placeSettings, blockInfo.pos).offset(blockPos) : blockInfo.pos;
                if (boundingbox == null || boundingbox.isInside(pos)) {
                    list.add(new StructureTemplate.StructureBlockInfo(pos, blockInfo.state.rotate(placeSettings.getRotation()), blockInfo.nbt));
                }
            }
            return list;
        }
    }

    public static List<BlockPos> getBlockLocations(BlockPos blockPos, StructureTemplate template, StructurePlaceSettings placeSettings, boolean relativePos) {
        List<BlockPos> list = Lists.newArrayList();
        BoundingBox boundingBox = placeSettings.getBoundingBox();
        if(template.palettes.isEmpty()) {
            return Collections.emptyList();
        }else {
            for (StructureTemplate.StructureBlockInfo blockInfo : placeSettings.getRandomPalette(template.palettes, blockPos).blocks()) {
                BlockPos pos = relativePos ? StructureTemplate.calculateRelativePosition(placeSettings,blockInfo.pos).offset(blockPos) : blockInfo.pos;
                if(boundingBox == null || boundingBox.isInside(pos)) list.add(pos);
            }
        }
        return list;
    }

}
