package com.ulto.customblocks.util;

import com.ulto.customblocks.CustomBlocksMod;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;

import java.util.function.Consumer;

public class BlockUtils {
    public static String materialNameFromMaterial(Material material) {
        String name;
        if (material == Material.WOOD) name = "wood";
        else if (material == Material.SOIL) name = "dirt";
        else if (material == Material.METAL) name = "metal";
        else if (material == Material.LEAVES) name = "leaves";
        else if (material == Material.AIR) name = "air";
        else if (material == Material.GLASS) name = "glass";
        else name = "stone";
        return name;
    }

    public static String rotationTypeFromBlock(Block block) {
        String rotationType = "none";
        if (block.getDefaultState().contains(Properties.FACING)) rotationType = "all_player";
        if (block.getDefaultState().contains(Properties.HORIZONTAL_FACING)) rotationType = "y_axis_player";
        if (block.getDefaultState().contains(Properties.AXIS) || block.getDefaultState().contains(Properties.HORIZONTAL_AXIS)) rotationType = "axis";
        return rotationType;
    }

    public static String mapColorFromBlock(Block block) {
        String mapColor = "default";
        MapColor color = block.settings.mapColorProvider.apply(block.getDefaultState());
        if (color == MapColor.WHITE) mapColor = "white";
        if (color == MapColor.ORANGE) mapColor = "orange";
        if (color == MapColor.MAGENTA) mapColor = "magenta";
        if (color == MapColor.LIGHT_BLUE) mapColor = "light_blue";
        if (color == MapColor.YELLOW) mapColor = "yellow";
        if (color == MapColor.LIME) mapColor = "lime";
        if (color == MapColor.PINK) mapColor = "pink";
        if (color == MapColor.GRAY) mapColor = "gray";
        if (color == MapColor.LIGHT_GRAY) mapColor = "light_grey";
        if (color == MapColor.CYAN) mapColor = "cyan";
        if (color == MapColor.PURPLE) mapColor = "purple";
        if (color == MapColor.BLUE) mapColor = "blue";
        if (color == MapColor.BROWN) mapColor = "brown";
        if (color == MapColor.GREEN) mapColor = "green";
        if (color == MapColor.RED) mapColor = "red";
        if (color == MapColor.BLACK) mapColor = "black";
        if (color == MapColor.CLEAR) mapColor = "clear";
        if (color == MapColor.BRIGHT_RED) mapColor = "bright_red";
        if (color == MapColor.DARK_GREEN) mapColor = "dark_green";
        if (color == MapColor.DARK_RED) mapColor = "dark_red";
        if (color == MapColor.DIAMOND_BLUE) mapColor = "diamond_blue";
        if (color == MapColor.DIRT_BROWN) mapColor = "dirt_brown";
        if (color == MapColor.EMERALD_GREEN) mapColor = "emerald_green";
        if (color == MapColor.GOLD) mapColor = "gold";
        if (color == MapColor.IRON_GRAY) mapColor = "iron_gray";
        if (color == MapColor.LAPIS_BLUE) mapColor = "lapis_blue";
        if (color == MapColor.LIGHT_BLUE_GRAY) mapColor = "light_blue_gray";
        if (color == MapColor.OAK_TAN) mapColor = "oak_tan";
        if (color == MapColor.OFF_WHITE) mapColor = "off_white";
        if (color == MapColor.PALE_GREEN) mapColor = "pale_green";
        if (color == MapColor.PALE_PURPLE) mapColor = "pale_green";
        if (color == MapColor.PALE_YELLOW) mapColor = "pale_yellow";
        if (color == MapColor.SPRUCE_BROWN) mapColor = "spruce_brown";
        if (color == MapColor.STONE_GRAY) mapColor = "stone_gray";
        if (color == MapColor.TERRACOTTA_WHITE) mapColor = "terracotta_white";
        if (color == MapColor.TERRACOTTA_ORANGE) mapColor = "terracotta_orange";
        if (color == MapColor.TERRACOTTA_MAGENTA) mapColor = "terracotta_magenta";
        if (color == MapColor.TERRACOTTA_LIGHT_BLUE) mapColor = "terracotta_light_blue";
        if (color == MapColor.TERRACOTTA_YELLOW) mapColor = "terracotta_yellow";
        if (color == MapColor.TERRACOTTA_LIME) mapColor = "terracotta_lime";
        if (color == MapColor.TERRACOTTA_PINK) mapColor = "terracotta_pink";
        if (color == MapColor.TERRACOTTA_GRAY) mapColor = "terracotta_gray";
        if (color == MapColor.TERRACOTTA_LIGHT_GRAY) mapColor = "terracotta_light_grey";
        if (color == MapColor.TERRACOTTA_CYAN) mapColor = "terracotta_cyan";
        if (color == MapColor.TERRACOTTA_PURPLE) mapColor = "terracotta_purple";
        if (color == MapColor.TERRACOTTA_BLUE) mapColor = "terracotta_blue";
        if (color == MapColor.TERRACOTTA_BROWN) mapColor = "terracotta_brown";
        if (color == MapColor.TERRACOTTA_GREEN) mapColor = "terracotta_green";
        if (color == MapColor.TERRACOTTA_RED) mapColor = "terracotta_red";
        if (color == MapColor.TERRACOTTA_BLACK) mapColor = "terracotta_black";
        if (color == MapColor.WATER_BLUE) mapColor = "water_blue";
        if (color == MapColor.WHITE_GRAY) mapColor = "white_gray";
        if (color == MapColor.DULL_RED) mapColor = "dull_red";
        if (color == MapColor.DULL_PINK) mapColor = "dull_pink";
        if (color == MapColor.DARK_CRIMSON) mapColor = "dark_crimson";
        if (color == MapColor.TEAL) mapColor = "teal";
        if (color == MapColor.DARK_AQUA) mapColor = "dark_aqua";
        if (color == MapColor.DARK_DULL_PINK) mapColor = "dark_dull_pink";
        if (color == MapColor.BRIGHT_TEAL) mapColor = "bright_teal";
        if (color == MapColor.DEEPSLATE_GRAY) mapColor = "deepslate_gray";
        if (color == MapColor.RAW_IRON_PINK) mapColor = "raw_iron_pink";
        if (color == MapColor.LICHEN_GREEN) mapColor = "lichen_green";
        return mapColor;
    }

