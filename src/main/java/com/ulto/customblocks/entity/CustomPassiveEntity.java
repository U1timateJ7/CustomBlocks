package com.ulto.customblocks.entity;

import com.google.gson.JsonObject;
import com.ulto.customblocks.EntityGenerator;
import com.ulto.customblocks.event.Events;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomPassiveEntity extends AgeableMob {
    private JsonObject entity = new JsonObject();

    public CustomPassiveEntity(EntityType<? extends AgeableMob> entityType, Level world, JsonObject entity) {
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

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }
}
