package com.ulto.customblocks;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.registries.ForgeRegistries;

public class PaintingGenerator {
    public static boolean add(JsonObject painting) {
        if (painting.has("width") && painting.has("height") && painting.has("namespace") && painting.has("id")) {
            int width = painting.get("width").getAsInt();
            int height = painting.get("height").getAsInt();
            String namespace = painting.get("namespace").getAsString();
            String id = painting.get("id").getAsString();
            Motive PAINTING = new Motive(width, height).setRegistryName(new ResourceLocation(namespace, id));
            ForgeRegistries.PAINTING_TYPES.register(PAINTING);
            return true;
        }
        return false;
    }
}
