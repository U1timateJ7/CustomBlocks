package com.ulto.customblocks.client;

import com.google.gson.JsonObject;
import com.ulto.customblocks.EntityGenerator;
import com.ulto.customblocks.client.renderer.entity.CustomEntityRenderer;
import com.ulto.customblocks.client.renderer.entity.model.CustomEntityModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientEntityGenerator {
    public static Map<String, JsonObject> entityModels = new HashMap<>();

    public static boolean addClient(JsonObject entity) {
        if (entity.has("model_name")) {
            ResourceLocation id = new ResourceLocation(entity.get("namespace").getAsString(), entity.get("id").getAsString());
            EntityRenderers.register(EntityGenerator.entities.get(id), context -> new CustomEntityRenderer<>(context, new CustomEntityModel<>(CustomEntityModel.getTexturedModelData(entityModels.get(entity.get("model_name").getAsString())).bakeRoot(), entityModels.get(entity.get("model_name").getAsString()), entity), entity.has("shadow_radius") ? entity.get("shadow_radius").getAsFloat() : 0.5f, entity));
            return true;
        }
        return false;
    }
}
