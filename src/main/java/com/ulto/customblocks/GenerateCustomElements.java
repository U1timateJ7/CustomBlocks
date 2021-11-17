package com.ulto.customblocks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.ulto.customblocks.event.global.GlobalEvents;
import com.ulto.customblocks.util.BooleanUtils;
import net.minecraft.client.Minecraft;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenerateCustomElements {
	public static File blocksFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "blocks");
	public static List<File> blocks = new ArrayList<>();
	public static File itemsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "items");
	public static List<File> items = new ArrayList<>();
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
		itemGroupsFolder.mkdirs();
		paintingsFolder.mkdirs();
		recipesFolder.mkdirs();
		globalEventsFolder.mkdirs();
		packsFolder.mkdirs();
		copyOldFiles();
		listFiles(blocksFolder, blocks, ".json");
		listFiles(itemsFolder, items, ".json");
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
				CustomBlocksMod.LOGGER.error("Failed to generate block " + value.getName() + "!");
			}
		}
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

	public static File blocksFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "blocks");
	public static List<File> blocksFv0 = listFiles(blocksFolderFv0, ".json");
	public static File itemsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "items");
	public static List<File> itemsFv0 = listFiles(itemsFolderFv0, ".json");
	public static File itemGroupsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "itemgroups");
	public static List<File> itemGroupsFv0 = listFiles(itemGroupsFolderFv0, ".json");
	public static File paintingsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "paintings");
	public static List<File> paintingsFv0 = listFiles(paintingsFolderFv0, ".json");
	public static File recipesFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "recipes");
	public static List<File> recipesFv0 = listFiles(recipesFolderFv0, ".json");
	public static File globalEventsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "global_events");
	public static List<File> globalEventsFv0 = listFiles(globalEventsFolderFv0, ".json");
	public static File packsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "packs");
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
		itemGroupsFolderFv0.delete();
		paintingsFolderFv0.delete();
		recipesFolderFv0.delete();
		globalEventsFolderFv0.delete();
		packsFolderFv0.delete();
	}
}