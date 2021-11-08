package com.ulto.customblocks.mixin;

import com.ulto.customblocks.block.CustomFallingFenceBlock;
import com.ulto.customblocks.block.CustomFenceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceBlock.class)
public class FenceBlockMixin {
    @Inject(at = @At("RETURN"), method = "isFence", cancellable = true)
    private void isFence(Block block, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof FenceBlock) cir.setReturnValue(true);
        if (block instanceof CustomFenceBlock) cir.setReturnValue(true);
        if (block instanceof CustomFallingFenceBlock) cir.setReturnValue(true);
    }
}
