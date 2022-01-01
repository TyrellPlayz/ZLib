package tyrellplayz.zlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tyrellplayz.zlib.network.message.HandshakeMessage;
import tyrellplayz.zlib.network.message.Message;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetworkManager {
    private static final Logger LOGGER = LogManager.getLogger();

    public final String protocolVersion;

    private final SimpleChannel PLAY_CHANNEL;
    private final SimpleChannel HANDSHAKE_CHANNEL;

    private int messageId;

    public NetworkManager(String modId) {
        this(modId,"1");
    }

    public NetworkManager(String modId, String protocolVersion) {
        this.protocolVersion = protocolVersion;
        this.PLAY_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(modId,"play"), () -> protocolVersion, s -> true, s -> true);
        this.HANDSHAKE_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(modId,"handshake"),() -> protocolVersion,s -> true,s -> true);

        registerHandshakeMessage(99, HandshakeMessage.ClientToServerAcknowledge.class);
    }

    public <T extends Message<T>> void registerPlayMessage(Class<T> messageType, LogicalSide logicalSide) {
        try {
            T instance = messageType.newInstance();
            registerPlayMessage(messageType, Message::writePacket, instance::readPacket, instance.handlePacket(),logicalSide);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T extends Message<T>> void registerPlayMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> handler, LogicalSide logicalSide) {
        PLAY_CHANNEL.registerMessage(messageId++, messageType,encoder,decoder,(t, contextSupplier) -> {
            if(contextSupplier.get().getDirection().getReceptionSide() != logicalSide)
                throw new RuntimeException("Attempted to handle message "+messageType.getSimpleName()+" on the wrong side");
            handler.accept(t,contextSupplier);
        });
    }

    public <T extends HandshakeMessage<T>> void registerHandshakeMessage(int index, Class<T> messageType) {
        try {
            T instance = messageType.newInstance();
            registerHandshakeMessage(index,messageType, HandshakeMessage::writePacket, instance::readPacket, instance.handlePacket(),instance.isLoginPacket());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T extends HandshakeMessage<T>> void registerHandshakeMessage(int index, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> handler, boolean isLoginPacket) {
        if(isLoginPacket) {
            HANDSHAKE_CHANNEL.messageBuilder(messageType,index)
                    .loginIndex(HandshakeMessage::getLoginIndex,HandshakeMessage::setLoginIndex)
                    .encoder(encoder)
                    .decoder(decoder)
                    .markAsLoginPacket()
                    .consumer(handler).add();
        }else {
            HANDSHAKE_CHANNEL.messageBuilder(messageType,index)
                    .loginIndex(HandshakeMessage::getLoginIndex,HandshakeMessage::setLoginIndex)
                    .encoder(encoder)
                    .decoder(decoder)
                    .consumer(handler).add();
        }
    }

    public SimpleChannel getPlayerChannel() {
        return PLAY_CHANNEL;
    }

    public SimpleChannel getHandshakeChannel() {
        return HANDSHAKE_CHANNEL;
    }

    /**
     * Sends a packet to the server.  Must only be ran on Client-side.
     * @param message The message
     */
    @OnlyIn(Dist.CLIENT)
    public <T extends Message<T>> void sendToServer(Message<T> message) {
        PLAY_CHANNEL.sendToServer(message);
    }

    /**
     * Sends a packet to a specific player. Must only be ran on server-side.
     * @param message The message.
     * @param player The player.
     */
    public <T extends Message<T>> void sendTo(Message<T> message, ServerPlayer player) {
        if(!(player instanceof FakePlayer)) {
            if(isServerSide()) PLAY_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),message);
        }
    }

    /**
     * Sends the packet to all players tracking the given chunk. Must only be ran on server-side.
     * @param message The message.
     * @param chunk The chunk.
     * @param <T>
     */
    public <T extends Message<T>> void sendToChunk(Message<T> message, LevelChunk chunk) {
        if(isServerSide()) PLAY_CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk),message);
    }

    /**
     * Sends the packet to all players connected to the server. Must only be ran on server-side.
     * @param message The message
     */
    public <T extends Message<T>> void sendToAll(Message<T> message) {
        if(isServerSide()) PLAY_CHANNEL.send(PacketDistributor.ALL.noArg(),message);
    }

    public boolean isServerSide() {
        if(Thread.currentThread().getName().equals("Render thread")) {
            LOGGER.error("Cannot send packets from a server if you are on a client.");
            return false;
        }
        return true;
    }

}
