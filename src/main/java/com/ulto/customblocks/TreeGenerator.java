package com.ulto.customblocks;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.treedecorator.*;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TreeGenerator {
    private static final Map<Identifier, ConfiguredFeature<TreeFeatureConfig, ?>> trees = new HashMap<>();

    public static boolean add(JsonObject tree) {
        if (tree.has("namespace") && tree.has("id") && tree.has("trunk_block") && tree.has("leaves_block")) {
            Identifier id = new Identifier(tree.get("namespace").getAsString(), tree.get("id").getAsString());
            Block trunkBlock = Registry.BLOCK.get(new Identifier(tree.get("trunk_block").getAsString()));
            Block leavesBlock = Registry.BLOCK.get(new Identifier(tree.get("leaves_block").getAsString()));
            int baseHeight = 4;
            if (tree.has("base_height")) baseHeight = tree.get("base_height").getAsInt();
            int firstRandomHeight = 2;
            if (tree.has("first_random_height")) firstRandomHeight = tree.get("first_random_height").getAsInt();
            int secondRandomHeight = 0;
            if (tree.has("second_random_height")) secondRandomHeight = tree.get("second_random_height").getAsInt();
            int leavesRadius = 2;
            if (tree.has("leaves_radius")) leavesRadius = tree.get("leaves_radius").getAsInt();
            boolean vines = false;
            if (tree.has("vines")) vines = tree.get("vines").getAsBoolean();
            boolean bees = false;
            if (tree.has("bees")) bees = tree.get("bees").getAsBoolean();
            Block fruitBlock = null;
            if (tree.has("fruit_block")) fruitBlock = Registry.BLOCK.get(new Identifier(tree.get("fruit_block").getAsString()));
            List<RegistryKey<Biome>> generatesIn = Collections.emptyList();
            if (tree.has("generates_in")) generatesIn = JsonUtils.jsonArrayToBiomeRegistryKeyList(tree.getAsJsonArray("generates_in"));
            boolean hasSapling = false;
            if (tree.has("has_sapling")) hasSapling = tree.get("has_sapling").getAsBoolean();

            if (hasSapling) {
                Block SAPLING = new CustomSaplingBlock(new CustomSaplingGenerator(id), AbstractBlock.Settings.copy(Blocks.OAK_SAPLING));
                Registry.register(Registry.BLOCK, new Identifier(id.getNamespace(), getSaplingId(id.getPath())), SAPLING);
                Registry.register(Registry.ITEM, new Identifier(id.getNamespace(), getSaplingId(id.getPath())), new BlockItem(SAPLING, new Item.Settings().group(ItemGroup.DECORATIONS)));

                Block POTTED_SAPLING = new FlowerPotBlock(SAPLING, AbstractBlock.Settings.copy(Blocks.POTTED_OAK_SAPLING));
                Registry.register(Registry.BLOCK, new Identifier(id.getNamespace(), "potted_" + getSaplingId(id.getPath())), POTTED_SAPLING);
            }

            TreeFeatureConfig.Builder builder = new TreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(trunkBlock.getDefaultState()), // Trunk block provider
                    new SimpleBlockStateProvider(leavesBlock.getDefaultState()), // Foliage block provider
                    new BlobFoliagePlacer(UniformIntDistribution.of(leavesRadius), UniformIntDistribution.of(0), 3), // places leaves as a blob (radius, offset from trunk, height)
                    new StraightTrunkPlacer(baseHeight, firstRandomHeight, secondRandomHeight), // places a straight trunk
                    new TwoLayersFeatureSize(1, 0, 1) // The width of the tree at different layers; used to see how tall the tree can be without clipping into blocks
            ).ignoreVines();
            List<TreeDecorator> decorators = new ArrayList<>();
            if (vines) decorators.addAll(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeavesVineTreeDecorator.INSTANCE));
            if (fruitBlock != null) decorators.add(new FruitTreeDecorator(0.2F, fruitBlock));
            builder.decorators(decorators);

            ConfiguredFeature<?, ?> TREE = Feature.TREE.configure(builder.heightmap(Heightmap.Type.MOTION_BLOCKING).build()).spreadHorizontally().applyChance(3);
            trees.put(id, Feature.TREE.configure(builder.build()));
            RegistryKey<ConfiguredFeature<?, ?>> customTree = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, id);
            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, customTree.getValue(), TREE);
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(generatesIn), GenerationStep.Feature.VEGETAL_DECORATION, customTree);

            if (bees) {
                decorators.add(new BeehiveTreeDecorator(0.05F));
                builder.decorators(decorators);
                ConfiguredFeature<?, ?> TREE_BEES = Feature.TREE.configure(builder.heightmap(Heightmap.Type.MOTION_BLOCKING).build()).spreadHorizontally().applyChance(3);
                trees.put(new Identifier(id.getNamespace(), id.getPath() + "_bees"), Feature.TREE.configure(builder.build()));
                RegistryKey<ConfiguredFeature<?, ?>> customTreeBees = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(id.getNamespace(), id.getPath() + "_bees"));
                Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, customTreeBees.getValue(), TREE_BEES);
                BiomeModifications.addFeature(BiomeSelectors.includeByKey(generatesIn).and(BiomeSelectors.spawnsOneOf(EntityType.BEE)), GenerationStep.Feature.VEGETAL_DECORATION, customTreeBees);
            }

            return true;
        }
        return false;
    }

    public static String getSaplingId(String id) {
        String saplingId = id.replace("_tree", "_sapling");
        if (!saplingId.contains("_sapling")) saplingId += "_sapling";
        return saplingId;
    }

    public static class CustomSaplingBlock extends SaplingBlock {
        public CustomSaplingBlock(SaplingGenerator generator, Settings settings) {
            super(generator, settings);
        }
    }

    public static class CustomSaplingGenerator extends SaplingGenerator {
        private final Identifier feature;

        public CustomSaplingGenerator(Identifier feature) {
            this.feature = feature;
        }

        @Nullable
        @Override
        protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bees) {
            return bees ? trees.get(new Identifier(feature.getNamespace(), feature.getPath() + "_bees")) : trees.get(feature);
        }
    }

    private static class FruitTreeDecorator extends TreeDecorator {
        private final float probability;
        private final Block fruit;

        public FruitTreeDecorator(float probability, Block fruit) {
            this.probability = probability;
            this.fruit = fruit;
        }

        protected TreeDecoratorType<?> getType() {
            return TreeDecoratorType.COCOA;
        }

        public void generate(StructureWorldAccess world, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions, Set<BlockPos> placedStates, BlockBox box) {
            if (!(random.nextFloat() >= this.probability)) {
                int i = logPositions.get(0).getY();
                logPositions.stream().filter((pos) -> pos.getY() - i <= 2).forEach((pos) -> {
                    for (Direction direction : Direction.Type.HORIZONTAL) {
                        if (random.nextFloat() <= 0.25F) {
                            Direction direction2 = direction.getOpposite();
                            BlockPos blockPos = pos.add(direction2.getOffsetX(), 0, direction2.getOffsetZ());
                            if (Feature.isAir(world, blockPos)) {
                                BlockState blockState = fruit.getDefaultState().contains(Properties.FACING) ? fruit.getDefaultState().with(Properties.FACING, direction) : fruit.getDefaultState();
                                this.setBlockStateAndEncompassPosition(world, blockPos, blockState, placedStates, box);
                            }
                        }
                    }
                });
            }
        }
    }
}