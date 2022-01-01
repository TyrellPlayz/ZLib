package tyrellplayz.zlib.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IProxy {

    default void onCommonSetup(FMLCommonSetupEvent fmlCommonSetupEvent) {}

    default void onClientSetup(FMLClientSetupEvent fmlClientSetupEvent) {}

}
