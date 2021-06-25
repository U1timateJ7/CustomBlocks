package com.ulto.customblocks;

import com.google.gson.JsonObject;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PaintingGenerator {
    public static boolean add(JsonObject painting) {
        if (painting.has("width") && painting.has("height") && painting.has("namespace") && painting.has("id")) {
            int width = painting.get("width").getAsInt();
            int height = painting.get("height").getAsInt();
            String namespace = painting.get("namespace").getAsString();
            String id = painting.get("id").getAsString();
            PaintingMotive PAINTING = new PaintingMotive(width, height);
            Registry.register(Registry.PAINTING_MOTIVE, new Identifier(namespace, id), PAINTING);
            return true;
        }
        return false;
    }
}
