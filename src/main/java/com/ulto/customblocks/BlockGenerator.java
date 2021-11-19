package com.ulto.customblocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.block.*;
import com.ulto.customblocks.block.waterlogged.*;
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
				default: material = Material.STONE;
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
				case "dull_red":
					mapColor = MapColor.field_25702;
					break;
				case "dull_pink":
					mapColor = MapColor.field_25703;
					break;
				case "dark_crimson":
					mapColor = MapColor.field_25704;
					break;
				case "teal":
					mapColor = MapColor.field_25705;
					break;
				case "dark_aqua":
					mapColor = MapColor.field_25706;
					break;
				case "dark_dull_pink":
					mapColor = MapColor.field_25707;
					break;
				case "bright_teal":
					mapColor = MapColor.field_25708;
					break;
				default:
					mapColor = material.getColor();
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
			FabricBlockSettings blockSettings = FabricBlockSettings.of(material).strength((float) _hardness, (float) _resistance).slipperiness((float) _slipperiness).materialColor(mapColor).sounds(sounds).luminance(luminance).velocityMultiplier(speedFactor).jumpVelocityMultiplier(jumpFactor);
			if (!breakTool.equals("none")) {
				blockSettings.breakByTool(efficientTool, harvestLevel);
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
				default:
					if (hasGravity) {
						switch (rotationType) {
							case "axis": {
								CustomPillarBlock BLOCK = waterloggable ? new CustomWaterloggedFallingPillarBlock(blockSettings, shape, block) : new CustomFallingPillarBlock(blockSettings, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							case "y_axis_player": {
								CustomHorizontalFacingBlock BLOCK = waterloggable ? new CustomWaterloggedFallingHorizontalFacingBlock(blockSettings, true, shape, block) : new CustomFallingHorizontalFacingBlock(blockSettings, true, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							case "y_axis": {
								CustomHorizontalFacingBlock BLOCK = waterloggable ? new CustomWaterloggedFallingHorizontalFacingBlock(blockSettings, false, shape, block) : new CustomFallingHorizontalFacingBlock(blockSettings, false, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							case "all_player": {
								CustomFacingBlock BLOCK = waterloggable ? new CustomWaterloggedFallingFacingBlock(blockSettings, true, shape, block) : new CustomFallingFacingBlock(blockSettings, true, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							case "all": {
								CustomFacingBlock BLOCK = waterloggable ? new CustomWaterloggedFallingFacingBlock(blockSettings, false, shape, block) : new CustomFallingFacingBlock(blockSettings, false, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							default: {
								Block BLOCK = waterloggable ? new CustomWaterloggedFallingBlock(blockSettings, shape, block) : new CustomFallingBlock(blockSettings, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
						}
					} else {
						switch (rotationType) {
							case "axis": {
								PillarBlock BLOCK = waterloggable ? new CustomWaterloggedPillarBlock(blockSettings, shape, block) : new CustomPillarBlock(blockSettings, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							case "y_axis_player": {
								Block BLOCK = waterloggable ? new CustomWaterloggedHorizontalFacingBlock(blockSettings, true, shape, block) : new CustomHorizontalFacingBlock(blockSettings, true, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							case "y_axis": {
								Block BLOCK = waterloggable ? new CustomWaterloggedHorizontalFacingBlock(blockSettings, false, shape, block) : new CustomHorizontalFacingBlock(blockSettings, false, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							case "all_player": {
								Block BLOCK = waterloggable ? new CustomWaterloggedFacingBlock(blockSettings, true, shape, block) : new CustomFacingBlock(blockSettings, true, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							case "all": {
								Block BLOCK = waterloggable ? new CustomWaterloggedFacingBlock(blockSettings, false, shape, block) : new CustomFacingBlock(blockSettings, false, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
							default: {
								Block BLOCK = waterloggable ? new CustomWaterloggedBlock(blockSettings, shape, block) : new CustomBlock(blockSettings, shape, block);
								Registry.register(Registry.BLOCK, new Identifier(namespace, id), BLOCK);
								NEW_BLOCK = BLOCK;
							}
							break;
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
