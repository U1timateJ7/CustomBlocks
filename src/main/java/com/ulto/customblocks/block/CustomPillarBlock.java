package com.ulto.customblocks.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomPillarBlock extends PillarBlock {
    List<JsonObject> shape = new ArrayList<>();
    JsonObject block;

    public CustomPillarBlock(Settings settings, List<JsonObject> shapeIn, JsonObject blockIn) {
        super(settings);
        shape.addAll(shapeIn);
        block = blockIn;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        if (block.has("redstone_power")) {
            return block.get("redstone_power").getAsInt() > 0;
        }
        return false;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (block.has("redstone_power")) {
            return block.get("redstone_power").getAsInt();
        }
        return 0;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape voxelShape = VoxelShapes.empty();
        for (VoxelShape voxelShape1 : JsonUtils.jsonObjectListToVoxelShapeList(shape)) {
            voxelShape = VoxelShapes.union(voxelShape, voxelShape1);
        }
        return voxelShape;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        if (block.has("drops")) {
            if (block.get("drops").isJsonArray()) {
                List<ItemStack> dropsOriginal = super.getDroppedStacks(state, builder);
                if (!dropsOriginal.isEmpty())
                    return dropsOriginal;
                List<ItemStack> realDrops = new ArrayList<>();
                for (JsonElement item : block.getAsJsonArray("drops")) {
                    realDrops.add(JsonUtils.itemStackFromJsonObject((JsonObject) item));
                }
                return realDrops;
            } else if (block.get("drops").isJsonPrimitive()) {
                if (block.getAsJsonPrimitive("drops").isString()) {
                    Identifier identifier = new Identifier(block.get("drops").getAsString());
                    if (identifier.equals(LootTables.EMPTY)) {
                        return Collections.emptyList();
                    } else {
                        LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
                        ServerWorld serverWorld = lootContext.getWorld();
                        LootTable lootTable = serverWorld.getServer().getLootManager().getTable(identifier);
                        return lootTable.generateLoot(lootContext);
                    }
                }
            }
        }
        List<ItemStack> dropsOriginal = super.getDroppedStacks(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this));
    }

    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (block.has("render_type")) {
            if (!block.get("render_type").getAsString().equals("opaque")) {
                return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
            }
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (block.has("stripped_block")) {
            if (FabricToolTags.AXES.contains(player.getStackInHand(hand).getItem()) && state.getBlock() != null) {
                BlockState _bs = Registry.BLOCK.get(new Identifier(block.get("stripped_block").getAsString())).getDefaultState().with(PillarBlock.AXIS, world.getBlockState(pos).get(PillarBlock.AXIS));
                world.playSound(null, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isClient) {
                    world.setBlockState(pos, _bs, 3);
                    player.getStackInHand(hand).damage(1, player, (p) -> p.sendToolBreakStatus(hand));
                }
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }
}
