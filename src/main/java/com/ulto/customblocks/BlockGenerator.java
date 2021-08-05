package com.ulto.customblocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.block.*;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.StaticTagHelper;
import net.minecraft.tags.StaticTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

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
			MaterialColor mapColor;
			SoundType sounds;
			ToolType efficientTool;
			CreativeModeTab itemGroup;
			material = switch (_material) {
				case "wood" -> Material.WOOD;
				case "dirt" -> Material.DIRT;
				case "metal" -> Material.METAL;
				case "leaves" -> Material.LEAVES;
				case "air" -> Material.AIR;
				case "glass" -> Material.GLASS;
				default -> Material.STONE;
			};
			mapColor = switch (_mapColor) {
				case "white" -> MaterialColor.SNOW;
				case "orange" -> MaterialColor.COLOR_ORANGE;
				case "magenta" -> MaterialColor.COLOR_MAGENTA;
				case "light_blue" -> MaterialColor.COLOR_LIGHT_BLUE;
				case "yellow" -> MaterialColor.COLOR_YELLOW;
				case "lime" -> MaterialColor.COLOR_LIGHT_GREEN;
				case "pink" -> MaterialColor.COLOR_PINK;
				case "gray", "grey" -> MaterialColor.COLOR_GRAY;
				case "light_gray", "light_grey" -> MaterialColor.COLOR_LIGHT_GRAY;
				case "cyan" -> MaterialColor.COLOR_CYAN;
				case "purple" -> MaterialColor.COLOR_PURPLE;
				case "blue" -> MaterialColor.COLOR_BLUE;
				case "brown" -> MaterialColor.COLOR_BROWN;
				case "green" -> MaterialColor.COLOR_GREEN;
				case "red" -> MaterialColor.COLOR_RED;
				case "black" -> MaterialColor.COLOR_BLACK;
				case "clear" -> MaterialColor.NONE;
				case "bright_red" -> MaterialColor.FIRE;
				case "dark_green" -> MaterialColor.PLANT;
				case "dark_red" -> MaterialColor.NETHER;
				case "diamond_blue" -> MaterialColor.DIAMOND;
				case "dirt_brown" -> MaterialColor.DIRT;
				case "emerald_green" -> MaterialColor.EMERALD;
				case "gold" -> MaterialColor.GOLD;
				case "iron_gray", "iron_grey" -> MaterialColor.METAL;
				case "lapis_blue" -> MaterialColor.LAPIS;
				case "light_blue_gray", "light_blue_grey" -> MaterialColor.CLAY;
				case "oak_tan" -> MaterialColor.WOOD;
				case "off_white" -> MaterialColor.QUARTZ;
				case "pale_green" -> MaterialColor.GRASS;
				case "pale_purple" -> MaterialColor.ICE;
				case "pale_yellow" -> MaterialColor.SAND;
				case "spruce_brown" -> MaterialColor.PODZOL;
				case "stone_gray", "stone_grey" -> MaterialColor.STONE;
				case "terracotta_white" -> MaterialColor.TERRACOTTA_WHITE;
				case "terracotta_orange" -> MaterialColor.TERRACOTTA_ORANGE;
				case "terracotta_magenta" -> MaterialColor.TERRACOTTA_MAGENTA;
				case "terracotta_light_blue" -> MaterialColor.TERRACOTTA_LIGHT_BLUE;
				case "terracotta_yellow" -> MaterialColor.TERRACOTTA_YELLOW;
				case "terracotta_lime" -> MaterialColor.TERRACOTTA_LIGHT_GREEN;
				case "terracotta_pink" -> MaterialColor.TERRACOTTA_PINK;
				case "terracotta_gray", "terracotta_grey" -> MaterialColor.TERRACOTTA_GRAY;
				case "terracotta_light_gray", "terracotta_light_grey" -> MaterialColor.TERRACOTTA_LIGHT_GRAY;
				case "terracotta_cyan" -> MaterialColor.TERRACOTTA_CYAN;
				case "terracotta_purple" -> MaterialColor.TERRACOTTA_PURPLE;
				case "terracotta_blue" -> MaterialColor.TERRACOTTA_BLUE;
				case "terracotta_brown" -> MaterialColor.TERRACOTTA_BROWN;
				case "terracotta_green" -> MaterialColor.TERRACOTTA_GREEN;
				case "terracotta_red" -> MaterialColor.TERRACOTTA_RED;
				case "terracotta_black" -> MaterialColor.TERRACOTTA_BLACK;
				case "water_blue" -> MaterialColor.WATER;
				case "white_gray", "white_grey" -> MaterialColor.WOOL;
				case "dull_red" -> MaterialColor.CRIMSON_NYLIUM;
				case "dull_pink" -> MaterialColor.CRIMSON_STEM;
				case "dark_crimson" -> MaterialColor.CRIMSON_HYPHAE;
				case "teal" -> MaterialColor.WARPED_NYLIUM;
				case "dark_aqua" -> MaterialColor.WARPED_STEM;
				case "dark_dull_pink" -> MaterialColor.WARPED_HYPHAE;
				case "bright_teal" -> MaterialColor.WARPED_WART_BLOCK;
				case "deepslate_gray", "deepslate_grey" -> MaterialColor.DEEPSLATE;
				case "raw_iron_pink" -> MaterialColor.RAW_IRON;
				case "lichen_green" -> MaterialColor.GLOW_LICHEN;
				default -> material.getColor();
			};
			sounds = switch (_sounds) {
				case "wood" -> SoundType.WOOD;
				case "dirt", "gravel" -> SoundType.GRAVEL;
				case "grass" -> SoundType.GRASS;
				case "metal" -> SoundType.METAL;
				case "sand" -> SoundType.SAND;
				case "glass" -> SoundType.GLASS;
				case "lily_pad" -> SoundType.LILY_PAD;
				case "wool" -> SoundType.WOOL;
				case "snow" -> SoundType.SNOW;
				case "ladder" -> SoundType.LADDER;
				case "anvil" -> SoundType.ANVIL;
				case "slime" -> SoundType.SLIME_BLOCK;
				case "honey" -> SoundType.HONEY_BLOCK;
				case "wet_grass" -> SoundType.WET_GRASS;
				case "coral" -> SoundType.CORAL_BLOCK;
				case "bamboo" -> SoundType.BAMBOO;
				case "bamboo_sapling" -> SoundType.BAMBOO_SAPLING;
				case "scaffolding" -> SoundType.SCAFFOLDING;
				case "sweet_berry_bush" -> SoundType.SWEET_BERRY_BUSH;
				case "crop" -> SoundType.CROP;
				case "stem" -> SoundType.HARD_CROP;
				case "vine" -> SoundType.VINE;
				case "nether_wart" -> SoundType.NETHER_WART;
				case "lantern" -> SoundType.LANTERN;
				case "nether_stem" -> SoundType.STEM;
				case "nylium" -> SoundType.NYLIUM;
				case "fungus" -> SoundType.FUNGUS;
				case "roots" -> SoundType.ROOTS;
				case "shroomlight" -> SoundType.SHROOMLIGHT;
				case "weeping_vines", "weeping_vines_low_pitch" -> SoundType.WEEPING_VINES;
				case "soul_sand" -> SoundType.SOUL_SAND;
				case "soul_soil" -> SoundType.SOUL_SOIL;
				case "basalt" -> SoundType.BASALT;
				case "wart_block" -> SoundType.WART_BLOCK;
				case "netherrack" -> SoundType.NETHERRACK;
				case "nether_bricks" -> SoundType.NETHER_BRICKS;
				case "nether_sprouts" -> SoundType.NETHER_SPROUTS;
				case "nether_ore" -> SoundType.NETHER_ORE;
				case "bone" -> SoundType.BONE_BLOCK;
				case "netherite" -> SoundType.NETHERITE_BLOCK;
				case "ancient_debris" -> SoundType.ANCIENT_DEBRIS;
				case "lodestone" -> SoundType.LODESTONE;
				case "chain" -> SoundType.CHAIN;
				case "nether_gold_ore" -> SoundType.NETHER_GOLD_ORE;
				case "gilded_blackstone" -> SoundType.GILDED_BLACKSTONE;
				case "candle" -> SoundType.CANDLE;
				case "amethyst_block" -> SoundType.AMETHYST;
				case "amethyst_cluster" -> SoundType.AMETHYST_CLUSTER;
				case "small_amethyst_bud" -> SoundType.SMALL_AMETHYST_BUD;
				case "medium_amethyst_bud" -> SoundType.MEDIUM_AMETHYST_BUD;
				case "large_amethyst_bud" -> SoundType.LARGE_AMETHYST_BUD;
				case "tuff" -> SoundType.TUFF;
				case "calcite" -> SoundType.CALCITE;
				case "copper" -> SoundType.COPPER;
				case "powder_snow" -> SoundType.POWDER_SNOW;
				case "dripstone_block" -> SoundType.DRIPSTONE_BLOCK;
				case "pointed_dripstone" -> SoundType.POINTED_DRIPSTONE;
				case "sculk_sensor" -> SoundType.SCULK_SENSOR;
				case "glow_lichen" -> SoundType.GLOW_LICHEN;
				case "cave_vines" -> SoundType.CAVE_VINES;
				case "spore_blossom" -> SoundType.SPORE_BLOSSOM;
				case "azalea" -> SoundType.AZALEA;
				case "flowering_azalea" -> SoundType.FLOWERING_AZALEA;
				case "moss_carpet" -> SoundType.MOSS_CARPET;
				case "moss_block" -> SoundType.MOSS;
				case "big_dripleaf" -> SoundType.BIG_DRIPLEAF;
				case "small_dripleaf" -> SoundType.SMALL_DRIPLEAF;
				case "rooted_dirt" -> SoundType.ROOTED_DIRT;
				case "hanging_roots" -> SoundType.HANGING_ROOTS;
				case "azalea_leaves" -> SoundType.AZALEA_LEAVES;
				case "deepslate" -> SoundType.DEEPSLATE;
				case "deepslate_bricks" -> SoundType.DEEPSLATE_BRICKS;
				case "deepslate_tiles" -> SoundType.DEEPSLATE_TILES;
				case "polished_deepslate" -> SoundType.POLISHED_DEEPSLATE;
				default -> SoundType.STONE;
			};
			efficientTool = switch (breakTool) {
				case "sword" -> ToolType.get("sword");
				case "axe" -> ToolType.AXE;
				case "shovel" -> ToolType.SHOVEL;
				case "hoe" -> ToolType.HOE;
				case "shears" -> ToolType.get("shears");
				default -> ToolType.PICKAXE;
			};
			ResourceLocation __itemGroup = new ResourceLocation("default");
			if (_itemGroup.matches("a-z0-9/._-:")) __itemGroup = new ResourceLocation(_itemGroup);
			itemGroup = switch (_itemGroup) {
				case "decorations" -> CreativeModeTab.TAB_DECORATIONS;
				case "redstone" -> CreativeModeTab.TAB_REDSTONE;
				case "transportation" -> CreativeModeTab.TAB_TRANSPORTATION;
				case "misc" -> CreativeModeTab.TAB_MISC;
				case "food" -> CreativeModeTab.TAB_FOOD;
				case "tools" -> CreativeModeTab.TAB_TOOLS;
				case "combat" -> CreativeModeTab.TAB_COMBAT;
				case "brewing" -> CreativeModeTab.TAB_BREWING;
				case "none" -> null;
				default -> ItemGroupGenerator.customTabs.getOrDefault(__itemGroup, CreativeModeTab.TAB_BUILDING_BLOCKS);
			};

			Block NEW_BLOCK;
			final int finalLuminance = luminance;
			BlockBehaviour.Properties blockSettings = BlockBehaviour.Properties.of(material, mapColor).strength((float) _hardness, (float) _resistance).friction((float) _slipperiness).sound(sounds).lightLevel((state) -> finalLuminance).speedFactor(speedFactor).jumpFactor(jumpFactor);
			if (!breakTool.equals("none")) {
				blockSettings.harvestTool(efficientTool).harvestLevel(harvestLevel);
				if (requiresTool) blockSettings.requiresCorrectToolForDrops();
			}
			if (!renderType.equals("opaque")) blockSettings.noOcclusion().isViewBlocking(BlockGenerator::never).isSuffocating(BlockGenerator::never).isRedstoneConductor(BlockGenerator::never);
			ResourceLocation registryName = new ResourceLocation(namespace, id);
			switch (base) {
				case "slab":
					if (hasGravity) NEW_BLOCK = register(new CustomFallingSlabBlock(blockSettings, drops, block).setRegistryName(registryName));
					else NEW_BLOCK = register(new CustomSlabBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				case "stairs":
					if (hasGravity) NEW_BLOCK = register(new CustomFallingStairBlock(blockSettings, drops, block).setRegistryName(registryName));
					else NEW_BLOCK = register(new CustomStairBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				case "wall":
					if (hasGravity) NEW_BLOCK = register(new CustomFallingWallBlock(blockSettings, drops, block).setRegistryName(registryName));
					else NEW_BLOCK = register(new CustomWallBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				case "fence":
					if (hasGravity) NEW_BLOCK = register(new CustomFallingFenceBlock(blockSettings, drops, block).setRegistryName(registryName));
					else NEW_BLOCK = register(new CustomFenceBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				case "fence_gate":
					if (hasGravity) NEW_BLOCK = register(new CustomFallingFenceGateBlock(blockSettings, drops, block).setRegistryName(registryName));
					else NEW_BLOCK = register(new CustomFenceGateBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				case "pane":
					if (hasGravity) NEW_BLOCK = register(new CustomFallingPaneBlock(blockSettings, drops, block).setRegistryName(registryName));
					else NEW_BLOCK = register(new CustomPaneBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				case "pressure_plate":
					if (hasGravity) NEW_BLOCK = register(new CustomFallingPressurePlateBlock(blockSettings, drops, block).setRegistryName(registryName));
					else NEW_BLOCK = register(new CustomPressurePlateBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				case "button":
					NEW_BLOCK = register(new CustomButtonBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				case "trapdoor":
					if (hasGravity) NEW_BLOCK = register(new CustomFallingTrapdoorBlock(blockSettings, drops, block).setRegistryName(registryName));
					else NEW_BLOCK = register(new CustomTrapdoorBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				case "door":
					NEW_BLOCK = register(new CustomDoorBlock(blockSettings, drops, block).setRegistryName(registryName));
					break;
				default:
					if (hasGravity) {
						switch (rotationType) {
							case "axis" -> NEW_BLOCK = register(new CustomFallingPillarBlock(blockSettings, drops, shape, block).setRegistryName(registryName));
							case "y_axis_player" -> NEW_BLOCK = register(new CustomFallingHorizontalFacingBlock(blockSettings, true, drops, shape, block).setRegistryName(registryName));
							case "y_axis" -> NEW_BLOCK = register(new CustomFallingHorizontalFacingBlock(blockSettings, false, drops, shape, block).setRegistryName(registryName));
							case "all_player" -> NEW_BLOCK = register(new CustomFallingFacingBlock(blockSettings, true, drops, shape, block).setRegistryName(registryName));
							case "all" -> NEW_BLOCK = register(new CustomFallingFacingBlock(blockSettings, false, drops, shape, block).setRegistryName(registryName));
							default -> NEW_BLOCK = register(new CustomFallingBlock(blockSettings, drops, shape, block).setRegistryName(registryName));
						}
					} else {
						switch (rotationType) {
							case "axis" -> NEW_BLOCK = register(new CustomPillarBlock(blockSettings, drops, shape, block).setRegistryName(registryName));
							case "y_axis_player" -> NEW_BLOCK = register(new CustomHorizontalFacingBlock(blockSettings, true, drops, shape, block).setRegistryName(registryName));
							case "y_axis" -> NEW_BLOCK = register(new CustomHorizontalFacingBlock(blockSettings, false, drops, shape, block).setRegistryName(registryName));
							case "all_player" -> NEW_BLOCK = register(new CustomFacingBlock(blockSettings, true, drops, shape, block).setRegistryName(registryName));
							case "all" -> NEW_BLOCK = register(new CustomFacingBlock(blockSettings, false, drops, shape, block).setRegistryName(registryName));
							default -> NEW_BLOCK = register(new CustomBlock(blockSettings, drops, shape, block).setRegistryName(registryName));
						}
					}
					break;
			}
			if (hasItem) {
				Item.Properties itemSettings = new Item.Properties().tab(itemGroup).stacksTo(maxStackSize);
				if (fireproof) itemSettings.fireResistant();
				Item BLOCK_ITEM;
				if (base.equals("door")) BLOCK_ITEM = new DoubleHighBlockItem(NEW_BLOCK, itemSettings).setRegistryName(registryName);
				else BLOCK_ITEM = new BlockItem(NEW_BLOCK, itemSettings).setRegistryName(registryName);
				register(BLOCK_ITEM);
			}
			return true;
		}
		return false;
	}

	private static Block register(Block block) {
		ForgeRegistries.BLOCKS.register(block);
		return block;
	}

	private static Item register(Item item) {
		ForgeRegistries.ITEMS.register(item);
		return item;
	}

	private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return false;
	}
}
