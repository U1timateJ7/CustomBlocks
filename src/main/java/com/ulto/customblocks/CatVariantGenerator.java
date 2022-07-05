package com.ulto.customblocks;

import com.google.gson.JsonObject;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.tag.CatVariantTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CatVariantGenerator {
    public static boolean add(JsonObject catVariant) {
        if (catVariant.has("namespace") && catVariant.has("id")) {
            Identifier id = new Identifier(catVariant.get("namespace").getAsString(), catVariant.get("id").getAsString());
            Identifier texture = new Identifier(id.getNamespace(), "textures/entity/cat/" + id.getPath() + ".png");
            CatVariant CAT_VARIANT = Registry.register(Registry.CAT_VARIANT, id, new CatVariant(texture));
            TagGenerator.add(TagGenerator.generateCustomTagObject(CatVariantTags.DEFAULT_SPAWNS.id(), "cat_variant", id));
            return true;
        }
        return false;
    }
}
