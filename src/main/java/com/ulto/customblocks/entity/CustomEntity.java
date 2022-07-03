package com.ulto.customblocks.entity;

import com.google.gson.JsonObject;
import com.ulto.customblocks.EntityGenerator;
import com.ulto.customblocks.event.Events;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class CustomEntity extends PathAwareEntity {
    private JsonObject entity = new JsonObject();

    public CustomEntity(EntityType<? extends PathAwareEntity> entityType, World world, JsonObject entity) {
        super(entityType, world);
        if (entity != null) this.entity = entity;
        if (!world.isClient) {
            EntityGenerator.addGoals(goalSelector, targetSelector, this.entity, this);
        }
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return entity.has("eye_height") ? entity.get("eye_height").getAsFloat() : super.getActiveEyeHeight(pose, dimensions);
    }

    public Identifier getTexture() {
        return entity.has("texture") ? new Identifier(entity.get("texture").getAsString()) : new Identifier(entity.get("namespace").getAsString(), "textures/entity/" + entity.get("id").getAsString() + ".png");
    }

    @Override
    protected void mobTick() {
        if (entity.has("on_tick")) Events.playEntityEvent(this, new HashMap<>(), entity.getAsJsonObject("on_tick"));
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (entity.has("on_death")) Events.playEntityEvent(this, new HashMap<>(), entity.getAsJsonObject("on_death"));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (entity.has("on_use")) return Events.playEntityEvent(this, Map.of("sourceentity", player), entity.getAsJsonObject("on_use"));
        return super.interactMob(player, hand);
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        if (entity.has("on_use")) Events.playEntityEvent(this, Map.of("blockstate", state), entity.getAsJsonObject("on_use"));
    }
}
