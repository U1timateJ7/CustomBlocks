package com.ulto.customblocks.event.global;

import com.google.gson.JsonObject;
import com.ulto.customblocks.event.Events;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class GlobalEvents {
    public static boolean register(JsonObject globalEvent) {
        if (globalEvent.has("event")) {
            JsonObject event = globalEvent.getAsJsonObject("event");
            if (event.has("id") && event.has("on_execute")) {
                @Mod.EventBusSubscriber
                class Event {
                    @SubscribeEvent
                    public void onBlockBroken(BlockEvent.BreakEvent forgeEvent) {
                        if (event.get("id").getAsString().equals("on_block_broken")) {
                            if (event.has("block")) {
                                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(event.get("block").getAsString()));
                                if (forgeEvent.getState().getBlock() == block) {
                                    Events.playBlockEvent(forgeEvent.getState(), forgeEvent.getPos(), (Level) forgeEvent.getWorld(), Map.of("entity", forgeEvent.getPlayer()), event.getAsJsonObject("on_execute"));
                                }
                            } else {
                                Events.playBlockEvent(forgeEvent.getState(), forgeEvent.getPos(), (Level) forgeEvent.getWorld(), Map.of("entity", forgeEvent.getPlayer()), event.getAsJsonObject("on_execute"));
                            }
                        }
                    }

                    @SubscribeEvent
                    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent forgeEvent) {
                        if (event.get("id").getAsString().equals("on_player_respawn")) {
                            Player player = forgeEvent.getPlayer();
                            Events.playEvent(Map.of("entity", player, "x", player.getX(), "y", player.getY(), "z", player.getZ(), "world", player.level), event.getAsJsonObject("on_execute"));
                        }
                    }

                    @SubscribeEvent
                    public void onEntityKilledAnother(LivingDeathEvent forgeEvent) {
                        if (event.get("id").getAsString().equals("on_entity_killed_another")) {
                            Entity killedEntity = forgeEvent.getEntityLiving();
                            Entity entity = forgeEvent.getSource().getEntity();
                            Events.playEvent(Map.of("entity", killedEntity, "x", killedEntity.getX(), "y", killedEntity.getY(), "z", killedEntity.getZ(), "world", killedEntity.level, "sourceentity", entity), event.getAsJsonObject("on_execute"));
                        }
                    }

                    @SubscribeEvent
                    public void onWorldTick(TickEvent.WorldTickEvent forgeEvent) {
                        if (event.get("id").getAsString().equals("on_world_tick")) {
                            Events.playEvent(Map.of("world", forgeEvent.world), event.getAsJsonObject("on_execute"));
                        }
                    }

                    @SubscribeEvent
                    public void onEntityTick(LivingEvent.LivingUpdateEvent forgeEvent) {
                        if (event.get("id").getAsString().equals("on_entity_tick")) {
                            Entity entity = forgeEvent.getEntityLiving();
                            Events.playEvent(Map.of("entity", entity, "x", entity.getX(), "y", entity.getY(), "z", entity.getZ(), "world", entity.level), event.getAsJsonObject("on_execute"));
                        }
                    }

                    @SubscribeEvent
                    public void onPlayerTick(TickEvent.PlayerTickEvent forgeEvent) {
                        if (event.get("id").getAsString().equals("on_player_tick")) {
                            Player entity = forgeEvent.player;
                            Events.playEvent(Map.of("entity", entity, "x", entity.getX(), "y", entity.getY(), "z", entity.getZ(), "world", entity.level), event.getAsJsonObject("on_execute"));
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
