package com.ulto.customblocks.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.event.Events;
import com.ulto.customblocks.util.JsonUtils;
import com.ulto.customblocks.util.MiscUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CustomBlock extends Block {
    List<JsonObject> shape = new ArrayList<>();
    JsonObject block;

    public CustomBlock(Settings settings, List<JsonObject> shapeIn, JsonObject blockIn) {
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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ActionResult result = super.onUse(state, world, pos, player, hand, hit);
        if (block.has("on_use")) return Events.playBlockActionEvent(state, pos, world, MiscUtils.mapOf("entity", player, "hand", hand, "blockhitresult", hit), block.getAsJsonObject("on_use"));
        return result;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        int tickRate = 10;
        if (block.has("tick_rate")) tickRate = block.get("tick_rate").getAsInt();
        if (!randomTicks) world.getBlockTickScheduler().schedule(pos, this, tickRate);
        if (block.has("on_added")) Events.playBlockEvent(state, pos, world, MiscUtils.mapOf("oldstate", oldState, "notify", notify), block.getAsJsonObject("on_added"));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        int tickRate = 10;
        if (block.has("tick_rate")) tickRate = block.get("tick_rate").getAsInt();
        if (!randomTicks) world.getBlockTickScheduler().schedule(pos, this, tickRate);
        if (block.has("on_tick")) Events.playBlockEvent(state, pos, world, MiscUtils.mapOf("random", random), block.getAsJsonObject("on_tick"));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (block.has("on_entity_collision")) Events.playBlockEvent(state, pos, world, MiscUtils.mapOf("entity", entity), block.getAsJsonObject("on_entity_collision"));
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (block.has("on_broken") && world instanceof World) Events.playBlockEvent(state, pos, (World) world, MiscUtils.mapOf(), block.getAsJsonObject("on_broken"));
    }

    @Override
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, pos, entity, fallDistance);
        if (block.has("on_entity_landed")) Events.playBlockEvent(world.getBlockState(pos), pos, world, MiscUtils.mapOf("entity", entity, "falldistance", fallDistance), block.getAsJsonObject("on_entity_landed"));
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (block.has("on_broken_by_player")) {
            if (blockEntity != null) Events.playBlockEvent(state, pos, world, MiscUtils.mapOf("entity", player, "blockentity", blockEntity, "itemstack", stack), block.getAsJsonObject("on_entity_landed"));
            else Events.playBlockEvent(state, pos, world, MiscUtils.mapOf("entity", player, "itemstack", stack), block.getAsJsonObject("on_entity_landed"));
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        super.onDestroyedByExplosion(world, pos, explosion);
        if (block.has("on_exploded")) Events.playBlockEvent(world.getBlockState(pos), pos, world, MiscUtils.mapOf("explosion", explosion), block.getAsJsonObject("on_exploded"));
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity) {
        super.onSteppedOn(world, pos, entity);
        if (block.has("on_entity_stepped")) Events.playBlockEvent(world.getBlockState(pos), pos, world, MiscUtils.mapOf("entity", entity), block.getAsJsonObject("on_entity_stepped"));
    }
}
