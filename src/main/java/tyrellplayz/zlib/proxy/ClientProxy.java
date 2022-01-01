package tyrellplayz.zlib.proxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tyrellplayz.zlib.client.ClientEvents;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy {

    @Override
    public void onClientSetup(FMLClientSetupEvent fmlClientSetupEvent) {

        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
    }
}
