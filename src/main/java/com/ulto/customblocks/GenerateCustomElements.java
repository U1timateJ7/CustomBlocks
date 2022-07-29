package com.ulto.customblocks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.ulto.customblocks.client.ClientEntityGenerator;
import com.ulto.customblocks.client.ClientPackGenerator;
import com.ulto.customblocks.event.global.GlobalEvents;
import com.ulto.customblocks.util.BooleanUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenerateCustomElements {
	public static File blocksFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "blocks");
	public static List<File> blocks = new ArrayList<>();
	public static File blockModelsFolder = new File(blocksFolder, File.separator + "models");
	public static List<File> blockModels = new ArrayList<>();
	public static File blockstatesFolder = new File(blocksFolder, File.separator + "blockstates");
	public static List<File> blockstates = new ArrayList<>();
	public static File blockItemModelsFolder = new File(blocksFolder, File.separator + "item_models");
	public static List<File> blockItemModels = new ArrayList<>();
	public static File itemsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "items");
	public static List<File> items = new ArrayList<>();
	public static File itemModelsFolder = new File(itemsFolder, File.separator + "models");
	public static List<File> itemModels = new ArrayList<>();
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
	public static File bannerPatternFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "banner_patterns");
	public static List<File> bannerPatterns = new ArrayList<>();
	public static File catVariantFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "cat_variants");
	public static List<File> catVariants = new ArrayList<>();
	public static File packsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "packs");
	public static List<File> packs = new ArrayList<>();

	public static void generate() {
		blocksFolder.mkdirs();
		itemsFolder.mkdirs();
		entityModelsFolder.mkdirs();
		itemGroupsFolder.mkdirs();
		paintingsFolder.mkdirs();
		recipesFolder.mkdirs();
		globalEventsFolder.mkdirs();
		bannerPatternFolder.mkdirs();
		catVariantFolder.mkdirs();
		packsFolder.mkdirs();
		listFiles(blocksFolder, blocks, ".json", "models", "blockstates", "item_models");
		if (blockModelsFolder.exists()) listFiles(blockModelsFolder, blockModels, ".json");
		if (blockstatesFolder.exists()) listFiles(blockstatesFolder, blockstates, ".json");
		if (blockItemModelsFolder.exists()) listFiles(blockItemModelsFolder, blockItemModels, ".json");
		listFiles(itemsFolder, items, ".json", "models");
		if (itemModelsFolder.exists()) listFiles(itemModelsFolder, itemModels, ".json");
		listFiles(entitiesFolder, entities, ".json", "models");
		listFiles(entityModelsFolder, entityModels, ".json");
		listFiles(itemGroupsFolder, itemGroups, ".json");
		listFiles(paintingsFolder, paintings, ".json");
		listFiles(recipesFolder, recipes, ".json");
		listFiles(globalEventsFolder, globalEvents, ".json");
		listFiles(bannerPatternFolder, bannerPatterns, ".json");
		listFiles(catVariantFolder, catVariants, ".json");
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
						CustomBlocksMod.LOGGER.error("Failed to generate bedrock edition block " + value.getName() + "!");
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
		for (File value : blockModels) {
			try {
				BufferedReader itemReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = itemReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject blockModel = new Gson().fromJson(json.toString(), JsonObject.class);
				CustomResourceCreator.blockModels.put(value.getName().replace(".json", ""), blockModel);
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
			}
		}
		for (File value : blockstates) {
			try {
				BufferedReader itemReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = itemReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject blockstate = new Gson().fromJson(json.toString(), JsonObject.class);
				CustomResourceCreator.blockstates.put(value.getName().replace(".json", ""), blockstate);
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
			}
		}
		for (File value : blockItemModels) {
			try {
				BufferedReader itemReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = itemReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject blockItemModel = new Gson().fromJson(json.toString(), JsonObject.class);
				CustomResourceCreator.blockItemModels.put(value.getName().replace(".json", ""), blockItemModel);
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
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
		for (File value : itemModels) {
			try {
				BufferedReader itemReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = itemReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject itemModel = new Gson().fromJson(json.toString(), JsonObject.class);
				CustomResourceCreator.itemModels.put(value.getName().replace(".json", ""), itemModel);
				itemReader.close();
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
		for (File value : bannerPatterns) {
			try {
				BufferedReader packReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = packReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject bannerPattern = new Gson().fromJson(json.toString(), JsonObject.class);
				if (BannerPatternGenerator.add(bannerPattern) && CustomResourceCreator.generateBannerPatternResources(bannerPattern) && LanguageHandler.addBannerPatternKey(bannerPattern)) {
					CustomBlocksMod.LOGGER.info("Generated Banner Pattern {}", bannerPattern.get("namespace").getAsString() + ":" + bannerPattern.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate banner pattern {}!", value.getName());
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Banner pattern " + value.getName() + " has an invalid JSON file!");
			}
		}
		for (File value : catVariants) {
			try {
				BufferedReader packReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = packReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject catVariant = new Gson().fromJson(json.toString(), JsonObject.class);
				if (CatVariantGenerator.add(catVariant) && CustomResourceCreator.generateCatVariantResources(catVariant)) {
					CustomBlocksMod.LOGGER.info("Generated Cat Variant {}", catVariant.get("namespace").getAsString() + ":" + catVariant.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate cat variant {}!", value.getName());
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Cat variant " + value.getName() + " has an invalid JSON file!");
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

	@OnlyIn(Dist.CLIENT)
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
						case "cutout" -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.cutout());
						case "cutout_mipped" -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.cutoutMipped());
						case "translucent" -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.translucent());
					}
				}
				test_blockReader.close();
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
				ClientEntityGenerator.entityModels.put(model.get("name").getAsString(), model);
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				CustomBlocksMod.LOGGER.error("Entity model " + value.getName() + " has an invalid JSON file!");
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

	public static List<File> listFiles(final File folder, String fileExtension, String... excludedSubfolders) {
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
}