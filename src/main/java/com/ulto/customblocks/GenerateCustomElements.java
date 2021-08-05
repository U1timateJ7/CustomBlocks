package com.ulto.customblocks;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
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
	
	public static void generate() {
		blocksFolder.mkdirs();
		itemsFolder.mkdirs();
		packsFolder.mkdirs();
		itemGroupsFolder.mkdirs();
		paintingsFolder.mkdirs();
		listFiles(blocksFolder, blocks);
		listFiles(itemsFolder, items);
		listFiles(packsFolder, packs);
		listFiles(itemGroupsFolder, itemGroups);
		listFiles(paintingsFolder, paintings);
		LanguageHandler.setupLanguage();
		CustomResourceCreator.setupResourcePack();
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
				if (ItemGenerator.add(item) && CustomResourceCreator.generateItemResources(item, item.get("namespace").getAsString(), item.get("id").getAsString(), item.get("texture").getAsString()) && LanguageHandler.addItemKey(item)) {
					CustomBlocksMod.LOGGER.info("Generated Item " + item.get("namespace").getAsString() + ":" + item.get("id").getAsString());
				} else {
					CustomBlocksMod.LOGGER.info("Failed to generate item " + value.getName() + "!");
				}
				itemReader.close();
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
					CustomBlocksMod.LOGGER.info("Failed to generate painting " + value.getName() + "!");
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
				CustomBlocksMod.LOGGER.info("Loading pack " + pack.get("name").getAsString() + "...");
				PackGenerator.add(pack);
				CustomBlocksMod.LOGGER.info("Loaded pack " + pack.get("name").getAsString());
				packReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LanguageHandler.saveLang();
	}

	@OnlyIn(Dist.CLIENT)
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
						case "cutout" -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.cutout());
						case "cutout_mipped" -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.cutoutMipped());
						case "translucent" -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.translucent());
						default -> ItemBlockRenderTypes.setRenderLayer(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.get("namespace").getAsString(), block.get("id").getAsString())), RenderType.solid());
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
}
