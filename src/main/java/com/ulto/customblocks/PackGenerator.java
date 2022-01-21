package com.ulto.customblocks;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.BooleanUtils;
import com.ulto.customblocks.util.JsonUtils;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.function.Function;

public class PackGenerator {
    public static void add(JsonObject pack) {
        if (pack.has("wood_pack")) {
            JsonObject woodPack = pack.getAsJsonObject("wood_pack");
            if (woodPack.has("namespace") && woodPack.has("id") && woodPack.has("display_name") && woodPack.has("planks_texture") && woodPack.has("log_top_texture") && woodPack.has("log_side_texture") && woodPack.has("stripped_log_top_texture") && woodPack.has("stripped_log_side_texture") && woodPack.has("door_top_texture") && woodPack.has("door_bottom_texture") && woodPack.has("door_item_texture") && woodPack.has("trapdoor_texture")) {
                JsonObject planks = new JsonObject();
                planks.addProperty("namespace", woodPack.get("namespace").getAsString());
                planks.addProperty("id", woodPack.get("id").getAsString() + "_planks");
                planks.addProperty("display_name", woodPack.get("display_name").getAsString() + " Planks");
                planks.addProperty("material", "wood");
                planks.addProperty("hardness", 2);
                planks.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    planks.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else planks.addProperty("has_gravity", false);
                if (woodPack.has("map_color"))
                    planks.addProperty("map_color", woodPack.get("map_color").getAsString());
                else planks.addProperty("map_color", "default");
                planks.addProperty("sounds", "wood");
                planks.addProperty("efficient_tool", "axe");
                planks.addProperty("requires_tool", false);
                planks.addProperty("harvest_level", 0);
                if (woodPack.has("texture_namespace"))
                    planks.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else planks.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                planks.add("textures", new JsonObject());
                JsonObject plankTextures = planks.getAsJsonObject("textures");
                plankTextures.addProperty("top_texture", woodPack.get("planks_texture").getAsString());
                plankTextures.addProperty("bottom_texture", woodPack.get("planks_texture").getAsString());
                plankTextures.addProperty("front_texture", woodPack.get("planks_texture").getAsString());
                plankTextures.addProperty("back_texture", woodPack.get("planks_texture").getAsString());
                plankTextures.addProperty("right_texture", woodPack.get("planks_texture").getAsString());
                plankTextures.addProperty("left_texture", woodPack.get("planks_texture").getAsString());
                if (BlockGenerator.add(planks) && CustomResourceCreator.generateBlockResources(planks) && LanguageHandler.addBlockKey(planks)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + planks.get("namespace").getAsString() + ":" + planks.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate planks!");
                }
                Identifier planksId = new Identifier(planks.get("namespace").getAsString(), planks.get("id").getAsString());
                JsonObject log = new JsonObject();
                log.addProperty("namespace", woodPack.get("namespace").getAsString());
                log.addProperty("id", woodPack.get("id").getAsString() + "_log");
                log.addProperty("display_name", woodPack.get("display_name").getAsString() + " Log");
                log.addProperty("material", "wood");
                log.addProperty("hardness", 2);
                log.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    log.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else log.addProperty("has_gravity", false);
                log.addProperty("rotation_type", "axis");
                if (woodPack.has("map_color"))
                    log.addProperty("map_color", woodPack.get("map_color").getAsString());
                else log.addProperty("map_color", "default");
                log.addProperty("sounds", "wood");
                log.addProperty("efficient_tool", "axe");
                log.addProperty("requires_tool", false);
                log.addProperty("harvest_level", 0);
                log.addProperty("stripped_block", woodPack.get("namespace").getAsString() + ":stripped_" + woodPack.get("id").getAsString() + "_log");
                if (woodPack.has("texture_namespace"))
                    log.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else log.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                log.add("textures", new JsonObject());
                JsonObject logTextures = log.getAsJsonObject("textures");
                logTextures.addProperty("top_texture", woodPack.get("log_top_texture").getAsString());
                logTextures.addProperty("bottom_texture", woodPack.get("log_top_texture").getAsString());
                logTextures.addProperty("front_texture", woodPack.get("log_side_texture").getAsString());
                logTextures.addProperty("back_texture", woodPack.get("log_side_texture").getAsString());
                logTextures.addProperty("right_texture", woodPack.get("log_side_texture").getAsString());
                logTextures.addProperty("left_texture", woodPack.get("log_side_texture").getAsString());
                if (BlockGenerator.add(log) && CustomResourceCreator.generateBlockResources(log) && LanguageHandler.addBlockKey(log)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + log.get("namespace").getAsString() + ":" + log.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate log!");
                }
                Identifier logId = new Identifier(log.get("namespace").getAsString(), log.get("id").getAsString());
                JsonObject woodBlock = new JsonObject();
                woodBlock.addProperty("namespace", woodPack.get("namespace").getAsString());
                woodBlock.addProperty("id", woodPack.get("id").getAsString() + "_wood");
                woodBlock.addProperty("display_name", woodPack.get("display_name").getAsString() + " Wood");
                woodBlock.addProperty("material", "wood");
                woodBlock.addProperty("hardness", 2);
                woodBlock.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    woodBlock.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else woodBlock.addProperty("has_gravity", false);
                woodBlock.addProperty("rotation_type", "axis");
                if (woodPack.has("map_color"))
                    woodBlock.addProperty("map_color", woodPack.get("map_color").getAsString());
                else woodBlock.addProperty("map_color", "default");
                woodBlock.addProperty("sounds", "wood");
                woodBlock.addProperty("efficient_tool", "axe");
                woodBlock.addProperty("requires_tool", false);
                woodBlock.addProperty("harvest_level", 0);
                woodBlock.addProperty("stripped_block", woodPack.get("namespace").getAsString() + ":stripped_" + woodPack.get("id").getAsString() + "_wood");
                if (woodPack.has("texture_namespace"))
                    woodBlock.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else woodBlock.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                woodBlock.add("textures", new JsonObject());
                JsonObject woodBlockTextures = woodBlock.getAsJsonObject("textures");
                woodBlockTextures.addProperty("top_texture", woodPack.get("log_side_texture").getAsString());
                woodBlockTextures.addProperty("bottom_texture", woodPack.get("log_side_texture").getAsString());
                woodBlockTextures.addProperty("front_texture", woodPack.get("log_side_texture").getAsString());
                woodBlockTextures.addProperty("back_texture", woodPack.get("log_side_texture").getAsString());
                woodBlockTextures.addProperty("right_texture", woodPack.get("log_side_texture").getAsString());
                woodBlockTextures.addProperty("left_texture", woodPack.get("log_side_texture").getAsString());
                if (BlockGenerator.add(woodBlock) && CustomResourceCreator.generateBlockResources(woodBlock) && LanguageHandler.addBlockKey(woodBlock)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + woodBlock.get("namespace").getAsString() + ":" + woodBlock.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate wood!");
                }
                Identifier woodBlockId = new Identifier(woodBlock.get("namespace").getAsString(), woodBlock.get("id").getAsString());
                JsonObject strippedLog = new JsonObject();
                strippedLog.addProperty("namespace", woodPack.get("namespace").getAsString());
                strippedLog.addProperty("id", "stripped_" + woodPack.get("id").getAsString() + "_log");
                strippedLog.addProperty("display_name", "Stripped " + woodPack.get("display_name").getAsString() + " Log");
                strippedLog.addProperty("material", "wood");
                strippedLog.addProperty("hardness", 2);
                strippedLog.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    strippedLog.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else strippedLog.addProperty("has_gravity", false);
                strippedLog.addProperty("rotation_type", "axis");
                if (woodPack.has("map_color"))
                    strippedLog.addProperty("map_color", woodPack.get("map_color").getAsString());
                else strippedLog.addProperty("map_color", "default");
                strippedLog.addProperty("sounds", "wood");
                strippedLog.addProperty("efficient_tool", "axe");
                strippedLog.addProperty("requires_tool", false);
                strippedLog.addProperty("harvest_level", 0);
                if (woodPack.has("texture_namespace"))
                    strippedLog.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else strippedLog.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                strippedLog.add("textures", new JsonObject());
                JsonObject strippedLogTextures = strippedLog.getAsJsonObject("textures");
                strippedLogTextures.addProperty("top_texture", woodPack.get("stripped_log_top_texture").getAsString());
                strippedLogTextures.addProperty("bottom_texture", woodPack.get("stripped_log_top_texture").getAsString());
                strippedLogTextures.addProperty("front_texture", woodPack.get("stripped_log_side_texture").getAsString());
                strippedLogTextures.addProperty("back_texture", woodPack.get("stripped_log_side_texture").getAsString());
                strippedLogTextures.addProperty("right_texture", woodPack.get("stripped_log_side_texture").getAsString());
                strippedLogTextures.addProperty("left_texture", woodPack.get("stripped_log_side_texture").getAsString());
                if (BlockGenerator.add(strippedLog) && CustomResourceCreator.generateBlockResources(strippedLog) && LanguageHandler.addBlockKey(strippedLog)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + strippedLog.get("namespace").getAsString() + ":" + strippedLog.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate log!");
                }
                Identifier strippedLogId = new Identifier(strippedLog.get("namespace").getAsString(), strippedLog.get("id").getAsString());
                JsonObject strippedWoodBlock = new JsonObject();
                strippedWoodBlock.addProperty("namespace", woodPack.get("namespace").getAsString());
                strippedWoodBlock.addProperty("id", "stripped_" + woodPack.get("id").getAsString() + "_wood");
                strippedWoodBlock.addProperty("display_name", "Stripped " + woodPack.get("display_name").getAsString() + " Wood");
                strippedWoodBlock.addProperty("material", "wood");
                strippedWoodBlock.addProperty("hardness", 2);
                strippedWoodBlock.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    strippedWoodBlock.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else strippedWoodBlock.addProperty("has_gravity", false);
                strippedWoodBlock.addProperty("rotation_type", "axis");
                if (woodPack.has("map_color"))
                    strippedWoodBlock.addProperty("map_color", woodPack.get("map_color").getAsString());
                else strippedWoodBlock.addProperty("map_color", "default");
                strippedWoodBlock.addProperty("sounds", "wood");
                strippedWoodBlock.addProperty("efficient_tool", "axe");
                strippedWoodBlock.addProperty("requires_tool", false);
                strippedWoodBlock.addProperty("harvest_level", 0);
                if (woodPack.has("texture_namespace"))
                    strippedWoodBlock.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else strippedWoodBlock.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                strippedWoodBlock.add("textures", new JsonObject());
                JsonObject strippedWoodBlockTextures = strippedWoodBlock.getAsJsonObject("textures");
                strippedWoodBlockTextures.addProperty("top_texture", woodPack.get("stripped_log_side_texture").getAsString());
                strippedWoodBlockTextures.addProperty("bottom_texture", woodPack.get("stripped_log_side_texture").getAsString());
                strippedWoodBlockTextures.addProperty("front_texture", woodPack.get("stripped_log_side_texture").getAsString());
                strippedWoodBlockTextures.addProperty("back_texture", woodPack.get("stripped_log_side_texture").getAsString());
                strippedWoodBlockTextures.addProperty("right_texture", woodPack.get("stripped_log_side_texture").getAsString());
                strippedWoodBlockTextures.addProperty("left_texture", woodPack.get("stripped_log_side_texture").getAsString());
                if (BlockGenerator.add(strippedWoodBlock) && CustomResourceCreator.generateBlockResources(strippedWoodBlock) && LanguageHandler.addBlockKey(strippedWoodBlock)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + strippedWoodBlock.get("namespace").getAsString() + ":" + strippedWoodBlock.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate wood!");
                }
                Identifier strippedWoodBlockId = new Identifier(strippedWoodBlock.get("namespace").getAsString(), strippedWoodBlock.get("id").getAsString());
                JsonObject slab = new JsonObject();
                slab.addProperty("namespace", woodPack.get("namespace").getAsString());
                slab.addProperty("id", woodPack.get("id").getAsString() + "_slab");
                slab.addProperty("display_name", woodPack.get("display_name").getAsString() + " Slab");
                slab.addProperty("base", "slab");
                slab.addProperty("material", "wood");
                slab.addProperty("hardness", 2);
                slab.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    slab.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else slab.addProperty("has_gravity", false);
                if (woodPack.has("map_color"))
                    slab.addProperty("map_color", woodPack.get("map_color").getAsString());
                else slab.addProperty("map_color", "default");
                slab.addProperty("sounds", "wood");
                slab.addProperty("efficient_tool", "axe");
                slab.addProperty("requires_tool", false);
                slab.addProperty("harvest_level", 0);
                if (woodPack.has("texture_namespace"))
                    slab.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else slab.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                slab.add("textures", new JsonObject());
                JsonObject slabTextures = slab.getAsJsonObject("textures");
                slabTextures.addProperty("top_texture", woodPack.get("planks_texture").getAsString());
                slabTextures.addProperty("bottom_texture", woodPack.get("planks_texture").getAsString());
                slabTextures.addProperty("front_texture", woodPack.get("planks_texture").getAsString());
                slabTextures.addProperty("back_texture", woodPack.get("planks_texture").getAsString());
                slabTextures.addProperty("right_texture", woodPack.get("planks_texture").getAsString());
                slabTextures.addProperty("left_texture", woodPack.get("planks_texture").getAsString());
                if (BlockGenerator.add(slab) && CustomResourceCreator.generateBlockResources(slab) && LanguageHandler.addBlockKey(slab)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + slab.get("namespace").getAsString() + ":" + slab.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate slab!");
                }
                Identifier slabId = new Identifier(slab.get("namespace").getAsString(), slab.get("id").getAsString());
                JsonObject stairs = new JsonObject();
                stairs.addProperty("namespace", woodPack.get("namespace").getAsString());
                stairs.addProperty("id", woodPack.get("id").getAsString() + "_stairs");
                stairs.addProperty("display_name", woodPack.get("display_name").getAsString() + " Stairs");
                stairs.addProperty("base", "stairs");
                stairs.addProperty("material", "wood");
                stairs.addProperty("hardness", 2);
                stairs.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    stairs.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else stairs.addProperty("has_gravity", false);
                if (woodPack.has("map_color"))
                    stairs.addProperty("map_color", woodPack.get("map_color").getAsString());
                else stairs.addProperty("map_color", "default");
                stairs.addProperty("sounds", "wood");
                stairs.addProperty("efficient_tool", "axe");
                stairs.addProperty("requires_tool", false);
                stairs.addProperty("harvest_level", 0);
                if (woodPack.has("texture_namespace"))
                    stairs.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else stairs.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                stairs.add("textures", new JsonObject());
                JsonObject stairTextures = stairs.getAsJsonObject("textures");
                stairTextures.addProperty("top_texture", woodPack.get("planks_texture").getAsString());
                stairTextures.addProperty("bottom_texture", woodPack.get("planks_texture").getAsString());
                stairTextures.addProperty("front_texture", woodPack.get("planks_texture").getAsString());
                stairTextures.addProperty("back_texture", woodPack.get("planks_texture").getAsString());
                stairTextures.addProperty("right_texture", woodPack.get("planks_texture").getAsString());
                stairTextures.addProperty("left_texture", woodPack.get("planks_texture").getAsString());
                if (BlockGenerator.add(stairs) && CustomResourceCreator.generateBlockResources(stairs) && LanguageHandler.addBlockKey(stairs)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + stairs.get("namespace").getAsString() + ":" + stairs.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate stairs!");
                }
                Identifier stairId = new Identifier(stairs.get("namespace").getAsString(), stairs.get("id").getAsString());
                JsonObject fence = new JsonObject();
                fence.addProperty("namespace", woodPack.get("namespace").getAsString());
                fence.addProperty("id", woodPack.get("id").getAsString() + "_fence");
                fence.addProperty("display_name", woodPack.get("display_name").getAsString() + " Fence");
                fence.addProperty("base", "fence");
                fence.addProperty("material", "wood");
                fence.addProperty("hardness", 2);
                fence.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    fence.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else fence.addProperty("has_gravity", false);
                if (woodPack.has("map_color"))
                    fence.addProperty("map_color", woodPack.get("map_color").getAsString());
                else fence.addProperty("map_color", "default");
                fence.addProperty("sounds", "wood");
                fence.addProperty("efficient_tool", "axe");
                fence.addProperty("requires_tool", false);
                fence.addProperty("harvest_level", 0);
                if (woodPack.has("texture_namespace"))
                    fence.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else fence.addProperty("texture_namespace", fence.get("namespace").getAsString());
                fence.add("textures", new JsonObject());
                JsonObject fenceTextures = fence.getAsJsonObject("textures");
                fenceTextures.addProperty("top_texture", woodPack.get("planks_texture").getAsString());
                fenceTextures.addProperty("bottom_texture", woodPack.get("planks_texture").getAsString());
                fenceTextures.addProperty("front_texture", woodPack.get("planks_texture").getAsString());
                fenceTextures.addProperty("back_texture", woodPack.get("planks_texture").getAsString());
                fenceTextures.addProperty("right_texture", woodPack.get("planks_texture").getAsString());
                fenceTextures.addProperty("left_texture", woodPack.get("planks_texture").getAsString());
                if (BlockGenerator.add(fence) && CustomResourceCreator.generateBlockResources(fence) && LanguageHandler.addBlockKey(fence)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + fence.get("namespace").getAsString() + ":" + fence.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate fence!");
                }
                Identifier fenceId = new Identifier(fence.get("namespace").getAsString(), fence.get("id").getAsString());
                JsonObject fenceGate = new JsonObject();
                fenceGate.addProperty("namespace", woodPack.get("namespace").getAsString());
                fenceGate.addProperty("id", woodPack.get("id").getAsString() + "_fence_gate");
                fenceGate.addProperty("display_name", woodPack.get("display_name").getAsString() + " Fence Gate");
                fenceGate.addProperty("base", "fence_gate");
                fenceGate.addProperty("material", "wood");
                fenceGate.addProperty("hardness", 2);
                fenceGate.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    fenceGate.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else fenceGate.addProperty("has_gravity", false);
                if (woodPack.has("map_color"))
                    fenceGate.addProperty("map_color", woodPack.get("map_color").getAsString());
                else fenceGate.addProperty("map_color", "default");
                fenceGate.addProperty("sounds", "wood");
                fenceGate.addProperty("efficient_tool", "axe");
                fenceGate.addProperty("requires_tool", false);
                fenceGate.addProperty("harvest_level", 0);
                if (woodPack.has("texture_namespace"))
                    fenceGate.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else fenceGate.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                fenceGate.add("textures", new JsonObject());
                JsonObject fenceGateTextures = fenceGate.getAsJsonObject("textures");
                fenceGateTextures.addProperty("top_texture", woodPack.get("planks_texture").getAsString());
                fenceGateTextures.addProperty("bottom_texture", woodPack.get("planks_texture").getAsString());
                fenceGateTextures.addProperty("front_texture", woodPack.get("planks_texture").getAsString());
                fenceGateTextures.addProperty("back_texture", woodPack.get("planks_texture").getAsString());
                fenceGateTextures.addProperty("right_texture", woodPack.get("planks_texture").getAsString());
                fenceGateTextures.addProperty("left_texture", woodPack.get("planks_texture").getAsString());
                if (BlockGenerator.add(fenceGate) && CustomResourceCreator.generateBlockResources(fenceGate) && LanguageHandler.addBlockKey(fenceGate)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + fenceGate.get("namespace").getAsString() + ":" + fenceGate.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate fence gate!");
                }
                Identifier fenceGateId = new Identifier(fenceGate.get("namespace").getAsString(), fenceGate.get("id").getAsString());
                JsonObject pressurePlate = new JsonObject();
                pressurePlate.addProperty("namespace", woodPack.get("namespace").getAsString());
                pressurePlate.addProperty("id", woodPack.get("id").getAsString() + "_pressure_plate");
                pressurePlate.addProperty("display_name", woodPack.get("display_name").getAsString() + " Pressure Plate");
                pressurePlate.addProperty("base", "pressure_plate");
                pressurePlate.addProperty("material", "wood");
                pressurePlate.addProperty("hardness", 2);
                pressurePlate.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    pressurePlate.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else pressurePlate.addProperty("has_gravity", false);
                if (woodPack.has("map_color"))
                    pressurePlate.addProperty("map_color", woodPack.get("map_color").getAsString());
                else pressurePlate.addProperty("map_color", "default");
                pressurePlate.addProperty("sounds", "wood");
                pressurePlate.addProperty("efficient_tool", "axe");
                pressurePlate.addProperty("requires_tool", false);
                pressurePlate.addProperty("harvest_level", 0);
                if (woodPack.has("texture_namespace"))
                    pressurePlate.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else pressurePlate.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                pressurePlate.add("textures", new JsonObject());
                JsonObject pressurePlateTextures = pressurePlate.getAsJsonObject("textures");
                pressurePlateTextures.addProperty("all", woodPack.get("planks_texture").getAsString());
                if (BlockGenerator.add(pressurePlate) && CustomResourceCreator.generateBlockResources(pressurePlate) && LanguageHandler.addBlockKey(pressurePlate)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + pressurePlate.get("namespace").getAsString() + ":" + pressurePlate.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate pressure plate!");
                }
                Identifier pressurePlateId = new Identifier(pressurePlate.get("namespace").getAsString(), pressurePlate.get("id").getAsString());
                JsonObject button = new JsonObject();
                button.addProperty("namespace", woodPack.get("namespace").getAsString());
                button.addProperty("id", woodPack.get("id").getAsString() + "_button");
                button.addProperty("display_name", woodPack.get("display_name").getAsString() + " Button");
                button.addProperty("base", "button");
                button.addProperty("material", "wood");
                button.addProperty("hardness", 2);
                button.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    button.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else button.addProperty("has_gravity", false);
                if (woodPack.has("map_color"))
                    button.addProperty("map_color", woodPack.get("map_color").getAsString());
                else button.addProperty("map_color", "default");
                button.addProperty("sounds", "wood");
                button.addProperty("efficient_tool", "axe");
                button.addProperty("requires_tool", false);
                button.addProperty("harvest_level", 0);
                if (woodPack.has("texture_namespace"))
                    button.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else button.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                button.add("textures", new JsonObject());
                JsonObject buttonTextures = button.getAsJsonObject("textures");
                buttonTextures.addProperty("all", woodPack.get("planks_texture").getAsString());
                if (BlockGenerator.add(button) && CustomResourceCreator.generateBlockResources(button) && LanguageHandler.addBlockKey(button)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + button.get("namespace").getAsString() + ":" + button.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate button!");
                }
                Identifier buttonId = new Identifier(button.get("namespace").getAsString(), button.get("id").getAsString());
                JsonObject door = new JsonObject();
                door.addProperty("namespace", woodPack.get("namespace").getAsString());
                door.addProperty("id", woodPack.get("id").getAsString() + "_door");
                door.addProperty("display_name", woodPack.get("display_name").getAsString() + " Door");
                door.addProperty("base", "door");
                door.addProperty("material", "wood");
                door.addProperty("hardness", 2);
                door.addProperty("resistance", 3);
                if (woodPack.has("map_color"))
                    door.addProperty("map_color", woodPack.get("map_color").getAsString());
                else woodPack.addProperty("map_color", "default");
                door.addProperty("sounds", "wood");
                door.addProperty("efficient_tool", "axe");
                door.addProperty("requires_tool", false);
                door.addProperty("harvest_level", 0);
                door.addProperty("render_type", "cutout");
                if (woodPack.has("texture_namespace"))
                    door.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else door.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                door.add("textures", new JsonObject());
                JsonObject doorTextures = door.getAsJsonObject("textures");
                doorTextures.addProperty("top_texture", woodPack.get("door_top_texture").getAsString());
                doorTextures.addProperty("bottom_texture", woodPack.get("door_bottom_texture").getAsString());
                doorTextures.addProperty("front_texture", woodPack.get("door_item_texture").getAsString());
                doorTextures.addProperty("back_texture", woodPack.get("door_bottom_texture").getAsString());
                doorTextures.addProperty("right_texture", woodPack.get("door_bottom_texture").getAsString());
                doorTextures.addProperty("left_texture", woodPack.get("door_bottom_texture").getAsString());
                if (BlockGenerator.add(door) && CustomResourceCreator.generateBlockResources(door) && LanguageHandler.addBlockKey(door)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + door.get("namespace").getAsString() + ":" + door.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate door!");
                }
                Identifier doorId = new Identifier(door.get("namespace").getAsString(), door.get("id").getAsString());
                JsonObject trapdoor = new JsonObject();
                trapdoor.addProperty("namespace", woodPack.get("namespace").getAsString());
                trapdoor.addProperty("id", woodPack.get("id").getAsString() + "_trapdoor");
                trapdoor.addProperty("display_name", woodPack.get("display_name").getAsString() + " Trapdoor");
                trapdoor.addProperty("base", "trapdoor");
                trapdoor.addProperty("material", "wood");
                trapdoor.addProperty("hardness", 2);
                trapdoor.addProperty("resistance", 3);
                if (woodPack.has("has_gravity"))
                    trapdoor.addProperty("has_gravity", woodPack.get("has_gravity").getAsBoolean());
                else trapdoor.addProperty("has_gravity", false);
                if (woodPack.has("map_color"))
                    trapdoor.addProperty("map_color", woodPack.get("map_color").getAsString());
                else trapdoor.addProperty("map_color", "default");
                trapdoor.addProperty("sounds", "wood");
                trapdoor.addProperty("efficient_tool", "axe");
                trapdoor.addProperty("requires_tool", false);
                trapdoor.addProperty("harvest_level", 0);
                trapdoor.addProperty("render_type", "cutout");
                if (woodPack.has("texture_namespace"))
                    trapdoor.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else trapdoor.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                trapdoor.add("textures", new JsonObject());
                JsonObject trapdoorTextures = trapdoor.getAsJsonObject("textures");
                trapdoorTextures.addProperty("all", woodPack.get("trapdoor_texture").getAsString());
                if (BlockGenerator.add(trapdoor) && CustomResourceCreator.generateBlockResources(trapdoor) && LanguageHandler.addBlockKey(trapdoor)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + trapdoor.get("namespace").getAsString() + ":" + trapdoor.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate trapdoor!");
                }
                JsonObject tree = new JsonObject();
                tree.addProperty("namespace", woodPack.get("namespace").getAsString());
                tree.addProperty("id", woodPack.get("id").getAsString() + "_trapdoor");
                tree.addProperty("display_name", woodPack.get("display_name").getAsString() + " Trapdoor");
                if (woodPack.has("texture_namespace"))
                    tree.addProperty("texture_namespace", woodPack.get("texture_namespace").getAsString());
                else tree.addProperty("texture_namespace", woodPack.get("namespace").getAsString());
                tree.addProperty("all", woodPack.get("sapling_texture").getAsString());
                if (TreeGenerator.add(tree) && CustomResourceCreator.generateSaplingResources(tree) && LanguageHandler.addSaplingKey(tree)) {
                    CustomBlocksMod.LOGGER.info("Generated Tree " + tree.get("namespace").getAsString() + ":" + tree.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate tree!");
                }
                Identifier saplingId = new Identifier(tree.get("namespace").getAsString(), TreeGenerator.getSaplingId(tree.get("id").getAsString()));
                Identifier pottedSaplingId = new Identifier(saplingId.getNamespace(), "potted_" + saplingId.getPath());
                Identifier trapdoorId = new Identifier(trapdoor.get("namespace").getAsString(), trapdoor.get("id").getAsString());
                BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), Registry.BLOCK.get(doorId), Registry.BLOCK.get(trapdoorId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fence_gates"), "blocks", fenceGateId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("logs_that_burn"), "blocks", logId, woodBlockId, strippedLogId, strippedWoodBlockId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("planks"), "blocks", planksId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_buttons"), "blocks", buttonId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_doors"), "blocks", doorId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_fences"), "blocks", fenceId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_pressure_plate"), "blocks", pressurePlateId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_slabs"), "blocks", slabId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_stairs"), "blocks", stairId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_trapdoors"), "blocks", trapdoorId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("saplings"), "blocks", saplingId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("flower_pots"), "blocks", pottedSaplingId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("logs_that_burn"), "items", logId, woodBlockId, strippedLogId, strippedWoodBlockId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("planks"), "items", planksId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_buttons"), "items", buttonId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_doors"), "items", doorId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_fences"), "items", fenceId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_pressure_plate"), "items", pressurePlateId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_slabs"), "items", slabId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_stairs"), "items", stairId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("wooden_trapdoors"), "items", trapdoorId));
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("saplings"), "items", saplingId));
                Ingredient logs = Ingredient.ofItems(Registry.BLOCK.get(logId), Registry.BLOCK.get(woodBlockId), Registry.BLOCK.get(strippedLogId), Registry.BLOCK.get(strippedWoodBlockId));
                Ingredient logIngredient = Ingredient.ofItems(Registry.BLOCK.get(logId));
                Ingredient strippedLogIngredient = Ingredient.ofItems(Registry.BLOCK.get(strippedLogId));
                Ingredient plank = Ingredient.ofItems(Registry.BLOCK.get(planksId));
                InventoryChangedCriterion.Conditions getPlanks = InventoryChangedCriterion.Conditions.items(Registry.BLOCK.get(planksId));
                ShapelessRecipeJsonFactory.create(Registry.BLOCK.get(planksId), 4).input(logs).group("planks").criterion("obtain_logs", InventoryChangedCriterion.Conditions.items(Registry.BLOCK.get(logId), Registry.BLOCK.get(woodBlockId), Registry.BLOCK.get(strippedLogId), Registry.BLOCK.get(strippedWoodBlockId))).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", planksId.getNamespace());
                    custom.addProperty("id", planksId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, planksId);
                ShapedRecipeJsonFactory.create(Registry.BLOCK.get(woodBlockId), 3).pattern("XX").pattern("XX").input('X', logIngredient).group("bark").criterion("obtain_log", InventoryChangedCriterion.Conditions.items(Registry.BLOCK.get(logId))).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", woodBlockId.getNamespace());
                    custom.addProperty("id", woodBlockId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, woodBlockId);
                ShapedRecipeJsonFactory.create(Registry.BLOCK.get(strippedWoodBlockId), 3).pattern("XX").pattern("XX").input('X', strippedLogIngredient).group("bark").criterion("obtain_stripped_log", InventoryChangedCriterion.Conditions.items(Registry.BLOCK.get(strippedLogId))).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", strippedWoodBlockId.getNamespace());
                    custom.addProperty("id", strippedWoodBlockId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, strippedWoodBlockId);
                ShapedRecipeJsonFactory.create(Registry.BLOCK.get(slabId), 6).pattern("XXX").input('X', plank).group("wooden_slab").criterion("get_planks", getPlanks).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", slabId.getNamespace());
                    custom.addProperty("id", slabId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, slabId);
                ShapedRecipeJsonFactory.create(Registry.BLOCK.get(stairId), 4).pattern("X  ").pattern("XX ").pattern("XXX").input('X', plank).group("wooden_stairs").criterion("get_planks", getPlanks).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", stairId.getNamespace());
                    custom.addProperty("id", stairId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, stairId);
                ShapedRecipeJsonFactory.create(Registry.BLOCK.get(fenceId), 3).pattern("XIX").pattern("XIX").input('X', plank).input('I', Ingredient.ofItems(Items.STICK)).group("wooden_fence").criterion("get_planks", getPlanks).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", fenceId.getNamespace());
                    custom.addProperty("id", fenceId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, fenceId);
                ShapedRecipeJsonFactory.create(Registry.BLOCK.get(fenceGateId), 1).pattern("IXI").pattern("IXI").input('I', Ingredient.ofItems(Items.STICK)).input('X', plank).group("wooden_fence_gate").criterion("get_planks", getPlanks).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", fenceGateId.getNamespace());
                    custom.addProperty("id", fenceGateId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, fenceGateId);
                ShapedRecipeJsonFactory.create(Registry.BLOCK.get(pressurePlateId), 1).pattern("XX").input('X', plank).group("wooden_pressure_plate").criterion("get_planks", getPlanks).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", pressurePlateId.getNamespace());
                    custom.addProperty("id", pressurePlateId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, pressurePlateId);
                ShapelessRecipeJsonFactory.create(Registry.BLOCK.get(buttonId), 1).input(plank).group("wooden_button").criterion("get_planks", getPlanks).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", buttonId.getNamespace());
                    custom.addProperty("id", buttonId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, buttonId);
                ShapedRecipeJsonFactory.create(Registry.BLOCK.get(doorId), 3).pattern("XX").pattern("XX").pattern("XX").input('X', plank).group("wooden_door").criterion("get_planks", getPlanks).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", doorId.getNamespace());
                    custom.addProperty("id", doorId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, doorId);
                ShapedRecipeJsonFactory.create(Registry.BLOCK.get(trapdoorId), 2).pattern("XXX").pattern("XXX").input('X', plank).group("wooden_trapdoor").criterion("get_planks", getPlanks).offerTo(recipeJsonProvider -> {
                    JsonObject recipe = recipeJsonProvider.toJson();
                    JsonObject custom = new JsonObject();
                    custom.addProperty("namespace", trapdoorId.getNamespace());
                    custom.addProperty("id", trapdoorId.getPath());
                    recipe.add("custom", custom);
                    RecipeGenerator.add(recipe);
                }, trapdoorId);
            } else {
                CustomBlocksMod.LOGGER.error("Wood pack " + pack.get("name").getAsString() + " is invalid! Skipping...");
            }
        }
        if (pack.has("blocks")) {
            List<JsonObject> blocks = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("blocks"));
            for (JsonObject block : blocks) {
                if (block.has("format_version")) {
                    if (!BlockGenerator.addBedrock(block, null)) {
                        CustomBlocksMod.LOGGER.error("Failed to generate block!");
                    }
                } else {
                    if (BlockGenerator.add(block) && CustomResourceCreator.generateBlockResources(block) && LanguageHandler.addBlockKey(block)) {
                        CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
                    } else {
                        CustomBlocksMod.LOGGER.error("Failed to generate block!");
                    }
                }
            }
        }
        if (pack.has("items")) {
            List<JsonObject> items = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("items"));
            for (JsonObject item : items) {
                if (ItemGenerator.add(item) && CustomResourceCreator.generateItemResources(item) && LanguageHandler.addItemKey(item)) {
                    CustomBlocksMod.LOGGER.info("Generated Item " + item.get("namespace").getAsString() + ":" + item.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate item!");
                }
            }
        }
        if (pack.has("fluids")) {
            List<JsonObject> items = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("fluids"));
            for (JsonObject fluid : items) {
                if (FluidGenerator.add(fluid) && CustomResourceCreator.generateFluidResources(fluid) && LanguageHandler.addFluidKeys(fluid)) {
                    CustomBlocksMod.LOGGER.info("Generated Fluid {}", fluid.get("namespace").getAsString() + ":" + fluid.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate fluid!");
                }
            }
        }
        if (pack.has("entities")) {
            List<JsonObject> entities = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("entities"));
            for (JsonObject entity : entities) {
                if (EntityGenerator.add(entity) && CustomResourceCreator.generateEntityResources(entity) && LanguageHandler.addEntityKey(entity)) {
                    CustomBlocksMod.LOGGER.info("Generated Entity {}", entity.get("namespace").getAsString() + ":" + entity.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate entity!");
                }
            }
        }
        if (pack.has("item_groups")) {
            List<JsonObject> itemGroups = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("item_groups"));
            for (JsonObject itemGroup : itemGroups) {
                if (ItemGroupGenerator.add(itemGroup) && LanguageHandler.addItemGroupKey(itemGroup)) {
                    CustomBlocksMod.LOGGER.info("Generated Item Group " + itemGroup.get("namespace").getAsString() + ":" + itemGroup.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate item group!");
                }
            }
        }
        if (pack.has("paintings")) {
            List<JsonObject> paintings = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("items"));
            for (JsonObject painting : paintings) {
                if (PaintingGenerator.add(painting) && CustomResourceCreator.generatePaintingResources(painting)) {
                    CustomBlocksMod.LOGGER.info("Generated Painting " + painting.get("namespace").getAsString() + ":" + painting.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate painting!");
                }
            }
        }
        if (pack.has("recipes")) {
            List<JsonObject> recipes = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("recipes"));
            for (JsonObject recipe : recipes) {
                if (RecipeGenerator.add(recipe)) {
                    CustomBlocksMod.LOGGER.info("Generated Recipe {}", recipe.getAsJsonObject("custom").get("namespace").getAsString() + ":" + recipe.getAsJsonObject("custom").get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate recipe!");
                }
            }
        }
        if (pack.has("trees")) {
            List<JsonObject> trees = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("trees"));
            for (JsonObject tree : trees) {
                if (TreeGenerator.add(tree) && CustomResourceCreator.generateSaplingResources(tree) && LanguageHandler.addSaplingKey(tree)) {
                    CustomBlocksMod.LOGGER.info("Generated Tree {}", tree.get("namespace").getAsString() + ":" + tree.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to generate tree!");
                }
            }
        }
        if (pack.has("packs")) {
            List<JsonObject> packs = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("packs"));
            for (JsonObject subPack : packs) {
                if (subPack.has("name")) {
                    CustomBlocksMod.LOGGER.info("Loading sub pack " + subPack.get("name").getAsString() + "...");
                    PackGenerator.add(subPack);
                    CustomBlocksMod.LOGGER.info("Loaded sub pack " + subPack.get("name").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.error("Failed to load sub pack!");
                }
            }
        }
    }
}
