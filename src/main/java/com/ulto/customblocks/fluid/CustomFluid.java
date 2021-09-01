package com.ulto.customblocks.fluid;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class CustomFluid extends FlowableFluid {
    JsonObject fluid;

    public CustomFluid(JsonObject fluid) {
        this.fluid = fluid;
    }

    @Override
    public Fluid getStill() {
        return Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), fluid.get("id").getAsString()));
    }

    @Override
    public Fluid getFlowing() {
        return Registry.FLUID.get(new Identifier(fluid.get("namespace").getAsString(), "flowing_" + fluid.get("id").getAsString()));
    }

    @Override
    public Item getBucketItem() {
        return Registry.ITEM.get(new Identifier(fluid.get("namespace").getAsString(), fluid.get("id").getAsString() + "_bucket"));
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return Registry.BLOCK.get(new Identifier(fluid.get("namespace").getAsString(), fluid.get("id").getAsString())).getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == getStill() || fluid == getFlowing();
    }

    @Override
    protected boolean isInfinite() {
        return !fluid.has("infinite") || fluid.get("infinite").getAsBoolean();
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getFlowSpeed(WorldView worldView) {
        return fluid.has("flow_speed") ? fluid.get("flow_speed").getAsInt() : 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView worldView) {
        return fluid.has("level_decrease_per_block") ? fluid.get("level_decrease_per_block").getAsInt() : 1;
    }

    @Override
    public int getTickRate(WorldView worldView) {
        return fluid.has("tick_rate") ? fluid.get("tick_rate").getAsInt() : 5;
    }

    @Override
    protected float getBlastResistance() {
        return fluid.has("resistance") ? fluid.get("resistance").getAsFloat() : 100.0f;
    }

    public static class Flowing extends CustomFluid {
        public Flowing(JsonObject fluid) {
            super(fluid);
        }

        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends CustomFluid {
        public Still(JsonObject fluid) {
            super(fluid);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
