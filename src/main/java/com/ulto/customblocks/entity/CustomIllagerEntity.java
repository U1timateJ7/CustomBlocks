package com.ulto.customblocks.entity;

import com.google.gson.JsonObject;
import com.ulto.customblocks.EntityGenerator;
import com.ulto.customblocks.event.Events;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class CustomIllagerEntity extends IllagerEntity {
    private JsonObject entity = new JsonObject();

    public CustomIllagerEntity(EntityType<? extends IllagerEntity> entityType, World world, JsonObject entity) {
        super(entityType, world);
        if (entity != null) this.entity = entity;
        if (!world.isClient) {
            EntityGenerator.addGoals(goalSelector, targetSelector, this.entity, this);
        }
    }

    public Identifier getTexture() {
        return entity.has("texture") ? new Identifier(entity.get("texture").getAsString()) : new Identifier(entity.get("namespace").getAsString(), "textures/entity/" + entity.get("id").getAsString() + ".png");
    }

    @Override
    protected void mobTick() {
        if (entity.has("on_tick")) Events.playEntityEvent(this, new HashMap<>(), entity.getAsJsonObject("on_tick"));
    }

    @Override
    public void addBonusForWave(int wave, boolean unused) {

    }

    @Override
    public void onDeath(DamageSource source) {
        if (entity.has("on_death")) Events.playEntityEvent(this, new HashMap<>(), entity.getAsJsonObject("on_death"));
    }

    @Override
    public SoundEvent getCelebratingSound() {
        return entity.has("celebrating_sound") ? Registry.SOUND_EVENT.get(new Identifier(entity.get("celebrating_sound").getAsString())) : SoundEvents.ENTITY_VINDICATOR_CELEBRATE;
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
