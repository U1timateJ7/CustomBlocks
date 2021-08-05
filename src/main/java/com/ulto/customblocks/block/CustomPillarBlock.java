package com.ulto.customblocks.block;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class CustomPillarBlock extends RotatedPillarBlock {
    List<JsonObject> drops = new ArrayList<>();
    List<JsonObject> shape = new ArrayList<>();
    JsonObject block;

    public CustomPillarBlock(Properties settings, List<JsonObject> dropsIn, List<JsonObject> shapeIn, JsonObject blockIn) {
        super(settings);
        drops.addAll(dropsIn);
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
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        List<ItemStack> realDrops = new ArrayList<>();
        for (JsonObject item : drops) {
            realDrops.add(JsonUtils.itemStackFromJsonObject(item));
        }
        return realDrops;
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
}
