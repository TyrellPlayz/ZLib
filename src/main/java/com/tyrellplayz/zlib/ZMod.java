package com.tyrellplayz.zlib;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZMod {

    protected static final Logger LOGGER = LogManager.getLogger();

    public ZMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::onCommonSetup);
        eventBus.addListener(this::onClientSetup);
        eventBus.addListener(this::gatherData);
    }

    public void onCommonSetup(final FMLCommonSetupEvent event) {}

    public void onClientSetup(final FMLClientSetupEvent event) {

    }

    public void gatherData(final GatherDataEvent event) {

    }

    public static Logger getLogger() {
        return LOGGER;
    }

}
