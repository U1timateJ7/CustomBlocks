package com.ulto.customblocks.block;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

import java.util.List;

public class CustomFacingBlock extends CustomBlock {
    public static final DirectionProperty FACING = FacingBlock.FACING;
    private static boolean fromPlayerFacing;

    public CustomFacingBlock(Settings settings, boolean _fromPlayerFacing, List<JsonObject> shape, JsonObject block) {
        super(settings, shape, block);
        fromPlayerFacing = _fromPlayerFacing;
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState rotate(BlockState state, BlockRotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        if (fromPlayerFacing) return this.getDefaultState().with(FACING, context.getPlayerLookDirection().getOpposite());
        else return this.getDefaultState().with(FACING, context.getSide());
    }
}
