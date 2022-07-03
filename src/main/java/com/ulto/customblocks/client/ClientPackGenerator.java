package com.ulto.customblocks.client;

import com.google.gson.JsonObject;
import com.ulto.customblocks.CustomBlocksMod;
import com.ulto.customblocks.util.BooleanUtils;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientPackGenerator {
    public static void addClient(JsonObject pack) {
        if (pack.has("blocks")) {
            List<JsonObject> blocks = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("blocks"));
            for (JsonObject block : blocks) {
                if (BooleanUtils.isValidBlock(block) && block.has("render_type")) {
                    switch (block.get("render_type").getAsString()) {
                        case "cutout" -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.cutout());
                        case "cutout_mipped" -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.cutoutMipped());
                        case "translucent" -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.translucent());
                    }
                }
            }
        }
        if (pack.has("entities")) {
            List<JsonObject> entities = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("entities"));
            for (JsonObject entity : entities) {
                if (ClientEntityGenerator.addClient(entity)) CustomBlocksMod.LOGGER.info("Created Entity Renderer for " + entity.get("namespace").getAsString() + ":" + entity.get("id").getAsString());
            }
        }
        if (pack.has("trees")) {
            List<JsonObject> trees = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("trees"));
            for (JsonObject tree : trees) {
                if (tree.has("has_sapling")) {
                    if (tree.get("has_sapling").getAsBoolean()) ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tree.get("namespace").getAsString(), (new Object() {
                        String getSaplingId(String id) {
                            String saplingId = id.replace("_tree", "_sapling");
                            if (!saplingId.contains("_sapling")) saplingId += "_sapling";
                            return saplingId;
                        }
                    }).getSaplingId(tree.get("id").getAsString()))), RenderType.cutout());
                }
            }
        }
    }
}
