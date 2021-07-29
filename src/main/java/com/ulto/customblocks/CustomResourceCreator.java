package com.ulto.customblocks;

import com.google.gson.JsonObject;
import com.ulto.customblocks.resource.CustomResourcePackProvider;
import com.ulto.customblocks.util.BooleanUtils;
import net.fabricmc.fabric.api.util.TriState;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CustomResourceCreator {
	static File assets = new File(CustomResourcePackProvider.customBlocksPath, File.separator + "assets");
	static File data = new File(CustomResourcePackProvider.customBlocksPath, File.separator + "data");

	public static void setupResourcePack() {
		File mcmeta = new File(CustomResourcePackProvider.customBlocksPath, File.separator + "pack.mcmeta");
		if (!mcmeta.exists()) {
			try {
				mcmeta.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		try {
			FileWriter mcmetawriter = new FileWriter(mcmeta);
			BufferedWriter mcmetabw = new BufferedWriter(mcmetawriter);
			mcmetabw.write("""
					{
					  "pack": {
					    "pack_format": 7,
					    "description": "In the config folder."
					  }
					}""");
			mcmetabw.close();
			mcmetawriter.close();
		} catch (IOException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		}
		assets.mkdirs();
		data.mkdirs();
	}

	public static boolean generateBlockResources(JsonObject _block) {
		if (BooleanUtils.isValidBlock(_block)) {
			String _namespace = _block.get("namespace").getAsString();
			String id = _block.get("id").getAsString();
			String base;
			if (_block.has("base")) base = _block.get("base").getAsString();
			else base = "none";
			String rotationType;
			if (_block.has("rotation_type")) rotationType = _block.get("rotation_type").getAsString();
			else rotationType = "none";
			String resourceNamespace;
			if (_block.has("texture_namespace")) resourceNamespace = _block.get("texture_namespace").getAsString();
			else resourceNamespace = _namespace;
			String customBlockModel;
			String customItemModel;
			String customBlockState;
			TriState differentTextures = BooleanUtils.hasDifferentTextures(_block);
			String bottomTexture = "";
			if (differentTextures == TriState.TRUE) bottomTexture = _block.getAsJsonObject("textures").get("bottom_texture").getAsString();
			String topTexture = bottomTexture;
			if (differentTextures == TriState.TRUE) topTexture = _block.getAsJsonObject("textures").get("top_texture").getAsString();
			String frontTexture = bottomTexture;
			if (differentTextures == TriState.TRUE) frontTexture = _block.getAsJsonObject("textures").get("front_texture").getAsString();
			String backTexture = bottomTexture;
			if (differentTextures == TriState.TRUE) backTexture = _block.getAsJsonObject("textures").get("back_texture").getAsString();
			String rightTexture = bottomTexture;
			if (differentTextures == TriState.TRUE) rightTexture = _block.getAsJsonObject("textures").get("right_texture").getAsString();
			String leftTexture = bottomTexture;
			if (differentTextures == TriState.TRUE) leftTexture = _block.getAsJsonObject("textures").get("left_texture").getAsString();
			String texture = bottomTexture;
			if (differentTextures == TriState.FALSE) texture = _block.getAsJsonObject("textures").get("all").getAsString();
			if (_block.has("custom_model")) {
				JsonObject customModel = _block.getAsJsonObject("custom_model");
				if (customModel.has("block")) customBlockModel = customModel.getAsJsonObject("block").toString();
				else customBlockModel = "none";
				if (customModel.has("item")) customItemModel = customModel.getAsJsonObject("item").toString();
				else customItemModel = "none";
				if (customModel.has("blockstate")) customBlockState = customModel.getAsJsonObject("blockstate").toString();
				else customBlockState = "none";
			}
			else {
				customBlockModel = "none";
				customItemModel = "none";
				customBlockState = "none";
			}
			File namespace = new File(assets, File.separator + _namespace);
			namespace.mkdirs();
			File textureNamespace = new File(assets, File.separator + resourceNamespace);
			textureNamespace.mkdirs();
			File models = new File(namespace, File.separator + "models");
			models.mkdirs();
			File block = new File(models, File.separator + "block");
			block.mkdirs();
			File item = new File(models, File.separator + "item");
			item.mkdirs();
			File blockModel = new File(block, File.separator + id + ".json");
			if (!blockModel.exists() && base.equals("block")) {
				try {
					blockModel.createNewFile();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			File blockItemModel = new File((namespace + ("/models/item")), File.separator + id + ".json");
			if (!blockItemModel.exists()) {
				try {
					blockItemModel.createNewFile();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			//Block Model(s)
			if (customBlockModel.equals("none")) {
				String directionalTextures = "  \"textures\": {\n" +
						"    \"up\": \"" + resourceNamespace + ":block/" + topTexture + "\",\n" +
						"    \"down\": \"" + resourceNamespace + ":block/" + bottomTexture + "\",\n" +
						"    \"north\": \"" + resourceNamespace + ":block/" + frontTexture + "\",\n" +
						"    \"south\": \"" + resourceNamespace + ":block/" + backTexture + "\",\n" +
						"    \"west\": \"" + resourceNamespace + ":block/" + rightTexture + "\",\n" +
						"    \"east\": \"" + resourceNamespace + ":block/" + leftTexture + "\",\n" +
						"    \"particle\": \"#down\"\n" +
						"  },\n";
				String texturesAll = "  \"textures\": {\n" +
						"    \"all\": \"" + resourceNamespace + ":block/" + texture + "\",\n" +
						"    \"particle\": \"#all\"\n" +
						"  },\n";
				String directionalTexturesNoComma = "  \"textures\": {\n" +
						"    \"up\": \"" + resourceNamespace + ":block/" + topTexture + "\",\n" +
						"    \"down\": \"" + resourceNamespace + ":block/" + bottomTexture + "\",\n" +
						"    \"north\": \"" + resourceNamespace + ":block/" + frontTexture + "\",\n" +
						"    \"south\": \"" + resourceNamespace + ":block/" + backTexture + "\",\n" +
						"    \"west\": \"" + resourceNamespace + ":block/" + rightTexture + "\",\n" +
						"    \"east\": \"" + resourceNamespace + ":block/" + leftTexture + "\",\n" +
						"    \"particle\": \"#down\"\n" +
						"  }\n";
				String texturesAllNoComma = "  \"textures\": {\n" +
						"    \"all\": \"" + resourceNamespace + ":block/" + texture + "\",\n" +
						"    \"particle\": \"#all\"\n" +
						"  }\n";
				String defaultCube = "{\n" +
						"  \"parent\": \"minecraft:block/cube\",\n" +
						directionalTexturesNoComma +
						"}";
				String cubeAll = "{\n" +
						"  \"parent\": \"minecraft:block/cube_all\",\n" +
						texturesAllNoComma +
						"}";
				switch (base) {
					case "slab":
						try {
							File blockSlabBottom = new File(block, File.separator + id + "_bottom.json");
							FileWriter blockSlabBottomwriter = new FileWriter(blockSlabBottom);
							BufferedWriter blockSlabBottombw = new BufferedWriter(blockSlabBottomwriter);
							if (differentTextures == TriState.TRUE) {
								blockSlabBottombw.write("{\n" +
										"  \"parent\": \"minecraft:block/block\",\n" +
										directionalTextures +
										"  \"elements\": [\n" +
										"    { \n" +
										"\t  \"from\": [ 0, 0, 0 ],\n" +
										"      \"to\": [ 16, 8, 16 ],\n" +
										"      \"faces\": {\n" +
										"        \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#down\", \"cullface\": \"down\" },\n" +
										"        \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#up\" },\n" +
										"        \"north\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#north\", \"cullface\": \"north\" },\n" +
										"        \"south\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#south\", \"cullface\": \"south\" },\n" +
										"        \"west\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#west\", \"cullface\": \"west\" },\n" +
										"        \"east\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#east\", \"cullface\": \"east\" }\n" +
										"      }\n" +
										"    }\n" +
										"  ]\n" +
										"}");
							} else if (differentTextures == TriState.FALSE) {
								blockSlabBottombw.write("{\n" +
										"  \"parent\": \"minecraft:block/block\",\n" +
										texturesAll +
										"  \"elements\": [\n" +
										"    { \n" +
										"\t  \"from\": [ 0, 0, 0 ],\n" +
										"      \"to\": [ 16, 8, 16 ],\n" +
										"      \"faces\": {\n" +
										"        \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#all\", \"cullface\": \"down\" },\n" +
										"        \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#all\" },\n" +
										"        \"north\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#all\", \"cullface\": \"north\" },\n" +
										"        \"south\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#all\", \"cullface\": \"south\" },\n" +
										"        \"west\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#all\", \"cullface\": \"west\" },\n" +
										"        \"east\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#all\", \"cullface\": \"east\" }\n" +
										"      }\n" +
										"    }\n" +
										"  ]\n" +
										"}");
							}
							blockSlabBottombw.close();
							blockSlabBottomwriter.close();
							File blockSlabTop = new File(block, File.separator + id + "_top.json");
							FileWriter blockSlabTopwriter = new FileWriter(blockSlabTop);
							BufferedWriter blockSlabTopbw = new BufferedWriter(blockSlabTopwriter);
							if (differentTextures == TriState.TRUE) {
								blockSlabTopbw.write("{\n" +
										"  \"parent\": \"minecraft:block/block\",\n" +
										directionalTextures +
										"  \"elements\": [\n" +
										"    { \n" +
										"\t  \"from\": [ 0, 8, 0 ],\n" +
										"      \"to\": [ 16, 16, 16 ],\n" +
										"      \"faces\": {\n" +
										"        \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#down\" },\n" +
										"        \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#up\", \"cullface\": \"up\" },\n" +
										"        \"north\": { \"uv\": [ 0, 0, 16, 8  ], \"texture\": \"#north\", \"cullface\": \"north\" },\n" +
										"        \"south\": { \"uv\": [ 0, 0, 16, 8  ], \"texture\": \"#south\", \"cullface\": \"south\" },\n" +
										"        \"west\":  { \"uv\": [ 0, 0, 16, 8  ], \"texture\": \"#west\", \"cullface\": \"west\" },\n" +
										"        \"east\":  { \"uv\": [ 0, 0, 16, 8  ], \"texture\": \"#east\", \"cullface\": \"east\" }\n" +
										"      }\n" +
										"    }\n" +
										"  ]\n" +
										"}");
							} else if (differentTextures == TriState.FALSE) {
								blockSlabTopbw.write("{\n" +
										"  \"parent\": \"minecraft:block/block\",\n" +
										texturesAll +
										"  \"elements\": [\n" +
										"    { \n" +
										"\t  \"from\": [ 0, 8, 0 ],\n" +
										"      \"to\": [ 16, 16, 16 ],\n" +
										"      \"faces\": {\n" +
										"        \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#all\" },\n" +
										"        \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#all\", \"cullface\": \"up\" },\n" +
										"        \"north\": { \"uv\": [ 0, 0, 16, 8 ], \"texture\": \"#all\", \"cullface\": \"north\" },\n" +
										"        \"south\": { \"uv\": [ 0, 0, 16, 8 ], \"texture\": \"#all\", \"cullface\": \"south\" },\n" +
										"        \"west\":  { \"uv\": [ 0, 0, 16, 8 ], \"texture\": \"#all\", \"cullface\": \"west\" },\n" +
										"        \"east\":  { \"uv\": [ 0, 0, 16, 8 ], \"texture\": \"#all\", \"cullface\": \"east\" }\n" +
										"      }\n" +
										"    }\n" +
										"  ]\n" +
										"}");
							}
							blockSlabTopbw.close();
							blockSlabTopwriter.close();
							File blockSlabDouble = new File(block, File.separator + id + "_double.json");
							FileWriter blockSlabDoublewriter = new FileWriter(blockSlabDouble);
							BufferedWriter blockSlabDoublebw = new BufferedWriter(blockSlabDoublewriter);
							if (differentTextures == TriState.TRUE) blockSlabDoublebw.write(defaultCube);
							else if (differentTextures == TriState.FALSE) blockSlabDoublebw.write(cubeAll);
							blockSlabDoublebw.close();
							blockSlabDoublewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "stairs":
						try {
							File blockStair = new File(block, File.separator + id + ".json");
							FileWriter blockStairwriter = new FileWriter(blockStair);
							BufferedWriter blockStairbw = new BufferedWriter(blockStairwriter);
							{
								String textures = directionalTextures;
								if (differentTextures == TriState.FALSE) textures = texturesAll;
								blockStairbw.write("{   \"parent\": \"block/block\",\n" +
										"    \"display\": {\n" +
										"        \"gui\": {\n" +
										"            \"rotation\": [ 30, 135, 0 ],\n" +
										"            \"translation\": [ 0, 0, 0],\n" +
										"            \"scale\":[ 0.625, 0.625, 0.625 ]\n" +
										"        },\n" +
										"        \"head\": {\n" +
										"            \"rotation\": [ 0, -90, 0 ],\n" +
										"            \"translation\": [ 0, 0, 0 ],\n" +
										"            \"scale\": [ 1, 1, 1 ]\n" +
										"        },\n" +
										"        \"thirdperson_lefthand\": {\n" +
										"            \"rotation\": [ 75, -135, 0 ],\n" +
										"            \"translation\": [ 0, 2.5, 0],\n" +
										"            \"scale\": [ 0.375, 0.375, 0.375 ]\n" +
										"        }\n" +
										"    },\n" +
										textures +
										"    \"elements\": [\n" +
										"        {   \"from\": [ 0, 0, 0 ],\n" +
										"            \"to\": [ 16, 8, 16 ],\n" +
										"            \"faces\": {\n" +
										"                \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#down\", \"cullface\": \"down\" },\n" +
										"                \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#up\" },\n" +
										"                \"north\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#north\", \"cullface\": \"north\" },\n" +
										"                \"south\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#south\", \"cullface\": \"south\" },\n" +
										"                \"west\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#west\", \"cullface\": \"west\" },\n" +
										"                \"east\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#east\", \"cullface\": \"east\" }\n" +
										"            }\n" +
										"        },\n" +
										"        {   \"from\": [ 8, 8, 0 ],\n" +
										"            \"to\": [ 16, 16, 16 ],\n" +
										"            \"faces\": {\n" +
										"                \"up\":    { \"uv\": [ 8, 0, 16, 16 ], \"texture\": \"#up\", \"cullface\": \"up\" },\n" +
										"                \"north\": { \"uv\": [ 0, 0,  8,  8 ], \"texture\": \"#north\", \"cullface\": \"north\" },\n" +
										"                \"south\": { \"uv\": [ 8, 0, 16,  8 ], \"texture\": \"#south\", \"cullface\": \"south\" },\n" +
										"                \"west\":  { \"uv\": [ 0, 0, 16,  8 ], \"texture\": \"#west\" },\n" +
										"                \"east\":  { \"uv\": [ 0, 0, 16,  8 ], \"texture\": \"#east\", \"cullface\": \"east\" }\n" +
										"            }\n" +
										"        }\n" +
										"    ]\n" +
										"}\n");
							}
							blockStairbw.close();
							blockStairwriter.close();
							File blockStairInner = new File(block, File.separator + id + "_inner.json");
							FileWriter blockStairInnerwriter = new FileWriter(blockStairInner);
							BufferedWriter blockStairInnerbw = new BufferedWriter(blockStairInnerwriter);
							{
								String textures = directionalTextures;
								if (differentTextures == TriState.FALSE) textures = texturesAll;
								blockStairInnerbw.write("{\n" +
										textures +
										"    \"elements\": [\n" +
										"        {   \"from\": [ 0, 0, 0 ],\n" +
										"            \"to\": [ 16, 8, 16 ],\n" +
										"            \"faces\": {\n" +
										"                \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#down\", \"cullface\": \"down\" },\n" +
										"                \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#up\" },\n" +
										"                \"north\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#north\", \"cullface\": \"north\" },\n" +
										"                \"south\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#south\", \"cullface\": \"south\" },\n" +
										"                \"west\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#west\", \"cullface\": \"west\" },\n" +
										"                \"east\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#east\", \"cullface\": \"east\" }\n" +
										"            }\n" +
										"        },\n" +
										"        {   \"from\": [ 8, 8, 0 ],\n" +
										"            \"to\": [ 16, 16, 16 ],\n" +
										"            \"faces\": {\n" +
										"                \"up\":    { \"uv\": [ 8, 0, 16, 16 ], \"texture\": \"#up\", \"cullface\": \"up\" },\n" +
										"                \"north\": { \"uv\": [ 0, 0,  8,  8 ], \"texture\": \"#north\", \"cullface\": \"north\" },\n" +
										"                \"south\": { \"uv\": [ 8, 0, 16,  8 ], \"texture\": \"#south\", \"cullface\": \"south\" },\n" +
										"                \"west\":  { \"uv\": [ 0, 0, 16,  8 ], \"texture\": \"#west\" },\n" +
										"                \"east\":  { \"uv\": [ 0, 0, 16,  8 ], \"texture\": \"#east\", \"cullface\": \"east\" }\n" +
										"            }\n" +
										"        },\n" +
										"        {   \"from\": [ 0, 8, 8 ],\n" +
										"            \"to\": [ 8, 16, 16 ],\n" +
										"            \"faces\": {\n" +
										"                \"up\":    { \"uv\": [ 0, 8,  8, 16 ], \"texture\": \"#up\", \"cullface\": \"up\" },\n" +
										"                \"north\": { \"uv\": [ 8, 0, 16,  8 ], \"texture\": \"#north\" },\n" +
										"                \"south\": { \"uv\": [ 0, 0,  8,  8 ], \"texture\": \"#south\", \"cullface\": \"south\" },\n" +
										"                \"west\":  { \"uv\": [ 8, 0, 16,  8 ], \"texture\": \"#west\", \"cullface\": \"west\" }\n" +
										"            }\n" +
										"        }\n" +
										"    ]\n" +
										"}\n");
							}
							blockStairInnerbw.close();
							blockStairInnerwriter.close();
							File blockStairOuter = new File(block, File.separator + id + "_outer.json");
							FileWriter blockStairOuterwriter = new FileWriter(blockStairOuter);
							BufferedWriter blockStairOuterbw = new BufferedWriter(blockStairOuterwriter);
							{
								String textures = directionalTextures;
								if (differentTextures == TriState.FALSE) textures = texturesAll;
								blockStairOuterbw.write("{\n" +
										textures +
										"    \"elements\": [\n" +
										"        {   \"from\": [ 0, 0, 0 ],\n" +
										"            \"to\": [ 16, 8, 16 ],\n" +
										"            \"faces\": {\n" +
										"                \"down\":  { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#down\", \"cullface\": \"down\" },\n" +
										"                \"up\":    { \"uv\": [ 0, 0, 16, 16 ], \"texture\": \"#up\" },\n" +
										"                \"north\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#north\", \"cullface\": \"north\" },\n" +
										"                \"south\": { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#south\", \"cullface\": \"south\" },\n" +
										"                \"west\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#west\", \"cullface\": \"west\" },\n" +
										"                \"east\":  { \"uv\": [ 0, 8, 16, 16 ], \"texture\": \"#east\", \"cullface\": \"east\" }\n" +
										"            }\n" +
										"        },\n" +
										"        {   \"from\": [ 8, 8, 8 ],\n" +
										"            \"to\": [ 16, 16, 16 ],\n" +
										"            \"faces\": {\n" +
										"                \"up\":    { \"uv\": [ 8, 8, 16, 16 ], \"texture\": \"#up\", \"cullface\": \"up\" },\n" +
										"                \"north\": { \"uv\": [ 0, 0,  8,  8 ], \"texture\": \"#north\" },\n" +
										"                \"south\": { \"uv\": [ 8, 0, 16,  8 ], \"texture\": \"#south\", \"cullface\": \"south\" },\n" +
										"                \"west\":  { \"uv\": [ 8, 0, 16,  8 ], \"texture\": \"#west\" },\n" +
										"                \"east\":  { \"uv\": [ 0, 0,  8,  8 ], \"texture\": \"#east\", \"cullface\": \"east\" }\n" +
										"            }\n" +
										"        }\n" +
										"    ]\n" +
										"}\n");
							}
							blockStairOuterbw.close();
							blockStairOuterwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "wall":
						try {
							File blockWall = new File(block, File.separator + id + "_post.json");
							FileWriter blockWallwriter = new FileWriter(blockWall);
							BufferedWriter blockWallbw = new BufferedWriter(blockWallwriter);
							{
								blockWallbw.write("{\n" +
										texturesAll +
										"    \"elements\": [\n" +
										"        {   \"from\": [ 4, 0, 4 ],\n" +
										"            \"to\": [ 12, 16, 12 ],\n" +
										"            \"faces\": {\n" +
										"                \"down\":  { \"texture\": \"#all\", \"cullface\": \"down\" },\n" +
										"                \"up\":    { \"texture\": \"#all\", \"cullface\": \"up\" },\n" +
										"                \"north\": { \"texture\": \"#all\" },\n" +
										"                \"south\": { \"texture\": \"#all\" },\n" +
										"                \"west\":  { \"texture\": \"#all\" },\n" +
										"                \"east\":  { \"texture\": \"#all\" }\n" +
										"            },\n" +
										"            \"__comment\": \"Center post\"\n" +
										"        }\n" +
										"    ]\n" +
										"}\n");
							}
							blockWallbw.close();
							blockWallwriter.close();
							File blockWallSide = new File(block, File.separator + id + "_side.json");
							FileWriter blockWallSidewriter = new FileWriter(blockWallSide);
							BufferedWriter blockWallSidebw = new BufferedWriter(blockWallSidewriter);
							{
								blockWallSidebw.write("{\n" +
										texturesAll +
										"    \"elements\": [\n" +
										"        {   \"from\": [ 5, 0, 0 ],\n" +
										"            \"to\": [ 11, 14, 8 ],\n" +
										"            \"faces\": {\n" +
										"                \"down\":  { \"texture\": \"#all\", \"cullface\": \"down\" },\n" +
										"                \"up\":    { \"texture\": \"#all\" },\n" +
										"                \"north\": { \"texture\": \"#all\", \"cullface\": \"north\" },\n" +
										"                \"west\":  { \"texture\": \"#all\" },\n" +
										"                \"east\":  { \"texture\": \"#all\" }\n" +
										"            },\n" +
										"            \"__comment\": \"wall\"\n" +
										"        }\n" +
										"    ]\n" +
										"}\n");
							}
							blockWallSidebw.close();
							blockWallSidewriter.close();
							File blockWallSideTall = new File(block, File.separator + id + "_side_tall.json");
							FileWriter blockWallSideTallwriter = new FileWriter(blockWallSideTall);
							BufferedWriter blockWallSideTallbw = new BufferedWriter(blockWallSideTallwriter);
							{
								blockWallSideTallbw.write("{\n" +
										texturesAll +
										"    \"elements\": [\n" +
										"        {   \"from\": [ 5, 0, 0 ],\n" +
										"            \"to\": [ 11, 16, 8 ],\n" +
										"            \"faces\": {\n" +
										"                \"down\":  { \"texture\": \"#all\", \"cullface\": \"down\" },\n" +
										"                \"up\":    { \"texture\": \"#all\", \"cullface\": \"up\"},\n" +
										"                \"north\": { \"texture\": \"#all\", \"cullface\": \"north\" },\n" +
										"                \"west\":  { \"texture\": \"#all\" },\n" +
										"                \"east\":  { \"texture\": \"#all\" }\n" +
										"            }\n" +
										"        }\n" +
										"    ]\n" +
										"}\n");
							}
							blockWallSideTallbw.close();
							blockWallSideTallwriter.close();
							File blockWallInventory = new File(block, File.separator + id + "_inventory.json");
							FileWriter blockWallInventorywriter = new FileWriter(blockWallInventory);
							BufferedWriter blockWallInventorybw = new BufferedWriter(blockWallInventorywriter);
							{
								blockWallInventorybw.write("{   \"parent\": \"block/block\",\n" +
										"    \"display\": {\n" +
										"        \"gui\": {\n" +
										"            \"rotation\": [ 30, 135, 0 ],\n" +
										"            \"translation\": [ 0, 0, 0],\n" +
										"            \"scale\":[ 0.625, 0.625, 0.625 ]\n" +
										"        },\n" +
										"        \"fixed\": {\n" +
										"            \"rotation\": [ 0, 90, 0 ],\n" +
										"            \"translation\": [ 0, 0, 0 ],\n" +
										"            \"scale\": [ 0.5, 0.5, 0.5 ]\n" +
										"        }\n" +
										"    },\n" +
										"    \"ambientocclusion\": false,\n" +
										texturesAll +
										"    \"elements\": [\n" +
										"        {   \"from\": [ 4, 0, 4 ],\n" +
										"            \"to\": [ 12, 16, 12 ],\n" +
										"            \"faces\": {\n" +
										"                \"down\":  { \"uv\": [ 4, 4, 12, 12 ], \"texture\": \"#all\", \"cullface\": \"down\" },\n" +
										"                \"up\":    { \"uv\": [ 4, 4, 12, 12 ], \"texture\": \"#all\" },\n" +
										"                \"north\": { \"uv\": [ 4, 0, 12, 16 ], \"texture\": \"#all\" },\n" +
										"                \"south\": { \"uv\": [ 4, 0, 12, 16 ], \"texture\": \"#all\" },\n" +
										"                \"west\":  { \"uv\": [ 4, 0, 12, 16 ], \"texture\": \"#all\" },\n" +
										"                \"east\":  { \"uv\": [ 4, 0, 12, 16 ], \"texture\": \"#all\" }\n" +
										"            },\n" +
										"            \"__comment\": \"Center post\"\n" +
										"        },\n" +
										"        {   \"from\": [ 5, 0, 0 ],\n" +
										"            \"to\": [ 11, 13, 16 ],\n" +
										"            \"faces\": {\n" +
										"                \"down\":  { \"uv\": [ 5, 0, 11, 16 ], \"texture\": \"#all\", \"cullface\": \"down\" },\n" +
										"                \"up\":    { \"uv\": [ 5, 0, 11, 16 ], \"texture\": \"#all\" },\n" +
										"                \"north\": { \"uv\": [ 5, 3, 11, 16 ], \"texture\": \"#all\", \"cullface\": \"north\" },\n" +
										"                \"south\": { \"uv\": [ 5, 3, 11, 16 ], \"texture\": \"#all\", \"cullface\": \"south\" },\n" +
										"                \"west\":  { \"uv\": [ 0, 3, 16, 16 ], \"texture\": \"#all\" },\n" +
										"                \"east\":  { \"uv\": [ 0, 3, 16, 16 ], \"texture\": \"#all\" }\n" +
										"            },\n" +
										"            \"__comment\": \"Full wall\"\n" +
										"        }\n" +
										"    ]\n" +
										"}\n");
							}
							blockWallInventorybw.close();
							blockWallInventorywriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "fence":
						try {
							File blockFencePost = new File(block, File.separator + id + "_post.json");
							FileWriter blockFencePostwriter = new FileWriter(blockFencePost);
							BufferedWriter blockFencePostbw = new BufferedWriter(blockFencePostwriter);
							{
								blockFencePostbw.write("{\n" +
										"  \"parent\": \"minecraft:block/fence_post\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockFencePostbw.close();
							blockFencePostwriter.close();
							File blockFenceSide = new File(block, File.separator + id + "_side.json");
							FileWriter blockFenceSidewriter = new FileWriter(blockFenceSide);
							BufferedWriter blockFenceSidebw = new BufferedWriter(blockFenceSidewriter);
							{
								blockFenceSidebw.write("{\n" +
										"  \"parent\": \"minecraft:block/fence_side\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockFenceSidebw.close();
							blockFenceSidewriter.close();
							File blockFenceInventory = new File(block, File.separator + id + "_inventory.json");
							FileWriter blockFenceInventorywriter = new FileWriter(blockFenceInventory);
							BufferedWriter blockFenceInventorybw = new BufferedWriter(blockFenceInventorywriter);
							{
								blockFenceInventorybw.write("{\n" +
										"  \"parent\": \"minecraft:block/fence_inventory\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockFenceInventorybw.close();
							blockFenceInventorywriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "fence_gate":
						try {
							File blockFenceGate = new File(block, File.separator + id + ".json");
							FileWriter blockFenceGatewriter = new FileWriter(blockFenceGate);
							BufferedWriter blockFenceGatebw = new BufferedWriter(blockFenceGatewriter);
							{
								blockFenceGatebw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_fence_gate\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockFenceGatebw.close();
							blockFenceGatewriter.close();
							File blockFenceGateOpen = new File(block, File.separator + id + "_open.json");
							FileWriter blockFenceGateOpenwriter = new FileWriter(blockFenceGateOpen);
							BufferedWriter blockFenceGateOpenbw = new BufferedWriter(blockFenceGateOpenwriter);
							{
								blockFenceGateOpenbw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_fence_gate_open\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockFenceGateOpenbw.close();
							blockFenceGateOpenwriter.close();
							File blockFenceGateWall = new File(block, File.separator + id + "_wall.json");
							FileWriter blockFenceGateWallwriter = new FileWriter(blockFenceGateWall);
							BufferedWriter blockFenceGateWallbw = new BufferedWriter(blockFenceGateWallwriter);
							{
								blockFenceGateWallbw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_fence_gate_wall\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockFenceGateWallbw.close();
							blockFenceGateWallwriter.close();
							File blockFenceGateWallOpen = new File(block, File.separator + id + "_wall_open.json");
							FileWriter blockFenceGateWallOpenwriter = new FileWriter(blockFenceGateWallOpen);
							BufferedWriter blockFenceGateWallOpenbw = new BufferedWriter(blockFenceGateWallOpenwriter);
							{
								blockFenceGateWallOpenbw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_fence_gate_wall_open\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockFenceGateWallOpenbw.close();
							blockFenceGateWallOpenwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "pane":
						try {
							File blockPaneNoside = new File(block, File.separator + id + "_noside.json");
							FileWriter blockPaneNosidewriter = new FileWriter(blockPaneNoside);
							BufferedWriter blockPaneNosidebw = new BufferedWriter(blockPaneNosidewriter);
							{
								blockPaneNosidebw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_glass_pane_noside\",\n" +
										"  \"textures\": {\n" +
										"    \"pane\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockPaneNosidebw.close();
							blockPaneNosidewriter.close();
							File blockPaneNosideAlt = new File(block, File.separator + id + "_noside_alt.json");
							FileWriter blockPaneNosideAltwriter = new FileWriter(blockPaneNosideAlt);
							BufferedWriter blockPaneNosideAltbw = new BufferedWriter(blockPaneNosideAltwriter);
							{
								blockPaneNosideAltbw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_glass_pane_noside_alt\",\n" +
										"  \"textures\": {\n" +
										"    \"pane\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockPaneNosideAltbw.close();
							blockPaneNosideAltwriter.close();
							File blockPanePost = new File(block, File.separator + id + "_post.json");
							FileWriter blockPanePostwriter = new FileWriter(blockPanePost);
							BufferedWriter blockPanePostbw = new BufferedWriter(blockPanePostwriter);
							{
								blockPanePostbw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_glass_pane_post\",\n" +
										"  \"textures\": {\n" +
										"    \"pane\": \"" + resourceNamespace + ":block/" + texture + "\",\n" +
										"    \"edge\": \"" + resourceNamespace + ":block/" + topTexture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockPanePostbw.close();
							blockPanePostwriter.close();
							File blockPaneSide = new File(block, File.separator + id + "_side.json");
							FileWriter blockPaneSidewriter = new FileWriter(blockPaneSide);
							BufferedWriter blockPaneSidebw = new BufferedWriter(blockPaneSidewriter);
							{
								blockPaneSidebw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_glass_pane_side\",\n" +
										"  \"textures\": {\n" +
										"    \"pane\": \"" + resourceNamespace + ":block/" + texture + "\",\n" +
										"    \"edge\": \"" + resourceNamespace + ":block/" + topTexture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockPaneSidebw.close();
							blockPaneSidewriter.close();
							File blockPaneSideAlt = new File(block, File.separator + id + "_side_alt.json");
							FileWriter blockPaneSideAltwriter = new FileWriter(blockPaneSideAlt);
							BufferedWriter blockPaneSideAltbw = new BufferedWriter(blockPaneSideAltwriter);
							{
								blockPaneSideAltbw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_glass_pane_side_alt\",\n" +
										"  \"textures\": {\n" +
										"    \"pane\": \"" + resourceNamespace + ":block/" + texture + "\",\n" +
										"    \"edge\": \"" + resourceNamespace + ":block/" + topTexture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockPaneSideAltbw.close();
							blockPaneSideAltwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "pressure_plate":
						try {
							FileWriter blockModelwriter = new FileWriter(blockModel);
							BufferedWriter blockModelbw = new BufferedWriter(blockModelwriter);
							{
								blockModelbw.write("{\n" +
										"  \"parent\": \"minecraft:block/pressure_plate_up\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockModelbw.close();
							blockModelwriter.close();
							File blockDown = new File(block, File.separator + id + "_down.json");
							FileWriter blockDownwriter = new FileWriter(blockDown);
							BufferedWriter blockDownbw = new BufferedWriter(blockDownwriter);
							{
								blockDownbw.write("{\n" +
										"  \"parent\": \"minecraft:block/pressure_plate_down\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockDownbw.close();
							blockDownwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "button":
						try {
							FileWriter blockModelwriter = new FileWriter(blockModel);
							BufferedWriter blockModelbw = new BufferedWriter(blockModelwriter);
							{
								blockModelbw.write("{\n" +
										"  \"parent\": \"minecraft:block/button\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockModelbw.close();
							blockModelwriter.close();
							File blockPressed = new File(block, File.separator + id + "_pressed.json");
							FileWriter blockPressedwriter = new FileWriter(blockPressed);
							BufferedWriter blockPressedbw = new BufferedWriter(blockPressedwriter);
							{
								blockPressedbw.write("{\n" +
										"  \"parent\": \"minecraft:block/button_pressed\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockPressedbw.close();
							blockPressedwriter.close();
							File blockInventory = new File(block, File.separator + id + "_inventory.json");
							FileWriter blockInventorywriter = new FileWriter(blockInventory);
							BufferedWriter blockInventorybw = new BufferedWriter(blockInventorywriter);
							{
								blockInventorybw.write("{\n" +
										"  \"parent\": \"minecraft:block/button_inventory\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockInventorybw.close();
							blockInventorywriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "trapdoor":
						try {
							File blockBottom = new File(block, File.separator + id + "_bottom.json");
							FileWriter blockBottomwriter = new FileWriter(blockBottom);
							BufferedWriter blockBottombw = new BufferedWriter(blockBottomwriter);
							{
								blockBottombw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_orientable_trapdoor_bottom\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockBottombw.close();
							blockBottomwriter.close();
							File blockTop = new File(block, File.separator + id + "_top.json");
							FileWriter blockTopwriter = new FileWriter(blockTop);
							BufferedWriter blockTopbw = new BufferedWriter(blockTopwriter);
							{
								blockTopbw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_orientable_trapdoor_top\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockTopbw.close();
							blockTopwriter.close();
							File blockOpen = new File(block, File.separator + id + "_open.json");
							FileWriter blockOpenwriter = new FileWriter(blockOpen);
							BufferedWriter blockOpenbw = new BufferedWriter(blockOpenwriter);
							{
								blockOpenbw.write("{\n" +
										"  \"parent\": \"minecraft:block/template_orientable_trapdoor_open\",\n" +
										"  \"textures\": {\n" +
										"    \"texture\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockOpenbw.close();
							blockOpenwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "door":
						try {
							File blockBottom = new File(block, File.separator + id + "_bottom.json");
							FileWriter blockBottomwriter = new FileWriter(blockBottom);
							BufferedWriter blockBottombw = new BufferedWriter(blockBottomwriter);
							{
								blockBottombw.write("{\n" +
										"  \"parent\": \"minecraft:block/door_bottom\",\n" +
										"  \"textures\": {\n" +
										"    \"top\": \"" + resourceNamespace + ":block/" + topTexture + "\",\n" +
										"    \"bottom\": \"" + resourceNamespace + ":block/" + bottomTexture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockBottombw.close();
							blockBottomwriter.close();
							File blockBottomHinge = new File(block, File.separator + id + "_bottom_hinge.json");
							FileWriter blockBottomHingewriter = new FileWriter(blockBottomHinge);
							BufferedWriter blockBottomHingebw = new BufferedWriter(blockBottomHingewriter);
							{
								blockBottomHingebw.write("{\n" +
										"  \"parent\": \"minecraft:block/door_bottom_rh\",\n" +
										"  \"textures\": {\n" +
										"    \"top\": \"" + resourceNamespace + ":block/" + topTexture + "\",\n" +
										"    \"bottom\": \"" + resourceNamespace + ":block/" + bottomTexture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockBottomHingebw.close();
							blockBottomHingewriter.close();
							File blockTop = new File(block, File.separator + id + "_top.json");
							FileWriter blockTopwriter = new FileWriter(blockTop);
							BufferedWriter blockTopbw = new BufferedWriter(blockTopwriter);
							{
								blockTopbw.write("{\n" +
										"  \"parent\": \"minecraft:block/door_top\",\n" +
										"  \"textures\": {\n" +
										"    \"top\": \"" + resourceNamespace + ":block/" + topTexture + "\",\n" +
										"    \"bottom\": \"" + resourceNamespace + ":block/" + bottomTexture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockTopbw.close();
							blockTopwriter.close();
							File blockTopHinge = new File(block, File.separator + id + "_top_hinge.json");
							FileWriter blockTopHingewriter = new FileWriter(blockTopHinge);
							BufferedWriter blockTopHingebw = new BufferedWriter(blockTopHingewriter);
							{
								blockTopHingebw.write("{\n" +
										"  \"parent\": \"minecraft:block/door_top_rh\",\n" +
										"  \"textures\": {\n" +
										"    \"top\": \"" + resourceNamespace + ":block/" + topTexture + "\",\n" +
										"    \"bottom\": \"" + resourceNamespace + ":block/" + bottomTexture + "\"\n" +
										"  }\n" +
										"}");
							}
							blockTopHingebw.close();
							blockTopHingewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					default:
						try {
							FileWriter blockModelwriter = new FileWriter(blockModel);
							BufferedWriter blockModelbw = new BufferedWriter(blockModelwriter);
							if (differentTextures == TriState.TRUE) blockModelbw.write(defaultCube);
							else if (differentTextures == TriState.FALSE) blockModelbw.write(cubeAll);
							blockModelbw.close();
							blockModelwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
				}
			}
			else {
				try {
					FileWriter blockModelwriter = new FileWriter(blockModel);
					BufferedWriter blockModelbw = new BufferedWriter(blockModelwriter);
					{
						blockModelbw.write(customBlockModel);
					}
					blockModelbw.close();
					blockModelwriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//Block Item Model
			if (customItemModel.equals("none")) {
				switch (base) {
					case "slab":
					case "trapdoor":
						try {
							FileWriter blockItemModelwriter = new FileWriter(blockItemModel);
							BufferedWriter blockItemModelbw = new BufferedWriter(blockItemModelwriter);
							{
								blockItemModelbw.write("{\n" +
										"  \"parent\": \"" + _namespace + ":block/" + id + "_bottom" + "\"\n" +
										"}");
							}
							blockItemModelbw.close();
							blockItemModelwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "wall":
					case "fence":
					case "button":
						try {
							FileWriter blockItemModelwriter = new FileWriter(blockItemModel);
							BufferedWriter blockItemModelbw = new BufferedWriter(blockItemModelwriter);
							{
								blockItemModelbw.write("{\n" +
										"  \"parent\": \"" + _namespace + ":block/" + id + "_inventory" + "\"\n" +
										"}");
							}
							blockItemModelbw.close();
							blockItemModelwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "pane":
						try {
							FileWriter blockItemModelwriter = new FileWriter(blockItemModel);
							BufferedWriter blockItemModelbw = new BufferedWriter(blockItemModelwriter);
							blockItemModelbw.write("{\n" +
									"  \"parent\": \"minecraft:item/generated\",\n" +
									"  \"textures\": {\n" +
									"    \"layer0\": \"" + resourceNamespace + ":block/" + texture + "\"\n" +
									"  }\n" +
									"}");
							blockItemModelbw.close();
							blockItemModelwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "door":
						try {
							FileWriter blockItemModelwriter = new FileWriter(blockItemModel);
							BufferedWriter blockItemModelbw = new BufferedWriter(blockItemModelwriter);
							blockItemModelbw.write("{\n" +
									"  \"parent\": \"minecraft:item/generated\",\n" +
									"  \"textures\": {\n" +
									"    \"layer0\": \"" + resourceNamespace + ":block/" + frontTexture + "\"\n" +
									"  }\n" +
									"}");
							blockItemModelbw.close();
							blockItemModelwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					default:
						try {
							FileWriter blockItemModelwriter = new FileWriter(blockItemModel);
							BufferedWriter blockItemModelbw = new BufferedWriter(blockItemModelwriter);
							{
								blockItemModelbw.write("{\n" +
										"  \"parent\": \"" + _namespace + ":block/" + id + "\"\n" +
										"}");
							}
							blockItemModelbw.close();
							blockItemModelwriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
				}
			}
			else {
				try {
					FileWriter blockItemModelwriter = new FileWriter(blockItemModel);
					BufferedWriter blockItemModelbw = new BufferedWriter(blockItemModelwriter);
					{
						blockItemModelbw.write(customItemModel);
					}
					blockItemModelbw.close();
					blockItemModelwriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			File textures = new File(textureNamespace, File.separator + "textures");
			textures.mkdirs();
			File blocks = new File(textures, File.separator + "block");
			blocks.mkdirs();
			File blockstates = new File(namespace, File.separator + "blockstates");
			blockstates.mkdirs();
			File blockState = new File(blockstates, File.separator + id + ".json");
			//Block State
			if (customBlockState.equals("none")) {
				switch (base) {
					case "slab":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"variants\": {\n" +
										"    \"type=bottom\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\"\n" +
										"    },\n" +
										"    \"type=double\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_double\"\n" +
										"    },\n" +
										"    \"type=top\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\"\n" +
										"    }\n" +
										"  }\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "stairs":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"variants\": {\n" +
										"    \"facing=east,half=bottom,shape=inner_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=east,half=bottom,shape=inner_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\"\n" +
										"    },\n" +
										"    \"facing=east,half=bottom,shape=outer_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=east,half=bottom,shape=outer_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\"\n" +
										"    },\n" +
										"    \"facing=east,half=bottom,shape=straight\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\"\n" +
										"    },\n" +
										"    \"facing=east,half=top,shape=inner_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"x\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=east,half=top,shape=inner_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=east,half=top,shape=outer_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"x\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=east,half=top,shape=outer_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=east,half=top,shape=straight\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"x\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=bottom,shape=inner_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=bottom,shape=inner_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=bottom,shape=outer_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=bottom,shape=outer_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=bottom,shape=straight\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=top,shape=inner_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=top,shape=inner_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"x\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=top,shape=outer_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=top,shape=outer_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"x\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=north,half=top,shape=straight\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=south,half=bottom,shape=inner_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\"\n" +
										"    },\n" +
										"    \"facing=south,half=bottom,shape=inner_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=south,half=bottom,shape=outer_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\"\n" +
										"    },\n" +
										"    \"facing=south,half=bottom,shape=outer_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=south,half=bottom,shape=straight\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=south,half=top,shape=inner_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=south,half=top,shape=inner_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=south,half=top,shape=outer_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=south,half=top,shape=outer_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=south,half=top,shape=straight\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=bottom,shape=inner_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=bottom,shape=inner_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=bottom,shape=outer_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"y\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=bottom,shape=outer_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=bottom,shape=straight\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=top,shape=inner_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=top,shape=inner_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_inner\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=top,shape=outer_left\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=top,shape=outer_right\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_outer\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 270,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"facing=west,half=top,shape=straight\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 180,\n" +
										"      \"uvlock\": true\n" +
										"    }\n" +
										"  }\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "wall":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"multipart\": [\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"up\": \"true\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_post\"\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"north\": \"low\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\",\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"east\": \"low\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\",\n" +
										"        \"y\": 90,\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"south\": \"low\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\",\n" +
										"        \"y\": 180,\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"west\": \"low\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\",\n" +
										"        \"y\": 270,\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"north\": \"tall\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side_tall\",\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"east\": \"tall\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side_tall\",\n" +
										"        \"y\": 90,\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"south\": \"tall\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side_tall\",\n" +
										"        \"y\": 180,\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"west\": \"tall\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side_tall\",\n" +
										"        \"y\": 270,\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    }\n" +
										"  ]\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "fence":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"multipart\": [\n" +
										"    {\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_post\"\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"north\": \"true\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\",\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"east\": \"true\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\",\n" +
										"        \"y\": 90,\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"south\": \"true\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\",\n" +
										"        \"y\": 180,\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"west\": \"true\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\",\n" +
										"        \"y\": 270,\n" +
										"        \"uvlock\": true\n" +
										"      }\n" +
										"    }\n" +
										"  ]\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "fence_gate":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"variants\": {\n" +
										"    \"facing=east,in_wall=false,open=false\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 270,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\"\n" +
										"    },\n" +
										"    \"facing=east,in_wall=false,open=true\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 270,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\"\n" +
										"    },\n" +
										"    \"facing=east,in_wall=true,open=false\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 270,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_wall\"\n" +
										"    },\n" +
										"    \"facing=east,in_wall=true,open=true\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 270,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_wall_open\"\n" +
										"    },\n" +
										"    \"facing=north,in_wall=false,open=false\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 180,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\"\n" +
										"    },\n" +
										"    \"facing=north,in_wall=false,open=true\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 180,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\"\n" +
										"    },\n" +
										"    \"facing=north,in_wall=true,open=false\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 180,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_wall\"\n" +
										"    },\n" +
										"    \"facing=north,in_wall=true,open=true\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 180,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_wall_open\"\n" +
										"    },\n" +
										"    \"facing=south,in_wall=false,open=false\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\"\n" +
										"    },\n" +
										"    \"facing=south,in_wall=false,open=true\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\"\n" +
										"    },\n" +
										"    \"facing=south,in_wall=true,open=false\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_wall\"\n" +
										"    },\n" +
										"    \"facing=south,in_wall=true,open=true\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_wall_open\"\n" +
										"    },\n" +
										"    \"facing=west,in_wall=false,open=false\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 90,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\"\n" +
										"    },\n" +
										"    \"facing=west,in_wall=false,open=true\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 90,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\"\n" +
										"    },\n" +
										"    \"facing=west,in_wall=true,open=false\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 90,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_wall\"\n" +
										"    },\n" +
										"    \"facing=west,in_wall=true,open=true\": {\n" +
										"      \"uvlock\": true,\n" +
										"      \"y\": 90,\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_wall_open\"\n" +
										"    }\n" +
										"  }\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "pane":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"multipart\": [\n" +
										"    {\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_post\"\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"north\": \"true\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\"\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"east\": \"true\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side\",\n" +
										"        \"y\": 90\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"south\": \"true\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side_alt\"\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"west\": \"true\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_side_alt\",\n" +
										"        \"y\": 90\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"north\": \"false\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_noside\"\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"east\": \"false\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_noside_alt\"\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"south\": \"false\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_noside_alt\",\n" +
										"        \"y\": 90\n" +
										"      }\n" +
										"    },\n" +
										"    {\n" +
										"      \"when\": {\n" +
										"        \"west\": \"false\"\n" +
										"      },\n" +
										"      \"apply\": {\n" +
										"        \"model\": \"" + _namespace + ":block/" + id + "_noside\",\n" +
										"        \"y\": 270\n" +
										"      }\n" +
										"    }\n" +
										"  ]\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "pressure_plate":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"variants\": {\n" +
										"    \"powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\"\n" +
										"    },\n" +
										"    \"powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_down\"\n" +
										"    }\n" +
										"  }\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "button":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"variants\": {\n" +
										"    \"face=ceiling,facing=east,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 270,\n" +
										"      \"x\": 180\n" +
										"    },\n" +
										"    \"face=ceiling,facing=east,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"y\": 270,\n" +
										"      \"x\": 180\n" +
										"    },\n" +
										"    \"face=ceiling,facing=north,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 180,\n" +
										"      \"x\": 180\n" +
										"    },\n" +
										"    \"face=ceiling,facing=north,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"y\": 180,\n" +
										"      \"x\": 180\n" +
										"    },\n" +
										"    \"face=ceiling,facing=south,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"x\": 180\n" +
										"    },\n" +
										"    \"face=ceiling,facing=south,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"x\": 180\n" +
										"    },\n" +
										"    \"face=ceiling,facing=west,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 90,\n" +
										"      \"x\": 180\n" +
										"    },\n" +
										"    \"face=ceiling,facing=west,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"y\": 90,\n" +
										"      \"x\": 180\n" +
										"    },\n" +
										"    \"face=floor,facing=east,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"face=floor,facing=east,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"face=floor,facing=north,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\"\n" +
										"    },\n" +
										"    \"face=floor,facing=north,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\"\n" +
										"    },\n" +
										"    \"face=floor,facing=south,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"face=floor,facing=south,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"face=floor,facing=west,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"face=floor,facing=west,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"face=wall,facing=east,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 90,\n" +
										"      \"x\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"face=wall,facing=east,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"y\": 90,\n" +
										"      \"x\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"face=wall,facing=north,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"x\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"face=wall,facing=north,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"x\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"face=wall,facing=south,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 180,\n" +
										"      \"x\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"face=wall,facing=south,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"y\": 180,\n" +
										"      \"x\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"face=wall,facing=west,powered=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
										"      \"y\": 270,\n" +
										"      \"x\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    },\n" +
										"    \"face=wall,facing=west,powered=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_pressed\",\n" +
										"      \"y\": 270,\n" +
										"      \"x\": 90,\n" +
										"      \"uvlock\": true\n" +
										"    }\n" +
										"  }\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "trapdoor":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"variants\": {\n" +
										"    \"facing=east,half=bottom,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=east,half=bottom,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=east,half=top,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=east,half=top,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=north,half=bottom,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\"\n" +
										"    },\n" +
										"    \"facing=north,half=bottom,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\"\n" +
										"    },\n" +
										"    \"facing=north,half=top,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\"\n" +
										"    },\n" +
										"    \"facing=north,half=top,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=south,half=bottom,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=south,half=bottom,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=south,half=top,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=south,half=top,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 0\n" +
										"    },\n" +
										"    \"facing=west,half=bottom,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=west,half=bottom,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=west,half=top,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=west,half=top,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_open\",\n" +
										"      \"x\": 180,\n" +
										"      \"y\": 90\n" +
										"    }\n" +
										"  }\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					case "door":
						try {
							FileWriter blockStatewriter = new FileWriter(blockState);
							BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
							{
								blockStatebw.write("{\n" +
										"  \"variants\": {\n" +
										"    \"facing=east,half=lower,hinge=left,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\"\n" +
										"    },\n" +
										"    \"facing=east,half=lower,hinge=left,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom_hinge\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=east,half=lower,hinge=right,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom_hinge\"\n" +
										"    },\n" +
										"    \"facing=east,half=lower,hinge=right,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=east,half=upper,hinge=left,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\"\n" +
										"    },\n" +
										"    \"facing=east,half=upper,hinge=left,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top_hinge\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=east,half=upper,hinge=right,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top_hinge\"\n" +
										"    },\n" +
										"    \"facing=east,half=upper,hinge=right,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=north,half=lower,hinge=left,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=north,half=lower,hinge=left,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom_hinge\"\n" +
										"    },\n" +
										"    \"facing=north,half=lower,hinge=right,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom_hinge\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=north,half=lower,hinge=right,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=north,half=upper,hinge=left,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=north,half=upper,hinge=left,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top_hinge\"\n" +
										"    },\n" +
										"    \"facing=north,half=upper,hinge=right,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top_hinge\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=north,half=upper,hinge=right,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=south,half=lower,hinge=left,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=south,half=lower,hinge=left,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom_hinge\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=south,half=lower,hinge=right,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom_hinge\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=south,half=lower,hinge=right,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\"\n" +
										"    },\n" +
										"    \"facing=south,half=upper,hinge=left,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=south,half=upper,hinge=left,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top_hinge\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=south,half=upper,hinge=right,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top_hinge\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=south,half=upper,hinge=right,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\"\n" +
										"    },\n" +
										"    \"facing=west,half=lower,hinge=left,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=west,half=lower,hinge=left,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom_hinge\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=west,half=lower,hinge=right,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom_hinge\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=west,half=lower,hinge=right,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_bottom\",\n" +
										"      \"y\": 90\n" +
										"    },\n" +
										"    \"facing=west,half=upper,hinge=left,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=west,half=upper,hinge=left,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top_hinge\",\n" +
										"      \"y\": 270\n" +
										"    },\n" +
										"    \"facing=west,half=upper,hinge=right,open=false\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top_hinge\",\n" +
										"      \"y\": 180\n" +
										"    },\n" +
										"    \"facing=west,half=upper,hinge=right,open=true\": {\n" +
										"      \"model\": \"" + _namespace + ":block/" + id + "_top\",\n" +
										"      \"y\": 90\n" +
										"    }\n" +
										"  }\n" +
										"}");
							}
							blockStatebw.close();
							blockStatewriter.close();
						} catch (IOException fileNotFoundException) {
							fileNotFoundException.printStackTrace();
						}
						break;
					default:
						switch (rotationType) {
							case "axis":
								try {
									FileWriter blockStatewriter = new FileWriter(blockState);
									BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
									{
										blockStatebw.write("{");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("  \"variants\": {");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("    \"axis=x\": {");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write((("      \"model\": \"") + "" + (_namespace) + "" + (":block/") + "" + (id) + "" + ("\",")));
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("      \"x\": 90,");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("      \"y\": 90");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("    },");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("    \"axis=y\": {");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write((("      \"model\": \"") + "" + (_namespace) + "" + (":block/") + "" + (id) + "" + ("\"")));
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("    },");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("    \"axis=z\": {");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write((("      \"model\": \"") + "" + (_namespace) + "" + (":block/") + "" + (id) + "" + ("\",")));
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("      \"x\": 90");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("    }");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("  }");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("}");
									}
									blockStatebw.close();
									blockStatewriter.close();
								} catch (IOException fileNotFoundException) {
									fileNotFoundException.printStackTrace();
								}
								break;
							case "y_axis":
							case "y_axis_player":
								try {
									FileWriter blockStatewriter = new FileWriter(blockState);
									BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
									{
										blockStatebw.write("{\n" +
												"  \"variants\": {\n" +
												"    \"facing=north\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\"\n" +
												"    },\n" +
												"    \"facing=east\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
												"      \"y\": 90\n" +
												"    },\n" +
												"    \"facing=south\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
												"      \"y\": 180\n" +
												"    },\n" +
												"    \"facing=west\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
												"      \"y\": 270\n" +
												"    }\n" +
												"  }\n" +
												"}");
									}
									blockStatebw.close();
									blockStatewriter.close();
								} catch (IOException fileNotFoundException) {
									fileNotFoundException.printStackTrace();
								}
								break;
							case "all":
							case "all_player":
								try {
									FileWriter blockStatewriter = new FileWriter(blockState);
									BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
									{
										blockStatebw.write("{\n" +
												"  \"variants\": {\n" +
												"    \"facing=north\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\"\n" +
												"    },\n" +
												"    \"facing=east\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
												"      \"y\": 90\n" +
												"    },\n" +
												"    \"facing=south\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
												"      \"y\": 180\n" +
												"    },\n" +
												"    \"facing=west\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
												"      \"y\": 270\n" +
												"    },\n" +
												"    \"facing=up\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
												"      \"x\": 270\n" +
												"    },\n" +
												"    \"facing=down\": {\n" +
												"      \"model\": \"" + _namespace + ":block/" + id + "\",\n" +
												"      \"x\": 90\n" +
												"    }\n" +
												"  }\n" +
												"}");
									}
									blockStatebw.close();
									blockStatewriter.close();
								} catch (IOException fileNotFoundException) {
									fileNotFoundException.printStackTrace();
								}
								break;
							default:
								try {
									FileWriter blockStatewriter = new FileWriter(blockState);
									BufferedWriter blockStatebw = new BufferedWriter(blockStatewriter);
									{
										blockStatebw.write("{");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("  \"variants\": {");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("    \"\": {");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write((("      \"model\": \"") + "" + (_namespace) + "" + (":block/") + "" + (id) + "" + ("\"")));
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("    }");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("  }");
										blockStatebw.newLine();
									}
									{
										blockStatebw.write("}");
									}
									blockStatebw.close();
									blockStatewriter.close();
								} catch (IOException fileNotFoundException) {
									fileNotFoundException.printStackTrace();
								}
								break;
						}
						break;
				}
			}
			else {
				try {
					FileWriter blockstatewriter = new FileWriter(blockState);
					BufferedWriter blockstatebw = new BufferedWriter(blockstatewriter);
					{
						blockstatebw.write(customBlockState);
					}
					blockstatebw.close();
					blockstatewriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

	public static boolean generateItemResources(JsonObject _item, String _namespace, String id, String texture) {
		if (_item.has("namespace") && _item.has("id") && _item.has("texture")) {
			String resourceNamespace;
			if (_item.has("texture_namespace")) resourceNamespace = _item.get("texture_namespace").getAsString();
			else resourceNamespace = _namespace;
			String toolType;
			if (_item.has("tool_type")) toolType = _item.get("tool_type").getAsString();
			else toolType = "none";
			String customModel;
			if (_item.has("custom_model")) customModel = _item.getAsJsonObject("custom_model").toString();
			else customModel = "none";
			File namespace = new File(assets, File.separator + _namespace);
			namespace.mkdirs();
			File textureNamespace = new File(assets, File.separator + resourceNamespace);
			textureNamespace.mkdirs();
			File models = new File(namespace, File.separator + "models");
			models.mkdirs();
			File item = new File(models, File.separator + "item");
			item.mkdirs();
			File itemModel = new File(item, File.separator + id + ".json");
			if (!itemModel.exists()) {
				try {
					itemModel.createNewFile();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			if (customModel.equals("none")) {
				try {
					FileWriter itemModelwriter = new FileWriter(itemModel);
					BufferedWriter itemModelbw = new BufferedWriter(itemModelwriter);
					{
						itemModelbw.write("{");
						itemModelbw.newLine();
					}
					if (toolType.equals("none")) {
						itemModelbw.write((("  \"parent\": \"minecraft:item/generated\",")));
					} else {
						itemModelbw.write((("  \"parent\": \"minecraft:item/handheld\",")));
					}
					itemModelbw.newLine();
					{
						itemModelbw.write((("  \"textures\": {")));
						itemModelbw.newLine();
					}
					{
						itemModelbw.write((("    \"layer0\": \"") + "" + (resourceNamespace) + "" + (":item/") + "" + (texture) + "" + ("\"")));
						itemModelbw.newLine();
					}
					{
						itemModelbw.write((("  }")));
						itemModelbw.newLine();
					}
					{
						itemModelbw.write("}");
					}
					itemModelbw.close();
					itemModelwriter.close();
				} catch (IOException fileNotFoundException) {
					fileNotFoundException.printStackTrace();
				}
			}
			else {
				try {
					FileWriter itemModelwriter = new FileWriter(itemModel);
					BufferedWriter itemModelbw = new BufferedWriter(itemModelwriter);
					{
						itemModelbw.write(customModel);
					}
					itemModelbw.close();
					itemModelwriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			File textures = new File(textureNamespace, File.separator + "textures");
			textures.mkdirs();
			File items = new File(textures, File.separator + "item");
			items.mkdirs();
			return true;
		}
		return false;
	}

	public static boolean generateFluidResources(JsonObject _fluid) {
		if (_fluid.has("namespace") && _fluid.has("id") && _fluid.has("texture") && _fluid.has("bucket_texture")) {
			String _namespace = _fluid.get("namespace").getAsString();
			String id = _fluid.get("id").getAsString();
			String resourceNamespace = _namespace;
			if (_fluid.has("texture_namespace")) resourceNamespace = _fluid.get("texture_namespace").getAsString();
			File namespace = new File(assets, File.separator + _namespace);
			namespace.mkdirs();
			File textureNamespace = new File(assets, File.separator + resourceNamespace);
			textureNamespace.mkdirs();
			File blockstates = new File(namespace, File.separator + "blockstates");
			blockstates.mkdirs();
			File blockstate = new File(blockstates, File.separator + id + ".json");
			try {
				FileWriter blockstatewriter = new FileWriter(blockstate);
				BufferedWriter blockstatebw = new BufferedWriter(blockstatewriter);
				blockstatebw.write("{\n" +
						"  \"variants\": {\n" +
						"    \"\": {\n" +
						"      \"model\": \"minecraft:block/water\"\n" +
						"    }\n" +
						"  }\n" +
						"}");
				blockstatebw.close();
				blockstatewriter.close();
			} catch (IOException fileNotFoundException) {
				fileNotFoundException.printStackTrace();
			}
			File textures = new File(textureNamespace, File.separator + "textures");
			textures.mkdirs();
			File block = new File(textures, File.separator + "block");
			block.mkdirs();
			JsonObject item = new JsonObject();
			item.addProperty("namespace", _namespace);
			item.addProperty("id", id + "_bucket");
			item.addProperty("texture_namespace", resourceNamespace);
			item.addProperty("texture", _fluid.get("bucket_texture").getAsString());
			generateItemResources(item, _namespace, id + "_bucket", _fluid.get("bucket_texture").getAsString());
			return true;
		}
		return false;
	}

	public static boolean generatePaintingResources(JsonObject _painting) {
		if (_painting.has("namespace") && _painting.has("id")) {
			String _namespace = _painting.get("namespace").getAsString();
			File namespace = new File(assets, File.separator + _namespace);
			namespace.mkdirs();
			File textures = new File(namespace, File.separator + "textures");
			textures.mkdirs();
			File painting = new File(textures, File.separator + "painting");
			painting.mkdirs();
			return true;
		}
		return false;
	}
}