    public static String blockSoundGroupfromBlock(Block block) {
        String name = "stone";
        BlockSoundGroup sounds = block.settings.soundGroup;
        if (sounds == BlockSoundGroup.WOOD) name = "wood";
        if (sounds == BlockSoundGroup.GRAVEL) name = "dirt";
        if (sounds == BlockSoundGroup.GRASS) name = "grass";
        if (sounds == BlockSoundGroup.METAL) name = "metal";
        if (sounds == BlockSoundGroup.SAND) name = "sand";
        if (sounds == BlockSoundGroup.GLASS) name = "glass";
        if (sounds == BlockSoundGroup.LILY_PAD) name = "lily_pad";
        if (sounds == BlockSoundGroup.WOOL) name = "wool";
        if (sounds == BlockSoundGroup.SNOW) name = "snow";
        if (sounds == BlockSoundGroup.LADDER) name = "ladder";
        if (sounds == BlockSoundGroup.ANVIL) name = "anvil";
        if (sounds == BlockSoundGroup.SLIME) name = "slime";
        if (sounds == BlockSoundGroup.HONEY) name = "honey";
        if (sounds == BlockSoundGroup.WET_GRASS) name = "wet_grass";
        if (sounds == BlockSoundGroup.CORAL) name = "coral";
        if (sounds == BlockSoundGroup.BAMBOO) name = "bamboo";
        if (sounds == BlockSoundGroup.BAMBOO_SAPLING) name = "bamboo_sapling";
        if (sounds == BlockSoundGroup.SCAFFOLDING) name = "scaffolding";
        if (sounds == BlockSoundGroup.SWEET_BERRY_BUSH) name = "sweet_berry_bush";
        if (sounds == BlockSoundGroup.CROP) name = "crop";
        if (sounds == BlockSoundGroup.STEM) name = "stem";
        if (sounds == BlockSoundGroup.VINE) name = "vine";
        if (sounds == BlockSoundGroup.NETHER_WART) name = "nether_wart";
        if (sounds == BlockSoundGroup.LANTERN) name = "lantern";
        if (sounds == BlockSoundGroup.NETHER_STEM) name = "nether_stem";
        if (sounds == BlockSoundGroup.NYLIUM) name = "nylium";
        if (sounds == BlockSoundGroup.FUNGUS) name = "fungus";
        if (sounds == BlockSoundGroup.ROOTS) name = "roots";
        if (sounds == BlockSoundGroup.SHROOMLIGHT) name = "shroomlight";
        if (sounds == BlockSoundGroup.WEEPING_VINES) name = "weeping_vines";
        if (sounds == BlockSoundGroup.WEEPING_VINES_LOW_PITCH) name = "weeping_vines_low_pitch";
        if (sounds == BlockSoundGroup.SOUL_SAND) name = "soul_sand";
        if (sounds == BlockSoundGroup.SOUL_SOIL) name = "soul_soil";
        if (sounds == BlockSoundGroup.BASALT) name = "basalt";
        if (sounds == BlockSoundGroup.WART_BLOCK) name = "wart_block";
        if (sounds == BlockSoundGroup.NETHERRACK) name = "netherrack";
        if (sounds == BlockSoundGroup.NETHER_BRICKS) name = "nether_bricks";
        if (sounds == BlockSoundGroup.NETHER_SPROUTS) name = "nether_sprouts";
        if (sounds == BlockSoundGroup.NETHER_ORE) name = "nether_ore";
        if (sounds == BlockSoundGroup.BONE) name = "bone";
        if (sounds == BlockSoundGroup.NETHERITE) name = "netherite";
        if (sounds == BlockSoundGroup.ANCIENT_DEBRIS) name = "ancient_debris";
        if (sounds == BlockSoundGroup.LODESTONE) name = "lodestone";
        if (sounds == BlockSoundGroup.CHAIN) name = "chain";
        if (sounds == BlockSoundGroup.NETHER_GOLD_ORE) name = "nether_gold_ore";
        if (sounds == BlockSoundGroup.GILDED_BLACKSTONE) name = "gilded_blackstone";
        if (sounds == BlockSoundGroup.CANDLE) name = "candle";
        if (sounds == BlockSoundGroup.AMETHYST_BLOCK) name = "amethyst_block";
        if (sounds == BlockSoundGroup.AMETHYST_CLUSTER) name = "amethyst_cluster";
        if (sounds == BlockSoundGroup.SMALL_AMETHYST_BUD) name = "small_amethyst_bud";
        if (sounds == BlockSoundGroup.MEDIUM_AMETHYST_BUD) name = "medium_amethyst_bud";
        if (sounds == BlockSoundGroup.LARGE_AMETHYST_BUD) name = "large_amethyst_bud";
        if (sounds == BlockSoundGroup.TUFF) name = "tuff";
        if (sounds == BlockSoundGroup.CALCITE) name = "calcite";
        if (sounds == BlockSoundGroup.COPPER) name = "copper";
        if (sounds == BlockSoundGroup.POWDER_SNOW) name = "powder_snow";
        if (sounds == BlockSoundGroup.DRIPSTONE_BLOCK) name = "dripstone_block";
        if (sounds == BlockSoundGroup.POINTED_DRIPSTONE) name = "pointed_dripstone";
        if (sounds == BlockSoundGroup.SCULK_SENSOR) name = "sculk_sensor";
        if (sounds == BlockSoundGroup.GLOW_LICHEN) name = "glow_lichen";
        if (sounds == BlockSoundGroup.CAVE_VINES) name = "cave_vines";
        if (sounds == BlockSoundGroup.SPORE_BLOSSOM) name = "spore_blossom";
        if (sounds == BlockSoundGroup.AZALEA) name = "azalea";
        if (sounds == BlockSoundGroup.FLOWERING_AZALEA) name = "flowing_azalea";
        if (sounds == BlockSoundGroup.MOSS_CARPET) name = "moss_carpet";
        if (sounds == BlockSoundGroup.MOSS_BLOCK) name = "moss_block";
        if (sounds == BlockSoundGroup.BIG_DRIPLEAF) name = "big_dripleaf";
        if (sounds == BlockSoundGroup.SMALL_DRIPLEAF) name = "small_dripleaf";
        if (sounds == BlockSoundGroup.ROOTED_DIRT) name = "rooted_dirt";
        if (sounds == BlockSoundGroup.HANGING_ROOTS) name = "hanging_roots";
        if (sounds == BlockSoundGroup.AZALEA_LEAVES) name = "azalea_leaves";
        if (sounds == BlockSoundGroup.DEEPSLATE) name = "deepslate";
        if (sounds == BlockSoundGroup.DEEPSLATE_BRICKS) name = "deepslate_bricks";
        if (sounds == BlockSoundGroup.DEEPSLATE_TILES) name = "deepslate_tiles";
        if (sounds == BlockSoundGroup.POLISHED_DEEPSLATE) name = "polished_deepslate";
        return name;
    }

