package com.ulto.customblocks;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.CatVariantTags;
import net.minecraft.world.entity.animal.CatVariant;

public class CatVariantGenerator {
    public static boolean add(JsonObject catVariant) {
        if (catVariant.has("namespace") && catVariant.has("id")) {
            ResourceLocation id = new ResourceLocation(catVariant.get("namespace").getAsString(), catVariant.get("id").getAsString());
            ResourceLocation texture = new ResourceLocation(id.getNamespace(), "textures/entity/cat/" + id.getPath() + ".png");
            CatVariant CAT_VARIANT = Registry.register(Registry.CAT_VARIANT, id, new CatVariant(texture));
            TagGenerator.add(TagGenerator.generateCustomTagObject(CatVariantTags.DEFAULT_SPAWNS.location(), "cat_variant", id));
            return true;
        }
        return false;
    }
}
