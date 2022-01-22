package com.tyrellplayz.zlib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlayerUtil {

    private PlayerUtil() {}

    @OnlyIn(Dist.CLIENT)
    public static void openScreen(Player player, Screen screen) {
        if(!ServerUtil.isServerLevel(player.level)) Minecraft.getInstance().setScreen(screen);
    }

}
