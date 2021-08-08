package com.ulto.customblocks.block;

import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.List;

public class CustomHorizontalFacingBlock extends CustomBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static boolean fromPlayerFacing;

    public CustomHorizontalFacingBlock(Properties settings, boolean _fromPlayerFacing, List<JsonObject> drops, List<JsonObject> shape, JsonObject block) {
        super(settings, drops, shape, block);
        fromPlayerFacing = _fromPlayerFacing;
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (fromPlayerFacing) return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        else {
            if (context.getClickedFace() == Direction.UP || context.getClickedFace() == Direction.DOWN)
                return this.defaultBlockState().setValue(FACING, Direction.NORTH);
            return this.defaultBlockState().setValue(FACING, context.getClickedFace());
        }
    }
}
