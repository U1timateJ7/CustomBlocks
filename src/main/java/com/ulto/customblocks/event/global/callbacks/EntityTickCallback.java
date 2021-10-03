package com.ulto.customblocks.event.global.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface EntityTickCallback {
    /**
     * Called before the entity ticks.
     */
    Event<EntityTickCallback> EVENT = EventFactory.createArrayBacked(EntityTickCallback.class,
            (listeners) -> (entity, world) -> {
                for (EntityTickCallback listener : listeners) {
                    listener.onEntityTick(entity, world);
                }
            });

    void onEntityTick(Entity entity, World world);

    /**
     * Called before a player ticks.
     */
    Event<Player> PLAYER = EventFactory.createArrayBacked(Player.class,
            (listeners) -> (player, world) -> {
                for (Player listener : listeners) {
                    listener.onPlayerTick(player, world);
                }
            });

    @FunctionalInterface
    interface Player {
        void onPlayerTick(PlayerEntity player, World world);
    }
}
