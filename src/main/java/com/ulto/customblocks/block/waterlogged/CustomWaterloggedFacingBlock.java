package com.ulto.customblocks.block.waterlogged;

import com.google.gson.JsonObject;
import com.ulto.customblocks.block.CustomBlock;
import com.ulto.customblocks.block.CustomFacingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomWaterloggedFacingBlock extends CustomFacingBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public CustomWaterloggedFacingBlock(Settings settings, boolean _fromPlayerFacing, List<JsonObject> shapeIn, JsonObject blockIn) {
        super(settings, _fromPlayerFacing, shapeIn, blockIn);
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        boolean flag = context.getWorld().getFluidState(context.getBlockPos()).getFluid() == Fluids.WATER;
        if (fromPlayerFacing) return this.getDefaultState().with(FACING, context.getPlayerLookDirection().getOpposite()).with(WATERLOGGED, flag);
        else return this.getDefaultState().with(FACING, context.getSide()).with(WATERLOGGED, flag);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState facingState, WorldAccess world, BlockPos currentPos, BlockPos facingPos) {
	    if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, facing, facingState, world, currentPos, facingPos);
    }
}
