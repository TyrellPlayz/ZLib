package com.tyrellplayz.zlib.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface JsonSerializer<T> {

    T fromJson(ResourceLocation id, JsonObject jsonObject) throws JsonSyntaxException;

    @javax.annotation.Nullable
    T fromNetwork(ResourceLocation id, FriendlyByteBuf buffer);

    void toNetwork(FriendlyByteBuf buffer, T t);

}
