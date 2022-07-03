package com.ulto.customblocks.client.renderer.entity;

import com.google.gson.JsonObject;
import com.ulto.customblocks.entity.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CustomEntityRenderer<E extends PathfinderMob, M extends EntityModel<E>> extends MobRenderer<E, M> {
    public CustomEntityRenderer(EntityRendererProvider.Context ctx, M model, float shadowRadius, JsonObject entity) {
        super(ctx, model, shadowRadius);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull E entity) {
        if (entity instanceof CustomEntity customEntity) return customEntity.getTexture();
        if (entity instanceof CustomPassiveEntity customEntity) return customEntity.getTexture();
        if (entity instanceof CustomHostileEntity customEntity) return customEntity.getTexture();
        if (entity instanceof CustomIllagerEntity customEntity) return customEntity.getTexture();
        if (entity instanceof CustomWaterEntity customEntity) return customEntity.getTexture();
        return new ResourceLocation("pish:posh");
    }
}
