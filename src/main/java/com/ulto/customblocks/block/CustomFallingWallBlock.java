package com.ulto.customblocks.block;

import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.LandingBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;


public class CustomFallingWallBlock extends CustomWallBlock implements LandingBlock {
    public CustomFallingWallBlock(Settings settings, JsonObject block) {
        super(settings, block);
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        world.createAndScheduleBlockTick(pos, this, this.getFallDelay());
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        world.createAndScheduleBlockTick(pos, this, this.getFallDelay());
        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if (canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= 0) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, pos, world.getBlockState(pos));
            this.configureFallingBlockEntity(fallingBlockEntity);
            world.spawnEntity(fallingBlockEntity);
        }
    }

    protected void configureFallingBlockEntity(FallingBlockEntity entity) {

    }

    protected int getFallDelay() {
        return 2;
    }

    public static boolean canFallThrough(BlockState state) {
        Material material = state.getMaterial();
        return state.isAir() || state.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }

    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity) {
    }

    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockPos = pos.down();
            if (canFallThrough(world.getBlockState(blockPos))) {
                double d = (double)pos.getX() + random.nextDouble();
                double e = (double)pos.getY() - 0.05D;
                double f = (double)pos.getZ() + random.nextDouble();
                world.addParticle(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, state), d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return -16777216;
    }
}
