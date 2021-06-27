package com.ulto.customblocks;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;

import java.util.List;

public class PackGenerator {
    public static void add(JsonObject pack) {
        if (pack.has("wood_pack")) {
            JsonObject woodPack = pack.getAsJsonObject("wood_pack");
            if (woodPack.has("planks")) {
                JsonObject planks = woodPack.getAsJsonObject("planks");
                if (planks.has("id") && planks.has("display_name") && planks.has("texture")) {
                    JsonObject block = new JsonObject();
                    block.addProperty("namespace", woodPack.get("namespace").getAsString());
                    block.addProperty("id", planks.get("id").getAsString());
                    block.addProperty("display_name", planks.get("display_name").getAsString());
                    block.addProperty("material", "wood");
                    block.addProperty("hardness", 2);
                    block.addProperty("resistance", 3);
                    if (planks.has("has_gravity"))
                        block.addProperty("has_gravity", planks.get("has_gravity").getAsBoolean());
                    else block.addProperty("has_gravity", false);
                    if (planks.has("map_color"))
                        block.addProperty("map_color", planks.get("map_color").getAsString());
                    else block.addProperty("map_color", "default");
                    block.addProperty("sounds", "wood");
                    block.addProperty("efficient_tool", "axe");
                    block.addProperty("requires_tool", false);
                    block.addProperty("harvest_level", 0);
                    if (planks.has("texture_namespace"))
                        block.addProperty("texture_namespace", planks.get("texture_namespace").getAsString());
                    else block.addProperty("texture_namespace", block.get("namespace").getAsString());
                    block.add("textures", new JsonObject());
                    JsonObject textures = block.getAsJsonObject("textures");
                    textures.addProperty("top_texture", planks.get("texture").getAsString());
                    textures.addProperty("bottom_texture", planks.get("texture").getAsString());
                    textures.addProperty("front_texture", planks.get("texture").getAsString());
                    textures.addProperty("back_texture", planks.get("texture").getAsString());
                    textures.addProperty("right_texture", planks.get("texture").getAsString());
                    textures.addProperty("left_texture", planks.get("texture").getAsString());
                    if (BlockGenerator.add(block) && ResourcePackGenerator.generateBlockResources(block, block.getAsJsonObject("textures").get("top_texture").getAsString(), block.getAsJsonObject("textures").get("bottom_texture").getAsString(), block.getAsJsonObject("textures").get("front_texture").getAsString(), block.getAsJsonObject("textures").get("back_texture").getAsString(), block.getAsJsonObject("textures").get("right_texture").getAsString(), block.getAsJsonObject("textures").get("left_texture").getAsString()) && LanguageHandler.addBlockKey(block)) {
                        CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
                    } else {
                        CustomBlocksMod.LOGGER.info("Failed to generate block!");
                    }
                } else {
                    CustomBlocksMod.LOGGER.info("Failed to generate block!");
                }
            }
            if (woodPack.has("log")) {
                JsonObject log = woodPack.getAsJsonObject("log");
                if (log.has("id") && log.has("display_name") && log.has("top_texture") && log.has("side_texture")) {
                    JsonObject block = new JsonObject();
                    block.addProperty("namespace", woodPack.get("namespace").getAsString());
                    block.addProperty("id", log.get("id").getAsString());
                    block.addProperty("display_name", log.get("display_name").getAsString());
                    block.addProperty("material", "wood");
                    block.addProperty("hardness", 2);
                    block.addProperty("resistance", 3);
                    if (log.has("has_gravity"))
                        block.addProperty("has_gravity", log.get("has_gravity").getAsBoolean());
                    else block.addProperty("has_gravity", false);
                    block.addProperty("rotation_type", "axis");
                    if (log.has("map_color"))
                        block.addProperty("map_color", log.get("map_color").getAsString());
                    else block.addProperty("map_color", "default");
                    block.addProperty("sounds", "wood");
                    block.addProperty("efficient_tool", "axe");
                    block.addProperty("requires_tool", false);
                    block.addProperty("harvest_level", 0);
                    if (log.has("texture_namespace"))
                        block.addProperty("texture_namespace", log.get("texture_namespace").getAsString());
                    else block.addProperty("texture_namespace", block.get("namespace").getAsString());
                    block.add("textures", new JsonObject());
                    JsonObject textures = block.getAsJsonObject("textures");
                    textures.addProperty("top_texture", log.get("top_texture").getAsString());
                    textures.addProperty("bottom_texture", log.get("top_texture").getAsString());
                    textures.addProperty("front_texture", log.get("side_texture").getAsString());
                    textures.addProperty("back_texture", log.get("side_texture").getAsString());
                    textures.addProperty("right_texture", log.get("side_texture").getAsString());
                    textures.addProperty("left_texture", log.get("side_texture").getAsString());
                    if (BlockGenerator.add(block) && ResourcePackGenerator.generateBlockResources(block, block.getAsJsonObject("textures").get("top_texture").getAsString(), block.getAsJsonObject("textures").get("bottom_texture").getAsString(), block.getAsJsonObject("textures").get("front_texture").getAsString(), block.getAsJsonObject("textures").get("back_texture").getAsString(), block.getAsJsonObject("textures").get("right_texture").getAsString(), block.getAsJsonObject("textures").get("left_texture").getAsString()) && LanguageHandler.addBlockKey(block)) {
                        CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
                    } else {
                        CustomBlocksMod.LOGGER.info("Failed to generate block!");
                    }
                } else {
                    CustomBlocksMod.LOGGER.info("Failed to generate block!");
                }
            }
            if (woodPack.has("wood")) {
                JsonObject wood = woodPack.getAsJsonObject("wood");
                if (wood.has("id") && wood.has("display_name") && wood.has("texture")) {
                    JsonObject block = new JsonObject();
                    block.addProperty("namespace", woodPack.get("namespace").getAsString());
                    block.addProperty("id", wood.get("id").getAsString());
                    block.addProperty("display_name", wood.get("display_name").getAsString());
                    block.addProperty("material", "wood");
                    block.addProperty("hardness", 2);
                    block.addProperty("resistance", 3);
                    if (wood.has("has_gravity"))
                        block.addProperty("has_gravity", wood.get("has_gravity").getAsBoolean());
                    else block.addProperty("has_gravity", false);
                    block.addProperty("rotation_type", "axis");
                    if (wood.has("map_color"))
                        block.addProperty("map_color", wood.get("map_color").getAsString());
                    else block.addProperty("map_color", "default");
                    block.addProperty("sounds", "wood");
                    block.addProperty("efficient_tool", "axe");
                    block.addProperty("requires_tool", false);
                    block.addProperty("harvest_level", 0);
                    if (wood.has("texture_namespace"))
                        block.addProperty("texture_namespace", wood.get("texture_namespace").getAsString());
                    else block.addProperty("texture_namespace", block.get("namespace").getAsString());
                    block.add("textures", new JsonObject());
                    JsonObject textures = block.getAsJsonObject("textures");
                    textures.addProperty("top_texture", wood.get("texture").getAsString());
                    textures.addProperty("bottom_texture", wood.get("texture").getAsString());
                    textures.addProperty("front_texture", wood.get("texture").getAsString());
                    textures.addProperty("back_texture", wood.get("texture").getAsString());
                    textures.addProperty("right_texture", wood.get("texture").getAsString());
                    textures.addProperty("left_texture", wood.get("texture").getAsString());
                    if (BlockGenerator.add(block) && ResourcePackGenerator.generateBlockResources(block, block.getAsJsonObject("textures").get("top_texture").getAsString(), block.getAsJsonObject("textures").get("bottom_texture").getAsString(), block.getAsJsonObject("textures").get("front_texture").getAsString(), block.getAsJsonObject("textures").get("back_texture").getAsString(), block.getAsJsonObject("textures").get("right_texture").getAsString(), block.getAsJsonObject("textures").get("left_texture").getAsString()) && LanguageHandler.addBlockKey(block)) {
                        CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
                    } else {
                        CustomBlocksMod.LOGGER.info("Failed to generate block!");
                    }
                } else {
                    CustomBlocksMod.LOGGER.info("Failed to generate block!");
                }
            }
            if (woodPack.has("slab")) {
                JsonObject slab = woodPack.getAsJsonObject("slab");
                if (slab.has("id") && slab.has("display_name") && slab.has("top_texture") && slab.has("side_texture") && slab.has("bottom_texture")) {
                    JsonObject block = new JsonObject();
                    block.addProperty("namespace", woodPack.get("namespace").getAsString());
                    block.addProperty("id", slab.get("id").getAsString());
                    block.addProperty("display_name", slab.get("display_name").getAsString());
                    block.addProperty("base", "slab");
                    block.addProperty("material", "wood");
                    block.addProperty("hardness", 2);
                    block.addProperty("resistance", 3);
                    if (slab.has("has_gravity"))
                        block.addProperty("has_gravity", slab.get("has_gravity").getAsBoolean());
                    else block.addProperty("has_gravity", false);
                    if (slab.has("map_color"))
                        block.addProperty("map_color", slab.get("map_color").getAsString());
                    else block.addProperty("map_color", "default");
                    block.addProperty("sounds", "wood");
                    block.addProperty("efficient_tool", "axe");
                    block.addProperty("requires_tool", false);
                    block.addProperty("harvest_level", 0);
                    if (slab.has("texture_namespace"))
                        block.addProperty("texture_namespace", slab.get("texture_namespace").getAsString());
                    else block.addProperty("texture_namespace", block.get("namespace").getAsString());
                    block.add("textures", new JsonObject());
                    JsonObject textures = block.getAsJsonObject("textures");
                    textures.addProperty("top_texture", slab.get("top_texture").getAsString());
                    textures.addProperty("bottom_texture", slab.get("bottom_texture").getAsString());
                    textures.addProperty("front_texture", slab.get("side_texture").getAsString());
                    textures.addProperty("back_texture", slab.get("side_texture").getAsString());
                    textures.addProperty("right_texture", slab.get("side_texture").getAsString());
                    textures.addProperty("left_texture", slab.get("side_texture").getAsString());
                    if (BlockGenerator.add(block) && ResourcePackGenerator.generateBlockResources(block, block.getAsJsonObject("textures").get("top_texture").getAsString(), block.getAsJsonObject("textures").get("bottom_texture").getAsString(), block.getAsJsonObject("textures").get("front_texture").getAsString(), block.getAsJsonObject("textures").get("back_texture").getAsString(), block.getAsJsonObject("textures").get("right_texture").getAsString(), block.getAsJsonObject("textures").get("left_texture").getAsString()) && LanguageHandler.addBlockKey(block)) {
                        CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
                    } else {
                        CustomBlocksMod.LOGGER.info("Failed to generate block!");
                    }
                } else {
                    CustomBlocksMod.LOGGER.info("Failed to generate block!");
                }
            }
            if (woodPack.has("stairs")) {
                JsonObject stairs = woodPack.getAsJsonObject("stairs");
                if (stairs.has("id") && stairs.has("display_name") && stairs.has("top_texture") && stairs.has("side_texture") && stairs.has("bottom_texture")) {
                    JsonObject block = new JsonObject();
                    block.addProperty("namespace", woodPack.get("namespace").getAsString());
                    block.addProperty("id", stairs.get("id").getAsString());
                    block.addProperty("display_name", stairs.get("display_name").getAsString());
                    block.addProperty("base", "stairs");
                    block.addProperty("material", "wood");
                    block.addProperty("hardness", 2);
                    block.addProperty("resistance", 3);
                    if (stairs.has("has_gravity"))
                        block.addProperty("has_gravity", stairs.get("has_gravity").getAsBoolean());
                    else block.addProperty("has_gravity", false);
                    if (stairs.has("map_color"))
                        block.addProperty("map_color", stairs.get("map_color").getAsString());
                    else block.addProperty("map_color", "default");
                    block.addProperty("sounds", "wood");
                    block.addProperty("efficient_tool", "axe");
                    block.addProperty("requires_tool", false);
                    block.addProperty("harvest_level", 0);
                    if (stairs.has("texture_namespace"))
                        block.addProperty("texture_namespace", stairs.get("texture_namespace").getAsString());
                    else block.addProperty("texture_namespace", block.get("namespace").getAsString());
                    block.add("textures", new JsonObject());
                    JsonObject textures = block.getAsJsonObject("textures");
                    textures.addProperty("top_texture", stairs.get("top_texture").getAsString());
                    textures.addProperty("bottom_texture", stairs.get("bottom_texture").getAsString());
                    textures.addProperty("front_texture", stairs.get("side_texture").getAsString());
                    textures.addProperty("back_texture", stairs.get("side_texture").getAsString());
                    textures.addProperty("right_texture", stairs.get("side_texture").getAsString());
                    textures.addProperty("left_texture", stairs.get("side_texture").getAsString());
                    if (BlockGenerator.add(block) && ResourcePackGenerator.generateBlockResources(block, block.getAsJsonObject("textures").get("top_texture").getAsString(), block.getAsJsonObject("textures").get("bottom_texture").getAsString(), block.getAsJsonObject("textures").get("front_texture").getAsString(), block.getAsJsonObject("textures").get("back_texture").getAsString(), block.getAsJsonObject("textures").get("right_texture").getAsString(), block.getAsJsonObject("textures").get("left_texture").getAsString()) && LanguageHandler.addBlockKey(block)) {
                        CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
                    } else {
                        CustomBlocksMod.LOGGER.info("Failed to generate block!");
                    }
                } else {
                    CustomBlocksMod.LOGGER.info("Failed to generate block!");
                }
            }
            if (woodPack.has("fence")) {
                JsonObject fence = woodPack.getAsJsonObject("fence");
                if (fence.has("id") && fence.has("display_name") && fence.has("texture")) {
                    JsonObject block = new JsonObject();
                    block.addProperty("namespace", woodPack.get("namespace").getAsString());
                    block.addProperty("id", fence.get("id").getAsString());
                    block.addProperty("display_name", fence.get("display_name").getAsString());
                    block.addProperty("base", "fence");
                    block.addProperty("material", "wood");
                    block.addProperty("hardness", 2);
                    block.addProperty("resistance", 3);
                    if (fence.has("has_gravity"))
                        block.addProperty("has_gravity", fence.get("has_gravity").getAsBoolean());
                    else block.addProperty("has_gravity", false);
                    if (fence.has("map_color"))
                        block.addProperty("map_color", fence.get("map_color").getAsString());
                    else block.addProperty("map_color", "default");
                    block.addProperty("sounds", "wood");
                    block.addProperty("efficient_tool", "axe");
                    block.addProperty("requires_tool", false);
                    block.addProperty("harvest_level", 0);
                    if (fence.has("texture_namespace"))
                        block.addProperty("texture_namespace", fence.get("texture_namespace").getAsString());
                    else block.addProperty("texture_namespace", block.get("namespace").getAsString());
                    block.add("textures", new JsonObject());
                    JsonObject textures = block.getAsJsonObject("textures");
                    textures.addProperty("top_texture", fence.get("texture").getAsString());
                    textures.addProperty("bottom_texture", fence.get("texture").getAsString());
                    textures.addProperty("front_texture", fence.get("texture").getAsString());
                    textures.addProperty("back_texture", fence.get("texture").getAsString());
                    textures.addProperty("right_texture", fence.get("texture").getAsString());
                    textures.addProperty("left_texture", fence.get("texture").getAsString());
                    if (BlockGenerator.add(block) && ResourcePackGenerator.generateBlockResources(block, block.getAsJsonObject("textures").get("top_texture").getAsString(), block.getAsJsonObject("textures").get("bottom_texture").getAsString(), block.getAsJsonObject("textures").get("front_texture").getAsString(), block.getAsJsonObject("textures").get("back_texture").getAsString(), block.getAsJsonObject("textures").get("right_texture").getAsString(), block.getAsJsonObject("textures").get("left_texture").getAsString()) && LanguageHandler.addBlockKey(block)) {
                        CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
                    } else {
                        CustomBlocksMod.LOGGER.info("Failed to generate block!");
                    }
                } else {
                    CustomBlocksMod.LOGGER.info("Failed to generate block!");
                }
            }
            if (woodPack.has("fence_gate")) {
                JsonObject fenceGate = woodPack.getAsJsonObject("fence_gate");
                if (fenceGate.has("id") && fenceGate.has("display_name") && fenceGate.has("texture")) {
                    JsonObject block = new JsonObject();
                    block.addProperty("namespace", woodPack.get("namespace").getAsString());
                    block.addProperty("id", fenceGate.get("id").getAsString());
                    block.addProperty("display_name", fenceGate.get("display_name").getAsString());
                    block.addProperty("base", "fence_gate");
                    block.addProperty("material", "wood");
                    block.addProperty("hardness", 2);
                    block.addProperty("resistance", 3);
                    if (fenceGate.has("has_gravity"))
                        block.addProperty("has_gravity", fenceGate.get("has_gravity").getAsBoolean());
                    else block.addProperty("has_gravity", false);
                    if (fenceGate.has("map_color"))
                        block.addProperty("map_color", fenceGate.get("map_color").getAsString());
                    else block.addProperty("map_color", "default");
                    block.addProperty("sounds", "wood");
                    block.addProperty("efficient_tool", "axe");
                    block.addProperty("requires_tool", false);
                    block.addProperty("harvest_level", 0);
                    if (fenceGate.has("texture_namespace"))
                        block.addProperty("texture_namespace", fenceGate.get("texture_namespace").getAsString());
                    else block.addProperty("texture_namespace", block.get("namespace").getAsString());
                    block.add("textures", new JsonObject());
                    JsonObject textures = block.getAsJsonObject("textures");
                    textures.addProperty("top_texture", fenceGate.get("texture").getAsString());
                    textures.addProperty("bottom_texture", fenceGate.get("texture").getAsString());
                    textures.addProperty("front_texture", fenceGate.get("texture").getAsString());
                    textures.addProperty("back_texture", fenceGate.get("texture").getAsString());
                    textures.addProperty("right_texture", fenceGate.get("texture").getAsString());
                    textures.addProperty("left_texture", fenceGate.get("texture").getAsString());
                    if (BlockGenerator.add(block) && ResourcePackGenerator.generateBlockResources(block, block.getAsJsonObject("textures").get("top_texture").getAsString(), block.getAsJsonObject("textures").get("bottom_texture").getAsString(), block.getAsJsonObject("textures").get("front_texture").getAsString(), block.getAsJsonObject("textures").get("back_texture").getAsString(), block.getAsJsonObject("textures").get("right_texture").getAsString(), block.getAsJsonObject("textures").get("left_texture").getAsString()) && LanguageHandler.addBlockKey(block)) {
                        CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
                    } else {
                        CustomBlocksMod.LOGGER.info("Failed to generate block!");
                    }
                } else {
                    CustomBlocksMod.LOGGER.info("Failed to generate block!");
                }
            }
        }
        if (pack.has("blocks")) {
            List<JsonObject> blocks = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("blocks"));
            for (JsonObject block : blocks) {
                if (BlockGenerator.add(block) && ResourcePackGenerator.generateBlockResources(block, block.get("textures").getAsJsonObject().get("top_texture").getAsString(), block.get("textures").getAsJsonObject().get("bottom_texture").getAsString(), block.get("textures").getAsJsonObject().get("front_texture").getAsString(), block.get("textures").getAsJsonObject().get("back_texture").getAsString(), block.get("textures").getAsJsonObject().get("right_texture").getAsString(), block.get("textures").getAsJsonObject().get("left_texture").getAsString()) && LanguageHandler.addBlockKey(block)) {
                    CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.info("Failed to generate block!");
                }
            }
        }
        if (pack.has("items")) {
            List<JsonObject> items = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("items"));
            for (JsonObject item : items) {
                if (ItemGenerator.add(item) && ResourcePackGenerator.generateItemResources(item, item.get("namespace").getAsString(), item.get("id").getAsString(), item.get("texture").getAsString()) && LanguageHandler.addItemKey(item)) {
                    CustomBlocksMod.LOGGER.info("Generated Item " + item.get("namespace").getAsString() + ":" + item.get("id").getAsString());
                } else {
                    CustomBlocksMod.LOGGER.info("Failed to generate item!");
                }
            }
        }
    }
}
