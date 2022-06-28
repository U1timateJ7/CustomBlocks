package com.ulto.customblocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.block.*;
import com.ulto.customblocks.block.waterlogged.*;
import com.ulto.customblocks.util.JsonUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BlockGenerator {
	public static boolean add(JsonObject block) {
		if (block.has("namespace") && block.has("id")) {
			String namespace = block.get("namespace").getAsString();
			String id = block.get("id").getAsString();
			String base;
			if (block.has("base")) base = block.get("base").getAsString();
			else base = "block";
			String _material;
			if (block.has("material")) _material = block.get("material").getAsString();
			else _material = "stone";
			double _hardness;
			if (block.has("hardness")) _hardness = block.get("hardness").getAsDouble();
			else _hardness = 1;
			double _resistance;
			if (block.has("resistance")) _resistance = block.get("resistance").getAsDouble();
			else _resistance = 10;
			double _slipperiness;
			if (block.has("slipperiness")) _slipperiness = block.get("slipperiness").getAsDouble();
			else _slipperiness = 0.6;
			boolean hasGravity;
			if (block.has("has_gravity")) hasGravity = block.get("has_gravity").getAsBoolean();
			else hasGravity = false;
			String rotationType;
			if (block.has("rotation_type")) rotationType = block.get("rotation_type").getAsString();
			else rotationType = "none";
			String _mapColor;
			if (block.has("map_color")) _mapColor = block.get("map_color").getAsString();
			else _mapColor = "default";
			String _sounds;
			if (block.has("sounds")) _sounds = block.get("sounds").getAsString();
			else _sounds = "stone";
			String breakTool;
			if (block.has("efficient_tool")) breakTool = block.get("efficient_tool").getAsString();
			else breakTool = "none";
			boolean requiresTool;
			if (block.has("requires_tool")) requiresTool = block.get("requires_tool").getAsBoolean();
			else requiresTool = true;
			int harvestLevel;
			if (block.has("harvest_level")) harvestLevel = block.get("harvest_level").getAsInt();
			else harvestLevel = 0;
			int luminance;
			if (block.has("luminance")) luminance = block.get("luminance").getAsInt();
			else luminance = 0;
			if (luminance > 15) luminance = 15;
			if (luminance < 0) luminance = 0;
			String renderType;
			if (block.has("render_type")) renderType = block.get("render_type").getAsString();
			else renderType = "opaque";
			float speedFactor;
			if (block.has("speed_factor")) speedFactor = block.get("speed_factor").getAsFloat();
			else speedFactor = 1f;
			float jumpFactor;
			if (block.has("jump_factor")) jumpFactor = block.get("jump_factor").getAsFloat();
			else jumpFactor = 1f;
			boolean waterloggable;
			if (block.has("waterloggable")) waterloggable = block.get("waterloggable").getAsBoolean();
			else waterloggable = false;
			boolean hasItem = true;
			if (block.has("has_item")) hasItem = block.get("has_item").getAsBoolean();
			int maxStackSize;
			if (block.has("max_stack_size")) maxStackSize = block.get("max_stack_size").getAsInt();
			else maxStackSize = 64;
			if (maxStackSize > 64) maxStackSize = 64;
			if (maxStackSize < 1) maxStackSize = 1;
			boolean fireproof = false;
			if (block.has("fireproof")) fireproof = block.get("fireproof").getAsBoolean();
			boolean hasRandomTick = false;
			if (block.has("randomly_ticks")) hasRandomTick = block.get("randomly_ticks").getAsBoolean();
			String _itemGroup;
			if (block.has("item_group")) _itemGroup = block.get("item_group").getAsString();
			else {
				_itemGroup = switch (base) {
					case "wall", "fence", "pane" -> "decorations";
					case "fence_gate", "pressure_plate", "button", "trapdoor", "door" -> "redstone";
					default -> "building_blocks";
				};
			}
			List<JsonObject> shape = new ArrayList<>();
			if (block.has("shape")) {
				JsonArray stacks = block.getAsJsonArray("shape");
				shape = JsonUtils.jsonArrayToJsonObjectList(stacks);
			}
			else {
				JsonObject fullCube = new JsonObject();
				fullCube.addProperty("min_x", 0);
				fullCube.addProperty("min_y", 0);
				fullCube.addProperty("min_z", 0);
				fullCube.addProperty("max_x", 16);
				fullCube.addProperty("max_y", 16);
				fullCube.addProperty("max_z", 16);
				shape.add(fullCube);
			}
			Material material;
			MapColor mapColor;
			BlockSoundGroup sounds;
			ItemGroup itemGroup;
			material = switch (_material) {
				case "wood" -> Material.WOOD;
				case "dirt" -> Material.SOIL;
				case "metal" -> Material.METAL;
				case "leaves" -> Material.LEAVES;
				case "air" -> Material.AIR;
				case "glass" -> Material.GLASS;
				default -> Material.STONE;
			};
			mapColor = switch (_mapColor) {
				case "white" -> MapColor.WHITE;
				case "orange" -> MapColor.ORANGE;
				case "magenta" -> MapColor.MAGENTA;
				case "light_blue" -> MapColor.LIGHT_BLUE;
				case "yellow" -> MapColor.YELLOW;
				case "lime" -> MapColor.LIME;
				case "pink" -> MapColor.PINK;
				case "gray", "grey" -> MapColor.GRAY;
				case "light_gray", "light_grey" -> MapColor.LIGHT_GRAY;
				case "cyan" -> MapColor.CYAN;
				case "purple" -> MapColor.PURPLE;
				case "blue" -> MapColor.BLUE;
				case "brown" -> MapColor.BROWN;
				case "green" -> MapColor.GREEN;
				case "red" -> MapColor.RED;
				case "black" -> MapColor.BLACK;
				case "clear" -> MapColor.CLEAR;
				case "bright_red" -> MapColor.BRIGHT_RED;
				case "dark_green" -> MapColor.DARK_GREEN;
				case "dark_red" -> MapColor.DARK_RED;
				case "diamond_blue" -> MapColor.DIAMOND_BLUE;
				case "dirt_brown" -> MapColor.DIRT_BROWN;
				case "emerald_green" -> MapColor.EMERALD_GREEN;
				case "gold" -> MapColor.GOLD;
				case "iron_gray", "iron_grey" -> MapColor.IRON_GRAY;
				case "lapis_blue" -> MapColor.LAPIS_BLUE;
				case "light_blue_gray", "light_blue_grey" -> MapColor.LIGHT_BLUE_GRAY;
				case "oak_tan" -> MapColor.OAK_TAN;
				case "off_white" -> MapColor.OFF_WHITE;
				case "pale_green" -> MapColor.PALE_GREEN;
				case "pale_purple" -> MapColor.PALE_PURPLE;
				case "pale_yellow" -> MapColor.PALE_YELLOW;
				case "spruce_brown" -> MapColor.SPRUCE_BROWN;
				case "stone_gray", "stone_grey" -> MapColor.STONE_GRAY;
				case "terracotta_white" -> MapColor.TERRACOTTA_WHITE;
				case "terracotta_orange" -> MapColor.TERRACOTTA_ORANGE;
				case "terracotta_magenta" -> MapColor.TERRACOTTA_MAGENTA;
				case "terracotta_light_blue" -> MapColor.TERRACOTTA_LIGHT_BLUE;
				case "terracotta_yellow" -> MapColor.TERRACOTTA_YELLOW;
				case "terracotta_lime" -> MapColor.TERRACOTTA_LIME;
				case "terracotta_pink" -> MapColor.TERRACOTTA_PINK;
				case "terracotta_gray", "terracotta_grey" -> MapColor.TERRACOTTA_GRAY;
				case "terracotta_light_gray", "terracotta_light_grey" -> MapColor.TERRACOTTA_LIGHT_GRAY;
				case "terracotta_cyan" -> MapColor.TERRACOTTA_CYAN;
				case "terracotta_purple" -> MapColor.TERRACOTTA_PURPLE;
				case "terracotta_blue" -> MapColor.TERRACOTTA_BLUE;
				case "terracotta_brown" -> MapColor.TERRACOTTA_BROWN;
				case "terracotta_green" -> MapColor.TERRACOTTA_GREEN;
				case "terracotta_red" -> MapColor.TERRACOTTA_RED;
				case "terracotta_black" -> MapColor.TERRACOTTA_BLACK;
				case "water_blue" -> MapColor.WATER_BLUE;
				case "white_gray", "white_grey" -> MapColor.WHITE_GRAY;
				case "dull_red" -> MapColor.DULL_RED;
				case "dull_pink" -> MapColor.DULL_PINK;
				case "dark_crimson" -> MapColor.DARK_CRIMSON;
				case "teal" -> MapColor.TEAL;
				case "dark_aqua" -> MapColor.DARK_AQUA;
				case "dark_dull_pink" -> MapColor.DARK_DULL_PINK;
				case "bright_teal" -> MapColor.BRIGHT_TEAL;
				case "deepslate_gray", "deepslate_grey" -> MapColor.DEEPSLATE_GRAY;
				case "raw_iron_pink" -> MapColor.RAW_IRON_PINK;
				case "lichen_green" -> MapColor.LICHEN_GREEN;
				default -> material.getColor();
			};
			sounds = switch (_sounds) {
				case "wood" -> BlockSoundGroup.WOOD;
				case "dirt", "gravel" -> BlockSoundGroup.GRAVEL;
				case "grass" -> BlockSoundGroup.GRASS;
				case "metal" -> BlockSoundGroup.METAL;
				case "sand" -> BlockSoundGroup.SAND;
				case "glass" -> BlockSoundGroup.GLASS;
				case "lily_pad" -> BlockSoundGroup.LILY_PAD;
				case "wool" -> BlockSoundGroup.WOOL;
				case "snow" -> BlockSoundGroup.SNOW;
				case "ladder" -> BlockSoundGroup.LADDER;
				case "anvil" -> BlockSoundGroup.ANVIL;
				case "slime" -> BlockSoundGroup.SLIME;
				case "honey" -> BlockSoundGroup.HONEY;
				case "wet_grass" -> BlockSoundGroup.WET_GRASS;
				case "coral" -> BlockSoundGroup.CORAL;
				case "bamboo" -> BlockSoundGroup.BAMBOO;
				case "bamboo_sapling" -> BlockSoundGroup.BAMBOO_SAPLING;
				case "scaffolding" -> BlockSoundGroup.SCAFFOLDING;
				case "sweet_berry_bush" -> BlockSoundGroup.SWEET_BERRY_BUSH;
				case "crop" -> BlockSoundGroup.CROP;
				case "stem" -> BlockSoundGroup.STEM;
				case "vine" -> BlockSoundGroup.VINE;
				case "nether_wart" -> BlockSoundGroup.NETHER_WART;
				case "lantern" -> BlockSoundGroup.LANTERN;
				case "nether_stem" -> BlockSoundGroup.NETHER_STEM;
				case "nylium" -> BlockSoundGroup.NYLIUM;
				case "fungus" -> BlockSoundGroup.FUNGUS;
				case "roots" -> BlockSoundGroup.ROOTS;
				case "shroomlight" -> BlockSoundGroup.SHROOMLIGHT;
				case "weeping_vines" -> BlockSoundGroup.WEEPING_VINES;
				case "weeping_vines_low_pitch" -> BlockSoundGroup.WEEPING_VINES_LOW_PITCH;
				case "soul_sand" -> BlockSoundGroup.SOUL_SAND;
				case "soul_soil" -> BlockSoundGroup.SOUL_SOIL;
				case "basalt" -> BlockSoundGroup.BASALT;
				case "wart_block" -> BlockSoundGroup.WART_BLOCK;
				case "netherrack" -> BlockSoundGroup.NETHERRACK;
				case "nether_bricks" -> BlockSoundGroup.NETHER_BRICKS;
				case "nether_sprouts" -> BlockSoundGroup.NETHER_SPROUTS;
				case "nether_ore" -> BlockSoundGroup.NETHER_ORE;
				case "bone" -> BlockSoundGroup.BONE;
				case "netherite" -> BlockSoundGroup.NETHERITE;
				case "ancient_debris" -> BlockSoundGroup.ANCIENT_DEBRIS;
				case "lodestone" -> BlockSoundGroup.LODESTONE;
				case "chain" -> BlockSoundGroup.CHAIN;
				case "nether_gold_ore" -> BlockSoundGroup.NETHER_GOLD_ORE;
				case "gilded_blackstone" -> BlockSoundGroup.GILDED_BLACKSTONE;
				case "candle" -> BlockSoundGroup.CANDLE;
				case "amethyst_block" -> BlockSoundGroup.AMETHYST_BLOCK;
				case "amethyst_cluster" -> BlockSoundGroup.AMETHYST_CLUSTER;
				case "small_amethyst_bud" -> BlockSoundGroup.SMALL_AMETHYST_BUD;
				case "medium_amethyst_bud" -> BlockSoundGroup.MEDIUM_AMETHYST_BUD;
				case "large_amethyst_bud" -> BlockSoundGroup.LARGE_AMETHYST_BUD;
				case "tuff" -> BlockSoundGroup.TUFF;
				case "calcite" -> BlockSoundGroup.CALCITE;
				case "copper" -> BlockSoundGroup.COPPER;
				case "powder_snow" -> BlockSoundGroup.POWDER_SNOW;
				case "dripstone_block" -> BlockSoundGroup.DRIPSTONE_BLOCK;
				case "pointed_dripstone" -> BlockSoundGroup.POINTED_DRIPSTONE;
				case "sculk_sensor" -> BlockSoundGroup.SCULK_SENSOR;
				case "glow_lichen" -> BlockSoundGroup.GLOW_LICHEN;
				case "cave_vines" -> BlockSoundGroup.CAVE_VINES;
				case "spore_blossom" -> BlockSoundGroup.SPORE_BLOSSOM;
				case "azalea" -> BlockSoundGroup.AZALEA;
				case "flowering_azalea" -> BlockSoundGroup.FLOWERING_AZALEA;
				case "moss_carpet" -> BlockSoundGroup.MOSS_CARPET;
				case "moss_block" -> BlockSoundGroup.MOSS_BLOCK;
				case "big_dripleaf" -> BlockSoundGroup.BIG_DRIPLEAF;
				case "small_dripleaf" -> BlockSoundGroup.SMALL_DRIPLEAF;
				case "rooted_dirt" -> BlockSoundGroup.ROOTED_DIRT;
				case "hanging_roots" -> BlockSoundGroup.HANGING_ROOTS;
				case "azalea_leaves" -> BlockSoundGroup.AZALEA_LEAVES;
				case "deepslate" -> BlockSoundGroup.DEEPSLATE;
				case "deepslate_bricks" -> BlockSoundGroup.DEEPSLATE_BRICKS;
				case "deepslate_tiles" -> BlockSoundGroup.DEEPSLATE_TILES;
				case "polished_deepslate" -> BlockSoundGroup.POLISHED_DEEPSLATE;
				default -> BlockSoundGroup.STONE;
			};
			itemGroup = switch (_itemGroup) {
				case "decorations" -> ItemGroup.DECORATIONS;
				case "redstone" -> ItemGroup.REDSTONE;
				case "transportation" -> ItemGroup.TRANSPORTATION;
				case "misc" -> ItemGroup.MISC;
				case "food" -> ItemGroup.FOOD;
				case "tools" -> ItemGroup.TOOLS;
				case "combat" -> ItemGroup.COMBAT;
				case "brewing" -> ItemGroup.BREWING;
				case "none" -> null;
				default -> ItemGroup.BUILDING_BLOCKS;
			};

			Block NEW_BLOCK;
			FabricBlockSettings blockSettings = FabricBlockSettings.of(material).strength((float) _hardness, (float) _resistance).slipperiness((float) _slipperiness).mapColor(mapColor).sounds(sounds).luminance(luminance).velocityMultiplier(speedFactor).jumpVelocityMultiplier(jumpFactor);
			if (!breakTool.equals("none")) {
				switch (harvestLevel) {
					case 1 -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("needs_stone_tool"), "blocks", new Identifier(namespace, id)));
					case 2 -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("needs_iron_tool"), "blocks", new Identifier(namespace, id)));
					case 3 -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("needs_diamond_tool"), "blocks", new Identifier(namespace, id)));
					default -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fabric", "needs_tool_level_" + harvestLevel), "blocks", new Identifier(namespace, id)));
				}
				switch (breakTool) {
					case "axe" -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("mineable/axe"), "blocks", new Identifier(namespace, id)));
					case "hoe" -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("mineable/hoe"), "blocks", new Identifier(namespace, id)));
					case "pickaxe" -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("mineable/pickaxe"), "blocks", new Identifier(namespace, id)));
					case "shovel" -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("mineable/shovel"), "blocks", new Identifier(namespace, id)));
					case "sword" -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fabric", "mineable/sword"), "blocks", new Identifier(namespace, id)));
					case "shears" -> TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fabric", "mineable/shears"), "blocks", new Identifier(namespace, id)));
				}
				if (requiresTool) blockSettings.requiresTool();
			}
			if (!renderType.equals("opaque")) blockSettings.nonOpaque().blockVision(BlockGenerator::never).suffocates(BlockGenerator::never).solidBlock(BlockGenerator::never);
			if (hasRandomTick) blockSettings.ticksRandomly();
			switch (base) {
				case "slab":
					if (hasGravity) {
						CustomFallingSlabBlock BLOCK = new CustomFallingSlabBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						SlabBlock BLOCK = new CustomSlabBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "stairs":
					if (hasGravity) {
						CustomFallingStairBlock BLOCK = new CustomFallingStairBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						StairsBlock BLOCK = new CustomStairBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "wall":
					if (hasGravity) {
						CustomFallingWallBlock BLOCK = new CustomFallingWallBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						WallBlock BLOCK = new CustomWallBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "fence":
					if (hasGravity) {
						CustomFallingFenceBlock BLOCK = new CustomFallingFenceBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						FenceBlock BLOCK = new CustomFenceBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "fence_gate":
					if (hasGravity) {
						CustomFallingFenceGateBlock BLOCK = new CustomFallingFenceGateBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						FenceGateBlock BLOCK = new CustomFenceGateBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "pane":
					if (hasGravity) {
						CustomFallingPaneBlock BLOCK = new CustomFallingPaneBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						PaneBlock BLOCK = new CustomPaneBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "pressure_plate":
					if (hasGravity) {
						CustomFallingPressurePlateBlock BLOCK = new CustomFallingPressurePlateBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						CustomPressurePlateBlock BLOCK = new CustomPressurePlateBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "button": {
						CustomButtonBlock BLOCK = new CustomButtonBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "trapdoor":
					if (hasGravity) {
						CustomFallingTrapdoorBlock BLOCK = new CustomFallingTrapdoorBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						CustomTrapdoorBlock BLOCK = new CustomTrapdoorBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "door": {
						CustomDoorBlock BLOCK = new CustomDoorBlock(blockSettings, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "lever":
				{
					CustomLeverBlock BLOCK = new CustomLeverBlock(blockSettings, block);
					Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
					NEW_BLOCK = BLOCK;
				}
				break;
				case "lichen":
				{
					CustomLichenBlock BLOCK = new CustomLichenBlock(blockSettings, block);
					Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
					NEW_BLOCK = BLOCK;
				}
				break;
				default:
					if (hasGravity) {
						switch (rotationType) {
							case "axis" -> {
								CustomPillarBlock BLOCK = waterloggable ? new CustomWaterloggedFallingPillarBlock(blockSettings, shape, block) : new CustomFallingPillarBlock(blockSettings, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							case "y_axis_player" -> {
								CustomHorizontalFacingBlock BLOCK = waterloggable ? new CustomWaterloggedFallingHorizontalFacingBlock(blockSettings, true, shape, block) : new CustomFallingHorizontalFacingBlock(blockSettings, true, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							case "y_axis" -> {
								CustomHorizontalFacingBlock BLOCK = waterloggable ? new CustomWaterloggedFallingHorizontalFacingBlock(blockSettings, false, shape, block) : new CustomFallingHorizontalFacingBlock(blockSettings, false, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							case "all_player" -> {
								CustomFacingBlock BLOCK = waterloggable ? new CustomWaterloggedFallingFacingBlock(blockSettings, true, shape, block) : new CustomFallingFacingBlock(blockSettings, true, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							case "all" -> {
								CustomFacingBlock BLOCK = waterloggable ? new CustomWaterloggedFallingFacingBlock(blockSettings, false, shape, block) : new CustomFallingFacingBlock(blockSettings, false, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							default -> {
								Block BLOCK = waterloggable ? new CustomWaterloggedFallingBlock(blockSettings, shape, block) : new CustomFallingBlock(blockSettings, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
						}
					} else {
						switch (rotationType) {
							case "axis" -> {
								PillarBlock BLOCK = waterloggable ? new CustomWaterloggedPillarBlock(blockSettings, shape, block) : new CustomPillarBlock(blockSettings, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							case "y_axis_player" -> {
								Block BLOCK = waterloggable ? new CustomWaterloggedHorizontalFacingBlock(blockSettings, true, shape, block) : new CustomHorizontalFacingBlock(blockSettings, true, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							case "y_axis" -> {
								Block BLOCK = waterloggable ? new CustomWaterloggedHorizontalFacingBlock(blockSettings, false, shape, block) : new CustomHorizontalFacingBlock(blockSettings, false, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							case "all_player" -> {
								Block BLOCK = waterloggable ? new CustomWaterloggedFacingBlock(blockSettings, true, shape, block) : new CustomFacingBlock(blockSettings, true, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							case "all" -> {
								Block BLOCK = waterloggable ? new CustomWaterloggedFacingBlock(blockSettings, false, shape, block) : new CustomFacingBlock(blockSettings, false, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							default -> {
								Block BLOCK = waterloggable ? new CustomWaterloggedBlock(blockSettings, shape, block) : new CustomBlock(blockSettings, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
						}
					}
					break;
			}
			if (hasItem) {
				FabricItemSettings itemSettings = new FabricItemSettings().group(itemGroup).maxCount(maxStackSize);
				if (fireproof) itemSettings.fireproof();
				BlockItem BLOCK_ITEM;
				if (base.equals("door")) BLOCK_ITEM = new TallBlockItem(NEW_BLOCK, itemSettings);
				else BLOCK_ITEM = new BlockItem(NEW_BLOCK, itemSettings);
				Registry.register(Registry.ITEM, new Identifier(namespace, id), BLOCK_ITEM);
			}
			return true;
		}
		return false;
	}

	public static boolean addBedrock(JsonObject block, @Nullable File file) {
		if (block.has("format_version") && block.has("minecraft:block")) {
			JsonObject minecraftBlock = block.getAsJsonObject("minecraft:block");
			if (minecraftBlock.has("description")) {
				JsonObject javaBlock = new JsonObject();
				JsonObject components = new JsonObject();
				if (minecraftBlock.has("components")) components = minecraftBlock.getAsJsonObject("components");
				Identifier identifier = new Identifier(minecraftBlock.getAsJsonObject("description").get("identifier").getAsString());
				String namespace = identifier.getNamespace();
				String id = identifier.getPath();
				StringBuilder displayName = new StringBuilder();
				String[] words = id.split("_");
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					String firstChar = String.valueOf(word.charAt(0)).toUpperCase(Locale.ROOT);
					StringBuilder otherChars = new StringBuilder();
					for (int j = 1; j < word.length(); j++) {
						otherChars.append(word.charAt(j));
					}
					if (i > 0) displayName.append(" ");
					displayName.append(firstChar).append(otherChars);
				}
				if (minecraftBlock.getAsJsonObject("description").has("display_name")) displayName = new StringBuilder(minecraftBlock.getAsJsonObject("description").get("display_name").getAsString());
				String itemGroup = "building_blocks";
				if (minecraftBlock.getAsJsonObject("description").has("creative_tab")) itemGroup = minecraftBlock.getAsJsonObject("description").get("creative_tab").getAsString();
				String mapColor = "default";
				if (components.has("minecraft:map_color")) mapColor = components.get("minecraft:map_color").getAsString();
				JsonElement drops = new JsonArray();
				JsonObject drop = new JsonObject();
				drop.addProperty("id", identifier.toString());
				drops.getAsJsonArray().add(drop);
				if (components.has("minecraft:loot")) drops = components.get("minecraft:loot");
				int luminance = 0;
				if (components.has("minecraft:block_light_emission")) luminance = components.get("minecraft:block_light_emission").getAsInt();
				if (luminance > 15) luminance = 15;
				if (luminance < 0) luminance = 0;
				float hardness = 1f;
				if (components.has("minecraft:destroy_time")) hardness = components.get("minecraft:destroy_time").getAsFloat();
				float resistance = 10f;
				if (components.has("minecraft:explosion_resistance")) resistance = components.get("minecraft:explosion_resistance").getAsFloat();
				float slipperiness = 10f;
				if (components.has("minecraft:friction")) slipperiness = components.get("minecraft:friction").getAsFloat();
				String material = "stone";
				if (components.has("custom_blocks:material")) material = components.get("custom_blocks:material").getAsString();
				String base = "block";
				if (components.has("custom_blocks:base")) base = components.get("custom_blocks:base").getAsString();
				String textureNamespace = namespace;
				JsonObject textures = new JsonObject();
				if (components.has("custom_blocks:textures")) {
					if (components.getAsJsonObject("custom_blocks:textures").has("namespace"))
						textureNamespace = components.getAsJsonObject("custom_blocks:textures").get("namespace").getAsString();
					textures = components.getAsJsonObject("custom_blocks:textures");
					textures.remove("namespace");
				} else {
					textures.addProperty("all", id);
				}

				javaBlock.addProperty("namespace", namespace);
				javaBlock.addProperty("id", id);
				javaBlock.addProperty("display_name", displayName.toString());
				javaBlock.addProperty("item_group", itemGroup);
				javaBlock.addProperty("map_color", mapColor);
				javaBlock.add("drops", drops);
				javaBlock.addProperty("luminance", luminance);
				javaBlock.addProperty("texture_namespace", textureNamespace);
				javaBlock.addProperty("hardness", hardness);
				javaBlock.addProperty("resistance", resistance);
				javaBlock.addProperty("slipperiness", slipperiness);
				javaBlock.addProperty("material", material);
				javaBlock.addProperty("base", base);
				javaBlock.add("textures", textures);
				if (add(javaBlock) && CustomResourceCreator.generateBlockResources(javaBlock) && LanguageHandler.addBlockKey(javaBlock)) {
					CustomBlocksMod.LOGGER.info("Generated Block " + javaBlock.get("namespace").getAsString() + ":" + javaBlock.get("id").getAsString());
				} else {
					if (file != null) CustomBlocksMod.LOGGER.error("Failed to generate block " + file.getName() + "!");
					else CustomBlocksMod.LOGGER.error("Failed to generate block!");
				}
				return true;
			}
		}
		return false;
	}

	private static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return false;
	}
}
