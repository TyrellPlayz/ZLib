package com.tyrellplayz.zlib.network;

import com.tyrellplayz.zlib.network.message.HandshakeMessage;
import com.tyrellplayz.zlib.network.message.Message;
import com.tyrellplayz.zlib.network.message.PlayMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class NetworkManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private final AtomicInteger idCount = new AtomicInteger(1);

    public final String protocolVersion;

    private final SimpleChannel PLAY_CHANNEL;
    private final SimpleChannel HANDSHAKE_CHANNEL;

    private final boolean requiresClient;
    private final boolean requiresServer;

    public NetworkManager(String modId, String protocolVersion) {
        this(modId,protocolVersion,true,true);
    }

    public NetworkManager(String modId, String protocolVersion, boolean requiresClient, boolean requiresServer) {
        this.protocolVersion = protocolVersion;
        this.requiresClient = requiresClient;
        this.requiresServer = requiresServer;
        this.PLAY_CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(modId,"play"))
                .networkProtocolVersion(() -> protocolVersion)
                .clientAcceptedVersions(s -> ignoresServer() || protocolVersion.equals(s))
                .serverAcceptedVersions(s -> ignoresClient() || protocolVersion.equals(s))
                .simpleChannel();

        this.HANDSHAKE_CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(modId,"handshake"))
                .networkProtocolVersion(() -> protocolVersion)
                .clientAcceptedVersions(s -> ignoresServer() || protocolVersion.equals(s))
                .serverAcceptedVersions(s -> ignoresClient() || protocolVersion.equals(s))
                .simpleChannel();

        registerAcknowledgeMessage();
    }

    public <MSG extends PlayMessage<MSG>> void registerPlayMessage(Class<MSG> messageClass) {
        registerPlayMessage(messageClass,null);
    }

    public <MSG extends PlayMessage<MSG>> void registerPlayMessage(Class<MSG> messageClass, @Nullable NetworkDirection networkDirection) {
        try {
            Constructor<MSG> constructor = messageClass.getDeclaredConstructor();
            MSG message = constructor.newInstance();
            PLAY_CHANNEL.registerMessage(idCount.getAndIncrement(), messageClass,message::writePacket,message::readPacket, message::handlePacket, Optional.ofNullable(networkDirection));
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("%s does not have an empty parameter constructor",messageClass.getName()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("The constructor of %s cannot be accessed. Ensure it is public",messageClass.getName()));
        }
        catch (InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public <MSG extends HandshakeMessage<MSG>> void registerHandshakeMessage(Class<MSG> messageClass) {
        registerHandshakeMessage(messageClass,null);
    }

    public <MSG extends HandshakeMessage<MSG>> void registerHandshakeMessage(Class<MSG> messageClass, @Nullable Function<Boolean, List<Pair<String,MSG>>> messages) {
        try{
            Constructor<MSG> constructor = messageClass.getDeclaredConstructor();
            MSG message = constructor.newInstance();

            SimpleChannel.MessageBuilder<MSG> builder = HANDSHAKE_CHANNEL.messageBuilder(messageClass, idCount.getAndIncrement());
            builder.loginIndex(HandshakeMessage::getLoginIndex,HandshakeMessage::setLoginIndex);
            builder.encoder(message::writePacket).decoder(message::readPacket).consumer(message::handlePacket);
            if(messages != null) {
                builder.buildLoginPacketList(messages);
            }else {
                builder.markAsLoginPacket();
            }
            builder.add();

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(String.format("%s does not have an empty parameter constructor",messageClass.getName()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("The constructor of %s cannot be accessed. Ensure it is public",messageClass.getName()));
        }
        catch (InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private <MSG extends HandshakeMessage<MSG>> void registerAcknowledgeMessage() {
        HandshakeMessage.ClientToServerAcknowledge message = new HandshakeMessage.ClientToServerAcknowledge();
        HANDSHAKE_CHANNEL.messageBuilder(HandshakeMessage.ClientToServerAcknowledge.class, idCount.getAndIncrement())
                .loginIndex(HandshakeMessage::getLoginIndex,HandshakeMessage::setLoginIndex)
                .encoder(message::writePacket)
                .decoder(message::readPacket)
                .consumer(HandshakeHandler.indexFirst((handler, acknowledge, context) -> message.handlePacket(acknowledge,context)))
                .add();
    }

    public SimpleChannel getPlayChannel() {
        return PLAY_CHANNEL;
    }

    public SimpleChannel getHandshakeChannel() {
        return HANDSHAKE_CHANNEL;
    }

    public boolean ignoresClient() {
        return !requiresClient;
    }

    public boolean ignoresServer() {
        return !requiresServer;
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
