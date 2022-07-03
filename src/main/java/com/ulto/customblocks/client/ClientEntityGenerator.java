package com.ulto.customblocks.client;

import com.google.gson.JsonObject;
import com.ulto.customblocks.EntityGenerator;
import com.ulto.customblocks.client.renderer.entity.CustomEntityRenderer;
import com.ulto.customblocks.client.renderer.entity.model.CustomEntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEntityGenerator {
    public static boolean addClient(JsonObject entity) {
        if (entity.has("model_name")) {
            ResourceLocation id = new ResourceLocation(entity.get("namespace").getAsString(), entity.get("id").getAsString());
            EntityRenderers.register(EntityGenerator.entities.get(id), context -> new CustomEntityRenderer<>(context, new CustomEntityModel<>(CustomEntityModel.getTexturedModelData(EntityGenerator.entityModels.get(entity.get("model_name").getAsString())).bakeRoot(), EntityGenerator.entityModels.get(entity.get("model_name").getAsString()), entity), entity.has("shadow_radius") ? entity.get("shadow_radius").getAsFloat() : 0.5f, entity));
            return true;
        }
        return false;
    }
}