    public static String efficientToolFromBlock(Block block) {
        String tool = "none";
        if (block.getDefaultState().isIn(BlockTags.AXE_MINEABLE)) tool = "axe";
        if (block.getDefaultState().isIn(BlockTags.PICKAXE_MINEABLE)) tool = "pickaxe";
        if (block.getDefaultState().isIn(BlockTags.SHOVEL_MINEABLE)) tool = "shovel";
        if (block.getDefaultState().isIn(BlockTags.HOE_MINEABLE)) tool = "hoe";
        return tool;
    }

    public static String harvestLevelFromBlock(Block block) {
        String harvestLevel = "";
        if (block.getDefaultState().isIn(BlockTags.NEEDS_STONE_TOOL)) harvestLevel = "1";
        if (block.getDefaultState().isIn(BlockTags.NEEDS_IRON_TOOL)) harvestLevel = "2";
        if (block.getDefaultState().isIn(BlockTags.NEEDS_DIAMOND_TOOL)) harvestLevel = "3";
        CustomBlocksMod.LOGGER.info(block.getDefaultState().isIn(BlockTags.NEEDS_STONE_TOOL));
        CustomBlocksMod.LOGGER.info(block.getDefaultState().isIn(BlockTags.NEEDS_IRON_TOOL));
        CustomBlocksMod.LOGGER.info(block.getDefaultState().isIn(BlockTags.NEEDS_DIAMOND_TOOL));
        block.getRegistryEntry().streamTags().forEach(blockTagKey -> {
            CustomBlocksMod.LOGGER.info(blockTagKey.id());
        });
        return harvestLevel;
    }
}
