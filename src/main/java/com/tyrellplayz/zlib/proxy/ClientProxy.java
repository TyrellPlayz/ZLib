package com.tyrellplayz.zlib.proxy;

import com.tyrellplayz.zlib.client.ClientEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements ModProxy {

    @Override
    public void onClientSetup(FMLClientSetupEvent fmlClientSetupEvent) {

        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
    }
}
