package tyrellplayz.zlib.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class Message<T extends Message<T>> {

    private final String name;

    public Message(String name) {
        this.name = name;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public abstract void writePacket(FriendlyByteBuf buf);

    /**
     * Reads the raw packet data from the data stream.
     */
    public abstract T readPacket(FriendlyByteBuf buf);

    /**
     * Handles the packet when received.
     */
    public abstract BiConsumer<T,Supplier<NetworkEvent.Context>> handlePacket();
    
    public String getName() {
        return name;
    }

}
