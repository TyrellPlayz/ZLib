package com.tyrellplayz.zlib;

import com.tyrellplayz.zlib.proxy.ClientProxy;
import com.tyrellplayz.zlib.proxy.CommonProxy;
import com.tyrellplayz.zlib.proxy.ModProxy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ZLib.MOD_ID)
public class ZLib {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "zlib";

    public static ModProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public ZLib() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onCommonSetup);
        eventBus.addListener(this::onClientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
    }

    public void onCommonSetup(final FMLCommonSetupEvent event) {
        proxy.onCommonSetup(event);
    }

    public void onClientSetup(final FMLClientSetupEvent event) {
        proxy.onClientSetup(event);
    }

}
