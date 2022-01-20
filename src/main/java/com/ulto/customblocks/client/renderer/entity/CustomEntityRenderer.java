package com.ulto.customblocks.client.renderer.entity;

import com.google.gson.JsonObject;
import com.ulto.customblocks.entity.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CustomEntityRenderer<E extends PathAwareEntity, M extends EntityModel<E>> extends LivingEntityRenderer<E, M> {
    public CustomEntityRenderer(EntityRendererFactory.Context ctx, M model, float shadowRadius, JsonObject entity) {
        super(ctx, model, shadowRadius);
    }

    @Override
    public Identifier getTexture(E entity) {
        if (entity instanceof CustomEntity customEntity) return customEntity.getTexture();
        if (entity instanceof CustomPassiveEntity customEntity) return customEntity.getTexture();
        if (entity instanceof CustomHostileEntity customEntity) return customEntity.getTexture();
        if (entity instanceof CustomIllagerEntity customEntity) return customEntity.getTexture();
        if (entity instanceof CustomWaterEntity customEntity) return customEntity.getTexture();
        return new Identifier("pish:posh");
    }
}
