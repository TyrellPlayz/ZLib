package com.tyrellplayz.zlib.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class ServerUtil {

    private ServerUtil() {}

    public static boolean isServerLevel(Level level) {
        return level instanceof ServerLevel;
    }

}
