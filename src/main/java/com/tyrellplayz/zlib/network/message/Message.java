package com.tyrellplayz.zlib.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface Message<T> {

    /**
     * Writes the raw packet data to the data stream.
     */
    void writePacket(T message, FriendlyByteBuf buf);

    /**
     * Reads the raw packet data from the data stream.
     */
    T readPacket(FriendlyByteBuf buf);

    /**
     * Handles the packet when received.
     */
    void handlePacket(T message, Supplier<NetworkEvent.Context> supplier);

}
