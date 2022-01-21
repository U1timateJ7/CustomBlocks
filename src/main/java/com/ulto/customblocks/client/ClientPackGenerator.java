package com.ulto.customblocks.client;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.BooleanUtils;
import com.ulto.customblocks.util.JsonUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ClientPackGenerator {
    public static void addClient(JsonObject pack) {
        if (pack.has("blocks")) {
            List<JsonObject> blocks = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("blocks"));
            for (JsonObject block : blocks) {
                if (BooleanUtils.isValidBlock(block) && block.has("render_type")) {
                    switch (block.get("render_type").getAsString()) {
                        case "cutout" -> BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderLayer.getCutout());
                        case "cutout_mipped" -> BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderLayer.getCutoutMipped());
                        case "translucent" -> BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderLayer.getTranslucent());
                    }
                }
            }
        }
        if (pack.has("fluids")) {
            List<JsonObject> fluids = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("fluids"));
            for (JsonObject fluid : fluids) {
                if (BooleanUtils.isValidFluid(fluid)) {
                    setupFluidRendering(Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), fluid.get("id").getAsString())), Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), "flowing_" + fluid.get("id").getAsString())), new Identifier(fluid.get("texture").getAsString()));
                    BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), fluid.get("id").getAsString())), Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), "flowing_" + fluid.get("id").getAsString())));
                }
            }
        }
        if (pack.has("trees")) {
            List<JsonObject> trees = JsonUtils.jsonArrayToJsonObjectList(pack.getAsJsonArray("trees"));
            for (JsonObject tree : trees) {
                if (tree.has("has_sapling")) {
                    if (tree.get("has_sapling").getAsBoolean()) BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(tree.get("namespace").getAsString(), (new Object() {
                        String getSaplingId(String id) {
                            String saplingId = id.replace("_tree", "_sapling");
                            if (!saplingId.contains("_sapling")) saplingId += "_sapling";
                            return saplingId;
                        }
                    }).getSaplingId(tree.get("id").getAsString()))), RenderLayer.getCutout());
                }
            }
        }
    }

    public static void setupFluidRendering(final Fluid still, final Fluid flowing, final Identifier textureFluidId) {
        final Identifier stillSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_still");
        final Identifier flowingSpriteId = new Identifier(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_flow");

        // If they're not already present, add the sprites to the block atlas
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(stillSpriteId);
            registry.register(flowingSpriteId);
        });

        final Identifier fluidId = Registry.FLUID.getId(still);
        final Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");

        final Sprite[] fluidSprites = { null, null };

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return listenerId;
            }

            @Override
            public void reload(ResourceManager resourceManager) {
                final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
                fluidSprites[0] = atlas.apply(stillSpriteId);
                fluidSprites[1] = atlas.apply(flowingSpriteId);
            }
        });

        // The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
        final FluidRenderHandler renderHandler = (view, pos, state) -> fluidSprites;

        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
    }
}
