package com.ulto.customblocks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenerateCustomElements {
	public static File blocksFolder = new File(MinecraftClient.getInstance().runDirectory, File.separator + "blocks");
	public static List<File> blocks = new ArrayList<>();
	public static File itemsFolder = new File(MinecraftClient.getInstance().runDirectory, File.separator + "items");
	public static List<File> items = new ArrayList<>();
	public static File packsFolder = new File(MinecraftClient.getInstance().runDirectory, File.separator + "packs");
	public static List<File> packs = new ArrayList<>();
	public static File itemGroupsFolder = new File(MinecraftClient.getInstance().runDirectory, File.separator + "itemgroups");
	public static List<File> itemGroups = new ArrayList<>();
	public static File paintingsFolder = new File(MinecraftClient.getInstance().runDirectory, File.separator + "paintings");
	public static List<File> paintings = new ArrayList<>();
	public static File recipesFolder = new File(MinecraftClient.getInstance().runDirectory, File.separator + "recipes");
	public static List<File> recipes = new ArrayList<>();
	public static List<JsonObject> jsonRecipes = new ArrayList<>();
	
	public static void generate() {
		blocksFolder.mkdirs();
		listFiles(blocksFolder, blocks);
		itemsFolder.mkdirs();
		listFiles(itemsFolder, items);
		packsFolder.mkdirs();
		listFiles(packsFolder, packs);
		itemGroupsFolder.mkdirs();
		listFiles(itemGroupsFolder, itemGroups);
		paintingsFolder.mkdirs();
		listFiles(paintingsFolder, paintings);
		recipesFolder.mkdirs();
		listFiles(recipesFolder, recipes);
		LanguageHandler.setupLanguage();
		ResourcePackGenerator.setupResourcePack();
		for (File value : blocks) {
			try {
				BufferedReader blockReader = new BufferedReader(new FileReader(value));
				StringBuilder json = new StringBuilder();
				String line;
				while ((line = blockReader.readLine()) != null) {
					json.append(line);
				}
				JsonObject block = new Gson().fromJson(json.toString(), JsonObject.class);
				if (BlockGenerator.add(block) && ResourcePackGenerator.generateBlockResources(block, block.get("textures").getAsJsonObject().get("top_texture").getAsString(), block.get("textures").getAsJsonObject().get("bottom_texture").getAsString(), block.get("textures").getAsJsonObject().get("front_texture").getAsString(), block.get("textures").getAsJsonObject().get("back_texture").getAsString(), block.get("textures").getAsJsonObject().get("right_texture").getAsString(), block.get("textures").getAsJsonObject().get("left_texture").getAsString()) && LanguageHandler.addBlockKey(block)) {
					CustomBlocksMod.LOGGER.info("Generated Block " + block.get("namespace").getAsString() + ":" + block.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.info("Failed to generate block " + value.getName() + " !");
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
				if (ItemGenerator.add(item) && ResourcePackGenerator.generateItemResources(item, item.get("namespace").getAsString(), item.get("id").getAsString(), item.get("texture").getAsString()) && LanguageHandler.addItemKey(item)) {
					CustomBlocksMod.LOGGER.info("Generated Item " + item.get("namespace").getAsString() + ":" + item.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.info("Failed to generate item " + value.getName() + " !");
				}
				itemReader.close();
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
				CustomBlocksMod.LOGGER.info("Loading pack " + pack.get("name").getAsString() + "...");
				PackGenerator.add(pack);
				CustomBlocksMod.LOGGER.info("Loaded pack " + pack.get("name").getAsString());
				packReader.close();
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
					CustomBlocksMod.LOGGER.info("Failed to generate item group " + value.getName() + "!");
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
				if (PaintingGenerator.add(painting) && ResourcePackGenerator.generatePaintingResources(painting)) {
					CustomBlocksMod.LOGGER.info("Generated painting " + painting.get("namespace").getAsString() + ":" + painting.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.info("Failed to generate painting " + value.getName() + "!");
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
				jsonRecipes.add(recipe);
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
				if (block.has("render_type")) {
					if (block.get("render_type").getAsString().equals("cutout")) {
						BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderLayer.getCutoutMipped());
					} else if (block.get("render_type").getAsString().equals("translucent")) {
						BlockRenderLayerMap.INSTANCE.putBlock(Registry.BLOCK.get(new Identifier(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderLayer.getTranslucent());
					}
				}
				test_blockReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void listFiles(final File folder, List<File> list) {
    	for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
			if (fileEntry.isDirectory()) {
				listFiles(fileEntry, list);
			} else {
				list.add(fileEntry);
			}
    	}
	}
}
