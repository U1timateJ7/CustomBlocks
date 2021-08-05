package com.ulto.customblocks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ulto.customblocks.util.BooleanUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
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

public class GenerateCustomElements {
	public static File blocksFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "blocks");
	public static List<File> blocks = new ArrayList<>();
	public static File itemsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "items");
	public static List<File> items = new ArrayList<>();
	public static File packsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "packs");
	public static List<File> packs = new ArrayList<>();
	public static File itemGroupsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "item_groups");
	public static List<File> itemGroups = new ArrayList<>();
	public static File paintingsFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "paintings");
	public static List<File> paintings = new ArrayList<>();
	public static File recipesFolder = new File(CustomBlocksMod.customBlocksConfig, File.separator + "recipes");
	public static List<File> recipes = new ArrayList<>();
	
	public static void generate() {
		blocksFolder.mkdirs();
		itemsFolder.mkdirs();
		packsFolder.mkdirs();
		itemGroupsFolder.mkdirs();
		paintingsFolder.mkdirs();
		recipesFolder.mkdirs();
		copyOldFiles();
		listFiles(blocksFolder, blocks);
		listFiles(itemsFolder, items);
		listFiles(packsFolder, packs);
		listFiles(itemGroupsFolder, itemGroups);
		listFiles(paintingsFolder, paintings);
		listFiles(recipesFolder, recipes);
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
				if (BlockGenerator.add(block) && CustomResourceCreator.generateBlockResources(block) && LanguageHandler.addBlockKey(block)) {
					CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate block " + value.getName() + " !");
				}
				blockReader.close();
			} catch (IOException e) {
				e.printStackTrace();
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
				if (ItemGenerator.add(item) && CustomResourceCreator.generateItemResources(item, item.get("namespace").getAsString(), item.get("id").getAsString(), item.get("texture").getAsString()) && LanguageHandler.addItemKey(item)) {
					CustomBlocksMod.LOGGER.info("Generated Item " + item.get("namespace").getAsString() + ":" + item.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate item " + value.getName() + " !");
				}
				itemReader.close();
			} catch (IOException e) {
				e.printStackTrace();
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
					CustomBlocksMod.LOGGER.info("Generated item group " + itemGroup.get("namespace").getAsString() + ":" + itemGroup.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate item group " + value.getName() + "!");
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
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
					CustomBlocksMod.LOGGER.info("Generated painting " + painting.get("namespace").getAsString() + ":" + painting.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to generate painting " + value.getName() + "!");
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
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
					CustomBlocksMod.LOGGER.error("Failed to generate recipe " + value.getName() + "!");
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
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
					CustomBlocksMod.LOGGER.info("Loading pack " + pack.get("name").getAsString() + "...");
					PackGenerator.add(pack);
					CustomBlocksMod.LOGGER.info("Loaded pack " + pack.get("name").getAsString());
				} else {
					CustomBlocksMod.LOGGER.error("Failed to load pack " + value.getName() + "!");
				}
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LanguageHandler.saveLang();
	}

	@Environment(EnvType.CLIENT)
	public static void generateClient() {
		listFiles(blocksFolder, blocks);
		for (File file : blocks) {
			try {
				BufferedReader test_blockReader = new BufferedReader(new FileReader(file));
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
			}
		}
	}

	private static void listFiles(final File folder, List<File> list) {
		if (folder.exists()) {
			for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
				if (fileEntry.isDirectory()) {
					listFiles(fileEntry, list);
				} else {
					list.add(fileEntry);
				}
			}
		}
	}

	private static List<File> listFiles(final File folder) {
		List<File> list = new ArrayList<>();
		if (folder.exists()) {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					listFiles(fileEntry, list);
				} else {
					list.add(fileEntry);
				}
			}
		}
		return list;
	}

	public static File blocksFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "blocks");
	public static List<File> blocksFv0 = listFiles(blocksFolderFv0);
	public static File itemsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "items");
	public static List<File> itemsFv0 = listFiles(itemsFolderFv0);
	public static File packsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "packs");
	public static List<File> packsFv0 = listFiles(packsFolderFv0);
	public static File itemGroupsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "itemgroups");
	public static List<File> itemGroupsFv0 = listFiles(itemGroupsFolderFv0);
	public static File paintingsFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "paintings");
	public static List<File> paintingsFv0 = listFiles(paintingsFolderFv0);
	public static File recipesFolderFv0 = new File(MinecraftClient.getInstance().runDirectory, File.separator + "recipes");
	public static List<File> recipesFv0 = listFiles(recipesFolderFv0);

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
		for (File fv0 : packsFv0) {
			try {
				Files.copy(fv0.toPath(), packsFolder.toPath().resolve(fv0.getName()));
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
		blocksFolderFv0.delete();
		itemsFolderFv0.delete();
		packsFolderFv0.delete();
		itemGroupsFolderFv0.delete();
		paintingsFolderFv0.delete();
		recipesFolderFv0.delete();
	}
}
