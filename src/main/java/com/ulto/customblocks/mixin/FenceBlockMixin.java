package com.ulto.customblocks.mixin;

import com.ulto.customblocks.block.CustomFallingFenceBlock;
import com.ulto.customblocks.block.CustomFenceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceBlock.class)
public class FenceBlockMixin {
    @Inject(at = @At("RETURN"), method = "canConnectToFence", cancellable = true)
    private void isFence(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof FenceBlock) cir.setReturnValue(true);
        if (state.getBlock() instanceof CustomFenceBlock) cir.setReturnValue(true);
        if (state.getBlock() instanceof CustomFallingFenceBlock) cir.setReturnValue(true);
    }
}
