package com.ulto.customblocks.entity;

import com.google.gson.JsonObject;
import com.ulto.customblocks.EntityGenerator;
import com.ulto.customblocks.event.Events;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class CustomHostileEntity extends Monster {
    private JsonObject entity = new JsonObject();

    public CustomHostileEntity(EntityType<? extends Monster> entityType, Level world, JsonObject entity) {
        super(entityType, world);
        if (entity != null) this.entity = entity;
        if (!world.isClientSide) {
            EntityGenerator.addGoals(goalSelector, targetSelector, this.entity, this);
        }
    }

    public ResourceLocation getTexture() {
        return entity.has("texture") ? new ResourceLocation(entity.get("texture").getAsString()) : new ResourceLocation(entity.get("namespace").getAsString(), "textures/entity/" + entity.get("id").getAsString() + ".png");
    }

    @Override
    public void customServerAiStep() {
        if (entity.has("on_tick")) Events.playEntityEvent(this, new HashMap<>(), entity.getAsJsonObject("on_tick"));
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (entity.has("on_death")) Events.playEntityEvent(this, new HashMap<>(), entity.getAsJsonObject("on_death"));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (entity.has("on_use")) return Events.playEntityEvent(this, Map.of("sourceentity", player), entity.getAsJsonObject("on_use"));
        return super.mobInteract(player, hand);
    }

    @Override
    protected void onInsideBlock(BlockState state) {
        if (entity.has("on_use")) Events.playEntityEvent(this, Map.of("blockstate", state), entity.getAsJsonObject("on_use"));
    }
}
