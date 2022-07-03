package com.ulto.customblocks.block.waterlogged;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;

public class CustomWaterloggedFallingBlock extends CustomWaterloggedBlock implements Fallable {
    public CustomWaterloggedFallingBlock(Properties settings, List<JsonObject> shape, JsonObject block) {
        super(settings, shape, block);
    }

    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onPlace(state, world, pos, oldState, notify);
        world.scheduleTick(pos, this, this.getFallDelay());
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        world.scheduleTick(pos, this, this.getFallDelay());
        return super.updateShape(state, direction, newState, world, pos, posFrom);
    }

    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.tick(state, world, pos, random);
        if (isFree(world.getBlockState(pos.below())) && pos.getY() >= 0) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(world, pos, world.getBlockState(pos));
            this.falling(fallingBlockEntity);
            world.addFreshEntity(fallingBlockEntity);
        }
    }

    protected void falling(FallingBlockEntity entity) {

    }

    protected int getFallDelay() {
        return 2;
    }

    public static boolean isFree(BlockState state) {
        Material material = state.getMaterial();
        return state.isAir() || state.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }

    @OnlyIn(Dist.CLIENT)
    public void randomDisplayTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockPos = pos.below();
            if (isFree(world.getBlockState(blockPos))) {
                double d = (double)pos.getX() + random.nextDouble();
                double e = (double)pos.getY() - 0.05D;
                double f = (double)pos.getZ() + random.nextDouble();
                world.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), d, e, f, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
