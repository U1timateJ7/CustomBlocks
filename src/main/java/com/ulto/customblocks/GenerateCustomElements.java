package com.ulto.customblocks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.ulto.customblocks.client.ClientEntityGenerator;
import com.ulto.customblocks.client.ClientPackGenerator;
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
import net.fabricmc.loader.api.FabricLoader;
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
	public static File entitiesFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "entities");
	public static List<File> entities = new ArrayList<>();
	public static File entityModelsFolder = new File(entitiesFolder, File.separator + "models");
	public static List<File> entityModels = new ArrayList<>();
	public static File itemGroupsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "item_groups");
	public static List<File> itemGroups = new ArrayList<>();
	public static File paintingsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "paintings");
	public static List<File> paintings = new ArrayList<>();
	public static File recipesFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "recipes");
	public static List<File> recipes = new ArrayList<>();
	public static File globalEventsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "global_events");
	public static List<File> globalEvents = new ArrayList<>();
	public static File treesFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "trees");
	public static List<File> trees = new ArrayList<>();
	public static File packsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "packs");
	public static List<File> packs = new ArrayList<>();

	public static void generate() {
		blocksFolder.mkdirs();
		itemsFolder.mkdirs();
		fluidsFolder.mkdirs();
		entityModelsFolder.mkdirs();
		itemGroupsFolder.mkdirs();
		paintingsFolder.mkdirs();
		recipesFolder.mkdirs();
		globalEventsFolder.mkdirs();
		treesFolder.mkdirs();
		packsFolder.mkdirs();
		copyOldFiles();
		listFiles(blocksFolder, blocks, ".json");
		listFiles(itemsFolder, items, ".json");
		listFiles(fluidsFolder, fluids, ".json");
		listFiles(entitiesFolder, entities, ".json", "models");
		listFiles(entityModelsFolder, entityModels, ".json");
		listFiles(itemGroupsFolder, itemGroups, ".json");
		listFiles(paintingsFolder, paintings, ".json");
		listFiles(recipesFolder, recipes, ".json");
		listFiles(globalEventsFolder, globalEvents, ".json");
		listFiles(treesFolder, trees, ".json");
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
				CustomBlocksMod.LOGGER.error("Block " + value.getName() + " has an invalid JSON file!");
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
				CustomBlocksMod.LOGGER.error("Item " + value.getName() + " has an invalid JSON file!");
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
				CustomBlocksMod.LOGGER.error("Fluid " + value.getName() + " has an invalid JSON file!");
			}
		}
		for (File value : entities) {
			try {
				BufferedReader itemReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = itemReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject entity = new Gson().fromJson(json.toString(), JsonObject.class);
				if (EntityGenerator.add(entity) && CustomResourceCreator.generateEntityResources(entity) && LanguageHandler.addEntityKey(entity)) {
					CustomBlocksMod.LOGGER.info("Generated Entity " + entity.get("namespace").getAsString() + ":" + entity.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate entity " + value.getName() + "!");
				}
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Entity " + value.getName() + " has an invalid JSON file!");
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
				CustomBlocksMod.LOGGER.error("Item group " + value.getName() + " has an invalid JSON file!");
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
				CustomBlocksMod.LOGGER.error("Painting " + value.getName() + " has an invalid JSON file!");
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
				CustomBlocksMod.LOGGER.error("Recipe " + value.getName() + " has an invalid JSON file!");
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
				CustomBlocksMod.LOGGER.error("Global event " + value.getName() + " has an invalid JSON file!");
			}
		}
		for (File value : trees) {
			try {
				BufferedReader itemReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = itemReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject tree = new Gson().fromJson(json.toString(), JsonObject.class);
				if (TreeGenerator.add(tree) && CustomResourceCreator.generateSaplingResources(tree) && LanguageHandler.addSaplingKey(tree)) {
					CustomBlocksMod.LOGGER.info("Generated Tree " + tree.get("namespace").getAsString() + ":" + tree.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate tree " + value.getName() + "!");
				}
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Tree " + value.getName() + " has an invalid JSON file!");
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
				CustomBlocksMod.LOGGER.error("Pack " + value.getName() + " has an invalid JSON file!");
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
			}
		}
		for (File value : entities) {
			try {
				BufferedReader itemReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = itemReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject entity = new Gson().fromJson(json.toString(), JsonObject.class);
				if (ClientEntityGenerator.addClient(entity)) CustomBlocksMod.LOGGER.info("Created Entity Renderer for " + entity.get("namespace").getAsString() + ":" + entity.get("id").getAsString());
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
			}
		}
		for (File value : entityModels) {
			try {
				BufferedReader itemReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = itemReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject model = new Gson().fromJson(json.toString(), JsonObject.class);
				EntityGenerator.entityModels.put(model.get("name").getAsString(), model);
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Entity model " + value.getName() + " has an invalid JSON file!");
			}
		}
		for (File value : trees) {
			try {
				BufferedReader test_blockReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = test_blockReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject tree = new Gson().fromJson(json.toString(), JsonObject.class);
				if (tree.has("has_sapling")) {
					if (tree.get("has_sapling").getAsBoolean()) {
						BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(tree.get("namespace").getAsString(), TreeGenerator.getSaplingId(tree.get("id").getAsString()))), RenderLayer.getCutout());
						BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(tree.get("namespace").getAsString(), "potted_" + TreeGenerator.getSaplingId(tree.get("id").getAsString()))), RenderLayer.getCutout());
					}
				}
				test_blockReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
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
					ClientPackGenerator.addClient(pack);
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
			}
		}
	}

	@Environment(EnvType.CLIENT)
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

	private static void listFiles(final File folder, List<File> list, String fileExtension, String... excludedSubfolders) {
		if (folder.exists()) {
			for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
				if (fileEntry.isDirectory()) {
					boolean isExcluded = false;
					for (String excludedName : excludedSubfolders) {
						if (fileEntry.getName().contains(excludedName)) {
							isExcluded = true;
							break;
						}
					}
					if (!isExcluded) listFiles(fileEntry, list, fileExtension);
				} else {
					if (fileEntry.getName().endsWith(fileExtension) || fileExtension.equals("*")) list.add(fileEntry);
				}
			}
		}
	}

	private static List<File> listFiles(final File folder, String fileExtension, String... excludedSubfolders) {
		List<File> list = new ArrayList<>();
		if (folder.exists()) {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					boolean isExcluded = false;
					for (String excludedName : excludedSubfolders) {
						if (fileEntry.getName().contains(excludedName)) {
							isExcluded = true;
							break;
						}
					}
					if (!isExcluded) listFiles(fileEntry, list, fileExtension);
				} else {
					if (fileEntry.getName().endsWith(fileExtension) || fileExtension.equals("*")) list.add(fileEntry);
				}
			}
		}
		return list;
	}

	public static File blocksFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "blocks");
	public static List<File> blocksFv0 = listFiles(blocksFolderFv0, ".json");
	public static File itemsFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "items");
	public static List<File> itemsFv0 = listFiles(itemsFolderFv0, ".json");
	public static File fluidsFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "fluids");
	public static List<File> fluidsFv0 = listFiles(fluidsFolderFv0, ".json");
	public static File entitiesFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "entities");
	public static List<File> entitiesFv0 = listFiles(entitiesFolderFv0, ".json", "models");
	public static File entityModelsFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "models");
	public static List<File> entityModelsFv0 = listFiles(entityModelsFolderFv0, ".json");
	public static File itemGroupsFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "itemgroups");
	public static List<File> itemGroupsFv0 = listFiles(itemGroupsFolderFv0, ".json");
	public static File paintingsFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "paintings");
	public static List<File> paintingsFv0 = listFiles(paintingsFolderFv0, ".json");
	public static File recipesFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "recipes");
	public static List<File> recipesFv0 = listFiles(recipesFolderFv0, ".json");
	public static File globalEventsFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "global_events");
	public static List<File> globalEventsFv0 = listFiles(globalEventsFolderFv0, ".json");
	public static File treesFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "trees");
	public static List<File> treesFv0 = listFiles(treesFolderFv0, ".json");
	public static File packsFolderFv0 = new File(FabricLoader.getInstance().getGameDir().toFile(), File.separator + "packs");
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
		for (File fv0 : entitiesFv0) {
			try {
				Files.copy(fv0.toPath(), entitiesFolder.toPath().resolve(fv0.getName()));
				fv0.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (File fv0 : entityModelsFv0) {
			try {
				Files.copy(fv0.toPath(), entityModelsFolder.toPath().resolve(fv0.getName()));
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
		for (File fv0 : treesFv0) {
			try {
				Files.copy(fv0.toPath(), treesFolder.toPath().resolve(fv0.getName()));
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
		entityModelsFolderFv0.delete();
		entitiesFolderFv0.delete();
		itemGroupsFolderFv0.delete();
		paintingsFolderFv0.delete();
		recipesFolderFv0.delete();
		globalEventsFolderFv0.delete();
		treesFolderFv0.delete();
		packsFolderFv0.delete();
	}
}
