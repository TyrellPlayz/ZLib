package com.tyrellplayz.zlib.network.message;

import com.tyrellplayz.zlib.ZLib;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public abstract class HandshakeMessage<T> implements Message<T> ,IntSupplier {

    public static final Marker HANDSHAKE = MarkerManager.getMarker("ZLIB_HANDSHAKE");

    private int loginIndex;

    public HandshakeMessage() {
        super();
    }

    public void setLoginIndex(int loginIndex) {
        this.loginIndex = loginIndex;
    }

    public int getLoginIndex() {
        return loginIndex;
    }

    @Override
    public int getAsInt() {
        return getLoginIndex();
    }

    public static class ClientToServerAcknowledge extends HandshakeMessage<ClientToServerAcknowledge> {

        public ClientToServerAcknowledge() {
            super();
        }

        @Override
        public void writePacket(ClientToServerAcknowledge message, FriendlyByteBuf buf) {

        }

        @Override
        public ClientToServerAcknowledge readPacket(FriendlyByteBuf buf) {
            return new ClientToServerAcknowledge();
        }

        @Override
        public void handlePacket(ClientToServerAcknowledge message, Supplier<NetworkEvent.Context> context) {
            ZLib.LOGGER.debug(HANDSHAKE, "Server received acknowledge from client");
            context.get().setPacketHandled(true);
        }

    }

}
