package com.ulto.customblocks.client.renderer.entity;

import com.google.gson.JsonObject;
import com.ulto.customblocks.entity.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CustomEntityRenderer<E extends PathAwareEntity, M extends EntityModel<E>> extends LivingEntityRenderer<E, M> {
    public CustomEntityRenderer(EntityRenderDispatcher dispatcher, M model, float shadowRadius, JsonObject entity) {
        super(dispatcher, model, shadowRadius);
    }

    @Override
    public Identifier getTexture(E entity) {
        if (entity instanceof CustomEntity) return ((CustomEntity) entity).getTexture();
        if (entity instanceof CustomPassiveEntity) return ((CustomPassiveEntity) entity).getTexture();
        if (entity instanceof CustomHostileEntity) return ((CustomHostileEntity) entity).getTexture();
        if (entity instanceof CustomIllagerEntity) return ((CustomIllagerEntity) entity).getTexture();
        if (entity instanceof CustomWaterEntity) return ((CustomWaterEntity) entity).getTexture();
        return new Identifier("pish:posh");
    }
}
