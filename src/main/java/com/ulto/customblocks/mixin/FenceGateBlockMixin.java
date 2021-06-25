package com.ulto.customblocks.mixin;

import com.ulto.customblocks.block.CustomFallingWallBlock;
import com.ulto.customblocks.block.CustomWallBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceGateBlock.class)
public class FenceGateBlockMixin {
    @Inject(at = @At("RETURN"), method = "isWall", cancellable = true)
    private void isFence(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof CustomWallBlock) cir.setReturnValue(true);
        if (state.getBlock() instanceof CustomFallingWallBlock) cir.setReturnValue(true);
    }
}
