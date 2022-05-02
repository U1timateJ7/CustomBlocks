package com.ulto.customblocks.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.event.Events;
import com.ulto.customblocks.util.JsonUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
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
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
        if (block.has("on_use")) Events.playBlockActionEvent(state, pos, world, Map.of("entity", player, "hand", hand, "blockhitresult", hit), block.getAsJsonObject("on_use"));
        if (block.has("stripped_block")) {
            if (player.getStackInHand(hand).getItem() instanceof AxeItem && state.getBlock() != null) {
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

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        int tickRate = 10;
        if (block.has("tick_rate")) tickRate = block.get("tick_rate").getAsInt();
        if (!randomTicks) world.createAndScheduleBlockTick(pos, this, tickRate);
        if (block.has("on_added")) Events.playBlockEvent(state, pos, world, Map.of("oldstate", oldState, "notify", notify), block.getAsJsonObject("on_added"));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        int tickRate = 10;
        if (block.has("tick_rate")) tickRate = block.get("tick_rate").getAsInt();
        if (!randomTicks) world.createAndScheduleBlockTick(pos, this, tickRate);
        if (block.has("on_tick")) Events.playBlockEvent(state, pos, world, Map.of("random", random), block.getAsJsonObject("on_tick"));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if (block.has("on_entity_collision")) Events.playBlockEvent(state, pos, world, Map.of("entity", entity), block.getAsJsonObject("on_entity_collision"));
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (block.has("on_broken") && world instanceof World) Events.playBlockEvent(state, pos, (World) world, Map.of(), block.getAsJsonObject("on_broken"));
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance);
        if (block.has("on_entity_landed")) Events.playBlockEvent(state, pos, world, Map.of("entity", entity, "falldistance", fallDistance), block.getAsJsonObject("on_entity_landed"));
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        super.afterBreak(world, player, pos, state, blockEntity, stack);
        if (block.has("on_broken_by_player")) {
            if (blockEntity != null) Events.playBlockEvent(state, pos, world, Map.of("entity", player, "blockentity", blockEntity, "itemstack", stack), block.getAsJsonObject("on_entity_landed"));
            else Events.playBlockEvent(state, pos, world, Map.of("entity", player, "itemstack", stack), block.getAsJsonObject("on_entity_landed"));
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        super.onDestroyedByExplosion(world, pos, explosion);
        if (block.has("on_exploded")) Events.playBlockEvent(world.getBlockState(pos), pos, world, Map.of("explosion", explosion), block.getAsJsonObject("on_exploded"));
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        if (block.has("on_entity_stepped")) Events.playBlockEvent(state, pos, world, Map.of("entity", entity), block.getAsJsonObject("on_entity_stepped"));
    }
}
