package com.ulto.customblocks;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.ForgeRegistries;

public class PaintingGenerator {
    public static boolean add(JsonObject painting) {
        if (painting.has("width") && painting.has("height") && painting.has("namespace") && painting.has("id")) {
            int width = painting.get("width").getAsInt();
            int height = painting.get("height").getAsInt();
            String namespace = painting.get("namespace").getAsString();
            String id = painting.get("id").getAsString();
            PaintingVariant PAINTING = new PaintingVariant(width, height);
            ForgeRegistries.PAINTING_VARIANTS.register(new ResourceLocation(namespace, id), PAINTING);
            return true;
        }
        return false;
    }
}
