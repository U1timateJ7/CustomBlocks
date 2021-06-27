package com.ulto.customblocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.block.*;
import com.ulto.customblocks.util.JsonUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;

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
			List<JsonObject> drops = new ArrayList<>();
			if (block.has("drops")) {
				JsonArray stacks = block.getAsJsonArray("drops");
				drops = JsonUtils.jsonArrayToJsonObjectList(stacks);
			}
			else {
				JsonObject thisBlock = new JsonObject();
				thisBlock.addProperty("id", namespace + ":" + id);
				drops.add(thisBlock);
			}
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
			boolean hasItem = true;
			if (block.has("has_item")) hasItem = block.get("has_item").getAsBoolean();
			int maxStackSize;
			if (block.has("max_stack_size")) maxStackSize = block.get("max_stack_size").getAsInt();
			else maxStackSize = 64;
			if (maxStackSize > 64) maxStackSize = 64;
			if (maxStackSize < 1) maxStackSize = 1;
			boolean fireproof;
			if (block.has("fireproof")) fireproof = block.get("fireproof").getAsBoolean();
			else fireproof = false;
			String _itemGroup;
			if (block.has("item_group")) _itemGroup = block.get("item_group").getAsString();
			else {
				switch (base) {
					case "wall":
					case "fence":
					case "pane":
						_itemGroup = "decorations";
						break;
					case "fence_gate":
					case "pressure_plate":
					case "button":
					case "trapdoor":
					case "door":
						_itemGroup = "redstone";
						break;
					default:
						_itemGroup = "building_blocks";
						break;
				}
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
			Tag<Item> efficientTool;
			ItemGroup itemGroup;
			switch (_material) {
				case "wood":
					material = Material.WOOD;
					break;
				case "dirt":
					material = Material.SOIL;
					break;
				case "metal":
					material = Material.METAL;
					break;
				case "leaves":
					material = Material.LEAVES;
					break;
				case "air":
					material = Material.AIR;
					break;
				case "glass":
					material = Material.GLASS;
					break;
				default:
					material = Material.STONE;
					break;
			}
			switch (_mapColor) {
				case "white":
					mapColor = MapColor.WHITE;
					break;
				case "orange":
					mapColor = MapColor.ORANGE;
					break;
				case "magenta":
					mapColor = MapColor.MAGENTA;
					break;
				case "light_blue":
					mapColor = MapColor.LIGHT_BLUE;
					break;
				case "yellow":
					mapColor = MapColor.YELLOW;
					break;
				case "lime":
					mapColor = MapColor.LIME;
					break;
				case "pink":
					mapColor = MapColor.PINK;
					break;
				case "gray":
				case "grey":
					mapColor = MapColor.GRAY;
					break;
				case "light_gray":
				case "light_grey":
					mapColor = MapColor.LIGHT_GRAY;
					break;
				case "cyan":
					mapColor = MapColor.CYAN;
					break;
				case "purple":
					mapColor = MapColor.PURPLE;
					break;
				case "blue":
					mapColor = MapColor.BLUE;
					break;
				case "brown":
					mapColor = MapColor.BROWN;
					break;
				case "green":
					mapColor = MapColor.GREEN;
					break;
				case "red":
					mapColor = MapColor.RED;
					break;
				case "black":
					mapColor = MapColor.BLACK;
					break;
				case "clear":
					mapColor = MapColor.CLEAR;
					break;
				case "bright_red":
					mapColor = MapColor.BRIGHT_RED;
					break;
				case "dark_green":
					mapColor = MapColor.DARK_GREEN;
					break;
				case "dark_red":
					mapColor = MapColor.DARK_RED;
					break;
				case "diamond_blue":
					mapColor = MapColor.DIAMOND_BLUE;
					break;
				case "dirt_brown":
					mapColor = MapColor.DIRT_BROWN;
					break;
				case "emerald_green":
					mapColor = MapColor.EMERALD_GREEN;
					break;
				case "gold":
					mapColor = MapColor.GOLD;
					break;
				case "iron_gray":
				case "iron_grey":
					mapColor = MapColor.IRON_GRAY;
					break;
				case "lapis_blue":
					mapColor = MapColor.LAPIS_BLUE;
					break;
				case "light_blue_gray":
				case "light_blue_grey":
					mapColor = MapColor.LIGHT_BLUE_GRAY;
					break;
				case "oak_tan":
					mapColor = MapColor.OAK_TAN;
					break;
				case "off_white":
					mapColor = MapColor.OFF_WHITE;
					break;
				case "pale_green":
					mapColor = MapColor.PALE_GREEN;
					break;
				case "pale_purple":
					mapColor = MapColor.PALE_PURPLE;
					break;
				case "pale_yellow":
					mapColor = MapColor.PALE_YELLOW;
					break;
				case "spruce_brown":
					mapColor = MapColor.SPRUCE_BROWN;
					break;
				case "stone_gray":
				case "stone_grey":
					mapColor = MapColor.STONE_GRAY;
					break;
				case "terracotta_white":
					mapColor = MapColor.TERRACOTTA_WHITE;
					break;
				case "terracotta_orange":
					mapColor = MapColor.TERRACOTTA_ORANGE;
					break;
				case "terracotta_magenta":
					mapColor = MapColor.TERRACOTTA_MAGENTA;
					break;
				case "terracotta_light_blue":
					mapColor = MapColor.TERRACOTTA_LIGHT_BLUE;
					break;
				case "terracotta_yellow":
					mapColor = MapColor.TERRACOTTA_YELLOW;
					break;
				case "terracotta_lime":
					mapColor = MapColor.TERRACOTTA_LIME;
					break;
				case "terracotta_pink":
					mapColor = MapColor.TERRACOTTA_PINK;
					break;
				case "terracotta_gray":
				case "terracotta_grey":
					mapColor = MapColor.TERRACOTTA_GRAY;
					break;
				case "terracotta_light_gray":
				case "terracotta_light_grey":
					mapColor = MapColor.TERRACOTTA_LIGHT_GRAY;
					break;
				case "terracotta_cyan":
					mapColor = MapColor.TERRACOTTA_CYAN;
					break;
				case "terracotta_purple":
					mapColor = MapColor.TERRACOTTA_PURPLE;
					break;
				case "terracotta_blue":
					mapColor = MapColor.TERRACOTTA_BLUE;
					break;
				case "terracotta_brown":
					mapColor = MapColor.TERRACOTTA_BROWN;
					break;
				case "terracotta_green":
					mapColor = MapColor.TERRACOTTA_GREEN;
					break;
				case "terracotta_red":
					mapColor = MapColor.TERRACOTTA_RED;
					break;
				case "terracotta_black":
					mapColor = MapColor.TERRACOTTA_BLACK;
					break;
				case "water_blue":
					mapColor = MapColor.WATER_BLUE;
					break;
				case "white_gray":
				case "white_grey":
					mapColor = MapColor.WHITE_GRAY;
					break;
				case "crimson_nylium":
					mapColor = MapColor.DULL_RED;
					break;
				case "crimson_stem":
					mapColor = MapColor.DULL_PINK;
					break;
				case "crimson_hyphae":
					mapColor = MapColor.DARK_CRIMSON;
					break;
				case "warped_nylium":
					mapColor = MapColor.TEAL;
					break;
				case "warped_stem":
					mapColor = MapColor.DARK_AQUA;
					break;
				case "warped_hyphae":
					mapColor = MapColor.DARK_DULL_PINK;
					break;
				case "warped_wart":
					mapColor = MapColor.BRIGHT_TEAL;
					break;
				default:
					mapColor = material.getColor();
					break;
			}
			switch (_sounds) {
				case "wood":
					sounds = BlockSoundGroup.WOOD;
					break;
				case "dirt":
				case "gravel":
					sounds = BlockSoundGroup.GRAVEL;
					break;
				case "grass":
					sounds = BlockSoundGroup.GRASS;
					break;
				case "metal":
					sounds = BlockSoundGroup.METAL;
					break;
				case "sand":
					sounds = BlockSoundGroup.SAND;
					break;
				case "glass":
					sounds = BlockSoundGroup.GLASS;
					break;
				case "lily_pad":
					sounds = BlockSoundGroup.LILY_PAD;
					break;
				case "wool":
					sounds = BlockSoundGroup.WOOL;
					break;
				case "snow":
					sounds = BlockSoundGroup.SNOW;
					break;
				case "ladder":
					sounds = BlockSoundGroup.LADDER;
					break;
				case "anvil":
					sounds = BlockSoundGroup.ANVIL;
					break;
				case "slime":
					sounds = BlockSoundGroup.SLIME;
					break;
				case "honey":
					sounds = BlockSoundGroup.HONEY;
					break;
				case "wet_grass":
					sounds = BlockSoundGroup.WET_GRASS;
					break;
				case "coral":
					sounds = BlockSoundGroup.CORAL;
					break;
				case "bamboo":
					sounds = BlockSoundGroup.BAMBOO;
					break;
				case "bamboo_sapling":
					sounds = BlockSoundGroup.BAMBOO_SAPLING;
					break;
				case "scaffolding":
					sounds = BlockSoundGroup.SCAFFOLDING;
					break;
				case "sweet_berry_bush":
					sounds = BlockSoundGroup.SWEET_BERRY_BUSH;
					break;
				case "crop":
					sounds = BlockSoundGroup.CROP;
					break;
				case "stem":
					sounds = BlockSoundGroup.STEM;
					break;
				case "vine":
					sounds = BlockSoundGroup.VINE;
					break;
				case "nether_wart":
					sounds = BlockSoundGroup.NETHER_WART;
					break;
				case "lantern":
					sounds = BlockSoundGroup.LANTERN;
					break;
				case "nether_stem":
					sounds = BlockSoundGroup.NETHER_STEM;
					break;
				case "nylium":
					sounds = BlockSoundGroup.NYLIUM;
					break;
				case "fungus":
					sounds = BlockSoundGroup.FUNGUS;
					break;
				case "roots":
					sounds = BlockSoundGroup.ROOTS;
					break;
				case "shroomlight":
					sounds = BlockSoundGroup.SHROOMLIGHT;
					break;
				case "weeping_vines":
					sounds = BlockSoundGroup.WEEPING_VINES;
					break;
				case "weeping_vines_low_pitch":
					sounds = BlockSoundGroup.WEEPING_VINES_LOW_PITCH;
					break;
				case "soul_sand":
					sounds = BlockSoundGroup.SOUL_SAND;
					break;
				case "soul_soil":
					sounds = BlockSoundGroup.SOUL_SOIL;
					break;
				case "basalt":
					sounds = BlockSoundGroup.BASALT;
					break;
				case "wart_block":
					sounds = BlockSoundGroup.WART_BLOCK;
					break;
				case "netherrack":
					sounds = BlockSoundGroup.NETHERRACK;
					break;
				case "nether_bricks":
					sounds = BlockSoundGroup.NETHER_BRICKS;
					break;
				case "nether_sprouts":
					sounds = BlockSoundGroup.NETHER_SPROUTS;
					break;
				case "nether_ore":
					sounds = BlockSoundGroup.NETHER_ORE;
					break;
				case "bone":
					sounds = BlockSoundGroup.BONE;
					break;
				case "netherite":
					sounds = BlockSoundGroup.NETHERITE;
					break;
				case "ancient_debris":
					sounds = BlockSoundGroup.ANCIENT_DEBRIS;
					break;
				case "lodestone":
					sounds = BlockSoundGroup.LODESTONE;
					break;
				case "chain":
					sounds = BlockSoundGroup.CHAIN;
					break;
				case "nether_gold_ore":
					sounds = BlockSoundGroup.NETHER_GOLD_ORE;
					break;
				case "gilded_blackstone":
					sounds = BlockSoundGroup.GILDED_BLACKSTONE;
					break;
				default:
					sounds = BlockSoundGroup.STONE;
					break;
			}
			switch (breakTool) {
				case "sword":
					efficientTool = FabricToolTags.SWORDS;
					break;
				case "axe":
					efficientTool = FabricToolTags.AXES;
					break;
				case "shovel":
					efficientTool = FabricToolTags.SHOVELS;
					break;
				case "hoe":
					efficientTool = FabricToolTags.HOES;
					break;
				case "shears":
					efficientTool = FabricToolTags.SHEARS;
					break;
				default:
					efficientTool = FabricToolTags.PICKAXES;
					break;
			}
			switch (_itemGroup) {
				case "decorations":
					itemGroup = ItemGroup.DECORATIONS;
					break;
				case "redstone":
					itemGroup = ItemGroup.REDSTONE;
					break;
				case "transportation":
					itemGroup = ItemGroup.TRANSPORTATION;
					break;
				case "misc":
					itemGroup = ItemGroup.MISC;
					break;
				case "food":
					itemGroup = ItemGroup.FOOD;
					break;
				case "tools":
					itemGroup = ItemGroup.TOOLS;
					break;
				case "combat":
					itemGroup = ItemGroup.COMBAT;
					break;
				case "brewing":
					itemGroup = ItemGroup.BREWING;
					break;
				case "none":
					itemGroup = null;
					break;
				default:
					itemGroup = ItemGroup.BUILDING_BLOCKS;
					break;
			}

			Block NEW_BLOCK;
			FabricBlockSettings blockSettings = FabricBlockSettings.of(material).strength((float) _hardness, (float) _resistance).slipperiness((float) _slipperiness).mapColor(mapColor).sounds(sounds).luminance(luminance).velocityMultiplier(speedFactor).jumpVelocityMultiplier(jumpFactor);
			if (!breakTool.equals("none")) {
				blockSettings.breakByTool(efficientTool, harvestLevel);
				if (requiresTool) blockSettings.requiresTool();
			}
			if (!renderType.equals("opaque")) blockSettings.nonOpaque().blockVision(BlockGenerator::never).suffocates(BlockGenerator::never).solidBlock(BlockGenerator::never);
			switch (base) {
				case "slab":
					if (hasGravity) {
						CustomFallingSlabBlock BLOCK = new CustomFallingSlabBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						SlabBlock BLOCK = new CustomSlabBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "stairs":
					if (hasGravity) {
						CustomFallingStairBlock BLOCK = new CustomFallingStairBlock(Blocks.STONE.getDefaultState(), blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						StairsBlock BLOCK = new CustomStairBlock(Blocks.STONE.getDefaultState(), blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "wall":
					if (hasGravity) {
						CustomFallingWallBlock BLOCK = new CustomFallingWallBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						WallBlock BLOCK = new CustomWallBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "fence":
					if (hasGravity) {
						CustomFallingFenceBlock BLOCK = new CustomFallingFenceBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						FenceBlock BLOCK = new CustomFenceBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "fence_gate":
					if (hasGravity) {
						CustomFallingFenceGateBlock BLOCK = new CustomFallingFenceGateBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						FenceGateBlock BLOCK = new CustomFenceGateBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "pane":
					if (hasGravity) {
						CustomFallingPaneBlock BLOCK = new CustomFallingPaneBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						PaneBlock BLOCK = new CustomPaneBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "pressure_plate":
					if (hasGravity) {
						CustomFallingPressurePlateBlock BLOCK = new CustomFallingPressurePlateBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						CustomPressurePlateBlock BLOCK = new CustomPressurePlateBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "button": {
						CustomButtonBlock BLOCK = new CustomButtonBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "trapdoor":
					if (hasGravity) {
						CustomFallingTrapdoorBlock BLOCK = new CustomFallingTrapdoorBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					} else {
						CustomTrapdoorBlock BLOCK = new CustomTrapdoorBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				case "door": {
						CustomDoorBlock BLOCK = new CustomDoorBlock(blockSettings, drops, block);
						Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
						NEW_BLOCK = BLOCK;
					}
					break;
				default:
					if (hasGravity) {
						switch (rotationType) {
							case "axis": {
								CustomFallingPillarBlock BLOCK = new CustomFallingPillarBlock(blockSettings, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							case "y_axis_player": {
								CustomFallingHorizontalFacingBlock BLOCK = new CustomFallingHorizontalFacingBlock(blockSettings, true, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							case "y_axis": {
								CustomFallingHorizontalFacingBlock BLOCK = new CustomFallingHorizontalFacingBlock(blockSettings, false, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							case "all_player": {
								CustomFallingFacingBlock BLOCK = new CustomFallingFacingBlock(blockSettings, true, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							case "all": {
								CustomFallingFacingBlock BLOCK = new CustomFallingFacingBlock(blockSettings, false, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							default: {
								FallingBlock BLOCK = new CustomFallingBlock(blockSettings, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
						}
					} else {
						switch (rotationType) {
							case "axis": {
								PillarBlock BLOCK = new CustomPillarBlock(blockSettings, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							case "y_axis_player": {
								Block BLOCK = new CustomHorizontalFacingBlock(blockSettings, true, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							case "y_axis": {
								Block BLOCK = new CustomHorizontalFacingBlock(blockSettings, false, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							case "all_player": {
								Block BLOCK = new CustomFacingBlock(blockSettings, true, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							case "all": {
								Block BLOCK = new CustomFacingBlock(blockSettings, false, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
							}
							default: {
								Block BLOCK = new CustomBlock(blockSettings, drops, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
								break;
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

	private static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return false;
	}
}
