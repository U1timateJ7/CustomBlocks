package com.ulto.customblocks.event.global;

import com.google.gson.JsonObject;
import com.ulto.customblocks.event.Events;
import com.ulto.customblocks.event.global.callbacks.EntityTickCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class GlobalEvents {
    public static boolean register(JsonObject globalEvent) {
        if (globalEvent.has("event")) {
            JsonObject event = globalEvent.getAsJsonObject("event");
            if (event.has("id") && event.has("on_execute")) {
                switch (event.get("id").getAsString()) {
                    case "on_block_broken" -> {
                        if (event.has("block")) {
                            Block block = Registry.BLOCK.get(new Identifier(event.get("block").getAsString()));
                            PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
                                if (state.getBlock() == block) {
                                    Events.playBlockEvent(state, pos, world, Map.of("entity", player), event.getAsJsonObject("on_execute"));
                                }
                            });
                        } else {
                            PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> Events.playBlockEvent(state, pos, world, Map.of("entity", player), event.getAsJsonObject("on_execute")));
                        }
                    }
                    case "on_player_respawn" -> ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> Events.playEvent(Map.of("entity", newPlayer, "x", oldPlayer.getX(), "y", oldPlayer.getY(), "z", oldPlayer.getZ(), "world", oldPlayer.world), event.getAsJsonObject("on_execute")));
                    case "on_entity_killed_another" -> ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> Events.playEvent(Map.of("entity", killedEntity, "x", killedEntity.getX(), "y", killedEntity.getY(), "z", killedEntity.getZ(), "world", world, "sourceentity", entity), event.getAsJsonObject("on_execute")));
                    case "on_world_tick" -> ServerTickEvents.END_WORLD_TICK.register((world) -> Events.playEvent(Map.of("world", world), event.getAsJsonObject("on_execute")));
                    case "on_entity_tick" -> EntityTickCallback.EVENT.register((entity, world) -> Events.playEvent(Map.of("entity", entity, "x", entity.getX(), "y", entity.getY(), "z", entity.getZ(), "world", world), event.getAsJsonObject("on_execute")));
                    case "on_player_tick" -> EntityTickCallback.PLAYER.register((entity, world) -> Events.playEvent(Map.of("entity", entity, "x", entity.getX(), "y", entity.getY(), "z", entity.getZ(), "world", world), event.getAsJsonObject("on_execute")));
                }
                return true;
            }
        }
        return false;
    }
}
