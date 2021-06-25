package com.ulto.customblocks.mixin;

import net.minecraft.resource.AbstractFileResourcePack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(AbstractFileResourcePack.class)
public class AbstractFileResourcePackMixin {
    @Shadow @Final
    private File base;

    @Inject(at = @At("RETURN"), method = "getName", cancellable = true)
    private void customName(CallbackInfoReturnable<String> cir) {
        if (base.getName().equals("custom_blocks")) cir.setReturnValue("Custom Block Resources");
    }
}
