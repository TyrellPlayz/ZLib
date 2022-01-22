package com.tyrellplayz.zlib.util;

import net.minecraft.world.level.ItemLike;

public class ItemUtil {

    private ItemUtil() {}

    public static String simpleItemName(ItemLike item) {
        return item.asItem().getRegistryName().getPath();
    }

}
