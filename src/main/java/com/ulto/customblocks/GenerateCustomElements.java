package com.ulto.customblocks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.ulto.customblocks.event.global.GlobalEvents;
import com.ulto.customblocks.util.BooleanUtils;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class GenerateCustomElements {
	public static File blocksFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "blocks");
	public static List<File> blocks = new ArrayList<>();
	public static File itemsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "items");
	public static List<File> items = new ArrayList<>();
	public static File fluidsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "fluids");
	public static List<File> fluids = new ArrayList<>();
	public static File itemGroupsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "item_groups");
	public static List<File> itemGroups = new ArrayList<>();
	public static File paintingsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "paintings");
	public static List<File> paintings = new ArrayList<>();
	public static File recipesFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "recipes");
	public static List<File> recipes = new ArrayList<>();
	public static File globalEventsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "global_events");
	public static List<File> globalEvents = new ArrayList<>();
	public static File packsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "packs");
	public static List<File> packs = new ArrayList<>();

	public static void generate() {
		blocksFolder.mkdirs();
		itemsFolder.mkdirs();
		fluidsFolder.mkdirs();
		itemGroupsFolder.mkdirs();
		paintingsFolder.mkdirs();
		recipesFolder.mkdirs();
		globalEventsFolder.mkdirs();
		packsFolder.mkdirs();
		copyOldFiles();
		listFiles(blocksFolder, blocks, ".json");
		listFiles(itemsFolder, items, ".json");
		listFiles(fluidsFolder, fluids, ".json");
		listFiles(itemGroupsFolder, itemGroups, ".json");
		listFiles(paintingsFolder, paintings, ".json");
		listFiles(recipesFolder, recipes, ".json");
		listFiles(globalEventsFolder, globalEvents, ".json");
		listFiles(packsFolder, packs, ".json");
		LanguageHandler.setupLanguage();
		CustomResourceCreator.setupResourcePack();
		for (File value : blocks) {
			try {
				BufferedReader blockReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = blockReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject block = new Gson().fromJson(json.toString(), JsonObject.class);
				if (block.has("format_version")) {
					if (!BlockGenerator.addBedrock(block, value)) {
						CustomBlocksMod.LOGGER.error("Failed to generate block " + value.getName() + "!");
					}
				} else {
					if (BlockGenerator.add(block) && CustomResourceCreator.generateBlockResources(block) && LanguageHandler.addBlockKey(block)) {
						CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
					} else {
						CustomBlocksMod.LOGGER.error("Failed to generate block " + value.getName() + "!");
					}
				}
				blockReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to generate block " + value.getName() + "!");
			}
		}
		for (File value : items) {
			try {
				BufferedReader itemReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = itemReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject item = new Gson().fromJson(json.toString(), JsonObject.class);
				if (ItemGenerator.add(item) && CustomResourceCreator.generateItemResources(item) && LanguageHandler.addItemKey(item)) {
					CustomBlocksMod.LOGGER.info("Generated Item " + item.get("namespace").getAsString() + ":" + item.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate item " + value.getName() + "!");
				}
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to generate item " + value.getName() + "!");
			}
		}
		for (File value : fluids) {
			try {
				BufferedReader packReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = packReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject fluid = new Gson().fromJson(json.toString(), JsonObject.class);
				if (FluidGenerator.add(fluid) && CustomResourceCreator.generateFluidResources(fluid) && LanguageHandler.addFluidKeys(fluid)) {
					CustomBlocksMod.LOGGER.info("Generated Fluid {}", fluid.get("namespace").getAsString() + ":" + fluid.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate fluid {}!", value.getName());
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to generate fluid " + value.getName() + "!");
			}
		}
		for (File value : itemGroups) {
			try {
				BufferedReader packReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = packReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject itemGroup = new Gson().fromJson(json.toString(), JsonObject.class);
				if (ItemGroupGenerator.add(itemGroup) && LanguageHandler.addItemGroupKey(itemGroup)) {
					CustomBlocksMod.LOGGER.info("Generated Item Group " + itemGroup.get("namespace").getAsString() + ":" + itemGroup.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate item group " + value.getName() + "!");
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to generate item group " + value.getName() + "!");
			}
		}
		for (File value : paintings) {
			try {
				BufferedReader packReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = packReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject painting = new Gson().fromJson(json.toString(), JsonObject.class);
				if (PaintingGenerator.add(painting) && CustomResourceCreator.generatePaintingResources(painting)) {
					CustomBlocksMod.LOGGER.info("Generated Painting " + painting.get("namespace").getAsString() + ":" + painting.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate painting " + value.getName() + "!");
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to generate painting " + value.getName() + "!");
			}
		}
		for (File value : recipes) {
			try {
				BufferedReader packReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = packReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject recipe = new Gson().fromJson(json.toString(), JsonObject.class);
				if (RecipeGenerator.add(recipe)) {
					CustomBlocksMod.LOGGER.info("Generated Recipe {}", recipe.getAsJsonObject("custom").get("namespace").getAsString() + ":" + recipe.getAsJsonObject("custom").get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate recipe {}!", value.getName());
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to generate recipe " + value.getName() + "!");
			}
		}
		for (File value : globalEvents) {
			try {
				BufferedReader packReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = packReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject event = new Gson().fromJson(json.toString(), JsonObject.class);
				if (GlobalEvents.register(event)) {
					CustomBlocksMod.LOGGER.info("Registered Global Event {}", event.get("namespace").getAsString() + ":" + event.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to register global event {}!", value.getName());
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to register global event " + value.getName() + "!");
			}
		}
		for (File value : packs) {
			try {
				BufferedReader packReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = packReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject pack = new Gson().fromJson(json.toString(), JsonObject.class);
				if (pack.has("name")) {
					CustomBlocksMod.LOGGER.info("Loading Pack " + pack.get("name").getAsString() + "...");
					PackGenerator.add(pack);
					CustomBlocksMod.LOGGER.info("Loaded Pack " + pack.get("name").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to load pack " + value.getName() + "!");
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to load pack " + value.getName() + "!");
			}
		}
		LanguageHandler.saveLang();
	}

	@Environment(EnvType.CLIENT)
	public static void generateClient() {
		for (File value : blocks) {
			try {
				BufferedReader test_blockReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = test_blockReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject block = new Gson().fromJson(json.toString(), JsonObject.class);
				if (BooleanUtils.isValidBlock(block) && block.has("render_type")) {
					switch (block.get("render_type").getAsString()) {
						case "cutout" -> BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderLayer.getCutout());
						case "cutout_mipped" -> BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderLayer.getCutoutMipped());
						case "translucent" -> BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderLayer.getTranslucent());
					}
				}
				test_blockReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to generate block " + value.getName() + "!");
			}
		}
		for (File value : fluids) {
			try {
				BufferedReader test_blockReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = test_blockReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject fluid = new Gson().fromJson(json.toString(), JsonObject.class);
				if (BooleanUtils.isValidFluid(fluid)) {
					setupFluidRendering(Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), fluid.get("id").getAsString())), Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), "flowing_" + fluid.get("id").getAsString())), new Identifier(fluid.get("texture").getAsString()));
					BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), fluid.get("id").getAsString())), Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), "flowing_" + fluid.get("id").getAsString())));
				}
				test_blockReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Failed to generate block " + value.getName() + "!");
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

	private static void listFiles(final File folder, List<File> list, String fileExtension) {
		if (folder.exists()) {
			for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
				if (fileEntry.isDirectory()) {
					listFiles(fileEntry, list, fileExtension);
				} else {
					if (fileEntry.getName().endsWith(fileExtension) || fileExtension.equals("*")) list.add(fileEntry);
				}
			}
		}
	}

	private static List<File> listFiles(final File folder, String fileExtension) {
		List<File> list = new ArrayList<>();
		if (folder.exists()) {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					listFiles(fileEntry, list, fileExtension);
				} else {
					if (fileEntry.getName().endsWith(fileExtension) || fileExtension.equals("*")) list.add(fileEntry);
				}
			}
		}
		return list;
	}

	public static File blocksFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "blocks");
	public static List<File> blocksFv0 = listFiles(blocksFolderFv0, ".json");
	public static File itemsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "items");
	public static List<File> itemsFv0 = listFiles(itemsFolderFv0, ".json");
	public static File fluidsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "fluids");
	public static List<File> fluidsFv0 = listFiles(fluidsFolderFv0, ".json");
	public static File itemGroupsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "itemgroups");
	public static List<File> itemGroupsFv0 = listFiles(itemGroupsFolderFv0, ".json");
	public static File paintingsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "paintings");
	public static List<File> paintingsFv0 = listFiles(paintingsFolderFv0, ".json");
	public static File recipesFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "recipes");
	public static List<File> recipesFv0 = listFiles(recipesFolderFv0, ".json");
	public static File globalEventsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "global_events");
	public static List<File> globalEventsFv0 = listFiles(globalEventsFolderFv0, ".json");
	public static File packsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "packs");
	public static List<File> packsFv0 = listFiles(packsFolderFv0, ".json");

	private static void copyOldFiles() {
		for (File fv0 : blocksFv0) {
			try {
				Files.copy(fv0.toPath(), blocksFolder.toPath().resolve(fv0.getName()));
				fv0.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File fv0 : itemsFv0) {
			try {
				Files.copy(fv0.toPath(), itemsFolder.toPath().resolve(fv0.getName()));
				fv0.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File fv0 : fluidsFv0) {
			try {
				Files.copy(fv0.toPath(), fluidsFolder.toPath().resolve(fv0.getName()));
				fv0.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File fv0 : itemGroupsFv0) {
			try {
				Files.copy(fv0.toPath(), itemGroupsFolder.toPath().resolve(fv0.getName()));
				fv0.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File fv0 : paintingsFv0) {
			try {
				Files.copy(fv0.toPath(), paintingsFolder.toPath().resolve(fv0.getName()));
				fv0.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File fv0 : recipesFv0) {
			try {
				Files.copy(fv0.toPath(), recipesFolder.toPath().resolve(fv0.getName()));
				fv0.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File fv0 : globalEventsFv0) {
			try {
				Files.copy(fv0.toPath(), globalEventsFolder.toPath().resolve(fv0.getName()));
				fv0.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File fv0 : packsFv0) {
			try {
				Files.copy(fv0.toPath(), packsFolder.toPath().resolve(fv0.getName()));
				fv0.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		blocksFolderFv0.delete();
		itemsFolderFv0.delete();
		fluidsFolderFv0.delete();
		itemGroupsFolderFv0.delete();
		paintingsFolderFv0.delete();
		recipesFolderFv0.delete();
		globalEventsFolderFv0.delete();
		packsFolderFv0.delete();
	}
}
