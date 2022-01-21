package com.ulto.customblocks.client;

import com.google.gson.JsonObject;
import com.ulto.customblocks.EntityGenerator;
import com.ulto.customblocks.client.renderer.entity.CustomEntityRenderer;
import com.ulto.customblocks.client.renderer.entity.model.CustomEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientEntityGenerator {
    public static boolean addClient(JsonObject entity) {
        if (entity.has("model_name")) {
            EntityModelLayer entityModelLayer = new EntityModelLayer(new Identifier(entity.get("namespace").getAsString(), entity.get("id").getAsString()), "main");
            EntityModelLayerRegistry.registerModelLayer(entityModelLayer, () -> CustomEntityModel.getTexturedModelData(EntityGenerator.entityModels.get(entity.get("model_name").getAsString())));

            EntityRendererRegistry.register(EntityGenerator.entities.get(new Identifier(entity.get("namespace").getAsString(), entity.get("id").getAsString())), (context) -> new CustomEntityRenderer<>(context, new CustomEntityModel<>(context.getPart(entityModelLayer), EntityGenerator.entityModels.get(entity.get("model_name").getAsString()), entity), entity.has("shadow_size") ? entity.get("shadow_size").getAsFloat() : 0.5f, entity));
            return true;
        }
        return false;
    }
}
