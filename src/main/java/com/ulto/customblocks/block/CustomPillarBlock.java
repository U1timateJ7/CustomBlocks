package com.ulto.customblocks.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.event.Events;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

public class CustomPillarBlock extends RotatedPillarBlock {
    List<JsonObject> shape = new ArrayList<>();
    JsonObject block;

    public CustomPillarBlock(Properties settings, List<JsonObject> shapeIn, JsonObject blockIn) {
        super(settings);
        shape.addAll(shapeIn);
        block = blockIn;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        if (block.has("redstone_power")) {
            return block.get("redstone_power").getAsInt() > 0;
        }
        return false;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        if (block.has("redstone_power")) {
            return block.get("redstone_power").getAsInt();
        }
        return 0;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape voxelShape = Shapes.empty();
        for (VoxelShape voxelShape1 : JsonUtils.jsonObjectListToVoxelShapeList(shape)) {
            voxelShape = Shapes.join(voxelShape, voxelShape1, BooleanOp.OR);
        }
        return voxelShape;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if (block.has("drops")) {
            if (block.get("drops").isJsonArray()) {
                List<ItemStack> dropsOriginal = super.getDrops(state, builder);
                if (!dropsOriginal.isEmpty())
                    return dropsOriginal;
                List<ItemStack> realDrops = new ArrayList<>();
                for (JsonElement item : block.getAsJsonArray("drops")) {
                    realDrops.add(JsonUtils.itemStackFromJsonObject((JsonObject) item));
                }
                return realDrops;
            } else if (block.get("drops").isJsonPrimitive()) {
                if (block.getAsJsonPrimitive("drops").isString()) {
                    ResourceLocation identifier = new ResourceLocation(block.get("drops").getAsString());
                    if (identifier.equals(BuiltInLootTables.EMPTY)) {
                        return Collections.emptyList();
                    } else {
                        LootContext lootContext = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK );
                        ServerLevel serverWorld = lootContext.getLevel();
                        LootTable lootTable = serverWorld.getServer().getLootTables().get(identifier);
                        return lootTable.getRandomItems(lootContext);
                    }
                }
            }
        }
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this));
    }

    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        if (block.has("render_type")) {
            if (!block.get("render_type").getAsString().equals("opaque")) {
                return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
            }
        }
        return super.skipRendering(state, stateFrom, direction);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = super.use(state, world, pos, player, hand, hit);
        if (block.has("on_use")) return Events.playBlockActionEvent(state, pos, world, Map.of("entity", player, "hand", hand, "blockhitresult", hit), block.getAsJsonObject("on_use"));
        return result;
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onPlace(state, world, pos, oldState, notify);
        int tickRate = 10;
        if (block.has("tick_rate")) tickRate = block.get("tick_rate").getAsInt();
        if (!isRandomlyTicking) world.scheduleTick(pos, this, tickRate);
        if (block.has("on_added")) Events.playBlockEvent(state, pos, world, Map.of("oldstate", oldState, "notify", notify), block.getAsJsonObject("on_added"));
    }

    @Override
    public void tick (BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.tick(state, world, pos, random);
        int tickRate = 10;
        if (block.has("tick_rate")) tickRate = block.get("tick_rate").getAsInt();
        if (!isRandomlyTicking) world.scheduleTick(pos, this, tickRate);
        if (block.has("on_tick")) Events.playBlockEvent(state, pos, world, Map.of("random", random), block.getAsJsonObject("on_tick"));
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        super.entityInside(state, world, pos, entity);
        if (block.has("on_entity_collision")) Events.playBlockEvent(state, pos, world, Map.of("entity", entity), block.getAsJsonObject("on_entity_collision"));
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState bs, boolean notify) {
        super.onRemove(state, world, pos, bs, notify);
        if (block.has("on_broken")) Events.playBlockEvent(state, pos, world, Map.of(), block.getAsJsonObject("on_broken"));
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(world, state, pos, entity, fallDistance);
        if (block.has("on_entity_landed")) Events.playBlockEvent(state, pos, world, Map.of("entity", entity, "falldistance", fallDistance), block.getAsJsonObject("on_entity_landed"));
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, world, pos, explosion);
        if (block.has("on_exploded")) Events.playBlockEvent(state, pos, world, Map.of("explosion", explosion), block.getAsJsonObject("on_exploded"));
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(world, pos, state, entity);
        if (block.has("on_entity_stepped")) Events.playBlockEvent(state, pos, world, Map.of("entity", entity), block.getAsJsonObject("on_entity_stepped"));
    }
}
