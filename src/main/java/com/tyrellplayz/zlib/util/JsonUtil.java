package com.tyrellplayz.zlib.util;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private JsonUtil() {}

    /**
     * Serializes an object into a {@link JsonObject}.
     * @param object The object to be serialized.
     */
    public static JsonObject serialize(Object object) {
        return serialize(new Gson(),object);
    }

    /**
     * Serializes an object into a {@link JsonObject}.
     * @param object The object to be serialized.
     */
    public static JsonObject serialize(Gson gson, Object object) {
        JsonParser parser = new JsonParser();
        return parser.parse(gson.toJson(object)).getAsJsonObject();
    }

    /**
     * Deserializes a {@link JsonObject} into the given class.
     * @param jsonObject The object to be serialized.
     * @param type The class of T.
     */
    public static <T> T deserialize(JsonObject jsonObject, Class<T> type) {
        return deserialize(new Gson(),jsonObject,type);
    }

    /**
     * Deserializes a {@link JsonObject} into the given class.
     * @param jsonObject The object to be serialized.
     * @param type The class of T.
     */
    public static <T> T deserialize(Gson gson,JsonObject jsonObject, Class<T> type) {
        return gson.fromJson(jsonObject,type);
    }

    public static String getStringOrDefault(JsonObject object, String key, String def) {
        JsonElement jsonElement = object.get(key);
        if(jsonElement == null) return def;
        return jsonElement.getAsString();
    }

    public static int getIntOrDefault(JsonObject object, String key, int def) {
        JsonElement jsonElement = object.get(key);
        if(jsonElement == null) return def;
        return jsonElement.getAsInt();
    }

    public static boolean getBoolOrDefault(JsonObject object, String key, boolean def) {
        JsonElement jsonElement = object.get(key);
        if(jsonElement == null) return def;
        return jsonElement.getAsBoolean();
    }

    public static List<String> getListOrDefault(JsonObject object, String key, List<String> def) {
        JsonElement jsonElement = object.get(key);
        if(jsonElement == null) return def;
        if(!jsonElement.isJsonArray()) return def;
        List<String> strings = new ArrayList<>();
        jsonElement.getAsJsonArray().forEach(jsonElement1 -> strings.add(jsonElement1.getAsString()));
        return strings;
    }

    public static ResourceLocation getResourceLocationOrDefault(JsonObject object, String key, ResourceLocation resourceLocation) {
        JsonElement jsonElement = object.get(key);
        if(jsonElement == null) return resourceLocation;
        return new ResourceLocation(jsonElement.getAsString());
    }

    public static JsonObject loadJson(File file) throws IOException, JsonParseException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return JsonParser.parseReader(reader).getAsJsonObject();
    }

    public static JsonObject loadJson(InputStream inputStream) throws JsonParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return JsonParser.parseReader(reader).getAsJsonObject();
    }

    public static JsonObject loadJson(String s) throws JsonParseException {
        return JsonParser.parseString(s).getAsJsonObject();
    }

    public static void saveJson(JsonObject jsonObject, File file) throws IOException{
        FileWriter fileWriter = new FileWriter(file);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        fileWriter.write(gson.toJson(jsonObject));
        fileWriter.flush();
    }

}
