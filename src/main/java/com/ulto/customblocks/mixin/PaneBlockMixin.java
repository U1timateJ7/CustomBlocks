package com.ulto.customblocks.mixin;

import com.ulto.customblocks.block.CustomFallingWallBlock;
import com.ulto.customblocks.block.CustomWallBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PaneBlock.class)
public class PaneBlockMixin {
    @Inject(at = @At("RETURN"), method = "connectsTo", cancellable = true)
    private void isFence(BlockState state, boolean sideSolidFullSquare, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof CustomWallBlock) cir.setReturnValue(true);
        if (state.getBlock() instanceof CustomFallingWallBlock) cir.setReturnValue(true);
    }
}
