package com.ulto.customblocks.block;

import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.List;

public class CustomFacingBlock extends CustomBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    protected static boolean fromPlayerFacing;

    public CustomFacingBlock(Properties settings, boolean _fromPlayerFacing, List<JsonObject> shape, JsonObject block) {
        super(settings, shape, block);
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
        if (fromPlayerFacing) return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
        else return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }
}
